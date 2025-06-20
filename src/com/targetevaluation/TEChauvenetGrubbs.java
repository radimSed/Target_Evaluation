package com.targetevaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class TEChauvenetGrubbs implements TEStrayBulletCalculation{
	protected TreeMap<Integer, Double> coeffs; // = new TreeMap<Integer, Double>();//new HashMap<Integer, Double>();
	protected TESettings settings;
	
	public TEChauvenetGrubbs(TreeMap<Integer, Double> coeffs, TESettings settings) {
		this.settings = settings;
		this.coeffs = coeffs;
	}

	@Override
	public List<TEImpact> calculateStrayBullet(List<TEImpact> impacts) {
		TEStatisctics stat = new TEStatisctics(settings);
		//calculate mpoi and stdDev from all impacts
		double mpoiCoords[] = stat.getMpoiCoords(impacts);
		double stdDev[] = stat.getStdDev(impacts, mpoiCoords);
		double coefficient = stat.getCoef(impacts.size(), coeffs);
		double testDistanceX = stdDev[0] * coefficient;
		double testDistanceY = stdDev[1] * coefficient;
		
		List<TEImpact> strayImpacts = new ArrayList<TEImpact>();
		for(TEImpact i: impacts) {
			double x = i.getX();
			double y = i.getY();
			if(((Math.abs(x - mpoiCoords[0])) > testDistanceX) || ((Math.abs(y - mpoiCoords[1])) > testDistanceY)) {
				strayImpacts.add(i);
			}
		}
		return strayImpacts;
	}
	
}
