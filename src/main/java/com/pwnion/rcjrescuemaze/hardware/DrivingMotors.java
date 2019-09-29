package com.pwnion.rcjrescuemaze.hardware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.google.inject.Inject;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

public abstract class DrivingMotors {
	private final Pins pins;
	private final Ultrasonic ultrasonic;
	
	protected long globalMoveDuration = 2150;

	private final HashMap<String, String> oppDirections = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("up", "down");
			put("left", "right");
			put("down", "up");
			put("right", "left");
		}
	};
	
	@Inject
	public DrivingMotors(Pins pins, Ultrasonic ultrasonic) {
		this.pins = pins;
		this.ultrasonic = ultrasonic;
	}
	
	ArrayList<String> QueueDir = new ArrayList<String>();
	ArrayList<Long> QueueTime = new ArrayList<Long>();
	
	public void start(String mainMoveDirection, long mainMoveDuration) {
		if(!(QueueDir.isEmpty() || QueueTime.isEmpty())) {
			System.out.println("ERROR FIX PLS ######*$&(@*#%&*&@^(*@&((!*$&#(*%(*#&@)(*#@)$@*%^$&*^!@%*#%^*&@%*&^");
		}
		QueueDir.clear();
		QueueTime.clear();
		
		QueueDir.add(mainMoveDirection);
		QueueTime.add(mainMoveDuration);
		
		String direction;
		long time;
		
		while(!(QueueDir.isEmpty() || QueueTime.isEmpty())) {
			direction = QueueDir.get(0);
			time = QueueTime.get(0);
			long timeMoved = 0;
			
			if(time > 3000) {
				QueueTime.set(0, time - 3000);
				start2(direction, 3000);
			} else {
				while(timeMoved < 3000) {
					if(timeMoved + time > 3000) {
						start2(direction, 3000 - timeMoved);
						QueueTime.set(0, time - (3000 - timeMoved));
						break;
					} else {
						start2(direction, time);
					}
					timeMoved += time;
					
					QueueDir.remove(0);
					QueueTime.remove(0);
					
					if(QueueDir.isEmpty()) {
						break;
					}
					
					direction = QueueDir.get(0);
					time = QueueTime.get(0);
				}
			}
			
			//Do US stuff
			HashMap<String, Float> sensorOutput = new HashMap<String, Float>();
			try {
				sensorOutput = ultrasonic.rawSensorOutput();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			
			for(String position : sensorOutput.keySet()) {
				float dis = sensorOutput.get(position);
				if(dis > 32 && dis < 42) {
					dis -= 30;
				}
				if(dis != -1 && dis < 15) {
					if(dis > 8.5) {
						//Move(position, 100)
					} else if(dis < 5.5) {
						//move(oppDirections(position), 100)
					}
				}
			}
		}
	}
	
	private String addDir(String a, String b) {
		if(b == "up" ) {
			 b = a;
			 a = "up";	
		}
		
		if(a == "up") {
			switch(b) {
			case "right":
				return "up_right";
			case "left":
				return "up_left";
			}
		}
		
		if(b == "down" ) {
			 b = a;
			 a = "down";	
		}
		
		if(a == "down") {
			switch(b) {
			case "right":
				return "down_right";
			case "left":
				return "down_left";
			}
		}
		return "";
	}
	
	private void pulse(HashMap<String, GpioPinDigitalOutput> motor, String direction, long pulseDuration) {
		motor.get("clockwise").low();
		motor.get("anticlockwise").low();
		motor.get(direction).pulse(pulseDuration);
	}

	//Moves the robot in the given direction using all 4 motors
	private final void start2(String direction, long localMoveDuration2) {
		Optional<Long> localMoveDuration = Optional.of(localMoveDuration2);
		switch (direction) {
		case "up":
			pulse(pins.upRight, "anticlockwise", localMoveDuration.orElse(globalMoveDuration));
			pulse(pins.downRight, "anticlockwise", localMoveDuration.orElse(globalMoveDuration));
			
			pulse(pins.upLeft, "clockwise", localMoveDuration.orElse(globalMoveDuration));
			pulse(pins.downLeft, "clockwise", localMoveDuration.orElse(globalMoveDuration));

			break;
		case "down":
			pulse(pins.upLeft, "anticlockwise", localMoveDuration.orElse(globalMoveDuration));
			pulse(pins.downLeft, "anticlockwise", localMoveDuration.orElse(globalMoveDuration));
			
			pulse(pins.upRight, "clockwise", localMoveDuration.orElse(globalMoveDuration));
			pulse(pins.downRight, "clockwise", localMoveDuration.orElse(globalMoveDuration));

			break;
		case "left":
			pulse(pins.upLeft, "anticlockwise", localMoveDuration.orElse(globalMoveDuration));
			pulse(pins.upRight, "anticlockwise", localMoveDuration.orElse(globalMoveDuration));
			
			pulse(pins.downLeft, "clockwise", localMoveDuration.orElse(globalMoveDuration));
			pulse(pins.downRight, "clockwise", localMoveDuration.orElse(globalMoveDuration));

			
			break;
		case "right":
			pulse(pins.downLeft, "anticlockwise", localMoveDuration.orElse(globalMoveDuration));
			pulse(pins.downRight, "anticlockwise", localMoveDuration.orElse(globalMoveDuration));
			
			pulse(pins.upLeft, "clockwise", localMoveDuration.orElse(globalMoveDuration));
			pulse(pins.upRight, "clockwise", localMoveDuration.orElse(globalMoveDuration));
			
			break;
		case "clockwise":
			pulse(pins.upRight, "clockwise", localMoveDuration.orElse(globalMoveDuration));
			pulse(pins.upLeft, "clockwise", localMoveDuration.orElse(globalMoveDuration));
			pulse(pins.downLeft, "clockwise", localMoveDuration.orElse(globalMoveDuration));
			pulse(pins.downRight, "clockwise", localMoveDuration.orElse(globalMoveDuration));
			break;
		case "anticlockwise":
			pulse(pins.upRight, "anticlockwise", localMoveDuration.orElse(globalMoveDuration));
			pulse(pins.upLeft, "anticlockwise", localMoveDuration.orElse(globalMoveDuration));
			pulse(pins.downLeft, "anticlockwise", localMoveDuration.orElse(globalMoveDuration));
			pulse(pins.downRight, "anticlockwise", localMoveDuration.orElse(globalMoveDuration));
			break;
			
		case "up_left":
			pulse(pins.upRight, "clockwise", localMoveDuration.orElse(globalMoveDuration));
			pulse(pins.downLeft, "clockwise", localMoveDuration.orElse(globalMoveDuration));
			break;
		case "up_right":
			pulse(pins.upLeft, "clockwise", localMoveDuration.orElse(globalMoveDuration));
			pulse(pins.downRight, "clockwise", localMoveDuration.orElse(globalMoveDuration));
			break;
		case "down_left":
			pulse(pins.upLeft, "anticlockwise", localMoveDuration.orElse(globalMoveDuration));
			pulse(pins.downRight, "anticlockwise", localMoveDuration.orElse(globalMoveDuration));
			break;
		case "down_right":
			pulse(pins.upRight, "anticlockwise", localMoveDuration.orElse(globalMoveDuration));
			pulse(pins.downLeft, "anticlockwise", localMoveDuration.orElse(globalMoveDuration));
			break;
		}
		pins.speedPin.setPwm(1024);
	}
	
	public final void stop() {
		pins.speedPin.setPwm(0);
		for(GpioPinDigitalOutput pin : pins.clockwisePins.values()) pin.low();
		for(GpioPinDigitalOutput pin : pins.anticlockwisePins.values()) pin.low();
	}
	
	public abstract boolean go(String direction);
	public abstract boolean go2(String direction, long inputMoveDuration);
	public abstract void goUntil(String direction, float distanceToWall) throws InterruptedException, ExecutionException;
	
}