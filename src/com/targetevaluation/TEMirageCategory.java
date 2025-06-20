package com.targetevaluation;

public enum TEMirageCategory {
	NotApplicable("Not Applicable"), 
	Light("Light"), 
	Intermediate("Intermediate"), 
	Heavy("Heavy");
	
	private String value;
	
	TEMirageCategory(String value) {
		this.value = value;
	}
	
	@Override
	public String toString(){
		return value;
	}
}
