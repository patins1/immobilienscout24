/**
 * Copyright (C) 2015 by Joerg Kiegeland
 */
package com.kiegeland.immobilienscout24.conditions;

import java.util.Collection;
import java.util.HashSet;

import com.kiegeland.immobilienscout24.domain.PureWohnung;

public class UnwantedStadtteilCondition extends Condition {

	static public final Collection<String> UNWANTED = new HashSet<String>();

	static {
		UNWANTED.add("Niederschönhausen");
		UNWANTED.add("Altglienicke");
		UNWANTED.add("Haselhorst");
		UNWANTED.add("Köpenick");
		UNWANTED.add("Biesdorf");
		UNWANTED.add("Rosenthal");
		UNWANTED.add("Kaulsdorf");
		UNWANTED.add("Friedrichshagen");
		UNWANTED.add("Staaken");
		UNWANTED.add("Niederschönhausen");
		UNWANTED.add("Alt-Hohenschönhausen");
		UNWANTED.add("Spandau");
		UNWANTED.add("Rahnsdorf");
		UNWANTED.add("Adlershof");
		UNWANTED.add("Hellersdorf");
		UNWANTED.add("Weißensee");
		UNWANTED.add("Mahlsdorf");
		UNWANTED.add("Johannisthal");
		UNWANTED.add("Müggelheim");
		UNWANTED.add("Grünau");
		UNWANTED.add("Siemensstadt");
		UNWANTED.add("Lichtenberg");
		UNWANTED.add("Friedrichsfelde");
		UNWANTED.add("Friedrichshain");
		UNWANTED.add("Neu-Hohenschönhausen");
		UNWANTED.add("Hohenschönhausen");
		UNWANTED.add("Marzahn");
	}

	@Override
	public float success(PureWohnung buy) {
		return UNWANTED.contains(buy.adress) ? 0 : 1;
	}
}
