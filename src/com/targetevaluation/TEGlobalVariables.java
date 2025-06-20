package com.targetevaluation;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

public class TEGlobalVariables{ 
	/**
	 * buffered image for display on canvas 
	 */
	private BufferedImage targetImage;

	/**
	 *  calculated value from measured pixels and entered value of distance in "units".
	 */
	private double pixelsPerUnit;
	
	/**
	 * Crosshair 
	 */
	private TECrosshair crosshair = new TECrosshair(); 
	
	/**
	 * variables to show/not show given item 
	 */
	private Map<String, Boolean> itemsToShow = new HashMap<>();
	
	private boolean isSettingCrosshair = false; //true if invoked setting new position of crosshair
	private boolean isSettingUnits = false;////true if invoked setting units of bgImage
	private boolean isCreatingFile = false;
	
	/**
	 * scale  
	 */
	private double scale = 1.0;
	
	/**
	 * impactSets holds the impact sets read/defined during session
	 */
	private int impacSetsMaxId = 1;
	private ArrayList<TEImpactSet> impactSets = new ArrayList<TEImpactSet>();
	
	/**
	 * field of color constants - basically constant as well, therefore public
	 */
	
	public static Color[] colors = {Color.GRAY, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA, Color.YELLOW};
	
	/**
	 * field of selection marks
	 */
	private ArrayList<TESelectionMark> selmarkList = new ArrayList<TESelectionMark>();

	/**
	 * fills listbox during impact selection 
	 */
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(TEGlobalVariables.class);

	/**
	 * global reference to GUI for easier calls for repaint canvas, change picToUnit etc
	 */
	private TEGui teGui;
	
	/**
	 * global reference to statisctic coefficients 
	 */
	private TEStatCoeffs statCoeffs;

	/**
	 * default caliber to be used for impact diameter calculation
	 */
	private double defaultCaliber = 7.62;
	
	/**
	 * image used as icon 
	 */
	private BufferedImage icon;
	private TESettings settings;
	//***********************************************************************************************
	
	public TEGlobalVariables() {
		//read statistic coeffs
		this.statCoeffs = new TEStatCoeffs("/resources/STATISTICS");
		
		//read icon picture
		try {
			InputStream is = getClass().getResourceAsStream("/resources/logo.png");
			icon = ImageIO. read(is);
		} catch (IOException | NullPointerException e) {
			System.err.println(e.getMessage());
		}

        //deserialization of settings 
		try ( ObjectInputStream in = new ObjectInputStream(new FileInputStream("settings.ser")) ){
            settings = (TESettings) in.readObject();
        } catch (IOException | ClassNotFoundException i) {
        	this.settings = new TESettings();
        }
	}
	
	public BufferedImage getIcon() {
		return icon;
	}
	
	public double getDefaultCaliber() {
		return defaultCaliber;
	}
	
	public void setDefaultCaliber(double dCaliber) {
		defaultCaliber = dCaliber;
	}
	
	public TESettings getSettings() {
		return settings;
	}
	
	public TEStatCoeffs getStatCoeffs() {
		return statCoeffs;
	}

	public void setTEGui(TEGui tegui) {
		this.teGui = tegui;
	}
	
	public TEGui getTEGui() {
		return teGui;
	}
	
	public void setCreatingFile(boolean value) {
		isCreatingFile = value;
	}
	
	public boolean getCreatingFile() {
		return isCreatingFile;
	}
	
	/**
	 * setting Property change listener 
	 */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
	       propertyChangeSupport.addPropertyChangeListener(listener);
	   }

	public void addSelMark(TESelectionMark sm) {
		List<TESelectionMark> oldvalue = new ArrayList<>(selmarkList);
		
		selmarkList.add(sm);

		// fills listbox and repaints canvas during impact selection and dimension settings 
		propertyChangeSupport.firePropertyChange("selmarkList", oldvalue, selmarkList);
	}
	
	public void clearSelMarks() {
		List<TESelectionMark> oldvalue = new ArrayList<>(selmarkList);
		
		selmarkList.clear();

		// fills listbox and repaints canvas during impact selection and dimension settings 
		propertyChangeSupport.firePropertyChange("selmarkList", oldvalue, selmarkList);
	}
	
	public int getImpacSetsMaxId() {
		return impacSetsMaxId;
	}
	
	public TEImpactSet getImpactSetById(int id) {
		for(TEImpactSet is : impactSets) {
			if(is.getId() == id) {
				return is;
			}
		}
		return null;
	}

	public List<TESelectionMark> getSelmarkList(){
		return selmarkList;
	}
	
	public void addImpactSet(TEImpactSet set) {
		impacSetsMaxId++;
		impactSets.add(set);
	}

	public List<TEImpactSet> getImpactSets(){
		return impactSets;
	}
	
	public TECrosshair getCrosshair() {
		return crosshair;
	}
	
	public void setCrosshair(TECrosshair ch) {
		crosshair = ch;
	}

	public void setScale(double value) {
		scale = value;
	}
	
	public double getScale() {
		return scale;
	}

	public void setSettingUnitsStatus(boolean value) {
		isSettingUnits = value;
	}
	public boolean getSettingUnitsStatus() {
		return isSettingUnits;
	}
	
	public void setSettingCrosshair(boolean value) {
		isSettingCrosshair = value;
	}
	
	public boolean getSettingCrosshair() {
		return isSettingCrosshair;
	}
	
	public Map<String, Boolean> getItemsToShow() {
		return itemsToShow;
	}

	public void setItemToShow(String key, boolean value) {
		itemsToShow.put(key, value);
	}
	
	public boolean getItemToShowValue(String key) {
		return itemsToShow.get(key);
	}

	public void setPixelsPerUnit(Double pixels) {
		pixelsPerUnit = pixels;
	}
	
	public double getPixelsPerUnit() {
		return pixelsPerUnit;
	}
	
	public void setBgImage(String path) {
		try {
			targetImage = ImageIO.read(new File(path));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public BufferedImage getBgImage() {
		return targetImage;
	}
	
}
