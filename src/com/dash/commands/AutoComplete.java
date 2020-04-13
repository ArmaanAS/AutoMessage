package com.dash.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

import org.lwjgl.input.Keyboard;

import com.dash.core.StateHandler;
import com.dash.utils.Debug;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.The5zigMod;

public class AutoComplete {
	public static boolean isTabDown = false;
	public static String[] completes = new String[0];
	public static int c = -1;
	public static String prev = "";
	
	private static Method getChatField, setChatText;
	private static Object bjc;
	static {
		try {
			getChatField = The5zigMod.getVars().getClass()
					.getDeclaredMethod("getChatField");
			getChatField.setAccessible(true);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	private static void getChatField() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		bjc = getChatField.invoke(The5zigMod.getVars());
		if (setChatText == null) {
			try {
				setChatText = bjc.getClass().getDeclaredMethod("a", String.class);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void setChatText(String msg) {
		if (!The5zigMod.getVars().isChatOpened()) {
			The5zigMod.getVars().typeInChatGUI("");
		}
		try {
			getChatField();
			setChatText.invoke(bjc, msg);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public static String getChatText() {
		if (!The5zigMod.getVars().isChatOpened()) {
			return "";
		}
		return The5zigMod.getVars().getChatBoxText();
	}
	
	public static boolean isChatOpen() {
		return The5zigMod.getVars().isChatOpened();
	}
	
	public static boolean isShifting() {
		return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || 
				Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
	}
	
	
	public static void onChatType() {
		if (!isTabDown) {
			if (Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
				String msg = getChatText();
				
				if (!msg.equals(prev)) {
					completes = complete();	
					c = -1;
				}
				
				if (completes.length > 0) {
					if (c == -1) {
						The5zigAPI.getAPI().messagePlayerInSecondChat(String.join(", ", completes));
						c = 0;
					} else {
						if (isShifting()) {
							c = (c-1+completes.length) % completes.length;
						} else {
							c = (c+1) % completes.length;
						}
					}
					msg = msg.substring(0, msg.lastIndexOf(" ")+1) + completes[c];
					setChatText(msg);
				}
				prev = msg;
			}
		}
		
		isTabDown = Keyboard.isKeyDown(Keyboard.KEY_TAB);
	}
	
	private static String[] complete() {
		String rmsg = getChatText(),
				msg = rmsg.toLowerCase();
		String[] tokens, t = msg.split("\\s+");
		if (msg.endsWith(" ")) {
			t = Arrays.copyOf(t, t.length+1);
			t[t.length-1] = "";
		}
		tokens = t;
		
		if (tokens[0].equals("/lb")) {
			if (tokens.length == 1) {
				return Command.cmds[1].getAutoComplete();
			} else if (tokens.length == 2) {
				return Command.cmds[1].getAutoComplete(tokens[1]);				
			} else if (tokens.length == 3) {
				if (tokens[1].startsWith("f") || tokens[1].equals("top") || tokens[1].equals("min")) {
					return Command.cmds[1].sub[2].getAutoComplete();
				} else if (tokens[1].startsWith("l")) {
					return The5zigAPI.getAPI().getServerPlayers().stream()
							.map(p -> p.getGameProfile().getName())
							.filter(n -> n.toLowerCase().startsWith(tokens[2]))
							.toArray(String[]::new);
				}
			}
		} else 
		if (tokens[0].equals("/agg")) {
			if (tokens.length == 1) {
				return Command.cmds[2].getAutoComplete();
			} else if (tokens.length == 2) {
				return Command.cmds[2].getAutoComplete(tokens[1]);
			}
			
			
		} else if (tokens[0].equals("/ping")) {
			if (tokens.length == 2) {
				return The5zigAPI.getAPI().getServerPlayers().stream()
						.map(p -> p.getGameProfile().getName())
						.filter(n -> n.toLowerCase().startsWith(tokens[1]))
						.toArray(String[]::new);
			}
			
			
		} else if (tokens[0].equals("/rep")) {
			if (tokens.length == 2) {
				return The5zigAPI.getAPI().getServerPlayers().stream()
						.map(p -> p.getGameProfile().getName())
						.filter(n -> n.toLowerCase().startsWith(tokens[1]))
						.toArray(String[]::new);
			}
			
			
		} else if (tokens[0].equals("/names")) {
			if (tokens.length == 2) {
				return The5zigAPI.getAPI().getServerPlayers().stream()
						.map(p -> p.getGameProfile().getName())
						.filter(n -> n.toLowerCase().startsWith(tokens[1]))
						.toArray(String[]::new);
			}

			
		} else if (tokens[0].equals("/p")) {
//			Debug.chatDebug("Tokens: " + tokens.length);
//			Debug.chatDebug("\"" + msg + "\"");
			if (tokens.length == 2) {
				return Stream.of("reinv", "remove")
						.filter(c -> c.startsWith(tokens[1]))
						.toArray(String[]::new);
			} else if (tokens.length == 3 && tokens[1].equals("reinv")) {				
				Debug.chatDebug(StateHandler.partyPlayers.toString());
				return StateHandler.partyPlayers.stream()
						.filter(n -> n.toLowerCase().startsWith(tokens[2]))
						.toArray(String[]::new);
			}
		}
		
		return new String[0];
	}
}
