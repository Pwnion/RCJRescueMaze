package com.pwnion.rcjrescuemaze.global.searching;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.pwnion.rcjrescuemaze.SharedData;
import com.pwnion.rcjrescuemaze.global.searching.pathing.drivers.Ultrasonic;

public class FindWalls {
	private final SharedData sharedData;
	private final Ultrasonic ultrasonic;
	
	@Inject
	FindWalls(SharedData sharedData, Ultrasonic ultrasonic) {
		this.sharedData = sharedData;
		this.ultrasonic = ultrasonic;
	}
	
	//Search using sensors to find walls
	
	public ArrayList<Boolean> find() {
		ArrayList<Boolean> walls = new ArrayList<Boolean>();
		
		ultrasonic.getDistances().values().forEach((distance) -> {
			walls.add(distance <= 22.5 && distance != -1 ? true : false);
		});

		return walls;
	}
}
