package com.deeplearningcars.game;

import java.awt.Graphics;

import com.deeplearningcars.game.entities.Entities;
import com.deeplearningcars.game.scenery.Scenery;
import com.deeplearningcars.game.ui.UI;
import com.deeplearningcars.neuralnetwork.Generation;

public class Game {
	public static void update() {
		Generation.update();
		Entities.update();
		UI.update();
	}
	public static void draw(Graphics g) {
		Scenery.draw(g);
		Entities.draw(g);
		UI.draw(g);
	}
}
