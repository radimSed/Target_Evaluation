package com.targetevaluation;

import java.awt.Color;
import java.io.Serializable;

@SuppressWarnings("serial")
public class TESettings implements Serializable{
	//canvas settings
	private Color canvasBgColor = Color.BLACK;
	private Color crossHairColor = Color.RED;
	
	//statistics settings
	private boolean outerEdge2R100 = false;
	
	//Selection cursor settings
	private double crossSize = 10;
	private double outerDiameter = 8;
	private double innerDiameter = 5;
	
	public Color getCanvasBgColor() {
		return canvasBgColor;
	}
	
	public double getCrossSize() {
		return crossSize;
	}

	public void setCrossSize(double crossSize) {
		this.crossSize = crossSize;
	}

	public double getOuterDiameter() {
		return outerDiameter;
	}

	public void setOuterDiameter(double outerDiameter) {
		this.outerDiameter = outerDiameter;
	}

	public double getInnerDiameter() {
		return innerDiameter;
	}

	public void setInnerDiameter(double innerDiameter) {
		this.innerDiameter = innerDiameter;
	}

	public void setCanvasBgColor(Color canvasBgColor) {
		this.canvasBgColor = canvasBgColor;
	}
	
	public Color getCrossHairColor() {
		return crossHairColor;
	}
	
	public void setCrossHairColor(Color crossHairColor) {
		this.crossHairColor = crossHairColor;
	}
	
	public boolean getOuterEdge2R100() {
		return outerEdge2R100;
	}
	
	public void setOuterEdge2R100(boolean value) {
		outerEdge2R100 = value;
	}
}
