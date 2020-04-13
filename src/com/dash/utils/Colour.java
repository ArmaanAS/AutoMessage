package com.dash.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dash.core.StateHandler;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;

public enum Colour {
	
	RESET('r'), BOLD('l'), ITALIC('o'), MAGIC('k'), UNDERLINE('n'), 
	STRIKETHROUGH('m'), FANCY('v'), RAINBOW('x'), FADE('y'), DIP('z'), WAVE('w');
	
	private static String 
		codes = "(?i)&[0-9a-f]",
		customCodes = "(?i)&([vx]|[wyz]([0-4]|\\[[0-9a-fklmno]+\\]))",
		advancedCodes = "(?i)&[wyz]([0-4]|\\[[0-9a-fklmno]+\\])",
		effectCodes = "(?i)&[klmnov]",
		breakCodes = "(?i)&([0-9a-fxr]|[wyz]([0-4]|\\[[0-9a-fklmno]+\\]))",
		allCodes = "(?i)&([0-9a-frklmnovx]|[wyz]([0-4]|\\[[0-9a-fklmno]+\\]))";
	private static boolean 
		init = false, 
		recBow = false, 
		recFade = false, 
		recDip = false,
		recWave = false;
	private char code;
	
	Colour(char i) {
		this.code = i;
	}
	
	public String toString() {
		return "&" + code;
	}
	
	public static String recolour(String msg) {
		if (!init) init();
		msg = msg.replace('§', '&');
		
		if (msg.contains(FANCY.toString())) msg = fancify(msg);
		if (!StateHandler.ranked) return msg.replaceAll(allCodes, "");
//		if (!msg.matches("(?i).*&[\\d\\w].*")) return msg;
		if (!msg.matches("(?s).*" + customCodes + ".*")) return msg;
		
		if (msg.contains(RAINBOW.toString())) msg = colourRainbow(msg);
		if (msg.contains(FADE.toString())) msg = colourFade(msg);
		if (msg.contains(DIP.toString())) msg = colourDip(msg);
		if (msg.contains(WAVE.toString())) msg = colourWave(msg);
		
		return msg;
		//return StateHandler.ranked ? msg : msg.replaceAll(codes, "");
	}
	
	public static String forceRecolour(String msg) {
		if (!init) init();
		msg = msg.replace('§', '&');
		
//		if (!msg.matches("(?i).*&[\\d\\w].*")) return msg;
		if (!msg.matches("(?s).*" + customCodes + ".*")) return msg;
		
		if (msg.contains(FANCY.toString())) msg = fancify(msg);
		if (msg.contains(RAINBOW.toString())) msg = colourRainbow(msg);
		if (msg.contains(FADE.toString())) msg = colourFade(msg);
		if (msg.contains(DIP.toString())) msg = colourDip(msg);
		if (msg.contains(WAVE.toString())) msg = colourWave(msg);
		
		return msg;
		//return StateHandler.ranked ? msg : msg.replaceAll(codes, "");
	}
	
	public static ChatColor pingColour(int ping) {
		if (ping <= 0) {
			return ChatColor.GRAY;
		} else if (ping < 45) {
			return ChatColor.AQUA;
		} else if (ping < 80) {
			return ChatColor.GREEN;
		} else if (ping < 120) {
			return ChatColor.YELLOW;
		} else if (ping < 160) {
			return ChatColor.GOLD;
		} else if (ping < 200) {
			return ChatColor.RED;
		} else if (ping < 1000) {
			return ChatColor.DARK_RED;
		} else {
			return ChatColor.DARK_PURPLE;
		}
	}
	
	public static String ping(int ping) {
		return pingColour(ping) + "" + ping;
	}
	
	public static String num(int n) {
		return numColour(n) + n;
	}
	
	public static String numColour(int n) {
		if (n == 0) {
			return ChatColor.DARK_GRAY.toString();
		} else if (n == 1) {
			return ChatColor.GRAY.toString();
		} else if (n == 2) {
			return ChatColor.DARK_BLUE.toString();
		} else if (n == 3) {
			return ChatColor.YELLOW.toString();
		} else if (n == 4) {
			return ChatColor.AQUA.toString();
		} else if (n == 5) {
			return ChatColor.GREEN.toString();
		} else if (n == 6) {
			return ChatColor.DARK_PURPLE.toString();
		} else if (n == 7) {
			return ChatColor.DARK_AQUA.toString();
		} else if (n == 8) {
			return ChatColor.GOLD.toString();
		} else if (n == 9) {
			return ChatColor.DARK_RED.toString();
		} else if (n == 10) {
			return ChatColor.DARK_GREEN.toString();
		} else if (n == 11) {
			return ChatColor.RED.toString();
		} else if (n == 12) {
			return ChatColor.LIGHT_PURPLE.toString();
		} else if (n == 13) {
			return ChatColor.BLUE.toString();
		} else if (n == 14) {
			return ChatColor.WHITE.toString();
		} else {
			return ChatColor.BLACK.toString();
		} 
	}
	
	public static String rank(int rank) {
		return rankColour(rank) + String.format("#%s", rank+".");
	}
	
	public static String rankColour(int rank) {
		if (rank < 10) {
			return ChatColor.GREEN.toString() + ChatColor.BOLD.toString();
		} else if (rank < 100) {
			return ChatColor.YELLOW.toString() + ChatColor.BOLD.toString();
		} else {
			return ChatColor.YELLOW.toString();
		}
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
	
	public static String[] split2(String msg, String regex) {
		Matcher m = Pattern.compile(regex).matcher(msg);
		if (!m.find()) {
			return new String[] {msg};
		} else {
			int i = m.start();
			return new String[] {msg.substring(0, i), msg.substring(i)};
		}
	}
	
	public static String startsWith(String msg, String regex) {
		Matcher m = Pattern.compile(regex).matcher(msg);
		if (m.lookingAt()) {
			return m.group();
		}
		return "";
	}
	
	private static String fancify(String msg) {
		while (msg.toLowerCase().contains(FANCY.toString())) {
			String[] split = msg.split(FANCY.toString(), 2),
//					superSplit = split2(split[1], "(?=" + breakCodes + ")");
					superSplit = split2(split[1], "(?=&r)");
			String coloured = split[0],
				toColour = superSplit[0];
//			Debug.chatState("split[0] = " + split[0]);
//			Debug.chatState("split[1] = " + split[1]);
//			Debug.chatState("superSplit[0] = " + superSplit[0]);
//			if (superSplit.length > 1)
//				Debug.chatState("superSplit[1] = " + superSplit[1]);
			for (int i = 0; i < toColour.length(); i++) {
				char c = toColour.charAt(i);
				if (c == '&' && i+1 < toColour.length()) {
					String d = ""+toColour.charAt(i+1);
//					if (i+2 < toColour.length() && "wyz".contains(d)) {
//						char e = toColour.charAt(i+2);
//						if ("01234".contains(e+"")) {
//							coloured += "&" + d + "" + e;
//							i += 2;
//							continue;
//						} else if (e == '[') {
//							
//						}
//					} else if ("klmnov".contains(d)) {
//						coloured += '&' + d;
//						i++;
//						continue;
//					}
					if ("0123456789abcdefxklmnov".contains(d)) {
						coloured += '&' + d;
						i++;
						continue;
					} else {
						String l = startsWith(toColour.substring(i), advancedCodes);
						if (l.length() != 0) {
							coloured += l;
							i += l.length()-1;
							continue;
						}
					}
				}
				
				int ord = c;
				if (ord > 32 && ord < 127) {
					coloured += Character.toString((char) (256*255-32+ord));
				} else {
					coloured += c;
				}
			}
			
			if (superSplit.length > 1) {
				coloured += superSplit[1];
			}
			
			msg = coloured;
		}
		
		return msg;
	}
	
	public static String applyTrimmedScale(String msg, char[] col) {
		int len = msg.replaceAll("(?i)(&[klmnov]| )", "").length();
		String coloured = "";
		
		float step = (float) (col.length-1)/len,
			  c = step/2;
		
		String effects = "", prev = "";
		for (int i = 0; i < msg.length(); i++) {
			char a = msg.charAt(i);
			if (a == ' ') {
				coloured += ' ';
				continue;
			} else if (a == '&') {
				String b = "" + msg.charAt(i+1);
				if ("klmnov".contains(b)) {
					effects += '&' + b;
					i++;
					continue;
				}
			}
			
			if (!(col[Math.round(c)] + effects).equals(prev)) {
				coloured += "&" + col[Math.round(c)] + effects + a;
				
				prev = col[Math.round(c)] + effects;
			} else {
				coloured += a;
			}
			c += step;
			
		}
		
		return coloured;
	}
	
	public static String applyUniformScale(String msg, char[] col) {
		int len = msg.replaceAll("(?i)(&[klmnov]| )", "").length();
		String coloured = "";
		
		float step = (float) col.length/len,
				c = 0;
		
		String effects = "", prev = "";
		for (int i = 0; i < msg.length(); i++) {
			char a = msg.charAt(i);
			if (a == ' ') {
				coloured += ' ';
				continue;
			} else if (a == '&') {
				String b = "" + msg.charAt(i+1);
				if ("klmnov".contains(b)) {
					effects += '&' + b;
					i++;
					continue;
				}
			}
			
//			coloured += "&" + col[(int) Math.floor(c)] + effects + a;
			if (!(col[(int) Math.floor(c)] + effects).equals(prev)) {
				coloured += "&" + col[(int) Math.floor(c)] + effects + a;
				
				prev = col[(int) Math.floor(c)] + effects;
			} else {
				coloured += a;
			}
			c += step;
		}
		
		return coloured;
	}
	
	public static String applyRepeatingScale(String msg, char[] col) {
		String coloured = "";
		
		int c = 0;
		String effects = "";
		for (int i = 0; i < msg.length(); i++) {
			char a = msg.charAt(i);
			if (a == ' ') {
				coloured += ' ';
				continue;
			} else if (a == '&') {
				String b = "" + msg.charAt(i+1);
				if ("klmnov".contains(b)) {
					effects += '&' + b;
					i++;
					continue;
				}
			}
			
			coloured += "&" + col[c] + effects + a;
			c = ++c % col.length;
		}
		
		return coloured;
	}
	
	private static char[] bow = "4c6eab9d5".toCharArray();
	private static String colourRainbow(String msg) {
		while (msg.toLowerCase().contains(RAINBOW.toString())) {
			String[] split = msg.split(RAINBOW.toString(), 2),
				superSplit = split2(split[1], "(?=" + breakCodes + ")");
			String coloured = split[0],
				toColour = superSplit[0];
			int len = toColour.replaceAll(effectCodes, "").length();
			
			if (len < bow.length && !recBow) {
				recommend("Rainbow", 6);
				recBow = true;
			}
						
			coloured += applyRepeatingScale(toColour, bow);
			
			if (superSplit.length > 1) {
				coloured += superSplit[1];
			}
			
			msg = coloured;
		}
		
		return msg;
	}
	
	private static char[] 
		redFade = "4c6ef".toCharArray(),
		greenFade = "2af".toCharArray(),
		blueFade = "193bf".toCharArray(),
		blackFade = "087f".toCharArray(),
		purpleFade = "5df".toCharArray();
	private static String colourFade(String msg) {
		while (msg.toLowerCase().contains(FADE.toString())) {
			String[] split = msg.split(FADE.toString(), 2),
				superSplit = split[1].split("(?=" + breakCodes + ")", 2);
			String coloured = split[0],
					toColour;
			
			char c = superSplit[0].charAt(0);
			char[] col;
			
			if (c == '[') {
				col = superSplit[0].split("\\]")[0].substring(1).toCharArray();
				toColour = superSplit[0].substring(2+col.length);
			} else {
				toColour = superSplit[0].substring(1);
				col = blackFade;
				switch (c) {
					case '0': break;
					case '1': col = blueFade; break;
					case '2': col = greenFade; break;
					case '3': col = redFade; break;
					case '4': col = purpleFade; break;
					default:  Debug.chatError("Unsupported Colour code: " + 
							superSplit[0].charAt(0));
						break;
				}
			}
			
			if (toColour.length() < col.length && !recFade) {
				recommend("Fade", col.length);
				recFade = true;
			}
			
			coloured += applyUniformScale(toColour, col);
			
			if (superSplit.length > 1) {
				coloured += superSplit[1];
			}
			
			
			msg = coloured;
		}
		
		return msg;
	}
	
	private static char[] 
			blackDip = "087f780".toCharArray(),
			blueDip = "193bfb391".toCharArray(),
			greenDip = "2afa2".toCharArray(),
			redDip = "4c6efe6c4".toCharArray(),
			purpleDip = "5dfd5".toCharArray();	
	private static String colourDip(String msg) {
		while (msg.toLowerCase().contains(DIP.toString())) {
			String[] split = msg.split(DIP.toString(), 2),
				superSplit = split[1].split("(?=" + breakCodes + ")", 2);
			String coloured = split[0],
				toColour;
			
			char c = superSplit[0].charAt(0);
			char[] col;
			
			if (c == '[') {
				String t = superSplit[0].split("\\]")[0].substring(1);
				toColour = superSplit[0].substring(2+t.length());
				t += new StringBuilder(t).reverse().toString().substring(1);
				col = t.toCharArray();
			} else {
				toColour = superSplit[0].substring(1);
				col = blackDip;
				switch (superSplit[0].charAt(0)) {
					case '0': break;
					case '1': 	col = blueDip; break;
					case '2': 	col = greenDip; break;
					case '3': 	col = redDip; break;
					case '4': 	col = purpleDip; break;
					default: 	Debug.chatError("Unsupported Colour code: " + 
									superSplit[0].charAt(0));
								break;
				}
			}
			
			if (toColour.length() < col.length && !recDip) {
				recommend("Dip", col.length);
				recDip = true;
			}
			
			coloured += applyTrimmedScale(toColour, col);
			
			if (superSplit.length > 1) {
				coloured += superSplit[1];
			}
			
			msg = coloured;
		}
		
		return msg;
	}
	
	private static char[] 
			blackWave = "087f78".toCharArray(),
			blueWave = "193bfb39".toCharArray(),
			greenWave = "2afa".toCharArray(),
			redWave = "4c6efe6c".toCharArray(),
			purpleWave = "5dfd".toCharArray();	
	private static String colourWave(String msg) {
		while (msg.toLowerCase().contains(WAVE.toString())) {
			String[] split = msg.split(WAVE.toString(), 2),
					superSplit = split[1].split("(?=" + breakCodes + ")", 2);
			String coloured = split[0],
					toColour;
			
			char c = superSplit[0].charAt(0);
			char[] col;
			
			if (c == '[') {
				col = superSplit[0].split("\\]")[0].substring(1).toCharArray();
				toColour = superSplit[0].substring(2+col.length);
			} else {
				toColour = superSplit[0].substring(1);
				col = blackWave;
				switch (superSplit[0].charAt(0)) {
					case '0': break;
					case '1': 	col = blueWave; break;
					case '2': 	col = greenWave; break;
					case '3': 	col = redWave; break;
					case '4': 	col = purpleWave; break;
					default: 	Debug.chatError("Unsupported Colour code: " + 
							superSplit[0].charAt(0));
					break;
				}
			}
			
			if (toColour.length() < col.length && !recWave) {
				recommend("Wave", col.length);
				recWave = true;
			}
			
			coloured += applyRepeatingScale(toColour, col);
			
			if (superSplit.length > 1) {
				coloured += superSplit[1];
			}
			
			msg = coloured;
		}
		
		return msg;
	}
}
