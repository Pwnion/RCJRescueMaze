package com.pwnion.rcjrescuemaze.hardware;

import java.io.IOException;

import com.google.inject.Inject;

public class GetColour extends Camera {
	int avgColours[];
	
	@Inject
	public GetColour() throws IOException {
		this.avgColours = super.getAvgColours();
	}
	
	@Override
	public int[] getAvgColours() {
		return avgColours;
	}
	
	@Override
	public String get() {
		return "silver";
	}
}
