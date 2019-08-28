package com.pwnion.rcjrescuemaze.software;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

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
	
	private String lastMoveWallLocation = "back";
	
	private int time = 0;
	ArrayList<String> fullPath = new ArrayList<String>();
	
	private HashMap<String, Integer> readAvgColours(String path) throws IOException {
		ArrayList<Integer> avgColours = new ArrayList<Integer>();
		 
		try (Scanner s = new Scanner(new FileReader(path))) {
		    while (s.hasNext()) {
		        avgColours.add(Integer.parseInt(s.nextLine()));
		    }
		}
		
		return new HashMap<String, Integer>() {
			private static final long serialVersionUID = 1L;
			{
				put("Red", avgColours.get(0));
				put("Green", avgColours.get(1));
				put("Blue", avgColours.get(2));
			}
		};
	}
	
	public HashMap<String, HashMap<String, Integer>> getTileValues() throws IOException {
		return new HashMap<String, HashMap<String, Integer>>()
		{
			private static final long serialVersionUID = 1L;
			{
				put("Silver", new HashMap<String, Integer>() {
					private static final long serialVersionUID = 1L;
					HashMap<String, Integer> silver = readAvgColours("/home/pi/tiles/silver.txt");
					{
						put("Red", silver.get("Red"));
						put("Green", silver.get("Green"));
						put("Blue", silver.get("Blue"));
					}
				});
			}
			{
				put("Black", new HashMap<String, Integer>() {
					private static final long serialVersionUID = 1L;
					HashMap<String, Integer> black = readAvgColours("/home/pi/tiles/black.txt");
					{
						put("Red", black.get("Red"));
						put("Green", black.get("Green"));
						put("Blue", black.get("Blue"));
					}
				});
			}
			{
				put("White", new HashMap<String, Integer>() {
					private static final long serialVersionUID = 1L;
					HashMap<String, Integer> white = readAvgColours("/home/pi/tiles/white.txt");
					{
						put("Red", white.get("Red"));
						put("Green", white.get("Green"));
						put("Blue", white.get("Blue"));
					}
				});
			}
			{
				put("Red", new HashMap<String, Integer>() {
					private static final long serialVersionUID = 1L;
					HashMap<String, Integer> red = readAvgColours("/home/pi/tiles/red.txt");
					{
						put("Red", red.get("Red"));
						put("Green", red.get("Green"));
						put("Blue", red.get("Blue"));
					}
				});
			}
			{
				put("Green", new HashMap<String, Integer>() {
					private static final long serialVersionUID = 1L;
					HashMap<String, Integer> green = readAvgColours("/home/pi/tiles/green.txt");
					{
						put("Red", green.get("Red"));
						put("Green", green.get("Green"));
						put("Blue", green.get("Blue"));
					}
				});
			}
			{
				put("Blue", new HashMap<String, Integer>() {
					private static final long serialVersionUID = 1L;
					HashMap<String, Integer> blue = readAvgColours("/home/pi/tiles/blue.txt");
					{
						put("Red", blue.get("Red"));
						put("Green", blue.get("Green"));
						put("Blue", blue.get("Blue"));
					}
				});
			}
			{
				put("Yellow", new HashMap<String, Integer>() {
					private static final long serialVersionUID = 1L;
					HashMap<String, Integer> yellow = readAvgColours("/home/pi/tiles/yellow.txt");
					{
						put("Red", yellow.get("Red"));
						put("Green", yellow.get("Green"));
						put("Blue", yellow.get("Blue"));
					}
				});
			}
		};
	}

	ArrayList<String> positions = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("front");
			add("left");
			add("back");
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
				lastMoveWallLocation = "back";
				break;
			case "left":
				lastMoveWallLocation = "right";
				break;
			case "down":
				lastMoveWallLocation = "front";
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
