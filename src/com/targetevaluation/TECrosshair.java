package com.targetevaluation;

import java.awt.Polygon;

@SuppressWarnings("serial")
public class TECrosshair extends Polygon{
	private int selLimitL = 100, selLimitT = 5;
	private Polygon selectionPolygon;
	
	private double x; // x-coord in pixels for scale 1
	private double y; // y-coord in pixels fpr scale 1
	
	public TECrosshair() {}

	public void setSelectionPolygon(Polygon polygon) {
		selectionPolygon = polygon;
	}
	
	public boolean isSelected(int x, int y) {
		return this.selectionPolygon.contains(x, y);
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double[] getCoords() {
		double[] coords = {x, y};
		return coords;
	}
	
	//sets position of crosshair in PIXELS
	public void setCoords(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public int getSelLimitL() {
		return selLimitL;
	}
	
	public int getSelLimitT() {
		return selLimitT;
	}
}
