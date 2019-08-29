package com.pwnion.rcjrescuemaze.hardware;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.pwnion.rcjrescuemaze.software.SharedData1;

public class GetColour extends RGBFromImage {
	private SharedData1 sharedData;
	
	private int[][][] rgbValues;
	private HashMap<String, int[][][]> sharedDataValues;
	private String colourPerPixel[][];
	private HashMap<String, Float> colourPercentages;

	private String[][] getColourPerPixel() throws IOException {
		String colourPerPixel[][] = new String[imgX][imgY];
		
		Set<String> sharedDataColours = sharedData.getTileValues().keySet();
		sharedDataValues = sharedData.getTileValues();
		int[][][] sharedDataRGB;
		

		for(int y = 0; y < imgY; y++) {
			for(int x = 0; x < imgX; x++) {
				int leastDiff = 1000;
				for(String colour : sharedDataColours) {
					int tempDiff = 0;
					sharedDataRGB = sharedDataValues.get(colour);
					
					for(int i = 0; i < 3; i++) {
						tempDiff += Math.abs(sharedDataRGB[x][y][i] - rgbValues[x][y][i]);
					}
					
					if(tempDiff < leastDiff) {
						leastDiff = tempDiff;
						colourPerPixel[x][y] = colour;
					}
				};
			}
		}
		return colourPerPixel;
	}
	
	public HashMap<String, Float> getColourPercentages() {
		HashMap<String, Float> colourPercentages = new HashMap<String, Float>();
		
		try {
			for(String colour : sharedData.getTileValues().keySet()) {
				colourPercentages.put(colour, 0f);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String pixelColour;
		for(int y = 0; y < imgY; y++) {
			for(int x = 0; x < imgX; x++) {
				pixelColour = colourPerPixel[x][y];
				colourPercentages.put(pixelColour, colourPercentages.get(pixelColour) + 1);
			}
		}
		
		for(String colour : colourPercentages.keySet()) {
			colourPercentages.put(colour, (colourPercentages.get(colour) / (imgX * imgY)) * 100);
		}
		
		return colourPercentages;
	}
	
	@Inject
	public GetColour(@Assisted String path, SharedData1 sharedData) throws IOException {
		this.sharedData = sharedData;
		
		System.out.println("this.rgbValues = super.getRGBValues(path);");
		this.rgbValues = super.getRGBValues(path);
		System.out.println("this.colourPerPixel = getColourPerPixel();");
		this.colourPerPixel = getColourPerPixel();
		System.out.println("this.colourPercentages = getColourPercentages();");
		this.colourPercentages = getColourPercentages();
		System.out.println("Finish");
	}
	
	public int[][][] getRGBValues() {
		return rgbValues;
	}
	
	@Override
	public String get() {
		String tileColour = "";
		float highestPercentage = 0;
		for(String colour : colourPercentages.keySet()) {
			if(colourPercentages.get(colour) > highestPercentage) {
				highestPercentage = colourPercentages.get(colour);
				tileColour = colour;
			}
		}
		return tileColour;
	}
	
	
}
