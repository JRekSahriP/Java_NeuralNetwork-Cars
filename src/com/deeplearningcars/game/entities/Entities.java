package com.deeplearningcars.game.entities;

import java.awt.Graphics;

import com.deeplearningcars.game.entities.car.Cars;

public class Entities {
	public static void update() {
		Cars.update();
	}
	public static void draw(Graphics g) {
		Cars.draw(g);
	}
}
