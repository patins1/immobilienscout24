/**
 * Copyright (C) 2015 by Joerg Kiegeland
 */
package com.kiegeland.immobilienscout24.table;

import org.eclipse.swt.graphics.RGB;

public interface ITaggedValues {

	Object getAttribute(String name);

	RGB getBackgoundColor(int row);

	int getInitialColumnWidth(int column);

}
