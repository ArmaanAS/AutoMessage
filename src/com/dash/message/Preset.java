package com.dash.message;

import com.dash.utils.Replace;
import com.dash.utils.Spam;

import eu.the5zig.mod.The5zigAPI;

public class Preset {
	private static String name = The5zigAPI.getAPI().getGameProfile().getName();
	
	public static final Condition
		GameOver1 	= new Condition(new String[]{"won the game"}, new String[0], true, true, Spam.SUPPRESS),
		GameOver2 	= new Condition(new String[]{"server", "restart"}, new String[0], true, false, Spam.SUPPRESS),
		GameOver3 	= new Condition(new String[]{"team", "won"}, new String[] {"winning"}, true, true, Spam.SUPPRESS),
		GameOver4 	= new Condition(new String[]{"1", "#", "trophies"}, new String[0], true, false, Spam.SUPPRESS),
		GameOver5 	= new Condition(new String[]{"1st", "points"}, new String[0], true, false, Spam.SUPPRESS),
		GameOver6 	= new Condition(new String[]{name + " finished"}, new String[0], true, false, Spam.SUPPRESS),
		GameOver7 	= new RegexCondition(new String[]{"^.{0,6}Game Length:"}, new String[0], false, false, Spam.SUPPRESS),
		GameStart1 	= new Condition(new String[]{"starting in", "1"}, new String[]{"5", "0", "minute"}, true, false),
		GameStart2 	= new Condition(new String[]{"How to play"}, new String[0], false, false),
		Lose1 		= new Condition(new String[]{"You're out"}, new String[0], true, true),
		Lose2 		= new Condition(new String[]{name, "has been eliminated"}, new String[0], true, true),
		Lose3 		= new Condition(new String[]{"Play Again"}, new String[0], true, true),
		Kill1 		= new Condition(new String[]{"was", "by ", name}, new String[0], true, false, Replace.KILL),
		Kill2 		= new Condition(new String[]{"thought", "fighting " + name}, new String[0], true, false, Replace.KILL),
		Kill3 		= new Condition(new String[]{"couldn't", "escaping " + name}, new String[0], true, false, Replace.KILL),
		Kill4 		= new Condition(new String[]{"thought", "escaping " + name}, new String[0], true, false, Replace.KILL),
		Kill5 		= new Condition(new String[]{"burned", "fighting " + name}, new String[0], true, false, Replace.KILL),
		Killed1 	= new Condition(new String[]{name + " was", "by"}, new String[] {"shot", "sniped"}, true, true, Replace.KILL),
		Shot1	 	= new Condition(new String[]{name + " was shot", "by"}, new String[0], true, true, Replace.KILL),
		Shot2	 	= new Condition(new String[]{name + " was sniped by"}, new String[0], true, true, Replace.KILL),
		Fall1	 	= new Condition(new String[]{name + " couldn't", "escaping"}, new String[0], true, true, Replace.KILL),
		Fall2	 	= new Condition(new String[]{name + " fell", "escaping"}, new String[0], true, true, Replace.KILL),
		Void1	 	= new Condition(new String[]{name + " thought", "escaping"}, new String[0], true, true, Replace.KILL),
		Void2	 	= new Condition(new String[]{name + " died", "escaping"}, new String[0], true, true, Replace.KILL),
		Fire	 	= new Condition(new String[]{name + " burned", "fighting"}, new String[0], true, true, Replace.KILL),
		TNT		 	= new Condition(new String[]{name + " blew up", "fighting"}, new String[0], true, true, Replace.KILL),
		Magic	 	= new Condition(new String[]{name + " was killed with magic", "fighting"}, new String[0], true, true, Replace.KILL),
		LevelUp 	= new Condition(new String[]{"You are now", "level"}, new String[0], true, false),
		GameJoin1	= new Condition(new String[]{name, "joined your game"}, new String[0], true, false),
		GameJoin2 	= new Condition(new String[]{"You are playing", "combat"}, new String[0], true, false),
		PartyAccept = new Condition(new String[]{"You have joined", "party"}, new String[0], true, false, Replace.PARTYACCEPT),
		PartyJoin   = new Condition(new String[]{"joined the party"}, new String[] {name}, true, false, Replace.PARTYJOIN),
		KillAssist  = new Condition(new String[]{"Kill assist"}, new String[0], true, false),
		DuelRematch = new Condition(new String[]{"rematch", "ACCEPT"}, new String[0], true, false, Replace.DUEL),
		FriendJoin  = new Condition(new String[]{"joined the network"}, new String[0], true, false, Replace.FRIEND),
		PartyLeave1	= new Condition(new String[]{"you are not in a party"}, new String[0], true, false),
		PartyLeave2	= new Condition(new String[]{"you have left your party"}, new String[0], true, false),
		PartyLeave3	= new Condition(new String[]{"no longer in a party"}, new String[0], true, false),
		PartyLeave4	= new Condition(new String[]{"kicked from your party"}, new String[0], true, false),
		PartyEnable	= new Condition(new String[]{"chat", "enabled"}, new String[0], true, false),
		PartyDisable= new Condition(new String[]{"chat", "disabled"}, new String[0], true, false),
		GameEnter	= new Condition(new String[]{"teleporting to map"}, new String[0], true, false),
		MainLobby	= new Condition(new String[]{"Welcome to"}, new String[0], true, false),
		PlayerMessage	= new RegexCondition(new String[]{"^:\\w+:"}, new String[]{name + ":"}, false, false, Replace.MAINCHAT),
		PartyMessage	= new RegexCondition(new String[]{"^\\[party\\] \\w+"}, new String[]{name + ":"}, false, false, Replace.PARTYCHAT),
		PrivateMessage	= new RegexCondition(new String[]{"^:\\w+: \\w+ -> Me"}, new String[0], false, false, Replace.PRIVATECHAT),
		FriendMessage	= new RegexCondition(new String[]{"^\\[Friend\\] \\w+ -> Me:"}, new String[0], false, false, Replace.FRIENDCHAT),
		TeamMessage		= new RegexCondition(new String[]{"^\\[Team\\] \\w+:"}, new String[]{name + ":"}, false, false, Replace.TEAMCHAT),
		GlobalMessage	= new RegexCondition(new String[]{"^\\[Global\\] \\w+:"}, new String[0], false, false, Replace.GLOBALCHAT),
		SoloMessage 	= new RegexCondition(new String[]{"^<\\w+> "}, new String[0], false, false, Replace.SOLOCHAT),
		Who				= new RegexCondition(new String[]{"There are", "players online:"}, new String[0], false, false, Replace.NONE),
		EquationSolver	= new Condition(new String[]{"Solve:"}, new String[0], false, false, Replace.EQUATION);

	public static Condition[] getPresets(String s) {
		String c = s.toLowerCase();
		
		if (c.contains("gameover")) {
			return new Condition[]{
				Preset.GameOver1, 
				Preset.GameOver2,
				Preset.GameOver3,
				Preset.GameOver4,
				Preset.GameOver5,
				Preset.GameOver6,
				Preset.GameOver7.clone()
			};
		} else if (c.contains("levelup")) {
			return new Condition[]{
				Preset.LevelUp
			};
		} else if (c.contains("lose")) {
			return new Condition[]{
				Preset.Lose1,
				Preset.Lose2
			};
		} else if (c.contains("gamestart")) {
			return new Condition[]{
				Preset.GameStart1, 
				Preset.GameStart2
			};
		} else if (c.contains("gamejoin")) {
			return new Condition[] {
				Preset.GameJoin1,
				Preset.GameJoin2
			};
		} else if (c.equals("killed")) {
			return new Condition[] {
				Preset.Killed1,
				Preset.Shot1,
				Preset.Shot2,
				Preset.Fire,
				Preset.Fall1,
				Preset.Fall2,
				Preset.TNT,
				Preset.Void1,
				Preset.Void2,
				Preset.Magic
			};
		} else if (c.equals("kill")) {
			return new Condition[] {
				Preset.Kill1,
				Preset.Kill2,
				Preset.Kill3,
				Preset.Kill4,
				Preset.Kill5
			};
		} else if (c.contains("partyaccept")) {
			return new Condition[] {
				Preset.PartyAccept
			};
		} else if (c.contains("killassist")) {
			return new Condition[] {
				Preset.KillAssist
			};
		} else if (c.contains("partyjoin")) {
			return new Condition[] {
				Preset.PartyJoin
			};
		} else if (c.contains("duelrematch")) {
			return new Condition[] {
				Preset.DuelRematch
			};
		} else if (c.contains("friendjoin")) {
			return new Condition[] {
				Preset.FriendJoin
			};
		} else if (c.contains("killedwithfire")) {
			return new Condition[] {
				Preset.Fire
			};
		} else if (c.contains("killedwithvoid")) {
			return new Condition[] {
				Preset.Void1,
				Preset.Void2
			};
		} else if (c.contains("killedwithfall")) {
			return new Condition[] {
				Preset.Fall1,
				Preset.Fall2
			};
		} else if (c.contains("killedwithshot")) {
			return new Condition[] {
				Preset.Shot1,
				Preset.Shot2
			};
		} else if (c.contains("killedwithtnt")) {
			return new Condition[] {
				Preset.TNT
			};
		} else if (c.contains("killedwithmagic")) {
			return new Condition[] {
				Preset.Magic
			};
		} else if (c.contains("killedwithother")) {
			return new Condition[] {
				Preset.Killed1
			};
		} else if (c.contains("playermessage")) {
			return new Condition[] {
				Preset.PlayerMessage.clone()
			};
		} else if (c.contains("partymessage")) {
			return new Condition[] {
				Preset.PartyMessage.clone()
			};
		} else if (c.contains("privatemessage")) {
			return new Condition[] {
				Preset.PrivateMessage.clone()
			};
		} else if (c.contains("friendmessage")) {
				return new Condition[] {
				Preset.FriendMessage.clone()
			};
		} else if (c.contains("globalmessage")) {
			return new Condition[] {
				Preset.GlobalMessage.clone()
			};
		} else if (c.contains("teammessage")) {
			return new Condition[] {
				Preset.TeamMessage.clone()
			};
		} else if (c.contains("solomessage")) {
			return new Condition[] {
				Preset.SoloMessage.clone()
			};
		} else if (c.equals("message")) {
			return new Condition[] {
				Preset.FriendMessage.clone(),
				Preset.PrivateMessage.clone(),
				Preset.GlobalMessage.clone(),
				Preset.PartyMessage.clone(),
				Preset.PlayerMessage.clone(),
				Preset.TeamMessage.clone()
			};
		}
		
		return new Condition[0];
	}
}
