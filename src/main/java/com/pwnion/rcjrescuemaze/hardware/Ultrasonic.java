package com.pwnion.rcjrescuemaze.hardware;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
		int passes = 20;
		for(int i = 0; i < passes; i++) {
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
			
			sensorOutputs.add((float) ((((endTime - startTime) / 1e3) / 2) / 29.1));
			
			Thread.sleep(3);
		}
		
		System.out.println(pos);
		if(sensorOutputs.isEmpty() || (sensorOutputs.size() < (passes / 2.5))) {
			return (float) -1;
		}
		
		//
		ArrayList<Float> newSensorOut = eliminateOutliers(sensorOutputs, (float) 0.5);
		float newMean = getMean(newSensorOut);
		
		if(findDifference(newSensorOut) > 10) { return (float) -1; }
		
		switch (pos) {
			case "front":
				newMean -= 0.5;
		}
		
		newMean = round(newMean, 1);

		System.out.println("Mean Before = " + round(getMean(newSensorOut), 1));
		System.out.println("Mean After = " + newMean);
		
		return newMean;
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
	
	private static float round(float value, int places) {
	    if (places < 0) throw new IllegalArgumentException();
	 
	    BigDecimal bd = new BigDecimal(Double.toString(value));
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.floatValue();
	}
	
	protected static float getMean(ArrayList<Float> values) {
		
		if(values.isEmpty()) {
			return -1;
		}
		
		Iterator<Float> it = values.iterator();
		while(it.hasNext()) {
			if(it.next() <= 0.2) {
				it.remove();
			}
		}
		
	    float sum = 0;
	    for (float value : values) {
	        sum += value;
	    }

	    return sum / values.size();
	}

	public static float getVariance(ArrayList<Float> values) {
	    float mean = getMean(values);
	    float temp = 0;

	    for (float a : values) {
	        temp += (a - mean) * (a - mean);
	    }
	    
	    return temp / (values.size() - 1);
	}

	public static float getStdDev(ArrayList<Float> values) {
	    return (float) Math.sqrt(getVariance(values));
	}

	public static ArrayList<Float> eliminateOutliers(ArrayList<Float> values, float scaleOfElimination) {
		
		System.out.println("Running eliminateOutliers with " + values);
		
		if(values.isEmpty()) {
			return new ArrayList<Float>();
		}
		
		Iterator<Float> it = values.iterator();
		while(it.hasNext()) {
			if(it.next() <= 0.2) {
				it.remove();
			}
		}
		
	    float mean = getMean(values);
	    float stdDev = getStdDev(values);

	    ArrayList<Float> newList = new ArrayList<>();
	    ArrayList<Float> eliminated = new ArrayList<>();

	    for (float value : values) {
	    	float lowerBound = mean - stdDev * scaleOfElimination;
	    	float upperBound = mean + stdDev * scaleOfElimination;
	    	

	        if (!(value < lowerBound) && !(value > upperBound)) {
	            newList.add(value);
	        } else {
	        	eliminated.add(value);
	        }
	    }
	    
	    if(eliminated.size() > newList.size()) {
	    	System.out.println("Eliminated > NewList");
	    	return values;
	    }

	    System.out.println("Eliminated " + eliminated);
	    System.out.println("Running eliminateOutliers returns " + newList);
	    return newList;
	}
	
	protected static float findMax(ArrayList<Float> values) {
		float max = values.get(0);
		for(float value : values) {
			if(value > max) {
				max = value;
			}
		}
		return max;
	}
	
	protected static float findMin(ArrayList<Float> values) {
		float min = values.get(0);
		for(float value : values) {
			if(value < min) {
				min = value;
			}
		}
		return min;
	}
	
	protected static float findDifference(ArrayList<Float> values) {
		return findMax(values) - findMin(values);
	}
	
	
	//Abstract implementation
	public abstract HashMap<String, Boolean> get();
	public abstract boolean get(String position);
}