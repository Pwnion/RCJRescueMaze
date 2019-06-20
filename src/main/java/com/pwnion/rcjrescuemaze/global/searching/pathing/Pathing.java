package com.pwnion.rcjrescuemaze.global.searching.pathing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;
import com.pwnion.rcjrescuemaze.Coords;
import com.pwnion.rcjrescuemaze.SharedData;
import com.pwnion.rcjrescuemaze.VisitedTileData;
import com.pwnion.rcjrescuemaze.global.searching.pathing.drivers.DrivingMotors;

public class Pathing {
	
	private final SharedData sharedData;
	private final DrivingMotors drivingMotors;
	
	@Inject
	Pathing(SharedData sharedData, DrivingMotors drivingMotors) {
		this.sharedData = sharedData;
		this.drivingMotors = drivingMotors;
	}
	
	/*
	Returns a set of coords surrounding the given coords that are viable, meaning
	that they are both visited, and there isn't a wall between the given coords and the
	surrounding coords
	*/
	private Set<Coords> viableSurroundingCoords(Coords coords) {
		/*
		Stores the coords that surround the coord given as keys
		in the hashmap, where the values are the index of the wall position
		between the given coordinate and the surrounding coord in question
		*/
		HashMap<Coords, Integer> viableCoords = new HashMap<Coords, Integer>() {
			private static final long serialVersionUID = 1L;
			{
				put(new Coords(coords.getX() + 1, coords.getY()), 3);
				put(new Coords(coords.getX() - 1, coords.getY()), 1);
				put(new Coords(coords.getX(), coords.getY() + 1), 0);
				put(new Coords(coords.getX(), coords.getY() - 1), 2);
			}
		};
		
		ArrayList<Coords> unviableCoords = new ArrayList<Coords>();
		ArrayList<Coords> visitedCoords = new ArrayList<Coords>();
		ArrayList<ArrayList<Boolean>> visitedWalls = new ArrayList<ArrayList<Boolean>>();
		
		/*
		Generate one list for the coordinates of all visited walls, and
		one for the wall positions of each visited tile
		*/
		for(VisitedTileData visitedTileData : sharedData.getVisited()) {
			visitedCoords.add(visitedTileData.getCoords());
			visitedWalls.add(visitedTileData.getWalls());
		}
		
		//Add unviable coords to a list
		for(Coords coord : viableCoords.keySet()) {
			if(visitedWalls.get(visitedCoords.indexOf(coords)).get(viableCoords.get(coord))) {
				unviableCoords.add(coord);
			} else if(!visitedCoords.contains(coord)) {
				if(!coord.equals(coords)) {
					unviableCoords.add(coord);
				}
			}
		}
		
		//Replaces code block above
		for(int i = 0; i < 4; i++) {
			if(!visitedWalls.get(visitedCoords.indexOf(coords)).get(i)) {
				switch(i) {
					case 0:
						//viableCoords.add(new Coords(coords.getX, coords.getY + 1));
						break;
				}
			}
		}

		//{loop 4 times
		//if coords does not have wall in counter direction
		//(switch statement checking index of wall location and giving coord of tile behind)
		//add that coord to viable tiles
		
		//Filter viable coords with the unviable coords list
		unviableCoords.forEach((coord) -> viableCoords.remove(coord));
		
		//Return viable coords
		return viableCoords.keySet();
	}
	
	/*
	Returns a hashmap that associates a set of viable coords with a number that represents the number of
	tiles the robot would have to move to get from the given coords to the coord in the hashmap
	The set of coords is populated until one of the coords is equal to the coords of the robots current position
	This creates a path that the robot can follow by always moving toward a surrounding coordinate associated with a 
	number that is lower than the one it is on currently
	*/
	public HashMap<Coords, Integer> generatePath(Coords coords) {
		HashMap<Coords, Integer> map = new HashMap<Coords, Integer>() {
			private static final long serialVersionUID = 1L;
			{
				put(sharedData.getCurrentPos(), 0);
			}
		};
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
			
			/*
			When one of the viable coords equals the coords given, return the hashmap
			of coords and distances
			*/
			if(combinedCoords.contains(coords)) return map;
			
			previousViableCoords = combinedCoords;
			
			counter++;
		} while(true);
	}

	/*
	Moves the robot along the path generated by the generatePath() function, and
	terminates when the robot gets to the given coords
	*/
	public void moveToCoords(Coords coords) {
		HashMap<Coords, Integer> path = generatePath(coords);
		while(sharedData.getCurrentPos() != coords) {
			ArrayList<Coords> surroundingCoords = new ArrayList<Coords>() {
				private static final long serialVersionUID = 1L;
				{
					add(new Coords(sharedData.getCurrentPos().getX() + 1, sharedData.getCurrentPos().getY()));
					add(new Coords(sharedData.getCurrentPos().getX() - 1, sharedData.getCurrentPos().getY()));
					add(new Coords(sharedData.getCurrentPos().getX(), sharedData.getCurrentPos().getY() + 1));
					add(new Coords(sharedData.getCurrentPos().getX(), sharedData.getCurrentPos().getY() - 1));
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
			the largest distance value and intepret which direction that is in
			*/
			String direction = "";
			int largestNumber = 0;
			for(int i = 0; i < 4; i++) {
				int tileValue = path.get(surroundingCoords.get(i));
				if(tileValue > largestNumber) {
					largestNumber = tileValue;
					direction = directions.get(i);
				}
			}
			
			//Move one tile in the correct direction determined
			drivingMotors.move(direction);
			
			//Update current position of the robot
			Coords newPos = sharedData.getCurrentPos();
			switch(direction) {
			  case "up":
				newPos.addY(1);
			    break;
			  case "down":
			    newPos.addY(-1);
			    break;
			  case "left":
				newPos.addX(-1);
				break;
			  case "right":
				newPos.addX(1);
				break;
			}
			sharedData.setCurrentPos(newPos);

			//Log any discrepancies with rotation or position 
			//If over tolerance levels repathing may be required

			//After moving 1 tile check for Silver tiles and update last visited silver (if required)
			//(This process does not happen physically and just checks Silver tile list vs Current Position)
			if (sharedData.getCurrentPos().equals(sharedData.getLastSilverTile())) {
				sharedData.setLastSilverTile(sharedData.getCurrentPos());
			}

		}//Repeat until end of path
	}
}
