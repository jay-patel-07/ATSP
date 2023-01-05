package metaheuristic;

import java.util.*;
import java.lang.Math;

public class SteepestAscentHillClimbing {
    private int lengthOfArray;
    private int[][] atsp;

    public void setATSP(int[][] atspArray) {
        this.atsp = atspArray;
    }

    public void setLength(int n) {
        this.lengthOfArray = n;
    }

    public int getLength() {
        return this.lengthOfArray;
    }

    public SteepestAscentHillClimbing() {
    }

    public static int getNum(ArrayList<Integer> v) {
        int n = v.size();
        int index = (int) (Math.random() * n);
        int num = v.get(index);
        v.set(index, v.get(n - 1));
        v.remove(n - 1);
        return num;
    }

    public Vector<Integer> generateRandom() {
        int n = this.lengthOfArray;
        ArrayList<Integer> v = new ArrayList<>(n);
        Vector<Integer> randomVector = new Vector<>();
        for (int i = 0; i < n; i++)
            v.add(i);
        while (v.size() > 0) {
            randomVector.add(getNum(v));
        }
        return randomVector;
    }

    public int getTotalDistance(Vector<Integer> arr) {
        int answer = 0, n = this.lengthOfArray;
        for (int i = 0; i < n; i++) {
            if (i != 0) {
                answer += this.atsp[arr.get(i - 1)][arr.get(i)];
            } else {
                answer += this.atsp[arr.get(n - 1)][arr.get(i)];
            }
        }
        return answer;
    }

    public Vector<Vector<Integer>> generateAllNeighbours(Vector<Integer> arr) {
        int n = this.lengthOfArray;
        Vector<Vector<Integer>> vectorOfAllNeighbours = new Vector<Vector<Integer>>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Vector<Integer> temp = new Vector(arr);
                Collections.swap(temp, i, j);
                vectorOfAllNeighbours.add(temp);
            }
        }
        return (vectorOfAllNeighbours);
    }

    public Vector<Integer> findBetterAnswer(Vector<Vector<Integer>> vectorOfAllNeighbours) {
        Vector<Integer> currentBest = vectorOfAllNeighbours.get(0);
        int bestCurrentDistance = getTotalDistance(currentBest);
        for (int i = 0; i < vectorOfAllNeighbours.size(); i++) {
            Vector<Integer> currentSolution = vectorOfAllNeighbours.get(i);
            int currentDistance = getTotalDistance(currentSolution);
            if (currentDistance < bestCurrentDistance) {
                currentBest = currentSolution;
            }
        }
        return currentBest;
    }

    public Vector<Integer> steepestAscentHillClimbingAlgorithm() {
        Vector<Integer> currentAnswer = generateRandom();
        int currentDistance = getTotalDistance(currentAnswer);
        Vector<Vector<Integer>> neighboursOfCurrentAnswer = generateAllNeighbours(currentAnswer);
        Vector<Integer> bestNeighbour = findBetterAnswer(neighboursOfCurrentAnswer);
        int bestNeighbourDistance = getTotalDistance(bestNeighbour);

        while (currentDistance > bestNeighbourDistance) {
            currentAnswer = bestNeighbour;
            currentDistance = getTotalDistance(currentAnswer);
            neighboursOfCurrentAnswer = generateAllNeighbours(currentAnswer);
            bestNeighbour = findBetterAnswer(neighboursOfCurrentAnswer);
            bestNeighbourDistance = getTotalDistance(bestNeighbour);
        }
        return currentAnswer;
    }

    public Vector<Integer> convert1DArrayToVector(int[] input) {
        Vector<Integer> ans = new Vector<>();
        for (int i = 0; i < input.length; i++) {
            ans.add(input[i]);
        }
        return ans;
    }

    public int[][] convert2DVectorToArray(Vector<Vector<Integer>> input) {
        int[][] answer = new int[input.size()][input.get(0).size()];
        for (int i = 0; i < input.size(); i++) {
            for (int j = 0; j < input.get(i).size(); j++) {
                answer[i][j] = input.get(i).get(j);
            }
        }
        return answer;
    }

    public void print1DArray(int[] inputArray) {
        for (int i = 0; i < inputArray.length; i++) {
            System.out.print(inputArray[i] + " ");
        }
        System.out.println("");
    }

    public void print2DArray(int[][] inputArray) {
        for (int i = 0; i < inputArray.length; i++) {
            for (int j = 0; j < inputArray[i].length; j++) {
                System.out.print(inputArray[i][j] + " ");
            }
            System.out.println("");
        }
    }

    public void print1DVector(Vector<Integer> inputVector) {
        for (int i = 0; i < inputVector.size(); i++) {
            System.out.print(inputVector.get(i) + " ");
        }
        System.out.println("");
    }

    public void print2DVector(Vector<Vector<Integer>> inputVector) {
        for (int i = 0; i < inputVector.size(); i++) {
            for (int j = 0; j < inputVector.get(i).size(); j++) {
                System.out.print(inputVector.get(i).get(j) + " ");
            }
            System.out.println("");
        }
    }

}
