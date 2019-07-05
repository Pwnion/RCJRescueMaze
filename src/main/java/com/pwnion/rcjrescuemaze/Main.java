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
import com.pwnion.rcjrescuemaze.hardware.ImplColour;
import com.pwnion.rcjrescuemaze.hardware.ImplInfared;
import com.pwnion.rcjrescuemaze.hardware.ImplUltrasonic;
import com.pwnion.rcjrescuemaze.hardware.Infared;
import com.pwnion.rcjrescuemaze.hardware.Ultrasonic;
import com.pwnion.rcjrescuemaze.software.ImplPathing;
import com.pwnion.rcjrescuemaze.software.Searching;
import com.pwnion.rcjrescuemaze.software.SharedData;

public class Main {
	@Inject
	private static SharedData sharedData;
	
	@Inject
	private static ImplPathing pathing;
	
	@Inject
	private static Searching searching;
	
	@Inject
	private static Colour colour;
	
	@Inject
	private static Ultrasonic ultrasonic;
	
	@Inject
	private static Infared infared;
	
	@Inject
	private static DrivingMotors drivingMotors;
	
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new MainBinder());

		sharedData = injector.getInstance(SharedData.class);
		pathing = injector.getInstance(ImplPathing.class);
		searching = injector.getInstance(Searching.class);
		colour = injector.getInstance(ImplColour.class);
		ultrasonic = injector.getInstance(ImplUltrasonic.class);
		infared = injector.getInstance(ImplInfared.class);
		drivingMotors = injector.getInstance(DrivingMotors.class);
		
		//Setup
		drivingMotors.move("up");
		sharedData.setCurrentPos(0, 0);
		sharedData.appendUnvisited(new UnvisitedTileData(new Coords(0, 0), 1));
		
		//While there are unvisited tiles
		while(sharedData.getUnvisited().size() > 0) {
		
			//Call upon searching function to find and move to next tile
			searching.findMoveUnvisited();
		
			//Remove current tile from unvisited
			sharedData.removeUnvisited(sharedData.getCurrentPos());
			
			//Update the list of unvisited tiles with viable surrounding tiles
			Coords coords;
			for(int i = 0; i < 4; i++) {
				coords = sharedData.getCurrentPos();
				if(!ultrasonic.findWalls().get(i)) {
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
					sharedData.appendUnvisited(new UnvisitedTileData(coords, 1));
				}
			}
			
			//Calculate any new corners found and add to list
			boolean corner = false;
			for (int i = 0; i < 4; i++) {
				if (ultrasonic.findWalls().get(i) && ultrasonic.findWalls().get(i == 3 ? 0 : i + 1)) {
					corner = true;
				}
	    	}
			
			//Add current tile to visited
			sharedData.appendVisited(new VisitedTileData(sharedData.getCurrentPos(), ultrasonic.findWalls(), corner, colour.detectSilver()));
		
			//Calculate distance to unvisited tiles and update each with new distance value (Uses Pathing.java functions)
			HashMap<Coords, Integer> map = pathing.generateMap();
			for (UnvisitedTileData unvisitedTile : sharedData.getUnvisited()) {
				unvisitedTile.setDistance(map.get(unvisitedTile.getCoords()));
			}

			//Detect for any problems in orientation or position (Mainly checks any information that may have been logged during Pathing)

			//Call upon Survivors function to search for any survivors and detect them
			infared.detectSurvivors(ultrasonic.findWalls());
		}
		
		if (true) {//If user interaction
			//Reset current position to last silver tile
			sharedData.setCurrentPos(sharedData.getLastSilverTile());
		}
	}
}