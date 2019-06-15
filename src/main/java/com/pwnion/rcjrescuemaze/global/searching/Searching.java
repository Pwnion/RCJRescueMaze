package com.pwnion.rcjrescuemaze.global.searching;

import com.google.inject.Inject;
import com.pwnion.rcjrescuemaze.SharedData;
import com.pwnion.rcjrescuemaze.UnvisitedTileData;

public class Searching {
	SharedData sharedData;
	
	@Inject
	Searching(SharedData sharedData) {
		this.sharedData = sharedData;
	}
	
	//Look through tiles and find which tile is the best to visit
	//Will find Distance from unvisited tiles and find which one is the closest.
	public void findMoveUnvisited() {
		
	}
	
	//Find closest unvisited tile
	UnvisitedTileData closestTile = sharedData.getUnvisited().get(0);
	
	for(UnvisitedTileData unvisitedTileData : sharedData.getUnvisited()) {
		if(unvisitedTileData.getDistance() < closestTile.getDistance()) {
			closestTile = unvisitedTileData;
		}
	}
		
	//Randomly chose from closest unvisited tiles and send value to Pathing to move to.
	//moveByPath(generatePath(closestTile.getCoords()));
}
