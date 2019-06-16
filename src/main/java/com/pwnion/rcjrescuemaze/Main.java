package com.pwnion.rcjrescuemaze;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.pwnion.rcjrescuemaze.global.Survivors;
import com.pwnion.rcjrescuemaze.global.searching.Searching;
import com.pwnion.rcjrescuemaze.global.searching.pathing.Pathing;

public class Main {
	@Inject
	private static SharedData sharedData;
	
	@Inject
	private static Pathing pathing;
	
	@Inject
	private static Searching searching;
	
	@Inject
	private static Survivors survivors;
	
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new MainBinder());

		sharedData = injector.getInstance(SharedData.class);
		pathing = injector.getInstance(Pathing.class);
		searching = injector.getInstance(Searching.class);
		survivors = injector.getInstance(Survivors.class);
		
		while(sharedData.getUnvisited().size() > 0) {
		//While there are unvisited tiles
		//Call upon searching function to find and move to next tile
		searching.findMoveUnvisited();
		
		//Move current tile to visited
		//Search adjacent tiles to find obstacles, walls and unvisited tiles
		//Add any found items to respective lists
		//Calculate any new corners found and add to list
		
		//Calculate distance to unvisited tiles and update each with new distance value (Uses Pathing.java functions)
		for (UnvisitedTileData unvisitedTile : sharedData.getUnvisited()) {
			unvisitedTile.setDistance(pathing.generatePath(unvisitedTile.getCoords()).size());
		}

		//Detect for any problems in orientation or position (Mainly checks any information that may have been logged during Pathing)

		//Call upon Survivors function to search for any survivors and detect them

		}
		
		if (true) {//If user interaction
			//Reset current position to last silver tile
			sharedData.setCurrentPos(sharedData.getLastSilverTile());
		}
	}
}
