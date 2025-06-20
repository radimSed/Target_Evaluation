package com.targetevaluation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
public class TE2R100Menu extends JPopupMenu{
	    private JMenuItem anItem;
	    
	    private TEGui gui;
	    private TE2R100 circle;

	    public TE2R100Menu(TE2R100 circle, TEGui gui) {;
	    	this.gui = gui;;
	    	this.circle = circle;
	    	
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
			    		gui.show2R100Info(circle);
		    			break;
				}
		    }
		};
	    
	}
