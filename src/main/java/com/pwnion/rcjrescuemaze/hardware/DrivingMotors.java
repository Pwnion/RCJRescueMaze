package com.pwnion.rcjrescuemaze.hardware;

import java.util.Optional;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

@Singleton
public abstract class DrivingMotors {
	private final Pins pins;
	
	protected long globalMoveDuration = 2000;

	@Inject
	public DrivingMotors(Pins pins) {
		this.pins = pins;
	}

	//Moves the robot in the given direction using all 4 motors
	protected final void start(String direction, Optional<Long> localMoveDuration) {
		pins.setSpeedPins(100);
		switch (direction) {
		case "up":
			pins.clockwisePins.get("front_left").pulse(localMoveDuration.orElse(globalMoveDuration));
			pins.clockwisePins.get("back_left").pulse(localMoveDuration.orElse(globalMoveDuration));

			pins.anticlockwisePins.get("front_right").pulse(localMoveDuration.orElse(globalMoveDuration));
			pins.anticlockwisePins.get("back_right").pulse(localMoveDuration.orElse(globalMoveDuration));

			break;
		case "down":
			pins.clockwisePins.get("front_right").pulse(localMoveDuration.orElse(globalMoveDuration));
			pins.clockwisePins.get("back_right").pulse(localMoveDuration.orElse(globalMoveDuration));

			pins.anticlockwisePins.get("front_left").pulse(localMoveDuration.orElse(globalMoveDuration));
			pins.anticlockwisePins.get("back_left").pulse(localMoveDuration.orElse(globalMoveDuration));

			break;
		case "left":
			pins.clockwisePins.get("back_left").pulse(localMoveDuration.orElse(globalMoveDuration));
			pins.clockwisePins.get("back_right").pulse(localMoveDuration.orElse(globalMoveDuration));

			pins.anticlockwisePins.get("front_left").pulse(localMoveDuration.orElse(globalMoveDuration));
			pins.anticlockwisePins.get("front_right").pulse(localMoveDuration.orElse(globalMoveDuration));

			break;
		case "right":
			pins.clockwisePins.get("front_right").pulse(localMoveDuration.orElse(globalMoveDuration));
			pins.clockwisePins.get("front_left").pulse(localMoveDuration.orElse(globalMoveDuration));

			pins.anticlockwisePins.get("back_left").pulse(localMoveDuration.orElse(globalMoveDuration));
			pins.anticlockwisePins.get("back_right").pulse(localMoveDuration.orElse(globalMoveDuration));

			break;
		}
	}
	
	protected final void stop() {
		for(GpioPinDigitalOutput pin : pins.clockwisePins.values()) pin.low();
		for(GpioPinDigitalOutput pin : pins.anticlockwisePins.values()) pin.low();
		pins.setSpeedPins(0);
	}
	
	public abstract void go(String direction);
}