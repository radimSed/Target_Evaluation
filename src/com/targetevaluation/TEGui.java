package com.targetevaluation;

import java.awt.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class TEGui extends JFrame{
	private JScrollPane jscrollPaneCanvas; 
	private JScrollPane jscrollPaneSettings;
	private TECanvas canvas;
	private JFileChooser imageChooser, impactFileChooser, jsonImpactFileChooser;
	private TESidePanel sidePanel;
	private TEMenu menu;
	
	private TEGlobalVariables globVars;
	
	private TEImpactSelectionDialog isd;
	private TEImpactSetsOperations iso;
	private TESettingsDialog settings;
	
	public TEGui(TEGlobalVariables globVars) {
		super();
		this.globVars = globVars;
		
		this.setTitle("Target Evaluation");
		this.setSize(1024, 768);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setIconImage(globVars.getIcon());
		
		this.addMenu();
		this.addCanvas();
		this.addSidePanel();
		this.setFileChoosers();

		this.setVisible(true);

		this.setDefaults();
		
		isd = new TEImpactSelectionDialog(this, globVars);
		iso = new TEImpactSetsOperations(this, globVars);
		settings = new TESettingsDialog(this, globVars);

		canvasRepaint();
	}
	
	private void setDefaults() {
		setDefaultAimPoint();
		setDefaultPixelsPerUnit();
		setDefaultScale();
	}
	
	protected void setDefaultScale() {
		sidePanel.setScaleindex(2);
	}
	
	protected void setDefaultPixelsPerUnit() {
		globVars.setPixelsPerUnit(96/25.4); //default for 96PPI which should be  default for monitors
		setPixelsPerUnitText();
	}
	
	protected void setPixelsPerUnit(double value) {
		globVars.setPixelsPerUnit(value); 
		setPixelsPerUnitText();
	}
	
	protected void setPixelsPerUnitText() {
		this.sidePanel.setPixelsPerUnitText();
	}

	private void setDefaultAimPoint() {
		globVars.getCrosshair().setCoords(canvas.getWidth()/2,canvas.getHeight()/2);
	}

	private void setFileChoosers() {
		imageChooser = new JFileChooser();
		FileNameExtensionFilter filter1 = new FileNameExtensionFilter("JPEG Images", "jpg");
		imageChooser.setAcceptAllFileFilterUsed(false);
		imageChooser.addChoosableFileFilter(filter1);
		
		impactFileChooser = new JFileChooser();
		FileNameExtensionFilter filter2 = new FileNameExtensionFilter("Impact files", "imp");
		impactFileChooser.setAcceptAllFileFilterUsed(false);
		impactFileChooser.addChoosableFileFilter(filter2);
		
		jsonImpactFileChooser = new JFileChooser();
		FileNameExtensionFilter filter3 =  new FileNameExtensionFilter("JSON impact set files", "jis");
		jsonImpactFileChooser.setAcceptAllFileFilterUsed(false);
		jsonImpactFileChooser.addChoosableFileFilter(filter3);
		
	}

	private void addSidePanel() {
		sidePanel = new TESidePanel(this, globVars);
		jscrollPaneSettings = new JScrollPane(sidePanel);
		this.add(this.jscrollPaneSettings, BorderLayout.EAST);

	}

	private void addCanvas() {
		canvas =  new TECanvas(this, globVars);
		jscrollPaneCanvas = new JScrollPane(canvas);
		this.add(this.jscrollPaneCanvas, BorderLayout.CENTER);
	}

	private void addMenu() {
		menu = new TEMenu(this, globVars);
		this.add(this.menu, BorderLayout.NORTH);
	}
	
	protected void importImage() {
		int result = imageChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = imageChooser.getSelectedFile();
			globVars.setBgImage(selectedFile.getAbsolutePath());
			canvasRepaint();
		}
	}
	
	protected void openFile() {
		int result = jsonImpactFileChooser.showOpenDialog(this);
		if(result != JFileChooser.APPROVE_OPTION) {
			return;
		}
		
		File selectedFile = jsonImpactFileChooser.getSelectedFile();
		try {
			readImpactFile(selectedFile);
		} catch (IOException e) {
			System.err.print(e.getMessage());
		}
		canvasRepaint();
	}
	
	protected void switchToCreateFileMode() {
		globVars.setCreatingFile(true);
		showIsd();
	}

	protected void showIsd() {
		isd.setVisible(true);
	}
	
	protected void showIso() {
		iso.setVisible(true);
	}
	
	protected void showSettings() {
		settings.setVisible(true);
	}

	protected void readImpactFile(File file) throws IOException {
		TEImpactSetRec isr = new ObjectMapper().readValue(file, TEImpactSetRec.class);

		int impacSetsMaxId = globVars.getImpacSetsMaxId();
		int colorIndex = (impacSetsMaxId - 1) % TEGlobalVariables.colors.length;
		TEImpactSet impactSet = new TEImpactSet(isr, impacSetsMaxId, file.getAbsolutePath(), TEGlobalVariables.colors[colorIndex], globVars);

		globVars.addImpactSet(impactSet);
		refreshSetsMenu();
		iso.fillList();
	}
	
	protected void refreshSetsMenu() {
		menu.updateSetsMenu();
	}
	
	protected void setXYTextInfo(double x, double y) {
		this.sidePanel.setXYInfo(x, y);
	}
	
	protected void canvasRepaint() {
		canvas.repaint();
	}
	
	protected void setStrayBullet(TEImpact si) {
		if(si.getStrayBullet()) {
			si.setStrayBullet(false);
		} else {
			si.setStrayBullet(true);
		}
		canvasRepaint();
	}
	
	protected void showImpactInfo(TEImpact si) {
		JOptionPane.showMessageDialog(this, si.getInfo(), "Info message", JOptionPane.INFORMATION_MESSAGE);
	}
	
	protected void showMpoiInfo(TEMpoiMark sm) {
		JOptionPane.showMessageDialog(this, sm.getInfo(), "Info message", JOptionPane.INFORMATION_MESSAGE);
	}
	
	protected void show2R100Info(TE2R100 circle) {
		JOptionPane.showMessageDialog(this, circle.getInfo(), "Info message", JOptionPane.INFORMATION_MESSAGE);
}
	
	protected JFileChooser getImpactFileChooser() {
		return impactFileChooser;
	}
	
	protected JFileChooser getJsonImpactFileChooser() {
		return jsonImpactFileChooser;
	}
}
