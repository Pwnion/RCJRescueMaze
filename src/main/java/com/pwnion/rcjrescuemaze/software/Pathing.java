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
		
		//Directions mapped to relative coordinates for the surrounding tiles
		HashMap<Integer, Coords> coordsToAdd = new HashMap<Integer, Coords>() {
			private static final long serialVersionUID = 1L;
			{
				put(0, new Coords(0, 1));
				put(1, new Coords(-1, 0));
				put(2, new Coords(0, -1));
				put(3, new Coords(1, 0));
			}
		};

		//Adds the coords surrounding the given coords that don't have a wall obstruction
		int visitedIndex = sharedData.getVisitedIndex(coords);
		
		if(visitedIndex != -1) {
			Coords nextTile;
			for(int i = 0; i < 4; i++) {
				nextTile = coords.plus(coordsToAdd.get(i));
				System.out.println("nextTile viableSurroundingCoords = " + nextTile);
				if(!sharedData.getVisitedWalls().get(visitedIndex).get(i) && !sharedData.getBlackTiles().contains(nextTile)) {
					viableSurroundingCoords.add(nextTile);
				}
			}
		}
		
		System.out.println("Adding viableSurroundingCoords: " + viableSurroundingCoords + " from " + coords);
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
		System.out.println(" **Unvisited Coords: " + sharedData.getUnvisitedCoords());
		System.out.println(" **Visited Coords: " + sharedData.getVisitedCoords());

		HashSet<Coords> combinedCoords = new HashSet<Coords>();
		
		int counter = 1;
		do {
			/*
			Add all coords previously determined to be viable to a hashset,
			beginning with the coords of the robot
			*/
			HashSet<Coords> viableCoords = new HashSet<Coords>();
			for(Coords coord : previousViableCoords) {
				
				viableCoords.addAll(viableSurroundingCoords(coord));
			}
			
			combinedCoords.addAll(viableCoords);
			
			/*
			Put all viable coords as keys in a hashmap, where the values are the
			distances between the coords of the robot and the viable coord in question
			*/ 
			for(Coords coord : viableCoords) {
				map.put(coord, counter);
			}
			
			previousViableCoords = viableCoords;
			
			//When the set of unvisited coords becomes a subset of combined coords, return the map
			if(sharedData.isSuperSetOfUnvisited(combinedCoords)) {
				return map;
			}

			counter++;
		} while(true);
	}
	
	protected ArrayList<String> generatePath(HashMap<Coords, Integer> map, Coords coords) {
		HashMap<String, Integer> mapStr = new HashMap<String, Integer>();
		
		for(Coords coord : map.keySet()) {
			mapStr.put(coord.toString(), map.get(coord));
		}
		
		ArrayList<String> path = new ArrayList<String>();
		Coords currentCoords = new Coords(coords);
		final int pathLength = mapStr.get(coords.toString());
		
		System.out.println("      GENERATING PATH TO " + coords.toString() + " estimated path length of: " + pathLength + "     *****************************");
		
		for(int i = 0; i < pathLength; i++) {
			
			
			ArrayList<Coords> surroundingCoords = currentCoords.surrounding();

			ArrayList<String> directions = new ArrayList<String>() {
				private static final long serialVersionUID = 1L;
				{
					add("up");
					add("left");
					add("down");
					add("right");
				}
			};
			
			System.out.println("Generating Path of " + currentCoords.toString());
			
			/*
			From the coords surrounding the robot, obtain the tile that has
			the largest distance value and interpret which direction that is in
			*/
			for(int j = 0; j < 4; j++) {
				
				System.out.println("Map of " + surroundingCoords.get(j).toString() + " = " + mapStr.get(surroundingCoords.get(j).toString()));
				
				if(mapStr.get(surroundingCoords.get(j).toString()) != null) {
					int tileValue = mapStr.get(surroundingCoords.get(j).toString());
					
					System.out.println("Searching for tileValue: " + tileValue);
					
					if(tileValue == mapStr.get(currentCoords.toString()) - 1) {
						path.add(directions.get(j));
						currentCoords = new Coords(surroundingCoords.get(j));
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
