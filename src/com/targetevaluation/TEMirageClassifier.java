package com.targetevaluation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TEMirageClassifier extends JPanel{

	JLabel crossWind1Label;
	JComboBox<TEMirageCategory> mirageCategoryCombo;
	JComboBox<TEMirageClassification> mirageClassificationCombo;
	
	public TEMirageClassifier() {
		super();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		this.setLayout(gbl);

		JLabel mirageLabel = new JLabel("Mirage");
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(10, 10, 5, 10); //top, left, bottom, right
		gbc.anchor = GridBagConstraints.CENTER;
		this.add(mirageLabel, gbc);

		mirageCategoryCombo = new JComboBox<TEMirageCategory>(TEMirageCategory.values());
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.insets = new Insets(0, 10, 5, 5); //top, left, bottom, right
		this.add(mirageCategoryCombo, gbc);
		mirageCategoryCombo.addActionListener(al);
		
		mirageClassificationCombo = new JComboBox<TEMirageClassification>(TEMirageClassification.values());
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridheight = 1;
		gbc.insets = new Insets(0, 10, 10, 5); //top, left, bottom, right
		this.add(mirageClassificationCombo, gbc);
		mirageClassificationCombo.addActionListener(al);

		crossWind1Label = new JLabel("<html><center>Estimated crosswind<br>Not Applicable</center></html>");
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridheight = 2;
		gbc.insets = new Insets(0, 0, 10, 10); //top, left, bottom, right
		this.add(crossWind1Label, gbc);
		
	}
	
	public void setEnabled(boolean value) {
		mirageCategoryCombo.setEnabled(value);
		mirageClassificationCombo.setEnabled(value);
	}
	

	
	public String getMirageClassification() {
		return mirageClassificationCombo.getSelectedItem().toString();
	}
	
	public String getMirageCategory() {
		return mirageCategoryCombo.getSelectedItem().toString();
	}
	
	public void setMirageClassification(String value) {
		for(int i = 0; i < mirageClassificationCombo.getItemCount(); i++) {
			if(mirageClassificationCombo.getItemAt(i).toString().equals(value)) {
				mirageClassificationCombo.setSelectedIndex(i);
				return;
			}
		}
	}
	
	public void setMirageCategory(String value) {
		for(int i = 0; i < mirageCategoryCombo.getItemCount(); i++) {
			if(mirageCategoryCombo.getItemAt(i).toString().equals(value)) {
				mirageCategoryCombo.setSelectedIndex(i);
				return;
			}
		}
	}
	
	public String getEstimatedCrosswind(String value) {
		String ecw;
		
		switch (value.strip()) {
		case "Boiling":
			ecw = "0";
			break;
		case "Slow":
			ecw = "0.5 - 1.3";
			break;
		case "Medium":
			ecw = "1.8 - 3.1";
			break;
		case "Fast":
			ecw = "3.6 - 5.4";
			break;
		default:
			ecw = "Not Applicable";
			break;
		}
		
		return ecw;
	}

	ActionListener al = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			crossWind1Label.setText("<html><center>Estimated crosswind<br>" + 
			getEstimatedCrosswind(mirageClassificationCombo.getSelectedItem().toString()) + "</center></html>");
		}
	};
		
}
