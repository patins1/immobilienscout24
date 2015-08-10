/**
 * Copyright (C) 2015 by Joerg Kiegeland
 */
package com.kiegeland.immobilienscout24.views;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.kiegeland.immobilienscout24.Main;
import com.kiegeland.immobilienscout24.Manager;
import com.kiegeland.immobilienscout24.conditions.AdresseCondition;
import com.kiegeland.immobilienscout24.conditions.HasTerrasseCondition;
import com.kiegeland.immobilienscout24.conditions.KeywordsCondition;
import com.kiegeland.immobilienscout24.conditions.MaxWarmCondition;
import com.kiegeland.immobilienscout24.conditions.NeubauCondition;
import com.kiegeland.immobilienscout24.conditions.PlzCondition;
import com.kiegeland.immobilienscout24.conditions.RecentScoutIDCondition;
import com.kiegeland.immobilienscout24.conditions.SubStringCondition;
import com.kiegeland.immobilienscout24.conditions.TopGeschossCondition;
import com.kiegeland.immobilienscout24.domain.Wohnung;

public class ControlView extends ViewPart {
	public static final String ID = ControlView.class.getName();

	private Composite panelBase;
	private Label baseLabel;
	private Composite panelPLZ;
	private Composite panelSubstring;
	private Composite panelScoutID;
	public Label totalFiltered;
	private Button good;
	private Button faraway;

	public ControlView() {
	}

	@Override
	public void createPartControl(Composite parent) {

		parent.setLayout(new RowLayout(SWT.VERTICAL));

		{
			panelBase = new Composite(parent, 0);
			panelBase.setLayout(new FillLayout(SWT.HORIZONTAL));
			final Slider slider = new Slider(panelBase, 0);
			baseLabel = new Label(panelBase, 0);
			slider.setMinimum(0);
			slider.setMaximum(72);
			slider.setSelection(NeubauCondition.FromYear - 1950);
			slider.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					NeubauCondition.FromYear = slider.getSelection() + 1950;
					updateYearLabel();
					Recalc();
				}

			});
			updateYearLabel();
		}

		{
			panelPLZ = new Composite(parent, 0);
			panelPLZ.setLayout(new FillLayout(SWT.HORIZONTAL));
			Label label = new Label(panelPLZ, 0);
			label.setText("Plz:");
			final Text plz = new Text(panelPLZ, 0);
			plz.addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					PlzCondition.Plz = plz.getText();
					Recalc();
				}

			});
		}

		{
			panelSubstring = new Composite(parent, 0);
			panelSubstring.setLayout(new FillLayout(SWT.HORIZONTAL));
			Label label = new Label(panelSubstring, 0);
			label.setText("Titel:");
			final Text text = new Text(panelSubstring, 0);
			text.addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					SubStringCondition.Substring = text.getText();
					Recalc();
				}

			});
		}

		{
			Composite panelAdresse = new Composite(parent, 0);
			panelAdresse.setLayout(new FillLayout(SWT.HORIZONTAL));
			Label label = new Label(panelAdresse, 0);
			label.setText("Adresse:");
			final Text text = new Text(panelAdresse, 0);
			text.addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					AdresseCondition.Adresse = text.getText();
					Recalc();
				}

			});
		}

		{
			panelScoutID = new Composite(parent, 0);
			panelScoutID.setLayout(new FillLayout(SWT.HORIZONTAL));
			Label label = new Label(panelScoutID, 0);
			label.setText("Scout ID");
			final Text text = new Text(panelScoutID, 0);
			text.addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					RecentScoutIDCondition.ScoutID = text.getText();
					Recalc();
				}

			});
		}

		{
			Composite panelAdresse = new Composite(parent, 0);
			panelAdresse.setLayout(new FillLayout(SWT.HORIZONTAL));
			Label label = new Label(panelAdresse, 0);
			label.setText("Max Warmmiete:");
			final Text text = new Text(panelAdresse, 0);
			text.setText("" + MaxWarmCondition.MaxWarmmiete);
			text.addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					try {
						MaxWarmCondition.MaxWarmmiete = Integer.parseInt(text.getText());
						Recalc();
					} catch (NumberFormatException e2) {
					}
				}

			});
		}

		{
			Composite panelAdresse = new Composite(parent, 0);
			panelAdresse.setLayout(new FillLayout(SWT.HORIZONTAL));
			Label label = new Label(panelAdresse, 0);
			label.setText("Keywords (separate by |):");
			final Text text = new Text(panelAdresse, 0);
			text.setText("");
			text.addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					if (!KeywordsCondition.Substring.equals(text.getText())) {
						KeywordsCondition.Substring = text.getText();
						Recalc();
					}
				}

			});
		}

		{
			Composite panel = new Composite(parent, 0);
			panel.setLayout(new FillLayout(SWT.HORIZONTAL));
			totalFiltered = new Label(panel, 0);
			// totalFiltered.setText("");
			updateTotalFiltered();
		}
		{
			Composite panel = new Composite(parent, 0);
			panel.setLayout(new FillLayout(SWT.HORIZONTAL));
			final Button enableDachgeschoss = new Button(panel, SWT.CHECK);
			enableDachgeschoss.setText("Nur Dachgeschoss");

			enableDachgeschoss.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// nothing to do
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					TopGeschossCondition.IsEnabled = enableDachgeschoss.getSelection();
					Recalc();
				}

			});
		}
		{
			Composite panel = new Composite(parent, 0);
			panel.setLayout(new FillLayout(SWT.HORIZONTAL));
			final Button enableTerrasse = new Button(panel, SWT.CHECK);
			enableTerrasse.setText("Nur mit Balkon/Terrasse");

			enableTerrasse.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// nothing to do
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					HasTerrasseCondition.IsEnabled = enableTerrasse.getSelection();
					Recalc();
				}

			});
		}

		{
			Composite panel = new Composite(parent, 0);
			panel.setLayout(new FillLayout(SWT.HORIZONTAL));
			good = new Button(panel, SWT.CHECK);
			good.setText("GOOD");

			good.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// nothing to do
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						AllStockGainsView viewPart1 = (AllStockGainsView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(AllStockGainsView.ID);
						if (viewPart1 != null) {
							for (Wohnung wohnung : viewPart1.getSelection()) {
								wohnung.setGood(good.getSelection());
								updateStates(wohnung);
							}
							viewPart1.gainsTable.redraw();
						}
					} catch (Throwable e2) {
						e2.printStackTrace();
						// throw new RuntimeException(e2);
					}
				}

			});
		}

		{
			Composite panel = new Composite(parent, 0);
			panel.setLayout(new FillLayout(SWT.HORIZONTAL));
			faraway = new Button(panel, SWT.CHECK);
			faraway.setText("FAR");

			faraway.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// nothing to do
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						AllStockGainsView viewPart1 = (AllStockGainsView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(AllStockGainsView.ID);
						if (viewPart1 != null) {
							for (Wohnung wohnung : viewPart1.getSelection()) {
								wohnung.setFaraway(faraway.getSelection());
								updateStates(wohnung);
							}
							viewPart1.gainsTable.redraw();
						}
					} catch (Throwable e2) {
						e2.printStackTrace();
						// throw new RuntimeException(e2);
					}
				}

			});
		}

		{
			Composite comp = new Composite(parent, 0);
			comp.setLayout(new FillLayout(SWT.HORIZONTAL));
			Button button = new Button(comp, 0);
			button.setText("Update");
			button.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					IRunnableWithProgress op = new IRunnableWithProgress() {
						public void run(IProgressMonitor monitor) {
							Main.main(monitor);
							Manager.init();
							Manager.ReCalc();
						}
					};
					getSite().getShell().getDisplay().readAndDispatch();
					try {
						ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(getSite().getShell()) {

							protected void configureShell(final Shell shell) {
								super.configureShell(shell);
								shell.setText("Update from immobilienscout24");
							}

						};
						progressMonitorDialog.run(true, true, op);
					} catch (Exception e2) {
						throw new RuntimeException(e2);
					}
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}

			});
		}
	}

	private void Recalc() {
		Manager.ReCalc();
	}

	private void updateTotalFiltered() {
		// totalFiltered.setText("#" + summedGains.size());
	}

	private void updateYearLabel() {
		baseLabel.setText(NeubauCondition.FromYear == 1950 ? "  All" : "  Year>=" + NeubauCondition.FromYear);
	}

	@Override
	public void setFocus() {
		// nothing to do
	}

	public void updateStates(Wohnung wohnung) {
		good.setSelection(wohnung.good);
		faraway.setSelection(wohnung.faraway);
	}

}
