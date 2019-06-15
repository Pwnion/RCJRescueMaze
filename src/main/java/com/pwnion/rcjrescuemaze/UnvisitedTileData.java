package com.pwnion.rcjrescuemaze;

import java.util.HashMap;

public class UnvisitedTileData {
	private final HashMap<Integer, Integer> coords;
	private int distance;
	
	UnvisitedTileData(HashMap<Integer, Integer> coords, int distance) {
		this.coords = coords;
		this.distance = distance;
	}
	
	HashMap<Integer, Integer> getCoords() {
		return coords;
	}
	
	int getDistance() {
		return distance;
	}
	
	void setDistance(int distance) {
		this.distance = distance;
	}
}
