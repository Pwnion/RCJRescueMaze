package com.pwnion.rcjrescuemaze;

import java.util.HashMap;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.pwnion.rcjrescuemaze.binders.MainBinder;
import com.pwnion.rcjrescuemaze.datatypes.Coords;
import com.pwnion.rcjrescuemaze.datatypes.UnvisitedTileData;
import com.pwnion.rcjrescuemaze.datatypes.VisitedTileData;
import com.pwnion.rcjrescuemaze.hardware.Colour;
import com.pwnion.rcjrescuemaze.hardware.DrivingMotors;
import com.pwnion.rcjrescuemaze.hardware.FindSurvivors;
import com.pwnion.rcjrescuemaze.hardware.FindWalls;
import com.pwnion.rcjrescuemaze.hardware.FindColour;
import com.pwnion.rcjrescuemaze.hardware.Infared;
import com.pwnion.rcjrescuemaze.hardware.Move;
import com.pwnion.rcjrescuemaze.software.MoveToCoords;
import com.pwnion.rcjrescuemaze.software.SharedData;

public class Main {
	@Inject
	private static SharedData sharedData;
	
	@Inject
	private static MoveToCoords pathing;
	
	@Inject
	private static Colour findColour;
	
	@Inject
	private static FindWalls findWalls;
	
	@Inject
	private static Infared findSurvivors;
	
	@Inject
	private static DrivingMotors move;
	
	private static final void manageTiles(boolean start, FindWalls findWalls) {
		//Update the list of unvisited tiles with viable surrounding tiles
		Coords coords;
		for(int i = 0; i < 4; i++) {
			coords = sharedData.getCurrentPos();
			if(!findWalls.get(i)) {
				switch(i) {
				case 0: //Front
					coords.addY(1);
					break;
				case 1: //Left
					coords.addX(-1);
					break;
				case 2: //Back
					coords.addY(-1);
					break;
				case 3: //Right
					coords.addX(1);
					break;
				}
				
				if(!sharedData.getVisitedCoords().contains(coords) && !(i == 2 && start)) {
					sharedData.appendUnvisited(new UnvisitedTileData(coords, 1));
				}
			}
		}
		
		//Calculate any new corners found and add to list
		boolean corner = false;
		for (int i = 0; i < 4; i++) {
			if (findWalls.get(i) && findWalls.get(i == 3 ? 0 : i + 1)) {
				corner = true;
			}
    	}
		
		//Add current tile to visited
		sharedData.appendVisited(new VisitedTileData(sharedData.getCurrentPos(), findWalls.get(), corner, findColour.get() == "silver" ? true : false));
	}
	
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new MainBinder());

		sharedData = injector.getInstance(SharedData.class);
		move = injector.getInstance(Move.class);
		
		//Setup
		move.go("up");
		sharedData.setCurrentPos(0, 0);
		manageTiles(true, injector.getInstance(FindWalls.class));
		
		//While there are unvisited tiles
		while(sharedData.getUnvisited().size() > 0) {
			findColour = injector.getInstance(FindColour.class);
			findWalls = injector.getInstance(FindWalls.class);
			findSurvivors = injector.getInstance(FindSurvivors.class);
			pathing = injector.getInstance(MoveToCoords.class);
		
			//Call upon searching function to find and move to next tile
			pathing.moveToCoords(sharedData.getClosestTile());
		
			//Remove current tile from unvisited
			sharedData.removeUnvisited(sharedData.getCurrentPos());
			
			manageTiles(false, findWalls);
		
			//Calculate distance to unvisited tiles and update each with new distance value (Uses Pathing.java functions)
			HashMap<Coords, Integer> map = pathing.generateMap();
			for (UnvisitedTileData unvisitedTile : sharedData.getUnvisited()) {
				unvisitedTile.setDistance(map.get(unvisitedTile.getCoords()));
			}

			//Detect for any problems in orientation or position (Mainly checks any information that may have been logged during Pathing)

			//Call upon Survivors function to search for any survivors and detect them
			findSurvivors.get(findWalls.get());
		}
		
		if (true) {//If user interaction
			//Reset current position to last silver tile
			sharedData.setCurrentPos(sharedData.getLastSilverTile());
		}
	}
}