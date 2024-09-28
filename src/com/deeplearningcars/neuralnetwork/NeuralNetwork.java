package com.deeplearningcars.neuralnetwork;

import java.util.ArrayList;

import com.deeplearningcars.game.entities.Cars;

public class NeuralNetwork {
	public Layer inputLayer = new Layer();
	public ArrayList<Layer> hiddenLayers = new ArrayList<>();
	public Layer outputLayer = new Layer();
	
	public int hiddenLayersCount;
	
	public int getWeightCount() {
    	int sum = 0;
    	for (int i = 0; i < hiddenLayersCount; i++) {
    		for(int j = 0; j < hiddenLayers.get(i).neuronsCount; j++) {
    			sum += hiddenLayers.get(i).neurons.get(j).ConnectionsCount;
    		}
    	}
    	for(int i = 0; i < outputLayer.neuronsCount; i++) {
    		sum += outputLayer.neurons.get(i).ConnectionsCount;
    	}
    	return sum;
    }
	
	public boolean[] getOutputs(double[] input) {
		double[] outputValues = new double[Cars.OUTPUT_COUNT];
		boolean[] output = new boolean[Cars.OUTPUT_COUNT];
		IA.copyToInput(this, input);
		IA.processOutput(this);
		IA.copyFromOutput(this, outputValues);
		
		for(int i = 0; i < outputValues.length; i++) {
			output[i] = outputValues[i] > 0;
		}
		return output;
	}
	
}
