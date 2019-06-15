package com.pwnion.rcjrescuemaze.global.searching;

public class Searching {
	//Look through tiles and find which tile is the best to visit
	//Will find Distance from unvisited tiles and find which one is the closest.
	
	//Pseudo Code: (in sequential order)
	//
	//Search adjacent tiles to find obstacles, walls and unvisited tiles
	//Add any found items to respective lists
	//
	//Calculate any new corners found and add to list
	//
	//Calculate distance to unvisited tiles and update each with new distance value (Uses Pathing.java functions)
	//
	//Find closest unvisited tiles
	//Randomly chose from closest unvisited tiles and send value to Pathing to move to.
	//
	//End
}
