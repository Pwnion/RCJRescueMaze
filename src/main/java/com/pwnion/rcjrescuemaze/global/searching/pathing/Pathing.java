package com.pwnion.rcjrescuemaze.global.searching.pathing;

import java.util.ArrayList;
import java.util.HashMap;

import com.pwnion.rcjrescuemaze.SharedData;

public class Pathing implements SharedData {
	//Move from point A to B
	//Requires Knowledge on walls and tiles visited
	
	public static ArrayList<String> generatePath(HashMap coords) {
		//{Function: Generate Path from [A] to [B]
		//(Insert Pathing Algorithm) Generates Path
		//}Return [Path]
		ArrayList<String> Path = null;
		return Path; 
	}

	private static void moveByPath(ArrayList<String> Path) {//{Function: Move using [Path]
		
		for (String direction: Path) {
			Move(direction);//Use path to Move 1 tile (Use DrivingMotors.java {Function: Move 1 tile in [Direction]})

			//Log any discrepancies with rotation or position 
			//If over tolerance levels repathing may be required

			//After moving 1 tile check for Silver tiles and update last visited silver (if required)
			//(This process does not happen physically and just checks Silver tile list vs Current Position)
			if (currentPostion == visted.silverTile) {
				lastSilverTile = currentPostion
			}

		}//Repeat until end of path
	}
}
