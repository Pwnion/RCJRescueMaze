package com.pwnion.rcjrescuemaze.hardware;

import java.util.HashMap;

import com.google.inject.Singleton;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.RaspiPin;

@Singleton
public class Pins {
	//Initialise GpioController for the Pi4J Library
	private final GpioController gpio = GpioFactory.getInstance();
	
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
	
	//Provision PWM motor output (EN(A/B)) pins and populate a hashmap with them based on their position on the robot
	final HashMap<String, GpioPinPwmOutput> speedPins = new HashMap<String, GpioPinPwmOutput>() {
		private static final long serialVersionUID = 1L;
		{
			put("front_left", gpio.provisionSoftPwmOutputPin(RaspiPin.GPIO_01));
			put("back_left", gpio.provisionSoftPwmOutputPin(RaspiPin.GPIO_24));
			put("back_right", gpio.provisionSoftPwmOutputPin(RaspiPin.GPIO_26));
			put("front_right", gpio.provisionSoftPwmOutputPin(RaspiPin.GPIO_23));
		}
	};
}