/**
 * Copyright (C) 2015 by Joerg Kiegeland
 */
package com.kiegeland.immobilienscout24.conditions;

import com.kiegeland.immobilienscout24.domain.PureWohnung;

public class AdresseCondition extends Condition {

	public static String Adresse = "";

	@Override
	public float success(PureWohnung buy) {
		return buy.adress.toLowerCase().contains(AdresseCondition.Adresse.toLowerCase()) || "".equals(AdresseCondition.Adresse) ? 1 : 0;
	}

}
