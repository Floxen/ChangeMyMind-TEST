package cz.floxen.gfx;

import java.util.Random;

public class Renderer {
	
	public final int WIDTH;
	public final int HEIGHT;
	
	public int[] pixels;
	public Random rand = new Random();
	
	public Renderer(int WIDTH, int HEIGHT) {
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
	}
	
	public void render(int xP, int yP, int xS, int yS, int[] iColArr, int[] eColArr) {
		
		int ww = WIDTH;
		int hh = HEIGHT;
		
		int xO = ww / 2 - (xS / 2) + xP;
		int yO = hh / 2 - (yS / 2) + yP;
		
		for (int x = 0; x < xS; x++) {
			int xp = x + xO;
			if (xp >= WIDTH) continue;
			if (xp < 0) continue;
			
			for (int y = 0; y < yS; y++) {
				int yp = y + yO;
				if (yp >= HEIGHT) continue;
				if (yp < 0) continue;
				
				int iTileIndex = xp + yp * ww;
				int eTileIndex = x + y * ww;
				
				iColArr[iTileIndex] = eColArr[eTileIndex];
			}
		}
	}
	
	public void clearRender(int[] iColArr) {
		for (int i = 0; i < iColArr.length; i++) {
			iColArr[i] = 0;
		}
	}
	
	public void createPerlinNoise() {
		int ww = WIDTH;
		int hh = HEIGHT;
		
		this.pixels = new int[ww * hh];
		
		for (int i = 0; i < this.pixels.length; i++) {
			this.pixels[i] = rand.nextInt();
		}
	}
}
