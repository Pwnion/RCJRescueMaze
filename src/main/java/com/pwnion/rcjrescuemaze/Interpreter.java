package com.pwnion.rcjrescuemaze;

import java.util.HashMap;
import java.util.Optional;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.pwnion.rcjrescuemaze.binders.MainBinder;
import com.pwnion.rcjrescuemaze.hardware.DrivingMotors;
import com.pwnion.rcjrescuemaze.hardware.Ultrasonic;

public class Interpreter {
	@Inject
	private static DrivingMotors drivingMotors;
	
	@Inject
	private static Ultrasonic ultrasonic;
	
	/*
	@Inject
	private static SurvivorFactory survivorFactory;
	
	private static GetSurvivors getSurvivors;
	*/
	
	public static void main(String[] args) throws NumberFormatException, InterruptedException {
		Injector injector = Guice.createInjector(new MainBinder());
		
		drivingMotors = injector.getInstance(DrivingMotors.class);
		ultrasonic = injector.getInstance(Ultrasonic.class);
		//ultrasonic = injector.getInstance(Ultrasonic.class);
		//survivorFactory = injector.getInstance(SurvivorFactory.class);
		
		/*
		ArrayList<Boolean> walls = new ArrayList<Boolean>() {
			private static final long serialVersionUID = 1L;
			{
				add(true);
				add(true);
				add(true);
				add(true);
			}
		};
		*/
		
		//getSurvivors = survivorFactory.create(walls);
		
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
				HashMap<String, Float> rawSensorOutput = ultrasonic.rawSensorOutput();
				if(args[1].equals("all")) {
					for(String pos : rawSensorOutput.keySet()) {
						System.out.println(pos + ": " + rawSensorOutput.get(pos));
					}
				} else {
					System.out.println(args[1] + ": " + rawSensorOutput.get(args[1]));
				}
				break;
			/*
			case "Infrared":
				while(true) {
					System.out.println("front: " + getSurvivors.get(0));
					System.out.println("left: " + getSurvivors.get(1));
					System.out.println("back: " + getSurvivors.get(2));
					System.out.println("right: " + getSurvivors.get(3));
					
					Thread.sleep(1000);
				}*/
			}
		} catch(Exception e) {
			System.out.println("Invalid input! Try again.");
			e.printStackTrace();
		}
		
		System.exit(0);
	}
}
