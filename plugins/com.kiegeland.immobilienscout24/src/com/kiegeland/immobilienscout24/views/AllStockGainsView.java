/**
 * Copyright (C) 2015 by Joerg Kiegeland
 */
package com.kiegeland.immobilienscout24.views;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.kiegeland.immobilienscout24.BorderLayout;
import com.kiegeland.immobilienscout24.Manager;
import com.kiegeland.immobilienscout24.domain.Wohnung;
import com.kiegeland.immobilienscout24.table.ITaggedValues;
import com.kiegeland.immobilienscout24.table.KImmoModel;
import com.kiegeland.immobilienscout24.util.Utilities;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableSortComparator;
import de.kupzog.ktable.KTableSortOnClick;
import de.kupzog.ktable.SWTX;

public class AllStockGainsView extends ViewPart {
	public static final String ID = AllStockGainsView.class.getName();

	public KTable gainsTable;
	private KTableSortOnClick oldListener;
	private SortComparatorExample comparator;

	public AllStockGainsView() {
	}

	@Override
	public void createPartControl(Composite parent) {

		parent.setLayout(new BorderLayout());

		gainsTable = new KTable(parent, SWT.FULL_SELECTION | SWTX.AUTO_SCROLL | SWT.FILL | SWTX.FILL_WITH_LASTCOL) {

			@Override
			protected void onKeyDown(KeyEvent e) {
				super.onKeyDown(e);
				if (e.keyCode == 127) {
					KImmoModel model = ((KImmoModel) this.getModel());
					Collection<Wohnung> toDelete = getSelection();
					for (Wohnung wohnung : toDelete) {
						try {
							Utilities.toFile(wohnung.getFile(), Wohnung.ISNOTHING);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					Manager.allWohnung.removeAll(toDelete);
					model.stocks.removeAll(toDelete);
					model.initialize();
					model.sort(comparator);
					for (int row : gainsTable.getRowSelection()) {
						this.setSelection(0, row - 1, false);
					}
					// if (minimalRow < model.getRowCount())
					// this.setSelection(0, minimalRow, false);
					// else
					// this.setSelection(0, model.getRowCount() - 1, false);
					this.redraw();
				}
			}

			@Override
			protected void fireCellSelection(int col, int row, int statemask) {
				KImmoModel model = ((KImmoModel) this.getModel());
				row = model.mapRowIndexToModel(row);
				ITaggedValues tv = model.stocks.get(row - 1);
				if (tv instanceof Wohnung) {
					Wohnung gain = (Wohnung) tv;
					try {
						// URI uri = new URI("file:///C:/wohnung/files/"
						// + gain.scoutID+".html");
						URI uri = new URI("http://www.immobilienscout24.de/" + gain.scoutID + "?objectTabListControl=tab.pictures");
						WebView webView = (WebView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(WebView.ID);
						if (webView != null)
							if (!uri.toString().equals(webView.webBrowser.getUrl()))
								webView.webBrowser.setUrl(uri.toString());

						if (!gain.visited) {
							Utilities.appendFile(gain.getFile(), Wohnung.VISITEDWOHNUNG);
							gain.visited = true;
						}

						ControlView controlView = (ControlView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ControlView.ID);
						if (controlView != null) {
							for (Wohnung wohnung : getSelection()) {
								controlView.updateStates(wohnung);
							}
						}

					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}

			@Override
			protected void fireCellDoubleClicked(int col, int row, int statemask) {
				KImmoModel model = ((KImmoModel) this.getModel());
				row = model.mapRowIndexToModel(row);
				ITaggedValues tv = model.stocks.get(row - 1);
				if (tv instanceof Wohnung) {
					Wohnung gain = (Wohnung) tv;
					try {
						// URI uri = new URI("file:///C:/wohnung/files/"
						// + gain.scoutID+".html");
						URI uri = new URI("http://www.immobilienscout24.de/" + gain.scoutID);
						Desktop.getDesktop().browse(uri);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}

		};
		gainsTable.setLayoutData(new BorderLayout.BorderData(BorderLayout.CENTER));

		calcMainTable();
	}

	public void calcMainTable() {
		List<Wohnung> summedGains = calcAll();
		Collections.sort(summedGains, new Comparator<Wohnung>() {
			@Override
			public int compare(Wohnung o1, Wohnung o2) {
				float success = o2.success - o1.success;
				if (success != 0) {
					return success > 0 ? 1 : -1;
				}
				return o1.scoutID.compareTo(o2.scoutID);
			}
		});

		KImmoModel model = new KImmoModel(summedGains, Arrays.asList("Scout-ID", "Kalt", "per m²", "Warm", "Etage", "Etagen", "m²", "Typ", "Jahr", "Success", "PLZ", "Adresse", "Titel"));
		model.initialize();
		gainsTable.setModel(model);

		// implement resorting when the user clicks on the table header:
		gainsTable.removeCellSelectionListener(oldListener);
		comparator = new SortComparatorExample(model, -1, KTableSortComparator.SORT_NONE);
		gainsTable.addCellSelectionListener(oldListener = new KTableSortOnClick(gainsTable, comparator));

	}

	private List<Wohnung> calcAll() {
		return Manager.calcGains(Manager.getEnabledMaerkte());
	}

	@Override
	public void setFocus() {
	}

	public Collection<Wohnung> getSelection() {
		Collection<Wohnung> result = new ArrayList<Wohnung>();
		KImmoModel model = ((KImmoModel) gainsTable.getModel());
		for (int row : gainsTable.getRowSelection()) {
			row = model.mapRowIndexToModel(row);
			ITaggedValues tv = model.stocks.get(row - 1);
			if (tv instanceof Wohnung) {
				result.add((Wohnung) tv);
			}
		}
		return result;
	}
}
