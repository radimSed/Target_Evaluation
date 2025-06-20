package com.targetevaluation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class TECanvas extends JPanel {
	
	private TEGui gui;
	private TEGlobalVariables globVars;
	private TEDrawings drawings;

	public TECanvas(TEGui gui, TEGlobalVariables globVars) {
		super();
		this.addMouseMotionListener(mouseMotionAdapter);
		this.addMouseListener(mouseAdapter);
		
		this.gui = gui;
		this.globVars = globVars;
		this.drawings = new TEDrawings(globVars);
	}
	
	PropertyChangeListener listener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			repaint();
		}
	};


	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		this.setBackground(globVars.getSettings().getCanvasBgColor());

		double scale = globVars.getScale();
		if(globVars.getItemToShowValue("imageCheckb")) {
			paintImage(g, scale);
		}

		if(globVars.getItemToShowValue("aimCrossCheckb")) {
			paintAimCross(g);
		}
		
		if(globVars.getItemToShowValue("impactCheckb")) {
			paintImpacts(g);
		}
		
		if(globVars.getItemToShowValue("middlePointCheckb")) {
			paintMPOI(g); //paint middle point of impacts
		}
		
		if(globVars.getItemToShowValue("s100Checkb")) {
			paint2R100(g); //paint middle point of impacts
		}

		paintSelectionMarks(g, scale);

	}

	private void paint2R100(Graphics g) {
		List<TEImpactSet> sets = globVars.getImpactSets();

		//copy sets to separate variable to avoid ConcurrentModificationException
		List<TEImpactSet> sets2 = new ArrayList<>();
		sets2.addAll(sets);
		
		for(TEImpactSet set : sets2) {
			if(set.getVisibility()) {
				drawings.draw2R100(g, set.get2R100());
			}
		}
		
	}
	
	private void paintMPOI(Graphics g) {
		List<TEImpactSet> sets = globVars.getImpactSets();

		//copy sets to separate variable to avoid ConcurrentModificationException
		List<TEImpactSet> sets2 = new ArrayList<>();
		sets2.addAll(sets);
		for(TEImpactSet set : sets) {
			if(set.getVisibility()) {
				drawings.drawTEMpoiMark(g, set.getMpoiMark());
			}
		}
	}
	
	private void paintImpacts(Graphics g) {
		List<TEImpactSet> sets = globVars.getImpactSets();

		//copy sets to separate variable to avoid ConcurrentModificationException
		List<TEImpactSet> sets2 = new ArrayList<>();
		sets2.addAll(sets);
		
		for(TEImpactSet set : sets2) {
			if(set.getVisibility()) {
				for(TEImpact i : set.getImpacts()) {
					drawings.drawImpact(g, i);
				}
			}
		}
	}

	private void paintSelectionMarks(Graphics g, double scale) {
		List<TESelectionMark> selmarks = globVars.getSelmarkList();
		for(TESelectionMark sm : selmarks) {
			drawings.drawSM(g, sm);
		}
	}

	private void paintImage(Graphics g, double scale) {
		BufferedImage i = globVars.getBgImage();
		if ( i != null ) {
			drawings.drawBGImage(g, i, this); //needs canvas to be able to set dimensions
		}
	}
	
	private void paintAimCross(Graphics g) {
		drawings.drawCrosshair(g, globVars.getCrosshair(), this.getWidth(), this.getHeight());
	}
	
	MouseMotionAdapter mouseMotionAdapter = new MouseMotionAdapter() {
		@Override
		public void mouseMoved(MouseEvent e) {
			super.mouseMoved(e);
			double scale = globVars.getScale();
			double mouseX = e.getX()/scale;
			double mouseY = e.getY()/scale;
			double[] coords = globVars.getCrosshair().getCoords();
			double x = mouseX - (int)coords[0];
			double y = (int)coords[1] - mouseY;
			gui.setXYTextInfo(x, y);
		}
	};

	MouseAdapter mouseAdapter = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);
			
			//left click processing
			if(e.getButton()==1) {
				if(globVars.getSettingCrosshair() && e.getButton() == 1) {
					setCrosshair(e.getX(),	e.getY());
				} else if(globVars.getSettingUnitsStatus()) {
					double scale = globVars.getScale();
					settingUnits((double)e.getX()/scale, (double)e.getY()/scale, scale);//x,y "normalized to scale = 1
				} else if(globVars.getCreatingFile()) {
					double scale = globVars.getScale();
					selectingImpacts((double)e.getX()/scale, (double)e.getY()/scale);//x,y "normalized to scale = 1);
				}
			}
			
			//right click processing
			if(e.getButton()==3) {
				
				TEImpact selImpact = isImpactSelected(e);
				TEMpoiMark selMpoi = isMpoiSelected(e);
				TE2R100 circle2R100 = is2R100selected(e);
				if(selMpoi != null) {
					TEMpoiMenu menu = new TEMpoiMenu(selMpoi, gui);
			        menu.show(e.getComponent(), e.getX(), e.getY());
				} else if( circle2R100 != null) {
					TE2R100Menu menu = new TE2R100Menu(circle2R100, gui);
			        menu.show(e.getComponent(), e.getX(), e.getY());
				} else if( selImpact != null) {
					TEImpactMenu menu = new TEImpactMenu(selImpact, gui);
			        menu.show(e.getComponent(), e.getX(), e.getY());
				} else if(isCrossHairSelected(e)) {
					TECrossHairMenu menu = new TECrossHairMenu(globVars);
			        menu.show(e.getComponent(), e.getX(), e.getY());
				} else if(isBgImageSelected(e)){
					TEBgImageMenu menu = new TEBgImageMenu(gui, globVars);
			        menu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			if(globVars.getSettingCrosshair() 
					|| globVars.getSettingUnitsStatus()
					|| globVars.getCreatingFile()) {
				
				TESelectionCursor myCursor = new TESelectionCursor(globVars);
				int size = myCursor.getSizePix(); //size in pix reflects pixPerUnit as well as scale 
				gui.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(myCursor.getCursor(), new Point(size, size), "SelCursor"));
				
			}

		}
		
		@Override
	    public void mouseExited(MouseEvent e) {
			if(globVars.getSettingCrosshair() 
					|| globVars.getSettingUnitsStatus()
					|| globVars.getCreatingFile()) {
				gui.setCursor(Cursor.DEFAULT_CURSOR);
			}
		}
		
	};

	private void setCrosshair(int x, int y) {
		//setting new position of crosshair
		double scale = globVars.getScale();
		globVars.getCrosshair().setCoords(x/scale , y/scale);
		globVars.setSettingCrosshair(false);
		gui.setCursor(Cursor.DEFAULT_CURSOR);
		repaint();

	}
	
	private TE2R100 is2R100selected(MouseEvent e) {
		if(!globVars.getItemToShowValue("s100Checkb")) {
			return null;
		}

		for(TEImpactSet is : globVars.getImpactSets()) {
			if(is.getVisibility()) { //select only from visible sets
				TE2R100 circle = is.get2R100();
				if(circle != null) { 
					if(circle.isSelected(e.getX(), e.getY())) {
						return circle;
					}
				}
			}
		}
		return null;
	}

	
	private TEMpoiMark isMpoiSelected(MouseEvent e) {
		if(!globVars.getItemToShowValue("middlePointCheckb")) {
			return null;
		}

		for(TEImpactSet is : globVars.getImpactSets()) {
			if(is.getVisibility()) { //select only from visible sets
				TEMpoiMark mpoiMark = is.getMpoiMark();
				if(mpoiMark != null) { //mpoiMark can be null for sets where all impacts are marked as stray
					if(mpoiMark.isSelected(e.getX(), e.getY())) {
						return mpoiMark;
					}
				}
			}
		}
		return null;
	}
	
	private TEImpact isImpactSelected(MouseEvent e) {
		if(!globVars.getItemToShowValue("impactCheckb")) {
			return null;
		} else {
			for(TEImpactSet is : globVars.getImpactSets()) {
				if(is.getVisibility()) { //select only from visible sets
					for(TEImpact i : is.getImpacts()) {
						if(i.isSelected(e.getX(), e.getY())){
							return i;
						}
					}
				}
			}
			return null;
		}
	}
	
	private boolean isCrossHairSelected(MouseEvent e) {
		boolean isCrossHairSelected;
		if(!globVars.getItemToShowValue("aimCrossCheckb")) {
			isCrossHairSelected = false;
		} else {
			isCrossHairSelected = globVars.getCrosshair().isSelected(e.getX(), e.getY());
		}
		return isCrossHairSelected;
	}
	

	private boolean isBgImageSelected(MouseEvent e) {
		boolean isSelected;
		if(!globVars.getItemToShowValue("imageCheckb")) {
			isSelected = false;
		} else {
			isSelected = true;
		}
		return isSelected;
	}

	private void settingUnits(double x, double y, double scale) {
		//setting units per pixels
		List<TESelectionMark> selmarks = globVars.getSelmarkList();
		int number = selmarks.size() + 1;
		globVars.addSelMark(new TESelectionMark(x, y, number, globVars));
		repaint();
		
		if(number == 2) {
			try{
				double pixelsPerUnit = calculateUnits(selmarks);
				gui.setPixelsPerUnit(pixelsPerUnit);
			} catch (TEException e) {
				JOptionPane.showMessageDialog(gui, e.getMessage(), "Invalid input", JOptionPane.ERROR_MESSAGE);
			}
			globVars.clearSelMarks();
			globVars.setSettingUnitsStatus(false);
			gui.setCursor(Cursor.DEFAULT_CURSOR);
			repaint();
		}
	}
	
	private void selectingImpacts(double x, double y) {
		List<TESelectionMark> selmarks = globVars.getSelmarkList();
		int number = selmarks.size() + 1;
		globVars.addSelMark(new TESelectionMark(x, y, number, globVars));
		repaint();
	}
	
	private double calculateUnits(List<TESelectionMark> sm) throws TEException{
		double dimension = -1.0;
		String text = JOptionPane.showInputDialog(gui, "Enter the dimension in mm");
		
		if(text == null || text.isEmpty()) {
			throw new TEException("Dimension not entered.");
		}
		try {
			dimension = Double.parseDouble(text);
		} catch (NumberFormatException e) {
			throw new TEException("Invalid format of number.");
		}
		
		if(dimension <= 0) {
			throw new TEException("Dimension must be larger than 0");
		}
		
		TESelectionMark sm1, sm2;
		sm1 = sm.get(0);
		sm2 = sm.get(1);
		double x1 = sm1.getX();
		double y1 = sm1.getY();
		double x2 = sm2.getX();
		double y2 = sm2.getY();

		double deltaX = x2 - x1;
		double deltaY = y2 - y1;
			
		double deltaPx = (Math.sqrt(deltaX * deltaX + deltaY * deltaY));
		return deltaPx/dimension;
	}

}
