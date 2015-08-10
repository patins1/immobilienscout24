/**
 * Copyright (C) 2015 by Joerg Kiegeland
 */
package com.kiegeland.immobilienscout24.domain;

import java.io.File;
import java.io.IOException;

import com.kiegeland.immobilienscout24.util.Utilities;

public class PureWohnung {

	public String title = "";

	public String html = "";

	public String scoutID;

	public int etage = -1;

	public int maxEtage = -1;

	public float success;

	public String wohnungstyp = "";

	public int baujahr = -1;

	public int kalt = -1;

	public int warm = -1;

	public String adress = "";

	public int plz;

	public boolean terrasse = false;

	public boolean visited = false;

	public boolean good = false;

	public boolean faraway = false;

	public int wohnflaeche = -1;

	public static final String GOODWOHNUNG = "GOODWOHNUNG ";

	public static final String VISITEDWOHNUNG = "VISITEDWOHNUNG ";

	public static final String FARAWAY = "FARAWAYWOHNUNG ";

	public static final String ISNOTHING = "";

	public PureWohnung(String scoutID) {
		this.scoutID = scoutID;
	}

	private Object noMinus(int etage2) {
		if (etage2 == -1)
			return "";
		return etage2;
	}

	public File getFile() {
		return new File("c:/wohnung/files/" + scoutID + ".html");
	}

	public void setGood(boolean selection) {
		if (selection != good) {
			good = selection;
			File file = getFile();
			if (good) {
				try {
					Utilities.appendFile(file, PureWohnung.GOODWOHNUNG);
				} catch (IOException e) {
					e.printStackTrace();
				}
				setFaraway(false);
			} else {
				try {
					Utilities.toFile(file, Utilities.fromFileSlow(file).replace(GOODWOHNUNG, ""));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void setFaraway(boolean selection) {
		if (selection != faraway) {
			faraway = selection;
			File file = getFile();
			if (faraway) {
				try {
					Utilities.appendFile(file, PureWohnung.FARAWAY);
				} catch (IOException e) {
					e.printStackTrace();
				}
				setGood(false);
			} else {
				try {
					Utilities.toFile(file, Utilities.fromFileSlow(file).replace(FARAWAY, ""));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
