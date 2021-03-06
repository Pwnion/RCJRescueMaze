package com.pwnion.rcjrescuemaze.software;

import com.google.inject.Inject;
import com.pwnion.rcjrescuemaze.datatypes.UnvisitedTileData;

public class Searching {
	private final SharedData sharedData;
	private final Pathing pathing;
	
	@Inject
	Searching(SharedData sharedData, ImplPathing pathing) {
		this.sharedData = sharedData;
		this.pathing = pathing;
	}
	
	
	//Look through tiles and find which tile is the best to visit
	//Will find Distance from unvisited tiles and find which one is the closest.
	public void findMoveUnvisited() {
		
		UnvisitedTileData closestTile = sharedData.getUnvisited().get(0);
	
		//Find closest unvisited tile
		for(UnvisitedTileData unvisitedTileData : sharedData.getUnvisited()) {
			if(unvisitedTileData.getDistance() < closestTile.getDistance()) {
				closestTile = unvisitedTileData;
				break;
			}	
		}
		
		//Randomly chose from closest unvisited tiles and send value to Pathing to move to.
		pathing.moveToCoords(closestTile.getCoords());
	}
}