package com.pwnion.rcjrescuemaze.hardware;

import com.google.inject.Inject;

import java.io.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class GetColour extends Colour {
	private String colour;
	private int imgX = 32;
	private int imgY = 32;

	private String getColour() throws IOException {
		new ProcessBuilder("raspistill", "-o", "/home/pi/cam/%d.jpg", "-w", "32", "-h", "32", "-t", "100").start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		File file = new File("/home/pi/cam_tmp");
		BufferedImage image = ImageIO.read(file);
		
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
		
		int avgColour[] = {avgRed / (imgX * imgY), avgGreen / (imgX * imgY), avgBlue / (imgX * imgY)};
		
		return "Average Red: " + avgColour[0] + "\nAverage Green: " + avgColour[1] + "\nAverage Blue: " + avgColour[2];
	}
	
	@Inject
	public GetColour(Pins pins) throws IOException {
		super(pins);
		
		this.colour = getColour();
	}
	
	@Override
	public String get() {
		return colour;
	}
}
