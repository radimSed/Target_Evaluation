package com.targetevaluation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TESidePanel extends JPanel{
	
	private TEInfoPanel infoPanel;
	private TECheckboxPanel checkBoxPanel;
	private TEComboPanel comboPanel;
	
	public TESidePanel(TEGui gui, TEGlobalVariables globVars) {
		super();
		
		this.infoPanel = new TEInfoPanel(globVars);
		this.checkBoxPanel = new TECheckboxPanel(gui, globVars);
		this.comboPanel = new TEComboPanel(gui, globVars);

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		
		this.setLayout(gbl);
		
		gbc.insets = new Insets(5,5,5,5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		this.add(infoPanel, gbc);

		gbc.insets = new Insets(5,5,5,5);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		this.add(checkBoxPanel, gbc);
		
		gbc.insets = new Insets(5,5,5,5);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weighty = 1;		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.SOUTH;
		this.add(comboPanel, gbc);
	}
	
	public void setXYInfo(double x, double y) {
		this.infoPanel.setXYText(x, y);
	}
	
	public void setPixelsPerUnitText() {
		this.infoPanel.setPixelsPerUnitText();
	}
	
	public void setScaleindex(int i) {
		this.comboPanel.setItem(i);
	}

}
