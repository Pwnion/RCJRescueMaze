package com.pwnion.rcjrescuemaze.software;

import java.util.ArrayList;
import java.util.HashMap;

import com.pwnion.rcjrescuemaze.datatypes.Coords;
import com.pwnion.rcjrescuemaze.hardware.DrivingMotors;

public class ImplPathing extends Pathing {
	
	public ImplPathing(SharedData sharedData, DrivingMotors drivingMotors) {
		super(sharedData, drivingMotors);
	}
	
	@Override
	public HashMap<Coords, Integer> generateMap() {
		return super.generateMap();
	}
	
	/*
	Moves the robot along the path generated by the generatePath() function, and
	terminates when the robot gets to the given coords
	*/
	@Override
	public void moveToCoords(Coords coords) {
		ArrayList<String> path = super.generatePath(super.generateMap(), coords);
		for(int i = path.size() - 1; i == 0; i--) {
			//Move one tile in the correct direction determined
			drivingMotors.move(path.get(i));
			
			//Update current position of the robot
			Coords newPos = sharedData.getCurrentPos();
			switch(path.get(i)) {
			  case "up":
				newPos.addY(1);
			    break;
			  case "down":
			    newPos.addY(-1);
			    break;
			  case "left":
				newPos.addX(-1);
				break;
			  case "right":
				newPos.addX(1);
				break;
			}
			sharedData.setCurrentPos(newPos);

			//Log any discrepancies with rotation or position 
			//If over tolerance levels repathing may be required

			//After moving 1 tile check for Silver tiles and update last visited silver (if required)
			//(This process does not happen physically and just checks Silver tile list vs Current Position)
			if(sharedData.getVisited().get(sharedData.getVisitedIndex(coords)).getSilverTile()) {
				sharedData.setLastSilverTile(sharedData.getCurrentPos());
			}

		}//Repeat until end of path
	}
}
