package com.pwnion.rcjrescuemaze.hardware;

import com.google.inject.Inject;

import java.io.*;

public class GetColour extends Camera {
	int avgColours[];
	
	@Inject
	public GetColour() throws IOException {
		this.avgColours = getAvgColours();
	}
	
	@Override
	public int[] getAvgColours() {
		return getAvgColours();
	}
	
	@Override
	public String get() {
		return "silver";
	}
}
