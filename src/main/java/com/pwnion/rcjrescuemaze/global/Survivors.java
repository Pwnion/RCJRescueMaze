package com.pwnion.rcjrescuemaze.global;

import com.google.inject.Inject;
import com.pwnion.rcjrescuemaze.SharedData;

public class Survivors {
	//Search for any survivors
	
	private final SharedData sharedData;
	
	@Inject
	Survivors(SharedData sharedData) {
		this.sharedData = sharedData;
	}
	
	public void checkWalls() {
		//use infared sensor to scan walls
		sharedData.getVisited().contains()
	}
}
