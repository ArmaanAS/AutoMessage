package com.dash.message;

import com.dash.message.condition.Condition.InvalidPresetException;
import com.dash.message.condition.Condition.ReplaceIncompatibleException;
import com.dash.message.condition.Condition.SyntaxException;
import com.dash.utils.ConfigParser;

public class Specials {
	public static AutoMessage[] messages = new AutoMessage[0];
	
	static {
		try {
			messages = new AutoMessage[] {
					ConfigParser.compile("@\"\\n\\n<col>&k&l::&6&l <user>&6's names: &7(<num>&7)\n&e<names>\\n\": [Names] and [PartyMessage]"),
					ConfigParser.compile("@\"\\n\\n<col>&k&l::&6&l <user>&6's ping: <userping>\\n\": [Ping] and [PartyMessage]"),
					ConfigParser.compile("@\"<names>\\n&6&l(<num>/<total>) Laggers\\n\": [Laggers] and [PartyMessage]"),
					ConfigParser.compile("@\"\\n\\n <col>&k&l::&6&l <user>&6's leaderboards: &7(<num>&7) &e<basicstats>\\n\": [Stats] and [PartyMessage] and not \"full\""),
					ConfigParser.compile("@\"\\n\\n <col>&k&l::&6&l <user>&6's leaderboards: &7(<num>&7) &e<stats>\\n\": [Stats] and [PartyMessage] and \"full\""),
					ConfigParser.compile("@\"\\n\\n <col>&k&l::&6&l <user>&6's offences: &7(<num>&7) <basicoffences>\\n\": [Offences] and [PartyMessage]"),
					ConfigParser.compile("@\"\\n\\n <col>&k&l::&6&l <user>&6's offences: &7(<num>&7) <offences>\\n\": [FullOffences] and [PartyMessage]"),
//					ConfigParser.compile("@\"\\n\\n <col>&k&l::&6&l <user>&6's Leaderboard rating:  &7(<col><rating>&7)\n&f - &e#<rank>. <user>  &e(Rating: <col>&l<rating>&e)\\n\": [Rank] and [PartyMessage]"),
					ConfigParser.compile("@\"\\n\\n <col>&k&l::&6&l <user>&6's Leaderboard rating: &7(<col><rating>&7) (Rank: <col>#<rank>&7)<ranks>\\n\": [Rank] and [PartyMessage]"),
					ConfigParser.compile("@\"\\n\\n <col>&k&l::&6&l Player Leaderboard ratings: &7(<num>&7/<total>) <rankings>\\n\": [rankings] and [PartyMessage]"),
					ConfigParser.compile("\"/f add Dashsmashing\": \"friend\" and $\"(?i)Dashsmashing:\""),
					ConfigParser.compile("\"/fmsg <player> \\n\\n <col>&k&l::&6&l <user>&6's leaderboards: &7(<num>&7) &e<basicstats>\\n\": [Stats] and [FriendMessage]"),
					ConfigParser.compile("\"/fmsg <player> \\n\\n <col>&k&l::&6&l <user>&6's Leaderboard rating: &7(<col><rating>&7)<ranks>\\n\": [Rank] and [FriendMessage]"),
			};
		} catch (SyntaxException | ReplaceIncompatibleException | InvalidPresetException e) {
			e.printStackTrace();
		}
	}
	
	public static void auto(String msg, String rawMsg) {
		for (AutoMessage a : messages) {
			a.sendMessage(msg, rawMsg);
		}
	}
}
