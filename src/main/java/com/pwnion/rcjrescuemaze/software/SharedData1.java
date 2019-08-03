package com.pwnion.rcjrescuemaze.software;

import java.util.ArrayList;
import java.util.HashSet;

import com.google.inject.Singleton;
import com.pwnion.rcjrescuemaze.datatypes.Coords;
import com.pwnion.rcjrescuemaze.datatypes.UnvisitedTileData;
import com.pwnion.rcjrescuemaze.datatypes.VisitedTileData;

@Singleton
public class SharedData1 {
	//LIST VISITED: (Location, 4 bits for walls, 1 bit for Corner, 1 bit for Silver)
	private ArrayList<VisitedTileData> visited = new ArrayList<VisitedTileData>();
			
	//LIST UNVISTED: (Location, Distance)
	private ArrayList<UnvisitedTileData> unvisited = new ArrayList<UnvisitedTileData>();
	
	//LIST BLACK TILES
	private ArrayList<Coords> blackTiles = new ArrayList<Coords>();
			
	//LAST SILVER TILE: (Location)
	private Coords lastSilverTile = new Coords(0, 0);
			
	//CURRENT POSITION: (Location)
	private Coords currentPos = new Coords(0, 0);
	
	//LOCATION AND DIRECTION OF THE RAMP TILE
	private Coords rampTile = new Coords(0, 0);
	private String rampDir = "";
	
	public ArrayList<VisitedTileData> getVisited() {
		return visited;
	}
	
	public ArrayList<UnvisitedTileData> getUnvisited() {
		return unvisited;
	}
	
	public ArrayList<Coords> getBlackTiles() {
		return blackTiles;
	}
	
	public Coords getLastSilverTile() {
		return lastSilverTile;
	}
	
	public Coords getCurrentPos() {
		return currentPos;
	}
	
	public Coords getRampTile() {
		return rampTile;
	}
	
	public String getRampDir() {
		return rampDir;
	}

	public int getVisitedIndex(Coords coords) {
		for(int i = 0; i < visited.size(); i++) {
			if(visited.get(i).getCoords() == coords) {
				return i;
			}
		}
		return -1;
	}

	public int getUnvisitedIndex(Coords coords) {
		for(int i = 0; i < unvisited.size(); i++) {
			if(unvisited.get(i).getCoords() == coords) {
				return i;
			}
		}
		return -1;
	}
	
	public ArrayList<Coords> getVisitedCoords() {
		ArrayList<Coords> visitedCoords = new ArrayList<Coords>();
		for(VisitedTileData visitedTileData : visited) {
			visitedCoords.add(visitedTileData.getCoords());
		}
		return visitedCoords;
	}
	
	public ArrayList<ArrayList<Boolean>> getVisitedWalls() {
		ArrayList<ArrayList<Boolean>> visitedWalls = new ArrayList<ArrayList<Boolean>>();
		for(VisitedTileData visitedTileData : visited) {
			visitedWalls.add(visitedTileData.getWalls());
		}
		return visitedWalls;
	}
	
	public ArrayList<Boolean> getVisitedCorners() {
		ArrayList<Boolean> visitedCorners = new ArrayList<Boolean>();
		for(VisitedTileData visitedTileData : visited) {
			visitedCorners.add(visitedTileData.getCorner());
		}
		return visitedCorners;
	}
	
	public ArrayList<Boolean> getVisitedSilverTiles() {
		ArrayList<Boolean> visitedSilverTiles = new ArrayList<Boolean>();
		for(VisitedTileData visitedTileData : visited) {
			visitedSilverTiles.add(visitedTileData.getSilverTile());
		}
		return visitedSilverTiles;
	}

	public ArrayList<Coords> getUnvisitedCoords() {
		ArrayList<Coords> unvisitedCoords = new ArrayList<Coords>();
		for(UnvisitedTileData unvisitedTileData : unvisited) {
			unvisitedCoords.add(unvisitedTileData.getCoords());
		}
		return unvisitedCoords;
	}
	
	public ArrayList<Integer> getUnvisitedDistances() {
		ArrayList<Integer> unvisitedDistances = new ArrayList<Integer>();
		for(UnvisitedTileData unvisitedTileData : unvisited) {
			unvisitedDistances.add(unvisitedTileData.getDistance());
		}
		return unvisitedDistances;
	}
	
	public Coords getClosestTile() {
		
		UnvisitedTileData closestTile = getUnvisited().get(0);
	
		//Find closest unvisited tile
		for(UnvisitedTileData unvisitedTileData : getUnvisited()) {
			if(unvisitedTileData.getDistance() < closestTile.getDistance()) {
				closestTile = unvisitedTileData;
				break;
			}	
		}
		return closestTile.getCoords();
	}

	public void appendVisited(VisitedTileData visitedTileData) {
		visited.add(visitedTileData);
	}
	
	public void appendUnvisited(UnvisitedTileData unvisitedTileData) {
		unvisited.add(unvisitedTileData);
	}
	
	public void appendBlackTiles(Coords coord) {
		blackTiles.add(coord);
	}
	
	public void setUnvisited(Coords coords, int distance) {
		for(UnvisitedTileData unvisitedTileData : unvisited) {
			if(unvisitedTileData.getCoords().equals(coords)) {
				unvisitedTileData = new UnvisitedTileData(coords, distance);
				return;
			}
		}
	}
	
	public void setUnvisited(int x, int y, int distance) {
		Coords coords = new Coords(x, y);
		for(UnvisitedTileData unvisitedTileData : unvisited) {
			if(unvisitedTileData.getCoords().equals(coords)) {
				unvisitedTileData = new UnvisitedTileData(coords, distance);
				return;
			}
		}
	}
	
	public void removeUnvisited(Coords coords) {
		for(UnvisitedTileData unvisitedTileData : unvisited) {
			if(unvisitedTileData.getCoords().equals(coords)) {
				unvisited.remove(unvisitedTileData);
				return;
			}
		}
	}
	
	public void removeUnvisited(int x, int y) {
		for(UnvisitedTileData unvisitedTileData : unvisited) {
			if(unvisitedTileData.getCoords().equals(new Coords(x, y))) {
				unvisited.remove(unvisitedTileData);
				return;
			}
		}
	}
	
	public boolean isSuperSetOfUnvisited(HashSet<Coords> superSet) {
		ArrayList<String> unvisitedSigs = new ArrayList<String>();
		ArrayList<String> superSetSigs = new ArrayList<String>();
		
		for(Coords coord : getUnvisitedCoords()) {
			unvisitedSigs.add(coord.getSignature());
		}
		
		for(Coords coord : superSet) {
			superSetSigs.add(coord.getSignature());
		}
		
		return superSetSigs.containsAll(unvisitedSigs);
	}
	
	public void setLastSilverTile(Coords coords) {
		lastSilverTile.set(coords);
	}
	
	public void setLastSilverTile(int x, int y) {
		lastSilverTile.set(x, y);
	}
	
	public void setCurrentPos(Coords coords) {
		currentPos.set(coords);
	}
	
	public void setCurrentPos(int x, int y) {
		currentPos.set(x, y);
	}
	
	public void setRampTile(Coords coords) {
		rampTile.set(coords);
	}
	
	public void setRampTile(int x, int y) {
		rampTile.set(x, y);
	}
	
	public void setRampDir(String direction) {
		rampDir = direction;
	}
}
