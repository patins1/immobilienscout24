/**
 * Copyright (C) 2015 by Joerg Kiegeland
 */
package com.kiegeland.immobilienscout24.conditions;

import com.kiegeland.immobilienscout24.domain.PureWohnung;

public class NeubauCondition extends Condition {

	public static int FromYear = 1950;

	@Override
	public float success(PureWohnung buy) {
		return buy.baujahr >= NeubauCondition.FromYear || NeubauCondition.FromYear == 1950 ? 1 : 0;
	}

}
