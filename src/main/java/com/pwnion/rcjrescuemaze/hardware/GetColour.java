package com.pwnion.rcjrescuemaze.hardware;

import java.io.IOException;
import java.util.HashMap;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.pwnion.rcjrescuemaze.software.SharedData1;

public class GetColour extends RGBFromImage {
	private SharedData1 sharedData;
	
	private int[][][] rgbValues;
	private String colourPerPixel[][][];
	private String tile;
	
	private int redDiff = 1000;
	private int greenDiff = 1000;
	private int blueDiff = 1000;

	private String[][][] getColourPerPixel() throws IOException {
		String colourPerPixel[][][] = new String[imgX][imgY][3];

		for(int y = 0; y < imgY; y++) {
			for(int x = 0; x < imgX; x++) {
				sharedData.getTileValues().keySet().forEach((colour) -> {
					try {
						redDiff = Math.abs(sharedData.getTileValues().get(colour).get("Red") - rgbValues[imgX][imgY][0]);
						greenDiff = Math.abs(sharedData.getTileValues().get(colour).get("Red") - rgbValues[imgX][imgY][0]);
						blueDiff = Math.abs(sharedData.getTileValues().get(colour).get("Red") - rgbValues[imgX][imgY][0]);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			}
		}
		
		return colourPerPixel;
	}
	
	@Inject
	public GetColour(@Assisted String path, SharedData1 sharedData) throws IOException {
		this.rgbValues = super.getRGBValues(path);
		this.sharedData = sharedData;
		
		int totalColourDiff = 1000;
		
		String closestTile = "";
		
		for(String tile : sharedData.getTileValues().keySet()) {
			int colourDiff = 0;
			
			for(String colour : sharedData.getTileValues().get(tile).keySet()) {
				colourDiff += Math.abs(sharedData.getTileValues().get(tile).get(colour) - avgColours.get(colour));
			}
			
			if(colourDiff < totalColourDiff) {
				totalColourDiff = colourDiff;
				closestTile = tile;
			}
		}
		tile = closestTile;
	}
	
	public int[][][] getRGBValues() {
		return rgbValues;
	}
	
	@Override
	public String get() {
		return tile;
	}
}
