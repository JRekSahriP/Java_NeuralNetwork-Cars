package com.deeplearningcars.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogCreate {
    private static Logger logger;

    static {
    	 try {
             File logDir = new File("Logs");
             if (!logDir.exists()) {
                 logDir.mkdir();
             }

             logger = Logger.getLogger("MyLog");
             FileHandler fh = new FileHandler("Logs/error_log.log", true);
             logger.addHandler(fh);
             SimpleFormatter formatter = new SimpleFormatter();
             fh.setFormatter(formatter);
             logger.setLevel(Level.ALL);
         } catch (IOException e) {
             e.printStackTrace();
         }
    }

    public static void logError(String message, Throwable throwable) {
        logger.severe(message + "\n" + throwable.toString());
    }
}
