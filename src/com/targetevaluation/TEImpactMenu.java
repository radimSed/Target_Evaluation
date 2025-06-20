package com.targetevaluation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
public class TEImpactMenu extends JPopupMenu{
    private JMenuItem anItem;
    
    private TEGui gui;
    private TEImpact si;

    public TEImpactMenu(TEImpact si, TEGui gui) {
    	super();
    	this.gui = gui;
    	this.si = si;
    	
        anItem = new JCheckBoxMenuItem("Is stray bullet");
        anItem.setName("isStrayBullet");
        anItem.setSelected(si.getStrayBullet());
        add(anItem);
        anItem.addActionListener(actionListener);
        
        anItem = new JMenuItem("Info");
        anItem.setName("info");
        add(anItem);
        anItem.addActionListener(actionListener);
    }
    
    ActionListener actionListener = new ActionListener() {
    	@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem mi = (JMenuItem) e.getSource();
    		
			switch(mi.getName()) {
				case "isStrayBullet":
		    		gui.setStrayBullet(si);
	    			break;
				case "info":
		    		gui.showImpactInfo(si);
	    			break;
			}
	    }
	};
}
