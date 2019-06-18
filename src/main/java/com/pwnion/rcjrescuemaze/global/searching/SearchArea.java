package com.pwnion.rcjrescuemaze.global.searching;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.pwnion.rcjrescuemaze.SharedData;

public class SearchArea {
	private final SharedData sharedData;
	
	@Inject
	SearchArea(SharedData sharedData) {
		this.sharedData = sharedData;
	}
	
	
	
	public ArrayList<Boolean> search() {
		ArrayList<Boolean> walls = null;
		//Search using sensors to find walls and hence find unvisited tiles
		return walls;
	}
}
