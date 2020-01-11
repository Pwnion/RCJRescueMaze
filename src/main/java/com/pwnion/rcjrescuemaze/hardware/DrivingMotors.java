package com.pwnion.rcjrescuemaze.hardware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.google.inject.Inject;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pwnion.rcjrescuemaze.Main;

public abstract class DrivingMotors {
	private final Pins pins;
	private final Ultrasonic ultrasonic;
	private final GetSurvivors getSurvivors;
	private final DispenserMotor dispenserMotor;
	
	public static long globalMoveDuration = 2200;

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
	public DrivingMotors(Pins pins, Ultrasonic ultrasonic, GetSurvivors getSurvivors, DispenserMotor dispenserMotor) {
		this.pins = pins;
		this.ultrasonic = ultrasonic;
		this.getSurvivors = getSurvivors;
		this.dispenserMotor = dispenserMotor;
	}
	
	ArrayList<String> QueueDir = new ArrayList<String>();
	ArrayList<Long> QueueTime = new ArrayList<Long>();
	
	long timeSince = System.currentTimeMillis();
	long timeTill = 0;
	
	public void start(String mainMoveDirection, long mainMoveDuration) {
		QueueDir.clear();
		QueueTime.clear();
		
		QueueDir.add(mainMoveDirection);
		QueueTime.add(mainMoveDuration);
		
		String direction;
		long time;
		
		long delay = 300;
		
		//Keep moving untill there are no more directions to follow in the queue
		while(!(QueueDir.isEmpty())) {
			direction = QueueDir.get(0);
			time = QueueTime.get(0);
			long timeMoved = 0;
			
			long interval = 750; //interval between checking with ultrasonics for irregular activity
			
			if(time >= interval) {
				QueueTime.set(0, time - interval);
				start2(direction, interval, delay);
			} else { //time < interval
				System.out.println("time < interval + time = " + time);
				while(timeMoved < interval) {
					if(timeMoved + time > interval) {
						start2(direction, interval - timeMoved, delay);
						QueueTime.set(0, time - (interval - timeMoved));
						
						System.out.println("Moving in " + direction + " for " + (interval - timeMoved) + " + timeMoved = " + timeMoved + " + time = " + time);
						break;
					} else {
						start2(direction, time);
					}
					timeMoved += time;
					
					QueueDir.remove(0);
					QueueTime.remove(0);
					
					if(QueueDir.isEmpty()) {
						try {
							Thread.sleep(delay);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					}
					
					direction = QueueDir.get(0);
					time = QueueTime.get(0);
					System.out.println("direction = " + direction + " + time = " + time);
				}
			}
			
			if(QueueDir.isEmpty()) {
				break;
			} else {
				System.out.println("QueueDir = " + QueueDir + " + QueueTime = " + QueueTime);
			}
			
			//Do US stuff
			HashMap<String, Float> sensorOutput = new HashMap<String, Float>();
			try {
				ultrasonic.populateSensorOutput();
				sensorOutput = ultrasonic.rawSensorOutput();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			
			//Measurements in cm
			double defaultMargin = 7;
			double normalTolerance = 1;
			
			long adjustTime = 140;
			
			for(String position : sensorOutput.keySet()) {
				if(QueueDir.isEmpty()) {
					break;
				}
				boolean perp = true;
				if(position == direction || oppDirections.get(position) == direction) {
					perp = false;
					continue;
				}
				float dis = sensorOutput.get(position);

				if(dis != -1 && dis < (defaultMargin * 2) + normalTolerance) {
					//Too Close
					if(dis < (defaultMargin - normalTolerance)) {
						addDirToList(oppDirections.get(position), adjustTime);
					}
					
					//Too Far
					if(dis > (defaultMargin + normalTolerance) && perp) {
						addDirToList(position, adjustTime);
					}
					
				}
			}
		}
	}
	
	private void addDirToList(String direction, long time) {
		System.out.println("addDirToList(" + direction + ", " + time + ") + QueueDir = " + QueueDir + " + QueueTime = " + QueueTime);
		
		int counter = -1;
		long totalTime = 0;
		
		while(totalTime < time && QueueDir.size() >= counter + 2) {
			counter += 1;
			
			String prevDir = QueueDir.get(counter);
			long prevTime = QueueTime.get(counter);
			
			if(addDir(direction, prevDir).length() == 0) {
				QueueDir.remove(counter);
				QueueTime.remove(counter);
				if(QueueDir.isEmpty()) {
					break;
				}
				continue;
			}
			
			if(addDir(direction, prevDir) == "both") {
				QueueDir.add(counter, direction);
				QueueTime.add(counter, time - totalTime);
				break;
			} else {
				QueueDir.set(counter, addDir(direction, prevDir));
			}
			
			if(prevTime + totalTime == time) {
				QueueTime.set(counter, time - totalTime);
				break;
			} else if(prevTime + totalTime > time) {
				QueueDir.add(counter + 1, prevDir);
				QueueTime.add(counter + 1, prevTime - (time - totalTime));
				QueueTime.set(counter, time - totalTime);
				break;
			}
			totalTime += QueueTime.get(counter);
		}
		
		if(QueueDir.size() > counter) {
			if(QueueDir.get(counter).length() == 0) {
				QueueDir.remove(counter);
				QueueTime.remove(counter);
			}
		}
	}
	
	protected String oppDir(String dir) {
		switch(dir) {
		case "up":
			return "down";
		case "down":
			return "up";
		case "right":
			return "left";
		case "left":
			return "right";
		}
		return "";
	}
	
	private String addDir(String a, String b) {
		return "both";
		/*
		System.out.println("ADDING DIRECTIONS A = " + a + " B = " + b);
		String temp;
		
		String[] mainDir = {"up","down"};
		String[] LRDir = {"left","right"};
		
		
		for(String UD : mainDir) {
			
			if(b.contains(UD)) {
				temp = b;
				 b = a;
				 a = temp;	
			}
			
			for(String LR : LRDir) {
				if(a == UD) {
					if(b == LR) {
						return UD + "_" + LR;
					}
					if(b == oppDir(UD) + "_" + LR) {
						return LR;
					}
				}
				
				if(a == UD + "_" + LR) {
					if(b == LR || b == UD) {
						return "both";
					}
					if(b == oppDir(LR)) {
						return UD;
					}
				}
			}
		}
		return "";//*/
	}
	
	private void pulse(HashMap<String, GpioPinDigitalOutput> motor, String direction, long pulseDuration) {
		//motor.get("clockwise").low();
		//motor.get("anticlockwise").low();
		
		if(pulseDuration > 0) {
			motor.get(direction).pulse(pulseDuration);
		}
		
		
	}
	
	public final void start2(String direction, long localMoveDuration, long delay) {
		start2(direction, localMoveDuration, delay, 1024);
	}

	public final void start2(String direction, long localMoveDuration) {
		start2(direction, localMoveDuration, 0, 1024);
	} // */
	
	
	//Moves the robot in the given direction using all 4 motors
	public final void start2(String direction, long localMoveDuration, long delay, int speed) {
		System.out.println("start2(" + direction + ", " + localMoveDuration + ") + delay:" + delay);
		long moveDuration = Optional.of(localMoveDuration).orElse(globalMoveDuration);
		
		//AntiCrash System Ver1
		long currentTime = System.currentTimeMillis();
		System.out.println((currentTime - timeSince) + " < " + timeTill);
		if(currentTime - timeSince < timeTill) {
			try {
				Thread.sleep(timeTill - (currentTime - timeSince));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Call upon Survivors function to search for any survivors and detect them
		getSurvivors.read();
		System.out.println("\n\n\n******\ngetSurvivors.get()\n*********\n\n\n" + getSurvivors.get());
		if(getSurvivors.get().values().contains(true)) {
			dispenserMotor.out(180);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			dispenserMotor.in(185);
			System.out.println("\n\n\nVICTORY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\n\n");
		}
		
		//Assign Motors to Move
		switch (direction) {
		case "up":
			pulse(pins.upRight, "anticlockwise", moveDuration);
			pulse(pins.downRight, "anticlockwise", moveDuration);
			
			pulse(pins.upLeft, "clockwise", moveDuration);
			pulse(pins.downLeft, "clockwise", moveDuration);

			break;
		case "down":
			pulse(pins.upLeft, "anticlockwise", moveDuration);
			pulse(pins.downLeft, "anticlockwise", moveDuration);
			
			pulse(pins.upRight, "clockwise", moveDuration);
			pulse(pins.downRight, "clockwise", moveDuration);

			break;
		case "left":
			pulse(pins.upLeft, "anticlockwise", moveDuration);
			pulse(pins.upRight, "anticlockwise", moveDuration);
			
			pulse(pins.downLeft, "clockwise", moveDuration);
			pulse(pins.downRight, "clockwise", moveDuration);

			
			break;
		case "right":
			pulse(pins.downLeft, "anticlockwise", moveDuration);
			pulse(pins.downRight, "anticlockwise", moveDuration);
			
			pulse(pins.upLeft, "clockwise", moveDuration);
			pulse(pins.upRight, "clockwise", moveDuration);
			
			break;
		case "clockwise":
			pulse(pins.upRight, "clockwise", moveDuration);
			pulse(pins.upLeft, "clockwise", moveDuration);
			pulse(pins.downLeft, "clockwise", moveDuration);
			pulse(pins.downRight, "clockwise", moveDuration);
			break;
		case "anticlockwise":
			pulse(pins.upRight, "anticlockwise", moveDuration);
			pulse(pins.upLeft, "anticlockwise", moveDuration);
			pulse(pins.downLeft, "anticlockwise", moveDuration);
			pulse(pins.downRight, "anticlockwise", moveDuration);
			break;
			
		case "up_left":
			pulse(pins.upRight, "anticlockwise", moveDuration);
			pulse(pins.downLeft, "clockwise", moveDuration);
			break;
		case "up_right":
			pulse(pins.upLeft, "clockwise", moveDuration);
			pulse(pins.downRight, "anticlockwise", moveDuration);
			break;
		case "down_left":
			pulse(pins.upLeft, "anticlockwise", moveDuration);
			pulse(pins.downRight, "clockwise", moveDuration);
			break;
		case "down_right":
			pulse(pins.upRight, "clockwise", moveDuration);
			pulse(pins.downLeft, "anticlockwise", moveDuration);
			break;
		}

		//Activate Motors
		pins.speedPin.setPwm(speed);
		
		//Sleep for Pulse and Record AntiCrash Vars
		try {
			timeSince = System.currentTimeMillis();
			if(moveDuration - delay > 0) {
				Thread.sleep(moveDuration - delay);
			}
			timeTill = moveDuration - delay;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public final void stop() {
		pins.speedPin.setPwm(0);
		for(GpioPinDigitalOutput pin : pins.clockwisePins.values()) pin.low();
		for(GpioPinDigitalOutput pin : pins.anticlockwisePins.values()) pin.low();
	}
	
	public abstract boolean go(String direction);
	public abstract boolean go2(String direction, long inputMoveDuration);
	public abstract void goUntil(String direction, float distanceToWall, int speed) throws InterruptedException, ExecutionException;
	public abstract void goUntil(String direction, float distanceToWall) throws InterruptedException, ExecutionException;
	
}