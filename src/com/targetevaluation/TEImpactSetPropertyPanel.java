package com.targetevaluation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper; 
import com.fasterxml.jackson.databind.ObjectWriter;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;

@SuppressWarnings("serial")
public class TEImpactSetPropertyPanel extends JDialog {
	private TEGui gui;
	private TEGlobalVariables globVars;
	
	private final int LENGTH = 10;
	
	//place and conditions
	private JTextField placeField;
	private DatePicker datePicker;
	private TimePicker timePicker;
	private JComboBox<TEPosition> positionCombo;
	private JTextField distanceField;
	private JTextField targetNmbrField;
	private JSpinner angleSpin;
	private TELightDirIndicator lightInd;
	
	//Weather conditions
	private JTextField tempField;
	private JTextField pressField;
	private JTextField humiField;
	private TEMirageClassifier mirage;	
	private TEWindIndicator windInd;

	//right panel
	//rifle panel
	private JTextField rifleField;
	private JTextField caliberField;
	private JTextField ammunitionField;
	private JTextField batchField;
	private JTextField initVelocityField;
	private JSpinner elevationSpin;
	private JSpinner windageSpin;
	private JComboBox<TEWindage> windageDirectionCombo;

	private JTextArea textArea;

	private JCheckBox cb;
	private JButton okBut;	
	private JButton cancelBut;
	
	private final String NULL = "null";
	
	public TEImpactSetPropertyPanel(TEImpactSet set) {
		this(null, null);
		this.setTitle(set.getName());
		okBut.setVisible(false);
		cb.setVisible(false);

		placeField.setText(set.getPlace());
		datePicker.setText(set.getDate());
		timePicker.setText(set.getTime());
		setPositionComboValue(set.getPosition());
		distanceField.setText(String.valueOf(set.getDistance()));
		targetNmbrField.setText(set.getTargetNmbr());
		angleSpin.setValue(set.getAngle());
		lightInd.setValue(set.getLightDirection());
		
		tempField.setText(String.valueOf(set.getTemperature()));
		pressField.setText(String.valueOf(set.getPressure()));
		humiField.setText(String.valueOf(set.getHumidity()));
		mirage.setMirageCategory(set.getMirageCategory());
		mirage.setMirageClassification(set.getMirageClassification());	
		windInd.setDirection(set.getWindDirection());
		windInd.setSpeed(set.getWindSpeed());
		
		rifleField.setText(set.getRifleAssembly());
		caliberField.setText(String.valueOf(set.getCaliber()));
		ammunitionField.setText(set.getAmmunition());
		batchField.setText(set.getBatch());
		initVelocityField.setText(String.valueOf(set.getInitialVelocity()));
		elevationSpin.setValue(set.getElevation());
		windageSpin.setValue(set.getWindageValue());
		setWindaqgeComboValue(set.getWindageDirection());
		textArea.setText(set.getNotes());
	}
	
	public TEImpactSetPropertyPanel(TEGui gui, TEGlobalVariables globVars) {
		super();
		this.gui = gui;;
		this.globVars = globVars;
		
		this.setTitle("Fill properties of the set");
		this.setSize(910, 550);
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		this.setLayout(gbl);
		
		JPanel leftPanel = createLeftPanel();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(10, 10, 0, 5); //top, left, bottom, right
		this.add(leftPanel, gbc);

		JPanel middlePanel = createMiddlePanel(); 
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(10, 5, 0, 5); //top, left, bottom, right
		this.add(middlePanel, gbc);

		JPanel rightPanel = createRightPanel(); 
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(10, 5, 0, 10); //top, left, bottom, right
		this.add(rightPanel, gbc);

		cb = new JCheckBox("After save load the file");
		cb.setSelected(true);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.ipadx = 5;
		gbc.ipady = 5;
		gbc.gridwidth = 3;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(0, 10, 10, 10); //top, left, bottom, right
		this.add(cb, gbc);
		
		okBut = new JButton("Ok");
		okBut.setName("ok");
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.ipadx = 5;
		gbc.ipady = 5;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(0, 10, 10, 10); //top, left, bottom, right
		gbc.fill = GridBagConstraints.NONE;
		this.add(okBut, gbc);
		
		cancelBut = new JButton("Cancel");
		cancelBut.setName("cancel");
		gbc.gridx = 2;
		gbc.gridy = 2;
		gbc.ipadx = 5;
		gbc.ipady = 5;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(0, 10, 10, 10); //top, left, bottom, right
		gbc.fill = GridBagConstraints.NONE;
		this.add(cancelBut, gbc);
		
		okBut.addActionListener(al);
		cancelBut.addActionListener(al);
	}
	
	private void setWindaqgeComboValue(String value) {
		for(int i = 0; i < windageDirectionCombo.getItemCount(); i++) {
			if(windageDirectionCombo.getItemAt(i).toString().equals(value)) {
				windageDirectionCombo.setSelectedIndex(i);
				return;
			}
		}
	}

	private void setPositionComboValue(String value) {
		for(int i = 0; i < positionCombo.getItemCount(); i++) {
			if(positionCombo.getItemAt(i).toString().equals(value)) {
				positionCombo.setSelectedIndex(i);
				return;
			}
		}
	}
	
	private JPanel createLeftPanel() {
		JPanel panel = new JPanel();
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		panel.setLayout(gbl);

		createPlaceDataBlock(panel, gbc, 0);
		createDateDataBlock(panel, gbc, 1);
		createTimeDataBlock(panel, gbc, 2);
		createPositionDataBlock(panel, gbc, 3);
		createDistanceDataBlock(panel, gbc, 4);
		createTargetNmbrDataBlock(panel, gbc, 5);
		createAngleDataBlock(panel, gbc, 6);
		createLightIndDataBlock(panel, gbc, 7);
		
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Place and conditions"));

		return panel;
	}

	private JPanel createMiddlePanel() {
		JPanel panel = new JPanel();
	
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		panel.setLayout(gbl);
		
		createAirTempDataBlock(panel, gbc, 0);
		createAirPressureDataBlock(panel, gbc, 1);
		createHumidityDataBlock(panel, gbc, 2);
		createMirageDataBlock(panel, gbc, 3);
		createWindDataBlock(panel, gbc, 4);
		
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Weather condition"));

		return panel;
}

	private JPanel createRightPanel() {
		JPanel panel = new JPanel();
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		panel.setLayout(gbl);
		
		JPanel riflePanel = createRiflePanel();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 0, 0, 0); //top, left, bottom, right
		panel.add(riflePanel, gbc);
		
		createNotesBlock(panel, gbc, 1);

		return panel;
	}

	private void createNotesBlock(JPanel panel, GridBagConstraints gbc, int rowNumber) {
		textArea = new JTextArea();
		gbc.gridx = 0;
		gbc.gridy = rowNumber;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(0, 0, 0, 0); //top, left, bottom, right
		panel.add(textArea, gbc);
		
		textArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Other notes"));
	}

	private JPanel createRiflePanel() {
		JPanel panel = new JPanel();

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		panel.setLayout(gbl);
		
		createRifleDataBlock(panel, gbc, 0);
		createCaliberDataBlock(panel, gbc, 1);
		createAmmunitionDataBlock(panel, gbc, 2);
		createBatchDataBlock(panel, gbc, 3);
		createVelocityDataBlock(panel, gbc, 4);
		createScopeLabel(panel, gbc, 5);
		createElevationDataBlock(panel, gbc, 6);
		createWindageDataBlock(panel, gbc, 7);

		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Rifle data"));

		return panel;
	}

	private void createWindageDataBlock(JPanel panel, GridBagConstraints gbc, int rowNumber) {
		JLabel elevationLabel = new JLabel("Windage");
		gbc.gridx = 0;
		gbc.gridy = rowNumber;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 10, 5, 5); //top, left, bottom, right
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		panel.add(elevationLabel, gbc);

		windageSpin = new JSpinner(new SpinnerNumberModel(0, 0, 320, 1));
		gbc.gridx = 1;
		gbc.gridy = rowNumber;
		gbc.insets = new Insets(0, 0, 5, 5); //top, left, bottom, right
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(windageSpin, gbc);
		
		JLabel elevationUnitLabel = new JLabel("click");
		gbc.gridx = 2;
		gbc.gridy = rowNumber;
		gbc.insets = new Insets(0, 0, 5, 5); //top, left, bottom, right
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(elevationUnitLabel, gbc);
		
		windageDirectionCombo = new JComboBox<TEWindage>(TEWindage.values());
		gbc.gridx = 3;
		gbc.gridy = rowNumber;
		gbc.gridheight = 1;
		gbc.insets = new Insets(0, 5, 5, 10); //top, left, bottom, right
		panel.add(windageDirectionCombo, gbc);
		
		
		
	}

	private void createElevationDataBlock(JPanel panel, GridBagConstraints gbc, int rowNumber) {
		JLabel elevationLabel = new JLabel("Elevation");
		gbc.gridx = 0;
		gbc.gridy = rowNumber;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 10, 5, 5); //top, left, bottom, right
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		panel.add(elevationLabel, gbc);

		elevationSpin = new JSpinner(new SpinnerNumberModel(0, -5, 320, 1));
		gbc.gridx = 1;
		gbc.gridy = rowNumber;
		gbc.insets = new Insets(0, 0, 5, 5); //top, left, bottom, right
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(elevationSpin, gbc);
		
		JLabel elevationUnitLabel = new JLabel("click");
		gbc.gridx = 2;
		gbc.gridy = rowNumber;
		gbc.insets = new Insets(0, 0, 5, 10); //top, left, bottom, right
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(elevationUnitLabel, gbc);
	}

	private void createVelocityDataBlock(JPanel panel, GridBagConstraints gbc, int rowNumber) {
		JLabel initVelocityLabel = new JLabel("<html>V<sub>0</sub></html>");
		gbc.gridx = 0;
		gbc.gridy = rowNumber;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 10, 5, 5); //top, left, bottom, right
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		panel.add(initVelocityLabel, gbc);

		initVelocityField = new JTextField(LENGTH);
		gbc.gridx = 1;
		gbc.gridy = rowNumber;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0, 0, 5, 5); //top, left, bottom, right
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(initVelocityField, gbc);

		JLabel unitVelocityLabel = new JLabel("m/s");
		gbc.gridx = 3;
		gbc.gridy = rowNumber;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 5, 10); //top, left, bottom, right
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(unitVelocityLabel, gbc);
	}

	private void createScopeLabel(JPanel panel, GridBagConstraints gbc, int rowNumber) {
		JLabel scopeLabel = new JLabel("Scope settings");
		gbc.gridx = 0;
		gbc.gridy = rowNumber;
		gbc.gridwidth = 4;
		gbc.insets = new Insets(10, 10, 5, 10); //top, left, bottom, right
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(scopeLabel, gbc);
	}

	private void createBatchDataBlock(JPanel panel, GridBagConstraints gbc, int rowNumber) {
		JLabel batchLabel = new JLabel("Batch");
		gbc.gridx = 0;
		gbc.gridy = rowNumber;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 10, 5, 5); //top, left, bottom, right
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		panel.add(batchLabel, gbc);
		
		batchField = new JTextField(LENGTH);
		gbc.gridx = 1;
		gbc.gridy = rowNumber;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0, 0, 5, 5); //top, left, bottom, right
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(batchField, gbc);
	}

	private void createAmmunitionDataBlock(JPanel panel, GridBagConstraints gbc, int rowNumber) {
		JLabel amunitionLabel = new JLabel("Ammunition");
		gbc.gridx = 0;
		gbc.gridy = rowNumber;
		gbc.insets = new Insets(0, 10, 5, 5); //top, left, bottom, right
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		panel.add(amunitionLabel, gbc);
		
		ammunitionField = new JTextField(LENGTH);
		gbc.gridx = 1;
		gbc.gridy = rowNumber;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0, 0, 5, 5); //top, left, bottom, right
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(ammunitionField, gbc);
	}

	private void createCaliberDataBlock(JPanel panel, GridBagConstraints gbc, int rowNumber) {
		JLabel caliberLabel = new JLabel("Caliber");
		gbc.gridx = 0;
		gbc.gridy = rowNumber;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 10, 5, 5); //top, left, bottom, right
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		panel.add(caliberLabel, gbc);
		
		caliberField = new JTextField(LENGTH);
		gbc.gridx = 1;
		gbc.gridy = rowNumber;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0, 0, 5, 5); //top, left, bottom, right
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(caliberField, gbc);
		
		JLabel caliberUnitLabel = new JLabel("mm");
		gbc.gridx = 3;
		gbc.gridy = rowNumber;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 5, 10); //top, left, bottom, right
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(caliberUnitLabel, gbc);
	}
	
	private void createRifleDataBlock(JPanel whereTo, GridBagConstraints gbc, int rowNumber) {
		JLabel rifleLabel = new JLabel("<html>Rifle<br>assembly</html>");
		gbc.gridx = 0;
		gbc.gridy = rowNumber;
		gbc.insets = new Insets(0, 10, 5, 5); //top, left, bottom, right
		gbc.anchor = GridBagConstraints.EAST;
		whereTo.add(rifleLabel, gbc);
		
		rifleField = new JTextField(LENGTH);
		gbc.gridx = 1;
		gbc.gridy = rowNumber;
		gbc.gridwidth = 3;
		gbc.insets = new Insets(0, 0, 5, 5); //top, left, bottom, right
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		whereTo.add(rifleField, gbc);

	}
	
	private void createWindDataBlock(JPanel panel, GridBagConstraints gbc, int rowNumber) {
		windInd = new TEWindIndicator(null, null);
		gbc.gridx = 0;
		gbc.gridy = rowNumber;
		gbc.gridwidth = 3;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(10, 10, 10, 10); //top, left, bottom, right
		panel.add(windInd, gbc);
	}

	private void createMirageDataBlock(JPanel panel, GridBagConstraints gbc, int rowNumber) {
		mirage = new TEMirageClassifier();
		gbc.gridx = 0;
		gbc.gridy = rowNumber;
		gbc.gridwidth = 3;
		panel.add(mirage, gbc);
	}

	private void createHumidityDataBlock(JPanel panel, GridBagConstraints gbc, int rowNumber) {
		JLabel humiLabel = new JLabel("Humidity");
		gbc.gridx = 0;
		gbc.gridy = rowNumber;
		gbc.insets = new Insets(0, 10, 5, 5); //top, left, bottom, right
		gbc.anchor = GridBagConstraints.EAST;
		panel.add(humiLabel, gbc);
		
		humiField = new JTextField(LENGTH);
		gbc.gridx = 1;
		gbc.gridy = rowNumber;
		gbc.insets = new Insets(0, 0, 5, 5); //top, left, bottom, right
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(humiField, gbc);
		
		JLabel humiUnitLabel = new JLabel("%");
		gbc.gridx = 2;
		gbc.gridy = rowNumber;
		gbc.insets = new Insets(0, 0, 5, 10); //top, left, bottom, right
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(humiUnitLabel, gbc);
	}

	private void createAirPressureDataBlock(JPanel panel, GridBagConstraints gbc, int rowNumber) {
		JLabel pressLabel = new JLabel("<html>Air<br>pressure</html>");
		gbc.gridx = 0;
		gbc.gridy = rowNumber;
		gbc.insets = new Insets(0, 10, 5, 5); //top, left, bottom, right
		gbc.anchor = GridBagConstraints.EAST;
		panel.add(pressLabel, gbc);
		
		pressField = new JTextField(LENGTH);
		gbc.gridx = 1;
		gbc.gridy = rowNumber;
		gbc.insets = new Insets(0, 0, 5, 5); //top, left, bottom, right
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(pressField, gbc);
		
		JLabel pressUnitLabel = new JLabel("hPa");
		gbc.gridx = 2;
		gbc.gridy = rowNumber;
		gbc.insets = new Insets(0, 0, 5, 10); //top, left, bottom, right
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(pressUnitLabel, gbc);
	}

	private void createAirTempDataBlock(JPanel panel, GridBagConstraints gbc, int rowNumber) {
		JLabel tempLabel = new JLabel("<html>Air<br>temperature</html>");
		gbc.gridx = 0;
		gbc.gridy = rowNumber;
		gbc.insets = new Insets(0, 10, 5, 5); //top, left, bottom, right
		gbc.anchor = GridBagConstraints.EAST;
		panel.add(tempLabel, gbc);
		
		tempField = new JTextField(LENGTH);
		gbc.gridx = 1;
		gbc.gridy = rowNumber;
		gbc.insets = new Insets(0, 0, 5, 5); //top, left, bottom, right
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(tempField, gbc);
		
		JLabel tempUnitLabel = new JLabel("˚C");
		gbc.gridx = 2;
		gbc.gridy = rowNumber;
		gbc.insets = new Insets(0, 0, 5, 10); //top, left, bottom, right
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(tempUnitLabel, gbc);
	}
	
	private void createLightIndDataBlock(JPanel panel, GridBagConstraints gbc, int rowNumber) {
		lightInd = new TELightDirIndicator(null);
		gbc.gridx = 1;
		gbc.gridy = rowNumber;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		//gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 0, 10, 5); //top, left, bottom, right
		panel.add(lightInd, gbc);
	}

	private void createAngleDataBlock(JPanel panel, GridBagConstraints gbc, int rowNumber) {
		JLabel angLabel = new JLabel("Angle");
		gbc.gridx = 0;
		gbc.gridy = rowNumber;
		gbc.insets = new Insets(0, 10, 10, 5); //top, left, bottom, right
		gbc.anchor = GridBagConstraints.EAST;
		panel.add(angLabel, gbc);
		
		angleSpin = new JSpinner(new SpinnerNumberModel(0, -90, 90, 1));
		gbc.gridx = 1;
		gbc.gridy = rowNumber;
		gbc.insets = new Insets(0, 0, 10, 5); //top, left, bottom, right
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(angleSpin, gbc);
		
		JLabel angUnitLabel = new JLabel("Deg");
		gbc.gridx = 2;
		gbc.gridy = rowNumber;
		gbc.insets = new Insets(0, 0, 10, 10); //top, left, bottom, right
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(angUnitLabel, gbc);
	}

	private void createTargetNmbrDataBlock(JPanel panel, GridBagConstraints gbc, int rowNumber) {
		JLabel targetNmbrLabel = new JLabel("<html>Target<br>number</html>");
		gbc.gridx = 0;
		gbc.gridy = rowNumber;
		gbc.insets = new Insets(0, 10, 5, 5); //top, left, bottom, right
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		panel.add(targetNmbrLabel, gbc);

		targetNmbrField = new JTextField(LENGTH);
		gbc.gridx = 1;
		gbc.gridy = rowNumber;
		gbc.insets = new Insets(0, 0, 5, 10); //top, left, bottom, right
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(targetNmbrField, gbc);
	}

	private void createDistanceDataBlock(JPanel panel, GridBagConstraints gbc, int rowNumber) {
		JLabel distanceLabel = new JLabel("Distance");
		gbc.gridx = 0;
		gbc.gridy = rowNumber;
		gbc.insets = new Insets(0, 10, 5, 5); //top, left, bottom, right
		gbc.anchor = GridBagConstraints.EAST;
		panel.add(distanceLabel, gbc);
		
		distanceField = new JTextField(LENGTH);
		gbc.gridx = 1;
		gbc.gridy = rowNumber;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 0, 5, 5); //top, left, bottom, right
		panel.add(distanceField, gbc);
		
		JLabel unitDistanceLabel = new JLabel("m");
		gbc.gridx = 2;
		gbc.gridy = rowNumber;
		gbc.insets = new Insets(0, 0, 5, 10); //top, left, bottom, right
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(unitDistanceLabel, gbc);
	}

	private void createPositionDataBlock(JPanel panel, GridBagConstraints gbc, int rowNumber) {
		JLabel positionLabel = new JLabel("Position");
		gbc.gridx = 0;
		gbc.gridy = rowNumber;
		gbc.insets = new Insets(0, 10, 5, 5); //top, left, bottom, right
		gbc.anchor = GridBagConstraints.EAST;
		panel.add(positionLabel, gbc);

		positionCombo = new JComboBox<TEPosition>(TEPosition.values());
		gbc.gridx = 1;
		gbc.gridy = rowNumber;
		gbc.insets = new Insets(0, 0, 5, 10); //top, left, bottom, right
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(positionCombo, gbc);
	}

	private void createTimeDataBlock(JPanel panel, GridBagConstraints gbc, int rowNumber) {
		JLabel timeLabel = new JLabel("Time");
		gbc.gridx = 0;
		gbc.gridy = rowNumber;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 10, 5, 5); //top, left, bottom, right
		gbc.anchor = GridBagConstraints.EAST;
		panel.add(timeLabel, gbc);

		TimePickerSettings timeSettings = new TimePickerSettings();
		timeSettings.setAllowEmptyTimes(false);
		timePicker = new TimePicker(timeSettings);		
		gbc.gridx = 1;
		gbc.gridy = rowNumber;
		gbc.insets = new Insets(0, 0, 5, 10); //top, left, bottom, right
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(timePicker, gbc);
	}

	private void createDateDataBlock(JPanel panel, GridBagConstraints gbc, int rowNumber) {
		JLabel dateLabel = new JLabel("Date");
		gbc.gridx = 0;
		gbc.gridy = rowNumber;
		gbc.insets = new Insets(0, 10, 5, 5); //top, left, bottom, right
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		panel.add(dateLabel, gbc);

		DatePickerSettings dateSettings = new DatePickerSettings();
		dateSettings.setAllowEmptyDates(false);
		datePicker = new DatePicker(dateSettings);		
		gbc.gridx = 1;
		gbc.gridy = rowNumber;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 5, 10); //top, left, bottom, right
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(datePicker, gbc);
	}

	private void createPlaceDataBlock(JPanel panel, GridBagConstraints gbc, int rowNumber) {
		JLabel placeLabel = new JLabel("Place");
		gbc.gridx = 0;
		gbc.gridy = rowNumber;
		gbc.insets = new Insets(10, 10, 5, 5); //top, left, bottom, right
		gbc.anchor = GridBagConstraints.EAST;
		panel.add(placeLabel, gbc);
		
		placeField = new JTextField(LENGTH);
		gbc.gridx = 1;
		gbc.gridy = rowNumber;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(10, 0, 5, 10); //top, left, bottom, right
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(placeField, gbc);
	}
	
	private void cancel() {
		if(globVars != null) {
			globVars.getSelmarkList().clear();
			globVars.setCreatingFile(false);
		}
		
		this.setVisible(false);
		
		if( gui!= null) {
			gui.canvasRepaint();
		}
	}
	
	private TEImpactSetRec prepareImpactSetRecord() {
		String place = placeField.getText();
		String date = datePicker.getDate().toString();
		String time = timePicker.getTime().toString();
		String position = positionCombo.getSelectedItem().toString();
		String distance = getNmbrString(distanceField, "Distance number input error");
		String targetNmbr = getNmbrString(targetNmbrField, "Target number input error");
		String angle = angleSpin.getValue().toString();
		String lightDir = lightInd.getValue();
		
		String temp = getNmbrString(tempField, "Temperature input error");
		String pressure = getNmbrString(pressField, "Air pressure input error");
		String humidity = getNmbrString(humiField, "Humidity input error");
		String mirageCategory = mirage.getMirageCategory();
		String mirageClassification = mirage.getMirageClassification();
		String windDirection = windInd.getDirection();
		String windSpeed = windInd.getSpeed();

		String rifleAssembly = rifleField.getText();
		String caliber = getNmbrString(caliberField, "Caliber input error"); 
		String amunition = ammunitionField.getText();
		String batch = batchField.getText();
		String initVelocity = getNmbrString(initVelocityField, "Velocity input error");
		String elevation = elevationSpin.getValue().toString();
		String windageVal = windageSpin.getValue().toString();
		String windageDir = windageDirectionCombo.getSelectedItem().toString();
		
		String notes = textArea.getText();
		
		if(distance == null || targetNmbr == null || temp == null || pressure == null || 
				humidity == null || caliber == null || initVelocity == null) {
			return null;
		}
			
		
		List<TESelectionMark> selmarks = globVars.getSelmarkList();
		List<TEImpactRec> impacts = new ArrayList<TEImpactRec>();

		for(TESelectionMark sm : selmarks) {
			impacts.add(new TEImpactRec(sm.getNumber(), sm.getUnitX(), sm.getUnitY()));
		}
		
		return new TEImpactSetRec(
				place,
				date,
				time,
				position,
				distance,
				targetNmbr,
				angle,
				lightDir,
				
				temp,
				pressure,
				humidity,
				mirageCategory,
				mirageClassification,
				windDirection,
				windSpeed,

				rifleAssembly,
				caliber, 
				amunition,
				batch,
				initVelocity,
				elevation,
				windageVal,
				windageDir,
				
				notes,
				impacts);	
	}

	private String getNmbrString(JTextField tf, String errorTitle) {
		String retString = tf.getText();
		if(retString.isEmpty()) {
			retString = NULL;
		} else {
			try {
				Double.parseDouble(retString);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), errorTitle, JOptionPane.ERROR_MESSAGE);
				return null;
			}		
		}
		return retString;
	}
	private void saveFile(boolean value) {
		final String SUFFIX = ".jis";
		String json = "";
		
		TEImpactSetRec is = prepareImpactSetRecord();
		if(is == null) {
			return;
		}	
			
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			json = ow.writeValueAsString(is);
		} catch (JsonProcessingException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Data processing error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		JFileChooser fileChooser = gui.getJsonImpactFileChooser();
		int result = fileChooser.showSaveDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();

			if(!file.getAbsolutePath().endsWith(SUFFIX)){ 
			    file = new File(fileChooser.getSelectedFile() + SUFFIX); //adds ".jis" suffix if omitted
			}
			writeFile(file, json);
			
			if(value) { //if automatic load is required
				try {
				gui.readImpactFile(file);
				} catch (IOException e) {
					System.err.print(e.getMessage());
				}
			}
		
			globVars.getSelmarkList().clear();
			globVars.setCreatingFile(false);
			this.setVisible(false);
			gui.canvasRepaint(); ///
		}
	}

	private void writeFile(File file, String s) {
		try( BufferedWriter  bw = new BufferedWriter(new FileWriter(file))){
			bw.write(s);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "File error", JOptionPane.ERROR_MESSAGE);			
		}
	}
	
	private ActionListener al = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			String bName = b.getName();

			switch (bName){
			case "ok":
				saveFile(cb.isSelected());
				break;
			case "cancel":
				cancel();
				break;
			}
		};
	};
	
}
