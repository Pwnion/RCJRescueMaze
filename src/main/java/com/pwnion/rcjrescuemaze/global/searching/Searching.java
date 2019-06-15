package com.pwnion.rcjrescuemaze.global.searching;

import java.util.ArrayList;
import java.util.HashMap;

import com.pwnion.rcjrescuemaze.SharedData;
import com.pwnion.rcjrescuemaze.UnvisitedTileData;

public class Searching implements SharedData {
	//Look through tiles and find which tile is the best to visit
	//Will find Distance from unvisited tiles and find which one is the closest.
	public static void findMoveUnvisited() {
		
		//Find closest unvisited tile
		UnvisitedTileData closestTile = unvisited.get(0);
		for (UnvisitedTileData unvisitedTile: unvisited) {
			if(unvisitedTile.getDistance() < closestTile.getDistance()) {
				closestTile.setDistance(unvisitedTile.getDistance());
			}
		}
		
		//Randomly chose from closest unvisited tiles and send value to Pathing to move to.
		moveByPath(generatePath(closestTile.getCoords()));
	}
}
