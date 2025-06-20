package com.targetevaluation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.*;

@SuppressWarnings("serial")
public class TEInfoPanel extends JPanel{
	private JLabel unitJlab, xJlab, yJlab;
	private TEGlobalVariables globVars;
	
	public TEInfoPanel(TEGlobalVariables globVars) {
		super();
		this.globVars = globVars;
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		
		this.setLayout(gbl);

		this.unitJlab = new JLabel();
		this.setPixelsPerUnitText();
		
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.add(unitJlab, gbc);
		
		this.xJlab = new JLabel("X = 0.0");
		gbc.gridx = 0;
		gbc.gridy = 1;
		this.add(xJlab, gbc);

		this.yJlab = new JLabel("Y = 0.0");
		gbc.gridx = 0;
		gbc.gridy = 2;
		this.add(yJlab, gbc);
		
		this.setBorder(BorderFactory.createEtchedBorder());
	}
	
	public void setXYText(double x, double y) { //x, y comes in pixels
		double unitx, unity; //x, y coordinates in "units"
		double pixToUnit = globVars.getPixelsPerUnit(); 
		unitx = x/pixToUnit;
		unity = y/pixToUnit;
		DecimalFormat df = new DecimalFormat("0.0");

		String xText = "X = " + df.format(unitx) + " mm";
		String yText = "Y = " + df.format(unity) + " mm";
		xJlab.setText(xText);
		yJlab.setText(yText);
	}
	
	public void setPixelsPerUnitText() {
		double pixels = globVars.getPixelsPerUnit() * globVars.getScale();
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);
		
		String text = "1 mm = " + nf.format(pixels) + " pixels";
		this.unitJlab.setText(text);
	}
}
