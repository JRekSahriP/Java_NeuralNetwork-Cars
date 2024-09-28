package com.deeplearningcars.neuralnetwork;

import java.util.ArrayList;

public class IA {
	//private final static double LEARNING_RATE = 0.1;
	private final static double INITIAL_WEIGHT = 1.0;
	private final static double BIAS = 1.0;
	
	public static final int HIDDEN_LAYERS_COUNT = 1;
	public static final int HIDDEN_NEURON_COUNT = 2;

	private static double hiddenActivation(double X) {return reLU(X);}
	private static double outputActivation(double X) {return reLU(X);}

//	private static double func(double X) {
//		if(X == 0) {return  0;}
//		if(X <  0) {return -1;}
//		if(X >  0) {return  1;}
//		throw new RuntimeException("Error");
//	}
	
	private static double reLU(double X) {
		return Math.max(0, X);
	}
	
//	private static double reLUDx(double X) {
//		if(X < 0) {return 0;}
//		else {return 1;}
//	}
	
	public static void createNeuron(Neuron neuron, int connections) {
		neuron.ConnectionsCount = connections;
		neuron.weight = new ArrayList<>(connections);
        
        for (int i = 0; i < connections; i++) {
        	neuron.weight.add(0.0);
        }
        for (int i = 0; i < connections; i++) {
            if ((int)(Math.random()) == 0) {
            	neuron.weight.set(i,Math.random() / INITIAL_WEIGHT);
            } else {
            	neuron.weight.set(i,-Math.random() / INITIAL_WEIGHT);
            }
        }
        neuron.error = 0;
        neuron.output = 1;
    }

    public static NeuralNetwork createNeuralNetwork(int hiddenLayersCount, int inputNeuronCount, int hiddenNeuronCount, int outputNeuronCount) {
    	inputNeuronCount += BIAS;
    	hiddenNeuronCount += BIAS;
    	
        NeuralNetwork nn = new NeuralNetwork();
        nn.inputLayer.neuronsCount = inputNeuronCount;
        
        for(int i = 0; i < inputNeuronCount; i++) {
        	nn.inputLayer.neurons.add(new Neuron());
        	nn.inputLayer.neurons.get(i).output = 1;
        }

        nn.hiddenLayersCount = hiddenLayersCount;

        for(int i = 0; i < hiddenLayersCount; i++) {
        	Layer layer = new Layer();
        	layer.neuronsCount = hiddenNeuronCount;
        	for(int j = 0; j < hiddenNeuronCount; j++) {
        		layer.neurons.add(new Neuron());
        		if(i == 0) {
        			createNeuron(layer.neurons.get(j), inputNeuronCount);
        		} else {
        			createNeuron(layer.neurons.get(j), hiddenNeuronCount);
        		}
        	}
        	nn.hiddenLayers.add(layer);
        }
       
        nn.outputLayer.neuronsCount = outputNeuronCount;
        for(int i = 0; i < outputNeuronCount; i++) {
        	nn.outputLayer.neurons.add(new Neuron());
        	createNeuron(nn.outputLayer.neurons.get(i), outputNeuronCount);
        }

        return nn;
    }
	
	public static void copyVectorToLayers(NeuralNetwork nn, double[] vector) {
		int count = 0;
		for(int i = 0; i < nn.hiddenLayersCount; i++) {
			for(int j = 0; j < nn.hiddenLayers.get(i).neuronsCount; j++) {
				for(int k = 0; k < nn.hiddenLayers.get(i).neurons.get(j).ConnectionsCount; k++) {
					nn.hiddenLayers.get(i).neurons.get(j).weight.set(k, vector[count]);
					count++;
				}
			}
		}
		for(int i = 0; i < nn.outputLayer.neuronsCount; i++) {
			for(int j = 0; j < nn.outputLayer.neurons.get(i).ConnectionsCount; j++) {
				nn.outputLayer.neurons.get(i).weight.set(j, vector[count]);
				count++;
			}
		}	
	}
	public static void copyLayersToVector(NeuralNetwork nn, double[] vector) {
		int count = 0;
		for(int i = 0; i < nn.hiddenLayersCount; i++) {
    		for(int j = 0; j < nn.hiddenLayers.get(i).neuronsCount;j++) {
    			for(int k = 0; k < nn.hiddenLayers.get(i).neurons.get(j).ConnectionsCount; k++) {
					vector[count] = nn.hiddenLayers.get(i).neurons.get(j).weight.get(k);
					count++;
				}
			}
		}
		for(int i = 0; i < nn.outputLayer.neuronsCount; i++) {
    		for(int j = 0; j < nn.outputLayer.neurons.get(i).ConnectionsCount; j++) {
				vector[count] = nn.outputLayer.neurons.get(i).weight.get(j);
				count++;
			}
		}	
	}
	
	public static void copyToInput(NeuralNetwork nn, double[] vectorInput) {
		for(int i = 0; i < nn.inputLayer.neuronsCount - BIAS; i++) {
			nn.inputLayer.neurons.get(i).output = vectorInput[i];
		}
	}
	public static void copyFromOutput(NeuralNetwork nn, double[] vectorOutput) {
		for(int i = 0; i < nn.outputLayer.neuronsCount; i++) {
			vectorOutput[i] = nn.outputLayer.neurons.get(i).output;
		}
	}
	public static void processOutput(NeuralNetwork nn) {
		double sum;
		int k;
		for(int i = 0; i < nn.hiddenLayers.get(0).neuronsCount - BIAS; i++) {
			sum = 0;
			for(int j = 0; j < nn.inputLayer.neuronsCount; j++) {
				double output = nn.inputLayer.neurons.get(j).output;
				double weight = nn.hiddenLayers.get(0).neurons.get(i).weight.get(j);
				sum += output * weight;
			}
			nn.hiddenLayers.get(0).neurons.get(i).output = hiddenActivation(sum);
		}
		
		for(k = 1; k < nn.hiddenLayersCount; k++) {
			for(int i = 0; i < nn.hiddenLayers.get(k).neuronsCount - BIAS; i++) {
				sum = 0;
				for(int j=0;j<nn.hiddenLayers.get(k-1).neuronsCount;j++) {
					double output = nn.hiddenLayers.get(k-1).neurons.get(j).output;
					double weight = nn.hiddenLayers.get(k).neurons.get(i).weight.get(j);
					sum += output * weight;	
				}
				nn.hiddenLayers.get(k).neurons.get(i).output = hiddenActivation(sum);
			}
		}
		
		for(int i = 1; i < nn.outputLayer.neuronsCount; i++) {
			sum = 0;
			for(int j = 0; j < nn.hiddenLayers.get(k-1).neuronsCount; j++) {
				double output = nn.hiddenLayers.get(k-1).neurons.get(j).output;
				double weight = nn.outputLayer.neurons.get(i).weight.get(j);
				sum += output * weight;
			}
			nn.outputLayer.neurons.get(i).output = outputActivation(sum);
		}
	}
}
