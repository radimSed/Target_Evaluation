package com.targetevaluation;

import java.awt.Color;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TEImpactSet {
	private final String NULL = "null";
	private final String NOTAPPLICABLE = "Not Applicable";
	
	private int impactSetId;
	private boolean isVisible;
	private String name; //name of the file
	private Color color;
	private TEMpoiMark mpoiMark; //middle point of impact mark
	private TE2R100 circle2R100; //2R100 circle
	
	private TEGlobalVariables globVars;
	
	private String place;
	private LocalDate date;
	private LocalTime time;
	private String position;
	private Double distance;
	private String targetNmbr;
	private int angle;
	private String lightDir;

	private Double temp;
	private Double pressure;
	private Double humidity;
	private String mirageCategory;
	private String mirageClassification;
	private String windDirection;
	private String windSpeed;

	private String rifleAssembly;
	private double caliber; 
	private String ammunition;
	private String batch;
	private Double initVelocity;
	private int elevation; 
	private int windageVal;
	private String windageDir;
	
	private String notes;
	
	private List<TEImpact> impacts = new ArrayList<TEImpact>();

	public TEImpactSet(TEImpactSetRec isr, int setId, String name, Color color, TEGlobalVariables globVars) {
		this.globVars = globVars;
		this.impactSetId = setId;
		this.name = name;
		this.color = color;
		this.isVisible = true;
		
		this.place = isr.place();
		this.date = LocalDate.parse(isr.date());
		this.time = LocalTime.parse(isr.time());
		this.position = isr.position();
		this.distance = getDoubleFromString(isr.distance());
		this.targetNmbr = isr.targetNmbr();
		this.angle = Integer.parseInt(isr.angle());
		this.lightDir = isr.lightDir();

		this.temp = getDoubleFromString(isr.temp());
		this.pressure = getDoubleFromString(isr.pressure());
		this.humidity = getDoubleFromString(isr.humidity());
		this.mirageCategory = isr.mirageCategory();
		this.mirageClassification = isr.mirageClassification();
		this.windDirection = isr.windDirection();
		this.windSpeed = isr.windSpeed();
		
		this.rifleAssembly = isr.rifleAssembly();
		Double tempCal = getDoubleFromString(isr.caliber());
		if(tempCal == null) { //use default caliber
			this.caliber = globVars.getDefaultCaliber();
		} else {
			this.caliber = tempCal;
		}
		this.ammunition = isr.amunition();
		this.batch = isr.batch();
		this.initVelocity = getDoubleFromString(isr.initVelocity());
		this.elevation = Integer.parseInt(isr.elevation());
		this.windageVal = Integer.parseInt(isr.windageVal());
		this.windageDir = isr.windageDir();
		
		this.notes = isr.notes();
		
		ArrayList<TEImpact> impactList = new ArrayList<TEImpact>();
		for(TEImpactRec ir: isr.impacts()) {
			impactList.add(new TEImpact(this, ir.impactNumber(), ir.x(), ir.y(), this.globVars));
		}
		setImpacts(impactList);
	}
	
	public String getNotes() {
		return notes;
	}
	
	public String getWindageDirection() {
		return windageDir;
	}
	
	public Integer getWindageValue() {
		return windageVal;
	}
	
	public Integer getElevation() {
		return elevation;
	}
	
	public Double getInitialVelocity() {
		return initVelocity;
	}
	
	public String getBatch() {
		return batch;
	}
	
	public String getAmmunition() {
		return ammunition;
	}
	
	public String getRifleAssembly() {
		return rifleAssembly;
	}
	
	private Double getDoubleFromString(String input) {
		if(input.equals(NULL)) {
			return null;
		} else {
			return Double.parseDouble(input); 
		}
	}
	
	public String getMirageClassification() {
		return mirageClassification;
	}
	
	public String getMirageCategory() {
		return mirageCategory;
	}
	
	public String getMirageEstimatedCrosswind() {
		return new TEMirageClassifier().getEstimatedCrosswind(mirageClassification);
	}
	public String getCalculatedCrosswind() {
		if(windDirection.equals(NOTAPPLICABLE)){
			return NOTAPPLICABLE;
		} else {
			return String.valueOf(new TEWindIndicator().getCalculatedCrosswind(Integer.parseInt(windDirection), Double.parseDouble(windSpeed)));
		}
	}
	
	
	public Double getHumidity() {
		return humidity;
	}
	
	public Double getPressure() {
		return pressure;
	}
	
	public Double getTemperature() {
		return temp;
	}
	
	public Integer getAngle() {
		return angle;
	}
	
	public String getTargetNmbr() {
		return targetNmbr;
	}
	
	public String getPosition() {
		return position;
	}
	
	public String getTime() {
		return time.toString();
	}
	
	public String getDate() {
		return date.toString();
	}
	
	public Double getDistance() {
		return distance;
	}
	
	public String getPlace() {
		return place;
	}
	
	public Double getMOA() {
		Double moa;
		
		if(distance != null) {
		double oneMOA = (2 * Math.PI * distance * 1000)/(360 * 60); //1 minute of angle at the distance
		moa = circle2R100.getD() / oneMOA; //return fraction of the MOA
		} else {
			moa = null;
		}
		
		return moa;
	}
	
	public void setImpacts(List<TEImpact> impacts) {
		this.impacts = impacts;
		TEStatisctics stat = new TEStatisctics(globVars.getSettings());
		mpoiMark = stat.calculateMpoi(impacts, this);
		circle2R100 = stat.calculate2R100(impacts, this);
	}
	
	public int getId() {
		return impactSetId;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Double getCaliber() {
		return caliber;
	}
	
	public List<TEImpact> getImpacts(){
		return impacts;
	}
	
	public void setMpoiMark(TEMpoiMark mpoiMark) {
		this.mpoiMark = mpoiMark;
	}
	
	public TEMpoiMark getMpoiMark() {
		return mpoiMark;
	}
	
	public void set2R100(TE2R100 circle2R100) {
		this.circle2R100 = circle2R100;
	}
	
	public TE2R100 get2R100() {
		return circle2R100;
	}
	
	public void recalculate2R100() {
		TEStatisctics stat = new TEStatisctics(globVars.getSettings());
		circle2R100 = stat.calculate2R100(impacts, this);
	}
	
	public boolean getVisibility() {
		return isVisible;
	}
	
	public void setVisibility(boolean value) {
		isVisible = value;
	}
	
	public String getName() {
		return name;
	}
	
	public Integer getLightDirection() {
		if(lightDir.equals(NOTAPPLICABLE)) {
			return null;
		} else {
			return (Integer.parseInt(lightDir));
		}
	}
	
	public Integer getWindDirection() {
		if(windDirection.equals(NOTAPPLICABLE)) {
			return null;
		} else {
			return (Integer.parseInt(windDirection));
		}
	}
	
	public Double getWindSpeed() {
		return Double.parseDouble(windSpeed);
	}
	
	/**
	 * 
	 * @return array of doubles - [xmin, xmax, ymin, ymax]. 
	 */
	public double[] getExtrems() {
		double[] coords = new double[2];
		double[] extrems = {0.0, 0.0, 0.0, 0.0};
		
		for(int i = 0; i < impacts.size(); i++) {
			coords = impacts.get(i).getCoords();
			if(i == 0) {
				extrems[0] = coords[0];
				extrems[1] = coords[0];
				extrems[2] = coords[1];
				extrems[3] = coords[1];
			} else {
				if(coords[0] < extrems[0]) {
					extrems[0] = coords[0];
				}
				if(coords[0] > extrems[1]) {
					extrems[1] = coords[0];
				}
				if(coords[1] < extrems[2]) {
					extrems[2] = coords[1];
				}
				if(coords[1] > extrems[3]) {
					extrems[3] = coords[1];
				}
			}
		}
		
		return extrems;
	}
	

}
