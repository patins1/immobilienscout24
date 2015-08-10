/**
 * Copyright (C) 2015 by Joerg Kiegeland
 */
package com.kiegeland.immobilienscout24.domain;

import org.eclipse.swt.graphics.RGB;

import com.kiegeland.immobilienscout24.table.ITaggedValues;

public class Wohnung extends PureWohnung implements ITaggedValues {

	public Wohnung(String scoutID) {
		super(scoutID);
	}

	@Override
	public Object getAttribute(String name) {
		if (name.equals("Scout-ID"))
			return scoutID;
		if (name.equals("Kalt"))
			return noMinus(kalt);
		if (name.equals("per m²") && kalt != -1 && wohnflaeche >= 1) {
			String s = "" + 100 * kalt / wohnflaeche;
			while (s.length() < 3)
				s = "0" + s;
			return s.substring(0, s.length() - 2) + "," + s.substring(s.length() - 2);
		}
		if (name.equals("Warm"))
			return noMinus(warm);
		if (name.equals("Etage"))
			return noMinus(etage);
		if (name.equals("Etagen"))
			return noMinus(maxEtage);
		if (name.equals("m²"))
			return noMinus(wohnflaeche);
		if (name.equals("Typ"))
			return wohnungstyp;
		if (name.equals("Jahr"))
			return noMinus(baujahr);
		if (name.equals("Success"))
			return "" + success;
		if (name.equals("PLZ"))
			return noMinus(plz);
		if (name.equals("Adresse"))
			return adress;
		if (name.equals("Titel"))
			return title;
		return "x";
	}

	private Object noMinus(int etage2) {
		if (etage2 == -1)
			return "";
		return etage2;
	}

	@Override
	public RGB getBackgoundColor(int column) {
		if (faraway)
			return new RGB(255, 180, 180);
		if (good)
			return new RGB(255, 255, 0);
		if (visited)
			return new RGB(200, 200, 200);
		return new RGB(255, 255, 255);
	}

	@Override
	public int getInitialColumnWidth(int column) {
		int[] widths = new int[] { 70, 50, 50, 50, 50, 50, 50, 120, 50, 60, 50, 140 };
		if (column < widths.length)
			return widths[column];
		return 100;
	}

}
