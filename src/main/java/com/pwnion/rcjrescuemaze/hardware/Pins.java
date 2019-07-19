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
	
	/*
	 * IR TEMP PINS
	 */
	
	//Slave Addresses
	final byte I2CADDR1 = (byte) 0x5A;
	final byte I2CADDR2 = (byte) 0x5A;
	final byte I2CADDR3 = (byte) 0x5A;
	final byte I2CADDR4 = (byte) 0x5A;

	//RAM
	final byte RAWIR1 = (byte) 0x04;
	final byte RAWIR2 = (byte) 0x05;
	final byte TA = (byte) 0x06;
	final byte TOBJ1 = (byte) 0x07;
	final byte TOBJ2 = (byte) 0x08;
	
	//EEPROM
	final byte TOMAX = (byte) 0x20;
	final byte TOMIN = (byte) 0x21;
	final byte PWMCTRL = (byte) 0x22;
	final byte TARANGE = (byte) 0x23;
	final byte EMISS = (byte) 0x24;
	final byte CONFIG = (byte) 0x25;
	final byte ADDR = (byte) 0x0E;
	final byte ID1 = (byte) 0x3C;
	final byte ID2 = (byte) 0x3D;
	final byte ID3 = (byte) 0x3E;
	final byte ID4 = (byte) 0x3F;
	
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
		for(GpioPinDigitalOutput pin : stepperPins.values()) {
			pin.setShutdownOptions(true, PinState.LOW);
		}
		tiltPin.setShutdownOptions(true, PinState.LOW);
	}
		
	//Initialise GpioController for the Pi4J Library
	private final GpioController gpio = GpioFactory.getInstance();
	
	//Tilt Sensor OUT Pin
	final GpioPinDigitalInput tiltPin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_28);
	
	//Stepper Motor IN(1/2/3/4) Pins
	final HashMap<String, GpioPinDigitalOutput> stepperPins = new HashMap<String, GpioPinDigitalOutput>() {
		private static final long serialVersionUID = 1L;
		{
			put("IN1", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21));
			put("IN2", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22));
			put("IN3", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25));
			put("IN4", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27));
		}
	};
	
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
	
	//Method to set value for (EN(A/B)) pins
	public final void setSpeedPins(int speed) {
		
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