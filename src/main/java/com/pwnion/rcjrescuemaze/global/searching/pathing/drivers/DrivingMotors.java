package com.pwnion.rcjrescuemaze.global.searching.pathing.drivers;

import com.google.inject.Inject;
	
public class DrivingMotors {
	private final Pins pins;
	private final Ultrasonic ultrasonic;
	
	@Inject
	DrivingMotors(Pins pins, Ultrasonic ultrasonic) {
		this.pins = pins;
		this.ultrasonic = ultrasonic;
	}
	
	public void move(String direction) {
		switch(direction) {
		case "up":
			pins.clockwisePins.get("front_left").high();
			pins.clockwisePins.get("back_left").high();
			
			pins.anticlockwisePins.get("front_right").high();
			pins.anticlockwisePins.get("back_right").high();
			
			while(ultrasonic.getDistances().get("front") > 7.5);
			
			pins.clockwisePins.get("front_left").low();
			pins.clockwisePins.get("back_left").low();
			
			pins.anticlockwisePins.get("front_right").low();
			pins.anticlockwisePins.get("back_right").low();
			
			break;
		case "down":
			pins.clockwisePins.get("front_right").high();
			pins.clockwisePins.get("back_right").high();
			
			pins.anticlockwisePins.get("front_left").high();
			pins.anticlockwisePins.get("back_left").high();
			
			while(ultrasonic.getDistances().get("back") > 7.5);
			
			pins.clockwisePins.get("front_right").low();
			pins.clockwisePins.get("back_right").low();
			
			pins.anticlockwisePins.get("front_left").low();
			pins.anticlockwisePins.get("back_left").low();
			
			break;
		case "left":
			pins.clockwisePins.get("back_left").high();
			pins.clockwisePins.get("back_right").high();
			
			pins.anticlockwisePins.get("front_left").high();
			pins.anticlockwisePins.get("front_right").high();
			
			while(ultrasonic.getDistances().get("left") > 7.5);
			
			pins.clockwisePins.get("back_left").low();
			pins.clockwisePins.get("back_right").low();
			
			pins.anticlockwisePins.get("front_left").low();
			pins.anticlockwisePins.get("front_right").low();
			
			break;
		case "right":
			pins.clockwisePins.get("front_right").high();
			pins.clockwisePins.get("front_left").high();
			
			pins.anticlockwisePins.get("back_left").high();
			pins.anticlockwisePins.get("back_right").high();
			
			while(ultrasonic.getDistances().get("right") > 7.5);
			
			pins.clockwisePins.get("front_right").low();
			pins.clockwisePins.get("front_left").low();
			
			pins.anticlockwisePins.get("back_left").low();
			pins.anticlockwisePins.get("back_right").low();
			
			break;
		}
	}
}

