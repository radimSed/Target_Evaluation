package com.targetevaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class TEStatisctics {
	private TESettings settings;
	
	public TEStatisctics(TESettings settings) {
		this.settings = settings;
		
	}
	/**
	 * calculates middle point of impact and returns MPOI mark
	 */
	public TEMpoiMark calculateMpoi(List<TEImpact> impacts, TEImpactSet set) {
		TEMpoiMark mark = null;

		ArrayList<TEImpact> impactsList = new ArrayList<TEImpact>();
		for(TEImpact i : impacts) {
			if(!(i.getStrayBullet())) {
				impactsList.add(i);
			}
		}
		
		double[] coords = getMpoiCoords(impactsList);
		
		if(coords != null) {
			mark = new TEMpoiMark(set, coords[0], coords[1]);
		}

		return mark;
	}

	/**
	 * only impacts valid for calculation should be included in the list
	 */
	public double[] getMpoiCoords(List<TEImpact> impacts) {
		
		if(impacts.size() == 0) { //in case of empty impact list
			return null;
		}
		
		double x = 0.0, y = 0.0, mx = 0, my = 0;
		int numberOfImpacts = 0;
		
			for(TEImpact i : impacts) {
				x += i.getX();
				y += i.getY();
				numberOfImpacts++;
			}
		
		mx = x / numberOfImpacts;
		my = y / numberOfImpacts;
		double[] result = {mx, my};
		return result;
	}

	/**
	 * 
	 * @param impacts - list of impacts included in the calculation
	 * @param mpoiCoords - coordinated of middle point of impact [x, y]
	 * @return standard deviation in x and y direction [sx, sy]
	 */
	public double[] getStdDev(List<TEImpact> impacts, double[] mpoiCoords) {
		double sumx = 0, sumy = 0;
//		double mpoiCoords[] = getMpoiCoords(impacts);
		
		if((impacts.size() == 0) || mpoiCoords == null) {
			return null;
		}
		
		for(TEImpact i : impacts) {
			sumx += Math.pow((i.getX() - mpoiCoords[0]), 2); 
			sumy += Math.pow((i.getY() - mpoiCoords[1]), 2); 
		}
		
		double sx = Math.sqrt(sumx/(impacts.size() - 1)); 
		double sy = Math.sqrt(sumy/(impacts.size() - 1));

		double[] result = {sx, sy};
		return result;
	}

	public TE2R100 calculate2R100(List<TEImpact> impacts, TEImpactSet set) {
		TE2R100 circle2p = null, circle3p = null;

		if(impacts.size() < 2) { //unable to calculate any 2R100
			return null; //finish
		}
		
		circle2p = get2PointCircle(impacts, set);
		if(test(impacts, circle2p)) {
			if(settings.getOuterEdge2R100()) {
				double caliber = set.getCaliber();
				circle2p.setD(circle2p.getD() + caliber);
			}
			return circle2p;
		}
		
		circle3p = get3PointCircle(impacts, set);
		if(settings.getOuterEdge2R100()) {
			double caliber = set.getCaliber();
			circle3p.setD(circle3p.getD() + caliber);
		}
		return circle3p;
	}

	/**
	 * checks if all impacts are within given circle
	 */
	private boolean test(List<TEImpact> impacts, TE2R100 circle) {
		double xc, yc, distance, diameter;

		if(circle == null) {
			return false;
		}
		
		xc = circle.getX();
		yc = circle.getY();
		diameter = circle.getD();
		
		for(TEImpact i : impacts) {
			if(!i.getStrayBullet()) {
				double deltaX = xc - i.getX();
				double deltaY = yc - i.getY();
				distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
				if(diameter/2 - distance < -0.001){  // if the impact is outside of the circle by less than 0.001, it is OK (to avoid numerical errors)
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * expects group of impact creating rather wide/high pattern so all impacts 
	 * are within a circle defined by two points which are most distant from each other  
	 */
	private TE2R100 get2PointCircle(List<TEImpact> impacts, TEImpactSet set) {
		double distance, distanceMax = 0, x, y, d;
		TEImpact impact1, impact2;
		double deltax, deltay;
		TE2R100 twoPointCircle = null;
		
		for(int i = 0; i < impacts.size() - 1; i++) {
			impact1 = impacts.get(i);

			if(!impact1.getStrayBullet()) {
				for(int j = i+1; j < impacts.size(); j++) {
					impact2 = impacts.get(j);

					if(!impact2.getStrayBullet()) { //calculate 2R100 only for non strayed impacts
						deltax = impact1.getX() - impact2.getX();
						deltay = impact1.getY() - impact2.getY();
						distance = Math.sqrt(Math.pow(deltax, 2) + Math.pow(deltay, 2));

						if( distance > distanceMax ) {
							distanceMax = distance;
							x = (impact1.getX() + impact2.getX()) / 2;
							y = (impact1.getY() + impact2.getY()) / 2;
							d = distanceMax;
							twoPointCircle = new TE2R100(set, x, y, d);
						}
					}
				}
			}
		}
		return twoPointCircle;
	}

	/**
	 * for group where 2point 2R100 does not contain all impacts 
	 */
	private TE2R100 get3PointCircle(List<TEImpact> impacts, TEImpactSet set) {
		TEImpact impact1, impact2, impact3;
		double minDiameter = Double.MAX_VALUE;
		double diameter;
		TE2R100 minCircle = null;
		
		for(int i = 0; i < impacts.size() - 2; i++) {
			impact1 = impacts.get(i);
			
			for(int j = i + 1; j < impacts.size() - 1; j++) {
				impact2 = impacts.get(j);
				
				for(int k = j + 1; k < impacts.size(); k++) {
					impact3 = impacts.get(k);

					if(!(impact1.getStrayBullet() || impact2.getStrayBullet() || impact3.getStrayBullet())) {

						TE2R100 circle = calculate3pointCircle(impact1, impact2, impact3, set);
						if(test(impacts, circle)) {
							diameter = circle.getD() ;
							if(diameter < minDiameter) {
								minCircle = circle;
								minDiameter = diameter;
							}
						}
					}
				}
			}
		}
		return minCircle;
	}
	
	private TE2R100 calculate3pointCircle(TEImpact i1, TEImpact i2, TEImpact i3, TEImpactSet set) {
		double x1, y1, x2, y2, x3, y3, m1, n1, m2, n2, xc, yc, d, c1, c2, mx;
		
		x1 = i1.getX(); //point A
		y1 = i1.getY(); //point A
		x2 = i2.getX(); //point B
		y2 = i2.getY(); //point B
		x3 = i3.getX(); //point C
		y3 = i3.getY(); //point C
		
        m1 = (x1 + x2) / 2; //middle point of A-B
        n1 = (y1 + y2) / 2;
        m2 = (x1 + x3) / 2; //middle point of A-C
        n2 = (y1 + y3) / 2;
        
        c1 = (x2 - x1) * m1 + (y2 - y1) * n1;
        c2 = (x3 - x1) * m2 + (y3 - y1) * n2;
        mx = (x2 - x1) * (y3 - y1) + (y2 - y1) * (x1 - x3);

        xc = (c1 * (y3 - y1) + c2 * (y1 - y2)) / mx; //coordinates of the center
        yc = (c1 * (x1 - x3) + c2 * (x2 - x1)) / mx; 
        d = (Math.sqrt(Math.pow((xc - x1),2) + Math.pow((yc - y1),2))) * 2; //radius
        
        return new TE2R100(set, xc, yc, d);
	}
	
	public double getCoef(int size, TreeMap<Integer, Double> coef) {
		Integer bottom = coef.lowerKey(size);
		Integer top = coef.higherKey(size);
		Double resultValue;

		resultValue = coef.get(size);
		if(resultValue == null) {
			bottom = coef.lowerKey(size);
			top = coef.higherKey(size);
			if( bottom == null ) {           //there is no lower key
				resultValue = coef.get(top);        // --> return nearest higher entry
			} else if (top == null) {        //there is no higher key
				resultValue = coef.get(bottom);     // --> return nearest lower entry
			} else {
				resultValue = findInterpolatedValue(size, coef, bottom, top);
			}
		}
		return resultValue;
	}
	
	private double findInterpolatedValue(int key, TreeMap<Integer, Double> coef, int bottom, int top) {
		double valueBottom = coef.get(bottom);
		double valueTop = coef.get(top);
		double tgAlpha = (valueTop - valueBottom)/(top - bottom); //tangent alpha = deltaY/deltaX
		return (key - bottom) * tgAlpha + valueBottom; //calculates interpolated value
	}
}
