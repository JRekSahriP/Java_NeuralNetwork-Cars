package com.deeplearningcars.neuralnetwork;

public class NeuralNetwork {
	public final static double BIAS = 1.0;
	
	private Layer inputLayer;
	private Layer[] hiddenLayers;
	private Layer outputLayer;
	
	private int weightCount;
	
	public NeuralNetwork(
			int hiddenLayersCount, int inputNeurons, 
			int hiddenNeurons, int outputNeurons) {
		inputNeurons += BIAS;
		hiddenNeurons += BIAS;
		
		inputLayer = new Layer(inputNeurons, 0);
		
		hiddenLayers = new Layer[hiddenLayersCount];
		hiddenLayers[0] = new Layer(hiddenNeurons, inputNeurons);
		
		for (int i = 1; i < hiddenLayersCount; i++) {
			hiddenLayers[i] = new Layer(hiddenNeurons, hiddenNeurons);
		}
		
		outputLayer = new Layer(outputNeurons, hiddenNeurons);		
		
		weightCount = 0;
		for (Layer layer : hiddenLayers) {
			weightCount += layer.getWeightCount();
		}
		weightCount += outputLayer.getWeightCount();

	}
	
	public int getWeightCount() {
		int sum = 0;
		
		for (Layer layer : hiddenLayers)
			sum += layer.getWeightCount();

		sum += outputLayer.getWeightCount();
		
		return sum;
	}
	
	public void copyVectorToLayers(double[] vector) {
		int offset = 0;
		
		for (Layer layer : hiddenLayers) {
			offset = layer.setWeightsFromVector(vector, offset);
		}
		
		outputLayer.setWeightsFromVector(vector, offset);
	}
	

	public double[] layersToVector() {
		double[] vector = new double[weightCount];
		int offset = 0;
		
		for (Layer layer : hiddenLayers) {
			offset = layer.setWeightsToVector(vector, offset);
		}
		
		outputLayer.setWeightsToVector(vector, offset);

		
		return vector;
	}
	
	
	public void copyToInput(double[] vector) {
		final Neuron[] neurons = inputLayer.getNeurons();
		for (int i = 0; i < inputLayer.getNeuronsCount() - BIAS; i++) {
			neurons[i].setOutput(vector[i]);
		}
	}
	
	public double[] getOutput() {
		final int len = outputLayer.getNeuronsCount();
		double[] output = new double[len];
		
		final Neuron[] neurons = outputLayer.getNeurons();
		for (int i = 0; i < len; i++) {
			output[i] = neurons[i].getOutput();
		}
		
		return output;
	}
	
	public void processOutput() {
		
		hiddenLayers[0].compute(inputLayer.getOutputs());

		for (int i = 1; i < hiddenLayers.length; i++) {
            hiddenLayers[i].compute(hiddenLayers[i - 1].getOutputs());
        }
		
		outputLayer.compute(hiddenLayers[hiddenLayers.length - 1].getOutputs());
    }
	
	
	public Layer getInputLayer() {
		return inputLayer;
	}
	
	public Layer[] getHiddenLayers() {
		return hiddenLayers;
	}
	
	public Layer getOutputLayer() {
		return outputLayer;
	}
		
	public int getHiddenLayersCount() {
		return hiddenLayers.length;
	}
	
}
