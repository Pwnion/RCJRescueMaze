package com.pwnion.rcjrescuemaze.hardware;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class RGBFromImage {
	protected int imgX = 720;
	protected int imgY = 720;
	
	protected int[][][] getRGBValues(String path) throws IOException {
		
		File file = new File(path);
		BufferedImage image = ImageIO.read(file);
		
		int rgbValues[][][] = new int[imgX][imgY][3];
		for(int y = 0; y < imgY; y++) {
			for(int x = 0; x < imgX; x++) {
				rgbValues[x][y][0] = (image.getRGB(x, y) & 0x00ff0000) >> 16;
				rgbValues[x][y][1] =  (image.getRGB(x, y) & 0x0000ff00) >> 8;
				rgbValues[x][y][2] = image.getRGB(x, y) & 0x000000ff;
			}
		}
		return rgbValues;
	}
	
	//Abstract implementations
	public abstract String get();
}
