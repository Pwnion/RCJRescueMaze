package com.pwnion.rcjrescuemaze.hardware;

import java.util.ArrayList;

import com.google.inject.Inject;

public class GetWalls extends Ultrasonic {
	private ArrayList<Boolean> walls;
	
	private ArrayList<Boolean> getWalls() {
		ArrayList<Boolean> walls = new ArrayList<Boolean>();
		
		for(String position : super.rawSensorOutput().keySet()) {
			walls.add(super.rawSensorOutput().get(position) != -1 && super.rawSensorOutput().get(position) < 22.5 ? true : false);
		}
		
		return walls;
	}
	
	@Inject
	public GetWalls(Pins pins) {
		super(pins);
		
		this.walls = getWalls();
	}
	
	@Override
	public ArrayList<Boolean> get() { return walls; }
	
	@Override
	public boolean get(int i) { return walls.get(i); };
	
}