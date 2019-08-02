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
import com.pwnion.rcjrescuemaze.hardware.GetSurvivors;
import com.pwnion.rcjrescuemaze.hardware.GetWalls;
import com.pwnion.rcjrescuemaze.hardware.GetColour;
import com.pwnion.rcjrescuemaze.hardware.Infrared;
import com.pwnion.rcjrescuemaze.hardware.Move;
import com.pwnion.rcjrescuemaze.hardware.SurvivorFactory;
import com.pwnion.rcjrescuemaze.software.MoveToCoords;
import com.pwnion.rcjrescuemaze.software.SharedData;

public class Main {
	@Inject
	private static SharedData sharedData;
	
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
		//Update the list of unvisited tiles with viable surrounding tiles
		Coords coords = new Coords(0, 0);
		System.out.print("\n");
		for(int i = 0; i < 4; i++) {
			coords.set(sharedData.getCurrentPos());
			System.out.println("Current Position, " + coords.getX() + "," + coords.getY());
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
				
				System.out.println("Coords in Visited, " + sharedData.getVisitedCoords().contains(coords));
				if(!sharedData.getVisitedCoords().contains(coords) && !(i == 2 && start)) {
					sharedData.appendUnvisited(new UnvisitedTileData(coords, 1));
					System.out.println("Append Unvisted");
				}
			} else {
				System.out.print("\n");
			}
		}
		
		//Calculate any new corners found and add to list
		boolean corner = false;
		for (int i = 0; i < 4; i++) {
			if (getWalls.get(i) && getWalls.get(i == 3 ? 0 : i + 1)) {
				corner = true;
			}
    	}
		
		System.out.println("Append Visited, (" + sharedData.getCurrentPos().getX() + "," + sharedData.getCurrentPos().getY() + " (coords), " + getWalls.get() + " (walls), " + corner + " (corner), " + getColour.get() + "(colour))");
		
		//Add current tile to visited
		sharedData.appendVisited(new VisitedTileData(sharedData.getCurrentPos(), getWalls.get(), corner, getColour.get() == "silver" ? true : false));
	}
	
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new MainBinder());

		sharedData = injector.getInstance(SharedData.class);
		move = injector.getInstance(Move.class);
		
		getColour = injector.getInstance(GetColour.class);
		getWalls = injector.getInstance(GetWalls.class);
		
		survivorFactory = injector.getInstance(SurvivorFactory.class);
		getSurvivors = survivorFactory.create(getWalls.get());
		
		//Setup
		move.go("up");
		sharedData.setCurrentPos(0, 0);
		manageTiles(true, injector.getInstance(GetWalls.class));
		
		for(int i = 0; i < 2; i++) {
			while(sharedData.getUnvisited().size() > 0) {
				getColour = injector.getInstance(GetColour.class);
				getWalls = injector.getInstance(GetWalls.class);
				getSurvivors = survivorFactory.create(getWalls.get());
				pathing = injector.getInstance(MoveToCoords.class);
			
				//Call upon searching function to find and move to next tile
				System.out.println("Closest Tile, (" + sharedData.getClosestTile().getX() + ", " + sharedData.getClosestTile().getY() + ")");
				
				pathing.moveToCoords(sharedData.getClosestTile());
			
				//Remove current tile from unvisited
				sharedData.removeUnvisited(sharedData.getCurrentPos());
				
				manageTiles(false, getWalls);
			
				//Calculate distance to unvisited tiles and update each with new distance value (Uses Pathing.java functions)
				HashMap<Coords, Integer> map = pathing.generateMap();
				for (UnvisitedTileData unvisitedTile : sharedData.getUnvisited()) {
					unvisitedTile.setDistance(map.get(unvisitedTile.getCoords()));
				}
	
				//Call upon Survivors function to search for any survivors and detect them
				getSurvivors.get();
			}
			
			if(!sharedData.getRampTile().equals(new Coords(0, 0))) {
				pathing.moveToCoords(sharedData.getRampTile());
				
				move.goUntil(sharedData.getRampDir(), 7);
				
				manageTiles(false, injector.getInstance(GetWalls.class));
			}
		}
		
		//pathing.moveToCoords(new Coords(0, 0));
		//move.go("down");
		
	}
}