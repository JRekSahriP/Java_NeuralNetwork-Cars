package com.deeplearningcars.game.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.deeplearningcars.configuration.ConfigurationPanel;
import com.deeplearningcars.configuration.configfile.ConfigFile;
import com.deeplearningcars.configuration.configfile.Configs;
import com.deeplearningcars.game.scenery.Map;
import com.deeplearningcars.neuralnetwork.IA;
import com.deeplearningcars.neuralnetwork.NeuralNetwork;
import com.deeplearningcars.utils.Constants;
import com.deeplearningcars.utils.Utils;
//import com.deeplearningcars.configuration.controllers.Keyboard;

public class Cars {
	public final static int OUTPUT_COUNT = 4;
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
	
	public static void loadNeuralNetworks() {
		carsList.forEach(car->{
			car.NN = IA.createNeuralNetwork(IA.HIDDEN_LAYERS_COUNT, car.getSensor().length, IA.HIDDEN_NEURON_COUNT, OUTPUT_COUNT);
			car.DNA = new double[car.NN.getWeightCount()];
			
			for(int i = 0; i < car.DNA.length; i++) {
				car.DNA[i] = Utils.randomNumber();
			}
			IA.copyVectorToLayers(car.NN, car.DNA);
		});
	}
	
	private static void newCar() {
		Car car = new Car(generateSpawn());
		car.color = Utils.randomColor();

		carsList.add(car);
	}
	
	public static void update() {
		carsList.forEach(car -> car.update());
	}
	
	public static void draw(Graphics g ) {
		carsList.forEach(car -> car.draw(g));
	}
	
	public static int getActiveCars() {
		return (int)carsList.stream().filter(car -> !car.collided).count();
	}
	
	public static void copyCarsToVector() {
		for(int i = 0; i < carsList.size(); i++) {
			vector[i] = carsList.get(i);
		}
	}
	
	public static void sortCarsByFitness() {
		Arrays.sort(vector, (car1, car2) -> Double.compare(car2.fitness, car1.fitness));
	}
	

	public static void setFitness() {
		carsList.forEach(car -> {
			double distance = Map.maxDistance - Map.getMapDistance(car.getCenterX()/Constants.UNIT_SIZE, car.getCenterY()/Constants.UNIT_SIZE);
			car.fitness = distance;
		});
	}
	
	public static void setBestCar() {
		int index = 0;
		double bestFitness = 0;
		for(int i = 0; i < carsList.size(); i++) {
			carsList.get(i).bestCar = false;
			double carFitness = carsList.get(i).fitness;
			if(carFitness > bestFitness) {
				bestFitness = carFitness;
				index = i;
			}
		}
		carsList.get(index).bestCar = true;
	}
	public static Car getBestCar() {
		return carsList.stream().filter(car -> car.bestCar).findFirst().orElse(carsList.get(0));
	}

	public static void shareBestsDNA() {
		int steps = 5;
		for(int i = 0; i < steps; i++) {
			for(int j = steps+i; j < carsList.size(); j+=steps) {
				for(int k = 0; k < vector[j].DNA.length; k++) {
					vector[j].DNA[k] = vector[i].DNA[k];
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
			int modifications = (int)((Math.random() * carsList.get(0).DNA.length)+1);
			int type;
			for(int k = 0; k < modifications; k++) {
				type = (int)(Math.random() * 101);//0 - 100
				
				int index = (int)Math.abs(Math.random() * vector[j].DNA.length);
				
				if (type < randomValueChance) {
					
				    double value = Utils.randomNumber();
				    vector[j].DNA[index] = value;
				    
				} else if (type < scaleValueChance) {
					
				    double value = (Math.random() % 10001) / 10000.0 + 0.5;
				    vector[j].DNA[index] *= value;
				    
				} else if (type <= addValueChance){
					
				    double value = Utils.randomNumber() / 100.0;
				    vector[j].DNA[index] += value;
				    
				}
			}
		}
		carsList.forEach(car -> {
			IA.copyVectorToLayers(car.NN, car.DNA);
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
			car.respawn(generateSpawn());
		});
	}
	
	
	public static class Car {
		public Car(Point spawn) {
			this.X = spawn.x;
			this.Y = spawn.y;
			collision = new Rectangle(X,Y,width,height);
		}
		
		private NeuralNetwork NN;
		private double[] DNA;
		private double fitness;
		
		private static final int width = 2*Constants.UNIT_SIZE;
		private static final int height = 1*Constants.UNIT_SIZE;
		private int X;
		private int Y;
		
		private double angle = 90.0;
		private float speed = 0.0f;
		Rectangle collision;
		
		private static final int raysCount = ConfigFile.getInteger(Configs.cars_rays_count.toString());
		private double[] sensor = new double[raysCount];
		private boolean collided = false;
		
		private Color color;
		private boolean bestCar = false;
		
		private static final int MIN_SPEED = 0;
		private static final int MAX_SPEED = ConfigFile.getInteger(Configs.cars_max_speed.toString());
		
		public void update() {
			if(collided) {return;}
			processSensorValues();
			
			applyOutputs();
			//applyControllers();
			
			speed-=0.05;
			
			if(speed < 0.05) {speed = 0.0f;}
			if(speed < MIN_SPEED) {speed = MIN_SPEED;}
			if(speed > MAX_SPEED) {speed = MAX_SPEED;}
			
			move();
			checkCollision();
			
		}
		
		private void applyOutputs() {
			boolean[] outputs = NN.getOutputs(this.getSensor());
			if(outputs[0]) {speed += (speed <= MAX_SPEED) ? 0.2+(speed/50) : 0.0;}
			if(outputs[1]) {angle -= (speed/8)*1.5;}
			if(outputs[2]) {speed -= (speed > MIN_SPEED) ? 0.05 : 0.0;}
			if(outputs[3]) {angle += (speed/8)*1.5;}
		}
		
//		private void applyControllers() {
//			if(Keyboard.isPressedIgnoreCase('i')) {speed += (speed <= MAX_SPEED) ? 0.2+(speed/50) : 0.0;}
//			if(Keyboard.isPressedIgnoreCase('j')) {angle -= (speed/8)*1.5;}
//			if(Keyboard.isPressedIgnoreCase('k')) {speed -= (speed > MIN_SPEED) ? 0.05 : 0.0;}
//			if(Keyboard.isPressedIgnoreCase('l')) {angle += (speed/8)*1.5;}
//		}
		

		public Point2D[] getRotatedCollision() {
		    double centerX = collision.getCenterX();
		    double centerY = collision.getCenterY();
		    
		    Point2D[] points = new Point2D[4];
		    points[0] = new Point2D.Double(collision.getMinX(), collision.getMinY());
		    points[1] = new Point2D.Double(collision.getMaxX(), collision.getMinY());
		    points[2] = new Point2D.Double(collision.getMaxX(), collision.getMaxY());
		    points[3] = new Point2D.Double(collision.getMinX(), collision.getMaxY());

		    for (int i = 0; i < points.length; i++) {
		        double x = points[i].getX() - centerX;
		        double y = points[i].getY() - centerY;

		        points[i].setLocation(
		            centerX + (x * Math.cos(Math.toRadians(angle))) - (y * Math.sin(Math.toRadians(angle))),
		            centerY + (x * Math.sin(Math.toRadians(angle))) + (y * Math.cos(Math.toRadians(angle)))
		        );
		    }
		    return points;
		}

		private void checkCollision() {
		    for (Point2D point : getRotatedCollision()) {
		        int X = (int) (point.getX() / Constants.UNIT_SIZE);
		        int Y = (int) (point.getY() / Constants.UNIT_SIZE);
		        collided = Map.getMapCollision(X, Y) == 1;
		        if(collided) {break;}
		    }
		}
		
		private void move() {
			X += Math.cos(Math.toRadians(angle))*speed;
			Y += Math.sin(Math.toRadians(angle))*speed;
			collision.setBounds(X,Y,width,height);
		}
		
		public void draw(Graphics g) {
			if(ConfigurationPanel.showBestCar.isSelected()) {
				if(!bestCar) {return;}
			}
			g.setColor(getColor(color));
			
			if(ConfigurationPanel.showCarSensor.isSelected() && !collided){
				drawSensor(g);
			}
			
			drawCar(g);
			
			if(ConfigurationPanel.showCarCollision.isSelected()) {
				drawCollision(g);
			}
			g.setColor(Color.BLACK);
		}		

		private void drawCar(Graphics g) {
			Graphics2D g2d = (Graphics2D)g;
			Rectangle rect = Utils.transformToCameraSpace(X, Y, width, height);
				
			int centerX = rect.x+rect.width/2;
			int centerY = rect.y+rect.height/2;

			AffineTransform tr = new AffineTransform();
			tr.rotate(Math.toRadians(angle), centerX, centerY);
			g2d.setTransform(tr);
			
			g2d.fillRect(rect.x, rect.y, rect.width, rect.height);
			
			g2d.setTransform(new AffineTransform());
		}
		
		private void drawCollision(Graphics g) {
			Point2D[] points = getRotatedCollision();
			
			for(Point2D i : points) {
				Point local = Utils.transformPointToCameraSpace((int)i.getX(), (int)i.getY());
				int size = Utils.transformSizeToCameraSpace(15);
				g.drawOval(local.x-size/2, local.y-size/2, size, size);
			}
		}

		private void drawSensor(Graphics g) {
			for(int i = 0; i < raysCount; i++) {
				double rayAngle = Math.toRadians(angle-90 + (i * (180 / (raysCount - 1))));
				int x2 = (int)(Math.cos(rayAngle)*sensor[i]);
				int y2 = (int)(Math.sin(rayAngle)*sensor[i]);
				
				int[] line = Utils.transformLineToCameraSpace(getCenterX(), getCenterY(), X+x2, Y+y2);
				g.setColor(getColor(Color.RED));
				g.drawLine(line[0], line[1], line[2], line[3]);
				g.setColor(getColor(color));
			}
		}
		
		public void processSensorValues() {
			for(int i = 0; i < raysCount; i++) {
				
				float rayX = X;
				float rayY = Y;
				
				double rayAngle = Math.toRadians(angle-90 + (i * (180 / (raysCount - 1))));
				double rayAddX = Math.cos(rayAngle)/2;
				double rayAddY = Math.sin(rayAngle)/2;
				
				while(true) {
					if(Map.getMapCollision((int)rayX/Constants.UNIT_SIZE, (int)rayY/Constants.UNIT_SIZE) == 1) {break;}
					rayX += rayAddX;
					rayY += rayAddY;
				}
				
				double distance = Math.sqrt(Math.pow(X - rayX, 2) + Math.pow(Y - rayY, 2));
				sensor[i] = distance;
			}
		}
		
		public double[] getSensor() {return sensor;}		
		
		public void respawn(Point spawn) {
			X = spawn.x;
			Y = spawn.y;
			angle = 90.0;
			speed = 0;
			collided = false;
		}
		
		public int getCenterX() {
			return X + width/2;
		}
		public int getCenterY() {
			return Y + height/2;
		}
		
		public Color getColor(Color color) {
			if(!ConfigurationPanel.showHighlightBestCar.isSelected()) {
				return color;
			}
			if(bestCar) {
				return color;
			} else {
				int rColor = color.getRed();
				int gColor = color.getGreen();
				int bColor = color.getBlue();
				return new Color(rColor, gColor, bColor, 100);
			}
		}
	}
	
}
