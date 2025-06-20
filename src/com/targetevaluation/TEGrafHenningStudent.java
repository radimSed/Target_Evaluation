package com.targetevaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class TEGrafHenningStudent extends TEChauvenetGrubbs{
	public TEGrafHenningStudent(TreeMap<Integer, Double> coeffs, TESettings settings) {	
		super(coeffs, settings);
	}
	
	@Override
	public List<TEImpact> calculateStrayBullet(List<TEImpact> impacts) {
		TEStatisctics stat = new TEStatisctics(settings);

		List<TEImpact> strayImpacts = new ArrayList<TEImpact>();

		double coefficient = stat.getCoef(impacts.size(), coeffs);

		//calculate mpoi and srdDev from all impacts except the tested impact
		for(TEImpact i: impacts) {
			List<TEImpact> validImpacts = new ArrayList<TEImpact>(impacts);
			validImpacts.remove(i);

			double mpoiCoords[] = stat.getMpoiCoords(validImpacts);
			double stdDev[] = stat.getStdDev(validImpacts, mpoiCoords);
			double testDistanceX = stdDev[0] * coefficient;
			double testDistanceY = stdDev[1] * coefficient;
		
			double x = i.getX();
			double y = i.getY();
			if(((Math.abs(x - mpoiCoords[0])) > testDistanceX) || ((Math.abs(y - mpoiCoords[1])) > testDistanceY)) {
				strayImpacts.add(i);
			}
		}
		return strayImpacts;
	}
}
