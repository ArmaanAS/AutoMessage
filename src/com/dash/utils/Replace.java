package com.dash.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.util.NetworkPlayerInfo;

public enum Replace {
	NONE, KILL, PARTYJOIN, PARTYACCEPT, 
	DUEL, FRIEND, MAINCHAT, PARTYCHAT, 
	PRIVATECHAT, FRIENDCHAT, GLOBALCHAT,
	TEAMCHAT, EQUATION, SOLOCHAT;
	
	private static String[] deadCondition = {"was", "couldn't", "thought", "burnt"},
				   killerCondition = {"by", "escaping", "fighting"};
	
	
	public static String replacePresets(String msg, Replace type, String line) {
		msg = indexReplace(msg, line);
		
		switch(type) {
			case NONE:  		return msg;
			case PARTYJOIN: 	return partyJoinReplace(msg, line);
			case PARTYACCEPT:	return partyAcceptReplace(msg, line);
			case KILL:  		return killReplace(msg, line);
			case DUEL:			return duelReplace(msg, line);
			case FRIEND:		return friendReplace(msg, line);
			case MAINCHAT:		return mainChatReplace(msg, line);
			case PARTYCHAT:		return partyChatReplace(msg, line);
			case PRIVATECHAT:	return privateChatReplace(msg, line);
			case FRIENDCHAT:	return friendChatReplace(msg, line);
			case GLOBALCHAT:	return globalChatReplace(msg, line);
			case TEAMCHAT:		return teamChatReplace(msg, line);
			case SOLOCHAT:		return soloChatReplace(msg, line);
			case EQUATION:		return equationReplace(line);
			default: 			return msg;
		}
	}
	
	private static String indexReplace(String msg, String line) {
		String[] split = line.split(" ");
		Matcher match = Pattern.compile("<-?\\d+>").matcher(msg);
		
		while (match.find()) {
			String num = match.group();
			
			int index = Integer.parseInt(num.substring(1, num.length()-1)) - 1;
			
			if (index < split.length) {
				msg = msg.replaceAll(num, split[index % split.length]);
			} else {
				Debug.chatError("Index out of bounds: " + index);
			}
		}
		
		return msg;
	}
	
	private static String killReplace(String msg, String line) {
		String dead = "", killer = "";
			
		String[] split = line.split(" ");
		for (int i = 0; i < split.length; i++) {
			for (String d : deadCondition) {
				if (split[i].equals(d)) {
					dead = split[i-1]
						.substring(0, split[i-1].length())
						.replaceAll("[^\\w]", "");;
				}
			}
			
			for (String k : killerCondition) {
				if (split[i].equals(k)) {
					killer = split[i+1]
					.substring(0, split[i+1].length())
					.replaceAll("[^\\w]", "");
				}
			}
		}
		return msg
			.replaceAll("(?i)<dead>", dead)
			.replaceAll("(?i)<killer>", killer);
	}
	
	private static String partyJoinReplace(String msg, String line) {		
		return msg
			.replaceAll("(?i)<player>", line.split(" ")[2]);
	}
	
	private static String partyAcceptReplace(String msg, String line) {
		return msg
			.replaceAll("(?i)<owner>",
				line.split(" ")[3]
				.replaceAll("'s", "")
			);
	}
	
	private static String duelReplace(String msg, String line) {
		return msg
			.replaceAll("(?i)<accept>", "/rematchyes")
			.replaceAll("(?i)<deny>", "/rematchno");
	}
	
	private static String friendReplace(String msg, String line) {		
		return msg
			.replaceAll("(?i)<friend>", line.split(" ")[2]);
	}
	
	private static String mainChatReplace(String msg, String line) {
		String name = line.split(" ")[1].replaceAll(":", "");
		return msg
			.replaceAll("(?i)<player>", name)
			.replaceAll("(?i)<message>", line.split(": ", 3)[2])
			.replaceAll("(?i)<rank>", line.split(" ")[0]
				.replaceAll(":", ""))
			.replaceAll("(?i)<ping>", Player.getPing(name)+"");
	}
	
	private static String partyChatReplace(String msg, String line) {
		String name = line.split(" ")[1].replaceAll(":", "");
		return msg
			.replaceAll("(?i)<player>", name)
			.replaceAll("(?i)<message>", line.split(": ", 2)[1])
			.replaceAll("(?i)<ping>", Player.getPing(name)+"");
	}
	
	private static String privateChatReplace(String msg, String line) {
		String name = line.split(" ")[1].replaceAll(":", "");
		return msg
			.replaceAll("(?i)<player>", name)
			.replaceAll("(?i)<message>", line.split(": ", 3)[2])
			.replaceAll("(?i)<rank>", line.split(" ")[0]
					.replaceAll(":", ""))
			.replaceAll("(?i)<ping>", Player.getPing(name)+"");
	}
	
	private static String friendChatReplace(String msg, String line) {
		String name = line.split(" ")[1].replaceAll(":", "");
		return msg
			.replaceAll("(?i)<player>", name)
			.replaceAll("(?i)<message>", line.split(": ", 2)[1])
			.replaceAll("(?i)<ping>", Player.getPing(name)+"");
	}
	
	private static String globalChatReplace(String msg, String line) {
		String name = line.split(" ")[1].replaceAll(":", "");
		return msg
			.replaceAll("(?i)<player>", name)
			.replaceAll("(?i)<message>", line.split(": ", 2)[1])
			.replaceAll("(?i)<ping>", Player.getPing(name)+"");
	}
	
	private static String teamChatReplace(String msg, String line) {
		String name = line.split(" ")[1].replaceAll(":", "");
		return msg
			.replaceAll("(?i)<player>", name)
			.replaceAll("(?i)<message>", line.split(": ", 2)[1])
			.replaceAll("(?i)<ping>", Player.getPing(name)+"");
	}
	
	private static String soloChatReplace(String msg, String line) {
		return msg
			.replaceAll("(?i)<player>", line.split(">", 2)[0]
				.replaceAll("<", ""))
			.replaceAll("(?i)<message>", line.split("> ", 2)[1]);
	}
	
	private static String equationReplace(String line) {
		String i = "";
		try {
			ScriptEngineManager mgr = new ScriptEngineManager(null);
		    ScriptEngine engine = mgr.getEngineByName("JavaScript");
		    i = engine.eval(line.split("Solve:")[1]).toString();
		} catch(ScriptException e) {
			Debug.chatError("Error in solving eqution: " 
				+ line.split("Solve:")[1]);
			i = "";
		}
		
		return i;
	}
}
