package com.pwnion.rcjrescuemaze.global;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.pwnion.rcjrescuemaze.SharedData;

public class Survivors {
	//Search for any survivors
	
	private final SharedData sharedData;
	
	@Inject
	Survivors(SharedData sharedData) {
		this.sharedData = sharedData;
	}
	
	public void detect(ArrayList<Boolean> walls) {
		//scan using infared sensor
	}
}
