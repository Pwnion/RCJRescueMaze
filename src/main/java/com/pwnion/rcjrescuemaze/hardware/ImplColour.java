package com.pwnion.rcjrescuemaze.hardware;

import com.google.inject.Inject;

public class ImplColour extends Colour {
	
	@Inject
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
