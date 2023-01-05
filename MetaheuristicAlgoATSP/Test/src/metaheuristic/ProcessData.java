package metaheuristic;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ProcessData {

    private String fileName;

    public ProcessData() {
        this.fileName = "No File";
    }

    public ProcessData(String fileName) {
        this.fileName = fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public Vector<Vector<Integer>> processInputFile() {
        Vector<Vector<Integer>> atsp = new Vector<Vector<Integer>>();
        try {
            int nowStart = 0, dimensionLength = 0;
            File myObj = new File(this.fileName);
            Scanner myReader = new Scanner(myObj);
            Vector<Integer> tempVector = new Vector<>();
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (nowStart == 0 && data.contains("DIMENSION")) {
                    String[] arrayDimension = data.split(" ");
                    dimensionLength = Integer.parseInt(arrayDimension[arrayDimension.length -1]);
                    continue;
                }
                if (data.equals("EDGE_WEIGHT_SECTION")) {
                    nowStart = 1;
                    continue;
                }
                if (nowStart == 1 && !data.equals("EOF")) {
                    String temp = data;
                    String[] dataArray = temp.split(" ");

                    for (int j = 0; j < dataArray.length; j++) {
                        if (dataArray[j].length() > 0) {
                            tempVector.add(Integer.parseInt(dataArray[j]));
                            if (tempVector.size() == dimensionLength) {
                                atsp.add((Vector) tempVector.clone());
                                tempVector.clear();
                            }
                        }
                    }
                }
            }
            myReader.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return atsp;
    }

    public int[][] transformATSPToTSP(Vector<Vector<Integer>> toBeTransformed) {
        int currentLength = toBeTransformed.size(), newLength = currentLength * 2;
        int[][] atsp = new int[newLength][newLength];

        for (int i = 0; i < newLength; i++) {
            for (int j = 0; j < newLength; j++) {
                atsp[i][j] = -1;
                if (i == j && i < currentLength) {
                    atsp[i][j] = toBeTransformed.get(i).get(j);
                }
            }
        }
        for (int i = 0; i < currentLength; i++) {
            atsp[i][i + currentLength] = 0;
            atsp[i + currentLength][i] = 0;
        }
        for (int i = 0; i < currentLength; i++) {
            for (int j = 0; j < currentLength; j++) {
                if (atsp[i + currentLength][j] == -1) {
                    atsp[i + currentLength][j] = toBeTransformed.get(i).get(j);
                    atsp[j][i + currentLength] = toBeTransformed.get(i).get(j);
                }
            }
        }
        for (int i = 0; i < newLength; i++) {
            for (int j = 0; j < newLength; j++) {
                if (atsp[i][j] == -1) {
                    atsp[i][j] = 10000000;
                }
            }
        }
        return atsp;
    }
    public int[][] transformATSPToTSP3Node(Vector<Vector<Integer>> toBeTransformed) {
        int currentLength = toBeTransformed.size(), newLength = currentLength * 3;
        int[][] atsp = new int[newLength][newLength];
        for (int i = 0; i < newLength; i++) {
            for (int j = 0; j < newLength; j++) {
                atsp[i][j] = -1;
                if (i == j && i < currentLength) {
                    atsp[i][j] = toBeTransformed.get(i).get(j);
                }
            }
        }
        for (int i = 0; i < currentLength; i++) {
            atsp[i][i + currentLength] = 0;
            atsp[i + currentLength][i] = 0;

            atsp[i][i + currentLength * 2] = 0;
            atsp[i + currentLength * 2][i] = 0;

            atsp[i + currentLength][i + currentLength * 2] = 0;
            atsp[i + currentLength * 2][i + currentLength] = 0;

        }
        for (int i = 0; i < currentLength; i++) {
            for (int j = 0; j < currentLength; j++) {
                if (atsp[i + currentLength * 2][j] == -1) {
                    atsp[i + currentLength * 2][j] = toBeTransformed.get(i).get(j);
                    atsp[j][i + currentLength * 2] = toBeTransformed.get(i).get(j);
                }
            }
        }
        for (int i = 0; i < newLength; i++) {
            for (int j = 0; j < newLength; j++) {
                if (atsp[i][j] == -1) {
                    atsp[i][j] = 10000000;
                }
            }
        }
        return atsp;
    }

    public int[][] transformVectorToArray(Vector<Vector<Integer>> toBeTransformed) {
        int currentLength = toBeTransformed.size();
        int[][] atsp = new int[currentLength][currentLength];

        for (int i = 0; i < currentLength; i++) {
            for (int j = 0; j < currentLength; j++) {
                atsp[i][j] = toBeTransformed.get(i).get(j);
            }
        }
        return atsp;
    }

    public void printArray(int[][] inputArray) {
        for (int i = 0; i < inputArray.length; i++) {
            for (int j = 0; j < inputArray.length; j++) {
                System.out.print(inputArray[i][j] + " ");
            }
            System.out.println("");
        }
    }

}
