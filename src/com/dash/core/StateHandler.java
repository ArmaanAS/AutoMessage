package com.dash.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.dash.message.condition.PresetCondition;
import com.dash.utils.Chat;
import com.dash.utils.Colour;
import com.dash.utils.Debug;
import com.dash.utils.DelayedSend;
import com.dash.utils.Sidebar;
import com.dash.utils.Thank;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.event.ChatEvent;
import eu.the5zig.mod.event.ChatSendEvent;
import eu.the5zig.mod.event.ServerQuitEvent;
import eu.the5zig.mod.util.NetworkPlayerInfo;
import eu.the5zig.util.minecraft.ChatColor;

public class StateHandler {
	
	public static boolean inParty = false, xinParty = false, 
			partyChat = false, xpartyChat = false,
			inGame = false, xinGame = false,
			muted = false, xmuted = false,
			ranked = false, plist = false;
	public static final String[] 
			teamNames = {"Team", "BlockWars", "UHC"},
			soloNames = {"FFA", "Survival", "Quake", "Spleef", "Minerware", "Wing"},
			gameNames = {"Eggwars"};
	public static List<String> partyPlayers = new ArrayList<>(),
			friends = new ArrayList<>();
	public static int pplayers = 0;
	public static Map<String, List<String>> teams = new HashMap<>();
	private static LinkedHashMap  postMute = new LinkedHashMap();
	private static String  previousMessage = "";
	private static Chat	   previousChat    = Chat.STANDARD;
	private static final boolean xD		   = true;  //false;
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
		
//		if (marks > global) event.setMessage(msg.substring(marks - global));
	}
	
	@Deprecated
	public static void PC(boolean state) {
		log();
		if ((partyChat != state) && inParty) {
			The5zigAPI.getAPI().sendPlayerMessage("/pc");
			partyChat = !partyChat;
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
		} else {
////			Debug.chatInfo("Fake message:");
////			Debug.chatInfo("" + msg.length());
//			if (msg.length() > 255) {
//				int min, max = 0;
//				do {
//					min = max;
//					max = min+msg.substring(min, min+255).lastIndexOf('\n', min+255-1);
//					if (max <= min)  {
////						Chat.fakePartyMessage("" + msg.substring(min));
////						return;
//						max = min + 255;
//					}
//					
////					Debug.chatDebug(min + ", " + max + " -> " + (max-min) + " (" + (msg.length()-max) + ")");
//					Chat.fakePartyMessage("" + msg.substring(min, max));
//				} while (msg.length()-max >= 255);
////				Debug.chatDebug(max + ", " + msg.length() + " - " + (msg.length()-max));
//				Chat.fakePartyMessage(msg.substring(max));
//			} else {
//				Chat.fakePartyMessage(msg);
//			}
			Chat.fakePartyMessage(msg);
			return;
		}
		
		
//		Debug.chatInfo("" + msg.length());
		int len = 200;
		if (msg.length() > len) {
			int min, max = 0, i = 0;
			do {
				min = max;
				max = min+msg.substring(min, min+len).lastIndexOf('\n', min+len-1);
				if (max <= min)  {
//					DelayedSend.SendMessage(prefix + msg.substring(min), Chat.PARTY, i*200);
//					return;
					max = min + len;
				}
				
//				Debug.chatDebug(min + ", " + max + " -> " + (max-min) + " (" + (msg.length()-max) + ")");
				DelayedSend.SendMessage("" + msg.substring(min, max), Chat.PARTY, 200*i);
				i++;
			} while (msg.length()-max >= len);
//			Debug.chatDebug(max + ", " + msg.length() + " - " + (msg.length()-max));
			DelayedSend.SendMessage(prefix + msg.substring(max), Chat.PARTY, 200*i);
//			int i = 0;
//			for (String s : msg.split("\n+(?=.+\n?)")) {
//				DelayedSend.SendMessage(prefix + s, Chat.PARTY, 3000*i);
//				i++;
//			}
		} else {
			chatMessage(prefix + msg, Chat.PARTY);
		}
		return;
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
		if (out == Chat.PARTY && !inParty)  {
			msg = Colour.forceRecolour(msg);
		} else {
			msg = Colour.recolour(msg);			
		}
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
			case GLOBAL:   	GlobalMessage(msg); return;
			case PARTY:	   	PartyMessage(msg); return;
			case STANDARD: 	StandardMessage(msg); return;
			case TEAM:     	TeamMessage(msg); return;
			default:		return;
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
	
	public static void updateStates(ServerQuitEvent event) {
		ranked = false;
		inParty = false;
		partyChat = false;
	}
	
	public static void updateStates(ChatEvent event, String msg) {
		if (PresetCondition.FriendsList.matches(msg)) {
			friends.clear();
			String[] lines = msg.split("\n");
			
			for (int i = 1; i < lines.length; i++) {
				if (lines[i].matches("\\w+ - .*")) {
					friends.add(lines[i].split(" ")[0]);
				} else {
					break;
				}
			}
			
			if (msg.contains("Offline:")) {
				String[] split = msg.split("Offline:\n", 2);
				
				friends.addAll(Arrays.asList(split[1].split(", ")));
			}
			
//			Debug.chatDebug("Friends: (" + friends.size() + ") " + friends.toString());
			
			if (Main.flist) {
				Main.flist = false;
				event.setCancelled(true);
			}
		}
		
		if (PresetCondition.PartyAccept.matches(msg)) {
			plist = true;
			sendChatOutput("/p info", Chat.STANDARD);
		}
		
		if (PresetCondition.PartyJoin.matches(msg)) {
			partyPlayers.add(msg.split(" ")[2]);
//			Debug.chatDebug("Added player: " + msg.split(" ")[2]);
		}
		
		if (PresetCondition.PartyLeft.matches(msg)) {
//			Debug.chatDebug("Players: " + partyPlayers.size());
			String name = msg.split(" ")[2];
			partyPlayers = partyPlayers.stream()
				.filter(n -> !n.equals(name))
				.collect(Collectors.toList());
//			Debug.chatDebug("Removed player: " + name);
//			Debug.chatDebug("Player: " + partyPlayers.size());
		}
		
		if (PresetCondition.PartyLeave.matches(msg)) {
			partyPlayers.clear();
		}
		
		if (PresetCondition.PartyInfo.matches(msg) || (pplayers > 0 && msg.matches("\\w+"))) {
			if (msg.toLowerCase().contains("party status")) {
				pplayers = Integer.parseInt(msg.split("[\\(/]")[1]);
				partyPlayers.clear();
			}
			
			if (msg.toLowerCase().contains(" - kick")) {
				pplayers--;
				String name = msg.split(" ", 2)[0];
				if (!name.equals(Main.name)) {
					partyPlayers.add(name);
//					Debug.chatDebug("Added to party: " + partyPlayers.get(partyPlayers.size()-1));
				}
			} else if (msg.startsWith("Owner:")) {
				pplayers--;
				String name = msg.split(" ", 2)[1];
				if (!name.equals(Main.name)) {
					partyPlayers.add(name);
				}
//				Debug.chatDebug("Added to the party: " + name);
			} else if (msg.matches("\\w+")) {
				pplayers--;
				String name = msg;
				if (!name.equals(Main.name)) {
					partyPlayers.add(name);
				}
//				Debug.chatDebug("Added to the party: " + name);
			}
			
			if (plist) {
				event.setCancelled(true);
				if (pplayers == 0) {
					plist = false;
				}
			}
		}
		
		if (PresetCondition.Thanked.matches(msg) && Thank.thanking > 0) {
//			Debug.chatDebug("Got to Thanked");
			event.setCancelled(true);
//			event.setMessage("Cancelled.");
			Thank.thanking--;
//			Debug.chatState("thanking = " + Thank.thanking);
		}
		
		if (PresetCondition.Thanking.matches(msg) && Thank.thanking > 0) {
			Thank.thanking--;
//			Debug.chatState("thanking = " + Thank.thanking);
		}
		
		// Not in party
		if (PresetCondition.PartyLeave.matches(msg)) {
				inParty = partyChat = false;
//				if (System.currentTimeMillis() - Main.time < 1000) {
				if (Main.party) {
					Main.party = false;
					event.setCancelled(true);
				}
				log();
		}
		
		// Joined a party
		if (PresetCondition.PartyAccept.matches(msg) ||
			PresetCondition.PartyJoin.matches(msg)
			) {
				inParty = true;
				log();
		}
		
		// Party chat - enabled
		if (PresetCondition.PartyEnable.matches(msg)) {
			inParty = partyChat = true;
//			if (System.currentTimeMillis() - Main.time < 3000) {
			if (Main.party) {
				Main.party = false;
				plist = true;
				event.setCancelled(true);
				DelayedSend.SendMessage("/pc", Chat.STANDARD, 0*1000);
				DelayedSend.SendMessage("/p info", Chat.STANDARD, 0*1000);				
			}
			log();
		}
		
		// Party chat - disabled
		if (PresetCondition.PartyDisable.matches(msg)) {
			inParty = true;
			partyChat = false;
//			if (System.currentTimeMillis() - Main.time < 1000) {
			if (Main.party) {
				Main.party = false;
				plist = true;
				event.setCancelled(true);
				DelayedSend.SendMessage("/pc", Chat.STANDARD, 0*1000);
				DelayedSend.SendMessage("/p info", Chat.STANDARD, 0*1000);			
			}
			log();
		}
		
		// Game start
		if (PresetCondition.GameStartSH.matches(msg)) {
				inGame = true;
				//Debug.chatDebug("Updated inGame: True");
				log();
				
		}
		
		// Game end
		if (PresetCondition.GameOver.matches(msg) ||
			PresetCondition.MainLobby.matches(msg) ||
			PresetCondition.Lose.matches(msg)) {
				inGame = false;
				log();
		}
		
		// Slash who? Getting rank...
		if (PresetCondition.Who.matches(msg)) {
			String[] who = msg.split(",? ");
			
			ranked = true;
			for (int i = who.length-1; i > 5; i--) {
				if (who[i].equals(Main.name)) {
					ranked = !who[i-1].equals(":Stone:");
//					Debug.chatState("Ranked = " + ranked);
					break;
				} 
//				else {
//					ranked = true;
//				}
			}
//			Debug.chatState("Ranked = " + ranked);
//			if (System.currentTimeMillis() - Main.time < 3000) {
			if (Main.who) {
				Main.who = false;
				event.setCancelled(true);
			}
		}
		
		// Unmuted, solo games
//		unmute: if (muted && (msg.contains("Go!") || msg.contains("Let the games begin") || msg.contains("20 second"))) {
		unmute: if (PresetCondition.Unmute.matches(msg)) {
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
		if (muted && PresetCondition.MainLobby.matches(msg)) {
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
