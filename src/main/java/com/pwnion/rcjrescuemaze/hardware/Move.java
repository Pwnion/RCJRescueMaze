package com.pwnion.rcjrescuemaze.hardware;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.pi4j.wiringpi.Gpio;
import com.pwnion.rcjrescuemaze.datatypes.Coords;
import com.pwnion.rcjrescuemaze.software.SharedData;

@Singleton
public class Move extends DrivingMotors {
	
	private final GetColour getColour;
	private final SharedData sharedData;
	private final Ultrasonic ultrasonic;
	private final Pins pins;
	
	boolean compCorrectiveTurning = false;
	
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
	public Move(Pins pins, SharedData sharedData, ColourFactory colourFactory, Ultrasonic ultrasonic) {
		super(pins);
		
		this.pins = pins;
		this.sharedData = sharedData;
		this.getColour = colourFactory.create("/home/pi/cam.jpg");
		this.ultrasonic = ultrasonic;
	}
	
	@Override
	public final boolean go(String direction) {
		boolean returnVal = true;
		
		if(compCorrectiveTurning) {
		switch (direction) {
				case "up":
					returnVal = go2(direction, globalMoveDuration / 2);
					go2("right", 200);
					go2("anticlockwise", 20);
					returnVal = go2(direction, globalMoveDuration / 2);
					go2("right", 300);
					break;
				case "right":
					returnVal = go2(direction, globalMoveDuration / 2);
					go2("anticlockwise", 150);
					go2("up", 200);
					returnVal = go2(direction, globalMoveDuration / 2);
					go2("up", 300);
					break;
				case "left":
					go2(direction, globalMoveDuration / 4);
					go2("down", 400);
					returnVal = go2(direction, globalMoveDuration / 4);
					go2("down", 200);
					returnVal = go2(direction, globalMoveDuration / 2);
					go2("down", 300);
					go2("left", 200);
					break;
				default:
					go2(direction, globalMoveDuration);
					break;
			}
		} else {
			returnVal = go2(direction, globalMoveDuration);
		}
		return returnVal;
	}
		
	public final boolean go2(String direction, long inputMoveDuration) {	
		boolean returnVal = true;
		
		start(direction, Optional.empty());
		
		long startTime = Gpio.millis();
		long moveDuration;
		while(Gpio.millis() - startTime < inputMoveDuration) {
			if(getColour.getColourPercentages().get("black") > 50f) {
				returnVal = false;
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
			} else if(pins.tiltPin.isHigh()) {
				returnVal = false;
				stop();
				moveDuration = Gpio.millis() - startTime;
				start(oppDirections.get(direction), Optional.of(moveDuration));
				try {
					Thread.sleep(moveDuration);
				} catch (InterruptedException e) {
					System.out.print("Couldn't sleep the thread...");
					e.printStackTrace();
				}
				
				sharedData.setRampTile(sharedData.getCurrentPos());
				sharedData.setRampDir(direction);
				sharedData.addWallForCurrentVisited(direction);
				
				break;
			}
		}
		stop();
		return returnVal;
	}
	
	@Override
	public final void goUntil(String direction, float distanceToWall) throws InterruptedException, ExecutionException {
		start(direction, Optional.of((long) 100000));
		while(ultrasonic.rawSensorOutput().get(dirToPos.get(direction)) > distanceToWall);
		stop();
	}
}
