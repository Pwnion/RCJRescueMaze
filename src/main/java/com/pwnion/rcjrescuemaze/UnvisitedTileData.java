package com.pwnion.rcjrescuemaze;

import java.util.HashMap;

public class UnvisitedTileData {
	private final int[] coords;
	private int distance;
	
	UnvisitedTileData(int[] coords, int distance) {
		this.coords = coords;
		this.distance = distance;
	}
	
	public int[] getCoords() {
		return coords;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}
}
