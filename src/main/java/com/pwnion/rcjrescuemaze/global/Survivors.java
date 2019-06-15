package com.pwnion.rcjrescuemaze.global;

import com.google.inject.Inject;
import com.pwnion.rcjrescuemaze.SharedData;

public class Survivors {
	//Search for any survivors
	
	SharedData sharedData;
	
	@Inject
	Survivors(SharedData sharedData) {
		this.sharedData = sharedData;
	}
}
