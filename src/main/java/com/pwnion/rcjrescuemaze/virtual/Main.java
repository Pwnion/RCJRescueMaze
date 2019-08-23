package com.pwnion.rcjrescuemaze.virtual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.pwnion.rcjrescuemaze.binders.MainBinder;
import com.pwnion.rcjrescuemaze.datatypes.Coords;
import com.pwnion.rcjrescuemaze.datatypes.UnvisitedTileData;
import com.pwnion.rcjrescuemaze.datatypes.VisitedTileData;
import com.pwnion.rcjrescuemaze.hardware.DrivingMotors;
import com.pwnion.rcjrescuemaze.hardware.GetSurvivors;
import com.pwnion.rcjrescuemaze.virtual.GetWalls;
import com.pwnion.rcjrescuemaze.hardware.GetColour;
import com.pwnion.rcjrescuemaze.hardware.Move;
import com.pwnion.rcjrescuemaze.hardware.SurvivorFactory;
import com.pwnion.rcjrescuemaze.software.MoveToCoords;
import com.pwnion.rcjrescuemaze.software.SharedData1;

public class Main {
	@Inject
	private static SharedData1 sharedData;
	
	@Inject
	private static MoveToCoords pathing;
	
	@Inject
	private static GetColour getColour;
	
	@Inject
	private static GetWalls getWalls;
	
	@Inject
	private static GetSurvivors getSurvivors;
	
	@Inject
	private static SurvivorFactory survivorFactory;
	
	@Inject
	private static DrivingMotors move;
	
	private static final void manageTiles(boolean start, GetWalls getWalls) {
		
		//Calculate any new corners found and add to list
			boolean corner = false;
			for (int i = 0; i < 4; i++) {
				if (getWalls.get(i) && getWalls.get(i == 3 ? 0 : i + 1)) {
					corner = true;
				}
		    }
		
		System.out.println(" Append Visited, (" + sharedData.getCurrentPos().toString() + " (coords), " + getWalls.get() + " (walls), " + corner + " (corner), " + getColour.get() + "(colour))");
		
		//Add current tile to visited
		sharedData.appendVisited(new VisitedTileData(new Coords(sharedData.getCurrentPos()), getWalls.get(), corner, getColour.get().equals("silver") ? true : false));
		
		System.out.println("Visited Coords, " + sharedData.getVisitedCoords() + " of Size, " + sharedData.getVisitedCoords().size());
		
		//Update the list of unvisited tiles with viable surrounding tiles
		for(int i = 0; i < 4; i++) {
			Coords coords = new Coords(sharedData.getCurrentPos());
			System.out.print("walls: " + getWalls.get(i));

			if(!getWalls.get(i)) {
				switch(i) {
				case 0: //Front
					coords.addY(1);
					System.out.print(" (Front) \n");
					break;
				case 1: //Left
					coords.addX(-1);
					System.out.print(" (Left) \n");
					break;
				case 2: //Back
					coords.addY(-1);
					System.out.print(" (Back) \n");
					break;
				case 3: //Right
					coords.addX(1);
					System.out.print(" (Right) \n");
					break;
						
				}
				
				
				System.out.println(" Coords in Visited, " + sharedData.getVisitedCoords().contains(coords));
				if(!sharedData.getVisitedCoords().toString().contains(coords.toString()) && !(i == 2 && start)) {
					sharedData.appendUnvisited(new UnvisitedTileData(coords, 1));
					System.out.println("  Append Unvisted " + coords.toString());
				}
			} else {
				System.out.print("\n");
			}
		}
		
	}
	
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new MainBinder());

		sharedData = injector.getInstance(SharedData1.class);
		move = injector.getInstance(Move.class);
		
		getColour = injector.getInstance(GetColour.class);
		getWalls = injector.getInstance(GetWalls.class);
		
		survivorFactory = injector.getInstance(SurvivorFactory.class);
		getSurvivors = survivorFactory.create(new ArrayList<Boolean>(Collections.nCopies(4, false)));
		
		//Setup
		move.go("up");
		sharedData.setCurrentPos(0, 0);
		manageTiles(true, injector.getInstance(GetWalls.class));
		
		//for(int i = 0; i < 2; i++) {
		while(sharedData.getUnvisited().size() > 0) {
			getColour = injector.getInstance(GetColour.class);
			getWalls = injector.getInstance(GetWalls.class);
			getSurvivors = survivorFactory.create(new ArrayList<Boolean>(Collections.nCopies(4, false)));
			pathing = injector.getInstance(MoveToCoords.class);
			
			//Calculate distance to unvisited tiles and update each with new distance value (Uses Pathing.java functions)
			HashMap<Coords, Integer> map = pathing.generateMap();
			HashMap<String, Integer> mapStr = new HashMap<String, Integer>();
			
			for(Coords coord : map.keySet()) {
				mapStr.put(coord.toString(), map.get(coord));
			}
			
			for (UnvisitedTileData unvisitedTile : sharedData.getUnvisited()) {
				unvisitedTile.setDistance(mapStr.get(unvisitedTile.getCoords().toString()));
			}
		
			//Call upon searching function to find and move to next tile
			System.out.println("Closest Tile, (" + sharedData.getClosestTile().getX() + ", " + sharedData.getClosestTile().getY() + ")");
			
			pathing.moveToCoords(sharedData.getClosestTile(), map);
		
			//Remove current tile from unvisited
			sharedData.removeUnvisited(sharedData.getCurrentPos());
			
			manageTiles(false, getWalls);

			//Call upon Survivors function to search for any survivors and detect them
			getSurvivors.get();
			
			//}
			
			/*
			if(!sharedData.getRampTile().equals(new Coords(0, 0))) {
				pathing.moveToCoords(sharedData.getRampTile());
				
				move.goUntil(sharedData.getRampDir(), 7);
				
				manageTiles(false, injector.getInstance(GetWalls.class));
			}*/
		}
		
		System.exit(0);
		
		//pathing.moveToCoords(new Coords(0, 0));
		//move.go("down");
		
	}
}