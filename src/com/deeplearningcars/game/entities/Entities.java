package com.deeplearningcars.game.entities;

import java.awt.Graphics;

public class Entities {
	public static void update() {
		Cars.update();
	}
	public static void draw(Graphics g) {
		Cars.draw(g);
	}
}
