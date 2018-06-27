package com.dash.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.dash.message.Preset;
import com.dash.utils.Chat;
import com.dash.utils.Debug;
import com.dash.utils.DelayedSend;
import com.dash.utils.Sidebar;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.event.ChatEvent;
import eu.the5zig.mod.event.ChatSendEvent;
import eu.the5zig.util.minecraft.ChatColor;

public class StateHandler {
	
	public static boolean inParty = false, xinParty = false, 
			partyChat = false, xpartyChat = false,
			inGame = false, xinGame = false,
			muted = false, xmuted = false,
			ranked = true;
	public static final String[] 
			teamNames = {"Team", "BlockWars", "UHC"},
			soloNames = {"FFA", "Survival", "Quake", "Spleef", "Minerware", "Wing"},
			gameNames = {"Eggwars"};
	private static LinkedHashMap  postMute = new LinkedHashMap();
	private static String  previousMessage = "";
	private static Chat	   previousChat    = Chat.STANDARD;
	private static final boolean xD		   = true;
	private static long    lastMessage 	   = 0;
	
	public static void init() {
		inParty = xinParty = partyChat = xpartyChat =
		inGame = xinGame = muted = xmuted = false;
		ranked = true;
		postMute = new LinkedHashMap();
		previousMessage = "";
		previousChat    = Chat.STANDARD;
		lastMessage 	   = 0;
	}
	
	public static void log() {
		if (xD) return;
		if (inParty == xinParty) {
			Debug.chatState("inParty = " + inParty);
		} else {
			xinParty = inParty;
			Debug.chatState(ChatColor.BOLD + "inParty = " + inParty);
		}
		if (partyChat == xpartyChat) {
			Debug.chatState("partyChat = " + partyChat);
		} else {
			xpartyChat = partyChat;
			Debug.chatState(ChatColor.BOLD + "partyChat = " + partyChat);
		}
		if (inGame == xinGame) {
			Debug.chatState("inGame = " + inGame);
		} else {
			xinGame = inGame;
			Debug.chatState(ChatColor.BOLD + "inGame = " + inGame);
		}
		if (muted == xmuted) {
			Debug.chatState("muted = " + muted);
		} else {
			xmuted = muted;
			Debug.chatState(ChatColor.BOLD + "muted = " + muted);
		}
	}
	
	public static void correctMessage(ChatSendEvent event) { //Debug.chatDebug(event.getMessage());
		int marks = 0;
		String msg = event.getMessage();
		
		for (int i = 0; i < msg.length(); i++) {			
			if (msg.charAt(i) == '!') {
				marks++;
			} else {
				if (marks == 0) return;
				break;
			}
		}
		
		int global = 0;
		if (isTeamGame() && inGame) global++;
		if (partyChat) global++;
		
		if (marks > global) event.setMessage(msg.substring(marks - global));
	}
	
	@Deprecated
	public static void PC(boolean state) {
		StateHandler.log();
		if ((StateHandler.partyChat != state) && StateHandler.inParty) {
			The5zigAPI.getAPI().sendPlayerMessage("/pc");
			StateHandler.partyChat = !StateHandler.partyChat;
		}
	}
	
	private static void chatMessage(String msg, Chat type) {
		The5zigAPI.getAPI().sendPlayerMessage(msg);
		The5zigAPI.getLogger().info(
			"Sending " + type.toString().toLowerCase() + 
			" message... " + msg
		);
		messageSent();
		
		previousMessage = msg;
		previousChat = type;
	}
	
	public static void GlobalMessage(String msg) {		
		String prefix = "";
		
		if (partyChat) {
			if (!msg.startsWith("!")) {
				prefix = "!";
			}
		}
		
		if (isTeamGame() && inGame) {
			if (!msg.startsWith("!!")) {
				prefix += "!";
			}
		}
		
		chatMessage(prefix + msg, Chat.GLOBAL);
	}
	
	public static void StandardMessage(String msg) {
		chatMessage(msg, Chat.STANDARD);
	}
	
	public static void PartyMessage(String msg) {
		String prefix = "";
		
		if (inParty) {
			if (!msg.startsWith("@")) {
				prefix = "@";
			}
		}
		
		
		chatMessage(prefix + msg, Chat.PARTY);
	}
	
	public static void TeamMessage(String msg) {
		String prefix = "";
		
		if (partyChat) {
			if (!msg.startsWith("!")) {
				prefix = "!";
			}
		}
		
		chatMessage(prefix + msg, Chat.TEAM);
	}
	
	public static void sendMessage(String msg, Chat out, boolean delayed) {
		if (delayed) {
			DelayedSend.SendMessage(msg, out);
		} else {
			sendChatOutput(msg, out);
		}
	}
	
	private static void sendChatOutput(String msg, Chat out) {
		if (muted && !out.equals(Chat.PARTY)) {
			The5zigAPI.getLogger()
				.warn("Message not sent, you are muted: " + msg);
			postMute.put(msg, out);
			return;
		}
		
		switch (out)  {
			case GLOBAL:   StateHandler.GlobalMessage(msg); return;
			case PARTY:	   StateHandler.PartyMessage(msg); return;
			case STANDARD: StateHandler.StandardMessage(msg); return;
			case TEAM:     StateHandler.TeamMessage(msg); return;
		}
	}
	
	private static boolean isTeamGame() {
		String title = Sidebar.getTitle();
		
		for (String i : teamNames) {
			if (title.toLowerCase().contains(i.toLowerCase())) {
				//Debug.chatState("Team Game = " + true);
				return true;
			}
		}
		
		for (String i : soloNames) {
			if (title.toLowerCase().contains(i.toLowerCase())) {
				//Debug.chatState("Team Game = " + false);
				return false;
			}
		}
		
		if (Sidebar.linesContain("Map:")) {
			//Debug.chatState("Team Game = " + false);
			return false;
		}
		
		for (String i : gameNames) {
			//Debug.chatState("Team Game = " + true);
			return true;
		}
		
		return false;
	}
	
	
	
	public static void messageSent() {
		lastMessage = System.currentTimeMillis();
	}
	
	public static long timeSinceLastMessage() {
		return System.currentTimeMillis() - lastMessage;
	}
	
	public static void updateStates(ChatEvent event, String msg) {
		// Not in party
		if (Preset.PartyLeave1.matches(msg) ||
			Preset.PartyLeave2.matches(msg) ||
			Preset.PartyLeave3.matches(msg) ||
			Preset.PartyLeave4.matches(msg)
			) {
				inParty = partyChat = false;
				if (System.currentTimeMillis() - Main.time < 1000) {
					event.setCancelled(true);
				}
				log();
		}
		
		// Joined a party
		if (Preset.PartyAccept.matches(msg) ||
			Preset.PartyJoin.matches(msg)
			) {
				inParty = true;
				log();
		}
		
		// Party chat - enabled
		if (Preset.PartyEnable.matches(msg)) {
			inParty = partyChat = true;
			if (System.currentTimeMillis() - Main.time < 3000) {
				event.setCancelled(true);
				DelayedSend.SendMessage("/pc", Chat.STANDARD, 1000);
			}
			log();
		}
		
		// Party chat - disabled
		if (Preset.PartyDisable.matches(msg)) {
			inParty = true;
			partyChat = false;
			if (System.currentTimeMillis() - Main.time < 1000) {
				event.setCancelled(true);
				DelayedSend.SendMessage("/pc", Chat.STANDARD, 1000);
			}
			log();
		}
		
		// Game start
		if (Preset.GameEnter.matches(msg) ||
			Preset.GameStart2.matches(msg)
			) {
				inGame = true;
				//Debug.chatDebug("Updated inGame: True");
				log();
		}
		
		// Game end
		if (Preset.GameOver1.matches(msg) ||
			Preset.GameOver2.matches(msg) ||
			Preset.GameOver3.matches(msg) ||
			Preset.GameOver4.matches(msg) ||
			Preset.GameOver5.matches(msg) ||
			Preset.GameOver7.matches(msg) ||
			Preset.MainLobby.matches(msg) ||
			Preset.Lose1.matches(msg) 	  ||
			Preset.Lose2.matches(msg)
			) {
				inGame = false;
				log();
		}
		
		// Slash who? Getting rank...
		if (Preset.Who.matches(msg)) {
			String[] who = msg.split(" ");
			
			for (int i = 0; i < who.length; i++) {
				
				if (who[i].contains(The5zigAPI.getAPI().getGameProfile().getName())) {
					ranked = !who[i-1].startsWith(":Stone");
					//Debug.chatState("Ranked = " + ranked);
					break;
				}
			}
			
			if (System.currentTimeMillis() - Main.time < 3000) {
				event.setCancelled(true);
			}
		}
		
		// Unmuted, solo games
		unmute: if (muted && (msg.contains("Go!") || msg.contains("Let the games begin") || msg.contains("20 second"))) {
			muted = false;
			log();
			if (postMute.isEmpty()) break unmute;
			
			Iterator i = postMute.entrySet().iterator();
			while (i.hasNext()) {
				Map.Entry me = (Map.Entry) i.next();
				
				DelayedSend.SendMessage(
					me.getKey().toString(), 
					(Chat) me.getValue()
				);
			}
			postMute.clear();
		}
		
		// Unmute but clear auto messages
		if (muted && Preset.MainLobby.matches(msg)) {
			muted = false;
			log();
			postMute.clear();
			DelayedSend.cancelAll();
		}
		
		// Cancel cant talk message
		if (msg.contains("the game starts!")) {
			muted = true;
			log();
			if (timeSinceLastMessage() < 1000) {
				event.setCancelled(true);
				The5zigAPI.getLogger().warn("Message cancelled!");
				
				postMute.put(previousMessage.toString(), previousChat);
			}			
		}
	}

	
}
