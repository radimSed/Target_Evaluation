package com.targetevaluation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class TEMenu extends JMenuBar{
	
	private TEGui gui;
	private TEGlobalVariables globVars;
	private JMenu jmenuSets; 

	public TEMenu(TEGui gui, TEGlobalVariables globVars) {
		super();
		this.gui = gui;
		this.globVars = globVars;
		
		JMenu jmenuFile = new JMenu("File");
		this.add(jmenuFile);

		this.jmenuSets = new JMenu("Sets");
		this.add(jmenuSets);
		
		JMenu jmenuProgram = new JMenu("Program");
		this.add(jmenuProgram);
		
		JMenuItem jmenuItemImportImage = new JMenuItem("Import image");
		jmenuItemImportImage.setName("importImage");
		jmenuFile.add(jmenuItemImportImage);

		JMenuItem jmenuItemCreateFile = new JMenuItem("Create file");
		jmenuItemCreateFile.setName("createFile");
		jmenuFile.add(jmenuItemCreateFile);

		JMenuItem jmenuItemOpenFile = new JMenuItem("Open file");
		jmenuItemOpenFile.setName("openFile");
		jmenuFile.add(jmenuItemOpenFile);

		jmenuItemImportImage.addActionListener(actionListener);
		jmenuItemCreateFile.addActionListener(actionListener);
		jmenuItemOpenFile.addActionListener(actionListener);
		
		jmenuSets.addSeparator();
		
		JMenuItem jmenuItemSetsOperation = new JMenuItem("Sets operations");
		jmenuItemSetsOperation.setName("setsOperation");
		jmenuSets.add(jmenuItemSetsOperation);
		
		jmenuItemSetsOperation.addActionListener(actionListener);

		JMenuItem jmenuSettings = new JMenuItem("Settings");
		jmenuSettings.setName("settings");
		jmenuProgram.add(jmenuSettings);

		jmenuSettings.addActionListener(actionListener);

	}
	
	ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem mi = (JMenuItem) e.getSource();
			String miName = mi.getName();

			switch (miName){
			case "importImage":
				gui.importImage();
				break;
			case "openFile":
				gui.openFile();
				break;
			case "createFile":
				gui.switchToCreateFileMode();
				break;
			case "setsOperation":
				gui.showIso();
				break;
			case "settings":
				gui.showSettings();
				break;
		}
	}};
	
	public void updateSetsMenu() {
		jmenuSets.removeAll();

		List<TEImpactSet> impactSets = globVars.getImpactSets();
		
		for(TEImpactSet is : impactSets) {
			int id = is.getId();
			String desc = id + ". " + is.getName();
			JMenuItem item = new JCheckBoxMenuItem(desc);
			item.setSelected(is.getVisibility());
			item.setName(String.valueOf(String.valueOf(id)));
			item.addActionListener(actionListenerSets);
			jmenuSets.add(item);
		}
		jmenuSets.addSeparator();
		JMenuItem jmenuItemSetsOperation = new JMenuItem("Sets operations");
		jmenuItemSetsOperation.setName("setsOperation");
		jmenuSets.add(jmenuItemSetsOperation);
		jmenuItemSetsOperation.addActionListener(actionListener);

	}
	
	ActionListener actionListenerSets = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem mi = (JMenuItem) e.getSource();
			String miName = mi.getName();
			TEImpactSet is = globVars.getImpactSetById(Integer.parseInt(miName));
			is.setVisibility(mi.isSelected());
			gui.canvasRepaint();
		}
	};
	
}
