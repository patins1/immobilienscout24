/**
 * Copyright (C) 2015 by Joerg Kiegeland
 */
package com.kiegeland.immobilienscout24.conditions;

import com.kiegeland.immobilienscout24.domain.PureWohnung;

public class HasTerrasseCondition extends Condition {

	static public boolean IsEnabled = false;

	@Override
	public float success(PureWohnung buy) {
		if (!IsEnabled)
			return 1;
		return buy.terrasse ? 1 : 0;
	}

}
