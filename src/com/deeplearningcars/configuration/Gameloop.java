package com.deeplearningcars.configuration;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.deeplearningcars.configuration.configfile.ConfigFile;
import com.deeplearningcars.configuration.configfile.Configs;
import com.deeplearningcars.configuration.controllers.Keyboard;
import com.deeplearningcars.game.Game;
import com.deeplearningcars.game.entities.Cars;
import com.deeplearningcars.game.scenery.Map;
import com.deeplearningcars.game.ui.Camera;
import com.deeplearningcars.utils.Constants;
import com.deeplearningcars.utils.Utils;

public class Gameloop extends JPanel implements Runnable {
	public static int FPS = 0;
	private static BufferedImage background;
	public Gameloop() {
		this.addKeyListener(new Keyboard());
		this.setFocusable(true);
		
		loadAll();
		
		new Thread(this).start();
	}
	
	public void loadAll() {
		Map.load();
		System.out.println("Map Loaded");
		
		background = Utils.loadBufferedImage("background.png");
		
		Cars.loadCars(ConfigFile.getInteger(Configs.cars_quantity.toString()));
		System.out.println("Cars Loaded");
		
		Cars.loadNeuralNetworks();
		System.out.println("Neural Networks Loaded");
		
		Camera.setX(Map.mapWidth*Constants.UNIT_SIZE/2);
		Camera.setY(Map.mapHeight*Constants.UNIT_SIZE/2);
		Camera.setZoom(25);
		System.out.println("Camera Loaded");
		
	}
	
	@Override
	public void run() {
		
		Long lasttime = System.nanoTime();
		double ticks = ConfigFile.getDouble(Configs.fps_limit.toString());
		double ns = 1_000_000_000/ticks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		
		while(true) {
			long now = System.nanoTime();
			delta+=(now-lasttime)/ns;
			lasttime=now;
			if(delta>=1) {
				update();
				repaint();
				frames++;
				delta--;
			}
			if(System.currentTimeMillis()-timer>=1000) {
				FPS = frames;
				timer+=1000;
				frames=0;
			}
		}
	}
	public void update() {
		Game.update();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), null);
		Game.draw(g);
	}
	
}
