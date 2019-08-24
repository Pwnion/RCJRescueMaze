package com.pwnion.rcjrescuemaze.hardware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
		ArrayList<Float> sensorOutputs = new ArrayList<Float>();
		boolean continueCondition = false;
		for(int i = 0; i < 10; i++) {
			System.out.println(i + "p1");
			pins.sendPin.high(); // Make trigger pin HIGH
			Thread.sleep((long) 0.01); // Delay for 10 microseconds
			pins.sendPin.low(); //Make trigger pin LOW
			
			long timeoutCounter = System.nanoTime();
			
			
			while(pins.receivePins.get(pos).isLow()) {
				if(System.nanoTime() - timeoutCounter > 1282000) {
					continueCondition = true;
					break;
				}
			}
			if(continueCondition) {
				continueCondition = false;
				continue;
			}
			
			System.out.println(i + "p2");
			
			long startTime = System.nanoTime(); // Store the current time to calculate ECHO pin HIGH time.
			
			while(pins.receivePins.get(pos).isHigh()) {
				if(((((System.nanoTime() - startTime) / 1e3) / 2) / 29.1) > 22.5) {
					continueCondition = true;
					break;
				}
			}
			if(continueCondition) {
				continueCondition = false;
				continue;
			}
			
			long endTime = System.nanoTime(); // Store the echo pin HIGH end time to calculate ECHO pin HIGH time.
			
			System.out.println(i + "p3");
			sensorOutputs.add((float) ((((endTime - startTime) / 1e3) / 2) / 29.1));
		}
		
		if(sensorOutputs.isEmpty()) {
			return (float) -1;
		}
		return getMean(eliminateOutliers(sensorOutputs, 1));
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
	
	protected static float getMean(ArrayList<Float> values) {
		System.out.println("Getting Mean");
		
		if(values.isEmpty()) {
			System.out.println("getMean values is Empty");
			return -1;
		}
		
		Iterator<Float> it = values.iterator();
		while(it.hasNext()) {
			if(it.next() < 1) {
				System.out.println("eliminateOutliers values is less then 1");
				it.remove();
			}
		}
		
	    int sum = 0;
	    for (float value : values) {
	        sum += value;
	    }

	    return (sum / values.size());
	}

	public static double getVariance(ArrayList<Float> values) {
		System.out.println("Getting Variance");
	    double mean = getMean(values);
	    int temp = 0;

	    for (float a : values) {
	        temp += (a - mean) * (a - mean);
	    }

	    return temp / (values.size() - 1);
	}

	public static double getStdDev(ArrayList<Float> values) {
		System.out.println("Getting Standard Deviation");
	    return Math.sqrt(getVariance(values));
	}

	public static ArrayList<Float> eliminateOutliers(ArrayList<Float> values, float scaleOfElimination) {
		
		System.out.println("Running eliminateOutliers with " + values);
		
		if(values.isEmpty()) {
			System.out.println("eliminateOutliers values is empty");
			return new ArrayList<Float>();
		}
		
		Iterator<Float> it = values.iterator();
		while(it.hasNext()) {
			if(it.next() < 1) {
				System.out.println("eliminateOutliers values is less then 1");
				it.remove();
			}
		}
		
	    double mean = getMean(values);
	    double stdDev = getStdDev(values);

	    final ArrayList<Float> newList = new ArrayList<>();

	    for (float value : values) {

	        if (!((value < (mean - stdDev * scaleOfElimination)) || (value > (mean + stdDev * scaleOfElimination)))) {
	            newList.add(value);
	        }
	    }

	    return values;

	}
	
	//Abstract implementation
	public abstract ArrayList<Boolean> get();
	public abstract boolean get(int i);
}