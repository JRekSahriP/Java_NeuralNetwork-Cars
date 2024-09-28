package com.deeplearningcars.configuration;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import com.deeplearningcars.neuralnetwork.Generation;

public class ConfigurationPanel extends JPanel {
	ConfigurationPanel() {	
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initializeComponents();
        addComponents();
    }
	
	public static JCheckBox showFPS;
	public static JCheckBox showGeneration;
	
    public static JRadioButton mapView;
    public static JRadioButton collisionView;
    public static JCheckBox distanceView;
    
    public static JRadioButton showAllCars;
    public static JRadioButton showBestCar;
    public static JRadioButton showHighlightBestCar;
    
    
    public static JCheckBox showCarSensor;
    public static JCheckBox showCarCollision;

    public static JRadioButton freeCamera;
    public static JRadioButton followBestCar;

    public static JButton skipGeneration;
    
    
    
    private void initializeComponents() {
    	
    	showFPS = new JCheckBox("Show FPS");
    	showGeneration = new JCheckBox("Show Generation");

        ButtonGroup mapViewOptions = new ButtonGroup();
        mapView = new JRadioButton("Map View");
        mapView.setSelected(true);
        collisionView = new JRadioButton("Collision View");
      
        mapViewOptions.add(mapView);
        mapViewOptions.add(collisionView);
        distanceView = new JCheckBox("Distance Visible");
        
        showCarSensor = new JCheckBox("Show car sensors");
        showCarCollision = new JCheckBox("Show car collisions");
        
        ButtonGroup carsViewOptions = new ButtonGroup();
        showAllCars = new JRadioButton("Show all cars");
        showBestCar = new JRadioButton("Show best car only");
        showHighlightBestCar = new JRadioButton("Show all cars, but highlight the best one");
        showHighlightBestCar.setSelected(true);
        
        carsViewOptions.add(showAllCars);
        carsViewOptions.add(showBestCar);
        carsViewOptions.add(showHighlightBestCar);
        
        ButtonGroup cameraViewOptions = new ButtonGroup();
        freeCamera = new JRadioButton("Free Movement Camera");
        freeCamera.setSelected(true);
        followBestCar = new JRadioButton("Follow the best car");
        
        cameraViewOptions.add(freeCamera);
        cameraViewOptions.add(followBestCar);
        
        skipGeneration = new JButton("skip current Generation");
        skipGeneration.addActionListener(e -> {Generation.reset();});
        
        
    }

    private void addComponents() {
        this.add(new JLabel("General:"));
        this.add(showFPS);
        this.add(showGeneration);
        
        this.add(new JLabel("Map:"));
        this.add(mapView);
        this.add(collisionView);
        this.add(distanceView);

        this.add(new JLabel("Cars:"));
        this.add(showAllCars);
        this.add(showBestCar);
        this.add(showHighlightBestCar);
        
        this.add(showCarSensor);
        this.add(showCarCollision);
        
        this.add(new JLabel("Camera:"));
        this.add(freeCamera);
        this.add(followBestCar);
        
        this.add(skipGeneration);
        
	}
    
    public static JScrollPane createScrollPane(int width, int height) {
        ConfigurationPanel configPanel = new ConfigurationPanel();
        JScrollPane scrollPane = new JScrollPane(configPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(width, height)); 
        return scrollPane;
    }
}