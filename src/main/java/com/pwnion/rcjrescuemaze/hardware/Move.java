package com.pwnion.rcjrescuemaze.hardware;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.pi4j.wiringpi.Gpio;
import com.pwnion.rcjrescuemaze.datatypes.Coords;
import com.pwnion.rcjrescuemaze.software.SharedData1;

@Singleton
public class Move extends DrivingMotors {
	
	private final Camera findColour;
	private final SharedData1 sharedData;
	private final Ultrasonic ultrasonic;
	
	private final HashMap<String, String> oppDirections = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("up", "down");
			put("left", "right");
			put("down", "up");
			put("right", "left");
		}
	};
	
	private final HashMap<String, int[]> coordsToAdd = new HashMap<String, int[]>() {
		private static final long serialVersionUID = 1L;
		{
			put("up", new int[] {0, 1});
			put("left", new int[] {-1, 0});
			put("down", new int[] {0, -1});
			put("right", new int[] {1, 0});
		}
	};
	
	private HashMap<String, String> dirToPos = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("up", "front");
			put("left", "left");
			put("down", "back");
			put("right", "right");
		}
	};
	
	@Inject
	public Move(Pins pins, SharedData1 sharedData, Camera findColour, Ultrasonic ultrasonic) {
		super(pins);
		
		this.sharedData = sharedData;
		this.findColour = findColour;
		this.ultrasonic = ultrasonic;
	}
	
	@SuppressWarnings("unused")
	@Override
	public final void go(String direction) {	
		start(direction, Optional.empty());
		
		long startTime = Gpio.millis();
		long moveDuration;
		while(Gpio.millis() - startTime < globalMoveDuration) {
			if(findColour.get().equals("black")) {
				stop();
				moveDuration = Gpio.millis() - startTime;
				start(oppDirections.get(direction), Optional.of(moveDuration));
				try {
					Thread.sleep(moveDuration);
				} catch (InterruptedException e) {
					System.out.print("Couldn't sleep the thread...");
					e.printStackTrace();
				}
				
				Coords blackCoords = new Coords(sharedData.getCurrentPos().getX() + coordsToAdd.get(direction)[0],
												sharedData.getCurrentPos().getY() + coordsToAdd.get(direction)[1]);
				
				sharedData.appendBlackTiles(blackCoords);
				break;
			} else if(false /*ramp condition*/) {
				
			}
		}
		stop();
	}
	
	@Override
	public final void goUntil(String direction, float distanceToWall) throws InterruptedException, ExecutionException {
		start(direction, Optional.of((long) 100000));
		while(ultrasonic.rawSensorOutput().get(dirToPos.get(direction)) > distanceToWall);
		stop();
	}
}
