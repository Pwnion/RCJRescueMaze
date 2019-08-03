package com.pwnion.rcjrescuemaze.software;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import com.google.inject.Inject;
import com.pwnion.rcjrescuemaze.datatypes.Coords;

public abstract class Pathing {
	
	protected final SharedData1 sharedData;
	
	@Inject
	protected Pathing(SharedData1 sharedData) {
		this.sharedData = sharedData;
	}
	
	/*
	Returns a set of coords surrounding the given coords that are viable, meaning
	that there isn't a wall between the given coords and the surrounding coords
	*/
	private HashSet<Coords> viableSurroundingCoords(Coords coords) {
		HashSet<Coords> viableSurroundingCoords = new HashSet<Coords>();
		
		System.out.println("Running viableSurroundingCoords(" + coords.getX() + ", " + coords.getY() + ")");
		
		//Directions mapped to relative coordinates for the surrounding tiles
		HashMap<Integer, int[]> coordsToAdd = new HashMap<Integer, int[]>() {
			private static final long serialVersionUID = 1L;
			{
				put(0, new int[] {0, 1});
				put(1, new int[] {-1, 0});
				put(2, new int[] {0, -1});
				put(3, new int[] {1, 0});
			}
		};

		//Adds the coords surrounding the given coords that don't have a wall obstruction
		int visitedIndex = sharedData.getVisitedCoords().indexOf(coords);
		System.out.println("Visited Coords, " + sharedData.getVisitedCoords() + " of Size, " + sharedData.getVisitedCoords().size());
		
		System.out.println("visitedIndex = " + visitedIndex);
		
		if(visitedIndex != -1) {
			
			System.out.println("visitedIndex != -1");
			
			Coords nextTile;
			for(int i = 0; i < 4; i++) {
				nextTile = new Coords(coords.getX() + coordsToAdd.get(i)[0], coords.getY() + coordsToAdd.get(i)[1]);
				if(!sharedData.getVisitedWalls().get(visitedIndex).get(i) && !sharedData.getBlackTiles().contains(nextTile)) {
					System.out.println("viableSurroundingCoords, Adding Next Tile");
					viableSurroundingCoords.add(nextTile);
				}
			}
		}

		//Return the coords surrounding the given coords that don't have a wall obstruction
		return viableSurroundingCoords;
	}
	
	/*
	Returns a hashmap that associates a set of viable coords with a number that represents the number of
	tiles the robot would have to move to get from the given coords to the coord in the hashmap
	The set of coords is populated until one of the coords is equal to the coords of the robots current position
	This creates a path that the robot can follow by always moving toward a surrounding coordinate associated with a 
	number that is lower than the one it is on currently
	*/
	public HashMap<Coords, Integer> generateMap() {
		HashMap<Coords, Integer> map = new HashMap<Coords, Integer>() {
			private static final long serialVersionUID = 1L;

			{
				put(sharedData.getCurrentPos(), 0);
			}
		};
		HashSet<Coords> previousViableCoords = new HashSet<Coords>(Arrays.asList(sharedData.getCurrentPos()));
		
		System.out.println("Generating Map");
		
		for(Coords coord : sharedData.getUnvisitedCoords()) {
			System.out.println(" **Unvisited Coord: [" + coord.getX() + ", " + coord.getY() + "]");
		}
		
		for(Coords coord : sharedData.getVisitedCoords()) {
			System.out.println(" **Visited Coord: [" + coord.getX() + ", " + coord.getY() + "]");
		}

		int counter = 1;
		do {
			HashSet<Coords> combinedCoords = new HashSet<Coords>();
			/*
			Add all coords previously determined to be viable to a hashset,
			beginning with the coords of the robot
			*/
			for(Coords coord : previousViableCoords) {
				combinedCoords.addAll(viableSurroundingCoords(coord));
			}
			
			/*
			Put all viable coords as keys in a hashmap, where the values are the
			distances between the coords of the robot and the viable coord in question
			*/ 
			for(Coords coord : combinedCoords) {
				System.out.println(" Unvisited Coords Size = " + sharedData.getUnvisited().size());
				System.out.println("  Combined Coord, " + coord.getX() + "," + coord.getY());
				map.put(coord, counter);
			}
			
			
			//When the set of unvisited coords becomes a subset of combined coords, return the map
			if(sharedData.isSuperSetOfUnvisited(combinedCoords)) {
				System.out.println("map: " + map.values() + " of size, " + map.values().size());
				return map;
			}
			
			previousViableCoords = combinedCoords;
			
			counter++;
		} while(true);
	}
	
	protected ArrayList<String> generatePath(HashMap<Coords, Integer> map, Coords coords) {
		HashMap<String, Integer> mapSig = new HashMap<String, Integer>();
		
		for(Coords coord : map.keySet()) {
			mapSig.put(coord.getSignature(), map.get(coord));
		}
		
		System.out.println("mapSig.get(coords.getSignature()): " + mapSig.get(coords.getSignature()));
		ArrayList<String> path = new ArrayList<String>();
		for(int i = 0; i == mapSig.get(coords.getSignature()) - 1; i++) {
			
			ArrayList<Coords> surroundingCoords = new ArrayList<Coords>() {
				private static final long serialVersionUID = 1L;
				{
					add(new Coords(coords.getX() + 1, coords.getY()));
					add(new Coords(coords.getX() - 1, coords.getY()));
					add(new Coords(coords.getX(), coords.getY() + 1));
					add(new Coords(coords.getX(), coords.getY() - 1));
				}
			};

			ArrayList<String> directions = new ArrayList<String>() {
				private static final long serialVersionUID = 1L;
				{
					add("up");
					add("left");
					add("down");
					add("right");
				}
			};
			
			System.out.println("Generating Path of [" + coords.getX() + "," + coords.getY() + "]");
			
			/*
			From the coords surrounding the robot, obtain the tile that has
			the largest distance value and interpret which direction that is in
			*/
			for(int j = 0; j < 4; j++) {
				
				System.out.println("Map of [" + surroundingCoords.get(j).getX() + "," + surroundingCoords.get(j).getY() + "] = " + map.get(surroundingCoords.get(j)));
				
				if(mapSig.get(surroundingCoords.get(j).getSignature()) != null) {
					int tileValue = mapSig.get(surroundingCoords.get(j).getSignature());
					
					System.out.println("tileValue: " + tileValue);
					
					if(tileValue == mapSig.get(coords.getSignature()) - 1) {
						path.add(directions.get(j));
						break;
					}
				} 
			}
		}
		return path;
	}

	//Abstract implementation
	public abstract void moveToCoords(Coords coords);
}
