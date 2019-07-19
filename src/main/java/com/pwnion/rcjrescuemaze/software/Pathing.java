package com.pwnion.rcjrescuemaze.software;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.pwnion.rcjrescuemaze.datatypes.Coords;
import com.pwnion.rcjrescuemaze.hardware.DrivingMotors;

public abstract class Pathing {
	
	protected final SharedData sharedData;
	protected final DrivingMotors drivingMotors;
	
	@Inject
	protected Pathing(SharedData sharedData, DrivingMotors drivingMotors) {
		this.sharedData = sharedData;
		this.drivingMotors = drivingMotors;
	}
	
	/*
	Returns a set of coords surrounding the given coords that are viable, meaning
	that there isn't a wall between the given coords and the surrounding coords
	*/
	private HashSet<Coords> viableSurroundingCoords(Coords coords) {
		HashSet<Coords> viableSurroundingCoords = new HashSet<Coords>();
		
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
		if(visitedIndex != -1) {
			Coords nextTile;
			for(int i = 0; i < 4; i++) {
				nextTile = new Coords(coords.getX() + coordsToAdd.get(i)[0], coords.getY() + coordsToAdd.get(i)[1]);
				if(!sharedData.getVisitedWalls().get(visitedIndex).get(i) && !sharedData.getBlackTiles().contains(nextTile)) {
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
		HashMap<Coords, Integer> map = new HashMap<Coords, Integer>();
		HashSet<Coords> previousViableCoords = new HashSet<Coords>(Arrays.asList(sharedData.getCurrentPos()));

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
				map.put(coord, counter);
			}
			
			//When the set of unvisited coords becomes a subset of combined coords, return the map
			if(combinedCoords.containsAll(sharedData.getUnvisitedCoords().stream().collect(Collectors.toSet()))) return map;
			
			previousViableCoords = combinedCoords;
			
			counter++;
		} while(true);
	}

	protected ArrayList<String> generatePath(HashMap<Coords, Integer> map, Coords coords) {
		ArrayList<String> path = new ArrayList<String>();
		for(int i = 0; i == map.get(coords) - 1; i++) {
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
			
			/*
			From the coords surrounding the robot, obtain the tile that has
			the largest distance value and interpret which direction that is in
			*/
			for(int j = 0; j < 4; j++) {
				int tileValue = map.get(surroundingCoords.get(j));
				if(tileValue == map.get(sharedData.getCurrentPos()) - 1) {
					path.add(directions.get(j));
					break;
				}
			}
		}
		return path;
	}

	//Abstract implementation
	public abstract void moveToCoords(Coords coords);
}
