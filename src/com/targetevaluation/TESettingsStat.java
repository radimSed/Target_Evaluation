package com.targetevaluation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TESettingsStat  extends JPanel{
	private TESettingsDialog parent;
	private TESettings settings;
	
	public TESettingsStat(TESettingsDialog parent, TESettings settings) {
	super();
	this.parent = parent;
	this.settings = settings;

	GridBagLayout gbl = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();
	this.setLayout(gbl);

	JLabel l1 = new JLabel("<html>Diameter of 2R100 circle<br>over outer edge of impacts<html>");
	gbc.insets = new Insets(5, 5, 5, 5);
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.gridwidth = 1;
	gbc.ipadx = 5;
	gbc.ipady = 5;
	gbc.weightx = 1;
	gbc.anchor = GridBagConstraints.EAST;
	this.add(l1, gbc);
	
	JCheckBox jcb = new JCheckBox("", settings.getOuterEdge2R100());
	jcb.setName("outerEdges");
	jcb.addItemListener(cbListener);
	gbc.gridx = 1;
	gbc.gridy = 0;
	gbc.gridwidth = 1;
	gbc.ipadx = 5;
	gbc.ipady = 5;
	gbc.weightx = 1;
	gbc.anchor = GridBagConstraints.WEST;
	this.add(jcb, gbc);
	}
	
	ItemListener cbListener = new ItemListener() {
		
		@Override
		public void itemStateChanged(ItemEvent e) {
			JCheckBox cb = (JCheckBox) e.getItem();
			String cbName = cb.getName();
			
			switch (cbName){
			case "outerEdges":
				settings.setOuterEdge2R100(cb.isSelected());
				break;
			}
			parent.refresh();
		}
	};
}
