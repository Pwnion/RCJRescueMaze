package com.pwnion.rcjrescuemaze;

import com.google.inject.AbstractModule;
import com.pwnion.rcjrescuemaze.global.searching.pathing.drivers.Ultrasonic;
import com.pwnion.rcjrescuemaze.global.Survivors;
import com.pwnion.rcjrescuemaze.global.searching.Searching;
import com.pwnion.rcjrescuemaze.global.searching.pathing.Pathing;
import com.pwnion.rcjrescuemaze.global.searching.pathing.drivers.DrivingMotors;
import com.pwnion.rcjrescuemaze.global.searching.pathing.drivers.Pins;

public class MainBinder extends AbstractModule {
    
    //Configure classes that use the Guice Dependency Injection Framework
    @Override
    protected void configure() {
    	bind(Pins.class).asEagerSingleton();
    	bind(SharedData.class).asEagerSingleton();
    	
    	bind(Pins.class).to(Ultrasonic.class);
    	bind(Pins.class).to(DrivingMotors.class);
    	bind(SharedData.class).to(Main.class);
    	bind(SharedData.class).to(Pathing.class);
    	bind(SharedData.class).to(Searching.class);
    	bind(SharedData.class).to(Survivors.class);
    	
    }
}
