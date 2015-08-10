/**
 * Copyright (C) 2015 by Joerg Kiegeland
 */
package com.kiegeland.immobilienscout24.conditions;

import java.util.ArrayList;
import java.util.List;

import com.kiegeland.immobilienscout24.domain.PureWohnung;

abstract public class Condition {

	abstract public float success(PureWohnung buy);

	public boolean fullfilled(PureWohnung buy) {
		return success(buy) >= 1;
	}

	public static float getGain(PureWohnung buy) {
		for (Condition condition : getConditions())
			if (!condition.fullfilled(buy)) {
				return 0;
			}
		for (Condition condition : getConditions())
			return buy.success = condition.success(buy);

		return 0;
	}

	public static List<Condition> getConditions() {
		List<Condition> conditions = new ArrayList<Condition>();
		conditions.add(new TopGeschossCondition());
		conditions.add(new HasTerrasseCondition());
		// conditions.add(new MaxKaltCondition());
		conditions.add(new MaxWarmCondition());
		conditions.add(new KeywordsCondition());
		conditions.add(new PlzCondition());
		conditions.add(new SubStringCondition());
		conditions.add(new RecentScoutIDCondition());
		conditions.add(new NeubauCondition());
		conditions.add(new UnwantedStadtteilCondition());
		conditions.add(new AdresseCondition());
		// conditions.add(new HasTerrasseCondition());
		return conditions;
	}

}
