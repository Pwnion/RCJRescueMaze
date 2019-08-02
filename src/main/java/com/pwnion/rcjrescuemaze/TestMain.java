package com.pwnion.rcjrescuemaze;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.pwnion.rcjrescuemaze.binders.MainBinder;
import com.pwnion.rcjrescuemaze.hardware.DrivingMotors;
import com.pwnion.rcjrescuemaze.hardware.Move;

public class TestMain {
	@Inject
	private static DrivingMotors move;
	
	//@Inject
	//private static Pins pins;
	
	//@Inject
	//private static Ultrasonic ultrasonic;
	
	public static void main(String[] args) throws InterruptedException {
		Injector injector = Guice.createInjector(new MainBinder());

		move = injector.getInstance(Move.class);
		//pins = injector.getInstance(Pins.class);
		//ultrasonic = injector.getInstance(Ultrasonic.class);
		
		/*
		while(true) {
			for(float value : ultrasonic.rawSensorOutput().values()) {
				System.out.println(value);
			}
			Thread.sleep(1000);
		}
		*/
		
		//pins.sendPin.high();
		
		/*
		
		pins.stepperPins.get("IN1").high();
		pins.stepperPins.get("IN3").high();
		Thread.sleep(2000);
		pins.stepperPins.get("IN1").low();
		pins.stepperPins.get("IN3").low();
		
		pins.stepperPins.get("IN2").high();
		pins.stepperPins.get("IN4").high();
		Thread.sleep(2000);
		pins.stepperPins.get("IN2").low();
		pins.stepperPins.get("IN4").low();
		
		
		pins.stepperPins.get("IN1").high();
		pins.stepperPins.get("IN2").high();
		Thread.sleep(2000);
		pins.stepperPins.get("IN1").low();
		pins.stepperPins.get("IN2").low();
		
		pins.stepperPins.get("IN3").high();
		pins.stepperPins.get("IN4").high();
		Thread.sleep(2000);
		pins.stepperPins.get("IN3").low();
		pins.stepperPins.get("IN4").low();
		*/
		
		/*
		while(true) {
			pins.stepperPins.get("IN2").high();
			pins.stepperPins.get("IN3").high();
			Thread.sleep(50);
			pins.stepperPins.get("IN2").low();
			pins.stepperPins.get("IN3").low();
		}
		*/
		
		/*
		pins.stepperPins.get("IN2").high();
		pins.stepperPins.get("IN3").high();
		Thread.sleep(2000);
		pins.stepperPins.get("IN2").low();
		pins.stepperPins.get("IN3").low();
		*/
		
		move.go("up");
		move.go("down");
		move.go("left");
		move.go("right");
		
	}
}