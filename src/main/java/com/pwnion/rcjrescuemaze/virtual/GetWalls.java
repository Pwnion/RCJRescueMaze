package com.pwnion.rcjrescuemaze.virtual;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import com.google.inject.Inject;
import com.pwnion.rcjrescuemaze.datatypes.Coords;
import com.pwnion.rcjrescuemaze.software.SharedData1;

public class GetWalls {
	private ArrayList<Boolean> walls;
	
	@Inject
	public GetWalls(SharedData1 sharedData) {
		ArrayList<Boolean> walls = new ArrayList<Boolean>();
		ArrayList<String> file = new ArrayList<String>();
		Coords pos = sharedData.getCurrentPos();
		
		try (Stream<String> stream = Files.lines(Paths.get(""))) {
	        stream.forEach(file::add);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(String line : file) {
			if(line.contains(Integer.toString(pos.getX()) + "," + Integer.toString(pos.getY()))) {
				String locLine = line.substring(line.indexOf("|" + 1));
				for(int i = 0; i < 8; i += 2) {
					walls.add(locLine.toCharArray()[i] == 1 ? true : false);
				}
			}
		}
		
		this.walls = walls;
	}
	
	public ArrayList<Boolean> get() { return walls; }
	
	public boolean get(int i) { return walls.get(i); };
	
}