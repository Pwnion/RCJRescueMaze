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
	private HashMap<Integer, Integer> lastSilverTile = new HashMap<Integer, Integer>();
			
	//CURRENT POSITION: (Location)
	private HashMap<Integer, Integer> currentPos = new HashMap<Integer, Integer>();
	
	public ArrayList<VisitedTileData> getVisited() {
		return visited;
	}
	
	public ArrayList<UnvisitedTileData> getUnvisited() {
		return unvisited;
	}
	
	public HashMap<Integer, Integer> getLastSilverTile() {
		return lastSilverTile;
	}
	
	public HashMap<Integer, Integer> getCurrentPos() {
		return currentPos;
	}

	public void appendVisited(VisitedTileData visitedTileData) {
		visited.add(visitedTileData);
	}
	
	public void appendUnvisited(UnvisitedTileData unvisitedTileData) {
		unvisited.add(unvisitedTileData);
	}
	
	public void setUnvisited(HashMap<Integer, Integer> coords, int distance) {
		for(UnvisitedTileData unvisitedTileData : unvisited) {
			if(unvisitedTileData.getCoords().equals(coords)) {
				unvisitedTileData = new UnvisitedTileData(coords, distance);
				return;
			}
		}
	}
	
	public void removeUnvisited(HashMap<Integer, Integer> coords) {
		for(UnvisitedTileData unvisitedTileData : unvisited) {
			if(unvisitedTileData.getCoords().equals(coords)) {
				unvisited.remove(unvisitedTileData);
				return;
			}
		}
	}
	
	public void setLastSilverTile(HashMap<Integer, Integer> coords) {
		lastSilverTile = coords;
	}
	
	public void setCurrentPos(HashMap<Integer, Integer> coords) {
		currentPos = coords;
	}
}
