package com.dash.utils;

import com.dash.core.StateHandler;

public enum Chat {
	GLOBAL, TEAM, PARTY, STANDARD;
	
	public static Chat getChatOutput(String msg) {
		if (msg.startsWith("!")) {
			return GLOBAL;
		} else if (msg.startsWith("@")) {
			return PARTY;
		} else if (msg.startsWith("#")) {
			return TEAM;
		} else {
			return STANDARD;
		}
	}
}
