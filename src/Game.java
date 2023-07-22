package cz.floxen.game;

import cz.floxen.gfx.Display;

public class Game {
	
	private static Display display = new Display();
	
	public static void main(String[] args) {
		new Thread(display).start();
	}
}
