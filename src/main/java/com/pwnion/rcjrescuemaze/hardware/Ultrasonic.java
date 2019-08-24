package com.pwnion.rcjrescuemaze.hardware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.google.inject.Inject;
import com.pi4j.wiringpi.Gpio;

public abstract class Ultrasonic{
	
	//Inject a pins object as a dependency
	private final Pins pins;
	
	//Constructor that takes the position of the ultrasonic sensor on the robot as a parameter
	@Inject
	protected Ultrasonic(Pins pins) {
		this.pins = pins;
	}
	
	//Triggers pulse of sound for sensor at specified position, records time and calculates/returns distance
	private final float getDistance(String pos) {
		int pulseTime = 20;
		pins.sendPin.pulse(pulseTime, TimeUnit.MICROSECONDS);
		Gpio.delayMicroseconds(pulseTime);
		
		/*
		try {
			Thread.sleep(10);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}*/
		
		long startTime = System.nanoTime();;
		long time = 0;
		
		
		while(pins.receivePins.get(pos).isLow()) {
			time = System.nanoTime() - startTime;
			if(time > 12820000) return -1;
		}
		while(pins.receivePins.get(pos).isHigh()) {
			time = System.nanoTime() - startTime;
		}

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return (float) (time);  //0.01715 = (343/2)/10^6
	}
	
	//Runs getDistance() for all four sensors, associates them with a position in a hashmap and returns said hashmap
	protected HashMap<String, Float> rawSensorOutput() {
		return new HashMap<String, Float>() {
			private static final long serialVersionUID = 1L;
			{
				put("front", getDistance("front"));
				put("left", getDistance("left"));
				put("back", getDistance("back"));
				put("right", getDistance("right"));
			}
		};
	}
	
	//Abstract implementation
	public abstract ArrayList<Boolean> get();
	public abstract boolean get(int i);
}