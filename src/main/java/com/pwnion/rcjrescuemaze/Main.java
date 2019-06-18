package com.pwnion.rcjrescuemaze;

import java.util.ArrayList;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.pwnion.rcjrescuemaze.global.Survivors;
import com.pwnion.rcjrescuemaze.global.searching.FindWalls;
import com.pwnion.rcjrescuemaze.global.searching.Searching;
import com.pwnion.rcjrescuemaze.global.searching.pathing.Pathing;
import com.pwnion.rcjrescuemaze.global.searching.pathing.drivers.Colour;

public class Main {
	@Inject
	private static SharedData sharedData;
	
	@Inject
	private static Pathing pathing;
	
	@Inject
	private static Searching searching;
	
	@Inject
	private static Survivors survivors;
	
	@Inject
	private static FindWalls findWalls;
	
	@Inject
	private static Colour colour;
	
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new MainBinder());

		sharedData = injector.getInstance(SharedData.class);
		pathing = injector.getInstance(Pathing.class);
		searching = injector.getInstance(Searching.class);
		survivors = injector.getInstance(Survivors.class);
		findWalls = injector.getInstance(FindWalls.class);
		colour = injector.getInstance(Colour.class);
		
		//While there are unvisited tiles
		while(sharedData.getUnvisited().size() > 0) {
		
			//Call upon searching function to find and move to next tile
			searching.findMoveUnvisited();
		
			//Remove current tile from unvisited
			sharedData.removeUnvisited(sharedData.getCurrentPos());
		
			//Search adjacent tiles to find obstacles, walls and unvisited tiles
			ArrayList<Boolean> walls = findWalls.find();
		
			//Calculate any new corners found and add to list
			boolean corner = false;
			for (int i = 0; i < 4; ++i) {
				if (walls.get(i) == true && walls.get(i + 1) == true) {
					corner = true;
				}
	    	}
		
			//Add current tile to visited
			sharedData.appendVisited(new VisitedTileData(sharedData.getCurrentPos(),walls,corner,colour.checkSilver()));
		
			//Calculate distance to unvisited tiles and update each with new distance value (Uses Pathing.java functions)
			for (UnvisitedTileData unvisitedTile : sharedData.getUnvisited()) {
				unvisitedTile.setDistance(pathing.generatePath(unvisitedTile.getCoords()).size());
			}

			//Detect for any problems in orientation or position (Mainly checks any information that may have been logged during Pathing)

			//Call upon Survivors function to search for any survivors and detect them
			survivors.detect(walls);

		}
		
		if (true) {//If user interaction
			//Reset current position to last silver tile
			sharedData.setCurrentPos(sharedData.getLastSilverTile());
		}
	}
}
