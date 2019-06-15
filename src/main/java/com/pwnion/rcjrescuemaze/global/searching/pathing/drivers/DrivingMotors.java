package com.pwnion.rcjrescuemaze.global.searching.pathing.drivers;

import com.google.inject.Inject;
	
public class DrivingMotors {
	Pins pins;
	
	@Inject
	DrivingMotors(Pins pins) {
		this.pins = pins;
	}
	
	//Vroom Vroom
	
	//{Function: (Move 1 tile in [Direction])
	//
	//Move using motors 
	//Check using colour sensors for black tiles
	//If found Move back and set that tile as visited and set walls around it
	//
	//Update Current Position (if required)}
}

