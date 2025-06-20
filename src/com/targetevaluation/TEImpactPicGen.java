package com.targetevaluation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JDialog;

@SuppressWarnings("serial")
public class TEImpactPicGen extends JDialog{
	private int WIDTH = 640; //width of the canvas in px
	private int HEIGHT = 480; //height of the canvas in px
	private int MARGIN = 0; //margins in px
	
	private TECrosshair crossHair;
	private TEGlobalVariables locVars;
	
	private TEImpactSet set;
	private TEDrawings drawings;
	
	private Color originalColor;
	

	public TEImpactPicGen(TEImpactSet set){
		super();
		this.set = set;
	}
	
	public BufferedImage generatePicOfImpacts() {
		originalColor = set.getColor();
		set.setColor(Color.BLACK);

		//prepare image
		locVars = new TEGlobalVariables(); //Variables independent from global
		locVars.setPixelsPerUnit(96/25.4);
		setScale();
		crossHair = new TECrosshair();
		locVars.setCrosshair(crossHair);

		//to ensure the crosshair for picture generation is allways in the middle of canvas
		//divide its coordinates with scale. The scale is now applied only on impact set
		crossHair.setCoords((WIDTH/2)/locVars.getScale(), (HEIGHT/2)/locVars.getScale());
		drawings = new TEDrawings(locVars);
		
		this.setUndecorated(true);
		this.setSize(WIDTH, HEIGHT);
		this.setVisible(true);

	    BufferedImage image=new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = image.createGraphics();
	    paint(g);

		this.setVisible(false);
		set.setColor(originalColor);
		
		return image;
	}
	
	private void setScale() {
		double pixPerUnit = locVars.getPixelsPerUnit();
		double extrems[] = set.getExtrems();
		double caliber = set.getCaliber();
		TE2R100 circle2R100 = set.get2R100();

		double centerX = circle2R100.getX();
		double centerY = circle2R100.getY();
		double radius = circle2R100.getD()/2;
		
		//calculate min/max dimension in pixels
		double maxX2R100 = WIDTH/2 + (centerX + radius) * pixPerUnit;
		double minX2R100 = WIDTH/2 + (centerX - radius) * pixPerUnit;
		double maxY2R100 = HEIGHT/2 - (centerY - radius) * pixPerUnit;
		double minY2R100 = HEIGHT/2 - (centerY + radius) * pixPerUnit;
		
		double maxXI = WIDTH/2 + (extrems[1] + caliber/2) * pixPerUnit;
		double minXI = WIDTH/2 + (extrems[0] - caliber/2) * pixPerUnit;
		
		double maxYI = HEIGHT/2 - (extrems[2] - caliber/2) * pixPerUnit;
		double minYI = HEIGHT/2 - (extrems[3] + caliber/2) * pixPerUnit;
		
		double minX = minX2R100 < minXI ? minX2R100 : minXI;
		double maxX = maxX2R100 > maxXI ? maxX2R100 : maxXI;
		double minY = minY2R100 < minYI ? minY2R100 : minYI;
		double maxY = maxY2R100 > maxYI ? maxY2R100 : maxYI;
		
		locVars.setScale(calculateScale(minX, maxX, minY, maxY));
	}
	
	/**
	 * calculates scale to fit all impacts to canvas of dimension WIDTH/HEIGHT/MARGIN and crosshair in the center
	 */
	private double calculateScale(double minX, double maxX, double minY, double maxY) {
		ArrayList<Double> scale = new ArrayList<Double>(); 
		
		if(minX < MARGIN) {
			scale.add((WIDTH/2 - MARGIN)/(WIDTH/2 - minX));
		}

		if(maxX > WIDTH - MARGIN) {
			scale.add((WIDTH/2 - MARGIN)/(maxX - WIDTH/2));
		}
		
		if(minY < MARGIN) {
			scale.add((HEIGHT/2 - MARGIN)/(HEIGHT/2 - minY));
		}
		
		if(maxY > HEIGHT - MARGIN) {
			scale.add((HEIGHT/2 - MARGIN)/(maxY - HEIGHT/2));
		}
		
		if(scale.size() == 0) {
			return 1.0;
		} else {
			return Collections.min(scale);
		}

	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		//this.setBackground(Color.WHITE); //nefunguje 
		
		drawings.drawCrosshair(g, crossHair, WIDTH, HEIGHT);

		for(TEImpact i : set.getImpacts()) {
			drawings.drawImpact(g, i);
		}
		
		drawings.drawTEMpoiMark(g, set.getMpoiMark());
		drawings.draw2R100(g, set.get2R100());
	}

}
