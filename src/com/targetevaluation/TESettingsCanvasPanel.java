package com.targetevaluation;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TESettingsCanvasPanel extends JPanel{
	private TESettingsDialog parent;
	private TESettings settings;
	
	private JButton selBgCol;
	private JButton selChCol;
	
	public TESettingsCanvasPanel(TESettingsDialog parent, TESettings settings) {
		super();
		this.parent = parent;
		this.settings = settings;
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		this.setLayout(gbl);

		JLabel l1 = new JLabel("Select background color");
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.ipadx = 5;
		gbc.ipady = 5;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.EAST;
		this.add(l1, gbc);
		
		selBgCol = new JButton();
		selBgCol.setName("selbgcol");
		selBgCol.setBackground(settings.getCanvasBgColor());
		selBgCol.addActionListener(al);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.ipadx = 5;
		gbc.ipady = 5;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(selBgCol, gbc);

		JLabel l2 = new JLabel("Select cross hair color");
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.ipadx = 5;
		gbc.ipady = 5;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.EAST;
		this.add(l2, gbc);
		
		selChCol = new JButton();
		selChCol.setName("selchcol");
		selChCol.setBackground(settings.getCrossHairColor());
		selChCol.addActionListener(al);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.ipadx = 5;
		gbc.ipady = 5;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(selChCol, gbc);
	}
	
	ActionListener al = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			String bName = b.getName();

			switch (bName){
			case "selbgcol":
				selectBgColor();
				break;
			case "selchcol":
				selectChColor();
				break;
			}
		};
	};
	
	private void selectBgColor() {
		Color color = JColorChooser.showDialog(null, "Choose a color", settings.getCanvasBgColor());

		if( color != null) {
			settings.setCanvasBgColor(color);
			selBgCol.setBackground(color);
			parent.refresh();
		}
	}

	private void selectChColor() {
		Color color = JColorChooser.showDialog(null, "Choose a color", settings.getCrossHairColor());

		if( color != null) {
			settings.setCrossHairColor(color);
			selChCol.setBackground(color);
			parent.refresh();
		}
	}

}
