package com.pwnion.rcjrescuemaze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main implements SharedData {
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new MainBinder());

		//This is probably important
		
		while (unvisited.size() > 0) {//While there are unvisited tiles
		//Call upon searching function to find and move to next tile
		findMoveUnvisited();
		
		//Move current tile to visited
		//Search adjacent tiles to find obstacles, walls and unvisited tiles
		//Add any found items to respective lists
		//Calculate any new corners found and add to list
		
		//Calculate distance to unvisited tiles and update each with new distance value (Uses Pathing.java functions)
		for (Integer coords: unvisited) {
			
		}

		//Detect for any problems in orientation or position (Mainly checks any information that may have been logged during Pathing)

		//Call upon Survivors function to search for any survivors and detect them

		}
		
		if () {//If user interaction
			//Reset current position to last silver tile
			currentPosition = lastSilverTile;
		}
	}
}
