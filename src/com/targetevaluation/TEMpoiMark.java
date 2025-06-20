package com.targetevaluation;

import java.awt.Color;
import java.awt.Polygon;
import java.text.NumberFormat;

@SuppressWarnings("serial")
public class TEMpoiMark extends Polygon{
	
	private int size = 30;
	private int selLimit = 5; //distance in px where the mark is sensitive to mouse
	private Polygon selectionPolygon = null;
	private TEImpactSet parent;
	
	private double x; // x-coord in units
	private double y; // y-coord in units

	
	public TEMpoiMark(TEImpactSet parent, double x, double y) {
		this.parent = parent;
		this.x = x;
		this.y = y;
	}
	
	public String getInfo() {
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);

		StringBuilder info = new StringBuilder("Set: " + parent.getId() + "\n");
		info.append("Coordinates of Middle Point of Impact:\n");
		info.append("X = " + nf.format(x) + " mm\n");
		info.append("Y = " + nf.format(y) + " mm");
		return info.toString();
	}

	public boolean isSelected(int x, int y) {
		return this.selectionPolygon.contains(x, y);
	}
	
	public int getSetId() {
		return parent.getId();
	}

	public double[] getMpoiCoords() {
		double[] coords = {x, y};
		return coords;
	}
	
	public void setMpoiCoords(double[] coords) {
		x = coords[0];
		y = coords[1];
	}
	
	public void setPolygon(Polygon polygon) {
		selectionPolygon = polygon;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getSelLimit() {
		return selLimit;
	}
	
	public Color getColor() {
		return parent.getColor();
	}
}
