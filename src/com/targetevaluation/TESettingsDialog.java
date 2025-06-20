package com.targetevaluation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class TESettingsDialog extends JDialog{
	private TEGui gui;
	private TEGlobalVariables globVars;

	public TESettingsDialog(TEGui gui, TEGlobalVariables globVars) {
		super();
		this.gui = gui;
		this.globVars = globVars;

		this.setTitle("Global Settings");
		this.setSize(300, 250);
		this.setModal(false);
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setIconImage(globVars.getIcon());

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		this.setLayout(gbl);
		
		JTabbedPane jtb = new JTabbedPane();
		jtb.addTab("Canvas", new TESettingsCanvasPanel(this, globVars.getSettings()));
		jtb.addTab("Statistics", new TESettingsStat(this, globVars.getSettings()));
		jtb.addTab("Selection Cursor", new TESettingsSelCursor(this, globVars.getSettings()));
		gbc.insets = new Insets(5, 5, 0, 5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		this.add(jtb, gbc);
		
		JButton hideBut = new JButton("Hide"); 
		hideBut.setName("hide");
		hideBut.addActionListener(al);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		this.add(hideBut, gbc);
		
		JButton applyBut = new JButton("Save changes permanently"); 
		applyBut.setName("save");
		applyBut.addActionListener(al);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		this.add(applyBut, gbc);
	}
	
	
	ActionListener al = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			String bName = b.getName();

			switch (bName){
			case "hide":
				hideSettings();
				break;
			case "save":
				saveChanges();
				break;
			}
		};
	};
	
	private void hideSettings() {
		this.setVisible(false);
	}
	
	private void saveChanges() {
        try( FileOutputStream fileOut = new FileOutputStream("settings.ser") ) {
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(globVars.getSettings());
            out.close();
            JOptionPane.showMessageDialog(this, "Serialized data is saved in settings.ser", 
            		"Info message", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException i) {
            JOptionPane.showMessageDialog(this, "Serialized data is NOT saved", 
            		"Info message", JOptionPane.ERROR_MESSAGE);
        }
	}
	
	protected void refresh() {
		for(TEImpactSet is : globVars.getImpactSets()) {
			is.recalculate2R100();
		}
		gui.canvasRepaint();
	}
}
