/**
 * Copyright (C) 2015 by Joerg Kiegeland
 */
package com.kiegeland.immobilienscout24;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class DemoWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	static public boolean REQUIRE_LOGIN = true;

	public DemoWorkbenchWindowAdvisor(final IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(800, 600));
		configurer.setShowCoolBar(false);
		configurer.setTitle("Immobilienscout24 Screenscraper");
		configurer.setShellStyle(SWT.TITLE | SWT.MAX | SWT.RESIZE);
		configurer.setShowProgressIndicator(true);
		configurer.setShowStatusLine(false);
	}

	public void postWindowOpen() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		Shell shell = window.getShell();
		Rectangle shellBounds = shell.getBounds();
		if (!shell.getMaximized() && shellBounds.x == 0 && shellBounds.y == 0) {
			shell.setLocation(70, 25);
		}
	}

	@Override
	public void postWindowCreate() {
		// close opened perspective
		// IPerspectiveDescriptor pers = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getPerspective();
		// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closePerspective(pers, false, false);

		Shell shell = getWindowConfigurer().getWindow().getShell();
		// getWindowConfigurer().setInitialSize(new Point(400, 300));
		getWindowConfigurer().setShowCoolBar(false);
		getWindowConfigurer().setShowStatusLine(false);
		getWindowConfigurer().setShowPerspectiveBar(false);
		shell.setMaximized(true);
	}
}
