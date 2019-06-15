package com.pwnion.rcjrescuemaze;

import com.google.inject.AbstractModule;
import com.pwnion.rcjrescuemaze.global.searching.pathing.drivers.Pins;

public class MainBinder extends AbstractModule {
    
    //Configure classes that use the Guice Dependency Injection Framework
    @Override
    protected void configure() {
    	bind(Pins.class).asEagerSingleton();
    	bind(SharedData.class).asEagerSingleton();
    }
}
