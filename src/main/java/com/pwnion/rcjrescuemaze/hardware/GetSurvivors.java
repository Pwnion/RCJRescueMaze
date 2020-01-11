package com.pwnion.rcjrescuemaze.hardware;

import java.util.HashMap;

import com.google.inject.Inject;
//import com.google.inject.assistedinject.Assisted;

public class GetSurvivors extends Infrared {
	private HashMap<String, Integer> rawSensorOutput;
	private HashMap<String, Boolean> survivors;
	
	private HashMap<String, Boolean> getSurvivors() {
		HashMap<String, Boolean> output = new HashMap<String, Boolean>();
		
		for(String pos : rawSensorOutput.keySet()) {
			output.put(pos, rawSensorOutput().get(pos) > 110 ? true : false);
		}
		
		return output;
	}
	
	@Inject
	protected GetSurvivors(Pins pins) {
		super(pins);
		
		this.rawSensorOutput = super.rawSensorOutput();
		this.survivors = getSurvivors();
	}
	
	public HashMap<String, Boolean> read() {
		this.rawSensorOutput = super.rawSensorOutput();
		this.survivors = getSurvivors();
		
		return survivors;
	}
	
	@Override
	public HashMap<String, Integer> rawSensorOutput() {
		return rawSensorOutput;
	}
	
	@Override
	public HashMap<String, Boolean> get() {
		/*
		ArrayList<Boolean> output = new ArrayList<Boolean>();
		for(boolean value : survivors.values()) {
			output.add(value);
		}
		return output;*/
		
		return survivors;
	}
	
	@Override
	public boolean get(String key) {
		return survivors.get(key);
	}
}
