package com.deeplearningcars.configuration;

import javax.swing.JFrame;

import com.deeplearningcars.configuration.configfile.ConfigFile;
import com.deeplearningcars.configuration.configfile.Configs;
import com.deeplearningcars.utils.Constants;

public class MainFrame extends JFrame {
	
	public MainFrame() {
		super("Deep Learning Cars");
		
		if(ConfigFile.getBoolean(Configs.config_menu_visible.toString())) {
			configurationsFrame();
		} else {
			// Initializes default configuration variables
			new ConfigurationPanel();
		}
		
		this.add(new Gameloop());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(Constants.SCREEN_SIZE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	public void configurationsFrame() {
		int width = ConfigFile.getInteger(Configs.config_menu_screen_width.toString());
		int height = ConfigFile.getInteger(Configs.config_menu_screen_height.toString());
		JFrame frame = new JFrame("Configurations");
		frame.add(ConfigurationPanel.createScrollPane(width, height));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(width, height);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	public static void main(String[] args) {
		ConfigFile.readJsonConfig();
		System.out.println("Configuration file Loaded");
		
		new MainFrame();
		
	}
}
