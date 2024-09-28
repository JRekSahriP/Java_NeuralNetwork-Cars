package com.deeplearningcars.configuration.configfile;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import com.deeplearningcars.utils.LogCreate;
import com.deeplearningcars.utils.Utils;

public class ConfigFile {

	private static HashMap<String, String> configValues = new HashMap<>();
	
	private static final String PATH = "DeepLearnConfig.json";

    public static void readJsonConfig() {
        StringBuilder content = new StringBuilder();
        try (Scanner scan = new Scanner(Utils.readConfigurationDocument(PATH))) {
            while (scan.hasNext()) {
                content.append(scan.nextLine());
            }
        } catch (FileNotFoundException e) {
            LogCreate.logError("Config file not found: " + PATH, e);
        }

        readJsonContent(content.toString());
    }

	
	private static void readJsonContent(String content) {
        try {
            content = content.replaceAll("[{}\"]", "").replaceAll("\\s+", "");
            String[] lines = content.split(",");

            for (String line : lines) {
                String[] pairs = line.split(":");
                if (pairs.length == 2) {
                    configValues.put(pairs[0], pairs[1]);
                } else {
                    LogCreate.logError("Invalid config line: " + line, new IllegalArgumentException("Invalid format"));
                }
            }

            ConfigVerifier.verify(configValues);
        } catch (Exception e) {
        	LogCreate.logError("Error processing JSON content", e);
        }
    }
	
	private static String get(String name) {return configValues.get(name);}
	
	public static boolean getBoolean(String name) {
        return Boolean.parseBoolean(get(name));
    }
    public static int getInteger(String name) {
        return Integer.parseInt(get(name));
    }
    public static double getDouble(String name) {
        return Double.parseDouble(get(name));
    }
    
}
