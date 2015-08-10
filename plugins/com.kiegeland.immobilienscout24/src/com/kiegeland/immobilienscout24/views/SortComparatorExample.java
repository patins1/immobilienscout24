/**
 * Copyright (C) 2015 by Joerg Kiegeland
 */
package com.kiegeland.immobilienscout24.views;

import de.kupzog.ktable.KTableSortComparator;
import de.kupzog.ktable.KTableSortedModel;

public class SortComparatorExample extends KTableSortComparator {

	public SortComparatorExample(KTableSortedModel model, int columnIndex, int direction) {
		super(model, columnIndex, direction);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kupzog.ktable.KTableSortComparator#doCompare(java.lang.Object, java.lang.Object, int, int)
	 */
	public int doCompare(Object o1, Object o2, int row1, int row2) {
		if (o1 instanceof Integer && o2 instanceof Integer)
			return ((Integer) o1 - (Integer) o2);
		return ("" + o1).compareTo(("" + o2));
	}

}
