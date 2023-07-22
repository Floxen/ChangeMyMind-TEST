package cz.floxen.gfx;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

public class Display extends Canvas implements Runnable {
	
	public static final long serialVersionUID = 1L;
	
	public final int HEIGHT = 600;
	public final int WIDTH = 600;
	public final int SCALE = 1;
	
	public JFrame display = new JFrame("ChangeMyMind");
	
	public BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	public int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	public int[] debugInfo = new int[2];
	
	public Graphics g;
	
	public Renderer renderer;
	
	public Display() {
		this.renderer = new Renderer(WIDTH, HEIGHT);
		this.renderer.createPerlinNoise();
		
		this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		this.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		this.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		
		display.add(this);
		display.setResizable(false);
		display.pack();
		display.setAlwaysOnTop(true);
		display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		display.setLocationRelativeTo(null);
		display.setVisible(true);
	}
	
	public void run() {
		
		long lastTime = System.nanoTime();
		long lastTimer = System.currentTimeMillis();
		
		double unpassed = 0;
		double maxFrames = Math.pow(10, 9) / 120.0;
		
		int frames = 0;
		int ticks = 0;
		
		while (true) {
			
			long now = System.nanoTime();
			unpassed += (now - lastTime) / maxFrames;
			lastTime = now;
			
			boolean shouldRender = false;
			while (unpassed >= 0) {
				unpassed--;
				ticks++;
				shouldRender = true;
			}
			
			try {
				Thread.sleep((int) Math.round(Math.abs(unpassed)));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (shouldRender) {
				frames++;
				renderProcess();
			}
			
			if (System.currentTimeMillis() - lastTimer > 1000) {
				lastTimer += 1000;
				System.out.println(frames + " frames, " + ticks + " ticks");
				debugInfo[0] = frames;
				debugInfo[1] = ticks;
				frames = 0;
				ticks = 0;
			}
		}
	}
	
	public void renderProcess() {
		BufferStrategy bs = this.getBufferStrategy();
		
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		renderer.clearRender(this.pixels);
		
		for (int i = 0; i < 64; i++) {
			int xP = (int) (Math.sin((System.currentTimeMillis() + i) % 2000.0 / 2000 * Math.PI * 2) * 200);
			int yP = (int) (Math.cos((System.currentTimeMillis() + i) % 2000.0 / 2000 * Math.PI * 2) * 200);
			
			renderer.render(xP, yP, 128, 128, this.pixels, this.renderer.pixels);
		}
		
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.setColor(Color.yellow);
		g.setFont(new Font("consolas", Font.PLAIN, 16));
		g.drawString("TESTING ONLY! (by fL0X3n)", 20, 30);
		this.renderDebugInfo();
		g.dispose();
		bs.show();
	}
	
	public void renderDebugInfo() {
		if (debugInfo[0] == 0 && debugInfo[1] == 0) return;
		g.setColor(Color.white);
		g.setFont(new Font("consolas", Font.PLAIN, 16));
		g.drawString("fps: " + debugInfo[0], 20, 70);
		g.drawString("ticks: " + debugInfo[1], 20, 90);
	}
}
