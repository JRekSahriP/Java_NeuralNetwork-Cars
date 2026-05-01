package com.deeplearningcars.game.entities.car;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.deeplearningcars.configuration.configfile.ConfigFile;
import com.deeplearningcars.configuration.configfile.Configs;
import com.deeplearningcars.utils.Constants;
import com.deeplearningcars.utils.Utils;

public class Cars {
	private static List<Car> carsList = new ArrayList<>();
	private static int spawnAreaX1,spawnAreaX2,spawnAreaY1,spawnAreaY2;
	
	public static Car[] vector;
	public static Car temp;
	
	public static void loadCars(int Quantity) {
		vector = new Car[Quantity];
		for(int i = 0; i < Quantity; i++) {
			newCar();
		}
	}
	
	private static void newCar() {
		Point s = generateSpawn();
		carsList.add(new Car(s.x, s.y));
	}
	
	public static void update() {
		carsList.forEach(car -> car.update());
	}
	
	public static void draw(Graphics g ) {
		carsList.forEach(car -> car.draw(g));
	}
	
	public static int getActiveCars() {
		return (int)carsList.stream().filter(car -> !car.getBody().isCollided()).count();
	}
	
	public static void copyCarsToVector() {
		for(int i = 0; i < carsList.size(); i++) {
			vector[i] = carsList.get(i);
		}
	}
	
	public static void sortCarsByFitness() {
		Arrays.sort(vector, (car1, car2) -> Double.compare(
				car2.getNeural().getFitness(), 
				car1.getNeural().getFitness()
		));
	}
	

	public static void setFitness() {
		carsList.forEach(Car::calculateFitness);
	}
	
	public static void setBestCar() {
		int index = 0;
		double bestFitness = 0;
		for(int i = 0; i < carsList.size(); i++) {
			carsList.get(i).setBestCar(false);
			double carFitness = carsList.get(i).getNeural().getFitness();
			if(carFitness > bestFitness) {
				bestFitness = carFitness;
				index = i;
			}
		}
		carsList.get(index).setBestCar(true);
	}
	public static Car getBestCar() {
		return carsList.stream().filter(car -> car.isBestCar()).findFirst().orElse(carsList.get(0));
	}

	public static void shareBestsDNA() {
		int steps = 5;
		for(int i = 0; i < steps; i++) {
			double[] main = vector[i].getNeural().getDna();
			for(int j = steps+i; j < carsList.size(); j+=steps) {
				double[] dna = vector[j].getNeural().getDna();
				for(int k = 0; k < dna.length; k++) {
					dna[k] = main[k];
				}
			}
		}
	}
	
	public static void mutation() {
		int steps = 5;
		
		int randomValueChance = ConfigFile.getInteger(Configs.mutation_chance_random_value.toString());
		int scaleValueChance = ConfigFile.getInteger(Configs.mutation_chance_scale_value.toString());
		int addValueChance = ConfigFile.getInteger(Configs.mutation_chance_add_value.toString());
		
		for(int j = steps; j < carsList.size(); j++) {
			double[] dna = vector[j].getNeural().getDna();
			
			int modifications = (int)((Math.random() * carsList.get(0).getNeural().getDna().length)+1);
			int type;
			for(int k = 0; k < modifications; k++) {
				type = (int)(Math.random() * 101);//0 - 100
				
				int index = (int)Math.abs(Math.random() * dna.length);
				
				if (type < randomValueChance) {
					
				    double value = Utils.randomNumber();
				    dna[index] = value;
				    
				} else if (type < scaleValueChance) {
					
				    double value = (Math.random() % 10001) / 10000.0 + 0.5;
				    dna[index] *= value;
				    
				} else if (type <= addValueChance){
					
				    double value = Utils.randomNumber() / 100.0;
				    dna[index] += value;
				    
				}
			}
		}
		carsList.forEach(car -> {
			car.getNeural().getNN().copyVectorToLayers(car.getNeural().getDna());
		});
	}
	
	
	public static void setSpawnArea1(Point point) {
		spawnAreaX1 = point.x;
		spawnAreaY1 = point.y;
	}
	public static void setSpawnArea2(Point point) {
		spawnAreaX2 = point.x;
		spawnAreaY2 = point.y;
	}
	
	public static Point getSpawnArea1() {return new Point(spawnAreaX1, spawnAreaY1);}
	public static Point getSpawnArea2() {return new Point(spawnAreaX2, spawnAreaY2);}

	private static Point generateSpawn() {
		int spawnX = (int)(spawnAreaX1 + Math.random() * (spawnAreaX2 - spawnAreaX1));
		int spawnY = (int)(spawnAreaY1 + Math.random() * (spawnAreaY2 - spawnAreaY1));
		spawnX*=Constants.UNIT_SIZE;
		spawnY*=Constants.UNIT_SIZE;
		
		return new Point(spawnX, spawnY);
	}
	
	public static void respawnCars() {
		carsList.forEach(car -> {
			Point s = generateSpawn();
			car.getBody().respawn(s.x, s.y);
		});
	}
	
	
}
