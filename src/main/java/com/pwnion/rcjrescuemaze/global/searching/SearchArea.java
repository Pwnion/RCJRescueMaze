package com.pwnion.rcjrescuemaze.global.searching;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.pwnion.rcjrescuemaze.Coords;
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
		Coords coords = sharedData.getCurrentPos();
		
		for(String position : ultrasonic.outputs().keySet()) {
			walls.add(ultrasonic.outputs().get(position) != -1 ? true : false);
			switch(position) {
			case "front":
				coords.addY(1);
				break;
			case "left":
				coords.addX(-1);
				break;
			case "back":
				coords.addY(-1);
				break;
			case "right":
				coords.addX(1);
				break;
			}
			sharedData.appendUnvisited(new UnvisitedTileData(coords, 1));
		}
		return walls;
	}
}
