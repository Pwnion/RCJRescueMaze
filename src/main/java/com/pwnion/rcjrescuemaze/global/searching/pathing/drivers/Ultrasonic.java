package com.pwnion.rcjrescuemaze.global.searching.pathing.drivers;

import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class Ultrasonic implements Pins, GpioPinListenerDigital {
	//Position of ultrasonic sensor to obtain distance from
	final String position;
	boolean echoStateChanged;
	
	Ultrasonic(String position) {
		this.position = position;
	}
	
	@Override
	public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
		echoStateChanged = true;
	}
	
	long startTime, endTime;
	
	int getDistance() {
		sendPins.get(position).pulse(10);
		startTime = System.currentTimeMillis();
		while(!echoStateChanged) {
			if(System.currentTimeMillis() - startTime > 4081);
		}
	}
}
