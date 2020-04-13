package com.dash.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import org.h2.util.StringUtils;

import eu.the5zig.util.minecraft.ChatColor;

public class Font {
	private static HashMap<Character, Integer> width = new HashMap<>();
	static {
		width.put(' ', 4);
		width.put('!', 2);
		width.put('"', 5);
		width.put('#', 6);
		width.put('$', 6);
		width.put('%', 6);
		width.put('&', 6);
		width.put('\'', 3);
		width.put('(', 5);
		width.put(')', 5);
		width.put('*', 5);
		width.put('+', 6);
		width.put(',', 2);
		width.put('-', 6);
		width.put('.', 2);
		width.put('/', 6);
		width.put('0', 6);
		width.put('1', 6);
		width.put('2', 6);
		width.put('3', 6);
		width.put('4', 6);
		width.put('5', 6);
		width.put('6', 6);
		width.put('7', 6);
		width.put('8', 6);
		width.put('9', 6);
		width.put(':', 2);
		width.put(';', 2);
		width.put('<', 5);
		width.put('=', 6);
		width.put('>', 5);
		width.put('?', 6);
		width.put('@', 7);
		width.put('A', 6);
		width.put('B', 6);
		width.put('C', 6);
		width.put('D', 6);
		width.put('E', 6);
		width.put('F', 6);
		width.put('G', 6);
		width.put('H', 6);
		width.put('I', 4);
		width.put('J', 6);
		width.put('K', 6);
		width.put('L', 6);
		width.put('M', 6);
		width.put('N', 6);
		width.put('O', 6);
		width.put('P', 6);
		width.put('Q', 6);
		width.put('R', 6);
		width.put('S', 6);
		width.put('T', 6);
		width.put('U', 6);
		width.put('V', 6);
		width.put('W', 6);
		width.put('X', 6);
		width.put('Y', 6);
		width.put('Z', 6);
		width.put('[', 4);
		width.put('\\', 6);
		width.put(']', 4);
		width.put('^', 6);
		width.put('_', 6);
		width.put('a', 6);
		width.put('b', 6);
		width.put('c', 6);
		width.put('d', 6);
		width.put('e', 6);
		width.put('f', 5);
		width.put('g', 6);
		width.put('h', 6);
		width.put('i', 2);
		width.put('j', 6);
		width.put('k', 5);
		width.put('l', 3);
		width.put('m', 6);
		width.put('n', 6);
		width.put('o', 6);
		width.put('p', 6);
		width.put('q', 6);
		width.put('r', 6);
		width.put('s', 6);
		width.put('t', 4);
		width.put('u', 6);
		width.put('v', 6);
		width.put('w', 6);
		width.put('x', 6);
		width.put('y', 6);
		width.put('z', 6);
		width.put('{', 5);
		width.put('|', 2);
		width.put('}', 5);
		width.put('~', 7);
	}
	
	private static int widthOf(char c) {
		if (width.containsKey(c)) {
			return width.get(c);
		}
		return 6;
	}
	
	private static String spaces(int w) {
		return spaces(w, "&r");
	}
	
	private static String spaces(int w, String col) {
		String r = "";
		for (int i = 0; i < w; i++) {
			r += ' ';
			if (i%3 == 2) r += col;
		}
		
		return r;
	}
	
	private static boolean isBold(String msg) {
		boolean bold = false;
		
		for (int i = 0; i < msg.length(); i++) {
			char c = msg.charAt(i);
			if (c == '&') {
				char d = msg.charAt(i+1);
				if (d == 'l') {
					bold = true;
					i++;
					continue;
				} else if ("0123456789abcdefr".contains(""+d)) {
					bold = false;
					i++;
					continue;
				} else if ("onmk".contains(""+d)) {
					i++;
					continue;
				}
			}
		}
		return bold;
	}
	
	public static String ljustLog(String msg, int width) {
		int w = getWidth(msg);
		if (w >= width) {
			Debug.chatDebug(
					"Msg: " + msg + ChatColor.GRAY + 
					" Spaces: " + (width-w)/4 + 
					" Width: " + w + 
					" Min: " + width
			);
			return msg;
		}
//		if ((width-w)%4 == 1 && (width-w) >= 5) {
//			if (isBold(msg)) {
//				msg += ' ';
//			} else {
//				msg += ChatColor.BOLD.toString() + ' ';
//			}
//			w += 5;
//		} else if ((width-w)%4 == 2 && (width-w) >= 10) {
//			if (isBold(msg)) {
//				msg += "  ";
//			} else {
//				msg += ChatColor.BOLD + "  ";
//			}
//			w += 10;
//		} else if ((width-w)%4 == 3 && (width-w) >= 15) {
//			if (isBold(msg)) {
//				msg += "   ";
//			} else {
//				msg += ChatColor.BOLD + "    ";
//			}
//			w += 15;
//		}
		boolean bold = false;
		while (width-w >= 5 && (width-w)%4!=0) {
			if (!bold) {
				msg += ChatColor.BOLD;
				bold = true;
			}
			msg += ' ';
			w += 5;
		}
		Debug.chatDebug(
				"Msg: " + msg + ChatColor.GRAY + 
				" Spaces: " + (width-w)/4 + 
				" Width: " + w + 
				" Min: " + width
		);
		Debug.chatDebug("\"" + msg + spaces((width-w)/4) + ChatColor.GRAY + "\" newWidth: " + getWidth(msg + "&r" + spaces((width-w)/4)));
		
		return msg + ChatColor.RESET + spaces((width-w)/4);
	}
	
	public static String ljust(String msg, int width) {
		return ljust(msg, width, "");
	}
	
	public static String ljust(String msg, int width, String reset) {
		int w = getWidth(msg);
		if (w >= width) {
			return msg;
		}
		boolean bold = false;
		while (width-w >= 5 && (width-w)%4!=0) {
			if (!bold) {
				msg += ChatColor.BOLD;
				bold = true;
			}
			msg += ' ';
			w += 5;
		}
		return msg + ChatColor.RESET + spaces((width-w)/4) + reset;
	}
	
	public static String rjust(String msg, int width, String col) {
		int w = getWidth(msg);
		if (w >= width) {
			return msg;
		}
		String space = "";
		boolean bold = false;
		while (width-w >= 5 && (width-w)%4!=0) {
			if (!bold) {
				space += ChatColor.BOLD;
				bold = true;
			}
			space += ' ';
			w += 5;
		}
		return space + col + spaces((width-w)/4, col) + msg;
	}
	
	public static String just(String msg, int width) {
		int w = getWidth(msg);
		if (w >= width) {
			return msg;
		}
		boolean bold = false;
		while (width-w >= 5 && (width-w)%4!=0) {
			if (!bold) {
				msg += ChatColor.BOLD;
				bold = true;
			}
			msg += ' ';
			w += 5;
		}
		return ChatColor.RESET + spaces((width-w)/4);
	}
	
	public static int getWidth(String msg) {
		msg = Colour.recolour(msg);
		List<Character> reg = new ArrayList<>();
		List<Character> bld = new ArrayList<>();
		boolean bold = false;
		
		for (int i = 0; i < msg.length(); i++) {
			char c = msg.charAt(i);
			if (c == '&') {
				char d = msg.charAt(i+1);
				if (d == 'l') {
					bold = true;
					i++;
					continue;
				} else if ("0123456789abcdefr".contains(""+d)) {
					bold = false;
					i++;
					continue;
				} else if ("onmk".contains(""+d)) {
					i++;
					continue;
				}
			}
			
			if (bold) {
				bld.add(c);
			} else {
				reg.add(c);
			}
		}
		
		return getBoldWidth(bld) + getRawWidth(reg);
	}
	
	public static int getBoldWidth(List<Character> msg) {
		if (msg.size() == 0) return 0;
		return IntStream.range(0, msg.size()).map(i -> widthOf(msg.get(i))+1).sum();
	}
	
	public static int getRawWidth(List<Character> msg) {
		if (msg.size() == 0) return 0;
		return IntStream.range(0, msg.size()).map(i -> widthOf(msg.get(i))).sum();		
	}
}
