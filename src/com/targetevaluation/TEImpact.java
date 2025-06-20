package com.targetevaluation;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.text.NumberFormat;

public class TEImpact{ // extends Ellipse2D{
	private double x, y; //coordinates of the impact in real units
	private int impactNumber;
	private boolean isStrayBullet;
	private Ellipse2D selectionEllipse;
	private TEImpactSet parent;
	private TEGlobalVariables globVars;
	
	public TEImpact(TEImpactSet parent, int number, double x, double y, TEGlobalVariables globVars) {
		this.globVars = globVars;
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.impactNumber = number;
		this.isStrayBullet = false;
	}
	
	public void setSellectionEllipse(Ellipse2D ellipse) {
		selectionEllipse = ellipse;
	}
	
	public double getCaliber() {
		return parent.getCaliber();
	}
	
	public Color getColor() {
		return parent.getColor();
	}
	
	public int getSetId() {
		return parent.getId();
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public double getDistanceFromZero() {
		return Math.sqrt((Math.pow(x, 2) + Math.pow(y, 2)));
	}
	
	public int getId() {
		return this.impactNumber;
	}
	
	/**
	 * 
	 * @return double array [x,y]
	 */
	public double[] getCoords() {
		double[] coords = {this.x, this.y};
		return coords;
	}
	
	public boolean getStrayBullet() {
		return isStrayBullet;
	}
	
	public void setStrayBullet(boolean value) {
		isStrayBullet = value;
		
		//recalculate mpoi and 2R100
		TEStatisctics stat = new TEStatisctics(globVars.getSettings());
		parent.setMpoiMark(stat.calculateMpoi(parent.getImpacts(), parent));
		parent.set2R100(stat.calculate2R100(parent.getImpacts(), parent));
	}

	public boolean isSelected(int x, int y) {
		 return selectionEllipse.contains(x, y); 
	}
	
	public String getInfo() {
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);

		StringBuilder info = new StringBuilder("Set: " + parent.getId() + "\n");
		info.append("Impact: " + impactNumber + "\n");
		info.append("Stray bullet: " + isStrayBullet + "\n");
		info.append("X = " + nf.format(x) + " mm\n");
		info.append("Y = " + nf.format(y) + " mm");
		return info.toString();
	}
}
