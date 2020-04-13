package com.dash.utils;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;

public class Debug {
	
	public static void chatDebug(String message) {
		message = ChatColor.translateAlternateColorCodes('&', message);
		if (The5zigAPI.getAPI().isInWorld()) {
			The5zigAPI.getAPI().messagePlayer(
				ChatColor.GRAY + "[" + 
				ChatColor.GREEN + "AutoGG" + 
				ChatColor.GRAY + "]" + 
				ChatColor.DARK_GRAY + " Debug" +
				ChatColor.WHITE + ": " + 
				ChatColor.GRAY + message
			);
		} else {
			The5zigAPI.getLogger().warn("[AutoGG] Debug: " + message);
		}
	}
	
	public static void chatInfo(String message) {
		message = ChatColor.translateAlternateColorCodes('&', message);
		if (The5zigAPI.getAPI().isInWorld()) {
			The5zigAPI.getAPI().messagePlayer(
				ChatColor.GRAY + "[" + 
				ChatColor.GREEN + "AutoGG" + 
				ChatColor.GRAY + "]" + 
				ChatColor.WHITE + " Info" +
				ChatColor.WHITE + ": " + 
				ChatColor.GRAY + message
			);
		} else {
			The5zigAPI.getLogger().warn("[AutoGG] Info: " + message);
		}
	}
	
	public static void chatState(String message) {
		message = ChatColor.translateAlternateColorCodes('&', message);
		if (The5zigAPI.getAPI().isInWorld()) {
			The5zigAPI.getAPI().messagePlayer(
				ChatColor.GRAY + "[" + 
				ChatColor.GREEN + "AutoGG" + 
				ChatColor.GRAY + "]" + 
				ChatColor.AQUA + " State" +
				ChatColor.WHITE + ": " + 
				ChatColor.GRAY + message
			);
		} else {
			The5zigAPI.getLogger().warn("[AutoGG] State: " + message);
		}
	}
	
	public static void chatWarn(String message) {
		message = ChatColor.translateAlternateColorCodes('&', message);
		if (The5zigAPI.getAPI().isInWorld()) {
			The5zigAPI.getAPI().messagePlayer(
				ChatColor.GRAY + "[" + 
				ChatColor.GREEN + "AutoGG" + 
				ChatColor.GRAY + "]" + 
				ChatColor.GOLD + " Warning" +
				ChatColor.WHITE + ": " + 
				ChatColor.YELLOW + message
			);
		} else {
			The5zigAPI.getLogger().warn("[AutoGG] Warn: " + message);
		}
	}
	
	public static void chatError(String message) {
		message = ChatColor.translateAlternateColorCodes('&', message);
		if (The5zigAPI.getAPI().isInWorld()) {
			The5zigAPI.getAPI().messagePlayer(
				ChatColor.GRAY + "[" + 
				ChatColor.GREEN + "AutoGG" + 
				ChatColor.GRAY + "]" + 
				ChatColor.DARK_RED + " Error" +
				ChatColor.WHITE + ": " + 
				ChatColor.RED + message
			);
		} else {
			The5zigAPI.getLogger().warn("[AutoGG] Error: " + message);
		}
	}
	
	public static void chatSuccess(String message) {
		message = ChatColor.translateAlternateColorCodes('&', message);
		if (The5zigAPI.getAPI().isInWorld()) {
			The5zigAPI.getAPI().messagePlayer(
				ChatColor.GRAY + "[" + 
				ChatColor.GREEN + "AutoGG" + 
				ChatColor.GRAY + "]" + 
				ChatColor.DARK_GREEN + " Success" +
				ChatColor.WHITE + ": " + 
				ChatColor.GREEN + message
			);
		} else {
			The5zigAPI.getLogger().warn("[AutoGG] Success: " + message);
		}
	}
	
	public static void chatRecommended(String message) {
		message = ChatColor.translateAlternateColorCodes('&', message);
		if (The5zigAPI.getAPI().isInWorld()) {
			The5zigAPI.getAPI().messagePlayer(
				ChatColor.GRAY + "[" + 
				ChatColor.GREEN + "AutoGG" + 
				ChatColor.GRAY + "]" + 
				ChatColor.LIGHT_PURPLE + " Recommended" +
				ChatColor.WHITE + ": " + 
				ChatColor.ITALIC.toString() + message
			);
		} else {
			The5zigAPI.getLogger().warn("[AutoGG] Recommended: " + message);
		}
	}
	
	public static void chatHelp(String message) {
		message = ChatColor.translateAlternateColorCodes('&', message);
		if (The5zigAPI.getAPI().isInWorld()) {
			The5zigAPI.getAPI().messagePlayer(
				ChatColor.GRAY + "[" + 
				ChatColor.GREEN + "AutoGG" + 
				ChatColor.GRAY + "]" + 
				ChatColor.DARK_GREEN + " Help" +
				ChatColor.WHITE + ": " + 
				ChatColor.GREEN + message
			);
		} else {
			The5zigAPI.getLogger().warn("[AutoGG] Recommended: " + message);
		}
	}
	
	public static void chatCustom(ChatColor col, String type, ChatColor mcol, String message) {
		message = ChatColor.translateAlternateColorCodes('&', message);
		if (The5zigAPI.getAPI().isInWorld()) {
			The5zigAPI.getAPI().messagePlayer(
					ChatColor.GRAY + "[" + 
							ChatColor.GREEN + "AutoGG" + 
							ChatColor.GRAY + "] " + 
							col + type +
							ChatColor.WHITE + ": " + 
							mcol + message
					);
		} else {
			The5zigAPI.getLogger().warn("[AutoGG] Recommended: " + message);
		}
	}
	
	public static void shadowChatDebug(String message) {
		message = ChatColor.translateAlternateColorCodes('&', message);
		if (The5zigAPI.getAPI().isInWorld()) {
			The5zigAPI.getAPI().messagePlayerInSecondChat(
					ChatColor.GRAY + "[" + 
					ChatColor.GREEN + "AutoGG" + 
					ChatColor.GRAY + "]" + 
					ChatColor.DARK_GRAY + " Debug" +
					ChatColor.WHITE + ": " + 
					ChatColor.GRAY + message
			);
		} else {
			The5zigAPI.getLogger().warn("[AutoGG] Debug: " + message);
		}
	}
	
	public static void shadowChatState(String message) {
		message = ChatColor.translateAlternateColorCodes('&', message);
		if (The5zigAPI.getAPI().isInWorld()) {
			The5zigAPI.getAPI().messagePlayerInSecondChat(
				ChatColor.GRAY + "[" + 
				ChatColor.GREEN + "AutoGG" + 
				ChatColor.GRAY + "]" + 
				ChatColor.AQUA + " State" +
				ChatColor.WHITE + ": " + 
				ChatColor.GRAY + message
			);
		} else {
			The5zigAPI.getLogger().warn("[AutoGG] State: " + message);
		}
	}
	
	public static void overHeadMessage(String message) {
		message = ChatColor.translateAlternateColorCodes('&', message);
		The5zigAPI.getAPI().createOverlay().displayMessage(
			ChatColor.GREEN + "AutoGG", 
			message
		);
	}
}
