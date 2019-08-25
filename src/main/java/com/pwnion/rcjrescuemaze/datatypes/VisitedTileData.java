package com.pwnion.rcjrescuemaze.datatypes;

import java.util.HashMap;

public class VisitedTileData {
	private final Coords coords;
	private final HashMap<String, Boolean> walls;
	private final boolean corner;
	private final boolean silverTile;
	
	public VisitedTileData(Coords coords, HashMap<String, Boolean> walls, boolean corner, boolean silverTile) {
		this.coords = coords;
		this.walls = walls;
		this.corner = corner;
		this.silverTile = silverTile;
	}
	
	public Coords getCoords() {
		return coords;
	}
	
	public HashMap<String, Boolean> getWalls() {
		return walls;
	}
	
	public boolean getWalls(String position) {
		return walls.get(position);
	}
	
	public boolean getCorner() {
		return corner;
	}
	
	public boolean getSilverTile() {
		return silverTile;
	}
}
