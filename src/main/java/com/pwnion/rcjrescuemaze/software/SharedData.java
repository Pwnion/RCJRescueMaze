package com.pwnion.rcjrescuemaze.software;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import com.google.inject.Singleton;
import com.pwnion.rcjrescuemaze.datatypes.Coords;
import com.pwnion.rcjrescuemaze.datatypes.UnvisitedTileData;
import com.pwnion.rcjrescuemaze.datatypes.VisitedTileData;

@Singleton
public class SharedData implements Serializable {
	private static final long serialVersionUID = 1L;

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
	private Coords rampTile = null;
	private String rampDir = "";
	
	private String lastMoveWallLocation = "down";
	
	private int time = 0;
	ArrayList<String> fullPath = new ArrayList<String>();
	
	public void clear() {
		visited = new ArrayList<VisitedTileData>();
		unvisited = new ArrayList<UnvisitedTileData>();
		blackTiles = new ArrayList<Coords>();
		lastSilverTile = new Coords(0, 0);
		currentPos = new Coords(0, 0);
		rampTile = new Coords(0, 0);
		rampDir = "";
		time = 0;
	}
	
	private final int imgX = 32;
	private final int imgY = 32;
	
	private int[][][] readRGBValues(String path) throws IOException {
		int[][][] rgbValues = new int[imgX][imgY][3];
		 
		try (Scanner s = new Scanner(new FileReader(path))) {
		    for(int y = 0; y < imgY; y++) {
				for(int x = 0; x < imgX; x++) {
					for(int i = 0; i < 3; i++) {
						rgbValues[x][y][i] = s.nextInt();
					}
				}
			}
		}
		return rgbValues;
	}
	
	public HashMap<String, int[][][]> getTileValues() throws IOException {
		return new HashMap<String, int[][][]>()
		{
			private static final long serialVersionUID = 1L;
			{
				put("silver", readRGBValues("/home/pi/tiles/silver.txt"));
				put("black", readRGBValues("/home/pi/tiles/black.txt"));
				put("white", readRGBValues("/home/pi/tiles/white.txt"));
				put("red", readRGBValues("/home/pi/tiles/red.txt"));
				put("green", readRGBValues("/home/pi/tiles/green.txt"));
				put("blue", readRGBValues("/home/pi/tiles/blue.txt"));
				put("yellow", readRGBValues("/home/pi/tiles/yellow.txt"));
			}
		};
	}

	ArrayList<String> positions = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("up");
			add("left");
			add("down");
			add("right");
		}
	};
	
	public ArrayList<VisitedTileData> getVisited() {
		return visited;
	}
	
	public ArrayList<UnvisitedTileData> getUnvisited() {
		return unvisited;
	}
	
	public ArrayList<Coords> getBlackTiles() {
		return blackTiles;
	}
	
	public boolean blackTilesContains(Coords coord) {
		if(blackTiles.contains(coord)) {
			return true;
		}
		return false;
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
			if(visited.get(i).getCoords().toString().equals(coords.toString())) {
				return i;
			}
		}
		return -1;
	}

	public int getUnvisitedIndex(Coords coords) {
		for(int i = 0; i < unvisited.size(); i++) {
			if(unvisited.get(i).getCoords().toString().equals(coords.toString())) {
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
	
	public boolean visitedCoordsContains(Coords coord) {
		for(Coords coords : getVisitedCoords()) {
			if(coords.equals(coord)) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<HashMap<String, Boolean>> getVisitedWalls() {
		ArrayList<HashMap<String, Boolean>> visitedWalls = new ArrayList<HashMap<String, Boolean>>();
		for(VisitedTileData visitedTileData : visited) {
			visitedWalls.add(visitedTileData.getWalls());
		}
		return visitedWalls;
	}
	
	public HashMap<String, Boolean> getWallsFromVisited(Coords coord) {
		return getVisitedWalls().get(getVisitedIndex(coord));
	}
	
	public boolean getWallsFromVisited(Coords coord, String position) {
		return getWallsFromVisited(coord).get(position);
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
			}	
		}
		return closestTile.getCoords();
	}

	public void appendVisited(VisitedTileData visitedTileData) {
		if(getVisitedIndex(visitedTileData.getCoords()) == -1) {
			visited.add(visitedTileData);
		}
	}
	
	public void appendUnvisited(UnvisitedTileData unvisitedTileData) {
		if(getUnvisitedIndex(unvisitedTileData.getCoords()) == -1 && getVisitedIndex(unvisitedTileData.getCoords()) == -1) {
			unvisited.add(unvisitedTileData);
		}
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
	
	public void addWallForCurrentVisited(String direction) {
		getVisited().get(getVisitedIndex(getCurrentPos())).getWalls().put(direction, true);
	}
	
	public void removeUnvisited(Coords coords) {
		for(UnvisitedTileData unvisitedTileData : unvisited) {
			if(unvisitedTileData.getCoords().toString().equals(coords.toString())) {
				unvisited.remove(unvisitedTileData);
				return;
			}
		}
	}
	
	public void removeUnvisited(int x, int y) {
		for(UnvisitedTileData unvisitedTileData : unvisited) {
			if(unvisitedTileData.getCoords().toString().equals(new Coords(x, y).toString())) {
				unvisited.remove(unvisitedTileData);
				return;
			}
		}
	}
	
	public boolean isSuperSetOfUnvisited(HashSet<Coords> superSet) {
		ArrayList<String> unvisitedStrs = new ArrayList<String>();
		ArrayList<String> superSetStrs = new ArrayList<String>();
		
		for(Coords coord : getUnvisitedCoords()) {
			unvisitedStrs.add(coord.toString());
		}
		
		for(Coords coord : superSet) {
			superSetStrs.add(coord.toString());
		}
		
		return superSetStrs.containsAll(unvisitedStrs);
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

	public int getTime() {
		return time;
	}
	
	public void timeAdd(int time) {
		this.time += time;
	}
	
	public ArrayList<String> getFullPath() {
		return fullPath;
	}
	
	public void pathAppend(String direction) {
		fullPath.add(direction);
	}
	
	public ArrayList<String> getPositions() {
		return positions;
	}
	
	public String getPositions(int i) {
		return positions.get(i);
	}
	
	public void updateLastMoveWallLocation(String direction) {
		switch (direction) {
			case "up":
				lastMoveWallLocation = "down";
				break;
			case "left":
				lastMoveWallLocation = "right";
				break;
			case "down":
				lastMoveWallLocation = "up";
				break;
			case "right":
				lastMoveWallLocation = "left";
				break;
		}
	}
	
	public String getLastMoveWallLocation() {
		return lastMoveWallLocation;
	}
	
}
