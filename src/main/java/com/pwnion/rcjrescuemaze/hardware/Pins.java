package com.pwnion.rcjrescuemaze.hardware;

import java.io.IOException;
import java.util.HashMap;

import com.google.inject.Singleton;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

@Singleton
public class Pins {
	
	//Initialise GpioController for the Pi4J Library
	private final GpioController gpio = GpioFactory.getInstance();
	
	//Set all pin shutdown options
	public Pins() {
		for(GpioPinDigitalOutput pin : sendPins.values()) {
			pin.setShutdownOptions(true, PinState.LOW);
		}
		for(GpioPinDigitalInput pin : receivePins.values()) {
			pin.setShutdownOptions(true, PinState.LOW);
		}
		for(GpioPinDigitalOutput pin : clockwisePins.values()) {
			pin.setShutdownOptions(true, PinState.LOW);
		}
		for(GpioPinDigitalOutput pin : anticlockwisePins.values()) {
			pin.setShutdownOptions(true, PinState.LOW);
		}
	}
	
	//Provision digital ultrasonic output (trig) pins and populate a hashmap with them based on their position on the robot
	final HashMap<String, GpioPinDigitalOutput> sendPins = new HashMap<String, GpioPinDigitalOutput>() {
		GpioPinDigitalOutput sharedTrigPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12);
		private static final long serialVersionUID = 1L;
		{
			put("front", sharedTrigPin);
			put("left", sharedTrigPin);
			put("back", sharedTrigPin);
			put("right", sharedTrigPin);
		}
	};
	
	//Provision digital ultrasonic input (echo) pins and populate a hashmap with them based on their position on the robot
	final HashMap<String, GpioPinDigitalInput> receivePins = new HashMap<String, GpioPinDigitalInput>() {
		private static final long serialVersionUID = 1L;
		{
			put("front", gpio.provisionDigitalInputPin(RaspiPin.GPIO_13));
			put("left", gpio.provisionDigitalInputPin(RaspiPin.GPIO_14));
			put("back", gpio.provisionDigitalInputPin(RaspiPin.GPIO_15));
			put("right", gpio.provisionDigitalInputPin(RaspiPin.GPIO_16));
		}
	};
	
	//Provision digital motor output (IN(1/3)) pins and populate a hashmap with them based on their position on the robot
	final HashMap<String, GpioPinDigitalOutput> clockwisePins = new HashMap<String, GpioPinDigitalOutput>() {
		private static final long serialVersionUID = 1L;
		{
			put("front_left", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02));
			put("back_left", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06));
			put("back_right", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_10));
			put("front_right", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04));
		}
	};
	
	//Provision digital motor output (IN(2/4)) pins and populate a hashmap with them based on their position on the robot
	final HashMap<String, GpioPinDigitalOutput> anticlockwisePins = new HashMap<String, GpioPinDigitalOutput>() {
		private static final long serialVersionUID = 1L;
		{
			put("front_left", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03));
			put("back_left", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07));
			put("back_right", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_11));
			put("front_right", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05));
		}
	};
	
	//Map motor directions to (EN(A/B)) pins based on their BCM numbering
	final HashMap<String, Integer> speedPins = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;
		{
			put("front_left", 18);
			put("back_left", 19);
			put("back_right", 12);
			put("front_right", 13);
		}
	};
	
	//Method to set value for (EN(A/B)) pins
	public final void setSpeedPins(int speed) {
		try {
			for(String direction : speedPins.keySet()) {
				new ProcessBuilder("/bin/sh", "-c", "echo \"" + speedPins.get(direction) + "=" + ((float) speed / 100) + "\" > /dev/pi-blaster").start();
			}
		} catch (IOException e) {
			System.out.println("Couldn't set value for software PWM pin");
			e.printStackTrace();
		}
	}
}