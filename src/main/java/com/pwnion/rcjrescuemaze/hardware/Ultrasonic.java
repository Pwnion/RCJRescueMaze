package com.pwnion.rcjrescuemaze.hardware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.inject.Inject;

public abstract class Ultrasonic {
	
	//Inject a pins object as a dependency
	private final Pins pins;
	
	//Constructor that takes the position of the ultrasonic sensor on the robot as a parameter
	@Inject
	protected Ultrasonic(Pins pins) {
		this.pins = pins;
	}
	
	//Triggers pulse of sound for sensor at specified position, records time and calculates/returns distance
	private final float getDistance(String pos) throws InterruptedException {
		pins.sendPin.high(); // Make trigger pin HIGH
		Thread.sleep((long) 0.01); // Delay for 10 microseconds
		pins.sendPin.low(); //Make trigger pin LOW
		
		long timeoutCounter = System.nanoTime();
		
		while(pins.receivePins.get(pos).isLow()) {
			if(System.nanoTime() - timeoutCounter > 1282000) return -1;
		}
		
		long startTime = System.nanoTime(); // Store the surrent time to calculate ECHO pin HIGH time.
		
		while(pins.receivePins.get(pos).isHigh()) {
			if(((((System.nanoTime() - startTime) / 1e3) / 2) / 29.1) > 22.5) return -1;
		}
		
		long endTime = System.nanoTime(); // Store the echo pin HIGH end time to calculate ECHO pin HIGH time.
		
		return (float) ((((endTime - startTime) / 1e3) / 2) / 29.1);
	}
	
	//Runs getDistance() for all four sensors, associates them with a position in a hashmap and returns said hashmap
	protected HashMap<String, Float> rawSensorOutput() throws InterruptedException, ExecutionException {
		ExecutorService pool = Executors.newFixedThreadPool(5); // creates a pool of threads for the Future to draw from

		return new HashMap<String, Float>() {
			private static final long serialVersionUID = 1L;
			{
				put("front", pool.submit(new Callable<Float>() {
					@Override
				    public Float call() throws InterruptedException { return getDistance("front"); }
				}).get());
				put("left", pool.submit(new Callable<Float>() {
					@Override
				    public Float call() throws InterruptedException { return getDistance("left"); }
				}).get());
				put("back", pool.submit(new Callable<Float>() {
					@Override
				    public Float call() throws InterruptedException { return getDistance("back"); }
				}).get());
				put("right", pool.submit(new Callable<Float>() {
					@Override
				    public Float call() throws InterruptedException { return getDistance("right"); }
				}).get());
			}
		};
	}
	
	//Abstract implementation
	public abstract ArrayList<Boolean> get();
	public abstract boolean get(int i);
}