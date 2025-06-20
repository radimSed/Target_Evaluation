package com.targetevaluation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class TEImpactSetsOperations extends JDialog{
	private TEImpactSetsPanel panel;
	
	public TEImpactSetsOperations(TEGui gui, TEGlobalVariables globVars) {
		super();
		
		this.setTitle("Sets operation panel");
		this.setSize(675, 375);
		this.setModal(false);
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setIconImage(globVars.getIcon());

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		this.setLayout(gbl);

		panel = new TEImpactSetsPanel(gui, globVars);
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		this.add(panel, gbc);

		
		JButton hideBut = new JButton("Hide panel");
		hideBut.addActionListener(al);
		hideBut.setSize(10, 5);
		hideBut.setName("hide");
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.NONE;
		this.add(hideBut, gbc);

	}
	
	ActionListener al = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			hidePanel();
		}
	};
	
	protected void fillList() {
		panel.fillList();
	}
	
	private void hidePanel() {
		this.setVisible(false);
	}
	
	
}
