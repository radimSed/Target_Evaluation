package com.targetevaluation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
public class TECrossHairMenu extends JPopupMenu{
    private JMenuItem anItem;
    private TEGlobalVariables globVars;

    public TECrossHairMenu(TEGlobalVariables globVars) {
    	super();
    	this.globVars = globVars;
    	
        anItem = new JMenuItem("Set the Aim Point");
        add(anItem);
        anItem.addActionListener(actionListener);
    }
    
    ActionListener actionListener = new ActionListener() {
    	@Override
		public void actionPerformed(ActionEvent e) {
    		globVars.setSettingCrosshair(true);
		}
    };
    
}
