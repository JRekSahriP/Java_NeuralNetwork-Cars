package com.deeplearningcars.game.ui;

import com.deeplearningcars.configuration.ConfigurationPanel;
import com.deeplearningcars.configuration.controllers.Keyboard;
import com.deeplearningcars.game.entities.Cars;

public class Camera {
	private static int X;
	private static int Y;
	private static final int SPEED = 15;
	
	private static int zoom = 100;//100%
	private static final int MIN_ZOOM = 10;
	private static final int MAX_ZOOM = 300;
	
	public static void update() {
		if(ConfigurationPanel.freeCamera.isSelected()) {
			freeMove();
		} else if(ConfigurationPanel.followBestCar.isSelected()) {
			lockCar();
		}
		
		if(Keyboard.isPressed('+')) {changeZoom(1);}
		if(Keyboard.isPressed('-')) {changeZoom(-1);}
	}
	
	private static void freeMove() {
		if(Keyboard.isPressedIgnoreCase('w')) {moveY(-SPEED);}
		if(Keyboard.isPressedIgnoreCase('a')) {moveX(-SPEED);}
		if(Keyboard.isPressedIgnoreCase('s')) {moveY(SPEED);}
		if(Keyboard.isPressedIgnoreCase('d')) {moveX(SPEED);}
	}
	private static void lockCar() {
		X = Cars.getBestCar().getCenterX();
		Y = Cars.getBestCar().getCenterY();
	}
	
	public static void setX(int x) {X = x;}
	public static void setY(int y) {Y = y;}
	public static void setZoom(int zoom) {Camera.zoom = zoom;}
	
	public static void moveX(int value) {X+=value;}
	public static void moveY(int value) {Y+=value;}
	public static void changeZoom(int value) {
		if(zoom + value >= MIN_ZOOM && zoom + value <= MAX_ZOOM) {
			zoom+=value;
		}
	}
	
	public static int getX() {return X;}
	public static int getY() {return Y;}
	public static int getZoom() {return zoom;}
	
}
