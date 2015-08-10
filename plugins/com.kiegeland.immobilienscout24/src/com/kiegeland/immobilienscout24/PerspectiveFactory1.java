/**
 * Copyright (C) 2015 by Joerg Kiegeland
 */
package com.kiegeland.immobilienscout24;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.kiegeland.immobilienscout24.views.AllStockGainsView;
import com.kiegeland.immobilienscout24.views.ControlView;
import com.kiegeland.immobilienscout24.views.WebView;

public class PerspectiveFactory1 implements IPerspectiveFactory {

	public PerspectiveFactory1() {
		super();
	}

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		IFolderLayout topLeft = layout.createFolder("topLeft", IPageLayout.LEFT, 0.25f, editorArea);
		topLeft.addView(ControlView.ID);
		IFolderLayout bottomLeft = layout.createFolder("bottomLeft", IPageLayout.BOTTOM, 0.50f, "topLeft");
		bottomLeft.addView(AllStockGainsView.ID);
		// IFolderLayout bottom = layout.createFolder( "bottom",
		// IPageLayout.BOTTOM,
		// 0.60f,
		// editorArea );
		IFolderLayout topRight = layout.createFolder("topRight", IPageLayout.RIGHT, 0.70f, editorArea);
		topRight.addView(WebView.ID);
	}

}
