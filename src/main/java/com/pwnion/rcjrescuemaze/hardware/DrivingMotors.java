package com.pwnion.rcjrescuemaze.hardware;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DrivingMotors {
	private final Pins pins;
	private final long moveDuration = 2000;

	@Inject
	public DrivingMotors(Pins pins) {
		this.pins = pins;
	}

	//Moves the robot in the given direction using all 4 motors
	public void move(String direction) {
		pins.setSpeedPins(100);
		switch (direction) {
		case "up":
			pins.clockwisePins.get("front_left").pulse(moveDuration);
			pins.clockwisePins.get("back_left").pulse(moveDuration);

			pins.anticlockwisePins.get("front_right").pulse(moveDuration);
			pins.anticlockwisePins.get("back_right").pulse(moveDuration);

			break;
		case "down":
			pins.clockwisePins.get("front_right").pulse(moveDuration);
			pins.clockwisePins.get("back_right").pulse(moveDuration);

			pins.anticlockwisePins.get("front_left").pulse(moveDuration);
			pins.anticlockwisePins.get("back_left").pulse(moveDuration);

			break;
		case "left":
			pins.clockwisePins.get("back_left").pulse(moveDuration);
			pins.clockwisePins.get("back_right").pulse(moveDuration);

			pins.anticlockwisePins.get("front_left").pulse(moveDuration);
			pins.anticlockwisePins.get("front_right").pulse(moveDuration);

			break;
		case "right":
			pins.clockwisePins.get("front_right").pulse(moveDuration);
			pins.clockwisePins.get("front_left").pulse(moveDuration);

			pins.anticlockwisePins.get("back_left").pulse(moveDuration);
			pins.anticlockwisePins.get("back_right").pulse(moveDuration);

			break;
		}
		try {
			Thread.sleep(moveDuration);
		} catch (InterruptedException e) {
			System.out.println("Sleep is causing some problems...");
		}
		pins.setSpeedPins(0);
	}
}