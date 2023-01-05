package metaheuristic;

import java.util.*;

public class DataBean implements Comparable {
    List<Integer> listOfCities;
    int[][] travelPrices;
    int startingCity;
    int numberOfCities = 0;
    int fitness;

    public DataBean(int numberOfCities, int[][] travelPrices, int startingCity){
        this.travelPrices = travelPrices;
        this.startingCity = startingCity;
        this.numberOfCities = numberOfCities;
        listOfCities = randomSalesman();
        fitness = this.calculateFitness();
    }

    public DataBean(List<Integer> permutationOfCities, int numberOfCities, int[][] travelPrices, int startingCity){
        listOfCities = permutationOfCities;
        this.travelPrices = travelPrices;
        this.startingCity = startingCity;
        this.numberOfCities = numberOfCities;
        fitness = this.calculateFitness();
    }

    public int calculateFitness(){
        int fitness = 0;
        int currentCity = startingCity;
        for ( int gene : listOfCities) {
            fitness += travelPrices[currentCity][gene];
            currentCity = gene;
        }
        fitness += travelPrices[listOfCities.get(numberOfCities-2)][startingCity];
        return fitness;
    }

    private List<Integer> randomSalesman(){
        List<Integer> result = new ArrayList<Integer>();
        for(int i=0; i<numberOfCities; i++) {
            if(i!=startingCity)
                result.add(i);
        }
        Collections.shuffle(result);
        return result;
    }

    public List<Integer> getGenome() {
        return listOfCities;
    }
    public Vector<Integer> vectorGetGenome()
    {
        Vector<Integer> vec = new Vector<>();
        List<Integer> temp = this.listOfCities;
        for(int i=0; i<temp.size(); i++)
        {
            vec.add(temp.get(i));
        }
        return vec;
    }

    public int getStartingCity() {
        return startingCity;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Path Travelled: ");
        sb.append(startingCity);
        for ( int gene: listOfCities ) {
            sb.append(" ");
            sb.append(gene);
        }
        sb.append(" ");
        sb.append(startingCity);
        sb.append("\nTotal Distance Travelled: ");
        sb.append(this.fitness);
        return sb.toString();
    }


    @Override
    public int compareTo(Object o) {
        DataBean listOfCities = (DataBean) o;
        if(this.fitness > listOfCities.getFitness())
            return 1;
        else if(this.fitness < listOfCities.getFitness())
            return -1;
        else
            return 0;
    }
}