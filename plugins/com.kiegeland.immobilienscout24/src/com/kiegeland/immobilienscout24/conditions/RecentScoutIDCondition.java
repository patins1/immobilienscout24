/**
 * Copyright (C) 2015 by Joerg Kiegeland
 */
package com.kiegeland.immobilienscout24.conditions;

import com.kiegeland.immobilienscout24.domain.PureWohnung;

public class RecentScoutIDCondition extends Condition {

	public static String ScoutID = "";

	@Override
	public float success(PureWohnung buy) {
		return RecentScoutIDCondition.ScoutID.compareTo(buy.scoutID) <= 0 ? 1 : 0;
	}

}
