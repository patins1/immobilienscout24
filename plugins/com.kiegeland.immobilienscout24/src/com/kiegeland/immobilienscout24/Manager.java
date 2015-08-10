/**
 * Copyright (C) 2015 by Joerg Kiegeland
 */
package com.kiegeland.immobilienscout24;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.kiegeland.immobilienscout24.conditions.Condition;
import com.kiegeland.immobilienscout24.domain.Wohnung;
import com.kiegeland.immobilienscout24.util.Utilities;
import com.kiegeland.immobilienscout24.views.AllStockGainsView;
import com.kiegeland.immobilienscout24.views.ControlView;

public class Manager {

	public static List<Wohnung> allWohnung;

	static {
		init();
	}

	public static void init() {
		allWohnung = new ArrayList<Wohnung>();
		String marktDir = "c:/wohnung/files";
		File[] listFiles = new File(marktDir).listFiles();
		if (listFiles != null)
			for (File file : listFiles) {
				// System.out.println("Reading " + file);
				if (!file.getName().endsWith(".html"))
					continue;
				String content;
				try {
					content = Utilities.fromFile(file);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				if (Wohnung.ISNOTHING.equals(content))
					continue;

				// System.out.println("Reading finished #" + allWohnung.size());

				Wohnung wohnung = new Wohnung(file.getName().substring(0, file.getName().length() - ".html".length()));

				Main.initialize(wohnung, content);

				wohnung.good = content.contains(Wohnung.GOODWOHNUNG);
				wohnung.faraway = content.contains(Wohnung.FARAWAY);
				wohnung.visited = content.contains(Wohnung.VISITEDWOHNUNG);
				//
				// if ("49178733".equals(wohnung.scoutID))
				// wohnung.visited = content.contains(Wohnung.VISITEDWOHNUNG);

				// float success = Condition.getGain(wohnung);
				// if (success == 0) {
				// success = Condition.getGain(wohnung);
				// continue;
				// }

				allWohnung.add(wohnung);
				// if (allWohnung.size() >= 200)
				// break;

			}
	}

	public static List<Wohnung> calcGains(List<Wohnung> stocks) {
		Date startDate = null;
		Date endDate = null;
		try {
			// startDate = Stock.dateFormat.parse("2001-12-01");
			// endDate = Stock.dateFormat.parse("2008-12-12");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		List<Wohnung> gains = new ArrayList<Wohnung>();
		for (Wohnung buy : stocks) {
			float success = Condition.getGain(buy);
			if (success == 0)
				continue;
			gains.add(buy);

		}
		return gains;
	}

	public static List<Wohnung> getEnabledMaerkte() {
		return allWohnung;
	}

	public static void ReCalc() {
		try {
			AllStockGainsView viewPart1 = (AllStockGainsView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(AllStockGainsView.ID);
			viewPart1.calcMainTable();

			ControlView controlView = (ControlView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ControlView.ID);
			if (controlView != null)
				controlView.totalFiltered.setText("#" + (viewPart1.gainsTable.getModel().getRowCount() - 1));
		} catch (PartInitException e2) {
			e2.printStackTrace();
			// throw new RuntimeException(e2);
		}
	}

}
