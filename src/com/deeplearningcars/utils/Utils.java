package com.deeplearningcars.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.deeplearningcars.game.ui.Camera;

public class Utils {
	public static BufferedImage loadBufferedImage(String path) {
		try {
			File file = new File("resources/images/"+path);
			return ImageIO.read(file);
		} catch (IOException e) {
			LogCreate.logError("Error to read Image: " + path, e);
			throw new IllegalArgumentException();
		}
	}
	public static File readConfigurationDocument(String path) {
		try {
			File fileConfig = new File("resources/configurations/" + path);
			return fileConfig;
		} catch(Exception e) {
			LogCreate.logError("Error to read Image: " + path, e);
			throw new IllegalArgumentException();
		}
	}
	
	public static int transformSizeToCameraSpace(int size) {
		double scale = Camera.getZoom()/100.0;
		
		int newSize = (int)(size * scale);
		
		return newSize;
	}
	
	public static int[] transformLineToCameraSpace(int x1, int y1, int x2, int y2) {
		double scale = Camera.getZoom()/100.0;
		
		x1 -= Camera.getX();
		y1 -= Camera.getY();
		x2 -= Camera.getX();
		y2 -= Camera.getY();

	
		int adjustX1 = (int)(x1*scale);
		int adjustY1 = (int)(y1*scale);
		int adjustX2 = (int)(x2*scale);
		int adjustY2 = (int)(y2*scale);

		adjustX1 += Constants.SCREEN_WIDTH*0.5;
		adjustY1 += Constants.SCREEN_HEIGHT*0.5;
		adjustX2 += Constants.SCREEN_WIDTH*0.5;
		adjustY2 += Constants.SCREEN_HEIGHT*0.5;
		
		return new int[]{adjustX1, adjustY1, adjustX2, adjustY2};
	}
	
	public static Rectangle transformToCameraSpace(int x, int y, int width, int height) {
		double scale = Camera.getZoom()/100.0;
		
		Point point = transformPointToCameraSpace(x, y);
		int adjustX = point.x;
		int adjustY = point.y;
		
		int adjustWidth = (int)(width * scale);
		int adjustHeight = (int)(height * scale);
	
		return new Rectangle(adjustX, adjustY, adjustWidth, adjustHeight);
	}
	
	public static Point transformPointToCameraSpace(int x, int y) {
		double scale = Camera.getZoom()/100.0;
		
		x -= Camera.getX();
		y -= Camera.getY();
	
		int adjustX = (int)(x*scale);
		int adjustY = (int)(y*scale);

		adjustX += Constants.SCREEN_WIDTH*0.5;
		adjustY += Constants.SCREEN_HEIGHT*0.5;
		
		return new Point(adjustX,adjustY);
	}
	
	public static Font getDefaultFont() {return new Font("SansSerif", Font.PLAIN, 12);}
	public static Font getFontOfSize(int size) {return new Font("SansSerif", Font.PLAIN, size);}
	
	public static double randomNumber(){return Math.random() * 2000.0 - 1000.0;}
	public static Color randomColor() {
		 int red = (int)(Math.random()*256);
	     int green = (int)(Math.random()*256);
	     int blue = (int)(Math.random()*256);
	     
	     return new Color(red, green, blue);
	}
}
