package com.pwnion.rcjrescuemaze.hardware;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public abstract class RGBFromImage {
	private int imgX = 32;
	private int imgY = 32;
	
	private int avgRed;
	private int avgGreen;
	private int avgBlue;
	
	protected HashMap<String, Integer> getAvgColours(String path) throws IOException {
		File file = new File(path);
		BufferedImage image = ImageIO.read(file);
		
		avgRed = 0;
		avgGreen = 0;
		avgBlue = 0;
		
		for(int y = 0; y < imgY; y++) {
			for(int x = 0; x < imgX; x++) {
				avgRed += (image.getRGB(x, y) & 0x00ff0000) >> 16;
				avgGreen += (image.getRGB(x, y) & 0x0000ff00) >> 8;
				avgBlue += image.getRGB(x, y) & 0x000000ff;
			}
		}
		
		return new HashMap<String, Integer>() {
			private static final long serialVersionUID = 1L;
			{
				put("Red", avgRed / (imgX * imgY));
				put("Green", avgGreen / (imgX * imgY));
				put("Blue", avgBlue / (imgX * imgY));
			};
		};
	}
	
	//Abstract implementations
	public abstract String get();
}
