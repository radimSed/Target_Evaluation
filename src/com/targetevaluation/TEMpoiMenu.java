package com.targetevaluation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
public class TEMpoiMenu extends JPopupMenu{
	    private JMenuItem anItem;
	    
	    private TEGui gui;
	    private TEMpoiMark sm;

	    public TEMpoiMenu(TEMpoiMark sm, TEGui gui){
	    	super();
	    	this.gui = gui;
	    	this.sm = sm;
	    	
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
					case "info":
			    		gui.showMpoiInfo(sm);
		    			break;
				}
		    }
		};
	    
	}
