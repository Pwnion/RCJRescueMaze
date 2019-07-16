package com.pwnion.rcjrescuemaze.hardware;

import java.util.ArrayList;

import com.google.inject.Inject;

public class FindSurvivors extends Infared {
	
	@Inject
	protected FindSurvivors(Pins pins) {
		super(pins);
	}

	@Override
	public void detectSurvivors(ArrayList<Boolean> walls) {
		
	}

}
