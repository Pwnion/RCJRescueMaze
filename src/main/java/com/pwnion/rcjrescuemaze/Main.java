package com.pwnion.rcjrescuemaze;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.pwnion.rcjrescuemaze.global.searching.pathing.drivers.Ultrasonic;

public class Main {

	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new MainBinder());
	}
	
	Ultrasonic testObject = new Ultrasonic("front");

}
