package com.dash.commands;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.dash.utils.Debug;
import com.dash.utils.Font;
import com.dash.utils.Leaderboard;
import com.dash.utils.Leaderboard.OnlineLB;

import eu.the5zig.util.minecraft.ChatColor;

public class Command {
	public static Command[] cmds = new Command[] {
			new Command("/thank", "Auto-thank everyone with multiplier."),
			new Command("/lb", new Command[] {
					new Command("most", "Shows players on most leaderboards."),
					new Command("rating <name|#rank|=num>", "Show players leaderboard rating."),
//					new Command("r2 <name>", "Shows players with highest rating2."),
					new Command("min", Leaderboard.lbs.stream()
							.map(l -> l.games)
							.flatMap(Arrays::stream)
							.map(g -> new Command(g.game, g.name))
							.toArray(Command[]::new)),
					new Command("top", Leaderboard.lbs.stream()
							.map(l -> l.games)
							.flatMap(Arrays::stream)
							.map(g -> new Command(g.game, g.name))
							.toArray(Command[]::new)),
					new Command("rl", "Reload the leaderboards."),
					new Command("find", Leaderboard.lbs.stream()
							.map(l -> l.games)
							.flatMap(Arrays::stream)
							.map(g -> new Command(g.game + " [name|#rank|=num]", g.name))
							.toArray(Command[]::new)),
					new Command("list [name]", "Show all the lbs that player is on.")
			}),
			new Command("/agg", new Command[] {
					new Command("rl", "Reloads the plugin."),
					new Command("col", "All Minecraft / AutoGG colour codes."),
					new Command("help", "This.")
			}),
			new Command("/p reinv [name]", "Kick + Invite player."),
			new Command("/names [name]", "Old names of player."),
			new Command("/ping [name]", "Ping of player."),
			new Command("/rep [name]", "Reports / Offences of player."),
			new PartyCommand("lb <name>", "Say the leaderboards of player.\n  (Name optional)"),
			new PartyCommand("ping <name>", "Say the player's ping."),
			new PartyCommand("names <name>", "Say the player's old names."),
			new PartyCommand("rep <name>", "Say all of the player's offences."),
			new PartyCommand("laggers", "Say the ping of all players above 60ms ping."),
			new PartyCommand("rank <name|#rank>", "Say the rank and rating of player."),
			new PartyCommand("ranks", "Say the rating of all players in your lobby."),
			new FriendCommand("lb <name>", "Say the leaderboards of player."),
	};
	public String cmd, usage, u, info, prefix = "";
	public Command[] sub = new Command[0];
	public Command parent = null;
	public int depth = 0;
	static {
//		Command c = new Command("min", "");
//		Leaderboard.lbs.map(l -> l.games).fl;
//		cmds[1].sub[1] = c;
	}
	
	public static String help = "";
	static {
		for (Command c : cmds) {
			help += c.display();
		}
	}
	
	public Command(String usage, String info) {
		this.usage = usage.toLowerCase();
		if (this.usage.contains(" ")) {
			this.u = this.usage.substring(0, this.usage.indexOf(" "));
		} else {
			this.u = this.usage;
		}
		this.info = info;
	}
	
	public Command(String cmd, Command[] sub) {
		this.cmd = cmd.toLowerCase();
		for (Command c : sub) {
			c.parent = this;
			c.depth = depth + 1;
		}
		this.sub = sub;
	}
	
	public String[] getAutoComplete() {
		return Arrays.stream(sub)
				.map(c -> c.sub.length == 0 ? c.u : c.cmd)
				.toArray(String[]::new);
	}
	
	public String[] getAutoComplete(String st) {
		String s = st.toLowerCase();
		return Arrays.stream(sub)
				.map(c -> c.sub.length == 0 ? c.u : c.cmd)
				.filter(n -> n.startsWith(s))
				.toArray(String[]::new);
	}
	
	public String display() {
		return display(0);
	}
	
	public String display(int depth) {
		if (sub.length == 0) {
			if (depth == 0) {
				return ChatColor.WHITE + "\n- " + prefix + ChatColor.GOLD + usage + 
						ChatColor.WHITE +  ": " + ChatColor.GRAY + info;
			} else {
				String tabs = "\n";
				for (int i = 0; i < depth; i++) {
					tabs += "    ";
				}
				return tabs + ChatColor.WHITE + "+ " + ChatColor.YELLOW + Font.ljust(usage + 
						ChatColor.WHITE +  ": ", 40) + ChatColor.GRAY + info;				
			}
		} else {
			String ret;
			if (depth == 0) {
				ret = ChatColor.WHITE + "\n- " + ChatColor.GOLD + cmd + " ...";				
			} else {
				String tabs = "\n";
				for (int i = 0; i < depth; i++) {
					tabs += "    ";
				}
				ret = tabs + ChatColor.WHITE + "+ " + ChatColor.GOLD + cmd + " ...";
			}
			
			if (sub.length > 3 && depth > 0) {
				for (int i = 0; i < 3; i++) {
					ret += sub[i].display(depth+1);
				}
				String tabs = "\n";
				for (int i = 0; i < depth+1; i++) {
					tabs += "    ";
				}
				ret += tabs + ChatColor.GRAY + "And more... &7(&2" + getCommand() + "&7)";
			} else {
				for (Command c : sub) {
					ret += c.display(depth+1);
				}
			}
			return ret;
		}
	}
	
	public String getCommand() {
		String cmd = this.cmd;
		Command c = this.parent;
		while (c != null) {
			cmd = c.cmd + " " + cmd;
			c = c.parent;
		}
		return cmd;
	}
	
	public void help() {
		Debug.chatHelp(display());
	}
	
	
	static class PartyCommand extends Command {
		public PartyCommand(String usage, String info) {
			super(usage, info);
			this.prefix = ChatColor.LIGHT_PURPLE + "Party ";
		}
	}
	
	
	static class FriendCommand extends Command {
		public FriendCommand(String usage, String info) {
			super(usage, info);
			this.prefix = ChatColor.YELLOW + "Fmsg ";
		}
	}
}
