/**
 * Copyright (C) 2015 by Joerg Kiegeland
 */
package com.kiegeland.immobilienscout24.conditions;

import com.kiegeland.immobilienscout24.domain.PureWohnung;

public class MaxWarmCondition extends Condition {

	public static int MaxWarmmiete = 400;

	@Override
	public float success(PureWohnung buy) {
		return buy.warm <= MaxWarmmiete ? 1 : 0;
	}

}
