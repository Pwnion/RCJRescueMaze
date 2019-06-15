package com.pwnion.rcjrescuemaze.global.searching.pathing.drivers;

import com.google.inject.Inject;

public class DrivingMotors {
	Pins pins;
	
	@Inject
	DrivingMotors(Pins pins) {
		this.pins = pins;
	}
}
