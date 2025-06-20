package com.targetevaluation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TEDial extends JPanel{
	private final int MARGIN = 10; //margin in pixels
	private final int CROSSSIZE = 20; //size of the middle cross
	private final int MARKSIZE = 10;  //size of the tick
	private final int MARKCOUNT = 12; //number of the ticks peripherally
	private final int ARROWSIZE = 15; //size of the arrow
	private final Color DIALCOLOR = Color.BLACK;
	private final Color ARROWCOLOR = Color.GRAY;

	private Integer angle; //angle in degrees
	private int size; //size of the container 

	public TEDial(int size, Integer angle) {
		super();
		this.size = size;
		this.angle = angle;
		this.setSize(size, size);
		this.repaint();
	}
	
	public BufferedImage getPic() {
		this.setBackground(Color.WHITE);
	    BufferedImage image=new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = image.createGraphics();
	    paint(g);
	    
	    return image;
	}
	
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        double radius = size/2 - MARGIN;
        double center = size/2;
        
        
        drawDial(g, center, radius);
        if (angle != null) {
        	drawArrow(g, center, radius, angle);
        }
    }

	private void drawArrow(Graphics g, double center, double radius, double angle) {
        double startX, startY, endX, endY;
		double angleRad = (2 * Math.PI) / 360 * angle;

		g.setColor(ARROWCOLOR);
        g.drawLine(size / 2, size / 2 - CROSSSIZE / 2, size / 2, size / 2 + CROSSSIZE / 2); //cross in the middle vertical
    	startX = center;
    	endX = center + (radius * Math.sin(angleRad));
    	startY = center;
    	endY = center - (radius * Math.cos(angleRad));
        g.drawLine((int)startX, (int)startY, (int)endX, (int)endY);
        
        //draw arrow at the end
        double arrowRadius = Math.sqrt( Math.pow(radius, 2) + Math.pow(ARROWSIZE, 2) - 2 * (radius * ARROWSIZE * Math.cos(((2 * Math.PI) / 360) * 30)));
        double arrowAngleRad = Math.acos(( Math.pow(arrowRadius, 2) + Math.pow(radius, 2) - Math.pow(ARROWSIZE, 2)) / (2 * arrowRadius * radius));//	0.9952910237103455	
		
        Polygon arrow = new Polygon();
        arrow.addPoint((int)endX, (int)endY);
        arrow.addPoint((int)(center + arrowRadius * Math.sin(angleRad - arrowAngleRad)), (int)(center - arrowRadius * Math.cos(angleRad - arrowAngleRad)));
        arrow.addPoint((int)(center + arrowRadius * Math.sin(angleRad + arrowAngleRad)), (int)(center - arrowRadius * Math.cos(angleRad + arrowAngleRad)));
        g.fillPolygon(arrow);
	}

	private void drawDial(Graphics g, double center, double radius) {
		g.setColor(DIALCOLOR);
        g.drawOval(MARGIN, MARGIN, size - 2 * MARGIN - 1, size - 2 * MARGIN - 1); //circle
        g.drawLine(size / 2, size / 2 - CROSSSIZE / 2, size / 2, size / 2 + CROSSSIZE / 2); //cross in the middle vertical
        g.drawLine(size / 2 - CROSSSIZE / 2, size / 2, size / 2 + CROSSSIZE / 2, size / 2); //cross in the middle horizontal

        //marks
        double startX, startY, endX, endY;
        double alpha;

        for(int i = 1; i <= MARKCOUNT; i++) {
        	alpha = ((Math.PI * 2) / MARKCOUNT) * i ;
        	startX = center + ((radius - MARKSIZE/2) * Math.sin(alpha));
        	endX = center + ((radius + MARKSIZE/2) * Math.sin(alpha));
        	startY = center - ((radius - MARKSIZE / 2) * Math.cos(alpha));
        	endY = center - ((radius + MARKSIZE / 2) * Math.cos(alpha));
            g.drawLine((int)startX, (int)startY, (int)endX, (int)endY);
        }
	}
	
	public void repaintDial(Integer angle) {
		this.angle = angle;
		repaint();
	}

}
 