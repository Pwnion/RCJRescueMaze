package com.pwnion.rcjrescuemaze.global;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.pwnion.rcjrescuemaze.SharedData;
import com.pwnion.rcjrescuemaze.global.searching.pathing.drivers.Infared;

public class Survivors {
	//Search for any survivors
	
	private final SharedData sharedData;
	private final Infared infared;
	
	@Inject
	Survivors(SharedData sharedData, Infared infared) {
		this.sharedData = sharedData;
		this.infared = infared;
	}
	
	public void detect(ArrayList<Boolean> walls) {
		//Scan for survivors using infared sensor
	}
}
