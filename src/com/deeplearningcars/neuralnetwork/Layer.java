package com.deeplearningcars.neuralnetwork;

public class Layer {
	private Neuron[] neurons;
	
	
	public Layer(int neurons, int neuronsConnections) {
		this.neurons = new Neuron[neurons];
		for (int i = 0; i < neurons; i++) {
			this.neurons[i] = new Neuron(neuronsConnections);
		}
		
	}

	public void compute(double[] inputs) {
		int range = neurons.length; 
		if (NeuralNetwork.BIAS > 0)
			range -= 1;
		
		for (int i = 0; i < range; i++) {
			this.neurons[i].active(inputs);
		}
		
	}
	
	public double[] getOutputs() {
		double[] outputs = new double[neurons.length];
		for (int i = 0; i < neurons.length; i++) {
			outputs[i] = neurons[i].getOutput();
		}
		return outputs;
	}
	
	public int setWeightsFromVector(double[] vector, int startIndex) {
		int index = startIndex;
		for (Neuron neuron : neurons) {
			double[] weights = neuron.getWeights();
			for (int i = 0; i < weights.length; i++) {
				weights[i] = vector[index++];
			}
		}
		return index;
	}
	public int setWeightsToVector(double[] vector, int startIndex) {
		int index = startIndex;
		for (Neuron neuron : neurons) {
			double[] weights = neuron.getWeights();
			for (int i = 0; i < weights.length; i++) {
				vector[index++] = weights[i];
			}
		}
		return index;
	}
	
	public int getWeightCount() {
    	int sum = 0;
    	for (Neuron neuron : neurons) {
    		sum += neuron.getConnectionsCount();
    	}
    	return sum;
    }
	
	public Neuron[] getNeurons() {
		return neurons;
	}

	public int getNeuronsCount() {
		return neurons.length;
	}
	
}
