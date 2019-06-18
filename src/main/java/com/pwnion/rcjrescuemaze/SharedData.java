package com.pwnion.rcjrescuemaze;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.inject.Singleton;

@Singleton
public class SharedData {
	//LIST VISITED: (Location, 4 bits for walls, 1 bit for Corner, 1 bit for Silver)
	private ArrayList<VisitedTileData> visited = new ArrayList<VisitedTileData>();
			
	//LIST UNVISTED: (Location, Distance)
	private ArrayList<UnvisitedTileData> unvisited = new ArrayList<UnvisitedTileData>();
			
	//LAST SILVER TILE: (Location)
	private int[] lastSilverTile = new int[]();
			
	//CURRENT POSITION: (Location)
	private int[] currentPos = new int[]();
	
	public ArrayList<VisitedTileData> getVisited() {
		return visited;
	}
	
	public ArrayList<UnvisitedTileData> getUnvisited() {
		return unvisited;
	}
	
	public int[] getLastSilverTile() {
		return lastSilverTile;
	}
	
	public int[] getCurrentPos() {
		return currentPos;
	}

	public void appendVisited(VisitedTileData visitedTileData) {
		visited.add(visitedTileData);
	}
	
	public void appendUnvisited(UnvisitedTileData unvisitedTileData) {
		unvisited.add(unvisitedTileData);
	}
	
	public void setUnvisited(int[] coords, int distance) {
		for(UnvisitedTileData unvisitedTileData : unvisited) {
			if(unvisitedTileData.getCoords().equals(coords)) {
				unvisitedTileData = new UnvisitedTileData(coords, distance);
				return;
			}
		}
	}
	
	public void removeUnvisited(int[] coords) {
		for(UnvisitedTileData unvisitedTileData : unvisited) {
			if(unvisitedTileData.getCoords().equals(coords)) {
				unvisited.remove(unvisitedTileData);
				return;
			}
		}
	}
	
	public void setLastSilverTile(int[] coords) {
		lastSilverTile = coords;
	}
	
	public void setCurrentPos(int[] coords) {
		currentPos = coords;
	}
}
