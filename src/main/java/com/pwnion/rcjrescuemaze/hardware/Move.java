package com.pwnion.rcjrescuemaze.hardware;

import java.util.HashMap;
import java.util.Optional;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.pi4j.wiringpi.Gpio;
import com.pwnion.rcjrescuemaze.datatypes.Coords;
import com.pwnion.rcjrescuemaze.software.SharedData;

@Singleton
public class Move extends DrivingMotors {
	
	private final Colour findColour;
	private final SharedData sharedData;
	
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
	public Move(Pins pins, SharedData sharedData, Colour findColour) {
		super(pins);
		
		this.sharedData = sharedData;
		this.findColour = findColour;
	}

	@Override
	public final void go(String direction) {
		start(direction, Optional.empty());
		
		long startTime = Gpio.millis();
		long moveDuration;
		while(Gpio.millis() - startTime < globalMoveDuration) {
			if(findColour.get() == "black") {
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
			} //else if(ramp) ramp stuff;
		}
		stop();
	}
}
