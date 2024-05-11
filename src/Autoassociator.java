import java.util.Arrays;
import java.util.Random;

public class Autoassociator {
	private int weights[][];
	private int trainingCapacity;

	public Autoassociator(CourseArray courses) {
		this.weights = new int[courses.length() - 1][courses.length() - 1];
		this.trainingCapacity = courses.length() - 1;
	}

	public int getTrainingCapacity() {
		// TO DO
		//????
		return 0;
	}

	public void training(int pattern[]) {
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[i].length; j++) {
				weights[i][j] += pattern[i] * pattern[j];
			}
		}
	}

	public int unitUpdate(int neurons[]) {
		Random random = new Random();
		int index = random.nextInt(neurons.length);
		int sum = 0;
		for (int i = 0; i < weights[index].length; i++) {
			sum += weights[index][i] * neurons[i];
		}
		neurons[index] = (sum >= 0) ? 1 : -1;

		return index;
	}

	public void unitUpdate(int neurons[], int index) {
		int sum = 0;
		for (int i = 0; i < weights[index].length; i++) {
			sum += weights[index][i] * neurons[i];
		}
		neurons[index] = (sum >= 0) ? 1 : -1;
	}

	public void chainUpdate(int neurons[], int steps) {
		for (int i = 0; i < steps; i++) {
			unitUpdate(neurons);
		}
		//??
	}

	public void fullUpdate(int neurons[]) {
		boolean Static = false;
		int newNeuronValue;
		while(!Static){
			Static = true;
			for (int i = 0; i < neurons.length; i++) {
				int sum = 0;
				for (int j = 0; j < weights[i].length; j++) {
					sum += weights[i][j] * neurons[j];
				}
				newNeuronValue = (sum >= 0) ? 1 : -1;
				if(neurons[i] != newNeuronValue){
					Static = false;
					neurons[i] = newNeuronValue;
				}
			}

		}
	}
}
