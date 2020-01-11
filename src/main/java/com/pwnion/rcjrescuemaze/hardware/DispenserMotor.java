package com.pwnion.rcjrescuemaze.hardware;

import com.google.inject.Inject;

public class DispenserMotor {
	private Pins pins;
	
	@Inject
	public DispenserMotor(Pins pins) {
		this.pins = pins;
	}
	
	public void out(int degrees) {
		pins.stepperMotor.step((int) (2048f * ((float) degrees / 360f)));
	}
	
	public void in(int degrees) {
		pins.stepperMotor.step((int) (-2048f * ((float) degrees / 360f)));
	}
}
