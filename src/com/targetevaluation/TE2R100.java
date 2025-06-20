package com.targetevaluation;

import java.awt.Color;
import java.awt.Polygon;
import java.text.NumberFormat;

public class TE2R100 {
	private double x; //x coordinate of the center
	private double y; //y coordinate of the center
	private double d; //diameter of the circle
	
	private int size = 30;
	private int selLimit = 5; //distance in px where the mark is sensitive to mouse
	private Polygon selPolygon;
	
	private TEImpactSet parent;

	
	public TE2R100(TEImpactSet parent, double x, double y, double d) {
		this.x = x;
		this.y = y;
		this.d = d;
		this.parent = parent;
	}
	
	public String getInfo() {
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);
		
		Double moa = parent.getMOA();
		String moaStr;
		
		if(moa == null) {
			moaStr = "N/A";
		} else {
			moaStr = String.valueOf(nf.format(moa));
		}
		
		StringBuilder info = new StringBuilder("Set: " + parent.getId() + "\n");
		info.append("Coordinates of center of 2R100 circle:\n");
		info.append("X = " + nf.format(x) + " mm\n");
		info.append("Y = " + nf.format(y) + " mm\n");
		info.append("Diameter = " + nf.format(d) + " mm (" + (moaStr) + " MOA)"); 
		return info.toString();
	}
	
	public int getSetId() {
		return parent.getId();
	}

	public boolean isSelected(int x, int y) {
		return this.selPolygon.contains(x, y);
	}

	public Double getX() {
		return x;
	}

	public Double getY() {
		return y;
	}

	public Double getD() {
		return d;
	}

	public void setD(double d) {
		this.d = d;
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
	
	public void setPolygon(Polygon polygon) {
		selPolygon = polygon;
	}
	
}
