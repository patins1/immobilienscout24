/**
 * Copyright (C) 2015 by Joerg Kiegeland
 */
package com.kiegeland.immobilienscout24.conditions;

import com.kiegeland.immobilienscout24.domain.PureWohnung;

public class PlzCondition extends Condition {

	public static String Plz = "";

	@Override
	public float success(PureWohnung buy) {
		return ("" + buy.plz).startsWith(PlzCondition.Plz) ? 1 : 0;
	}

}
