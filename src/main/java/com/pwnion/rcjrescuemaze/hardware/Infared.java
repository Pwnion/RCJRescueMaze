package com.pwnion.rcjrescuemaze.hardware;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.inject.Inject;

public abstract class Infared {

	protected final Pins pins;
	
	@Inject
	protected Infared(Pins pins) {
		this.pins = pins;
	}
	
	protected HashMap<String, Float> rawSensorOutput() {
		return new HashMap<String, Float>();
	}
	
	//Abstract implementation
	public abstract void get(ArrayList<Boolean> walls);
}
