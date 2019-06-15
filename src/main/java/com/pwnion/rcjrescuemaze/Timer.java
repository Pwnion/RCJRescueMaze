package com.pwnion.rcjrescuemaze;

public class Timer {
	boolean timer(int ms) {
		long startTime = System.currentTimeMillis();
		while(System.currentTimeMillis() != startTime + ms);
		return true;
	}
}
