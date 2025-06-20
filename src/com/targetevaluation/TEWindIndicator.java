package com.targetevaluation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class TEWindIndicator  extends JPanel{
	private int DIALSIZE = 100;
	private String NOTAPPLICABLE = "Not Applicable";
	
	private TEDial dial;
	private JSpinner directionSpin;
	
	private JSpinner velocitySpin;
	private JLabel velocityLabel;
	
	public TEWindIndicator() {
		super();
	}

	public TEWindIndicator(Integer angle, Double speed) {
		super();
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		this.setLayout(gbl);
		
		JLabel label = new JLabel("Wind direction and speed");
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		gbc.anchor = GridBagConstraints.CENTER;
		this.add(label, gbc);

		int columns[] = {120, 80, 20};
		gbl.columnWidths = columns;
		
		SpinnerListModel spinModel = new SpinnerListModel(prepareValues());
		 		
		directionSpin = new JSpinner(spinModel);
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(directionSpin, gbc);
		directionSpin.addChangeListener(al);

		dial = new TEDial(DIALSIZE, angle);
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		this.add(dial, gbc);
		
		velocitySpin = new JSpinner(new SpinnerNumberModel(0, 0, 20, 0.1));
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(velocitySpin, gbc);
		velocitySpin.addChangeListener(al);
		
		JLabel label2 = new JLabel("m/s");
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		this.add(label2, gbc);

		velocityLabel = new JLabel("<html><center>Crosswind<br>Not Applicable<center><html>");
		gbc.insets = new Insets(15, 5, 5, 10);
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.NORTHEAST;
		this.add(velocityLabel, gbc);

		if(speed != null) {
			velocitySpin.setValue(speed);
		}
		if(angle != null) {
			directionSpin.setValue(String.valueOf(angle));
		} else {
			directionSpin.setValue(NOTAPPLICABLE);
		}
}
	
	private void setSpeedLabel(Integer angle, double speed) {
		if(angle == null) {
			velocityLabel.setText("<html><center>Crosswind<br>Not Applicable<center><html>");
		} else {
			double crossWind = getCalculatedCrosswind(angle, speed);
			NumberFormat nf = NumberFormat.getNumberInstance();
			nf.setMaximumFractionDigits(2);
			velocityLabel.setText("<html><center>Crosswind<br>" + nf.format(crossWind) + " m/s<center><html>");
		}
	}
	
	public Double getCalculatedCrosswind(Integer angle, double speed) {
		Double result;
		if(angle == null) {
			result = null;
		} else {
			double angleRad = (2 * Math.PI) / 360 * angle;
			result = Math.abs(speed * Math.sin(angleRad));
		}
		
		return result;
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
	
	public void setDirection(Integer value) {
		if(value == null) {
			directionSpin.setValue(NOTAPPLICABLE);
		} else {
			directionSpin.setValue(String.valueOf(value));
		}
	}
	
	public void setSpeed(double speed) {
		velocitySpin.setValue(speed);
	}
	
	public String getDirection() {
		return (String)directionSpin.getValue();
	}

	public String getSpeed() {
		return velocitySpin.getValue().toString();
	}
	
	public void setEnabled(boolean value) {
		directionSpin.setEnabled(value);
		velocitySpin.setEnabled(value);
	}

	
	ChangeListener al = new ChangeListener() {
		
		@Override
		public void stateChanged(ChangeEvent e) {
			String value = (String) directionSpin.getValue();
			if(value.equals(NOTAPPLICABLE)) {
				dial.repaintDial(null);
				setSpeedLabel(null, (double)velocitySpin.getValue());
			} else {
				dial.repaintDial(Integer.parseInt(value));
				setSpeedLabel(Integer.parseInt(value), (double)velocitySpin.getValue());
			}
		};
	};

}
