package com.deeplearningcars.game.scenery;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.deeplearningcars.configuration.ConfigurationPanel;
import com.deeplearningcars.game.entities.Cars;
import com.deeplearningcars.utils.Constants;
import com.deeplearningcars.utils.Utils;

public class Map {
	private static BufferedImage mapImageCollision, mapImageView;
	private static int[][] mapCollision, mapDistances;
	private static int endX, endY;
	public static int maxDistance;
	public static int mapWidth;
	public static int mapHeight;
	
	/* 
	 * white = -1 || 0
	 * black = -16777216
	 * red = -65536
	 * green = -16711936 
	 * yellow = -256
	 */
	
	public static void load() {
		mapImageCollision = Utils.loadBufferedImage("mapCollision.png");
		mapImageView = Utils.loadBufferedImage("mapView.png");
		
		mapWidth = mapImageCollision.getWidth();
		mapHeight = mapImageCollision.getHeight();

		
		mapCollision = new int[mapWidth][mapHeight];
		mapDistances = new int[mapWidth][mapHeight];
		
		loadMapCollision();
		loadMapDistancies();
	}
	
	private static void loadMapCollision() {
		for(int y = 0; y<mapCollision.length; y++) {
			for(int x = 0; x<mapCollision[y].length; x++) {
				int rgb = mapImageCollision.getRGB(x, y);
				mapCollision[y][x] = (rgb == -16777216) ? 1 : 0;
				processColor(rgb, x, y);
			}
		}
	}
	private static void loadMapDistancies() {
		int newDistance;
		
		fillDistances();
		resetEnd();
		
		while(true) {
			int changes = 0;
			for (int y = 0; y < mapCollision.length; y++) {
		        for (int x = 0; x < mapCollision[y].length; x++) {
		            if (mapCollision[y][x] == 0) {
		                newDistance = 1 + getMinNeighborValue(x, y);
		                if(newDistance < mapDistances[y][x]) {
		                	mapDistances[y][x] = newDistance;
		                	changes++;
		                }
		            }
		        }
		    }
			if(changes == 0) {break;}
		}
		for (int y = 0; y < mapCollision.length; y++) {
	        for (int x = 0; x < mapCollision[y].length; x++) {
	            if (mapCollision[y][x] == 1) {
	               mapDistances[y][x] = 1000000;
	            }
	        }
	    }
		
		resetEnd();
		maxDistance = findMaxDistance();
	}
	
	private static int getMinNeighborValue(int x, int y) {
		int[] neighbors = new int[8];
		
		try {
			neighbors[0] = mapDistances[y][x+1];
			neighbors[1] = mapDistances[y][x-1];
			neighbors[2] = mapDistances[y+1][x];
			neighbors[3] = mapDistances[y-1][x];
			neighbors[4] = mapDistances[y+1][x+1];
			neighbors[5] = mapDistances[y+1][x-1];
			neighbors[6] = mapDistances[y-1][x+1];
			neighbors[7] = mapDistances[y-1][x-1];
		}catch(ArrayIndexOutOfBoundsException e) {/*Do nothing*/}
		int min = Integer.MAX_VALUE;
		for(int i : neighbors) {
			if(i < min && i >= 0) {
				min = i;
			}
		}
		return min;
	}
	private static int findMaxDistance() {
	    int max = 0;
	    for (int y = 0; y < mapCollision.length; y++) {
	        for (int x = 0; x < mapCollision[y].length; x++) {
	            if (mapCollision[y][x] == 0) {
	                if (mapDistances[y][x] > max && mapDistances[y][x] != Integer.MAX_VALUE) {
	                    max = mapDistances[y][x];
	                }
	            }
	        }
	    }
	    return max;
	}
	private static void resetEnd() {mapDistances[endY][endX] = 0;}
	private static void fillDistances() {
		for (int y = 0; y < mapCollision.length; y++) {
	        for (int x = 0; x < mapCollision[y].length; x++) {
	           mapDistances[y][x] = 99999;
	        }
	    }
	}
	private static void processColor(int rgb, int x, int y) {
		if(rgb == -256) {
			endX = x;
			endY = y;
		}
		if(rgb == -65536) {
			Point p1 = Cars.getSpawnArea1();
			Point p2 = Cars.getSpawnArea2();
			
			if(p1.x == 0 && p1.y == 0) {
				Cars.setSpawnArea1(new Point(x,y));
			} else if(p2.x == 0 && p2.y == 0) {
				Cars.setSpawnArea2(new Point(x,y));
			}
		}
	}
	
	public static void draw(Graphics g) {
		
		if(ConfigurationPanel.mapView.isSelected()) {
			drawMapView(g);
		} else if(ConfigurationPanel.collisionView.isSelected()) {
			drawMapCollision(g);
		}
		
		if(ConfigurationPanel.distanceView.isSelected()) {
			drawMapDistances(g);
		}
		
	}
	private static void drawMapView(Graphics g) {
		Rectangle r = Utils.transformToCameraSpace(0, 0,mapImageView.getWidth()*Constants.UNIT_SIZE, mapImageView.getHeight()*Constants.UNIT_SIZE);
		g.drawImage(mapImageView, r.x, r.y, r.width, r.height, null);
	}
	private static void drawMapCollision(Graphics g) {
		for(int y = 0; y<mapCollision.length; y++) {
			for(int x = 0; x<mapCollision[y].length; x++) {
				if(mapCollision[y][x]==1) {
					Rectangle r = Utils.transformToCameraSpace(x*Constants.UNIT_SIZE, y*Constants.UNIT_SIZE, Constants.UNIT_SIZE, Constants.UNIT_SIZE);
					g.fillRect(r.x, r.y, r.width, r.height);
				}
			}
		}
	}
	private static void drawMapDistances(Graphics g) {
		g.setFont(Utils.getFontOfSize(Utils.transformSizeToCameraSpace(16)));
		
		float fixCenterX = 0.3f;
		float fixCenterY = 0.55f;
		for(int y = 0; y<mapCollision.length; y++) {
			for(int x = 0; x<mapCollision[y].length; x++) {
				int value = mapDistances[y][x];
				if(value > 10000) {continue;}
				String text = Integer.toString(value);
				
				Point p = Utils.transformPointToCameraSpace((int)((x+fixCenterX)*Constants.UNIT_SIZE), (int)((y+fixCenterY)*Constants.UNIT_SIZE));
					
				g.drawString(text, p.x, p.y);
			}
		}
		g.setFont(Utils.getDefaultFont());
	}
	
	public static int getMapCollision(int x, int y){return mapCollision[y][x];}
	public static int getMapDistance(int x, int y) {return mapDistances[y][x];}
}
