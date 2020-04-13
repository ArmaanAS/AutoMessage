package com.dash.utils;

import org.apache.commons.lang3.StringUtils;

import com.dash.core.Main;
import com.dash.core.StateHandler;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;

public enum Chat {
	GLOBAL, TEAM, PARTY, STANDARD, MESSAGE, FRIENDMESSAGE, OTHER;
	
	public static Chat getChatOutput(String msg) {
		char i = msg.charAt(0);
		if (i == '!') {
			return GLOBAL;
		} else if (i == '@') {
			return PARTY;
		} else if (i == '#') {
			return TEAM;
		} else if (i == '"'){
			return STANDARD;
		} else {
			Debug.chatWarn(i + " is not a valid message prefix. Defaulting to current chat.");
			return STANDARD;
		}
	}
	
	public static Chat getChatType(String line) {
		if (line.length() < 9) return OTHER;
		
		char i = line.charAt(1),
			 j = line.charAt(0);
		if (j == '[') {
			if (i == 'P' && line.charAt(8) != '[') {
				return PARTY;
			} else if (i == 'G') {
				return GLOBAL;
			} else if (i == 'T') {
				return TEAM;
			} else if (i == 'F') {
				if (line.charAt(9) == '[') return OTHER;
				// [Friend] Dashsmashing -> me:
				// [Friend] Me -> Dashsmashing:
				if (line.substring(11, 14).equals(" ->") || line.split(" ", 5)[3].equals("Me:")) {
					return FRIENDMESSAGE;
				}
			}
		} else if (line.matches("^(Me -> :\\w+: \\w+|:\\w+: \\w+ -> Me): .*")) {
			return MESSAGE;
		} else if (j == ':' && StringUtils.countMatches(line, ":") != 2) {
			return STANDARD;
		}
		
		return OTHER;
	}
	
	public static String getRankMainChat(String line) {
		if (line.charAt(0) != '[') {
			String rawRank = line.split(" ", 2)[0];
			return rawRank.substring(1, rawRank.length()-1);
		} else return "";
	}
	
	private static String getNameMainChat(String line) {
		String[] split = line.split(" ", 4);
		if (split[1].length() == 1) {
			return split[2];
		} else if (split[1].endsWith(":")) {
			return split[1].substring(0, split[1].length()-1);
		} else return split[1];
	}
	
	// PTGM = PartyTeamGlobalMessage
	private static String getNamePTGMChat(String line) {
		String[] split = line.split(" ", 3);
		return split[1].substring(0, split[1].length()-1);
	}
	
	private static String getNameFriendChat(String line) {
		String[] split = line.split("[ :]", 5);
		if (split[1].equals("Me")) {
			return split[3];
		} else {
			return split[1];
		}
	}
	
	public static String getName(String line) {
		return getName(line, Chat.getChatType(line));
	}
	
	public static String getName(String line, Chat type) {
		if (type == Chat.OTHER) {
			return "";
		} else if (type == Chat.STANDARD) {
			return getNameMainChat(line);
		} else if (type == Chat.FRIENDMESSAGE) {
			return getNameFriendChat(line);
		} else {
			return getNamePTGMChat(line);
		}
	}
	
	public static void out(String msg) {
		if (The5zigAPI.getAPI().isInWorld()) {
			The5zigAPI.getAPI().messagePlayer(msg);
		}
	}
	
	public static void fakePartyMessage(String msg) {
		The5zigAPI.getAPI().messagePlayer(ChatColor.GRAY + 
			"[" + ChatColor.LIGHT_PURPLE + "Party" + 
			ChatColor.GRAY + "] " + ChatColor.DARK_GRAY +
			The5zigAPI.getAPI().getGameProfile().getName() + 
			ChatColor.GRAY + ": " + ChatColor.YELLOW + 
			ChatColor.translateAlternateColorCodes('&', msg)
		);
	}
	
	public static void fakeGlobalMessage(String msg) {
		The5zigAPI.getAPI().messagePlayer(ChatColor.GRAY + 
			"[" + ChatColor.LIGHT_PURPLE + "Party" + 
			ChatColor.GRAY + "] " + ChatColor.DARK_GRAY +
			The5zigAPI.getAPI().getGameProfile().getName() + 
			ChatColor.GRAY + ": " + ChatColor.YELLOW + 
			ChatColor.translateAlternateColorCodes('&', msg)
		);
	}
}
