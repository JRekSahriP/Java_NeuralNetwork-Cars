package com.deeplearningcars.utils;

import java.awt.Dimension;

import com.deeplearningcars.configuration.configfile.ConfigFile;
import com.deeplearningcars.configuration.configfile.Configs;

public class Constants {

	public static final int SCREEN_WIDTH = ConfigFile.getInteger(Configs.game_screen_width.toString()),
							SCREEN_HEIGHT = ConfigFile.getInteger(Configs.game_screen_height.toString());
	public static final Dimension SCREEN_SIZE = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);
	public static final int UNIT_SIZE = 50;
	
	
}
