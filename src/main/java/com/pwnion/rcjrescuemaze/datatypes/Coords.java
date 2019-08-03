package com.pwnion.rcjrescuemaze.datatypes;

public class Coords {
	private int x;
	private int y;
	
	public boolean compare(Coords coord) {
		return x == coord.getX() && y == coord.getY();
	}
	
	public String getSignature() {
		return Integer.toString(x) + Integer.toString(y);
	}
	
	public Coords(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Coords(Coords coords) {
		this.x = coords.getX();
		this.y = coords.getY();
	}
	
	public int[] get() {
		return new int[] {x, y};
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void set(Coords coords) {
		this.x = coords.getX();
		this.y = coords.getY();
	}
	
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void add(int x, int y) {
		this.x += x;
		this.y += y; 
	}
	
	public void add(Coords coords) {
		this.x += coords.getX();
		this.y += coords.getY();
	}
	
	public void addX(int x) {
		this.x += x;
	}
	
	public void addY(int y) {
		this.y += y;
	}
}
