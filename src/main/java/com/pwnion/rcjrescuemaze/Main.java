package com.pwnion.rcjrescuemaze;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.pwnion.rcjrescuemaze.binders.MainBinder;
import com.pwnion.rcjrescuemaze.datatypes.Coords;
import com.pwnion.rcjrescuemaze.datatypes.UnvisitedTileData;
import com.pwnion.rcjrescuemaze.datatypes.VisitedTileData;
import com.pwnion.rcjrescuemaze.hardware.ColourFactory;
import com.pwnion.rcjrescuemaze.hardware.DispenserMotor;
import com.pwnion.rcjrescuemaze.hardware.DrivingMotors;
import com.pwnion.rcjrescuemaze.hardware.GetSurvivors;
import com.pwnion.rcjrescuemaze.hardware.GetWalls;
import com.pwnion.rcjrescuemaze.hardware.GetColour;
import com.pwnion.rcjrescuemaze.hardware.Move;
import com.pwnion.rcjrescuemaze.hardware.Pins;
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
	private static DrivingMotors move;
	
	@Inject
	private static DispenserMotor dispenserMotor;
	
	@Inject
	private static Pins pins;
	
	@Inject
	private static GetWalls getWalls;
	
	private static boolean start = false;
	
	//private static int levelChangeCounter = 0;
	
	private final static HashMap<String, String> oppDir = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("up", "down");
			put("left", "right");
			put("down", "up");
			put("right", "left");
		}
	};
	
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
	
	public static boolean restart = false;

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		Injector injector = Guice.createInjector(new MainBinder());
		
		sharedData = injector.getInstance(SharedData.class);
		move = injector.getInstance(Move.class);
		getColour = injector.getInstance(ColourFactory.class).create("/home/pi/cam.jpg");
		pins = injector.getInstance(Pins.class);
		dispenserMotor = injector.getInstance(DispenserMotor.class);
		getWalls = injector.getInstance(GetWalls.class);
		
		while(true) {
			ProcessBuilder pb = new ProcessBuilder("raspistill", "-o", "/home/pi/cam.jpg", "-w", "32", "-h", "32", "-t", "0", "-tl", "0", "-ss", "100000", "-ex", "night",
					   "-co", "25", "-sa", "10", "-br", "55", "-drc", "low").inheritIO();
			Process p = pb.start();
			
			System.out.println("Ready");
			while(pins.buttonPin.isLow());
			sharedData.startTime();
			
			//Setup Test
			move.go("up");
			sharedData.setCurrentPos(0, 0);
			manageTiles(injector.getInstance(GetWalls.class));
			
			ExecutorService pool = Executors.newFixedThreadPool(2); // creates a pool of threads for the Future to draw from

			pool.submit(new Callable<Integer>() {
				@Override
				public Integer call() throws IOException {
					while(pins.buttonPin.isLow());
					Main.restart = true;
					System.out.println("23-8562-83956: " + Main.restart);
					return 0;
				}
			});
			
			//for(int i = 0; i < 3; i++) {
			while(sharedData.getUnvisited().size() > 0) {
				//getColour = injector.getInstance(GetColour.class);
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
				
				if(restart) {
					System.out.println("23-85sdvsdvsdv62-83956: " + restart);
					restart = false;
					break;
				}
				
				pathing.moveToCoords(sharedData.getClosestTile(), map);
				
				if(restart) {
					System.out.println("23-8562-839asvdfbd56: " + restart);
					restart = false;
					break;
				}
				
				/*
				long modTime = new File("/home/pi/cam.jpg").lastModified();
				
				while(modTime == new File("/home/pi/cam.jpg").lastModified());
				
				if(getColour.get().equals("")) {
					long startTime = System.currentTimeMillis();
					while((System.currentTimeMillis() - startTime) < 10000) {
						if(pins.buttonPin.isHigh()) {
							
						}
					}
				}*/
			
				//Remove current tile from unvisited
				sharedData.removeUnvisited(sharedData.getCurrentPos());
				
				manageTiles(injector.getInstance(GetWalls.class));
	
				
				
				
				//Do Sensor stuff
				getSurvivors = injector.getInstance(GetSurvivors.class);
				HashMap<String, Float> sensorOutput = new HashMap<String, Float>();
				HashMap<String, Integer> infa = new HashMap<String, Integer>();
				getWalls.populateSensorOutput();
				sensorOutput = getWalls.rawSensorOutput();
				infa = getSurvivors.rawSensorOutput();
				
				//Measurements in cm
				long adjustTime = 500;
				long moveDuration = (DrivingMotors.globalMoveDuration / 4) - adjustTime;
				int infaredThreshold = 40;
				
				System.out.println(sensorOutput.keySet());

				
				
				for(int j = 0; j < 4; j++) {
					String position = sharedData.getPositions(j);
					System.out.println(sensorOutput.keySet() + " + " + position);
					float dis = sensorOutput.get(position);

					if(dis == -1 && (infa.get(position) > infaredThreshold)) {
						System.out.println("Found Invalid Wall Readjusting");
						move.start2(oppDir.get(position), adjustTime);
						getWalls.populateSensorOutput();
						float newWallDis = getWalls.rawSensorOutput().get(position);
						if(newWallDis != -1) {
							move.start2(oppDir.get(position), moveDuration);
							System.out.println("/n/n/n           DETECTED WALL MOVING AWAY");
						} else {
							move.start2(position, adjustTime);
						}
					}
					System.out.println(dis);
				}	
			}
			
			/*
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
			} else break;*/
			//}
			
			//pathing.moveToCoords(new Coords(0, 0));
			//move.go("down");
			
			p.destroy();
		}
		//System.out.println("\n\n Finished in " + ((System.currentTimeMillis() - sharedData.getStartTime()) / 1000) + "sec, Moved " + sharedData.getFullPath().size() + " tiles or " + (sharedData.getFullPath().size() * 30) + "cm");
		//System.out.println("Full Path: " + sharedData.getFullPath());
		
		//p.destroy();
		
		//System.exit(0);
	}
}