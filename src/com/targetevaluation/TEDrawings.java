package com.targetevaluation;

import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class TEDrawings {
	private TEGlobalVariables globVars;
	
	public TEDrawings(TEGlobalVariables globVars) {
		this.globVars = globVars;
	}
	
	public void  drawCrosshair(Graphics g, TECrosshair crossHair, int width, int height) {
		int selLimitL, selLimitT;
		double x, y ;
		
		double scale = globVars.getScale();
		
		x = crossHair.getX() * scale;
		y = crossHair.getY() * scale;
		selLimitL = crossHair.getSelLimitL();
		selLimitT = crossHair.getSelLimitT();
		
		Graphics2D g2 = (Graphics2D) g;
		Stroke fullLine = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0);
		g2.setStroke(fullLine);
		g2.setColor(globVars.getSettings().getCrossHairColor());
		g2.drawLine(0, (int)y, width, (int)y); //horizontal line
		g2.drawLine((int)x, 0, (int)x, height); //vertical line
		
		crossHair.setSelectionPolygon(createSelectionPolygon((int)x, (int)y, selLimitL, selLimitT));
	}
	
	public void drawImpact(Graphics g, TEImpact i){
		Graphics2D g2 = (Graphics2D)g;
		double scale = globVars.getScale();
		double pixPerUnit = globVars.getPixelsPerUnit();
		double[] crosshairCoords = globVars.getCrosshair().getCoords();
		Ellipse2D selectionEllipse;
		
		double x1px, y1px, calpx; //location of impact in pixels for scale = 1.0
		x1px = i.getX()*pixPerUnit + crosshairCoords[0];
		y1px = crosshairCoords[1] - i.getY()*pixPerUnit;
		calpx = i.getCaliber() * pixPerUnit;
		
		selectionEllipse = new Ellipse2D.Double(Math.round((x1px-calpx/2)*scale), Math.round((y1px-calpx/2)*scale), 
				Math.round(calpx*scale), Math.round(calpx*scale));
		
		g2.setColor(i.getColor());
		if(!i.getStrayBullet()) {
			g2.fill(selectionEllipse);
		} else {
			g2.draw(selectionEllipse);
		}
		g2.drawString(String.valueOf(i.getId()), (int)Math.round((x1px-calpx/2)*scale), (int)Math.round((y1px-calpx/2)*scale));
		
		i.setSellectionEllipse(selectionEllipse); //reset selection ellipse to reflect changes in scale and pixPerUnit
	}
	
	public void drawTEMpoiMark( Graphics g, TEMpoiMark mark) {
		if(mark == null) {
			return;
		}
		double x1px, y1px; //location of MPOI in pixels for scale = 1.0

		double[] mpoiCoords = mark.getMpoiCoords(); //coordinated of MPOI in units
		double pixPerUnit = globVars.getPixelsPerUnit();
		double[] zeroCoords = globVars.getCrosshair().getCoords();
		double scale = globVars.getScale();
		
		int size = mark.getSize();
		int selLimit = mark.getSelLimit();
		
		x1px = (mpoiCoords[0]*pixPerUnit + zeroCoords[0]) * scale;
		y1px = (zeroCoords[1] - mpoiCoords[1]*pixPerUnit) * scale;
		
		g.setColor(mark.getColor());
		g.drawLine(	(int)(Math.round(x1px - size)), (int)(Math.round(y1px)), (int)(Math.round(x1px + size)), (int)(Math.round(y1px))); //horizontal line
		g.drawLine( (int)(Math.round(x1px)), (int)(Math.round(y1px - size)), (int)(Math.round(x1px)), (int)(Math.round(y1px + size))); //vertical line
		
		mark.setPolygon(createSelectionPolygon((int)(Math.round(x1px)), (int)(Math.round(y1px)), size, selLimit));
	}
	
	private Polygon createSelectionPolygon(int x, int y, int size, int selLimit) {
		Polygon selectionPolygon = new Polygon();
		selectionPolygon.addPoint(x - selLimit, y - size);
		selectionPolygon.addPoint(x - selLimit, y - selLimit);
		selectionPolygon.addPoint(x - size, y - selLimit);
		selectionPolygon.addPoint(x - size, y + selLimit);
		selectionPolygon.addPoint(x - selLimit, y + selLimit);
		selectionPolygon.addPoint(x - selLimit, y + size);
		selectionPolygon.addPoint(x + selLimit, y + size);
		selectionPolygon.addPoint(x + selLimit, y + selLimit);
		selectionPolygon.addPoint(x + size, y + selLimit);
		selectionPolygon.addPoint(x + size, y - selLimit);
		selectionPolygon.addPoint(x + selLimit, y - selLimit);
		selectionPolygon.addPoint(x + selLimit, y - size);

		return selectionPolygon;
	}
	
	public void drawSM(Graphics g, TESelectionMark mark) {
		double x = mark.getX();
		double y = mark.getY();
		double size = mark.getSize();
		int number = mark.getNumber();
		double scale = globVars.getScale();
		
		Graphics2D g2 = (Graphics2D) g;
		Stroke fullLine = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
		g2.setStroke(fullLine);
		g2.setColor(Color.RED);

		g2.drawLine((int)Math.round((x*scale) - size), 
					(int)Math.round((y*scale)), 
					(int)Math.round((x*scale) + size), 
					(int)Math.round((y*scale))); //horizontal line
		g2.drawLine(	(int)Math.round((x*scale)), 
					(int)Math.round((y*scale) - size), 
					(int)Math.round((x*scale)), 
					(int)Math.round((y*scale) + size)); //vertical line
		String nmbr = String.valueOf(number);
		g2.drawString(nmbr, Math.round((int)(x*scale)) + 3, (int)Math.round((y*scale) - 3));
	}
	
	public void drawBGImage(Graphics g, BufferedImage i, TECanvas canvas) {
		double scale = globVars.getScale();

		g.drawImage(i, 0, 0, (int)Math.round(i.getWidth()*scale), (int)Math.round(i.getHeight()*scale), canvas);
		canvas.setPreferredSize(new Dimension(
			(int)Math.round(i.getWidth()*scale), 
			(int)Math.round(i.getHeight()*scale)
			));
	}
	
	public void draw2R100(Graphics g,  TE2R100 circle) {
		if(circle == null) {
			return;
		}
		
		double x1px, y1px, r1px; //location of center in pixels for scale = 1.0

		double pixPerUnit = globVars.getPixelsPerUnit();
		double[] zeroCoords = globVars.getCrosshair().getCoords();
		double scale = globVars.getScale();

		int size = circle.getSize();
		int selLimit = circle.getSelLimit();
		
		x1px = (circle.getX() * pixPerUnit + zeroCoords[0]) * scale;
		y1px = (zeroCoords[1] - circle.getY() * pixPerUnit) * scale;
		r1px = circle.getD() * pixPerUnit * scale / 2;
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(circle.getColor());
		Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
		g2.setStroke(dashed);
		g2.drawLine(	(int)(Math.round(x1px - size)), (int)(Math.round(y1px)), (int)(Math.round(x1px + size)), (int)(Math.round(y1px)) ); //horizontal line
		g2.drawLine( (int)(Math.round(x1px)), (int)(Math.round(y1px - size)), (int)(Math.round(x1px)), (int)(Math.round(y1px + size)) ); //vertical line
		g2.drawOval( (int)(Math.round(x1px - r1px)), (int)(Math.round(y1px - r1px)), (int)(Math.round(r1px * 2)), (int)(Math.round(r1px * 2)) ); //circle
		
		circle.setPolygon(createSelectionPolygon((int)(Math.round(x1px)), (int)(Math.round(y1px)), size, selLimit));
	}

}
