package com.pwnion.rcjrescuemaze.hardware;

import com.google.inject.Inject;

public class DispenserMotor {
	private Pins pins;
	
	@Inject
	public DispenserMotor(Pins pins) {
		this.pins = pins;
	}
	
	public void clockwise(int degrees) {
		pins.stepperMotor.step(2048 * (360 / degrees));
	}
	
	public void anticlockwise(int degrees) {
		pins.stepperMotor.step(-2048 * (360 / degrees));
	}
}
