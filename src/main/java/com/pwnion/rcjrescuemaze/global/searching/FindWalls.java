package com.pwnion.rcjrescuemaze.global.searching;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.pwnion.rcjrescuemaze.SharedData;

public class FindWalls {
	private final SharedData sharedData;
	
	@Inject
	FindWalls(SharedData sharedData) {
		this.sharedData = sharedData;
	}
	
	//Search using sensors to find walls
	
	public ArrayList<Boolean> find() {
		ArrayList<Boolean> walls = new ArrayList<Boolean>();
		
		return walls;
	}
}
