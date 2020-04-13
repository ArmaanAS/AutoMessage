package com.dash.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
	public static int ordinalIndexOf(String regex, String str, int ordinal) {
		Matcher m = Pattern.compile(regex).matcher(str);
		
		for (int i = 0; m.find() && i <= ordinal; i++) {
			if (i == ordinal) {
				return m.start();
			}
		}
		return -1;
	}
}
