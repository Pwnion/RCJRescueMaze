package com.pwnion.rcjrescuemaze;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Optional;
import java.util.Iterator;
import java.util.Optional;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.pwnion.rcjrescuemaze.binders.MainBinder;
import com.pwnion.rcjrescuemaze.hardware.ColourFactory;
import com.pwnion.rcjrescuemaze.hardware.DrivingMotors;
//import com.pwnion.rcjrescuemaze.hardware.DrivingMotors;
import com.pwnion.rcjrescuemaze.hardware.GetColour;
import com.pwnion.rcjrescuemaze.hardware.GetSurvivors;
import com.pwnion.rcjrescuemaze.hardware.GetWalls;
import com.pwnion.rcjrescuemaze.hardware.Move;
import com.pwnion.rcjrescuemaze.hardware.Pins;
import com.pwnion.rcjrescuemaze.hardware.SurvivorFactory;
import com.pwnion.rcjrescuemaze.software.SharedData;

public class Interpreter {
	@Inject
	private static DrivingMotors drivingMotors;
	
	@Inject
	private static Move move;

	//@Inject
	//private static SurvivorFactory survivorFactory;
	
	//@Inject
	//private static ColourFactory colourFactory;
	
	@Inject
	private static Pins pins;
	
	//@Inject
	//private static SharedData sharedData;
	
	//private static GetSurvivors getSurvivors;
	
	public static HashMap<String, HashMap<String, Integer>> tileValues;
	
	public static void main(String[] args) throws NumberFormatException, InterruptedException {
		long FullStartTime = System.nanoTime();
		Injector injector = Guice.createInjector(new MainBinder());
		
		//survivorFactory = injector.getInstance(SurvivorFactory.class);
		//colourFactory = injector.getInstance(ColourFactory.class);
		//sharedData = injector.getInstance(SharedData.class);
		pins = injector.getInstance(Pins.class);
		drivingMotors = injector.getInstance(DrivingMotors.class);
		move = injector.getInstance(Move.class);
		
		ArrayList<Boolean> walls = new ArrayList<Boolean>() {
			private static final long serialVersionUID = 1L;
			{
				add(true);
				add(true);
				add(true);
				add(true);
			}
		};
		
		ArrayList<String> directions = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
			{
				add("up");
				add("left");
				add("down");
				add("right");
			}
		};
		
		
		
		System.out.println("**************************************");
		
		try {
			switch(args[0]) {
			case "DrivingMotors":
				switch(args[1]) {
				case "all":
					drivingMotors.start("up", Optional.of(Long.parseLong(args[2])));
					Thread.sleep(Long.parseLong(args[2]));
					drivingMotors.stop();
					
					drivingMotors.start("left", Optional.of(Long.parseLong(args[2])));
					Thread.sleep(Long.parseLong(args[2]));
					drivingMotors.stop();
					
					drivingMotors.start("down", Optional.of(Long.parseLong(args[2])));
					Thread.sleep(Long.parseLong(args[2]));
					drivingMotors.stop();
					
					drivingMotors.start("right", Optional.of(Long.parseLong(args[2])));
					Thread.sleep(Long.parseLong(args[2]));
					drivingMotors.stop();
					break;
				case "global":
					move.setGlobal(Integer.parseInt(args[2]));
					move.go("down");
					break;
				case "test":
					pins.clockwisePins.get("up_left").pulse(5000);
					Thread.sleep(1000);
					pins.clockwisePins.get("up_left").low();
					pins.clockwisePins.get("up_left").high();
					Thread.sleep(10000);
					pins.clockwisePins.get("up_left").pulse(1000);
					Thread.sleep(1000);
					
					break;
				default:
					drivingMotors.start(args[1], Optional.of(Long.parseLong(args[2])));
					Thread.sleep(Long.parseLong(args[2]));
					drivingMotors.stop();
					break;
				}
				
				break;
				
				/*
			case "Ultrasonic":
				
				if(args[1].equals("all")) {
					FullStartTime = System.nanoTime();
					HashMap<String, Float> rawSensorOutput = injector.getInstance(GetWalls.class).rawSensorOutput();
					
					for(String pos : rawSensorOutput.keySet()) {
						System.out.println("US " + pos.toUpperCase() + "\n    VALUE: " + rawSensorOutput.get(pos));
						//counter++;
					}
					
					System.out.println("Finished in " + round((System.nanoTime() - FullStartTime) / 1e6, 2) + "ms");
					/*
					pins.sendPin.high();
					Thread.sleep(2000);
					for(int i = 0; i < 10000; i++) {
						pins.receivePins.values().forEach((pin) -> {
							if(pin.isHigh()) System.out.print("OH MY FUCKING GOD ONE OF THE PINS IS HIGH!");
						});
					}
					pins.sendPin.low();
					
				}
				
				break;
			/*case "Infrared":
				switch (args[1]) {
					case "all":
						int count = 0;
						while(true) {
							getSurvivors = survivorFactory.create(walls);
							for(int i = 0; i < 4; i++) {
								System.out.println("*IR " + directions.get(i).toUpperCase() + "*");
								System.out.println("    RAW: " + getSurvivors.rawSensorOutput().get(directions.get(i)));
								System.out.println("    PRESENT: " + getSurvivors.get(directions.get(i)));
							}
							
							count += 1;
							System.out.println(count);
							
							Thread.sleep(1000);
						}
					case "diff":
						
						if(!directions.contains(args[2])) {
							break;
						}
						
						ArrayList<Integer> IRvalues = new ArrayList<Integer>();
						String direction = args[2];
						
						for(int i = 0; i < 50; i++) {
							getSurvivors = survivorFactory.create(walls);
							IRvalues.add(getSurvivors.rawSensorOutput().get(direction));
							Thread.sleep(10);
						}
						
							int max = findMax(IRvalues);
							int min = findMin(IRvalues);
							
							System.out.println("*IR " + direction.toUpperCase() + "*");
							System.out.println("    VALUES: " + IRvalues);
							System.out.println("    MAX: " + max);
							System.out.println("    MIN: " + min);
							System.out.println("    DIFFERENCE: " + (max - min));
						
				}
				
				break; 
			case "Colour":
				/*
				for(String colour : sharedData.getTileValues().get(args[1]).keySet()) {
					System.out.println(colour + ": " + sharedData.getTileValues().get(args[1]).get(colour));
				}
				
				long modBefore = new File("/home/pi/cam.jpg").lastModified();
				long modAfter = modBefore;
				
				ProcessBuilder pb = new ProcessBuilder("raspistill", "-o", "/home/pi/cam.jpg", "-w", "32", "-h", "32", "-t", "0", "-tl", "0", "-ss", "100000", "-ex", "night",
													   "-co", "25", "-sa", "10", "-br", "55", "-drc", "low").inheritIO();
				Process p = pb.start();
				
				for(int i = 0; i < 1000; i++) {
					while(modBefore == modAfter) {
						modAfter = new File("/home/pi/cam.jpg").lastModified();
					}
					modBefore = modAfter;
					
					GetColour getColour = colourFactory.create("/home/pi/cam.jpg");
					
					HashMap<String, Float> colourPercentages = getColour.getColourPercentages();
					String mostColour = "";
					float mostPercentage = 0;
					
					float percentage;
					for(String colour : colourPercentages.keySet()) {
						percentage = colourPercentages.get(colour);
						
						if(percentage > mostPercentage) {
							mostColour = colour;
							mostPercentage = percentage;
						}
						System.out.println(colour + ": " + colourPercentages.get(colour) + "%");
					}
					
					System.out.println("MAJORITY IS: " + mostColour + " WITH " + mostPercentage + "%\n");

				}
				
				p.destroy();
				break; */
			}
		} catch(Exception e) {
			System.out.println("Invalid input! Try again.");
			e.printStackTrace();
		}
		
		System.exit(0);
	}
	
	private static float round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();
	 
	    BigDecimal bd = new BigDecimal(Double.toString(value));
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.floatValue();
	}
	
	protected int getMean(ArrayList<Integer> values) {
		
		if(values.isEmpty()) {
			return -1;
		}
		
		Iterator<Integer> it = values.iterator();
		while(it.hasNext()) {
			if(it.next() <= 0.2) {
				it.remove();
			}
		}
		
	    float sum = 0;
	    for (float value : values) {
	        sum += value;
	    }

	    return (int) (sum / values.size());
	}
	
	protected static int findMax(ArrayList<Integer> values) {
		int max = values.get(0);
		for(int value : values) {
			if(value > max) {
				max = value;
			}
		}
		return max;
	}
	
	protected static int findMin(ArrayList<Integer> values) {
		int min = values.get(0);
		for(int value : values) {
			if(value < min) {
				min = value;
			}
		}
		return min;
	}
}
