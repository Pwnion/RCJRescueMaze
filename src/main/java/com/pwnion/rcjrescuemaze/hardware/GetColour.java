package com.pwnion.rcjrescuemaze.hardware;

import java.io.IOException;
import java.util.HashMap;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.pwnion.rcjrescuemaze.software.SharedData1;

public class GetColour extends RGBFromImage {
	private HashMap<String, Integer> avgColours;
	private String tile;

	@Inject
	public GetColour(@Assisted String path, SharedData1 sharedData) throws IOException {
		this.avgColours = super.getAvgColours(path);
		
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
	
	public HashMap<String, Integer> getAvgColours() {
		return avgColours;
	}
	
	@Override
	public String get() {
		return tile;
	}
}
