package com.pwnion.rcjrescuemaze.hardware;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class Camera {
	private int imgX = 32;
	private int imgY = 32;
	
	private BufferedImage image;
	
	protected Camera() throws IOException {
		this.image = rawSensorOutput();
	}
	
	private BufferedImage rawSensorOutput() throws IOException {
		File file = new File("/home/pi/cam.jpg");
		return ImageIO.read(file);
	}
	
	protected int[] getAvgColours() throws IOException {
		int avgRed = 0;
		int avgGreen = 0;
		int avgBlue = 0;
		
		for(int y = 0; y < imgY; y++) {
			for(int x = 0; x < imgX; x++) {
				avgRed += (image.getRGB(x, y) & 0x00ff0000) >> 16;
				avgGreen += (image.getRGB(x, y) & 0x0000ff00) >> 8;
				avgBlue += image.getRGB(x, y) & 0x000000ff;
			}
		}
		
		return new int[] {avgRed / (imgX * imgY), avgGreen / (imgX * imgY), avgBlue / (imgX * imgY)};
	}
	
	//Abstract implementations
	public abstract String get();
}
