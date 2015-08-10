/**
 * Copyright (C) 2015 by Joerg Kiegeland
 */
package com.kiegeland.immobilienscout24.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class WebView extends ViewPart {
	public static final String ID = WebView.class.getName();

	private Composite panelBase;
	public Browser webBrowser;

	public WebView() {
	}

	@Override
	public void createPartControl(Composite parent) {

		// parent.setLayout(new RowLayout(SWT.VERTICAL));

		{
			// panelBase = new Composite(parent, 0);
			// panelBase.setLayout(new FillLayout(SWT.HORIZONTAL));
			webBrowser = new Browser(parent, SWT.FILL/* | SWT.MOZILLA */);
		}

	}

	@Override
	public void setFocus() {
		// nothing to do
	}

}
