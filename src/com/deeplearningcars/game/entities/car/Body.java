package com.deeplearningcars.game.entities.car;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import com.deeplearningcars.configuration.configfile.ConfigFile;
import com.deeplearningcars.configuration.configfile.Configs;
import com.deeplearningcars.game.scenery.Map;
import com.deeplearningcars.utils.Constants;
import com.deeplearningcars.utils.Utils;

public class Body {
	public static final int WIDTH = 2*Constants.UNIT_SIZE;
	public static final int HEIGHT = 1*Constants.UNIT_SIZE;
	public static final int MIN_SPEED = 0;
	public static final int MAX_SPEED = ConfigFile.getInteger(Configs.cars_max_speed.toString());

	private int x;
	private int y;
	private float speed;
	
	private float angle;
	private Rectangle collision;
	private Color color;

	private boolean collided;
	
	public Body(int x, int y) {
		this.x = x;
		this.y = y;
		this.speed = 0.0f;
		
		this.collision = new Rectangle(x, y, WIDTH, HEIGHT);
		
		this.angle = 90.0f;
		this.color = Utils.randomColor();
		
		this.collided = false;
	}
	
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

	void checkCollision() {
	    for (Point2D point : getRotatedCollision()) {
	        int x = (int) (point.getX() / Constants.UNIT_SIZE);
	        int y = (int) (point.getY() / Constants.UNIT_SIZE);
	        collided = Map.getMapCollision(x, y) == 1;
	        
	        if(collided)
	        	break;
	        
	    }
	}
	
	void move() {
		x += Math.cos(Math.toRadians(angle))*speed;
		y += Math.sin(Math.toRadians(angle))*speed;
		collision.setBounds(x, y, WIDTH, HEIGHT);
	}
	
	void setSpeed() {

		speed-=0.05;
		
		if(speed < 0.05) {speed = 0.0f;}
		if(speed < MIN_SPEED) {speed = MIN_SPEED;}
		if(speed > MAX_SPEED) {speed = MAX_SPEED;}
	}
	
	void applyOutputs(boolean[] outputs) {
		if(outputs[0]) {speed += (speed <= MAX_SPEED) ? 0.2+(speed/50) : 0.0;}
		if(outputs[1]) {angle -= (speed/8)*1.5;}
		if(outputs[2]) {speed -= (speed > MIN_SPEED) ? 0.05 : 0.0;}
		if(outputs[3]) {angle += (speed/8)*1.5;}
	}

	void drawCollision(Graphics g) {
		Point2D[] points = getRotatedCollision();
		
		for(Point2D i : points) {
			Point local = Utils.transformPointToCameraSpace((int)i.getX(), (int)i.getY());
			int size = Utils.transformSizeToCameraSpace(15);
			g.drawOval(local.x-size/2, local.y-size/2, size, size);
		}
	}

	public void respawn(int x, int y) {
		this.x = x;
		this.y = y;
		angle = 90.0f;
		speed = 0;
		collided = false;
	}

	
	public int getCenterX() {
		return x + WIDTH/2;
	}
	public int getCenterY() {
		return y + HEIGHT/2;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public float getAngle() {
		return angle;
	}

	public boolean isCollided() {
		return collided;
	}

	public Color getColor() {
		return color;
	}
	
	
	

//	private void applyControllers() {
//		if(Keyboard.isPressedIgnoreCase('i')) {speed += (speed <= MAX_SPEED) ? 0.2+(speed/50) : 0.0;}
//		if(Keyboard.isPressedIgnoreCase('j')) {angle -= (speed/8)*1.5;}
//		if(Keyboard.isPressedIgnoreCase('k')) {speed -= (speed > MIN_SPEED) ? 0.05 : 0.0;}
//		if(Keyboard.isPressedIgnoreCase('l')) {angle += (speed/8)*1.5;}
//	}
	
}
