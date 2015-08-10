/**
 * Copyright (C) 2015 by Joerg Kiegeland
 */
package com.kiegeland.immobilienscout24;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.kiegeland.immobilienscout24.conditions.UnwantedStadtteilCondition;
import com.kiegeland.immobilienscout24.domain.PureWohnung;
import com.kiegeland.immobilienscout24.util.SearchStructure;
import com.kiegeland.immobilienscout24.util.Utilities;

public class Main {

	private static final int rel = 5;
	private static int runningThreads = 0;
	private static int totalNew = 0;

	public static Map<String, String> repos = new HashMap<String, String>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		main(new NullProgressMonitor());
	}

	public static void main(IProgressMonitor monitor) {
		File kursFile = new File("c:/wohnung/files");
		for (int m1 = 20; m1 < 80; m1 += rel) {
			String meters = "Download meters " + m1 + " to " + (m1 + rel - 1);
			monitor.subTask(meters);
			System.out.println(meters);
			for (int m2 = m1; m2 <= m1 + rel - 1 && m2 < 110; m2++) {
				if (m2 > 50)
					continue;
				final int fjahr = m2;
				String url = "http://www.immobilienscout24.de/Suche/S-T/Wohnung-Miete/Berlin/Berlin/-/1,00-2,00/60,00-61,00/EURO-100,00-500,00?pagerReporting=true";
				url = url.replace("60,00-61,00", fjahr + ",00-" + (fjahr + 1) + ",00");
				final String furl = url;
				final int fm2 = m2;
				System.out.println("Will download " + furl);
				runningThreads++;
				new Thread() {
					@Override
					public void run() {
						downloadStocks(furl, fm2);
						runningThreads--;
					}
				}.start();
			}
			while (runningThreads > 0) {
				if (monitor.isCanceled())
					return;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if (monitor.isCanceled())
			return;
		if (kursFile.list() != null)
			for (String f : kursFile.list()) {
				if (f.endsWith(".html")) {
					String scoutID = f.substring(0, f.length() - ".html".length());
					if (!scoutIDs.contains(scoutID)) {
						new File("c:/wohnung/files/" + f).delete();
						System.out.println("Deleted " + scoutID);
					}
				}
			}
		System.out.println("Finished (minimal=" + minimal + ")!");
	}

	static Set<String> scoutIDs = new HashSet<String>();
	private static String minimal = null;

	static void downloadStocks(String url, int m2) {
		try {
			if (!url.contains("Wohnung-Miete"))
				throw new RuntimeException("'Wohnung-Miete' missing!");
			int page = 1;
			// System.out.println("Downloading " + url);
			while (true) {
				if (url.indexOf("/P-") != -1)
					throw new RuntimeException("Must be first page:" + url);
				String ss = url;
				if (page >= 2)
					ss = ss.replace("Wohnung-Miete", "P-" + page + "/Wohnung-Miete");
				String kurseString = Utilities.downloadURL(ss);
				SearchStructure sea = new SearchStructure(kurseString);
				int numberNew = 0;
				int number = 0;
				String scoutID;
				while ((scoutID = sea.findNext("name=\"", "\"")) != null) {
					try {
						Integer.parseInt(scoutID);
						synchronized (scoutIDs) {
							if (!scoutIDs.add(scoutID))
								continue;
						}
						File kursFile = new File("c:/wohnung/files/" + scoutID + ".html");
						if (!kursFile.exists()) {

							synchronized (scoutIDs) {
								if (minimal == null || minimal.compareTo(scoutID) > 0) {
									minimal = scoutID;
								}
							}

							String content = Utilities.downloadURL("http://www.immobilienscout24.de/" + scoutID);
							kursFile.getParentFile().mkdirs();
							PureWohnung wohnung = new PureWohnung(scoutID);
							initialize(wohnung, content);
							boolean hasUnwanted = false;
							for (String unwanted : UnwantedStadtteilCondition.UNWANTED)
								if (content.contains(unwanted))
									hasUnwanted = true;
							// if (hasUnwanted || Condition.getGain(wohnung) == 0) {
							// Utilities.toFile(kursFile, Wohnung.ISNOTHING);
							// } else {
							Utilities.toFile(kursFile, content);
							totalNew++;
							// }
							numberNew++;
						}
						number++;
					} catch (NumberFormatException e) {
					}
				}
				System.out.println("Page " + page + " for " + m2 + "m² has " + numberNew + "/" + number + " IDs, total new " + totalNew);
				if (numberNew == 0)
					break;
				page++;
				if (page >= 66)
					throw new RuntimeException("Too much pages for " + url);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void initialize(PureWohnung wohnung, String content) {
		wohnung.html = SearchStructure.removeTags(content).toLowerCase();

		String s;
		if ((s = SearchStructure.find(content, "<td class=\"is24qa-etage\">", "</td>")) != null) {
			wohnung.etage = Integer.parseInt(s);
		}

		if ((s = SearchStructure.find(content, "<td class=\"is24qa-etagenanzahl\">", "</td>")) != null) {
			wohnung.maxEtage = Integer.parseInt(s);
		}

		if ((s = SearchStructure.find(content, "<td class=\"is24qa-wohnflaeche-ca\">", "</td>")) != null) {
			if (s.indexOf(",") != -1)
				wohnung.wohnflaeche = Integer.parseInt(s.substring(0, s.indexOf(",")));
		}

		if ((s = SearchStructure.find(content, "<strong class=\"is24qa-kaltmiete\">", "</strong>")) != null) {
			wohnung.kalt = getMoney(s);
		}

		if ((s = SearchStructure.find(content, "<strong class=\"is24qa-gesamtmiete\">", "</strong>")) != null) {
			wohnung.warm = getMoney(s);
		}

		if ((s = SearchStructure.find(content, "<td class=\"is24qa-wohnungstyp\">", "</td>")) != null) {
			if (!repos.containsKey(s)) {
				repos.put(s, s);
			} else {
				s = repos.get(s);
			}
			wohnung.wohnungstyp = s;
		}

		if ((s = SearchStructure.find(content, "<td class=\"is24qa-baujahr\">", "</td>")) != null) {
			wohnung.baujahr = Integer.parseInt(s);
		}

		if ((s = SearchStructure.find(content, "<title>", "</title>")) != null) {
			wohnung.title = s;
		}

		SearchStructure sea = new SearchStructure(content);
		if ((s = sea.tryFindNext("<div class=\"is24-text is24-ex-address\">", "</div>")) != null) {
			s = s.trim();
			String ss;
			if ((ss = SearchStructure.find(s, ",", "(")) != null) {
				wohnung.adress = ss.trim();
			} else {
				wohnung.adress = s.trim();
			}
			if (SearchStructure.find(content, "ndige Adresse der Immobilie erhalten Sie vom Anbieter", ".") != null)
				wohnung.adress = "Vom Anbieter";
			int berlin = s.toLowerCase().indexOf("berlin");
			if (berlin != -1) {
				while (berlin >= 1 && s.substring(berlin - 1, berlin).trim().isEmpty()) {
					berlin--;
				}
				int plz = s.lastIndexOf(" ", berlin - 1);
				try {
					wohnung.plz = Integer.parseInt(s.substring(plz + 1, berlin));
				} catch (NumberFormatException e) {
				}

			}
		}

		wohnung.terrasse = content.contains("Terrasse");
	}

	private static int getMoney(String s) {
		if (s.indexOf(',') != -1)
			s = s.substring(0, s.indexOf(','));
		if (s.indexOf("EUR") != -1)
			s = s.substring(0, s.indexOf("EUR"));
		if (s.startsWith("="))
			s = s.substring(1);
		s = s.trim();
		return Integer.parseInt(s.replace(".", "").replace(" EUR", ""));
	}
}
