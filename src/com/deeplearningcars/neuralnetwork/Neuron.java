package com.deeplearningcars.neuralnetwork;

public class Neuron {
	private final static double INITIAL_WEIGHTS = 1.0;
	
	private double[] weights;
	private double error;
	private double output;
	
	public Neuron(int connections) {
		this.weights = new double[connections];
		for (int i = 0; i < connections; i++) {
			double factor = (Math.random() * 2) - 1;
			this.weights[i] = factor / INITIAL_WEIGHTS; 
		}

		this.error = 0;
		this.output = 1;
	}

	public void active(double[] inputs) {
		double sum = 0;
		for (int i = 0; i < weights.length; i++) {
			sum += inputs[i] * weights[i];
		}
		this.output = reLU(sum);
	}
	
	private double reLU(double x) {
		return Math.max(0, x);
	}
	
	public double[] getWeights() {
		return weights;
	}

	public double getError() {
		return error;
	}

	public void setError(double error) {
		this.error = error;
	}

	public double getOutput() {
		return output;
	}

	public void setOutput(double output) {
		this.output = output;
	}

	public int getConnectionsCount() {
		return weights.length;
	}
	

}
