package com.targetevaluation;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class TELightDirIndicator extends JPanel{
	private int DIALSIZE = 100;
	
	private TEDial dial;
	private JSpinner spin;
	
	private String NOTAPPLICABLE = "Not Applicable";

	
	public TELightDirIndicator(Integer arrowAngle) {
		super();
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		this.setLayout(gbl);
		
		JLabel label = new JLabel("Light direction");
		gbc.insets = new Insets(5, 5, 0, 5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(label, gbc);
		
		SpinnerListModel spinModel = new SpinnerListModel(prepareValues());
		spin = new JSpinner(spinModel);
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(spin, gbc);

		dial = new TEDial(DIALSIZE, arrowAngle);
		gbc.insets = new Insets(0, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		this.add(dial, gbc);

		spin.addChangeListener(al);
	}
	
	private List<String> prepareValues() {
		List<String> values = new ArrayList<String>();
		values.add(NOTAPPLICABLE);
		values.add("0");
		values.add("10");
		values.add("20");
		values.add("30");
		values.add("40");
		values.add("45");
		values.add("50");
		values.add("60");
		values.add("70");
		values.add("80");
		values.add("90");
		values.add("100");
		values.add("110");
		values.add("120");
		values.add("130");
		values.add("135");
		values.add("140");
		values.add("150");
		values.add("160");
		values.add("170");
		values.add("180");
		values.add("190");
		values.add("200");
		values.add("210");
		values.add("220");
		values.add("225");
		values.add("230");
		values.add("240");
		values.add("250");
		values.add("260");
		values.add("270");
		values.add("280");
		values.add("290");
		values.add("300");
		values.add("310");
		values.add("315");
		values.add("320");
		values.add("330");
		values.add("340");
		values.add("350");
		
		return values;
	}
	
	public String getValue() {
		return (String)spin.getValue();
	}
	
	public void setValue(Integer value) {
		if(value == null) {
			spin.setValue(NOTAPPLICABLE);
		} else {
			spin.setValue(String.valueOf(value));
		}
	}
	
	public void setColor(Color color) {
		spin.setForeground(color);
	}
	

	
	ChangeListener al = new ChangeListener() {
		
		@Override
		public void stateChanged(ChangeEvent e) {
			JSpinner spin = (JSpinner) e.getSource();
			String value = (String)spin.getValue();
			if(value.equals(NOTAPPLICABLE)) {
				dial.repaintDial(null);
			} else {
				dial.repaintDial(Integer.parseInt(value));
			}
		};
	};

}
