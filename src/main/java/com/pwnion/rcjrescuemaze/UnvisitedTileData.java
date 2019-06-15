package com.pwnion.rcjrescuemaze;

import java.util.HashMap;

public class UnvisitedTileData {
	private final HashMap<Integer, Integer> coords;
	private int distance;
	
	UnvisitedTileData(HashMap<Integer, Integer> coords, int distance) {
		this.coords = coords;
		this.distance = distance;
	}
	
	public HashMap<Integer, Integer> getCoords() {
		return coords;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}
}
