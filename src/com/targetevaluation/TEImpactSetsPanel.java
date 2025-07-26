package com.targetevaluation;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.io.*;

@SuppressWarnings("serial")
public class TEImpactSetsPanel extends JPanel{
	private DefaultListModel<String> setsList; 
	private JList<String> list;
	private JComboBox<String> methodCombo;
	
	private TEGlobalVariables globVars;
	private TEGui gui;

	private final String DIR_EXTENSION = "_SUMMARY";
	private final String NOTAPPLICABLE = "Not Applicable";

	public TEImpactSetsPanel(TEGui gui, TEGlobalVariables globVars) {
		super();
		this.gui = gui;
		this.globVars = globVars;
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		this.setLayout(gbl);
		
		setsList = new DefaultListModel<>();
		
		list = new JList<String>(setsList);
		JScrollPane jscrollPaneList = new JScrollPane(list);
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 4;
		gbc.ipadx = 5;
		gbc.ipady = 5;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.BOTH;
		this.add(jscrollPaneList, gbc);
		
		JButton remBut = new JButton("Remove selected sets");
		remBut.setName("remove");
		remBut.addActionListener(al);
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.ipadx = 5;
		gbc.ipady = 5;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.NONE;
		this.add(remBut, gbc);

		JButton showInfoBut = new JButton("Generate summary");
		showInfoBut.setName("summary");
		showInfoBut.addActionListener(al);
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.ipadx = 5;
		gbc.ipady = 5;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		this.add(showInfoBut, gbc);

		JButton showPropertiesBut = new JButton("Show properties");
		showPropertiesBut.setName("showproperties");
		showPropertiesBut.addActionListener(al);
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.ipadx = 5;
		gbc.ipady = 5;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		this.add(showPropertiesBut, gbc);

		JButton setColorBut = new JButton("Select color");
		setColorBut.setName("setcolor");
		setColorBut.addActionListener(al);
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 3;
		gbc.gridy = 1;
		gbc.ipadx = 5;
		gbc.ipady = 5;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		this.add(setColorBut, gbc);

		JLabel methodLabel =  new JLabel("<html>Select method for<br>stray bullet determination</html>");
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.EAST;
		this.add(methodLabel, gbc);
		
		methodCombo = new JComboBox<>();
		methodCombo.addItem("Chauvenet");
		methodCombo.addItem("Grubbs");
		methodCombo.addItem("Graf-Henning");
		methodCombo.addItem("Student");
		methodCombo.setSelectedIndex(1);
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(methodCombo, gbc);
		
		JButton determineBut = new JButton("Determine stray bullets");
		determineBut.addActionListener(al);
		determineBut.setSize(10, 5);
		determineBut.setName("determine");
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 2;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		this.add(determineBut, gbc);
		
		this.setBorder(BorderFactory.createEtchedBorder());
	}
	
	ActionListener al = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			String bName = b.getName();

			switch (bName){
				case "determine":
					determine();
					break;
				case "remove":
					removeSets();
					break;
				case "summary":
					generateReports();
					break;
				case "setcolor":
					setColor();
					break;
				case "showproperties":
					showProperties();
					break;
			}
		};
	};
	
	private void showProperties() {
		List<TEImpactSet> sets = globVars.getImpactSets();
		int[] indices = list.getSelectedIndices();
		for(int i = indices.length - 1 ; i >= 0; i--) {
			new TEImpactSetPropertyPanel(sets.get(indices[i])).setVisible(true);
		}
	}
	
	private void generateReports() {
		List<TEImpactSet> sets = globVars.getImpactSets();
		int[] indices = list.getSelectedIndices();
		
		//read the resources only once for all
		byte[] logo = readResource("logo.svg");
		byte[] css = readResource("index.css");
		String page = new String(readResource("index.html"));
		
		for(int i = indices.length - 1 ; i >= 0; i--) {
			createReport(sets.get(indices[i]), logo, css, page);
		}
	}
	
	private byte[] readResource(String filename) {
		byte[] data = null;
		try {
			data = getClass().getResourceAsStream("/resources/" + filename).readAllBytes();
		} catch (IOException | NullPointerException e) {
			System.err.println(e.getMessage());
		}
		return data;
	}
	
	private void createReport(TEImpactSet set, byte[] logo, byte[] css, String page) {
		File dir = createDir(set.getName());
		byte[] finalPage = modifyPage(set, page).getBytes();
		generatePic(set, dir);
		copyFile(dir, "logo.svg", logo);
		copyFile(dir, "index.css", css);
		copyFile(dir, "index.html", finalPage);
	}

	private void copyFile(File destDir, String filename, byte[] stream) {
		if(stream != null) {
			File copiedFile = new File(destDir + System.getProperty("file.separator") + filename);
			try(FileOutputStream fw = new FileOutputStream(copiedFile)){
				fw.write(stream);
			} catch (IOException e) {
				System.err.print(e.getMessage());
			}
		}
	}
	
	private String modifyPage(TEImpactSet set, String page) {
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);

		StringBuilder sb = new StringBuilder(page);
		int[] startEnd = new int[2];
		
		startEnd = getStartEnd(sb, "+filename+");
		sb.replace(startEnd[0], startEnd[1], set.getName());
		
		startEnd = getStartEnd(sb, "+place+");
		sb.replace(startEnd[0], startEnd[1], set.getPlace());
		
		startEnd = getStartEnd(sb, "+date+");
		sb.replace(startEnd[0], startEnd[1], set.getDate());
		
		startEnd = getStartEnd(sb, "+time+");
		sb.replace(startEnd[0], startEnd[1], set.getTime());
		
		startEnd = getStartEnd(sb, "+position+");
		sb.replace(startEnd[0], startEnd[1], set.getPosition());
		
		startEnd = getStartEnd(sb, "+distance+");
		Double distance = set.getDistance();
		if(distance != null) {
		sb.replace(startEnd[0], startEnd[1], String.valueOf(nf.format(distance)));
		} else {
			sb.replace(startEnd[0], startEnd[1], "null");
		}
		
		startEnd = getStartEnd(sb, "+targetNmbr+");
		sb.replace(startEnd[0], startEnd[1], set.getTargetNmbr());
		
		startEnd = getStartEnd(sb, "+angle+");
		sb.replace(startEnd[0], startEnd[1], String.valueOf(set.getAngle()));

		startEnd = getStartEnd(sb, "+temp+");
		Double temperature = set.getTemperature();
		if( temperature != null) {
			sb.replace(startEnd[0], startEnd[1], String.valueOf(nf.format(temperature)));
		} else {
			sb.replace(startEnd[0], startEnd[1], "null");
		}

		startEnd = getStartEnd(sb, "+press+");
		Double pressure = set.getPressure();
		if(pressure != null) {
			sb.replace(startEnd[0], startEnd[1], String.valueOf(nf.format(pressure)));
		} else {
			sb.replace(startEnd[0], startEnd[1], "null");
		}
		
		startEnd = getStartEnd(sb, "+humi+");
		Double humidity = set.getHumidity();
		if( humidity != null) {
			sb.replace(startEnd[0], startEnd[1], String.valueOf(nf.format(humidity)));
		} else {
			sb.replace(startEnd[0], startEnd[1], "null");
		}

		startEnd = getStartEnd(sb, "+category+");
		sb.replace(startEnd[0], startEnd[1], set.getMirageCategory());

		startEnd = getStartEnd(sb, "+classification+");
		sb.replace(startEnd[0], startEnd[1], set.getMirageClassification());

		startEnd = getStartEnd(sb, "+escrosswind+");
		sb.replace(startEnd[0], startEnd[1], set.getMirageEstimatedCrosswind());
		
		startEnd = getStartEnd(sb, "+windspeed+");
		Double windSpeed = set.getWindSpeed();
		if( windSpeed != null) {
		sb.replace(startEnd[0], startEnd[1], String.valueOf(nf.format(windSpeed)));
		} else {
			sb.replace(startEnd[0], startEnd[1], "null");
		}
		
		String crossWind = set.getCalculatedCrosswind();
		startEnd = getStartEnd(sb, "+crosswind+");
		if(crossWind.equals(NOTAPPLICABLE)){
			sb.replace(startEnd[0], startEnd[1],crossWind);
		} else {
			sb.replace(startEnd[0], startEnd[1], String.valueOf(nf.format(crossWind)));
		}
		
		startEnd = getStartEnd(sb, "+assembly+");
		sb.replace(startEnd[0], startEnd[1], set.getRifleAssembly());

		startEnd = getStartEnd(sb, "+cal+");
		sb.replace(startEnd[0], startEnd[1], String.valueOf(set.getCaliber()));

		startEnd = getStartEnd(sb, "+ammo+");
		sb.replace(startEnd[0], startEnd[1], set.getAmmunition());

		startEnd = getStartEnd(sb, "+batch+");
		sb.replace(startEnd[0], startEnd[1], set.getBatch());

		startEnd = getStartEnd(sb, "+v0+");
		Double initialV = set.getInitialVelocity();
		if( initialV != null) {
			sb.replace(startEnd[0], startEnd[1], String.valueOf(nf.format(initialV)));
		} else {
			sb.replace(startEnd[0], startEnd[1], "null");
		}

		startEnd = getStartEnd(sb, "+elev+");
		sb.replace(startEnd[0], startEnd[1], String.valueOf(set.getElevation()));

		startEnd = getStartEnd(sb, "+windageval+");
		sb.replace(startEnd[0], startEnd[1], String.valueOf(set.getWindageValue()));
		startEnd = getStartEnd(sb, "+windagedir+");
		sb.replace(startEnd[0], startEnd[1], set.getWindageDirection());

		TE2R100 circle2r100 = set.get2R100();
		
		startEnd = getStartEnd(sb, "+2r100x+");
		Double x2r100 = circle2r100.getX();
		if( x2r100 != null) {
			sb.replace(startEnd[0], startEnd[1], String.valueOf(nf.format(x2r100)));
		} else {
			sb.replace(startEnd[0], startEnd[1], "null");
		}

		startEnd = getStartEnd(sb, "+2r100y+");
		Double y2r100 = circle2r100.getY();
		if( y2r100 != null) {
		sb.replace(startEnd[0], startEnd[1], String.valueOf(nf.format(y2r100)));
		} else {
			sb.replace(startEnd[0], startEnd[1], "null");
		}

		startEnd = getStartEnd(sb, "+2r100d+");
		Double d2r100 = circle2r100.getD();
		if( d2r100 != null) {
		sb.replace(startEnd[0], startEnd[1], String.valueOf(nf.format(d2r100)));
		} else {
			sb.replace(startEnd[0], startEnd[1], "null");
		}

		startEnd = getStartEnd(sb, "+moa+");
		sb.replace(startEnd[0], startEnd[1], String.valueOf(nf.format(set.getMOA())));

		double[] mpoiCoords = set.getMpoiMark().getMpoiCoords();
		startEnd = getStartEnd(sb, "+mpoix+");
		sb.replace(startEnd[0], startEnd[1], String.valueOf(nf.format(mpoiCoords[0])));

		startEnd = getStartEnd(sb, "+mpoiy+");
		sb.replace(startEnd[0], startEnd[1], String.valueOf(nf.format(mpoiCoords[1])));

		startEnd = getStartEnd(sb, "+notes+");
		sb.replace(startEnd[0], startEnd[1], set.getNotes());

		return sb.toString();
	}
	
	private int[] getStartEnd(StringBuilder str, String substr) {
		int[] result = new int[2];
		result[0] = str.indexOf(substr);
		result[1] = result[0] + substr.length();
		return result;
	}
	
	private void generatePic(TEImpactSet set, File dir) {
		TEPicGenerator pg = new TEPicGenerator();

		//create impacts image
		BufferedImage bi = pg.createImpactPic(set);
	    try { 
	        ImageIO.write(bi, "png", new File(dir.getAbsolutePath() + System.getProperty("file.separator") + "impactsPic.png")); 
	    } catch (IOException e) {
	        e.printStackTrace();
	    }	    
		
	    //create light direction image
		BufferedImage bld = pg.createLightDirectionPic(set);
	    try { 
	        ImageIO.write(bld, "png", new File(dir.getAbsolutePath() + System.getProperty("file.separator") + "lightDir.png")); 
	    } catch (IOException e) {
	        e.printStackTrace();
	    }	    
		
	    //create wind direction image
		BufferedImage bwd = pg.createWindDirectionPic(set);
	    try { 
	        ImageIO.write(bwd, "png", new File(dir.getAbsolutePath() + System.getProperty("file.separator") + "windDir.png")); 
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	
	
	private File createDir(String filename) {
		 String dirName = (FilenameUtils.removeExtension(filename)) + DIR_EXTENSION;
		 File dir = new File(dirName);
		 if(dir.exists()) { //if the directory exists from previous run, delete it.
			 try{
				 FileUtils.deleteDirectory(dir);
			 } catch (IOException e){
				 JOptionPane.showMessageDialog(gui, "Directory " + dirName + "already exists and cannot be removed.", "Unable to delete", JOptionPane.INFORMATION_MESSAGE);
			 }
		 }
		 dir.mkdir(); //create directory for summary report
		 
		 return dir;
	}

	private void setColor() {
		Color color = JColorChooser.showDialog(null, "Choose a color", Color.RED);

		if( color != null) {
			List<TEImpactSet> sets = globVars.getImpactSets();

			int[] indices = list.getSelectedIndices();
			for(int i = indices.length - 1 ; i >= 0; i--) {
				sets.get(indices[i]).setColor(color);
			}
		}
		gui.canvasRepaint();
	}

	private void removeSets() {
		List<TEImpactSet> sets = globVars.getImpactSets();
		//remove selected impacts
		int[] indices = list.getSelectedIndices();
		for(int i = indices.length - 1 ; i >= 0; i--) {
			sets.remove(indices[i]);
		}
		
		setsList.clear();
		fillList();
		gui.refreshSetsMenu();
		gui.canvasRepaint();
	}

	private void determine() {
		int index = methodCombo.getSelectedIndex();
		TEStrayBulletCalculation sbCalc = null;
		
		switch (index) {
			case 0: sbCalc= new TEChauvenetGrubbs(globVars.getStatCoeffs().getChauvenet(), globVars.getSettings());
			break;
			case 1: sbCalc = new TEChauvenetGrubbs(globVars.getStatCoeffs().getGrubbs(), globVars.getSettings());
			break;
			case 2: sbCalc = new TEGrafHenningStudent(globVars.getStatCoeffs().getGrafHenning(), globVars.getSettings());
			break;
			case 3: sbCalc = new TEGrafHenningStudent(globVars.getStatCoeffs().getStudent(), globVars.getSettings());
			break;
		}
		
		if(sbCalc == null) {
			JOptionPane.showMessageDialog(this, "Method of calculation stray impacts is not assigned.", "File error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		int[] indicesOfSets = list.getSelectedIndices();
		List<TEImpactSet> impactSets = globVars.getImpactSets();
		

		for(int i = 0; i < indicesOfSets.length; i++) {
			List<TEImpact> impacts = impactSets.get(indicesOfSets[i]).getImpacts();
			for(TEImpact im : impacts) {
				im.setStrayBullet(false); //make all impacts valid which is necessary for correct stray bullets calculation
			}

			List<TEImpact> strayImpacts = sbCalc.calculateStrayBullet(impacts);
			for(TEImpact is : strayImpacts) {
				is.setStrayBullet(true);
			}
		}
		gui.canvasRepaint();
	}
	
	protected void fillList() {
		List<TEImpactSet> setList = globVars.getImpactSets(); 
		List<String> selection = new ArrayList<>();

		for(TEImpactSet is : setList) {
			String s = is.getId() + " " + is.getName();
			selection.add(s);
		}
		setsList.clear();
		setsList.addAll(selection);
	}

}
