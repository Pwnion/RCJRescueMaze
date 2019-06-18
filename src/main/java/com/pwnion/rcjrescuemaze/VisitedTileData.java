package com.pwnion.rcjrescuemaze;

import java.util.ArrayList;
import java.util.HashMap;

public class VisitedTileData {
	private final HashMap<Integer, Integer> coords;
	private final ArrayList<Boolean> walls;
	private final boolean corner;
	private final boolean silverTile;
	
	VisitedTileData(HashMap<Integer, Integer> coords, ArrayList<Boolean> walls, boolean corner, boolean silverTile) {
		this.coords = coords;
		this.walls = walls;
		this.corner = corner;
		this.silverTile = silverTile;
	}
	
	public HashMap<Integer, Integer> getCoords() {
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
