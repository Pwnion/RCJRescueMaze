package com.pwnion.rcjrescuemaze.hardware;

import java.util.ArrayList;

import com.google.inject.Inject;

public class FindWalls extends Ultrasonic {
	private ArrayList<Boolean> walls;
	
	private ArrayList<Boolean> findWalls() {
		ArrayList<Boolean> walls = new ArrayList<Boolean>();
		
		for(String position : super.rawSensorOutput().keySet()) {
			walls.add(super.rawSensorOutput().get(position) != -1 ? true : false);
		}
		
		return walls;
	}
	
	@Inject
	public FindWalls(Pins pins) {
		super(pins);
		
		this.walls = findWalls();
	}
	
	@Override
	public ArrayList<Boolean> get() { return walls; }
	
	@Override
	public boolean get(int i) { return walls.get(i); };
	
}