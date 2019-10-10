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
	
	@Inject
	public Move(Pins pins, SharedData sharedData, ColourFactory colourFactory, Ultrasonic ultrasonic) {
		super(pins, ultrasonic);
		
		this.pins = pins;
		this.sharedData = sharedData;
		this.getColour = colourFactory.create("/home/pi/cam.jpg");
		this.ultrasonic = ultrasonic;
	}
	
	@Override
	public final boolean go(String direction) {
		boolean returnVal = true;
		
		if(compCorrectiveTurning) {
			
		int turnAmount = 0;
			
		switch (direction) {
				case "up":
					turnAmount = 150;
					go2("clockwise", turnAmount);
					returnVal = go2(direction, globalMoveDuration);
					go2("anticlockwise", (long) (turnAmount * .8666));
					break;
				case "right":
					turnAmount = 155;
					go2("anticlockwise", turnAmount);
					returnVal = go2(direction, globalMoveDuration);
					go2("clockwise", (long) (turnAmount * .3));
					break;
				case "left":
					turnAmount = 95;
					go2("anticlockwise", turnAmount);
					returnVal = go2(direction, globalMoveDuration);
					go2("clockwise", (long) (turnAmount * 1.3));
					break;
				case "down":
					turnAmount = 40;
					//go2("clockwise", turnAmount);
					returnVal = go2(direction, globalMoveDuration);
					go2("anticlockwise", (long) (turnAmount));
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
	
	
	
	public void setGlobal(int newGlobal) {
		globalMoveDuration = newGlobal;
	}
	
	
	
		
	public final boolean go2(String direction, long inputMoveDuration) {	
		boolean returnVal = true;
		
		if(inputMoveDuration <= 0) {
			return false;
		}
		
		start(direction, inputMoveDuration);
		
		long startTime = Gpio.millis();
		long moveDuration;
		while(Gpio.millis() - startTime < inputMoveDuration) {
			/*if(getColour.getColourPercentages().get("black") > 50f) {
				returnVal = false;
				stop();
				moveDuration = Gpio.millis() - startTime;
				start(oppDirections.get(direction), moveDuration);
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
			} else //*/
			if(pins.tiltPin.isHigh()) {
				returnVal = false;
				stop();
				moveDuration = Gpio.millis() - startTime;
				start(oppDirections.get(direction), moveDuration);
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
		goUntil(direction, distanceToWall, 1024);
	}
	
	@Override
	public final void goUntil(String direction, float distanceToWall, int speed) throws InterruptedException, ExecutionException {
		start2(direction, 100000, 0, speed);
		while(ultrasonic.rawSensorOutput().get(direction) > distanceToWall);
		stop();
	}
}
