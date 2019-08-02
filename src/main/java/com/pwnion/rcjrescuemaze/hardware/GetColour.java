package com.pwnion.rcjrescuemaze.hardware;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GetColour extends Colour {
	private String colour;
	
	private String getColour() {
		return "silver";
	}
	
	@Inject
	public GetColour(Pins pins) {
		super(pins);
		
		this.colour = getColour();
	}
	
	@Override
	public String get() {
		return colour;
	}
}
