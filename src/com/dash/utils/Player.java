package com.dash.utils;

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
	
}
