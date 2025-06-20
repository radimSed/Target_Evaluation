package com.targetevaluation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;

@SuppressWarnings("serial")
public class TEImpactSelectionDialog extends JDialog{
	private TEGui gui;
	private DefaultListModel<String> impactList; 
	private JList<String> list;
	private TEGlobalVariables globVars;

	public TEImpactSelectionDialog(TEGui gui, TEGlobalVariables globVars) {
		super();
		this.gui = gui;
		this.globVars = globVars;
		
		this.setTitle("Creating impact file");
		this.setSize(400, 300);
		this.setModal(false);
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setIconImage(globVars.getIcon());

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		this.setLayout(gbl);
		
		JLabel label = new JLabel("Select impacts, then press Ok");
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.ipadx = 5;
		gbc.ipady = 5;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		this.add(label, gbc);
		
		impactList = new DefaultListModel<>();;
		
		list = new JList<String>(impactList);
		JScrollPane jscrollPaneList = new JScrollPane(list);
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.ipadx = 5;
		gbc.ipady = 5;
		gbc.weightx = 0;
		gbc.weighty = 0;
		this.add(jscrollPaneList, gbc);
		jscrollPaneList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				"Number          X          Y"));

		JButton remBut = new JButton("<html>Remove<br>selected<br>impacts</html>");
		remBut.setName("remove");
		remBut.addActionListener(al);
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.ipadx = 5;
		gbc.ipady = 5;
		gbc.weightx = 0;
		gbc.weighty = 0;
		this.add(remBut, gbc);
		
		JButton okBut = new JButton("Ok");
		okBut.addActionListener(al);
		okBut.setSize(10, 5);
		okBut.setName("ok");
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		this.add(okBut, gbc);
		
		JButton cancelBut = new JButton("Cancel");
		cancelBut.addActionListener(al);
		cancelBut.setSize(10, 5);
		cancelBut.setName("cancel");
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		this.add(cancelBut, gbc);
		
		globVars.addPropertyChangeListener(listener);

	}
	
	ActionListener al = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			String bName = b.getName();

			switch (bName){
			case "ok":
				showImpactPropertyPanel();
				break;
			case "cancel":
				cancel();
				break;
			case "remove":
				remove();
				break;

			}
		};
	};
	
	PropertyChangeListener listener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			impactList.clear();
			impactList.addAll(fillList());
		}
	};

	private void showImpactPropertyPanel() {
		this.setVisible(false);
		TEImpactSetPropertyPanel icp = new TEImpactSetPropertyPanel(gui, globVars);
		icp.setVisible(true);
	}
	
	private void cancel() {
		globVars.getSelmarkList().clear();
		globVars.setCreatingFile(false);
		this.setVisible(false);
		gui.canvasRepaint();
	}
	
	private List<String> fillList() {
		List<TESelectionMark> smList = globVars.getSelmarkList(); 
		List<String> selection = new ArrayList<>();

		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);

		for(TESelectionMark sm : smList) {
			String s = "     " + sm.getNumber() + "          " + nf.format(sm.getUnitX()) 
			+ "          " + nf.format(sm.getUnitY());
			selection.add(s);
		}
		return selection;
	}
	
	private void remove() {
		List<TESelectionMark> selmarkList = globVars.getSelmarkList();
		//remove selected impacts
		int[] indices = list.getSelectedIndices();
		for(int i = indices.length - 1 ; i >= 0; i--) {
			selmarkList.remove(indices[i]);
		}
		
		//renumber selectionMarks
		for(int i = 0; i < selmarkList.size(); i++) {
			selmarkList.get(i).setNumber(i+1);
		}
		
		impactList.clear();
		impactList.addAll(fillList());
		gui.canvasRepaint();
	}


}
