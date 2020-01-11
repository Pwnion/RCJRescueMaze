package com.pwnion.rcjrescuemaze.hardware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import com.google.inject.Inject;

public class GetWalls extends Ultrasonic {
	private HashMap<String, Boolean> walls;
	
	private HashMap<String, Boolean> getWalls() {
		HashMap<String, Boolean> walls = new HashMap<String, Boolean>();
		
		for(String position : rawSensorOutput.keySet()) {
			boolean isWall = rawSensorOutput.get(position) != -1 && rawSensorOutput.get(position) < 10 ? true : false;
			walls.put(position, isWall);
		}
		
		return walls;
	}
	
	@Inject
	public GetWalls(Pins pins) throws InterruptedException, ExecutionException {
		super(pins);
		
		this.rawSensorOutput = super.rawSensorOutput();
		this.walls = getWalls();
	}
	
	@Override
	public HashMap<String, Float> rawSensorOutput() {
		return rawSensorOutput;
	}
	
	@Override
	public HashMap<String, Boolean> get() {
		return walls; 
	}
	
	public ArrayList<Boolean> gets() {
		ArrayList<Boolean> walls = new ArrayList<Boolean>(4);
		
		walls.add(this.walls.get("up"));
		walls.add(this.walls.get("left"));
		walls.add(this.walls.get("down"));
		walls.add(this.walls.get("right"));
		
		return walls;
	}
	
	public void set(String position, boolean wall) { 
		walls.put(position, wall);
	}
	
	@Override
	public boolean get(String position) { return walls.get(position); };
	
}