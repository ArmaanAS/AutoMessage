package com.dash.message.condition;

import com.dash.message.condition.Condition.InvalidPresetException;
import com.dash.utils.Debug;
import com.dash.utils.Replace;
import com.dash.utils.Spam;

import eu.the5zig.mod.The5zigAPI;

public class PresetCondition {
	private static String 
		name = The5zigAPI.getAPI().getGameProfile().getName(),
		sname = "\"" + name + "\"";
	
	public static final Condition
		GameOver = new Condition(true, Replace.SIDEBAR, Spam.SUPPRESS, 
				"\"won the game\"", 
				"\"server\" and \"restart\"",
				//"\"team\" and \"won\" and not \"winning\" and not \"by\" and not \"equal\"",
				"\"1\" and \"#\" and \"trophies\"",
				"\"" + name + " finished\"",
				//"\"Thank you for playing\"",
				"\"1st\" and \"with\" and \"points\"",
				"\"The game has ended in a tie!\""),
		GameStartSH = new Condition(false, Replace.SIDEBAR,
				"\"Teleporting to map...\"",
				"\"How to play\""),
		GameStart = new Condition(false, Replace.SIDEBAR,
				"\" starting in\" and \"1 \" and not (\"5\" or \"0\" or \" min\")",
				"\"How to play\" and not \"Protect your castle\""),
		FriendsList = new Condition(false, 
				"\"------- Friends (\""),
		Unmute = new Condition(false, 
				"\"Go!\" or \"Let the games begin\" or \"20 second\""),
		Cubelet = new Condition(false, 
				"\"Cubelet found\" and not \":\"\""),
		Lose = new Condition(true, Replace.SIDEBAR, Spam.SUPPRESS,
				"\"You're out\"",
				"$\"^.{0,6}Game length:\"",
				"\"" + name + " has been eliminated\""),
//				"\"Play Again\" and not \"use\""),
		Kill = new Condition(false, Replace.KILL, 
				"\"was\" and \"by\" and " + sname,
				"\"thought\" and \"fighting " + name + "\"",
				"\"couldn't\" and \"escaping " + name + "\"",
				"\"thought\" and \"escaping " + name + "\"",
				"\"burned\" and \"fighting " + name + "\""),
		Killed = new Condition(true, Replace.KILL, 
				"\"" + name + " was\" and \"by\" and not (\"shot\" or \"sniped\")"),
		Shot = new Condition(true, Replace.KILL,
				"\"" + name + " was shot\" and \"by\"",
				"\"" + name + " was sniped by\""),
		Fall = new Condition(true, Replace.KILL,
				"\"" + name + " couldn't\" and \"escaping\"",
				"\"" + name + " died\" and \"escaping\""),
		Void = new Condition(true, Replace.KILL,
				"\"" + name + " thought\" and \"escaping\"",
				"\"" + name + " died\" and \"escaping\""),
		Fire = new Condition(true, Replace.KILL,
				"\"" + name + " burned\" and \"fighting\""),
		TNT = new Condition(true, Replace.KILL,
				"\"" + name + " blew up\" and \"fighting\""),
		Magic = new Condition(true, Replace.KILL, 
				"\"" + name + " was killed with magic\" and \"fighting\""),
		LevelUp = new Condition(false, 
				"\"You are now\" and \"level\""),
		GameJoin = new Condition(true, 
				sname + " and \"joined your game\"",
				"\"You are playing\" and \"combat\""),
		PartyAccept = new Condition(false, Replace.PARTYACCEPT,
				"\"You have joined\" and \"party\""),
		PartyJoin = new Condition(false, Replace.PARTYJOIN,
				"\"joined the party\" and not " + sname),
		PartyInfo = new Condition(false, 
				"\"Party Status\" or $\"^Owner: \" or $\"^Members:\" or \" - KICK\""),
		PartyLeft = new Condition(false,
				"\" left the party.\" and not " + sname),
		KillAssist = new Condition(false, 
				"\"Kill assist\""),
		DuelRematch = new Condition(false, Replace.DUEL,
				"\"rematch\" and \"ACCEPT\""),
		FriendJoin = new Condition(false, Replace.FRIENDJOIN, 
				"\"joined the network\""),
		PartyLeave = new Condition(false, 
				"\"you are not in a party\"",
				"\"you have left your party\"",
				"\"no longer in a party\"",
				"\"kicked from your party\""),
		PartyEnable = new Condition(false, 
				"\"chat\" and \"enabled\""),
		PartyDisable = new Condition(false, 
				"\"chat\" and \"disabled\""),
		Thanking = new Condition(false,
				"\"You thanked \""),
		Thanked = new Condition(false,
				"\"You've already thanked \""),
		Multiplier = new Condition(false, 
				"\" has activated a Double\""),
		MainLobby = new Condition(true, false, 
				"\"Welcome to\""),
		PlayerMessage = new Condition(true, false, Replace.MAINCHAT, 
				"$\"^:\\w+:\""),
		PartyMessage = new Condition(true, false, Replace.PARTYCHAT,
				"$\"^\\[Party\\] \\w+:\""),
		PrivateMessage = new Condition(true, false, Replace.PRIVATECHAT,
				"$\"^(:\\w+: \\w+ -> Me|Me -> :\\w+: \\w+): \""),
		FriendMessage = new Condition(true, false, Replace.FRIENDCHAT,
				"$\"^\\[Friend\\] (\\w+ -> Me|Me -> \\w+): \""),
		TeamMessage = new Condition(true, false, Replace.TEAMCHAT, 
				"$\"^\\[Team\\] \\w+:\""),
		GlobalMessage = new Condition(true, false, Replace.GLOBALCHAT,
				"$\"^\\[Global\\](\\[\\w+\\])? \\w+:\""),
		SoloMessage = new Condition(false, Replace.SOLOCHAT,
				"$\"^<\\w+> \""),
		Who = new Condition(false, 
				"\"There are\" and \"players online:\""),
		Names = new Condition(true, false, Replace.NAMES, true,
				"$\"(?i): names(  ?\\w+)?$\""),
		Ping = new Condition(true, false, Replace.PING,
				"$\"(?i): ping(  ?\\w+)?$\""),
		Stats = new Condition(true, false, Replace.STATS, true,
				"$\"(?i): (full )?(lb|leaderboards)(  ?\\w+)?$\""),
		Laggers = new Condition(true, false, Replace.LAGGERS, true, 
				"$\"(?i): laggers$\""),
		Offences = new Condition(true, false, Replace.OFFENCES, true,
				"$\"(?i): (reports|rep)(  ?\\w+)?$\""),
		FullOffences = new Condition(true, false, Replace.FULLOFFENCES, true,
				"$\"(?i): full (reports|rep)(  ?\\w+)?$\""),
		Rank = new Condition(true, false, Replace.RANK, true, 
				"$\"(?i): rank(  ?(\\w+|#\\d+))?$\""),
		Rankings = new Condition(true, false, Replace.RANKINGS, true, 
				"$\"(?i): (ranks|rankings)$\"");
	
	public static Condition
		KilledAll, ChatMessage;
	
	static {
		KilledAll = new Condition(Replace.KILL);
		KilledAll.condition = new OrCondition();
		OrCondition or = (OrCondition) KilledAll.condition;
		
		or.add(Killed.condition);
		or.add(Shot.condition);
		or.add(Fall.condition);
		or.add(Fire.condition);
		or.add(Void.condition);
		or.add(TNT.condition);
		or.add(Magic.condition);
		
		ChatMessage = new Condition(true, false, Replace.KILL);
		ChatMessage.condition = new OrCondition();
		or = (OrCondition) ChatMessage.condition;
		
		or.add(PartyMessage.condition);
		or.add(GlobalMessage.condition);
		or.add(TeamMessage.condition);
		or.add(PrivateMessage.condition);
		or.add(FriendMessage.condition);
		or.add(PlayerMessage.condition);
	}
	
	public static Condition getPresetSafe(String s) {
		try {
			return getPreset(s);
		} catch (InvalidPresetException e) {
			Debug.chatWarn("Invalid preset(getPresetSafe): " + s);
			return null;
		}
	}
	
	public static Condition getPreset(String s) throws InvalidPresetException {
		String c = s.toLowerCase();
		
		if (c.contains("gameover")) {
			return GameOver;
		} else if (c.contains("levelup")) {
			return LevelUp;
		} else if (c.contains("lose")) {
			return Lose;
		} else if (c.contains("gamestart")) {
			return GameStart;
		} else if (c.contains("gamejoin")) {
			return GameJoin;
		} else if (c.equals("killed")) {
			return KilledAll;
		} else if (c.equals("kill")) {
			return Kill;
		} else if (c.contains("partyaccept")) {
			return PartyAccept;
		} else if (c.contains("killassist")) {
			return KillAssist;
		} else if (c.contains("partyjoin")) {
			return PartyJoin;
		} else if (c.contains("duelrematch")) {
			return DuelRematch;
		} else if (c.contains("friendjoin")) {
			return FriendJoin;
		} else if (c.contains("killedwithfire")) {
			return Fire;
		} else if (c.contains("killedwithvoid")) {
			return Void;
		} else if (c.contains("killedwithfall")) {
			return Fall;
		} else if (c.contains("killedwithshot")) {
			return Shot;
		} else if (c.contains("killedwithtnt")) {
			return TNT;
		} else if (c.contains("killedwithmagic")) {
			return Magic;
		} else if (c.contains("killedwithother")) {
			return Killed;
		} else if (c.contains("playermessage")) {
			return PlayerMessage;
		} else if (c.contains("partymessage")) {
			return PartyMessage;
		} else if (c.contains("privatemessage")) {
			return PrivateMessage;
		} else if (c.contains("friendmessage")) {
			return FriendMessage;
		} else if (c.contains("globalmessage")) {
			return GlobalMessage;
		} else if (c.contains("teammessage")) {
			return TeamMessage;
		} else if (c.contains("solomessage")) {
			return SoloMessage;
		} else if (c.contains("names")) {
			return Names;
		} else if (c.contains("ping")) {
			return Ping;
		} else if (c.contains("stats")) {
			return Stats;
		} else if (c.contains("laggers")) {
			return Laggers;
		} else if (c.contains("fulloffences")) {
			return FullOffences;
		} else if (c.contains("offences")) {
			return Offences;
		} else if (c.equals("message")) {
			return ChatMessage;
		} else if (c.equals("rank")) {
			return Rank;
		} else if (c.equals("rankings")) {
			return Rankings;
		} else if (c.equals("cubelet")) {
			return Cubelet;
		}
		
		throw new InvalidPresetException(s);
	}
}
