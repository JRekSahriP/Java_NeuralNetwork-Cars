package com.deeplearningcars.neuralnetwork;

import com.deeplearningcars.configuration.configfile.ConfigFile;
import com.deeplearningcars.configuration.configfile.Configs;
import com.deeplearningcars.game.entities.Cars;

public class Generation {
	public static int generation = 0;
	private static Long startTime = -1L;
	private static final int MAX_TIME = ConfigFile.getInteger(Configs.generation_max_time.toString())*1000;
	public static void update() {
		if(startTime == -1) {startTime = System.currentTimeMillis();}
		
		boolean reset = 
				System.currentTimeMillis() - startTime > MAX_TIME ||
				Cars.getActiveCars() == 0;	
		
		if(reset) {
			reset();
		}
		
	}
	
	public static void reset() {
		Cars.setFitness();
		Cars.copyCarsToVector();
		Cars.sortCarsByFitness();
		Cars.shareBestsDNA();
		Cars.mutation();
		
		Cars.setBestCar();
		Cars.respawnCars();
		startTime = System.currentTimeMillis();
		generation++;
	}
}
