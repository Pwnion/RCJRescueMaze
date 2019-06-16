package com.pwnion.rcjrescuemaze.global.searching.pathing.drivers;

import java.util.HashMap;

import com.google.inject.Inject;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.Gpio;

public class Ultrasonic implements GpioPinListenerDigital {
	
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
	
	private final int getDistance(String pos) {
		pins.sendPins.get(pos).pulse(10);
		long startTime = Gpio.micros();
		while(!echoStateChanged) {
			if(Gpio.micros() - startTime > 1312); return -1;
		}
		return (int) ((Gpio.micros() - startTime) * 171500);
	}
	
	//Obtains the distance in cm from the ultrasonic sensor specified to the nearest object it detects
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