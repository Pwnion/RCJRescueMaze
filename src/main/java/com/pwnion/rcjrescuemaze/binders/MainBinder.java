package com.pwnion.rcjrescuemaze.binders;

import com.google.inject.AbstractModule;
import com.pwnion.rcjrescuemaze.hardware.Colour;
import com.pwnion.rcjrescuemaze.hardware.DispenserMotor;
import com.pwnion.rcjrescuemaze.hardware.DrivingMotors;
import com.pwnion.rcjrescuemaze.hardware.ImplColour;
import com.pwnion.rcjrescuemaze.hardware.ImplInfared;
import com.pwnion.rcjrescuemaze.hardware.ImplUltrasonic;
import com.pwnion.rcjrescuemaze.hardware.Infared;
import com.pwnion.rcjrescuemaze.hardware.Pins;
import com.pwnion.rcjrescuemaze.hardware.Ultrasonic;
import com.pwnion.rcjrescuemaze.software.ImplPathing;
import com.pwnion.rcjrescuemaze.software.Pathing;
import com.pwnion.rcjrescuemaze.software.Searching;
import com.pwnion.rcjrescuemaze.software.SharedData;

public class MainBinder extends AbstractModule {
    
    //Configure classes that use the Guice Dependency Injection Framework
    @Override
    protected void configure() {
    	bind(Pins.class).asEagerSingleton();
    	bind(SharedData.class).asEagerSingleton();
    	bind(DispenserMotor.class).asEagerSingleton();
    	bind(DrivingMotors.class).asEagerSingleton();
    	bind(Searching.class).asEagerSingleton();
    	
    	bind(Ultrasonic.class).to(ImplUltrasonic.class).asEagerSingleton();
    	bind(Colour.class).to(ImplColour.class).asEagerSingleton();
    	bind(Infared.class).to(ImplInfared.class).asEagerSingleton();
    	
    	bind(Pathing.class).to(ImplPathing.class).asEagerSingleton();;
    }
}
