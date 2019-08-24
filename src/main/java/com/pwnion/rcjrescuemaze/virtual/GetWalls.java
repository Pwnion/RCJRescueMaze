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
		
		try (Stream<String> stream = Files.lines(Paths.get("/home/pi/Maze.txt"))) {
	        stream.forEach(file::add);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(String line : file) {
			int pipe = line.indexOf("|");
			if(pipe == -1) {
				continue;
			}
			String X = "";
			String Y = "";
			int comma = line.indexOf(",");
			for(int i = 0; i < line.indexOf("|"); i++) {
				if(comma > i) {
					X += line.toCharArray()[i];
				} else if(comma < i) {
					Y += line.toCharArray()[i];
				}
			}
			if(!X.isEmpty() && !Y.isEmpty()) {
				if(pos.compare(X, Y)) {
					String locLine = line.substring(pipe + 1);
					for(int i = 0; i < 8; i += 2) {
						walls.add(locLine.toCharArray()[i] == "1".toCharArray()[0] ? true : false);
					}
					System.out.println(line + " locline = " + locLine);
					this.walls = walls;
				}
			}

		}
		
		//this.walls = walls;
	}
	
	public ArrayList<Boolean> get() { return walls; }
	
	public boolean get(int i) { return walls.get(i); };
	
}