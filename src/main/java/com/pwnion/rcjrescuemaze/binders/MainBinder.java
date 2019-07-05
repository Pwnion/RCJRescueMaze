package com.pwnion.rcjrescuemaze.binders;

import com.google.inject.AbstractModule;
import com.pwnion.rcjrescuemaze.hardware.ImplUltrasonic;
import com.pwnion.rcjrescuemaze.hardware.Pins;
import com.pwnion.rcjrescuemaze.hardware.Ultrasonic;
import com.pwnion.rcjrescuemaze.software.SharedData;

public class MainBinder extends AbstractModule {
    
    //Configure classes that use the Guice Dependency Injection Framework
    @Override
    protected void configure() {
    	bind(Pins.class).asEagerSingleton();
    	bind(SharedData.class).asEagerSingleton();
    	
    	bind(Ultrasonic.class).to(ImplUltrasonic.class);
    }
}
