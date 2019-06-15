package com.pwnion.rcjrescuemaze;

import com.google.inject.AbstractModule;
import com.pwnion.rcjrescuemaze.global.searching.pathing.drivers.Pins;

public class MainModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(Pins.class).asEagerSingleton();
	}
}
