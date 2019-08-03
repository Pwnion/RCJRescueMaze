package com.pwnion.rcjrescuemaze.hardware;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.inject.Inject;

public class GetWalls extends Ultrasonic {
	private HashMap<String, Float> rawSensorOutput;
	private ArrayList<Boolean> walls;
	
	private ArrayList<Boolean> getWalls() {
		ArrayList<Boolean> walls = new ArrayList<Boolean>();
		
		for(String position : rawSensorOutput.keySet()) {
			walls.add(rawSensorOutput.get(position) != -1 && rawSensorOutput.get(position) < 22.5 ? true : false);
		}
		
		return walls;
	}
	
	@Inject
	public GetWalls(Pins pins) {
		super(pins);
		
		this.rawSensorOutput = super.rawSensorOutput();
		this.walls = getWalls();
	}
	
	public HashMap<String, Float> getRawSensorOutput() {
		return rawSensorOutput;
	}
	
	@Override
	public ArrayList<Boolean> get() { return walls; }
	
	@Override
	public boolean get(int i) { return walls.get(i); };
	
}