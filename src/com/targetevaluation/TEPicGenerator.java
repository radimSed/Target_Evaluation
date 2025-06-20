package com.targetevaluation;

import java.awt.image.BufferedImage;


public class TEPicGenerator{
	private final int DIALPICHEIGHT = 150;
	
	public TEPicGenerator() {
	}
	
	public BufferedImage createImpactPic(TEImpactSet set) {
		TEImpactPicGen ig = new TEImpactPicGen(set);
		return ig.generatePicOfImpacts();
	}
	
	public BufferedImage createLightDirectionPic(TEImpactSet set) {
		TEDial d = new TEDial(DIALPICHEIGHT, set.getLightDirection());
		return d.getPic();
	}
	
	public BufferedImage createWindDirectionPic(TEImpactSet set) {
		TEDial d = new TEDial(DIALPICHEIGHT, set.getWindDirection());
		return d.getPic();
	}
	
}
