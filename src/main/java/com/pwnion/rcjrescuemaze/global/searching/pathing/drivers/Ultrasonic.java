package com.pwnion.rcjrescuemaze.global.searching.pathing.drivers;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class Ultrasonic implements GpioPinListenerDigital {
	//Position of ultrasonic sensor to obtain distance from
	private final String position;
	
	private final Pins pins;
	
	//Boolean that is true when the ultrasonic sensor receives the sound wave back
	private boolean echoStateChanged;
	
	//Constructor that takes the position of the ultrasonic sensor on the robot as a parameter
	@Inject
	public Ultrasonic(@Assisted String position, Pins pins) {
		this.position = position;
		this.pins = pins;
	}
	
	//Listener for the echo pin changing states
	@Override
	public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
		echoStateChanged = true;
	}
	
	//Obtains the distance in cm from the ultrasonic sensor specified to the nearest object it detects
	public int getDistance() {
		pins.sendPins.get(position).pulse(10);
		long startTime = System.currentTimeMillis();
		while(!echoStateChanged) {
			if(System.currentTimeMillis() - startTime > 66); return -1;
		}
		return (int) (((System.currentTimeMillis() - startTime) * 1000) * 171.5);
	}
}