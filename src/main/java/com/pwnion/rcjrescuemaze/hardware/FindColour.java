package com.pwnion.rcjrescuemaze.hardware;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class FindColour extends Colour {
	
	@Inject
	public FindColour(Pins pins) {
		super(pins);
	}

	@Override
	public String get() {
		return "silver";
	}
}
