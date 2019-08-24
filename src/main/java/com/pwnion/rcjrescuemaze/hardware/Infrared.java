package com.pwnion.rcjrescuemaze.hardware;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

import com.google.inject.Inject;

public abstract class Infrared {

	protected final Pins pins;
	
	@Inject
	protected Infrared(Pins pins) {
		this.pins = pins;
	}
	
	protected ArrayList<String> positions = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("front");
			add("left");
			add("back");
			add("right");
		}
	};
	
	private int getTemp(String pos) {
		int temp = 0;
		try {
    		ProcessBuilder pb = new ProcessBuilder("i2cget", "-y", Integer.toString(pins.I2CBUS), Byte.toString(pins.I2CADDRS.get(positions.indexOf(pos))), Byte.toString(pins.RAWIR1));
		    Process p = pb.start();
		    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

		    temp = Integer.parseInt(reader.lines().findAny().get().substring(2), 16);

		    p.waitFor();
		    p.destroy();
		    
		    return temp;
		} catch (IOException | InterruptedException | NoSuchElementException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	protected HashMap<String, Integer> rawSensorOutput() {
		HashMap<String, Integer> output = new HashMap<String, Integer>();
    	
    	for(String pos : positions) {
    		output.put(pos, getTemp(pos));
    	}
		
		return output;
	}
	
	//Abstract implementation
	public abstract ArrayList<Boolean> get();
	public abstract boolean get(int i);
}
