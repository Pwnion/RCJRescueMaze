package com.pwnion.rcjrescuemaze.datatypes;

import java.util.ArrayList;

public class Coords {
	private int x;
	private int y;
	
	public boolean compare(Coords coord) {
		return x == coord.getX() && y == coord.getY();
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

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	public ArrayList<Coords> surrounding() {
		ArrayList<Coords> surroundingCoords = new ArrayList<Coords>() {
			private static final long serialVersionUID = 1L;
			{
				add(new Coords(x, y - 1));
				add(new Coords(x + 1, y));
				add(new Coords(x, y + 1));
				add(new Coords(x - 1, y));
			}
		};
		return surroundingCoords;
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

	public Coords plus(Coords coords) {
		return new Coords(coords.getX() + x, coords.getY() + y);
	}
}
