package com.pwnion.rcjrescuemaze.hardware;

import java.util.ArrayList;

public interface SurvivorFactory {
	GetSurvivors create(ArrayList<Boolean> walls);
}
