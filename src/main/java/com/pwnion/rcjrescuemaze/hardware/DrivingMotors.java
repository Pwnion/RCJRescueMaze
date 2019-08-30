package com.pwnion.rcjrescuemaze.hardware;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.google.inject.Inject;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

public abstract class DrivingMotors {
	private final Pins pins;
	
	protected long globalMoveDuration = 4000;

	@Inject
	public DrivingMotors(Pins pins) {
		this.pins = pins;
	}

	//Moves the robot in the given direction using all 4 motors
	public final void start(String direction, Optional<Long> localMoveDuration) {
		switch (direction) {
		case "up":
			pins.anticlockwisePins.get("front_right").pulse(localMoveDuration.orElse(globalMoveDuration));
			pins.anticlockwisePins.get("back_right").pulse(localMoveDuration.orElse(globalMoveDuration));
			
			pins.clockwisePins.get("front_left").pulse(localMoveDuration.orElse(globalMoveDuration));
			pins.clockwisePins.get("back_left").pulse(localMoveDuration.orElse(globalMoveDuration));
			
			/*
			pins.clockwisePins.get("front_left").pulse(localMoveDuration.orElse(globalMoveDuration));
			
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			pins.anticlockwisePins.get("front_left").pulse(localMoveDuration.orElse(globalMoveDuration));
			*/

			break;
		case "down":
			pins.clockwisePins.get("front_right").pulse(localMoveDuration.orElse(globalMoveDuration));
			pins.anticlockwisePins.get("back_left").pulse(localMoveDuration.orElse(globalMoveDuration));
			
			pins.anticlockwisePins.get("front_left").pulse(localMoveDuration.orElse(globalMoveDuration));
			pins.clockwisePins.get("back_right").pulse(localMoveDuration.orElse(globalMoveDuration));
			
			/*
			pins.clockwisePins.get("back_left").pulse(localMoveDuration.orElse(globalMoveDuration));
			
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			pins.anticlockwisePins.get("back_left").pulse(localMoveDuration.orElse(globalMoveDuration));
			*/

			break;
		case "left":
			pins.clockwisePins.get("back_left").pulse(localMoveDuration.orElse(globalMoveDuration));
			pins.clockwisePins.get("back_right").pulse(localMoveDuration.orElse(globalMoveDuration));

			pins.anticlockwisePins.get("front_left").pulse(localMoveDuration.orElse(globalMoveDuration));
			pins.anticlockwisePins.get("front_right").pulse(localMoveDuration.orElse(globalMoveDuration));

			/*
			pins.clockwisePins.get("back_right").pulse(localMoveDuration.orElse(globalMoveDuration));
			
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			pins.anticlockwisePins.get("back_right").pulse(localMoveDuration.orElse(globalMoveDuration));
			*/
			
			break;
		case "right":
			pins.clockwisePins.get("front_right").pulse(localMoveDuration.orElse(globalMoveDuration));
			pins.clockwisePins.get("front_left").pulse(localMoveDuration.orElse(globalMoveDuration));

			pins.anticlockwisePins.get("back_left").pulse(localMoveDuration.orElse(globalMoveDuration));
			pins.anticlockwisePins.get("back_right").pulse(localMoveDuration.orElse(globalMoveDuration));
			
			/*
			pins.clockwisePins.get("front_right").pulse(localMoveDuration.orElse(globalMoveDuration));
			
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			pins.anticlockwisePins.get("front_right").pulse(localMoveDuration.orElse(globalMoveDuration));
			*/
			
			break;
		case "clockwise":
			pins.clockwisePins.get("front_right").pulse(localMoveDuration.orElse(globalMoveDuration));
			pins.clockwisePins.get("front_left").pulse(localMoveDuration.orElse(globalMoveDuration));
			pins.clockwisePins.get("back_left").pulse(localMoveDuration.orElse(globalMoveDuration));
			pins.clockwisePins.get("back_right").pulse(localMoveDuration.orElse(globalMoveDuration));
			break;
		case "anticlockwise":
			pins.anticlockwisePins.get("front_right").pulse(localMoveDuration.orElse(globalMoveDuration));
			pins.anticlockwisePins.get("front_left").pulse(localMoveDuration.orElse(globalMoveDuration));
			pins.anticlockwisePins.get("back_left").pulse(localMoveDuration.orElse(globalMoveDuration));
			pins.anticlockwisePins.get("back_right").pulse(localMoveDuration.orElse(globalMoveDuration));
			break;
		}
		pins.speedPin.setPwm(1024);
	}
	
	public final void stop() {
		pins.speedPin.setPwm(0);
		for(GpioPinDigitalOutput pin : pins.clockwisePins.values()) pin.low();
		for(GpioPinDigitalOutput pin : pins.anticlockwisePins.values()) pin.low();
	}
	
	public abstract void go(String direction);
	public abstract boolean go2(String direction, long inputMoveDuration);
	public abstract void goUntil(String direction, float distanceToWall) throws InterruptedException, ExecutionException;
	
}