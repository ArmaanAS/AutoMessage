package com.dash.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.dash.utils.network.JsonReader;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.util.NetworkPlayerInfo;

public class Player {
	
	public static int getPing(String name) {
		int ping = -1;
		for (NetworkPlayerInfo i : The5zigAPI.getAPI().getServerPlayers()) {
			if (i.getGameProfile().getName().equals(name)) {
				ping = i.getPing();
				break;
			}
		}
		return ping;
	}
	
	public static String getUUID(String name) {
		name = name.toLowerCase();
		for (NetworkPlayerInfo p : The5zigAPI.getAPI().getServerPlayers()) {
			if (p.getGameProfile().getName().toLowerCase().equals(name)) {
				return p.getGameProfile().getId().toString().replaceAll("-", "");
			}
		}
		
	    try {
	    	JSONObject json = JsonReader.readJsonFromUrl("https://api.mojang.com/users/profiles/minecraft/" + name);
	    	
	    	return json.getString("id");
	    } catch (Exception e) {
	    	return "";
	    }
	}
	
	public static String[] getOldNames(String name) {
		List<String> names = new ArrayList<String>();
		try {
			String array = JsonReader.readUrl("https://api.mojang.com/user/profiles/" + getUUID(name) + "/names");
			array = array.substring(1, array.length()-1);
			String[] jsons = array.split("(?<=\\}),(?=\\{)");
			
			for (int i = 0; i < jsons.length-1; i++) {
				JSONObject json = new JSONObject(jsons[i]);
				String oldName = json.getString("name");
				
				if (!names.contains(oldName)) {				
					names.add(oldName);
				}
			}
		} catch (JSONException | IOException e) {
//			e.printStackTrace();
			return new String[0];
		}
		Collections.reverse(names);
		return names.toArray(new String[names.size()]);
	}	
}
