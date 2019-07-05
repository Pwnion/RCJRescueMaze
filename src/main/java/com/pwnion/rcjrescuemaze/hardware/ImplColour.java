package com.pwnion.rcjrescuemaze.hardware;

public class ImplColour extends Colour {
	
	public ImplColour(Pins pins) {
		super(pins);
	}

	@Override
	public void detectSurvivor(String position) {
		//Scan for survivors using colour sensor
	}
	
	@Override
	public boolean detectSilver() {
		return true;
	}
}
