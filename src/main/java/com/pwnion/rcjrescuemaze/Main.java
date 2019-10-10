package com.pwnion.rcjrescuemaze;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.pwnion.rcjrescuemaze.binders.MainBinder;
import com.pwnion.rcjrescuemaze.datatypes.Coords;
import com.pwnion.rcjrescuemaze.datatypes.UnvisitedTileData;
import com.pwnion.rcjrescuemaze.datatypes.VisitedTileData;
import com.pwnion.rcjrescuemaze.hardware.ColourFactory;
import com.pwnion.rcjrescuemaze.hardware.DrivingMotors;
import com.pwnion.rcjrescuemaze.hardware.GetSurvivors;
import com.pwnion.rcjrescuemaze.hardware.GetWalls;
import com.pwnion.rcjrescuemaze.hardware.GetColour;
import com.pwnion.rcjrescuemaze.hardware.Move;
import com.pwnion.rcjrescuemaze.hardware.Pins;
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
	private static GetSurvivors getSurvivors;
	
	@Inject
	private static SurvivorFactory survivorFactory;
	
	@Inject
	private static DrivingMotors move;
	
	@Inject
	private static Pins pins;
	
	private static boolean start = false;
	
	private static int levelChangeCounter = 0;
	
	private static final void manageTiles(GetWalls getWalls) {
		if(start) {
			getWalls.set(sharedData.getLastMoveWallLocation(), true); //Generates artificial wall at start
			start = false;
		} else {
			getWalls.set(sharedData.getLastMoveWallLocation(), false);
		}
		
		
		//Calculate any new corners found and add to list
		boolean corner = false;
		for (int i = 0; i < 4; i++) {
			int j = i + 1;
			if(j == 4) j = 0;
			
			if (getWalls.get(sharedData.getPositions(i)) && getWalls.get(sharedData.getPositions(j))) {
				corner = true;
			}
		}
		
		System.out.println(" Append Visited, (" + sharedData.getCurrentPos() + " (coords), " + getWalls.get() + " (walls), " + corner + " (corner), " + true + "(colour))");
		
		//Add current tile to visited
		sharedData.appendVisited(new VisitedTileData(new Coords(sharedData.getCurrentPos()), getWalls.get(), corner, getColour.get().equals("Silver") ? true : false));
		
		System.out.println("Visited Coords, " + sharedData.getVisitedCoords() + " of Size, " + sharedData.getVisitedCoords().size());
		
		//Update the list of unvisited tiles with viable surrounding tiles
		for(String position : sharedData.getPositions()) {
			Coords coords = new Coords(sharedData.getCurrentPos());
			System.out.print("walls: " + getWalls.get(position) + " (" + position + ")");
			

			if(!getWalls.get(position)) {
				switch(position) {
				case "up": 
					coords.addY(1);
					break;
				case "left":
					coords.addX(-1);
					break;
				case "down": 
					coords.addY(-1);
					break;
				case "right": 
					coords.addX(1);
					break;
						
				}
				
				System.out.println(" Coords in Visited, " + sharedData.visitedCoordsContains(coords));
				if(!(position == "down")) {
					sharedData.appendUnvisited(new UnvisitedTileData(coords, 1));
					System.out.println("  Append Unvisted " + coords.toString());
				}
			} else {
				System.out.print("\n");
			}
		}
		
	}
	
	public static Injector injector = Guice.createInjector(new MainBinder());

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		sharedData = injector.getInstance(SharedData.class);
		move = injector.getInstance(Move.class);
		getColour = injector.getInstance(ColourFactory.class).create("/home/pi/cam.jpg");
		survivorFactory = injector.getInstance(SurvivorFactory.class);
		getSurvivors = survivorFactory.create(new ArrayList<Boolean>(Collections.nCopies(4, false)));
		pins = injector.getInstance(Pins.class);
		
		System.out.println("Ready");
		while(pins.buttonPin.isLow());
		
		ProcessBuilder pb = new ProcessBuilder("raspistill", "-o", "/home/pi/cam.jpg", "-w", "32", "-h", "32", "-t", "0", "-tl", "0", "-ss", "100000", "-ex", "night",
				   "-co", "25", "-sa", "10", "-br", "55", "-drc", "low").inheritIO();
		Process p = pb.start();
		
		//Setup Test
		move.go("up");
		sharedData.setCurrentPos(0, 0);
		manageTiles(injector.getInstance(GetWalls.class));
		
		for(int i = 0; i < 3; i++) {
			while(sharedData.getUnvisited().size() > 0) {
				//getColour = injector.getInstance(GetColour.class);
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
				System.out.println("Closest Tile, " + sharedData.getClosestTile());
				
				pathing.moveToCoords(sharedData.getClosestTile(), map);
			
				//Remove current tile from unvisited
				sharedData.removeUnvisited(sharedData.getCurrentPos());
				
				manageTiles(injector.getInstance(GetWalls.class));
	
				//Call upon Survivors function to search for any survivors and detect them
				getSurvivors.get();
			}
			
			if(!sharedData.getRampTile().toString().equals(null)) {
				Consumer<Integer> save = num -> {
					try {
						FileOutputStream fileOutputStream = new FileOutputStream("/home/pi/sharedData" + String.valueOf(num) + ".txt");
					    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
					    objectOutputStream.writeObject(sharedData);
					    objectOutputStream.flush();
					    objectOutputStream.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
				
				Consumer<Integer> load = num -> {
				    try {
				    	FileInputStream fileInputStream = new FileInputStream("/home/pi/sharedData" + String.valueOf(num) + ".txt");
					    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
					    SharedData loadedSharedData = (SharedData) objectInputStream.readObject();
						objectInputStream.close();
						
						sharedData.load(
								loadedSharedData.getVisited(),
								loadedSharedData.getUnvisited(),
								loadedSharedData.getBlackTiles(),
								loadedSharedData.getLastSilverTile(),
								loadedSharedData.getCurrentPos(),
								loadedSharedData.getRampTile(),
								loadedSharedData.getRampDir());
					} catch (IOException | ClassNotFoundException e) {
						e.printStackTrace();
					}
				};
				
				save.accept(levelChangeCounter);
				
				pathing.moveToCoords(sharedData.getRampTile(), pathing.generateMap());
				
				String rampDir = sharedData.getRampDir();
				move.goUntil(rampDir, 7);
				sharedData.pathAppend("RAMP - " + rampDir);
			    
			    sharedData.clear();
			    if(levelChangeCounter == 1) {
			    	load.accept(0);
			    }
			    levelChangeCounter++;
			    
			    start = true;
			    
			    sharedData.setRampDir(new HashMap<String, String>() {
					private static final long serialVersionUID = 1L;
					{
						put("up", "down");
						put("left", "right");
						put("down", "up");
						put("right", "left");
					}
				}.get(rampDir));
			} else break;
		}
		
		System.out.println("\n\n Finished in " + sharedData.getTime() + "sec, Moved " + (sharedData.getTime() / 3) + " tiles or " + (sharedData.getTime() * 10) + "cm");
		System.out.println("Full Path: " + sharedData.getFullPath());
		
		p.destroy();
		
		System.exit(0);
		
		//pathing.moveToCoords(new Coords(0, 0));
		//move.go("down");
		
	}
}