package com.pwnion.rcjrescuemaze;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
	
	//LIST VISITED: (Location, Distance, 4 bits for walls, 1 bit for Corner, 1 bit for Silver)
	ArrayList<HashMap<Integer, Integer>, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean> visited;
	
	//LIST UNVISTED: (Location)
	ArrayList<HashMap<Integer, Integer>> unvisited;
	
	//LAST SILVER TILE: (Location)
	HashMap<Integer, Integer> lastSilverTile;

	public static void main(String[] args) {
		//This is probably important
		
		while (unvisited.length > 0) {//While there are unvisited tiles
		//Call upon searching function to find and move to next tile
		findMoveUnvisited();
		
		//Move current tile to visited
		//Search adjacent tiles to find obstacles, walls and unvisited tiles
		//Add any found items to respective lists
		//Calculate any new corners found and add to list
		
		//Calculate distance to unvisited tiles and update each with new distance value (Uses Pathing.java functions)

		//Detect for any problems in orientation or position (Mainly checks any information that may have been logged during Pathing)

		//Call upon Survivors function to search for any survivors and detect them

		}
		
		if () {//If user interaction
			//Reset current position to last silver tile
			currentPosition = lastSilverTile;
		}
	}
}
