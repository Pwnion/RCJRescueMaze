package com.pwnion.rcjrescuemaze;

public class UnvisitedTileData {
	private final Coords coords;
	private int distance;
	
	public UnvisitedTileData(Coords coords, int distance) {
		this.coords = coords;
		this.distance = distance;
	}
	
	public Coords getCoords() {
		return coords;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}
}
