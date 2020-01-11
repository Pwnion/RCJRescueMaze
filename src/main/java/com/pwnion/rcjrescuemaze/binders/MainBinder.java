package com.pwnion.rcjrescuemaze.binders;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.pwnion.rcjrescuemaze.hardware.RGBFromImage;
import com.pwnion.rcjrescuemaze.hardware.ColourFactory;
import com.pwnion.rcjrescuemaze.hardware.DispenserMotor;
import com.pwnion.rcjrescuemaze.hardware.DrivingMotors;
import com.pwnion.rcjrescuemaze.hardware.GetSurvivors;
import com.pwnion.rcjrescuemaze.hardware.GetWalls;
import com.pwnion.rcjrescuemaze.hardware.GetColour;
import com.pwnion.rcjrescuemaze.hardware.Infrared;
import com.pwnion.rcjrescuemaze.hardware.Move;
import com.pwnion.rcjrescuemaze.hardware.Pins;
import com.pwnion.rcjrescuemaze.hardware.Ultrasonic;
import com.pwnion.rcjrescuemaze.software.MoveToCoords;
import com.pwnion.rcjrescuemaze.software.Pathing;
import com.pwnion.rcjrescuemaze.software.SharedData;

public class MainBinder extends AbstractModule implements Module {
    
    //Configure classes that use the Guice Dependency Injection Framework
    @Override
    protected void configure() {
    	bind(Pins.class).asEagerSingleton();
    	bind(DispenserMotor.class).asEagerSingleton();
    	
    	bind(SharedData.class).asEagerSingleton();
    	
    	bind(Ultrasonic.class).to(GetWalls.class);
    	bind(DrivingMotors.class).to(Move.class);
    	
    	bind(Pathing.class).to(MoveToCoords.class);
    	
    	bind(Infrared.class).to(GetSurvivors.class);
    	
    	install(new FactoryModuleBuilder()
    			.implement(RGBFromImage.class, GetColour.class)
    			.build(ColourFactory.class));
    }
}
