/**
 * Copyright (C) 2015 by Joerg Kiegeland
 */
package com.kiegeland.immobilienscout24.conditions;

import com.kiegeland.immobilienscout24.domain.PureWohnung;

public class MaxKaltCondition extends Condition {

	@Override
	public float success(PureWohnung buy) {
		return buy.kalt <= 330 ? 1 : 0;
	}

}
