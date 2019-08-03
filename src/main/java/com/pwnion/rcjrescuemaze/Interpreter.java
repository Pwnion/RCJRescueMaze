package com.pwnion.rcjrescuemaze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.pwnion.rcjrescuemaze.binders.MainBinder;
import com.pwnion.rcjrescuemaze.hardware.DrivingMotors;
import com.pwnion.rcjrescuemaze.hardware.GetSurvivors;
import com.pwnion.rcjrescuemaze.hardware.GetWalls;
import com.pwnion.rcjrescuemaze.hardware.SurvivorFactory;

public class Interpreter {
	@Inject
	private static DrivingMotors drivingMotors;
	
	@Inject
	private static GetWalls getWalls;
	
	@Inject
	private static SurvivorFactory survivorFactory;
	
	private static GetSurvivors getSurvivors;
	
	public static void main(String[] args) throws NumberFormatException, InterruptedException {
		Injector injector = Guice.createInjector(new MainBinder());
		
		drivingMotors = injector.getInstance(DrivingMotors.class);
		getWalls = injector.getInstance(GetWalls.class);
		survivorFactory = injector.getInstance(SurvivorFactory.class);
		
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
			
			case "Ultrasonic":
				HashMap<String, Float> rawSensorOutput = getWalls.getRawSensorOutput();
				if(args[1].equals("all")) {
					//int counter = 0;
					for(String pos : rawSensorOutput.keySet()) {
						System.out.println("US " + pos.toUpperCase() + "\n    RAW: " + rawSensorOutput.get(pos)/* + "\n    PRESENT: " + getWalls.get(rawSensorOutput.keySet())*/);
						//counter++;
					}
				}
				break;
			case "Infrared":
				while(true) {
					System.out.println("*IR FRONT*\n    RAW: " + getSurvivors.getRawSensorOutput().get("front") + "\n    PRESENT: " + getSurvivors.get(0));
					System.out.println("*IR LEFT*\n    RAW: " + getSurvivors.getRawSensorOutput().get("left") + "\n    PRESENT: " + getSurvivors.get(1));
					System.out.println("*IR BACK*\n    RAW: " + getSurvivors.getRawSensorOutput().get("back") + "\n    PRESENT: " + getSurvivors.get(2));
					System.out.println("*IR RIGHT*\n    RAW: " + getSurvivors.getRawSensorOutput().get("right") + "\n    PRESENT: " + getSurvivors.get(3));
					
					Thread.sleep(1000);
				}
			}
		} catch(Exception e) {
			System.out.println("Invalid input! Try again.");
			e.printStackTrace();
		}
		
		System.exit(0);
	}
}
