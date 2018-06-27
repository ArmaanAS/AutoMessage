package com.dash.utils;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;

public class Debug {
	
	public static void chatDebug(String message) {
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
	
	public static void chatState(String message) {
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
	
	public static void chatError(String message) {
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
	
	public static void chatRecommended(String message) {
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
	
	public static void shadowChatDebug(String message) {
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
			The5zigAPI.getLogger().warn("[AutoGG] State: " + message);
		}
	}
	
	public static void shadowChatState(String message) {
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
		The5zigAPI.getAPI().createOverlay().displayMessage(
			ChatColor.GREEN + "AutoGG", 
			message
		);
	}
}
