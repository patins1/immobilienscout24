/**
 * Copyright (C) 2015 by Joerg Kiegeland
 */
package com.kiegeland.immobilienscout24.conditions;

import com.kiegeland.immobilienscout24.domain.PureWohnung;

public class TopGeschossCondition extends Condition {

	static public boolean IsEnabled = false;

	@Override
	public float success(PureWohnung buy) {
		if (!IsEnabled)
			return 1;
		if (buy.etage != -1 && buy.maxEtage == -1 && "Dachgeschoss".equals(buy.wohnungstyp))
			return buy.etage + 2;
		if (buy.etage == -1 && buy.maxEtage != -1 && "Dachgeschoss".equals(buy.wohnungstyp))
			return buy.maxEtage + 2;
		if (buy.etage != -1 && buy.maxEtage != -1 && buy.etage == buy.maxEtage)
			return buy.etage + 2;
		if ("Dachgeschoss".equals(buy.wohnungstyp))
			return 1;
		if ("Dachgeschoss".equals(buy.wohnungstyp))
			return 1;
		if ("Maisonette".equals(buy.wohnungstyp))
			return 1;
		return 0;
	}

}
