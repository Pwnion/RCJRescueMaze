package com.pwnion.rcjrescuemaze.binders;

import com.google.inject.AbstractModule;
import com.pwnion.rcjrescuemaze.hardware.Colour;
import com.pwnion.rcjrescuemaze.hardware.DispenserMotor;
import com.pwnion.rcjrescuemaze.hardware.DrivingMotors;
import com.pwnion.rcjrescuemaze.hardware.FindSurvivors;
import com.pwnion.rcjrescuemaze.hardware.FindWalls;
import com.pwnion.rcjrescuemaze.hardware.FindColour;
import com.pwnion.rcjrescuemaze.hardware.Infared;
import com.pwnion.rcjrescuemaze.hardware.Pins;
import com.pwnion.rcjrescuemaze.hardware.Ultrasonic;
import com.pwnion.rcjrescuemaze.software.MoveToCoords;
import com.pwnion.rcjrescuemaze.software.Pathing;
import com.pwnion.rcjrescuemaze.software.SharedData;

public class MainBinder extends AbstractModule {
    
    //Configure classes that use the Guice Dependency Injection Framework
    @Override
    protected void configure() {
    	bind(Pins.class).asEagerSingleton();
    	bind(SharedData.class).asEagerSingleton();
    	bind(DispenserMotor.class).asEagerSingleton();
    	bind(DrivingMotors.class).asEagerSingleton();
    	
    	bind(Ultrasonic.class).to(FindWalls.class).asEagerSingleton();
    	bind(Colour.class).to(FindColour.class).asEagerSingleton();
    	bind(Infared.class).to(FindSurvivors.class).asEagerSingleton();
    	
    	bind(Pathing.class).to(MoveToCoords.class).asEagerSingleton();;
    }
}
