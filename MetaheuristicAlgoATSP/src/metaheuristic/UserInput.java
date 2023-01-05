package metaheuristic;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import com.github.sh0nk.matplotlib4j.NumpyUtils;
import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import metaheuristic.FirstAscentHillClimbing;
import metaheuristic.SteepestAscentHillClimbing;
import metaheuristic.SimulatedAnnealing;
import metaheuristic.GeneticAlgorithm;

public class UserInput extends ProcessData implements ActionListener {
	JTextField tf1, tf2, tf3;
	JCheckBox cb1, cb2, cb3, cb4;
	JButton b1, b2, b3;
	String fileName;
	int[][] nowTSP, ATSP;
	int tspLength;
	int atspLength;
	static int iterations = 5;
	int bestDistanceSteepestAscent;
	Vector<Integer> pathTravelledSteepestAscent = new Vector<>();
	int bestDistanceFirstAscent;
	Vector<Integer> pathTravelledFirstAscent = new Vector<>();;
	int bestDistanceGenetic;
	Vector<Integer> pathTravelledGenetic = new Vector<>();;
	int bestDistanceSimulatedAnnealing;
	Vector<Integer> pathTravelledSimulatedAnnealing = new Vector<>();;
	List<Double> forFirstAscentGraph = new ArrayList<>();
	List<Double> forSteepestAscentGraph = new ArrayList<>();
	List<Double> forSimulatedAnnealingGraph = new ArrayList<>();
	List<Double> forGeneticAlgorithmGraph = new ArrayList<>();
	List<Double> forXCoordinate = new ArrayList<>();
	List<Double> forGeneticAlgorithmGraph3Node = new ArrayList<>();

	public void setDefault() {
		iterations = 5;
		this.bestDistanceFirstAscent = Integer.MAX_VALUE;
		this.bestDistanceSimulatedAnnealing = Integer.MAX_VALUE;
		this.bestDistanceSteepestAscent = Integer.MAX_VALUE;
		this.bestDistanceGenetic = Integer.MAX_VALUE;
		this.pathTravelledSteepestAscent.clear();
		this.pathTravelledFirstAscent.clear();
		this.pathTravelledGenetic.clear();
		this.pathTravelledSimulatedAnnealing.clear();
		this.forFirstAscentGraph.clear();
		this.forSteepestAscentGraph.clear();
		this.forSimulatedAnnealingGraph.clear();
		this.forXCoordinate.clear();
	}

	public UserInput() {
		JFrame f = new JFrame("Asymmetric Travelling Salesman Problem");

		cb1 = new JCheckBox("First Ascent Hill Climbing Algorithm");
		cb1.setBounds(100, 100, 250, 30);

		cb2 = new JCheckBox("Steepest Ascent Hill Climbing Algorithm");
		cb2.setBounds(100, 150, 250, 30);

		cb3 = new JCheckBox("Simulated Annealing Algorithm");
		cb3.setBounds(100, 200, 250, 30);

		cb4 = new JCheckBox("Genetic Algorithm");
		cb4.setBounds(100, 250, 250, 30);

		f.add(cb1);
		f.add(cb2);
		f.add(cb3);
		f.add(cb4);

		b1 = new JButton("Select file");
		b1.setBounds(100, 300, 95, 30);
		b1.addActionListener(this);
		f.add(b1);

		b2 = new JButton("Run");
		b2.setBounds(200, 300, 95, 30);
		b2.addActionListener(this);
		f.add(b2);

		f.setSize(500, 500);
		f.setLayout(null);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {
		Vector<String> selectedAlgorithms = new Vector<>();
		Vector<Integer> selectedAlgo = new Vector<>();
		if (e.getSource() == b2) {
			setDefault();
			//this.fileName = "D://Master's Projects//AI Testing//Project//br17.atsp";
			createTSPAndATSP();

			if (cb1.isSelected()) {
				selectedAlgo.add(1);
				selectedAlgorithms.add("FirstAscentHillClimbing");
				callFirstAscentAlgorithm();
				printOutputData(this.bestDistanceFirstAscent, this.pathTravelledFirstAscent,
						"First Ascent Hill Climbing");
			}
			if (cb2.isSelected()) {
				selectedAlgo.add(2);
				selectedAlgorithms.add("SteepestAscentHillClimbing");
				callSteepestAscentAlgorithm();
				printOutputData(this.bestDistanceSteepestAscent, this.pathTravelledSteepestAscent,
						"Steepest Ascent Hill Climbing");
			}
			if (cb3.isSelected()) {
				selectedAlgo.add(3);
				selectedAlgorithms.add("SimulatedAnnealing");
				callSimulatedAnnealingAlgorithm();
				printOutputData(this.bestDistanceSimulatedAnnealing, this.pathTravelledSimulatedAnnealing,
						"Simulated Annealing");
			}
			if (cb4.isSelected()) {
				selectedAlgo.add(4);
				selectedAlgorithms.add("GeneticAlgorithm");
				callGeneticAlgorithm();
				// callGeneticAlgorithm3Node();
				printOutputData(this.bestDistanceGenetic, this.pathTravelledGenetic, "Genetic Search");
			}

			printOutputFrame(selectedAlgo);

		}

		if (e.getSource() == b1) {

			JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			jfc.setDialogTitle("File Manager");
			int returnValue = jfc.showDialog(null, "Select");
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				System.out.println(jfc.getSelectedFile().getPath());
				this.fileName = jfc.getSelectedFile().getPath();
			}
		}
		if (e.getSource() == b3) {
			if (cb1.isSelected()) {
				selectedAlgo.add(1);
			}
			if (cb2.isSelected()) {
				selectedAlgo.add(2);
			}
			if (cb3.isSelected()) {
				selectedAlgo.add(3);
			}
			if (cb4.isSelected()) {
				selectedAlgo.add(4);
			}
			createGraph(selectedAlgo);
		}
	}

	public void createTSPAndATSP() {
		ProcessData processDataObj = new ProcessData();
		processDataObj.setFileName(this.fileName);
		Vector<Vector<Integer>> input = processDataObj.processInputFile();
		this.nowTSP = processDataObj.transformVectorToArray(input);
		this.ATSP = processDataObj.transformATSPToTSP(input);
		this.tspLength = input.size();
		this.atspLength = input.size() * 2;
	}

	public void callSteepestAscentAlgorithm() {
		SteepestAscentHillClimbing steepestAscentHillClimbing = new SteepestAscentHillClimbing();
		steepestAscentHillClimbing.setLength(atspLength);
		steepestAscentHillClimbing.setATSP(ATSP);
		int counter = iterations;
		Vector<Integer> bestPath = new Vector<>();
		int bestSoFar = Integer.MAX_VALUE;
		int i = 0, flag = 0;
		while (counter > 0) {
			i++;
			Vector<Integer> bestSolutionSteepestAscentHillClimbing = steepestAscentHillClimbing
					.steepestAscentHillClimbingAlgorithm();
			int distanceSteepestAscent = steepestAscentHillClimbing
					.getTotalDistance(bestSolutionSteepestAscentHillClimbing);
			if (i > 0) {
				SteepestAscentHillClimbing steepestAscentHillClimbing2 = new SteepestAscentHillClimbing();
				steepestAscentHillClimbing2.setLength(tspLength);
				steepestAscentHillClimbing2.setATSP(nowTSP);
				Vector<Integer> bestSolutionSteepestAscentHillClimbing2 = steepestAscentHillClimbing2
						.steepestAscentHillClimbingAlgorithm();
				int distanceSteepestAscent2 = steepestAscentHillClimbing2
						.getTotalDistance(bestSolutionSteepestAscentHillClimbing2);
				if (distanceSteepestAscent2 < bestSoFar) {
					bestSoFar = distanceSteepestAscent2;
					bestPath = bestSolutionSteepestAscentHillClimbing;
				}
				counter--;
				this.forSteepestAscentGraph.add((double) (distanceSteepestAscent2));			
			}
			else if (distanceSteepestAscent < 10000000) {
				if (distanceSteepestAscent < bestSoFar) {
					bestSoFar = distanceSteepestAscent;
					bestPath = bestSolutionSteepestAscentHillClimbing;
				}
				counter--;
				this.forSteepestAscentGraph.add((double) (distanceSteepestAscent));
			}
		}
		this.bestDistanceSteepestAscent = bestSoFar;
		this.pathTravelledSteepestAscent = bestPath;
	}

	public void callFirstAscentAlgorithm() {
		FirstAscentHillClimbing firstAscentHillClimbing = new FirstAscentHillClimbing();
		firstAscentHillClimbing.setLength(atspLength);
		firstAscentHillClimbing.setATSP(ATSP);
		int counter = iterations;
		Vector<Integer> bestPath = new Vector<>();
		int bestSoFar = Integer.MAX_VALUE;
		int i = 0, flag = 0;
		while (counter > 0) {
			i++;
			Vector<Integer> bestSolutionFirstAscentHillClimbing = firstAscentHillClimbing
					.firstAscentHillClimbingAlgorithm();
			int distanceFirstAscent = firstAscentHillClimbing.getTotalDistance(bestSolutionFirstAscentHillClimbing);
			if (i > 0) {
				FirstAscentHillClimbing steepestAscentHillClimbing2 = new FirstAscentHillClimbing();
				steepestAscentHillClimbing2.setLength(tspLength);
				steepestAscentHillClimbing2.setATSP(nowTSP);
				Vector<Integer> bestSolutionSteepestAscentHillClimbing2 = steepestAscentHillClimbing2
						.firstAscentHillClimbingAlgorithm();
				int distanceSteepestAscent2 = steepestAscentHillClimbing2
						.getTotalDistance(bestSolutionSteepestAscentHillClimbing2);
				if (distanceSteepestAscent2 < bestSoFar) {
					bestSoFar = distanceSteepestAscent2;
					bestPath = bestSolutionFirstAscentHillClimbing;
				}
				counter--;
				this.forFirstAscentGraph.add((double) (distanceSteepestAscent2));
			}
			else if (distanceFirstAscent < 10000000) {
				if (distanceFirstAscent < bestSoFar) {
					bestSoFar = distanceFirstAscent;
					bestPath = bestSolutionFirstAscentHillClimbing;
				}
				counter--;
				this.forFirstAscentGraph.add((double) (distanceFirstAscent));
			}
		}
		this.bestDistanceFirstAscent = bestSoFar;
		this.pathTravelledFirstAscent = bestPath;
	}

	public void callSimulatedAnnealingAlgorithm() {
		SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing();
		simulatedAnnealing.setLength(atspLength);
		simulatedAnnealing.setATSP(ATSP);
		int counter = iterations;
		Vector<Integer> bestPath = new Vector<>();
		int bestSoFar = Integer.MAX_VALUE;
		int i = 0, flag = 0;
		while (counter > 0) {
			i++;
			Vector<Integer> bestSolutionSimulatedAnnealing = simulatedAnnealing.simulatedAnnealingAlgorithm();
			int distanceSimulatedAnnealing = simulatedAnnealing.getTotalDistance(bestSolutionSimulatedAnnealing);
			if (i > 0) {
				SimulatedAnnealing steepestAscentHillClimbing2 = new SimulatedAnnealing();
				steepestAscentHillClimbing2.setLength(tspLength);
				steepestAscentHillClimbing2.setATSP(nowTSP);
				Vector<Integer> bestSolutionSteepestAscentHillClimbing2 = steepestAscentHillClimbing2
						.simulatedAnnealingAlgorithm();
				int distanceSteepestAscent2 = steepestAscentHillClimbing2
						.getTotalDistance(bestSolutionSteepestAscentHillClimbing2);
				if (distanceSteepestAscent2 < bestSoFar) {
					bestSoFar = distanceSteepestAscent2;
					bestPath = bestSolutionSimulatedAnnealing;
				}
				counter--;
				this.forSimulatedAnnealingGraph.add((double) (distanceSteepestAscent2));
			}
			else if (flag == 1 || distanceSimulatedAnnealing < 10000000) {
				if (distanceSimulatedAnnealing < bestSoFar) {
					bestSoFar = distanceSimulatedAnnealing;
					bestPath = bestSolutionSimulatedAnnealing;
				}
				counter--;
				this.forSimulatedAnnealingGraph.add((double) (distanceSimulatedAnnealing));
				flag = 0;
			}
		}
		this.bestDistanceSimulatedAnnealing = bestSoFar;
		this.pathTravelledSimulatedAnnealing = bestPath;
	}

	public void callGeneticAlgorithm() {
		int counter = iterations;
		Vector<Integer> bestPath = new Vector<>();
		int bestSoFar = Integer.MAX_VALUE;
		int i = 0, flag = 0;
		while (counter > 0) {
			i++;
			GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(atspLength, "TOURNAMENT", ATSP, 0, 0);
			DataBean result = geneticAlgorithm.optimize();
			if (i > 0) {
				GeneticAlgorithm steepestAscentHillClimbing2 = new GeneticAlgorithm(tspLength, "TOURNAMENT", nowTSP, 0, 0);
				DataBean result2 = steepestAscentHillClimbing2.optimize();
				if (result.getFitness() < bestSoFar) {
					bestSoFar = result2.getFitness();
					bestPath = result.vectorGetGenome();
				}
				counter--;
				this.forGeneticAlgorithmGraph.add((double) (result2.getFitness()));
			}
			else if (result.getFitness() < 10000000) {
				if (result.getFitness() < bestSoFar) {
					bestSoFar = result.getFitness();
					bestPath = result.vectorGetGenome();
				}
				this.forGeneticAlgorithmGraph.add((double) (result.getFitness()));
				counter--;
			}
		}
		this.bestDistanceGenetic = bestSoFar;
		this.pathTravelledGenetic = bestPath;
	}

	public void callGeneticAlgorithm3Node() {
		int counter = iterations;
		Vector<Integer> bestPath = new Vector<>();
		int bestSoFar = Integer.MAX_VALUE;
		while (counter > 0) {
			GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(this.tspLength, "TOURNAMENT", this.nowTSP, 0, 0);
			DataBean result = geneticAlgorithm.optimize();
			if (result.getFitness() < 10000000) {
				if (result.getFitness() < bestSoFar) {
					bestSoFar = result.getFitness();
					bestPath = result.vectorGetGenome();
				}
				System.out.println(bestSoFar + " 3node");
				this.forGeneticAlgorithmGraph3Node.add((double) (result.getFitness()));
				counter--;
			}
		}
		this.bestDistanceGenetic = bestSoFar;
		this.pathTravelledGenetic = bestPath;
	}

	public void printOutputData(int distance, Vector<Integer> path, String algorithm) {
		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println(
				"Algorithm: " + algorithm + "\n" + "Distance travelled: " + distance + "\n" + "Path is: " + path);
		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("");
	}

	public void printOutputFrame(Vector<Integer> selectedAlgo) {
		JFrame f2 = new JFrame("Output");
		String outputText = "<html> ";
		for (int i = 0; i < selectedAlgo.size(); i++) {
			if (selectedAlgo.get(i) == 1) {
				outputText += "<br> ----------------------------------------------------------------------------------------------------";
				outputText += "<br> <b> Algorithm: First Ascent </b> <br> <b> Distance travelled: </b>";
				outputText += this.bestDistanceFirstAscent;
				outputText += "<br> Path is: ";
				outputText += this.pathTravelledFirstAscent;
				outputText += "<br> ---------------------------------------------------------------------------------------------------- <br>";
			} else if (selectedAlgo.get(i) == 2) {
				outputText += "<br> ----------------------------------------------------------------------------------------------------";
				outputText += "<br> <b> Algorithm: Steepest Ascent </b> <br> <b> Distance travelled: </b>";
				outputText += this.bestDistanceSteepestAscent;
				outputText += "<br> Path is: ";
				outputText += this.pathTravelledSteepestAscent;
				outputText += "<br> ---------------------------------------------------------------------------------------------------- <br>";
			} else if (selectedAlgo.get(i) == 3) {
				outputText += "<br> ----------------------------------------------------------------------------------------------------";
				outputText += "<br><b> Algorithm: Simulated Annealing </b> <br> <b> Distance travelled: </b>";
				outputText += this.bestDistanceSimulatedAnnealing;
				outputText += "<br> Path is: ";
				outputText += this.pathTravelledSimulatedAnnealing;
				outputText += "<br> ---------------------------------------------------------------------------------------------------- <br>";
			} else {
				outputText += "<br> ----------------------------------------------------------------------------------------------------";
				outputText += "<br> <b> Algorithm: Genetic Search </b> <br> <b> Distance travelled: </b>";
				outputText += this.bestDistanceGenetic;
				outputText += "<br> Path is: ";
				outputText += this.pathTravelledGenetic;
				outputText += "<br> ---------------------------------------------------------------------------------------------------- <br>";
			}
		}
		outputText += "</html>";
		JLabel l1;
		l1 = new JLabel(outputText);
		l1.setBounds(40, 0, 400, 438);

		b3 = new JButton("Graph");
		b3.setBounds(200, 450, 95, 30);
		b3.addActionListener(this);
		f2.add(b3);

		f2.add(l1);
		f2.setSize(500, 550);
		f2.setLayout(null);
		f2.setVisible(true);
	}

	public void createGraph(Vector<Integer> selectedAlgo) {
		List<Double> x = new ArrayList<>();
		List<Double> y = new ArrayList<>();
		Plot plt = Plot.create();
		for (int i = 0; i < iterations; i++) {
			this.forXCoordinate.add((double) i + 1);
		}
		for (int i = 0; i < selectedAlgo.size(); i++) {
			if (selectedAlgo.get(i) == 1) {
				y = this.forFirstAscentGraph;
				x = this.forXCoordinate;
				plt.plot().add(x, y, "o").label("First Ascent").linestyle("--");
			} else if (selectedAlgo.get(i) == 2) {
				y = this.forSteepestAscentGraph;
				x = this.forXCoordinate;
				plt.plot().add(x, y, "o").label("Steepest Ascent").linestyle("--");
			} else if (selectedAlgo.get(i) == 3) {
				y = this.forSimulatedAnnealingGraph;
				x = this.forXCoordinate;
				plt.plot().add(x, y, "o").label("Simulated Annealing").linestyle("--");
			} else if (selectedAlgo.get(i) == 4) {
				y = this.forGeneticAlgorithmGraph;
				x = this.forXCoordinate;
				plt.plot().add(x, y, "o").label("Genetic Search").linestyle("--");
			}
		}

		plt.xlabel("Iteration");
		plt.ylabel("Best distance");
		plt.title("Algorithm Comparison on 'x' Iterations");
		plt.legend();
		try {
			plt.show();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PythonExecutionException e) {
			e.printStackTrace();
		}
	}

	public void createGraph2Node3Node() {
		List<Double> x = new ArrayList<>();
		List<Double> y = new ArrayList<>();
		Plot plt = Plot.create();
		for (int i = 0; i < iterations; i++) {
			this.forXCoordinate.add((double) i + 1);
		}
		y = this.forGeneticAlgorithmGraph3Node;
		x = this.forXCoordinate;
		plt.plot().add(x, y, "o").label("3 Node").linestyle("--");

		y = this.forGeneticAlgorithmGraph;
		x = this.forXCoordinate;
		plt.plot().add(x, y, "o").label("2 Node").linestyle("--");

		plt.xlabel("Iteration");
		plt.ylabel("Best distance");
		plt.title("Transformation to apply Genetic Algorithm");
		plt.legend();
		try {
			plt.show();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PythonExecutionException e) {
			e.printStackTrace();
		}
	}

}
