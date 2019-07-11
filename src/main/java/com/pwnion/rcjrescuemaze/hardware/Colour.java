package com.pwnion.rcjrescuemaze.hardware;

import java.util.HashMap;

import com.google.inject.Inject;

public abstract class Colour {
	
	protected final Pins pins;
	
	@Inject
	protected Colour(Pins pins) {
		this.pins = pins;
	}
	
	protected HashMap<String, Integer> rawSensorOutput() {
		return new HashMap<String, Integer>();
	}
	
	//Abstract implementations
	public abstract void detectSurvivor(String position);
	public abstract boolean detectSilver();
}
