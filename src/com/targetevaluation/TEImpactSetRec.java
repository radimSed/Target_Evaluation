package com.targetevaluation;

import java.util.List;

public record TEImpactSetRec(
		String place,
		String date,
		String time,
		String position,
		String distance,
		String targetNmbr,
		String angle,
		String lightDir,

		String temp,
		String pressure,
		String humidity,
		String mirageCategory,
		String mirageClassification,
		String windDirection,
		String windSpeed,

		String rifleAssembly,
		String caliber, 
		String amunition,
		String batch,
		String initVelocity,
		String elevation, 
		String windageVal,
		String windageDir,
		
		String notes,
		List<TEImpactRec> impacts) {

}
