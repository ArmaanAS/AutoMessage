package com.dash.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;

import com.dash.core.StateHandler;
import com.dash.utils.Appeals.Offence;
import com.dash.utils.Leaderboard.Player;
import com.dash.utils.Leaderboard.Stat;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.util.NetworkPlayerInfo;
import eu.the5zig.util.minecraft.ChatColor;

public enum Replace {
	NONE(false), KILL(false, "dead killer"), PARTYJOIN(false, "player"), 
	PARTYACCEPT(false, "owner"), DUEL(false, "accept deny"), 
	FRIENDJOIN(false, "friend"), MAINCHAT, PARTYCHAT, 
	PRIVATECHAT("player message ping rank"), 
	FRIENDCHAT(false, "player message"), GLOBALCHAT,
	TEAMCHAT, /*EQUATION(false),*/ SOLOCHAT(false, "player message"), 
	NAMES("num names user col"), PING(false, "userping user col"), 
	STATS("num stats user col"), LAGGERS("num names total"),
	OFFENCES("num user basicoffences col"), FULLOFFENCES("num user offences col"),
	SIDEBAR("sidebar"), RANK("rank col rating user just ranks"), 
	RANKINGS("num total rankings col");
	
	public final static String[] std = {"ping", "player", "message", "col"};
	
	public boolean stdRep = true;
	public String[] replacements = new String[0];
	
	
	private Replace() {}
	
	private Replace(String rep) {
		this.replacements = rep.split(" ");
	}
	
	private Replace(boolean stdRep) {
		this.stdRep = stdRep;
	}
	
	private Replace(boolean stdRep, String rep) {
		this.stdRep = stdRep;
//		if (stdRep) { 
//			this.replacements = Stream.of(rep.split(" "), std)
//					.flatMap(Stream::of)
//                    .toArray(String[]::new);
//		} else {
//			this.replacements = rep.split(" ");
//		}
		this.replacements = rep.split(" ");
	}
	
	private static String[] deadCondition = {"was", "couldn't", "thought", "burnt"},
				   killerCondition = {"by", "escaping", "fighting"};
	
	public static String replacePresets(String msg, Replace type, String line) {
		return replacePresets(msg, type, line, type.stdRep);
	}
	
	public static String replacePresets(String msg, Replace type, String line, boolean stdRep) {
		msg = indexReplace(msg, line);
		if (stdRep) {
			msg = chatReplace(msg, line);
		}
		
		switch(type) {
			case NONE:  		return msg;
			case PARTYJOIN: 	return partyJoinReplace(msg, line);
			case PARTYACCEPT:	return partyAcceptReplace(msg, line);
			case KILL:  		return killReplace(msg, line);
			case DUEL:			return duelReplace(msg, line);
			case FRIENDJOIN:	return friendReplace(msg, line);
//			case MAINCHAT:		return mainChatReplace(msg, line);
//			case PARTYCHAT:		return partyChatReplace(msg, line);
			case PRIVATECHAT:	return privateChatReplace(msg, line);
			case FRIENDCHAT:	return friendChatReplace(msg, line);
//			case GLOBALCHAT:	return globalChatReplace(msg, line);
//			case TEAMCHAT:		return teamChatReplace(msg, line);
			case SOLOCHAT:		return soloChatReplace(msg, line);
			//case EQUATION:		return equationReplace(line);
			case NAMES:			return namesReplace(msg, line);
			case PING:			return pingReplace(msg, line);
			case STATS:			return statsReplace(msg, line);
			case LAGGERS:		return laggersReplace(msg, line);
			case OFFENCES:		return basicOffencesReplace(msg, line);
			case FULLOFFENCES:	return offencesReplace(msg, line);
			case SIDEBAR:		return sidebarReplace(msg, line);
			case RANK:			return rankReplace(msg, line);
			case RANKINGS:		return rankingsReplace(msg, line);
			default: 			return msg;
		}
	}
	
	private static String chatReplace(String msg, String line) {
		Chat type = Chat.getChatType(line);
		if (type != Chat.OTHER) {
			String name = Chat.getName(line, type);
			int p = com.dash.utils.Player.getPing(name);
			return msg
				.replaceAll("(?i)<player>", name)
				.replaceAll("(?i)<message>", line.split(": ", 2)[1])
				.replaceAll("(?i)<ping>", Colour.ping(p))
				.replaceAll("(?i)<pingcol>", Colour.pingColour(p).toString());
		} else {
			return msg;
		}
	}
	
	private static String indexReplace(String msg, String line) {
		String[] tokens = line.split("\\s+");
		Matcher match = Pattern.compile("<-?\\d+>").matcher(msg);
		
		while (match.find()) {
			String m = match.group();
			
			int index = Integer.parseInt(m.substring(1, m.length()-1));
			if (index > 0) index--;
			int i = (index + tokens.length) % tokens.length;
			
			if (index < tokens.length) {
				msg = msg.replaceAll(m, tokens[i]);
			} else {
				Debug.chatError("Index out of bounds: " + index + ", (" + i + ")");
			}
		}
		
		match = Pattern.compile("<-?\\d+\\.{3}>").matcher(msg);
		
		while (match.find()) {
			String m = match.group();
			
			int index = Integer.parseInt(m.substring(1, m.length()-4));
			if (index > 0) index--;
			int i = (index + tokens.length) % tokens.length;
			
			if (index < tokens.length) {
				String[] split = line.split("\\s+", i+1);
				msg = msg.replaceAll(m, split[i]);
			} else {
				Debug.chatError("Index out of bounds: " + index + ", (" + i + ")");
			}
		}
		
		match = Pattern.compile("<\\.{3}-?\\d+>").matcher(msg);
		
		while (match.find()) {
			String m = match.group();
			
			int index = Integer.parseInt(m.substring(4, m.length()-1));
			if (index > 0) index--;
			int i = (index + tokens.length) % tokens.length;
			
			if (index < tokens.length) {
				String split = line.substring(0, RegexUtils.ordinalIndexOf("\\s+", line, i));
				msg = msg.replaceAll(m, split);
			} else {
				Debug.chatError("Index out of bounds: " + index + ", (" + i + ")");
			}
		}
		
		match = Pattern.compile("<-?\\d+\\.{3}-?\\d+>").matcher(msg);
		
		while (match.find()) {
			String m = match.group();
			
			String[] ij = m.split("(<|>|\\.{3})");
			
			int indexI = Integer.parseInt(ij[1]),
				indexJ = Integer.parseInt(ij[2]),
				i, j;
			
			if (indexI > 0) {
				i = indexI-1;
			} else {
				i = (indexI + tokens.length) % tokens.length;
			}
			
			if (indexJ > 0) {
				j = indexJ-1;
			} else {
				j = (indexJ + tokens.length) % tokens.length;
			}
			
			if (i >= tokens.length) {
				Debug.chatError("Index out of bounds: " + i + "(" + indexI + "");
			} else if (j > tokens.length) {
				Debug.chatError("Index out of bounds: " + j + "(" + indexJ + "");
			} else {
				int a, b;
				if (i == 0) {
					a = 0;
				} else if (i == tokens.length-1) {
					a = line.length();
				} else {				
					a = RegexUtils.ordinalIndexOf("\\s+", line, i-1)+1;
				}
				
				if (j == tokens.length-1) {
					b = line.length();
				} else {
					b = RegexUtils.ordinalIndexOf("\\s+", line, j);
				}
				
				msg = msg.replaceAll(m, line.substring(a, b));
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
	
//	private static String mainChatReplace(String msg, String line) {
//		String name = line.split(" ")[1].replaceAll(":", "");
//		return msg
//			.replaceAll("(?i)<player>", name)
//			.replaceAll("(?i)<message>", line.split(": ", 3)[2])
//			.replaceAll("(?i)<rank>", line.split(" ")[0]
//				.replaceAll(":", ""))
//			.replaceAll("(?i)<ping>", Player.getPing(name)+"");
//	}
	
//	private static String partyChatReplace(String msg, String line) {
//		String name = line.split(" ")[1].replaceAll(":", "");
//		return msg
//			.replaceAll("(?i)<player>", name)
//			.replaceAll("(?i)<message>", line.split(": ", 2)[1])
//			.replaceAll("(?i)<ping>", Player.getPing(name)+"");
//	}
	
	private static String privateChatReplace(String msg, String line) {
		String name = line.split(" ")[1].replaceAll(":", "");
		return msg
			.replaceAll("(?i)<player>", name)
			.replaceAll("(?i)<message>", line.split(": ", 3)[2])
			.replaceAll("(?i)<rank>", line.split(" ")[0]
					.replaceAll(":", ""))
			.replaceAll("(?i)<ping>", com.dash.utils.Player.getPing(name)+"");
	}
	
	private static String friendChatReplace(String msg, String line) {
		String name = Chat.getName(line, Chat.FRIENDMESSAGE);
		return msg
			.replaceAll("(?i)<player>", name)
			.replaceAll("(?i)<message>", line.split(": ", 2)[1]);
	}
	
//	private static String globalChatReplace(String msg, String line) {
//		String name = line.split(" ")[1].replaceAll(":", "");
//		return msg
//			.replaceAll("(?i)<player>", name)
//			.replaceAll("(?i)<message>", line.split(": ", 2)[1])
//			.replaceAll("(?i)<ping>", Player.getPing(name)+"");
//	}
	
//	private static String teamChatReplace(String msg, String line) {
//		String name = line.split(" ")[1].replaceAll(":", "");
//		return msg
//			.replaceAll("(?i)<player>", name)
//			.replaceAll("(?i)<message>", line.split(": ", 2)[1])
//			.replaceAll("(?i)<ping>", Player.getPing(name)+"");
//	}
	
	private static String soloChatReplace(String msg, String line) {
		return msg
			.replaceAll("(?i)<player>", line.split(">", 2)[0]
				.replaceAll("<", ""))
			.replaceAll("(?i)<message>", line.split("> ", 2)[1]);
	}
	
	// -- // -- // -- // -- // -- // -- // -- // -- //
	
	private static String namesReplace(String msg, String line) {
		String n;
		if (line.matches("(?i).*names  ?\\w+")) {
			Pattern p = Pattern.compile("(?i)(?<=names  ?)\\w+");
			Matcher m = p.matcher(line);
			
			m.find();
			n = m.group();
		} else {
			n = Chat.getName(line);
		}
		String[] names = com.dash.utils.Player.getOldNames(n);
		String namesString = String.join(", ", names);//names.toString();
//		namesString = "\n" + namesString.substring(1, namesString.length()-1);
		
		return msg.replaceAll("(?i)<names>", namesString)
				.replaceAll("(?i)<num>", ""+Colour.num(names.length))
				.replaceAll("(?i)<user>", n)
				.replaceAll("(?i)<col>", Colour.numColour(names.length));
	}
	
	private static String pingReplace(String msg, String line) {
		String name = line.split(" ")[1].replaceAll(":", "");
		msg = msg
			.replaceAll("(?i)<player>", name)
			.replaceAll("(?i)<message>", line.split(": ", 2)[1]);
		
		String n;
		if (line.matches("(?i).*ping(  ?\\w+)$")) {
			String[] tokens = line.split("\\s+");
			n = tokens[tokens.length-1];
		} else {
			n = Chat.getName(line);
		}
				
		int ping = com.dash.utils.Player.getPing(n);
		String p = Colour.pingColour(ping) + "" + ping + "ms";
		return msg.replaceAll("(?i)<user>", n)
				.replaceAll("(?i)<userping>", p)
				.replaceAll("(?i)<col>", Colour.pingColour(ping).toString());
	}
	
	private static String statsReplace(String msg, String line) {
		String n;
		if (line.matches("(?i).*(lb|leaderboards)  ?\\w+")) {
			Matcher m = Pattern.compile("(?i)(?<=lb  ?|leaderboards  ?)\\w+")
					.matcher(line);
			
			m.find();
			n = m.group();
		} else {
			n = Chat.getName(line);
		}
		
		List<Stat> stats = Leaderboard.getStats(n);
		Collections.sort(stats, (a, b) -> a.rank - b.rank);
		
		String statsString = "";
		String basicStats = "";
		String col = Colour.numColour(stats.size());
		for (Stat s : stats) {
			String rcol = "&e"; // Colour.rankColour(s.rank);
//			statsString += "\n#" + s.rank + ". " + s.game + 
//					" (" + s.wins + " " + s.award + ")";
			basicStats += "\n - #" + Font.ljust(s.rrank + ". ", 28, "&e") + s.game;
//			basicStats += "\n - #" + s.rank + ". " + s.game;
			statsString += String.format("\n%s &m-&e %s%s %s(%d %s)", 
					col, Font.ljust("#"+s.rrank+".", 34), Font.ljust(rcol+s.game, 102), rcol, s.wins, s.award);
//			statsString += String.format("\n%s&m&l-&e &v%s %s %s(%d %s)&r", 
//					col, Colour.rank(s.rank), rcol+s.game, rcol, s.wins, s.award);
		}
		
//		if (stats.size() > 7) {
//			int i = StringUtils.ordinalIndexOf("\n", basicStats, stats.size()/2);
//		}
		
		return msg.replaceAll("(?i)<stats>", statsString)
				.replaceAll("(?i)<basicStats>", basicStats)
				.replaceAll("(?i)<num>", ""+Colour.num(stats.size()))
				.replaceAll("(?i)<user>", stats.size()>0?stats.get(0).rname:n)
				.replaceAll("(?i)<col>", col);
	}
	
	private static String laggersReplace(String msg, String line) {	
		String laggers = "";
		int num = 0, total = The5zigAPI.getAPI().getServerPlayers().size();
		List<NetworkPlayerInfo> players = The5zigAPI.getAPI().getServerPlayers();
		Collections.sort(players, (a, b) -> a.getPing()-b.getPing());
		for (NetworkPlayerInfo p : players) {
			if (p.getPing() > 60) {
				num++;
				laggers += String.format("\n%5sms%s - %s%s", Colour.ping(p.getPing()), ChatColor.WHITE, ChatColor.YELLOW, p.getGameProfile().getName());
			}
		}
		
		
		return msg.replaceAll("(?i)<names>", laggers)
				.replaceAll("(?i)<num>", ""+num)
				.replaceAll("(?i)<total>", ""+players.size());
	}
	
	private static String offencesReplace(String msg, String line) {
		String n;
		if (line.matches("(?i).*(reports|rep)  ?\\w+")) {
			Pattern p = Pattern.compile("(?i)(?<=reports  ?|rep  ?)\\w+");
			Matcher m = p.matcher(line);
			
			m.find();
			n = m.group();
			Debug.chatInfo(n);
		} else {
			n = Chat.getName(line);
			Debug.chatInfo(n);
		}
		Offence[] offences = Appeals.getOffences(n);
		String offencesString = "";
		

		for (Offence o : offences) {
			if (o.current) {
				offencesString += String.format("\n&8(&4%s&8) &eBy &6%s &8(&5%s&8) &f- &3%s", 
						o.punishment, o.staff, o.date, o.evidence);
			} else {
				offencesString += String.format("\n&7(&c%s&7) &eBy &6%s &7(&d%s&7) &f- &b%s", 
						o.punishment, o.staff, o.date, o.evidence);
			}
		}
				
		return msg.replaceAll("(?i)<offences>", offencesString)
				.replaceAll("(?i)<num>", ""+Colour.num(offences.length))
				.replaceAll("(?i)<user>", n)
				.replaceAll("(?i)<col>", Colour.numColour(offences.length));
	}
	
	private static String basicOffencesReplace(String msg, String line) {
		String n;
		if (line.matches("(?i).*(reports|rep)  ?\\w+")) {
			Pattern p = Pattern.compile("(?i)(?<=reports  ?|rep  ?)\\w+");
			Matcher m = p.matcher(line);
			
			m.find();
			n = m.group();
			Debug.chatInfo(n);
		} else {
			n = Chat.getName(line);
			Debug.chatInfo(n);
		}
		
		Offence[] offences = Appeals.getOffences(n);
		String offencesString = "";
		
		
		for (Offence o : offences) {
			if (o.current) {
				offencesString += String.format("\n&f - &8(&4%s&8) &eBy &6%s &8(&5%s&8)", 
						o.punishment, o.staff, o.date);
			} else {
				offencesString += String.format("\n&f - &7(&c%s&7) &eBy &6%s &7(&d%s&7)", 
						o.punishment, o.staff, o.date);
			}
		}
		
		return msg.replaceAll("(?i)<basicoffences>", offencesString)
				.replaceAll("(?i)<num>", ""+Colour.num(offences.length))
				.replaceAll("(?i)<user>", n)
				.replaceAll("(?i)<col>", Colour.numColour(offences.length));
	}
	
	public static String sidebarReplace(String msg, String line) {
		return msg
				.replaceAll("(?i)<sidebar>", "\n" + ChatColor.BOLD + Sidebar.getTitle() + ChatColor.RESET + "\n" + String.join("\n", Sidebar.getLines()));
	}

	private static String rankReplace(String msg, String line) {
		String[] tokens = line.split("\\s+");
		String name = tokens[tokens.length-1];
		String n = "", col = "";
		int rank = -1, rating = -1;
		String ratings = "&e";
		if (name.matches("\\w+")) {
			if (line.matches("(?i).*rank  ?\\w+")) {
				Pattern p = Pattern.compile("(?i)(?<=rank  ?)\\w+");
				Matcher m = p.matcher(line);
				
				m.find();
				n = m.group();
			} else {
				n = Chat.getName(line);
			}
			
	
			Leaderboard.Player[] pl = Leaderboard.findPlayers(n);
			if (Leaderboard.players.containsKey(n.toLowerCase())) {
				pl = new Leaderboard.Player[] {pl[0]};
				rank = Leaderboard.rank(n);
				rating = Leaderboard.rating(n);
//				col = Colour.numColour(Leaderboard.getStats(n).size());
				col = Leaderboard.col(n);
			} else {
				Arrays.sort(pl, Comparator.comparing((Leaderboard.Player a) -> -a.rating2()));
				if (pl.length > 0) {
					rank = Leaderboard.rank(pl[0].lname);
					rating = pl[0].rating2();
					col = Colour.numColour(pl[0].getStats().size());
				}
			}
			
			if (pl.length <= 10) { 
				for (Leaderboard.Player p : pl) {
					n = p.name;
					int r = Leaderboard.rank(p.name);
					col = Colour.numColour(p.getStats().size());
					ratings += String.format("\n - %s%s (Rating: %s%d&e)", 
							Font.ljust("#"+r+".", 38, "&e"), Font.ljust(p.name, 102, "&e"), Colour.numColour(p.getStats().size()), p.rating2());
				}
			} else {
				ratings = "\n&e" + String.join(", ", Arrays.stream(pl).map(p -> p.name).toArray(String[]::new));
			}
		} else if (name.matches("#\\d+")) {
			rank = Integer.parseInt(name.substring(1));
			if (rank < Leaderboard.ratings2.size()) {
				Leaderboard.Player p = Leaderboard.ratings2.get(rank-1);
				n = p.name;
				col = Colour.numColour(p.getStats().size());
				rating = p.rating2();
//				ratings += String.format("\n - %s%s (Rating: %s%d&e)", 
//						Font.ljust("#"+rank+".", 32, "&e"), Font.ljust(n, 102, "&e"), col, rating);
				
				int i, j = rank - 1;
				int min, max;
				if (j < 2) {
					min = 0;
					max = min + 5;
				} else if (j > Leaderboard.ratings2.size()-3) {
					min = j-5;
					max = Leaderboard.ratings2.size();
				} else {
					min = j - 2;
					max = j + 3;
				}
				
				for (i = min; i < max; i++) {
					Player k = Leaderboard.ratings2.get(i);
					int r = Leaderboard.rank(k.name);
//					if (i == j) {
//						ratings += String.format("\n&f&l - &e&l%s &e&l%s&7&l[%s&l%s&7&l]", 
//								Font.ljust("#"+r+".", 20), Font.ljust(k.name, 102), Colour.numColour(k.getStats().size()), k.rating2());// p.rating2_());
//					} else {
//						ratings += String.format("\n&f - &e%s &e%s&7[%s&7]", 
//								Font.ljust("#"+r+".", 20), Font.ljust(k.name, 102), Colour.numColour(k.getStats().size()) + k.rating2());
//					}
					ratings += String.format("\n - %s (Rating: %s%d&e) %s", 
							Font.ljust("#"+r+".", 32, "&e"), Colour.numColour(k.getStats().size()), k.rating2(), k.name);

				}
			}
		}
		
		
				
		return msg.replaceAll("(?i)<rating>", ""+rating)
				.replaceAll("(?i)<rank>", ""+rank)
				.replaceAll("(?i)<user>", n)
				.replaceAll("(?i)<col>", col)
				.replaceAll("(?i)<just>", Font.just(n, 108))
				.replaceAll("(?i)<ranks>", ratings);
	}
	
	private static String rankingsReplace(String msg, String line) {		
		String rankings = "";
		int num = 0;
		
		List<String> players = The5zigAPI.getAPI().getServerPlayers().stream()
				.map(p -> p.getGameProfile().getName())
				.sorted(Comparator.comparing((String n) -> -Leaderboard.rating(n)))
				.collect(Collectors.toList());
		for (String name : players) {
			int rating = Leaderboard.rating(name);
			if (rating == 0) continue;
			int rank = Leaderboard.rank(name);
			String col = Colour.numColour(Leaderboard.getStats(name).size());
			num++;
			
			rankings += String.format("\n&e - %s(Rating: %s) %s", 
					Font.ljust("#"+rank+".", 42, "&e"), Font.rjust(""+rating, 24, "&e"), name);
		}
		
		
		return msg.replaceAll("(?i)<rankings>", rankings)
				.replaceAll("(?i)<num>", ""+num)
				.replaceAll("(?i)<total>", ""+players.size())
				.replaceAll("(?i)<col>", Colour.numColour(num));
	}
	/*private static String equationReplace(String line) {
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
	}*/
}
