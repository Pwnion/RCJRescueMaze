package com.pwnion.rcjrescuemaze.hardware;

import java.util.ArrayList;

import com.google.inject.Inject;

public class ImplInfared extends Infared {
	
	@Inject
	public ImplInfared(Pins pins) {
		super(pins);
	}

	public void detectSurvivors(ArrayList<Boolean> walls) {
		//Scan for survivors using infared sensor
	}
}
