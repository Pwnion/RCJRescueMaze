package com.pwnion.rcjrescuemaze;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.inject.Singleton;

@Singleton
public interface SharedData {
	//LIST VISITED: (Location, 4 bits for walls, 1 bit for Corner, 1 bit for Silver)
	ArrayList<VisitedTileData> visited = new ArrayList<VisitedTileData>();
			
	//LIST UNVISTED: (Location, Distance)
	ArrayList<UnvisitedTileData> unvisited = new ArrayList<UnvisitedTileData>();
			
	//LAST SILVER TILE: (Location)
	HashMap<Integer, Integer> lastSilverTile = new HashMap<Integer, Integer>();
			
	//CURRENT POSITION: (Location)
	HashMap<Integer, Integer> currentPosition = new HashMap<Integer, Integer>();
}
