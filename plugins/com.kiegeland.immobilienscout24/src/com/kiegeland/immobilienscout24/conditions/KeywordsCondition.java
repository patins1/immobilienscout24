/**
 * Copyright (C) 2015 by Joerg Kiegeland
 */
package com.kiegeland.immobilienscout24.conditions;

import com.kiegeland.immobilienscout24.domain.PureWohnung;

public class KeywordsCondition extends Condition {

	public static String Substring = "";

	@Override
	public float success(PureWohnung buy) {
		for (String keyword : KeywordsCondition.Substring.toLowerCase().split("\\|")) {
			if ("".equals(keyword) || buy.html.contains(keyword))
				return 1;
		}
		return 0;
	}

}
