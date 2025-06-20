package com.targetevaluation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class TECheckboxPanel extends JPanel{
	private JCheckBox aimCrossCheckb, impactCheckb, middlePointCheckb, s100Checkb, imageCheckb;
	private TEGui gui;
	private TEGlobalVariables globVars;
	
	public TECheckboxPanel(TEGui gui, TEGlobalVariables globVars) {
		super();
		this.gui = gui;
		this.globVars = globVars;
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		this.setLayout(gbl);
		
		aimCrossCheckb = new JCheckBox("Aim Cross", true);
		aimCrossCheckb.setName("aimCrossCheckb");
		impactCheckb = new JCheckBox("Impacts", true);
		impactCheckb.setName("impactCheckb");
		middlePointCheckb = new JCheckBox("Middle Point Of Impacts", true);
		middlePointCheckb.setName("middlePointCheckb");
		s100Checkb = new JCheckBox("2R100", true);
		s100Checkb.setName("s100Checkb");
		imageCheckb = new JCheckBox("Image", true);
		imageCheckb.setName("imageCheckb");
		
		globVars.setItemToShow("aimCrossCheckb", true);
		globVars.setItemToShow("impactCheckb", true);
		globVars.setItemToShow("middlePointCheckb", true);
		globVars.setItemToShow("s100Checkb", true);
		globVars.setItemToShow("imageCheckb", true);
		
		aimCrossCheckb.addItemListener(cbListener);
		impactCheckb.addItemListener(cbListener);
		middlePointCheckb.addItemListener(cbListener);
		s100Checkb.addItemListener(cbListener);
		imageCheckb.addItemListener(cbListener);
		
		gbc.insets = new Insets(5, 5, 0, 5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(aimCrossCheckb, gbc);

		gbc.insets = new Insets(0, 5, 0, 5);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(impactCheckb, gbc);

		gbc.insets = new Insets(0, 5, 0, 5);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(middlePointCheckb, gbc);

		gbc.insets = new Insets(0, 5, 0, 5);
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(s100Checkb, gbc);

		gbc.insets = new Insets(0, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(imageCheckb, gbc);

		this.setBorder(BorderFactory.createEtchedBorder());
	}
	
	ItemListener cbListener = new ItemListener() {
		
		@Override
		public void itemStateChanged(ItemEvent e) {
			JCheckBox cb = (JCheckBox) e.getItem();
			String cbName = cb.getName();
			
			switch (cbName){
			case "aimCrossCheckb":
				globVars.setItemToShow("aimCrossCheckb", cb.isSelected());
				break;
			case "impactCheckb":
				globVars.setItemToShow("impactCheckb", cb.isSelected());
				break;
			case "middlePointCheckb":
				globVars.setItemToShow("middlePointCheckb", cb.isSelected());
				break;
			case "s100Checkb":
				globVars.setItemToShow("s100Checkb", cb.isSelected());
				break;
			case "imageCheckb":
				globVars.setItemToShow("imageCheckb", cb.isSelected());
				break;
			}
			gui.canvasRepaint();
		}
	};
}
