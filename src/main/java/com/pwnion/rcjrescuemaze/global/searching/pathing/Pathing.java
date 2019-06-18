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
	
	private Set<Coords> viableSurroundingCoords(Coords coords) {
		HashMap<Coords, Integer> viableCoords = new HashMap<Coords, Integer>() {
			private static final long serialVersionUID = 1L;
			{
				put(new Coords(coords.getX() + 1, coords.getY()), 1);
				put(new Coords(coords.getX() - 1, coords.getY()), 3);
				put(new Coords(coords.getX(), coords.getY() + 1), 2);
				put(new Coords(coords.getX(), coords.getY() - 1), 0);
			}
		};
		
		ArrayList<Coords> unviableCoords = new ArrayList<Coords>();
		ArrayList<Coords> visitedCoords = new ArrayList<Coords>();
		ArrayList<ArrayList<Boolean>> visitedWalls = new ArrayList<ArrayList<Boolean>>();
		
		for(VisitedTileData visitedTileData : sharedData.getVisited()) {
			visitedCoords.add(visitedTileData.getCoords());
			visitedWalls.add(visitedTileData.getWalls());
		}
		
		int counter = 0;
		for(Coords coord : viableCoords.keySet()) {
			if(!visitedCoords.contains(coord) || visitedWalls.get(counter).get(viableCoords.get(coord))) {
				unviableCoords.add(coord);
			}
			counter++;
		}
		
		for(Coords coord : unviableCoords) {
			if(viableCoords.containsKey(coord)) {
				viableCoords.remove(coord);
			}
		}
		
		return viableCoords.keySet();
	}
	
	public HashMap<Coords, Integer> generatePath(Coords coords) {
		HashMap<Coords, Integer> map = new HashMap<Coords, Integer>();
		HashSet<Coords> previousViableCoords = new HashSet<Coords>(Arrays.asList(coords));
		int counter = 0;
		do {
			HashSet<Coords> combinedCoords = new HashSet<Coords>();
			for(Coords coord : previousViableCoords) {
				combinedCoords.addAll(viableSurroundingCoords(coord));
			}
			
			for(Coords coord : combinedCoords) {
				map.put(coord, counter);
			}
			
			if(combinedCoords.contains(sharedData.getCurrentPos())) return map;
			
			previousViableCoords = combinedCoords;
			
			counter++;
		} while(true);
	}

	public void moveToCoords(Coords coords) {//{Function: Move using [Path]
		HashMap<Coords, Integer> path = generatePath(coords);
		while(sharedData.getCurrentPos() != coords) {
			
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
				{
					add("up");
					add("left");
					add("down");
					add("right");
				}
			};
			
			String direction = "";
			int smallestNumber = 100;
			for(int i = 0; i < 4; i++) {
				int tileValue = path.get(surroundingCoords.get(i));
				if(tileValue < smallestNumber) {
					smallestNumber = tileValue;
					direction = directions.get(i);
				}
			}
			
			drivingMotors.move(direction);
			
			//Update current position
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
