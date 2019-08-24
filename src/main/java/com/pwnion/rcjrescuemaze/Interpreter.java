package com.pwnion.rcjrescuemaze;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Optional;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.pwnion.rcjrescuemaze.binders.MainBinder;
//import com.pwnion.rcjrescuemaze.hardware.DrivingMotors;
import com.pwnion.rcjrescuemaze.hardware.GetColour;
import com.pwnion.rcjrescuemaze.hardware.GetSurvivors;
import com.pwnion.rcjrescuemaze.hardware.GetWalls;
import com.pwnion.rcjrescuemaze.hardware.Pins;
import com.pwnion.rcjrescuemaze.hardware.SurvivorFactory;

public class Interpreter {
	//@Inject
	//private static DrivingMotors drivingMotors;

	@Inject
	private static SurvivorFactory survivorFactory;
	
	@Inject
	private static Pins pins;
	
	private static GetSurvivors getSurvivors;
	
	public static void main(String[] args) throws NumberFormatException, InterruptedException {
		Injector injector = Guice.createInjector(new MainBinder());
		
		//drivingMotors = injector.getInstance(DrivingMotors.class);
		survivorFactory = injector.getInstance(SurvivorFactory.class);
		pins = injector.getInstance(Pins.class);
		//drivingMotors = injector.getInstance(DrivingMotors.class);
		
		ArrayList<Boolean> walls = new ArrayList<Boolean>() {
			private static final long serialVersionUID = 1L;
			{
				add(true);
				add(true);
				add(true);
				add(true);
			}
		};
		
		getSurvivors = survivorFactory.create(walls);
		
		System.out.println("**************************************");
		
		try {
			switch(args[0]) {
			/*
			case "DrivingMotors":
				if(args[1].equals("all")) {
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
				} else {
					drivingMotors.start(args[1], Optional.of(Long.parseLong(args[2])));
					Thread.sleep(Long.parseLong(args[2]));
					drivingMotors.stop();
				}
				break;
				*/
			case "Ultrasonic":
				
				if(args[1].equals("all")) {
					HashMap<String, Float> rawSensorOutput = injector.getInstance(GetWalls.class).rawSensorOutput();
					for(String pos : rawSensorOutput.keySet()) {
						System.out.println("US " + pos.toUpperCase() + "\n    RAW: " + rawSensorOutput.get(pos)/* + "\n    PRESENT: " + getWalls.get(rawSensorOutput.keySet())*/);
						//counter++;
					}
					
					
					/*
					pins.sendPin.high();
					Thread.sleep(2000);
					for(int i = 0; i < 10000; i++) {
						pins.receivePins.values().forEach((pin) -> {
							if(pin.isHigh()) System.out.print("OH MY FUCKING GOD ONE OF THE PINS IS HIGH!");
						});
					}
					pins.sendPin.low();
					*/
				}
				break;
			case "Infrared":
				while(true) {
					System.out.println("*IR FRONT*\n    RAW: " + getSurvivors.rawSensorOutput().get("front") + "\n    PRESENT: " + getSurvivors.get(0));
					System.out.println("*IR LEFT*\n    RAW: " + getSurvivors.rawSensorOutput().get("left") + "\n    PRESENT: " + getSurvivors.get(1));
					System.out.println("*IR BACK*\n    RAW: " + getSurvivors.rawSensorOutput().get("back") + "\n    PRESENT: " + getSurvivors.get(2));
					System.out.println("*IR RIGHT*\n    RAW: " + getSurvivors.rawSensorOutput().get("right") + "\n    PRESENT: " + getSurvivors.get(3));
					
					Thread.sleep(1000);
				}
			case "Colour":
				ProcessBuilder pb = new ProcessBuilder("raspistill", "-o", "/home/pi/cam.jpg", "-w", "32", "-h", "32", "-t", "0", "-tl", "0");
				Process p = pb.start();
				
				long modBefore = new File("/home/pi/cam.jpg").lastModified();
				long modAfter = modBefore;
				
				for(int i = 0; i < 20; i++) {
					int colours[] = injector.getInstance(GetColour.class).getAvgColours();
					System.out.println("RED: " + colours[0]);
					System.out.println("GREEN: " + colours[1]);
					System.out.println("BLUE: " + colours[2]);
					
					while(modBefore == modAfter) {
						modAfter = new File("/home/pi/cam.jpg").lastModified();
					}
					modBefore = modAfter;
				}
				
				p.destroy();
			}
		} catch(Exception e) {
			System.out.println("Invalid input! Try again.");
			e.printStackTrace();
		}
		
		System.exit(0);
	}
}
