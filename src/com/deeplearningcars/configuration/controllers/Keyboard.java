package com.deeplearningcars.configuration.controllers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class Keyboard implements KeyListener {
	private static List<Character> keys = new ArrayList<>();
	@Override
	public void keyPressed(KeyEvent e) {
		char key = e.getKeyChar();
		if(!(isPressed(key))){
			keys.add(key);
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		keys.removeIf(key -> key.equals(e.getKeyChar()));
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	
	public static boolean isPressed(char key) {
		return keys.contains(key);
	}
	public static boolean isPressedIgnoreCase(char key) {
		Character Lower = Character.toLowerCase(key);
		Character Upper = Character.toUpperCase(key);
		return keys.contains(Lower) || keys.contains(Upper);
	}
}