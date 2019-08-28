package com.pwnion.rcjrescuemaze.hardware;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.inject.Singleton;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

@Singleton
public class Pins {
	
	/*
	 * IR TEMP PINS
	 */
	
	public final int I2CBUS = 1;

	//Slave addresses
	public final ArrayList<Byte> I2CADDRS = new ArrayList<Byte>() {
		private static final long serialVersionUID = 1L;
		{
			add((byte) 0x5A); //1
			add((byte) 0x5B); //2
			add((byte) 0x5C); //3
			add((byte) 0x5D); //4
		}
	};

	//RAM
	public final byte RAWIR1 = (byte) 0x04;
	public final byte RAWIR2 = (byte) 0x05;
	public final byte TA = (byte) 0x06;
	public final byte TOBJ1 = (byte) 0x07;
	public final byte TOBJ2 = (byte) 0x08;
	
	//EEPROM
	public final byte TOMAX = (byte) 0x20;
	public final byte TOMIN = (byte) 0x21;
	public final byte PWMCTRL = (byte) 0x22;
	public final byte TARANGE = (byte) 0x23;
	public final byte EMISS = (byte) 0x24;
	public final byte CONFIG = (byte) 0x25;
	public final byte ADDR = (byte) 0x0E;
	public final byte ID1 = (byte) 0x3C;
	public final byte ID2 = (byte) 0x3D;
	public final byte ID3 = (byte) 0x3E;
	public final byte ID4 = (byte) 0x3F;
	
	//Set all pin shutdown options
	public Pins() {
		sendPin.setShutdownOptions(true, PinState.LOW);
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
		speedPin.setShutdownOptions(true, PinState.LOW);
		
		buttonPin.setShutdownOptions(true, PinState.LOW);
		
		tiltPin.setShutdownOptions(true, PinState.LOW);
	}
		
	//Initialise GpioController for the Pi4J Library
	private final GpioController gpio = GpioFactory.getInstance();
	
	public final GpioPinDigitalOutput buttonPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_28);
	public final GpioPinDigitalOutput tiltPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29);
	
	//Stepper Motor IN(1/2/3/4) Pins
	public final HashMap<String, GpioPinDigitalOutput> stepperPins = new HashMap<String, GpioPinDigitalOutput>() {
		private static final long serialVersionUID = 1L;
		{
			put("IN1", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21));
			put("IN2", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22));
			put("IN3", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25));
			put("IN4", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27));
		}
	};
	
	//Provision digital ultrasonic output (trig) pins and populate a hashmap with them based on their position on the robot
	public final GpioPinDigitalOutput sendPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12);
	
	//Provision digital ultrasonic input (echo) pins and populate a hashmap with them based on their position on the robot
	public final HashMap<String, GpioPinDigitalInput> receivePins = new HashMap<String, GpioPinDigitalInput>() {
		private static final long serialVersionUID = 1L;
		{
			put("front", gpio.provisionDigitalInputPin(RaspiPin.GPIO_13));
			put("left", gpio.provisionDigitalInputPin(RaspiPin.GPIO_14));
			put("back", gpio.provisionDigitalInputPin(RaspiPin.GPIO_15));
			put("right", gpio.provisionDigitalInputPin(RaspiPin.GPIO_16));
		}
	};
	
	//Provision digital motor output (IN(1/3)) pins and populate a hashmap with them based on their position on the robot
	public final HashMap<String, GpioPinDigitalOutput> clockwisePins = new HashMap<String, GpioPinDigitalOutput>() {
		private static final long serialVersionUID = 1L;
		{
			put("front_left", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02));
			put("front_right", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07));
			put("back_right", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_11));
			put("back_left", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04));
		}
	};
	
	//Provision digital motor output (IN(2/4)) pins and populate a hashmap with them based on their position on the robot
	public final HashMap<String, GpioPinDigitalOutput> anticlockwisePins = new HashMap<String, GpioPinDigitalOutput>() {
		private static final long serialVersionUID = 1L;
		{
			put("front_left", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00));
			put("front_right", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06));
			put("back_right", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_10));
			put("back_left", gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05));
		}
	};
	
	//Provision the pin that controls all EN(A/B) pins
	public final GpioPinPwmOutput speedPin = gpio.provisionPwmOutputPin(RaspiPin.GPIO_26);
}