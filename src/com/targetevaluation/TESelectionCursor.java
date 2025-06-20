package com.targetevaluation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class TESelectionCursor {
	private double size; //=5; //length of the arm in units for scale 1
	private double diameter1; // = 8; //outer diameter in units for scale 1
	private double diameter2; // = 5; //inner diameter in units for scale 1
	
	private TEGlobalVariables globVars;
	
	public TESelectionCursor(TEGlobalVariables globVars) {
		this.globVars = globVars;
		TESettings settings = globVars.getSettings();
		this.size = settings.getCrossSize() / 2;
		this.diameter1 = settings.getOuterDiameter();
		this.diameter2 = settings.getInnerDiameter();
	}
	
	public BufferedImage getCursor() {
		double sizePix, dia1Pix, dia2Pix;
		double scale, pixPerUnit;
		
		pixPerUnit = globVars.getPixelsPerUnit();
		scale = globVars.getScale();
		
		sizePix = size * pixPerUnit * scale; //size of the arm
		dia1Pix = diameter1 * pixPerUnit * scale;
		dia2Pix = diameter2 * pixPerUnit * scale;
		
		BufferedImage cursor = new BufferedImage((int)(Math.round(2 * sizePix)), (int)(Math.round(2 * sizePix)), BufferedImage.TYPE_INT_ARGB );
		Graphics2D g = (Graphics2D)cursor.createGraphics();
		g.setColor(Color.RED);
		g.setBackground(new Color(1f, 0f, 0f, 1.0f));
		
		g.drawLine(0, (int)(Math.round(sizePix)), (int)(Math.round(2 * sizePix)), (int)(Math.round(sizePix))); //horizontal line
		g.drawLine((int)(Math.round(sizePix)), 0, (int)(Math.round(sizePix)), (int)(Math.round(2 * sizePix))); //vertical line
		g.drawOval((int)(Math.round(sizePix - dia1Pix /2)), (int)(Math.round(sizePix - dia1Pix /2)), (int)(Math.round(dia1Pix)), (int)(Math.round(dia1Pix))); //outer circle
		g.drawOval((int)(Math.round(sizePix - dia2Pix /2)), (int)(Math.round(sizePix - dia2Pix /2)), (int)(Math.round(dia2Pix)), (int)(Math.round(dia2Pix))); //inner circle
		return cursor;
	}
	
	public int getSizePix() {
		return (int)(Math.round(size * globVars.getPixelsPerUnit() * globVars.getScale()));
	}

}
