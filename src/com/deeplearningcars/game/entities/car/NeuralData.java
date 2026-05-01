package com.deeplearningcars.game.entities.car;

import java.awt.Color;
import java.awt.Graphics;

import com.deeplearningcars.configuration.configfile.ConfigFile;
import com.deeplearningcars.configuration.configfile.Configs;
import com.deeplearningcars.game.scenery.Map;
import com.deeplearningcars.neuralnetwork.NeuralNetwork;
import com.deeplearningcars.utils.Constants;
import com.deeplearningcars.utils.Utils;


public class NeuralData {

	public final static int OUTPUT_COUNT = 4;
	public static final int HIDDEN_LAYERS_COUNT = 1;
	public static final int HIDDEN_NEURON_COUNT = 2;
	private static final int RAYS_COUNT = ConfigFile.getInteger(Configs.cars_rays_count.toString());
	
	private NeuralNetwork nn;
	private double[] dna;
	private double fitness;
	private double[] sensor;
	
	
	public NeuralData() {
		nn = new NeuralNetwork(HIDDEN_LAYERS_COUNT, RAYS_COUNT, HIDDEN_NEURON_COUNT, OUTPUT_COUNT);
		dna = new double[nn.getWeightCount()];
		
		for(int i = 0; i < dna.length; i++) {
			dna[i] = Utils.randomNumber();
		}
		
		nn.copyVectorToLayers(dna);
		sensor = new double[RAYS_COUNT];
	}
	

	public boolean[] getOutputs() {
		double[] outputValues = new double[OUTPUT_COUNT];
		boolean[] output = new boolean[OUTPUT_COUNT];
		nn.copyToInput(this.sensor);
		nn.processOutput();
		outputValues = nn.getOutput();
		
		for(int i = 0; i < outputValues.length; i++) {
			output[i] = outputValues[i] > 0;
		}
		return output;
	
	}
	

	public void processSensorValues(int x, int y, float angle) {
		for(int i = 0; i < RAYS_COUNT; i++) {
			
			float rayX = x;
			float rayY = y;
			
			double rayAngle = Math.toRadians(angle - 90 + (i * (180 / (RAYS_COUNT - 1))));
			double rayAddX = Math.cos(rayAngle)/2;
			double rayAddY = Math.sin(rayAngle)/2;
			
			while(true) {
				if(Map.getMapCollision((int)rayX/Constants.UNIT_SIZE, (int)rayY/Constants.UNIT_SIZE) == 1) {break;}
				rayX += rayAddX;
				rayY += rayAddY;
			}
			
			double distance = Math.sqrt(Math.pow(x - rayX, 2) + Math.pow(y - rayY, 2));
			sensor[i] = distance;
		}
	}
	
	void drawSensor(Graphics g, Car car) {
		Body body = car.getBody();
		for(int i = 0; i < RAYS_COUNT; i++) {
			
			double rayAngle = Math.toRadians(body.getAngle() - 90 + (i * (180 / (RAYS_COUNT - 1))));
			
			int x2 = (int)(Math.cos(rayAngle)*sensor[i]);
			int y2 = (int)(Math.sin(rayAngle)*sensor[i]);
			
			int[] line = Utils.transformLineToCameraSpace(
					body.getCenterX(),
					body.getCenterY(),
					body.getX() + x2,
					body.getY() + y2);
			
			g.setColor(car.getColor(Color.RED));
			g.drawLine(line[0], line[1], line[2], line[3]);
			g.setColor(car.getColor(body.getColor()));
		}
	}

	
	public NeuralNetwork getNN() {
		return nn;
	}

	public double[] getSensor() {
		return sensor;
	}

	public double getFitness() {
		return fitness;
	}
	
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public double[] getDna() {
		return dna;
	}

	
	
}
