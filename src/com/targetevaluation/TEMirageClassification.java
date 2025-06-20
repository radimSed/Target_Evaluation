package com.targetevaluation;

public enum TEMirageClassification {
	NotApplicable("Not Applicable"), 
	Boiling("Boiling"), 
	Slow("Slow"), 
	Medium("Medium"), 
	Fast("Fast");
	
	private String value;
	
	TEMirageClassification(String value) {
		this.value = value;
	}
	
	@Override
	public String toString(){
		return value;
	}
}
