package com.targetevaluation;

import java.io.BufferedInputStream;
import java.util.Scanner;
import java.util.TreeMap;

public class TEStatCoeffs {
	private TreeMap<Integer, Double> chauvenet, grubbs, student, grafHenning;
	
	public TEStatCoeffs(String pathToResources) {
		this.chauvenet = readCoeffs(pathToResources + "/chauvenet");
		this.grubbs = readCoeffs(pathToResources + "/grubbs");
		this.student = readCoeffs(pathToResources + "/student");
		this.grafHenning = readCoeffs(pathToResources + "/graf_henning");
	}
	
	private TreeMap<Integer, Double> readCoeffs(String resource) {
		TreeMap<Integer, Double> returnMap = new TreeMap<Integer, Double>();
		String line;

		Scanner sc = new Scanner(new BufferedInputStream(getClass().getResourceAsStream(resource)));
		while(sc.hasNextLine() == true ) {
			line = sc.nextLine();
			if((!line.trim().isBlank() || !line.trim().isEmpty()) && line.charAt(0)!='#') { //ignore comments, and empty lines
				String line2 = line.replaceAll("\t+", " "); //replaces all (duplicate) tabs with a single space 
				String line3 = line2.replaceAll(" +", " "); //replaces all (duplicate) spaces with a single space 
				String[] portions = line3.split(" ");
				returnMap.put(Integer.parseInt(portions[0]), Double.parseDouble(portions[1]));
			}
		}
		sc.close();
		
		return returnMap;
	}

	public TreeMap<Integer, Double> getChauvenet() {
		return chauvenet;
	}

	public TreeMap<Integer, Double> getGrubbs() {
		return grubbs;
	}

	public TreeMap<Integer, Double> getStudent() {
		return student;
	}

	public TreeMap<Integer, Double> getGrafHenning() {
		return grafHenning;
	}

	
}
