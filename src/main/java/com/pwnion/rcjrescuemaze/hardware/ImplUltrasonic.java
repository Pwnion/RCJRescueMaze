package com.pwnion.rcjrescuemaze.hardware;

import java.util.ArrayList;

public class ImplUltrasonic extends Ultrasonic {
	public ImplUltrasonic(Pins pins) {
		super(pins);
	}
	
	@Override
	public ArrayList<Boolean> findWalls() {
		ArrayList<Boolean> walls = new ArrayList<Boolean>();
		
		for(String position : super.rawSensorOutput().keySet()) {
			walls.add(super.rawSensorOutput().get(position) != -1 ? true : false);
		}
		
		return walls;
	}
}
