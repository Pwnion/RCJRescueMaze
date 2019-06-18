package com.pwnion.rcjrescuemaze.global.searching.pathing;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.inject.Inject;
import com.pwnion.rcjrescuemaze.SharedData;
import com.pwnion.rcjrescuemaze.global.searching.pathing.drivers.DrivingMotors;

public class Pathing {
	//Move from point A to B
	//Requires Knowledge on walls and tiles visited
	
	private final SharedData sharedData;
	private final DrivingMotors drivingMotors;
	
	@Inject
	Pathing(SharedData sharedData, DrivingMotors drivingMotors) {
		this.sharedData = sharedData;
		this.drivingMotors = drivingMotors;
	}
	
	public ArrayList<String> generatePath(HashMap<Integer, Integer> coords) {
		//{Function: Generate Path from [A] to [B]
		//(Insert Pathing Algorithm) Generates Path
		//}Return [Path]
		ArrayList<String> Path = null;
		return Path; 
	}

	public void moveByPath(ArrayList<String> Path) {//{Function: Move using [Path]
		for (String direction : Path) {
			drivingMotors.move(direction);

			//Log any discrepancies with rotation or position 
			//If over tolerance levels repathing may be required

			//After moving 1 tile check for Silver tiles and update last visited silver (if required)
			//(This process does not happen physically and just checks Silver tile list vs Current Position)
			if (sharedData.getCurrentPos().equals(sharedData.getLastSilverTile())) {
				sharedData.setLastSilverTile(sharedData.getCurrentPos());
			}

		}//Repeat until end of path
	}
}
