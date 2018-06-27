package com.dash.utils;

import java.util.Set;

import eu.the5zig.mod.The5zigAPI;

public class Sidebar {
	public static String getTitle() {
		try {
			return The5zigAPI.getAPI()
				.getSideScoreboard()
				.getTitle()
				.replaceAll("§[0-9a-zA-Z]",	"");
		} catch (NullPointerException e) {
			return "";
		}
	}	
	
	public static boolean linesContain(String msg) {
		try {
			Set<String> lines = The5zigAPI.getAPI()
				.getSideScoreboard().getLines().keySet();
			for (String i : lines) {
				if (i.contains(msg)) {
					return true;
				}
			}
			
			return false;
		} catch (NullPointerException e) {
			return false;
		}
	}	
}
