package com.pwnion.rcjrescuemaze.datatypes;

import java.util.ArrayList;

public class VisitedTileData {
	private final Coords coords;
	private final ArrayList<Boolean> walls;
	private final boolean corner;
	private final boolean silverTile;
	
	public VisitedTileData(Coords coords, ArrayList<Boolean> walls, boolean corner, boolean silverTile) {
		this.coords = coords;
		this.walls = walls;
		this.corner = corner;
		this.silverTile = silverTile;
	}
	
	public Coords getCoords() {
		return coords;
	}
	
	public ArrayList<Boolean> getWalls() {
		return walls;
	}
	
	public boolean getCorner() {
		return corner;
	}
	
	public boolean getSilverTile() {
		return silverTile;
	}
}
