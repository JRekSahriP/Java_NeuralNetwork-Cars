package com.deeplearningcars.game.ui;

import static com.deeplearningcars.configuration.ConfigurationPanel.showFPS;
import static com.deeplearningcars.configuration.ConfigurationPanel.showGeneration;

import java.awt.Color;
import java.awt.Graphics;

import com.deeplearningcars.configuration.Gameloop;
import com.deeplearningcars.neuralnetwork.Generation;

public class UI {
	public static void update() {
		Camera.update();	
	}
	public static void draw(Graphics g) {
		int boxHeight = 0;
		int addHeight = 30;
		int elementY = addHeight-10;
		
		if(showFPS.isSelected()) {
			boxHeight += addHeight;
		}
		if(showGeneration.isSelected()) {
			boxHeight += addHeight;
		}

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 100, boxHeight);
		int margin = 2;
		g.setColor(Color.WHITE);
		g.fillRect(margin, margin, 100-margin*2, boxHeight-margin*2);
		g.setColor(Color.BLACK);
		
		if(showFPS.isSelected()) {
			String text = String.format("FPS: %d", Gameloop.FPS);
			g.drawString(text, 10, elementY);
			elementY += addHeight;
		}
		if(showGeneration.isSelected()) {
			String text = String.format("Generation: %d", Generation.generation);
			g.drawString(text, 10, elementY);
			elementY += addHeight;
		}
	}
}
