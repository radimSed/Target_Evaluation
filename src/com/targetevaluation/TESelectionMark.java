package com.targetevaluation;

public class TESelectionMark {
	private double x, y; //coordinates in pixels
	private int size = 20; //length of the arm in pixels
	private int number; 
	private TEGlobalVariables globVars;
	
	public TESelectionMark(double x, double y, int number, TEGlobalVariables globVars) {
		this.x = x;
		this.y = y;
		this.number = number;
		this.globVars = globVars;
	}

	public int getSize() {
		return size;
	}
	
	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}
	
	public void setNumber(int id) {
		number = id;
	}
	
	public int getNumber() {
		return number;
	}
	
	public double getUnitX() {
		double unitX = (x - globVars.getCrosshair().getX()) / globVars.getPixelsPerUnit();
		return unitX;
	}
	
	public double getUnitY() {
		double unitY = (globVars.getCrosshair().getY() - y) / globVars.getPixelsPerUnit();
		return unitY;
	}

}
