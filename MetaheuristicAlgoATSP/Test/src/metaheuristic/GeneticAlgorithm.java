package metaheuristic;

import java.util.*;

public class GeneticAlgorithm {
    private int generationSize;
    private int genomeSize;
    private int numberOfCities;
    private int reproductionSize;
    private int maxIterations;
    private float mutationRate;
    private int tournamentSize;
    private String selectionType;
    private int[][] travelPrices;
    private int startingCity;
    private int targetFitness;

    public GeneticAlgorithm(int numberOfCities, String selectionType, int[][] travelPrices, int startingCity,
            int targetFitness) {
        this.numberOfCities = numberOfCities;
        this.genomeSize = numberOfCities - 1;
        this.selectionType = selectionType;
        this.travelPrices = travelPrices;
        this.startingCity = startingCity;
        this.targetFitness = targetFitness;

        generationSize = 5000;
        reproductionSize = 200;
        maxIterations = 1000;
        mutationRate = 0.1f;
        tournamentSize = 40;
    }

    public List<DataBean> initialPopulation() {
        List<DataBean> population = new ArrayList<>();
        for (int i = 0; i < generationSize; i++) {
            population.add(new DataBean(numberOfCities, travelPrices, startingCity));
        }
        return population;
    }

    public List<DataBean> selection(List<DataBean> population) {
        List<DataBean> selected = new ArrayList<>();
        DataBean winner;
        for (int i = 0; i < reproductionSize; i++) {
            selected.add(tournamentSelection(population));
        }

        return selected;
    }

    public static <E> List<E> pickNRandomElements(List<E> list, int n) {
        Random r = new Random();
        int length = list.size();

        if (length < n)
            return null;

        for (int i = length - 1; i >= length - n; --i) {
            Collections.swap(list, i, r.nextInt(i + 1));
        }
        return list.subList(length - n, length);
    }

    public DataBean tournamentSelection(List<DataBean> population) {
        List<DataBean> selected = pickNRandomElements(population, tournamentSize);
        return Collections.min(selected);
    }

    public DataBean mutate(DataBean salesman) {
        Random random = new Random();
        float mutate = random.nextFloat();
        if (mutate < mutationRate) {
            List<Integer> genome = salesman.getGenome();
            Collections.swap(genome, random.nextInt(genomeSize), random.nextInt(genomeSize));
            return new DataBean(genome, numberOfCities, travelPrices, startingCity);
        }
        return salesman;
    }

    public List<DataBean> createGeneration(List<DataBean> population) {
        List<DataBean> generation = new ArrayList<>();
        int currentGenerationSize = 0;
        while (currentGenerationSize < generationSize) {
            List<DataBean> parents = pickNRandomElements(population, 2);
            List<DataBean> children = crossover(parents);
            children.set(0, mutate(children.get(0)));
            children.set(1, mutate(children.get(1)));
            generation.addAll(children);
            currentGenerationSize += 2;
        }
        return generation;
    }

    public List<DataBean> crossover(List<DataBean> parents) {
        Random random = new Random();
        int breakpoint = random.nextInt(genomeSize);
        List<DataBean> children = new ArrayList<>();
        List<Integer> parent1Genome = new ArrayList<>(parents.get(0).getGenome());
        List<Integer> parent2Genome = new ArrayList<>(parents.get(1).getGenome());

        for (int i = 0; i < breakpoint; i++) {
            int newVal;
            newVal = parent2Genome.get(i);
            Collections.swap(parent1Genome, parent1Genome.indexOf(newVal), i);
        }
        children.add(new DataBean(parent1Genome, numberOfCities, travelPrices, startingCity));
        parent1Genome = parents.get(0).getGenome();

        for (int i = breakpoint; i < genomeSize; i++) {
            int newVal = parent1Genome.get(i);
            Collections.swap(parent2Genome, parent2Genome.indexOf(newVal), i);
        }
        children.add(new DataBean(parent2Genome, numberOfCities, travelPrices, startingCity));

        return children;
    }

    public DataBean optimize() {
        List<DataBean> population = initialPopulation();
        DataBean globalBestGenome = population.get(0);
        for (int i = 0; i < maxIterations; i++) {
            List<DataBean> selected = selection(population);
            population = createGeneration(selected);
            globalBestGenome = Collections.min(population);
            if (globalBestGenome.getFitness() < targetFitness)
                break;
        }
        return globalBestGenome;
    }

    public void printGeneration(List<DataBean> generation) {
        for (DataBean genome : generation) {
            System.out.println(genome);
        }
    }
}