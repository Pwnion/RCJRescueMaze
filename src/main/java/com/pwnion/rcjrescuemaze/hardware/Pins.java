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
		private static final long serialVersionUID = 1L;
		{
			put("front", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00));
			put("left", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00));
			put("back", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00));
			put("right", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00));
		}
	};
	
	//Provision digital ultrasonic input (echo) pins and populate a hashmap with them based on their position on the robot
	final HashMap<String, GpioPinDigitalInput> receivePins = new HashMap<String, GpioPinDigitalInput>() {
		private static final long serialVersionUID = 1L;
		{
			put("front", gpio.provisionDigitalInputPin(RaspiPin.GPIO_00));
			put("left", gpio.provisionDigitalInputPin(RaspiPin.GPIO_00));
			put("back", gpio.provisionDigitalInputPin(RaspiPin.GPIO_00));
			put("right", gpio.provisionDigitalInputPin(RaspiPin.GPIO_00));
		}
	};
	
	//Provision digital motor output (IN(1/3)) pins and populate a hashmap with them based on their position on the robot
	final HashMap<String, GpioPinDigitalOutput> clockwisePins = new HashMap<String, GpioPinDigitalOutput>() {
		private static final long serialVersionUID = 1L;
		{
			put("front_left", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00));
			put("back_left", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00));
			put("back_right", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00));
			put("front_right", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00));
		}
	};
	
	//Provision digital motor output (IN(2/4)) pins and populate a hashmap with them based on their position on the robot
	final HashMap<String, GpioPinDigitalOutput> anticlockwisePins = new HashMap<String, GpioPinDigitalOutput>() {
		private static final long serialVersionUID = 1L;
		{
			put("front_left", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00));
			put("back_left", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00));
			put("back_right", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00));
			put("front_right", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00));
		}
	};
	
	//Provision PWM motor output (EN(A/B)) pins and populate a hashmap with them based on their position on the robot
	final HashMap<String, GpioPinPwmOutput> speedPins = new HashMap<String, GpioPinPwmOutput>() {
		private static final long serialVersionUID = 1L;
		{
			put("front_left", gpio.provisionSoftPwmOutputPin(RaspiPin.GPIO_00));
			put("back_left", gpio.provisionSoftPwmOutputPin(RaspiPin.GPIO_00));
			put("back_right", gpio.provisionSoftPwmOutputPin(RaspiPin.GPIO_00));
			put("front_right", gpio.provisionSoftPwmOutputPin(RaspiPin.GPIO_00));
		}
	};
}