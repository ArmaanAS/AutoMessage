package com.dash.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
	
	private static LinkedHashMap<String, Integer> sortHashMapByValues(
	        HashMap<String, Integer> passedMap) {
	    List<String> mapKeys = new ArrayList<>(passedMap.keySet());
	    List<Integer> mapValues = new ArrayList<>(passedMap.values());
	    Collections.sort(mapValues);
	    Collections.sort(mapKeys);

	    LinkedHashMap<String, Integer> sortedMap =
	        new LinkedHashMap<>();
	    
	    Iterator<Integer> valueIt = mapValues.iterator();
	    while (valueIt.hasNext()) {
	        int val = valueIt.next();
	        Iterator<String> keyIt = mapKeys.iterator();

	        while (keyIt.hasNext()) {
	            String key = keyIt.next();
	            int comp1 = passedMap.get(key);
	            int comp2 = val;

	            if (comp1 == comp2) {
	                keyIt.remove();
	                sortedMap.put(key, val);
	                break;
	            }
	        }
	    }
	    
	    return sortedMap;
	}
	
	public static List<String> getLines() {
		try {
			HashMap<String, Integer> side = sortHashMapByValues(
					The5zigAPI.getAPI().getSideScoreboard().getLines()
			);

			List<String> lines = new ArrayList<String>();
		    side.forEach((k, v) -> lines.add(k));
		    
		    Collections.reverse(lines);
			return lines;
			
		} catch (NullPointerException e) {
			return Collections.emptyList();
		}
	}
	
	public static boolean linesContain(String msg) {
		for (String i : getLines()) {
			if (i.contains(msg)) {
				return true;
			}
		}
		
		return false;
	}	
}
