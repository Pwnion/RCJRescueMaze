package com.pwnion.rcjrescuemaze.global.searching;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.pwnion.rcjrescuemaze.SharedData;
import com.pwnion.rcjrescuemaze.UnvisitedTileData;
import com.pwnion.rcjrescuemaze.global.searching.pathing.drivers.Ultrasonic;

public class SearchArea {
	private final SharedData sharedData;
	private final Ultrasonic ultrasonic;
	
	@Inject
	SearchArea(SharedData sharedData, Ultrasonic ultrasonic) {
		this.sharedData = sharedData;
		this.ultrasonic = ultrasonic;
	}
	
	
	
	public ArrayList<Boolean> search() {
		ArrayList<Boolean> walls = new ArrayList<Boolean>();
		
		//Search using sensors to find walls and hence find unvisited tiles
		Integer count = 0;
		for (Integer distance : ultrasonic.getDistances().values()) {
			if (distance <= 22 && distance != -1) {
				walls.add(true);
			} else {
				walls.add(false);
				ultrasonic.getDistances().keySet()
					switch(direction) {
						case "front":
							newPos.addY(1);
							break;
						case "back":
							newPos.addY(-1);
							break;
						case "left":
							newPos.addX(-1);
							break;
						case "right":
							newPos.addX(1);
							break;
					}
			}
			count += 1;
		}
		return walls;
	}
}
