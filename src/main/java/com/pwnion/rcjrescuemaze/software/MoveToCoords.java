package com.pwnion.rcjrescuemaze.software;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.pwnion.rcjrescuemaze.datatypes.Coords;
import com.pwnion.rcjrescuemaze.hardware.Move;

@Singleton
public class MoveToCoords extends Pathing {
	private final Move move;
	
	@Inject
	public MoveToCoords(SharedData1 sharedData, Move move) {
		super(sharedData);
		
		this.move = move;
	}
	
	/*
	Moves the robot along the path generated by the generatePath() function, and
	terminates when the robot gets to the given coords
	*/
	@Override
	public void moveToCoords(Coords coords, HashMap<Coords, Integer> map) {
		ArrayList<String> path = super.generatePath(map, coords);
		System.out.println("MAP: " + map.keySet() + " Map Values: " + map.values() + " Map Size = " + map.size());
		System.out.println("PATH TO STRING: " + path.toString() + " PATH SIZE: " + path.size() + " Current Position: " + sharedData.getCurrentPos());
		
		for(int i = path.size() - 1; i >= 0; i--) {
			//Move one tile in the correct direction determined
			
			System.out.println("Path position " + i + "   ***********************");
			sharedData.timeAdd(3);
			sharedData.pathAppend(path.get(i));
			
			move.go(path.get(i));
			
			//After moving 1 tile check for Silver tiles and update last visited silver (if required)
			//(This process does not happen physically and just checks Silver tile list vs Current Position)
			if(sharedData.getVisitedIndex(sharedData.getCurrentPos()) != - 1) {
				if(sharedData.getVisited().get(sharedData.getVisitedIndex(sharedData.getCurrentPos())).getSilverTile()) {
					sharedData.setLastSilverTile(sharedData.getCurrentPos());
				}
			}
			
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

			

		}//Repeat until end of path
	}
}
