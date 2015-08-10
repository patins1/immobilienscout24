/**
 * Copyright (C) 2015 by Joerg Kiegeland
 */
package com.kiegeland.immobilienscout24.conditions;

import com.kiegeland.immobilienscout24.domain.PureWohnung;

public class SubStringCondition extends Condition {

	public static String Substring = "";

	@Override
	public float success(PureWohnung buy) {
		return buy.title.toLowerCase().contains(SubStringCondition.Substring.toLowerCase()) || "".equals(SubStringCondition.Substring) ? 1 : 0;
	}

}
