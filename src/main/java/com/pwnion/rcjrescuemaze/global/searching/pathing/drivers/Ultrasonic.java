package com.pwnion.rcjrescuemaze.global.searching.pathing.drivers;

import java.util.HashMap;

import com.google.inject.Inject;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.Gpio;

public class Ultrasonic implements GpioPinListenerDigital {
	
	//Inject a pins object as a dependency
	private final Pins pins;
	
	//Boolean that is true when the ultrasonic sensor receives the sound wave back
	private boolean echoStateChanged;
	
	//Constructor that takes the position of the ultrasonic sensor on the robot as a parameter
	@Inject
	public Ultrasonic(Pins pins) {
		this.pins = pins;
	}
	
	//Listener for the echo pin changing states
	@Override
	public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
		echoStateChanged = event.getState().isHigh();
	}
	
	//Triggers pulse of sound for sensor at specified position, records time and calculates/returns distance
	private final int getDistance(String pos) {
		pins.sendPins.get(pos).pulse(10);
		long startTime = Gpio.micros();
		while(!echoStateChanged) {
			if(Gpio.micros() - startTime > 1312); return -1; //1312 = time taken in microseconds for a pulse of sound to travel 22.5cm and back again
		}
		return (int) ((Gpio.micros() - startTime) * 171500);
	}
	
	//Runs getDistance() for all four sensors, associates them with a position in a hashmap and returns said hashmap
	public final HashMap<String, Integer> getDistances() {
		return new HashMap<String, Integer>() {
			private static final long serialVersionUID = 1L;
			{
				put("front", getDistance("front"));
				put("left", getDistance("left"));
				put("back", getDistance("back"));
				put("right", getDistance("right"));
			}
		};
	}
}