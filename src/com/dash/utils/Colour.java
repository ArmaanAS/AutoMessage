package com.dash.utils;

import com.dash.core.StateHandler;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;

public enum Colour {
	
	RESET('r'), BOLD('l'), ITALIC('o'), MAGIC('k'), UNDERLINE('n'), 
	STRIKETHROUGH('m'), RAINBOW('x'), FADE('y'), DIP('z');
	
	private static String codes = "(?i)&[0-9a-f";
	private static boolean init = false;
	private char code;
	
	Colour(char i) {
		this.code = i;
	}
	
	private String getCode() {
		return "&" + code;
	}
	
	public static String recolour(String msg) {
		if (!init) init();
		
		if (!msg.matches("(?i).*&[\\d\\w].*")) return msg;
		if (!StateHandler.ranked) return msg.replaceAll(codes, "");
		
		if (msg.contains(RAINBOW.getCode())) msg = colourRainbow(msg);
		if (msg.contains(FADE.getCode())) msg = colourFade(msg);
		if (msg.contains(DIP.getCode())) msg = colourDip(msg);
		
		return msg;
		//return StateHandler.ranked ? msg : msg.replaceAll(codes, "");
	}
	
	private static void init() { init = true;
		for (Colour i : Colour.values()) {
			codes += i.code;
		}
		codes += "]";
		//Debug.chatDebug(codes);
	}
	
	private static void recommend(String name, int min) {
		Debug.chatRecommended(
			ChatColor.BOLD.toString() + name + 
			ChatColor.WHITE + ChatColor.ITALIC.toString() + 
			" effect works best when the message is over " + 
			ChatColor.GREEN + ChatColor.BOLD.toString() + min + 
			ChatColor.WHITE + ChatColor.ITALIC.toString() + " characters.");
	}
	
	private static char[] bow = {'4', 'c', '6', 'e', 'a', 'b', '9', 'd', '5'};
	private static String colourRainbow(String msg) {
		while (true) {
			String[] split = msg.split(RAINBOW.getCode(), 2),
				superSplit = split[1].split("(?=" + codes + ")", 2);
			String coloured = split[0],
				toColour = superSplit[0];
			float inc = toColour.length() < bow.length 
				? bow.length*1f/toColour.length() : 1f; 
			int offset = 0;
			
			for (int i = 0; i < toColour.length(); i++) {
				if (toColour.charAt(i) == ' ') {
					offset++;
					coloured += " ";
					continue;
				}
				int cIndex = Math.round((i - offset) * inc);
				coloured += "&" + bow[cIndex % bow.length] + toColour.charAt(i);
			}
			
			if (superSplit.length > 1) {
				coloured += "&r" + superSplit[1];
			}
			
			if (toColour.length() - offset < 5) {
				recommend("Rainbow", 5);
			}
			
			msg = coloured;
			
			if (!msg.toLowerCase().contains(RAINBOW.getCode())) break;
		}
		
		//The5zigAPI.getAPI().messagePlayerInSecondChat(ChatColor.translateAlternateColorCodes('&', msg));
		return msg;
	}
	
	private static char[] redFade = {'4', 'c', '6', 'e', 'f'},
		greenFade = {'2', 'a', 'f'},
		blueFade = {'1', '9', '3', 'b', 'f'},
		blackFade = {'0', '8', '7', 'f'};
	private static String colourFade(String msg) {
		while (true) {
			String[] split = msg.split(FADE.getCode(), 2),
				superSplit = split[1].split("(?=" + codes + ")", 2);
			String coloured = split[0],
				toColour = superSplit[0].substring(1);
			
			char[] col = blackFade;
			switch (superSplit[0].charAt(0)) {
				case '0': break;
				case '1': col = blueFade; break;
				case '2': col = greenFade; break;
				case '3': col = redFade; break;
			}
			
			float inc = 1f/col.length * toColour.length(); 
			int cIndex = 0;
			
			for (int i = 0; i < col.length; i++) {
				coloured += "&" + col[i] + 
					toColour.substring(
						cIndex, 
						(cIndex = (int) Math.floor((i+1) * inc))
				);
			}
			
			if (superSplit.length > 1) {
				coloured += "&r" + superSplit[1];
			}
			
			if (toColour.length() < col.length) {
				recommend("Fade", col.length);
			}
			
			msg = coloured;
			
			if (!msg.toLowerCase().contains(FADE.getCode())) break;
		}
		
		//The5zigAPI.getAPI().messagePlayerInSecondChat(ChatColor.translateAlternateColorCodes('&', msg));
		return msg;
	}
	
	private static char[] redDip = {'4', 'c', '6', 'e', 'f', 'e', '6', 'c', '4'},
			greenDip = {'2', 'a', 'f', 'a', '2'},
			blueDip = {'1', '9', '3', 'b', 'f', 'b', '3', '9', '1'},
			blackDip = {'0', '8', '7', 'f', '7', '8', '0'};	
	private static String colourDip(String msg) {
		while (true) {
			String[] split = msg.split(DIP.getCode(), 2),
				superSplit = split[1].split("(?=" + codes + ")", 2);
			String coloured = split[0],
				toColour = superSplit[0].substring(1);
			
			char[] col = blackDip;
			switch (superSplit[0].charAt(0)) {
				case '0': break;
				case '1': 	col = blueDip; break;
				case '2': 	col = greenDip; break;
				case '3': 	col = redDip; break;
				default: 	Debug.chatError("Unsupported Colour code: " + 
								superSplit[0].charAt(0));
							break;
			}
			
			float inc = 1f/col.length * toColour.length(); 
			int cIndex = 0;
			
			for (int i = 0; i < col.length; i++) {
				coloured += "&" + col[i] + 
					toColour.substring(
						cIndex, 
						(cIndex = (int) Math.floor((i+1) * inc))
				);
			}
			
			if (superSplit.length > 1) {
				coloured += "&r" + superSplit[1];
			}
			
			if (toColour.length() < col.length) {
				recommend("Dip", col.length);
			}
			
			msg = coloured;
			
			if (!msg.toLowerCase().contains(DIP.getCode())) break;
		}
		
		//The5zigAPI.getAPI().messagePlayerInSecondChat(ChatColor.translateAlternateColorCodes('&', msg));
		return msg;
	}
}
