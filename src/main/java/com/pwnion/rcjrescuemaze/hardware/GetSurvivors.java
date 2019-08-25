package com.pwnion.rcjrescuemaze.hardware;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
//import com.google.inject.assistedinject.Assisted;

public class GetSurvivors extends Infrared {
	private HashMap<String, Integer> rawSensorOutput;
	private HashMap<String, Boolean> survivors;
	
	private HashMap<String, Boolean> getSurvivors(ArrayList<Boolean> walls) {
		HashMap<String, Boolean> output = new HashMap<String, Boolean>();
		HashMap<String, Boolean> tempWalls = new HashMap<String, Boolean>();
		
		for(int i = 0; i < 4; i++) {
			tempWalls.put(super.positions.get(i), walls.get(i));
		}
		
		for(String pos : tempWalls.keySet()) {
			if(tempWalls.get(pos) == false) {
				output.put(pos, false);
			} else {
				output.put(pos, rawSensorOutput().get(pos) > 50 ? true : false);
			}
		}
		
		return output;
	}
	
	@Inject
	protected GetSurvivors(@Assisted ArrayList<Boolean> walls, Pins pins) {
		super(pins);
		
		this.rawSensorOutput = super.rawSensorOutput();
		this.survivors = getSurvivors(walls);
	}
	
	@Override
	public HashMap<String, Integer> rawSensorOutput() {
		return rawSensorOutput;
	}
	
	@Override
	public ArrayList<Boolean> get() {
		ArrayList<Boolean> output = new ArrayList<Boolean>();
		for(boolean value : survivors.values()) {
			output.add(value);
		}
		return output;
	}
	
	@Override
	public boolean get(String key) {
		return survivors.get(key);
	}
}
