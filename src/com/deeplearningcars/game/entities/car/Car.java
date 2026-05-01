package com.deeplearningcars.game.entities.car;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import com.deeplearningcars.configuration.ConfigurationPanel;
import com.deeplearningcars.game.scenery.Map;
import com.deeplearningcars.utils.Constants;
import com.deeplearningcars.utils.Utils;

public class Car {

	private NeuralData neural;
	private Body body;
	
	private boolean bestCar;
	
	public Car(int x, int y) {
		this.neural = new NeuralData();
		this.body = new Body(x, y);
		this.bestCar = false;		
	}
	
	public void update() {
		if(body.isCollided()) {return;}
		
		neural.processSensorValues(body.getX(), body.getY(), body.getAngle());
		
		this.applyOutputs();
		
		body.setSpeed();
		body.move();
		body.checkCollision();
		
	}
	
	private void applyOutputs() {
		boolean[] outputs = neural.getOutputs();
		body.applyOutputs(outputs);
	}
	
	public void calculateFitness() {
		int centerX = getBody().getCenterX();
		int centerY = getBody().getCenterY();
		double distance = Map.maxDistance - Map.getMapDistance(
				centerX/Constants.UNIT_SIZE,
				centerY/Constants.UNIT_SIZE
		);
		getNeural().setFitness(distance);
	}
	
	
	public void draw(Graphics g) {
		if(ConfigurationPanel.showBestCar.isSelected()) {
			if(!bestCar) {return;}
		}
		g.setColor(getColor(body.getColor()));
		
		if(ConfigurationPanel.showCarSensor.isSelected() && !body.isCollided()){
			neural.drawSensor(g, this);
		}
		
		drawCar(g);
		
		if(ConfigurationPanel.showCarCollision.isSelected()) {
			body.drawCollision(g);
		}
		g.setColor(Color.BLACK);
	}		

	private void drawCar(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		Rectangle rect = Utils.transformToCameraSpace(body.getX(), body.getY(), Body.WIDTH, Body.HEIGHT);
			
		int centerX = rect.x+rect.width/2;
		int centerY = rect.y+rect.height/2;

		AffineTransform tr = new AffineTransform();
		tr.rotate(Math.toRadians(body.getAngle()), centerX, centerY);
		g2d.setTransform(tr);
		
		g2d.fillRect(rect.x, rect.y, rect.width, rect.height);
		
		g2d.setTransform(new AffineTransform());
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

	public NeuralData getNeural() {
		return neural;
	}

	public Body getBody() {
		return body;
	}

	public boolean isBestCar() {
		return bestCar;
	}

	public void setBestCar(boolean bestCar) {
		this.bestCar = bestCar;
	}
	
	
	
}