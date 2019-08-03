package com.pwnion.rcjrescuemaze.hardware;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
//import com.google.inject.assistedinject.Assisted;

public class GetSurvivors extends Infrared {
	private HashMap<String, Integer> rawSensorOutput;
	private ArrayList<Boolean> survivors;
	
	private ArrayList<Boolean> getSurvivors(ArrayList<Boolean> walls) {
		ArrayList<Boolean> output = new ArrayList<Boolean>();
		
		HashMap<String, Boolean> tempWalls = new HashMap<String, Boolean>();
		
		for(int i = 0; i < 4; i++) {
			tempWalls.put(super.positions.get(i), walls.get(i));
		}
		
		for(String pos : tempWalls.keySet()) {
			output.add(rawSensorOutput().get(pos) > 50 ? true : false);
		}
		
		return output;
	}
	
	@Inject
	protected GetSurvivors(@Assisted ArrayList<Boolean> walls, Pins pins) {
		super(pins);
		
		this.rawSensorOutput = super.rawSensorOutput();
		this.survivors = getSurvivors(walls);
	}
	
	public HashMap<String, Integer> getRawSensorOutput() {
		return rawSensorOutput;
	}
	
	@Override
	public ArrayList<Boolean> get() {
		return survivors;
	}
	
	@Override
	public boolean get(int i) {
		return survivors.get(i);
	}
}
