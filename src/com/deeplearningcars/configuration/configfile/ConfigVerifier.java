package com.deeplearningcars.configuration.configfile;

import java.util.HashMap;

public class ConfigVerifier {

    public static void verify(HashMap<String, String> config) {
        double fpsLimit = Double.parseDouble(config.get(Configs.fps_limit.toString()));
        if (fpsLimit <= 0.00) {
            config.put(Configs.fps_limit.toString(), "60.0");
        }
        
        int generationMaxTime;
        try {
            generationMaxTime = Integer.parseInt(config.get(Configs.generation_max_time.toString()));
        } catch (NumberFormatException e) {
            generationMaxTime = 0;
        }
        if (generationMaxTime <= 0) {
            config.put(Configs.generation_max_time.toString(), "120");
        }

        int gameScreenWidth, gameScreenHeight;
        try {
            gameScreenWidth = Integer.parseInt(config.get(Configs.game_screen_width.toString()));
            gameScreenHeight = Integer.parseInt(config.get(Configs.game_screen_height.toString()));
        } catch (NumberFormatException e) {
            gameScreenWidth = gameScreenHeight = 0;
        }
        if (gameScreenWidth == 0) {
            config.put(Configs.game_screen_width.toString(), "600");
        }
        if (gameScreenHeight == 0) {
            config.put(Configs.game_screen_height.toString(), "500");
        }

        int configMenuScreenWidth, configMenuScreenHeight;
        try {
            configMenuScreenWidth = Integer.parseInt(config.get(Configs.config_menu_screen_width.toString()));
            configMenuScreenHeight = Integer.parseInt(config.get(Configs.config_menu_screen_height.toString()));
        } catch (NumberFormatException e) {
            configMenuScreenWidth = configMenuScreenHeight = 0;
        }
        if (configMenuScreenWidth == 0) {
            config.put(Configs.config_menu_screen_width.toString(), "400");
        }
        if (configMenuScreenHeight == 0) {
            config.put(Configs.config_menu_screen_height.toString(), "350");
        }

        int carsQuantity;
        try {
            carsQuantity = Integer.parseInt(config.get(Configs.cars_quantity.toString()));
        } catch (NumberFormatException e) {
            carsQuantity = 0;
        }
        if (carsQuantity <= 7) {
            config.put(Configs.cars_quantity.toString(), "15");
        }

        int carsRaysCount;
        try {
            carsRaysCount = Integer.parseInt(config.get(Configs.cars_rays_count.toString()));
        } catch (NumberFormatException e) {
            carsRaysCount = 0;
        }
        if (carsRaysCount < 9) {
            config.put(Configs.cars_rays_count.toString(), "9");
        }

        int carsMaxSpeed;
        try {
            carsMaxSpeed = Integer.parseInt(config.get(Configs.cars_max_speed.toString()));
        } catch (NumberFormatException e) {
            carsMaxSpeed = 0;
        }
        if (carsMaxSpeed <= 0) {
            config.put(Configs.cars_max_speed.toString(), "10");
        }

        int mutationChanceRandomValue;
        try {
            mutationChanceRandomValue = Integer.parseInt(config.get(Configs.mutation_chance_random_value.toString()));
        } catch (NumberFormatException e) {
            mutationChanceRandomValue = 0;
        }
        if (mutationChanceRandomValue < 0 || mutationChanceRandomValue > 100) {
            config.put(Configs.mutation_chance_random_value.toString(), "10");
            mutationChanceRandomValue = 10;
        }

        int mutationChanceScaleValue;
        try {
            mutationChanceScaleValue = Integer.parseInt(config.get(Configs.mutation_chance_scale_value.toString()));
        } catch (NumberFormatException e) {
            mutationChanceScaleValue = 0;
        }
        if (mutationChanceScaleValue < 0 || mutationChanceScaleValue > 100) {
            config.put(Configs.mutation_chance_scale_value.toString(), "55");
            mutationChanceScaleValue = 55;
        }

        int mutationChanceAddValue;
        try {
            mutationChanceAddValue = Integer.parseInt(config.get(Configs.mutation_chance_add_value.toString()));
        } catch (NumberFormatException e) {
            mutationChanceAddValue = 0;
        }
        if (mutationChanceAddValue < 0 || mutationChanceAddValue > 100) {
            config.put(Configs.mutation_chance_add_value.toString(), "35");
            mutationChanceAddValue = 35;
        }
        
        if(mutationChanceRandomValue + mutationChanceAddValue + mutationChanceScaleValue != 100) {
        	config.put(Configs.mutation_chance_random_value.toString(), "10");
        	config.put(Configs.mutation_chance_scale_value.toString(), "55");
          	config.put(Configs.mutation_chance_add_value.toString(), "35");
        }
    }
}
