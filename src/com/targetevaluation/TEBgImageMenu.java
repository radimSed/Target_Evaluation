package com.targetevaluation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
public class TEBgImageMenu extends JPopupMenu{
	private JMenuItem anItem;
	private TEGui gui;
	private TEGlobalVariables globVars;

	public TEBgImageMenu(TEGui gui, TEGlobalVariables globVars) {
		super();
		this.gui = gui;
		this.globVars = globVars;
		
	    anItem = new JMenuItem("Set the Dimension");
	    anItem.setName("setPixPerUnit");
	    add(anItem);
	    anItem.addActionListener(actionListener);
	     
	    anItem = new JMenuItem("Reset to default pixels/unit value"); 
	    anItem.setName("resetPixPerUnit");
	    add(anItem);
	    anItem.addActionListener(actionListener);
	}
	    
	ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem mi = (JMenuItem) e.getSource();
	    		
			switch(mi.getName()) {
				case "setPixPerUnit":
					globVars.setSettingUnitsStatus(true);
	    			break;
	    		case "resetPixPerUnit":
	    			gui.setDefaultPixelsPerUnit();;
	    			break;
			}
	    }
	};
}
