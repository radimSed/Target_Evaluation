package com.targetevaluation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TEComboPanel extends JPanel{
	private TEGui gui;
	private TEGlobalVariables globVars;
	
	private JComboBox<String> scaleCombo;
	
	public TEComboPanel(TEGui gui, TEGlobalVariables globVars) {
		super();
		this.gui = gui;
		this.globVars = globVars;
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		this.setLayout(gbl);
		
		String[] scales = {"200", "150", "100", "75", "50"};
		scaleCombo = new JComboBox<String>(scales);
		scaleCombo.addItemListener(coListener);
		
		gbc.insets = new Insets(5,5,5,5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(scaleCombo, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;		
		JLabel label = new JLabel("%");
		this.add(label, gbc);

		this.setBorder(BorderFactory.createEtchedBorder());
}
	
	ItemListener coListener = new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
			setScale(e);
		}
	};
	
	private void setScale(ItemEvent e) {
		String s = "100";
		if(e.getStateChange() == 1) { //process only selected item
			s = (String) e.getItem();
		}
		
		double scale = Double.parseDouble(s)/100.0;
		globVars.setScale(scale);
		gui.setPixelsPerUnitText();
		gui.canvasRepaint();
	}
	
	public void setItem(int i) {
		this.scaleCombo.setSelectedIndex(i);
		gui.canvasRepaint();
	}

}
