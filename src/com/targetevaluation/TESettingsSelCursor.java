package com.targetevaluation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class TESettingsSelCursor extends JPanel{
	private TESettingsDialog parent;
	private TESettings settings;
	
	public TESettingsSelCursor(TESettingsDialog parent, TESettings settings) {
	super();
	
	this.parent = parent;
	this.settings = settings;
	
	GridBagLayout gbl = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();
	this.setLayout(gbl);

	JLabel l1 = new JLabel("Size of the Cross");
	gbc.insets = new Insets(5, 5, 5, 5);
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.gridwidth = 1;
	gbc.ipadx = 5;
	gbc.ipady = 5;
	gbc.weightx = 1;
	gbc.anchor = GridBagConstraints.EAST;
	this.add(l1, gbc);
	
	SpinnerNumberModel sizeSpinModel = new SpinnerNumberModel(settings.getCrossSize(), 0, 20, 0.1);
	JSpinner sizeSpin = new JSpinner(sizeSpinModel);
	sizeSpin.setName("size");
	gbc.insets = new Insets(5, 5, 5, 5);
	gbc.gridx = 1;
	gbc.gridy = 0;
	gbc.anchor = GridBagConstraints.WEST;
	this.add(sizeSpin, gbc);


	JLabel l2 = new JLabel("Outer Diameter");
	gbc.insets = new Insets(5, 5, 5, 5);
	gbc.gridx = 0;
	gbc.gridy = 1;
	gbc.anchor = GridBagConstraints.EAST;
	this.add(l2, gbc);
	
	SpinnerNumberModel outerDSpinModel = new SpinnerNumberModel(settings.getOuterDiameter(), 0, 20, 0.1);
	JSpinner outerDSpin = new JSpinner(outerDSpinModel);
	outerDSpin.setName("outer");
	gbc.insets = new Insets(5, 5, 5, 5);
	gbc.gridx = 1;
	gbc.gridy = 1;
	gbc.anchor = GridBagConstraints.WEST;
	this.add(outerDSpin, gbc);


	JLabel l3 = new JLabel("Inner Diameter");
	gbc.insets = new Insets(5, 5, 5, 5);
	gbc.gridx = 0;
	gbc.gridy = 2;
	gbc.anchor = GridBagConstraints.EAST;
	this.add(l3, gbc);
	
	SpinnerNumberModel innerDSpinModel = new SpinnerNumberModel(settings.getInnerDiameter(), 0, 20, 0.1);
	JSpinner innerDSpin = new JSpinner(innerDSpinModel);
	innerDSpin.setName("inner");
	gbc.insets = new Insets(5, 5, 5, 5);
	gbc.gridx = 1;
	gbc.gridy = 2;
	gbc.anchor = GridBagConstraints.WEST;
	this.add(innerDSpin, gbc);
	
	sizeSpin.addChangeListener(cl);
	outerDSpin.addChangeListener(cl);
	innerDSpin.addChangeListener(cl);
	}
	
	ChangeListener cl = new ChangeListener() {
		
		@Override
		public void stateChanged(ChangeEvent e) {
			JSpinner s = (JSpinner) e.getSource();
			String sName = s.getName();
			
			switch (sName){
			case "size":
				settings.setCrossSize((double)s.getValue());
				break;

			case "outer":
				settings.setOuterDiameter((double)s.getValue());
				break;
			
			case "inner":
				settings.setInnerDiameter((double)s.getValue());
				break;
			}
			parent.refresh();
		}
	};
}
