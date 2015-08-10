/**
 * Copyright (C) 2015 by Joerg Kiegeland
 */
package com.kiegeland.immobilienscout24.util;

public class SearchStructure {

	private String string;

	int index = 0;

	private int startedAt;

	public SearchStructure(String string) {
		this.string = string;
	}

	public String findNextRawExclusive(String startsWith, String endsWith) {
		if (index == -1)
			throw new RuntimeException("Already failed findNext cannot be resumed!");
		index = string.indexOf(startsWith, index);
		if (index == -1) {
			return null;
		}
		startedAt = index;
		index += startsWith.length();
		int beginIndex = index;
		index = string.indexOf(endsWith, index);
		if (index == -1) {
			return null;
		}
		String result = string.substring(beginIndex, index).trim();
		return result;
	}

	public String findNextRaw(String startsWith, String endsWith) {
		String result = findNextRawExclusive(startsWith, endsWith);
		if (result != null) {
			index += endsWith.length();
		}
		return result;
	}

	public String findNext(String startsWith, String endsWith) {
		String result = findNextRaw(startsWith, endsWith);
		if (result == null)
			return null;
		result = removeTags(result);
		result = resolveEntities(result);
		return result.trim();
	}

	public String tryFindNext(String startsWith, String endsWith) {
		int oldIndex = index;
		String result = findNext(startsWith, endsWith);
		index = oldIndex;
		if ("".equals(result))
			return null;
		return result;
	}

	public void replaceBy(String replacement) {
		string = string.substring(0, startedAt) + replacement + string.substring(index);
		index = startedAt + replacement.length();
	}

	public String getReplacedString() {
		return string;
	}

	static public String removeTags(String s) {
		if (s.indexOf("<") == -1)
			return s;
		SearchStructure sea = new SearchStructure(">" + s + "<");
		StringBuilder builder = new StringBuilder();
		String ss;
		while ((ss = sea.findNext(">", "<")) != null) {
			builder.append(ss.trim());
		}
		return builder.toString();
	}

	private String resolveEntities(String s) {
		SearchStructure sea = new SearchStructure(s);
		String rawText;
		while ((rawText = sea.findNext("&", ";")) != null) {
			sea.replaceBy(resolveEntity(rawText));
		}
		return sea.getReplacedString();
	}

	/**
	 * Resolves an entity reference or character reference to its value.
	 * 
	 * @param entName
	 *            The 'name' of the reference. This is the string between &amp; and ;, e.g. amp, quot, #65 or #x41.
	 * @return The value of the supplied reference, or the reference itself if it could not be resolved.
	 */
	public static String resolveEntity(String entName) {
		if (entName.startsWith("#")) {
			// character reference
			StringBuilder sb = new StringBuilder();
			if (entName.charAt(1) == 'x') {
				// Hex-notation
				sb.append((char) Integer.parseInt(entName.substring(2), 16));
			} else {
				// Dec-notation
				sb.append((char) Integer.parseInt(entName.substring(1)));
			}
			return sb.toString();
		} else if (entName.equals("apos")) {
			return "'";
		} else if (entName.equals("quot")) {
			return "\"";
		} else if (entName.equals("gt")) {
			return ">";
		} else if (entName.equals("lt")) {
			return "<";
		} else if (entName.equals("amp")) {
			return "&";
		} else if (entName.equals("nbsp")) {
			return " ";
		} else {
			return entName;
		}
	}

	public static String find(String content, String startsWith, String endsWith) {
		SearchStructure ss = new SearchStructure(content);
		return ss.findNext(startsWith, endsWith);
	}

}
