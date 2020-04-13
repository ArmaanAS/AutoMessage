package com.dash.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.dash.commands.AutoComplete;
import com.dash.commands.Command;
import com.dash.gui.AutoMessagesGui;
import com.dash.message.AutoGG;
import com.dash.message.AutoMessage;
import com.dash.message.Specials;
import com.dash.message.condition.Condition;
import com.dash.message.condition.PresetCondition;
import com.dash.utils.Appeals;
import com.dash.utils.Appeals.Offence;
import com.dash.utils.Chat;
import com.dash.utils.Colour;
import com.dash.utils.Debug;
import com.dash.utils.DelayedSend;
import com.dash.utils.Font;
import com.dash.utils.Leaderboard;
import com.dash.utils.Leaderboard.Player;
import com.dash.utils.Leaderboard.Stat;
import com.dash.utils.Thank;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.event.ChatEvent;
import eu.the5zig.mod.event.ChatSendEvent;
import eu.the5zig.mod.event.ChestSetSlotEvent;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.event.LoadEvent;
import eu.the5zig.mod.event.PayloadEvent;
import eu.the5zig.mod.event.ServerJoinEvent;
import eu.the5zig.mod.event.ServerQuitEvent;
import eu.the5zig.mod.event.TickEvent;
import eu.the5zig.mod.event.WorldTickEvent;
import eu.the5zig.mod.gui.GuiWelcome;
import eu.the5zig.mod.gui.ingame.ItemStack;
import eu.the5zig.mod.modules.items.server.ServerPlayers;
import eu.the5zig.mod.plugin.LoadedPlugin;
import eu.the5zig.mod.plugin.Plugin;
import eu.the5zig.mod.render.GuiIngame;
import eu.the5zig.mod.util.NetworkPlayerInfo;
import eu.the5zig.mod.util.Wrapped;
import eu.the5zig.util.StringUtil;
import eu.the5zig.util.minecraft.ChatColor;

import org.h2.util.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

@Plugin(name = "Auto GG", version = "Release 3.2") // "Release 3.0 ") 
public class Main {	
	private List<String> messages;
	public static long time = System.currentTimeMillis();
	public static int tick = 0;
	public static boolean who = false,
			party = false, mult = false,
			loaded = false, flist = false,
			specials = true;
	public static final String name = The5zigAPI.getAPI().getGameProfile().getName();
	
	
	@EventHandler
	public void onLoad(LoadEvent event) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// Initialise Variables
		long t = System.currentTimeMillis();
		messages = new ArrayList<String>();
		tick = 0;
				
		// Print presets
//		for (Field f : PresetCondition.class.getDeclaredFields()) {
//			if (Modifier.isStatic(f.getModifiers())) {
//				if (f.getType() == Condition.class) {
//					Debug.chatState(f.getName() + " = " + ((Condition) f.get(this)).toString());
//				}
//			}
//		}
		
		Chat.out("");
		
		// Initialise Statehandler and AutoGG class
		The5zigAPI.getLogger().info("Initialising state handler...");
		StateHandler.init();
		AutoGG.init();
		
		// Load config
		The5zigAPI.getLogger().info("Initialising config...");
		Config.init();	
		
		// Update state handler, rank and party state
		if (The5zigAPI.getAPI().isInWorld()) {
			try {
				if (The5zigAPI.getAPI().getServer().contains("cube")) {
					who = true;
					party = true;
					flist = true;
//					mult = true;
					The5zigAPI.getAPI().sendPlayerMessage("/pc");
					The5zigAPI.getAPI().sendPlayerMessage("/who");
					The5zigAPI.getAPI().sendPlayerMessage("/f list");
					Thank.thankAll();
					time = System.currentTimeMillis();
				}
			} catch (NullPointerException e) {}
		}
		
		// Leadeboards
		new Thread(() -> {
			The5zigAPI.getLogger().info("Loading leaderboards...");
			Leaderboard.loadLeaderboards();
			The5zigAPI.getLogger().info("Loaded leaderboards.");
		}).start();
		
		// Print number of messages loaded
		The5zigAPI.getLogger().info(Config.getAutoMessages().size() + " messages loaded.");
		Debug.chatDebug(Config.getAutoMessages().size() + " messages loaded.");

//		The5zigMod.getVars().displayScreen(
//			new AutoMessagesGui(
//				The5zigMod.getVars().getCurrentScreen() 
//			)
//		);
		
		The5zigAPI.getLogger().info("Finished loading Auto GG");
	}
	
	@EventHandler
	public void stateCheck(ServerJoinEvent event) {
		if (event.getHost().contains("cube")) {
			time = System.currentTimeMillis();
			who = true;
			party = true;
			mult = true;
			flist = true;
			The5zigAPI.getAPI().sendPlayerMessage("/pc");
//			The5zigAPI.getAPI().sendPlayerMessage("/who");
			DelayedSend.SendMessage("/who", Chat.STANDARD, 500);
			DelayedSend.SendMessage("/f list", Chat.STANDARD, 3000);
			
			Thank.thankAll();
		}
	}
	
	@EventHandler
	public static void inventory(ChestSetSlotEvent event) {
		if (mult) {
//			Debug.chatDebug("Title: " + event.getContainerTitle());
//			Debug.chatDebug("Name: " + event.getItemStack().getDisplayName());
			Thank.multipliers(event);
		}
	} 
	
	@EventHandler
	public void autoGG(ChatEvent event) {
		String msg = ChatColor.stripColor(event.getMessage());
		String rawMsg = event.getMessage();
		if (!msg.startsWith("[AutoGG]")) {
			
		}
		
		StateHandler.updateStates(event, msg);
		AutoGG.timeGG(msg);
		Thank.autoThank(msg, rawMsg);
		if (specials) {
			Specials.auto(msg, rawMsg);
		}
		
		if (Config.getAutoMessages() == null) {
			return;
		}
		
		Config.getAutoMessages().forEach(i -> {
			i.sendMessage(msg, rawMsg);
		});	
	}
	
	@EventHandler
	public void commands(ChatSendEvent event) {
		String msg = event.getMessage();
		if (msg.startsWith("/t ")) {
			msg = msg.substring(3);
			String rep = "";
			for (char c : msg.toCharArray()) {
				int ord = c;
				if (ord > 32 && ord < 127) {
					rep += Character.toString((char) (256*255-32+ord));
				} else {
					rep += c;
				}
			}
			The5zigAPI.getAPI().messagePlayer(rep);
			The5zigAPI.getAPI().messagePlayer(
					ChatColor.translateAlternateColorCodes('&', Colour.recolour(msg)) + 
					ChatColor.RESET + "\nWidth: " + Font.getWidth(msg));
			
			event.setCancelled(true);
			return;
		}
		if (msg.startsWith("%")) {
			event.setCancelled(true);
			Chat.fakePartyMessage(
					Colour.forceRecolour(
							msg.substring(1)
					)
			);
			return;
		}
		if (msg.toLowerCase().startsWith("/p reinv")) {
			String name = msg.split(" ")[2];
			StateHandler.sendMessage("/p kick " + name, Chat.STANDARD, false);
			StateHandler.sendMessage("/p invite " + name, Chat.STANDARD, true);
			event.setCancelled(true);
		}
		if (msg.toLowerCase().equals("/ranked")) {
			StateHandler.ranked = !StateHandler.ranked;
			Debug.chatState("Ranked = " + StateHandler.ranked);
			
			event.setCancelled(true);
			return;
		}
		if (msg.toLowerCase().equals("/thank") || msg.toLowerCase().equals("/thanks")) {
			Thank.thankAll();
			event.setCancelled(true);
			return;
		}
		
		if (msg.toLowerCase().startsWith("/agg") || msg.toLowerCase().startsWith("autogg")) {
			String[] tokens = msg.split(" ");
			if (tokens.length == 1 || tokens[1].equals("help") || tokens[1].equals("?")) {
				Debug.chatHelp(Command.help);
				event.setCancelled(true);
			} else if (tokens.length > 1 && (tokens[1].equals("rl") || tokens[1].equals("reload"))) {
				reload();
				event.setCancelled(true);
			} else if (tokens.length > 1 && tokens[1].toLowerCase().startsWith("col")) {
				String[] codes = 
						"0 1 2 3 4 5 6 7 8 9 a b c d e f k l m n o r v x w[01234] y[01234] z[01234] y0 y1 y2 y3 y4".split(" ");
				String[] example = {
						"&0Black",
						"&1Dark Blue",
						"&2Dark Green",
						"&3Dark Aqua",
						"&4Dark Red",
						"&5Dark Purple",
						"&6Gold",
						"&7Gray",
						"&8Dark Gray",
						"&9Blue",
						"&aGreen",
						"&bAqua",
						"&cRed",
						"&dLight Purple",
						"&eYellow",
						"&fWhite",
						"&k::&f Magic",
						"&lBold",
						"&mStrike",
						"&nUnderline",
						"&oItalic",
						"&rReset",
						"&vFancy",
						"&xRainbow Text",
						"&w0Colour Wave Texts",
						"&y0Fade Colour Effect",
						"&z0Colour Dip Message",
						"&w0&m----&w0Black&m----",
						"&w1&m----&w1Blue&m----",
						"&w2&m---&w2Green&m---",
						"&w3&m----&w3Red&m----",
						"&w4&m---&w4Purple&m---"
				};
				String line = "\n&f&m------------------------------&f";
				char bar = '\u239c';
				
				String cols = "Colours: &f" + line;
				for (int i = 0; i < codes.length; i++) {
					cols += String.format("\n\u239c &7&&7%s \u239c &7%s&r", 
							Font.ljust(codes[i], 52), example[i]); //Font.ljust(example[i], 100));
					if (i == 15 || i == 21 || i == 31) {
						cols += line;
					}
				}
				Debug.chatInfo(Colour.forceRecolour(cols));
				event.setCancelled(true);
			} else {
				Debug.chatError("Unknown command: " + msg);
			}
		}
		if (msg.toLowerCase().startsWith("/lb")) {
			String[] tokens = msg.toLowerCase().split(" "),
					rtokens = msg.split(" ");
			
			
			if (tokens.length > 1 && tokens[1].startsWith("most")) {
				int m = 16;
				if (tokens.length > 2) {
					if (tokens[2].matches("=\\d+")) {
						m = Integer.parseInt(tokens[2].substring(1));
					}
				}
				String most = "";
				int last = 0, i;
				for (i = 0; i < m || last == Leaderboard.mostLB.get(i).getStats().size(); i++) {
					Player p = Leaderboard.mostLB.get(i);
					last = p.getStats().size();
					most += String.format("\n&f - &7[%s&7] &e%s", Colour.num(last), p.name);
				}
				Debug.chatInfo("\n&6&lTop players by #LB: &7(&b" + i + "&7)" + most + "\n");
				event.setCancelled(true);
				
				
			} else if (tokens.length > 1 && tokens[1].startsWith("f")) {
				Stat[] game = new Stat[0];
				if (tokens.length > 2) {
					game = Leaderboard.getLB(tokens[2]);
				} 
				if (game.length == 0) {
					Command.cmds[1].sub[5].help();
					event.setCancelled(true);
					return;
				}
				
				String name = game[0].name, rname = game[0].rname;
				Stat[] stats = new Stat[0];
				int i, j = 0, min, max;
				if (tokens.length == 3) {
					j = -1;
				} else if (tokens[3].matches("\\w{1,16}")) {
					name = tokens[3];
					stats = Leaderboard.findStats(tokens[2], name);
					Debug.chatDebug("Length: " + stats.length);
					j = Leaderboard.getLBIndex(tokens[2], name);
					if (stats.length == 0) {
						Debug.chatError(name + " is not on " + game[0].game + " &cleaderboard");
						event.setCancelled(true);
						return;
					} else if (stats.length == 1) {
						name = stats[0].name;
						rname = stats[0].rname;
						j = Leaderboard.getLBIndex(tokens[2], name);
					} else if (j > 0 && j <= game.length) {
						stats = new Stat[] {game[j]};
						name = stats[0].name;
						rname = stats[0].rname;
						j = Leaderboard.getLBIndex(tokens[2], name);
					}
				} else if (tokens[3].matches("#\\d+")) {
					int rank = Integer.parseInt(tokens[3].substring(1));
					if (rank > game.length) {
						Debug.chatError("Rank: #" + rank + " is greater than " + game.length + ".");
						return;
					} else if (rank < 1) {
						Debug.chatError("Rank: #" + rank + " is less than 1.");
						return;
					}
					rname = name = game[rank-1].rname;
					j = rank-1;
				} else if (tokens[3].matches("=\\d+")) {
					int l = Integer.parseInt(tokens[3].substring(1));
					l = l > game.length ? game.length : l;
					String f = "";
					for (i = 0; i < l; i++) {
						Stat s = game[i];
						f += String.format("\n&f - &e%s &e%s&e(%d %s&e)", 
								Font.ljust("#"+s.rank+".", 20), Font.ljust(s.rname, 102), s.wins, s.award);
					}
					Debug.chatInfo("\n&6&lTop players for &a&l" + game[0].game + "&6&l: &7(&b" + i + "&7)" + f + "\n");
					event.setCancelled(true);
					return;
				}
				
				String find = "", 
						col = Leaderboard.col(name);
				if (stats.length > 1) {
					if (stats.length < 10) {
						for (Stat s : stats) {
							find += String.format("\n&f - &e%s &e%s(%s %s)", 
									Font.ljust("#"+s.rank+".", 32), Font.ljust(s.rname, 102, "&a"), s.wins, s.award);
						}
					} else {
						find = "\n&e" + String.join(", ", Arrays.stream(stats).map(s -> s.rname).collect(Collectors.toList()));
					}
					Debug.chatInfo("\n&6Player search for &a&l" + stats[0].game + "&6: &l" + tokens[3] + " &7(" + stats.length + "&7)" + find + "\n");
				} else {
					if (j < 0) {
						min = 0;
						max = 18;
						rname = name = "";
					} else if (j < 3) {
						min = 0;
						max = min + 7;
					} else if (j > game.length-4) {
						min = game.length-7;
						max = game.length;
					} else {
						min = j - 3;
						max = j + 4;
					}
					Stat s = null;
					for (i = min; i < max; i++) {
						s = game[i];
						if (i == j) {
							find += String.format("\n&f&l - &e&l%s &e&l%s(%s %s)", 
									Font.ljust("#"+s.rank+".", 32), Font.ljust(s.rname, 102, "&a"), s.wins, s.award);
						} else {
							find += String.format("\n&f - &e%s &e%s(%s %s)", 
									Font.ljust("#"+s.rank+".", 32), Font.ljust(s.rname, 102, "&a"), s.wins, s.award);
						}
					}
					if (j < 0) {
						Debug.chatInfo("\n&a&l" + s.game + " &6Leaderboard: &7(&b18&7)" + find + "\n");
					} else {
						Debug.chatInfo("\n&6&l" + rname + "&6's rank on &a&l" + s.game + " &6Lb: &7(" +Leaderboard.col(name) + "&l#" + (j+1) + "&7)" + find + "\n");
					}
				}
				event.setCancelled(true);
				
				
			} 
//				else if (tokens.length > 1 && tokens[1].startsWith("r") && tokens[1].contains("1")) {
//				String most = "";
//				int i;
//				if (tokens.length > 2) {
//					String name = tokens[2];
//					if (Leaderboard.players.containsKey(name)) {
//						Player pl = Leaderboard.players.get(name);
//						int j = Leaderboard.ratings1.indexOf(pl);
//						int min, max;
//						if (j < 3) {
//							min = 0;
//							max = min + 7;
//						} else if (j > Leaderboard.ratings1.size()-3) {
//							min = j-3;
//							max = Leaderboard.ratings1.size()-1;
//						} else {
//							min = j - 3;
//							max = j + 4;
//						}
//						for (i = min; i < max; i++) {
//							Player p = Leaderboard.ratings1.get(i);
//							if (i == j) {
//								most += String.format("\n&f&l - &e&l%s &e&l%s&7[%s%s&7]", 
//										Font.ljust("#"+(i+1)+".", 20), Font.ljust(p.name, 102), Colour.numColour(p.getStats().size()), p.rating1());
//							} else {
//								most += String.format("\n&f - &e%s &e%s&7[%s&7]", 
//										Font.ljust("#"+(i+1)+".", 20), Font.ljust(p.name, 102), Colour.numColour(p.getStats().size()) + p.rating1());
//							}
//						}
//						Debug.chatInfo("\n&6Rating1 ranking for: &a&l" + pl.name + " &7(&b" + (max-min) + "&7)" + most + "\n");
//					
//					} else {
//						Debug.chatError(rtokens[2] + " is not on any leaderboards.");
//					}
//				} else {
//					for (i = 0; i < 18; i++) {
//						Player p = Leaderboard.ratings1.get(i);
//						most += String.format("\n&f - &e%s &e%s&7[%s&7]", 
//								Font.ljust("#"+(i+1)+".", 20), Font.ljust(p.name, 102), Colour.numColour(p.getStats().size()) + p.rating1());
//					}
//					Debug.chatInfo("\n&6&lTop players by Ratings1: &7(&b" + i + "&7)" + most + "\n");
//				}
//				event.setCancelled(true);
//			} 
			
			
			else if (tokens.length > 1 && tokens[1].matches("r(?!l|eload).*")) {  // && tokens[1].contains("2")) {
				String rating = "";
				int i, j = 0, l = -1;
				if (tokens.length > 2) {
					String name = "", rname = "";
					Player[] pl;
					
					if (tokens[2].matches("\\w{1,16}")) {
						name = tokens[2].toLowerCase();
						
						pl = Leaderboard.findPlayers(name);
						j = Leaderboard.getRatingsIndex(name);
						
						if (pl.length == 0) {
							Debug.chatError("Could not find any player containing: " + name);
							event.setCancelled(true);
							return;
						} else if (pl.length == 1) {
							name = pl[0].name;
							rname = pl[0].name;
							j = Leaderboard.getRatingsIndex(name);
						} else if (j >= 0) {
							pl = new Player[] {Leaderboard.ratings2.get(j)};
							rname = pl[0].name;
							name = rname.toLowerCase();
							j = Leaderboard.getRatingsIndex(name);
						} else {
							name = "";
							String players = "";
							Arrays.sort(pl, Comparator.comparing((Player p) -> -p.rating2()));
							if (pl.length < 10) {
								for (Player p : pl) {
									int rank = Leaderboard.rank(p.name);
									String col = Colour.numColour(p.getStats().size());
									players += String.format("\n&f - &e%s %s &7[%s%s&7]", 
											Font.ljust("#"+rank+".", 32, "&e"), Font.ljust(p.name, 102, "&e"), col, p.rating2());
								}
							} else {
								players = "\n&e" + String.join(", ", Arrays.stream(pl).map(p -> p.name).collect(Collectors.toList()));
							}
							Debug.chatInfo("\n&6Leaderboard search for: &l" + tokens[2] + " &7(&b" + pl.length + "&7)" + players + "\n");		
							event.setCancelled(true);
							return;
						}

					} else if (tokens[2].matches("#\\d+")) {
						j = Integer.parseInt(tokens[2].substring(1)) - 1;
						if (j >= Leaderboard.ratings2.size()) {
							Debug.chatError("#" + (j+1) + " is greater than " + Leaderboard.ratings2.size() + ".");
							event.setCancelled(true);
							return;
						}
						rname = Leaderboard.ratings2.get(j).name;
						name = rname.toLowerCase();
					} else if (tokens[2].matches("=\\d+")) {
						l = Integer.parseInt(tokens[2].substring(1));
						l = l > Leaderboard.ratings2.size() ? Leaderboard.ratings2.size() : l;
						for (i = 0; i < l; i++) {
							Player p = Leaderboard.ratings2.get(i);
							int rank = Leaderboard.rank(p.name);
							rating += String.format("\n&f - &e%s &e%s&7[%s&7]", 
									Font.ljust("#"+rank+".", 20), Font.ljust(p.name, 102), Colour.numColour(p.getStats().size()) + p.rating2());
						}
						Debug.chatInfo("\n&6&lTop players by Ratings: &7(&b" + i + "&7)" + rating + "\n");
						event.setCancelled(true);
						return;
					} else {
						Command.cmds[1].sub[1].help();
						event.setCancelled(true);
						return;
					}
					
					int min, max;
					if (j < 3) {
						min = 0;
						max = min + 7;
					} else if (j > Leaderboard.ratings2.size()-4) {
						min = j-7;
						max = Leaderboard.ratings2.size();
					} else {
						min = j - 3;
						max = j + 4;
					}
					Player r = Leaderboard.ratings2.get(j);
					for (i = min; i < max; i++) {
						Player p = Leaderboard.ratings2.get(i);
						int rank = Leaderboard.rank(p.name);
						if (i == j) {
							rating += String.format("\n&f&l - &e&l%s &e&l%s&7&l[%s&l%s&7&l]", 
									Font.ljust("#"+rank+".", 20), Font.ljust(p.name, 102), Colour.numColour(p.getStats().size()), p.rating2());// p.rating2_());
						} else {
							rating += String.format("\n&f - &e%s &e%s&7[%s&7]", 
									Font.ljust("#"+rank+".", 20), Font.ljust(p.name, 102), Colour.numColour(p.getStats().size()) + p.rating2());
						}
					}
					Debug.chatInfo("\n&a&l" + rname + "&6's leaderboard rating: &a&l" + "&7(" + Leaderboard.col(name) + "&l" + r.rating2() + "&7)" + rating + "\n");
				
				} else {
					for (i = 0; i < 18; i++) {
						Player p = Leaderboard.ratings2.get(i);
						int rank = Leaderboard.rank(p.name);
						rating += String.format("\n&f - &e%s &e%s&7[%s&7]", 
								Font.ljust("#"+rank+".", 20), Font.ljust(p.name, 102), Colour.numColour(p.getStats().size()) + p.rating2());
					}
					Debug.chatInfo("\n&6&lTop players by Ratings: &7(&b" + i + "&7)" + rating + "\n");
				}
				event.setCancelled(true);
				
				
			} else if (tokens.length > 1 && tokens[1].startsWith("min")) {
				if (tokens.length > 2 && Leaderboard.games.containsKey(tokens[2])) {
					Stat[] game = Leaderboard.games.get(tokens[2]);
					String min = "";
					for (int i = game.length-5; i < game.length; i++) {
						Stat s = game[i];
						min += String.format("\n&f - &e#%d. %s &a(%d %s)", s.rank, Font.ljust(s.rname, 16*6, "&e"), s.wins, s.award);
					}
					Debug.chatInfo("\n&6Lowest Ranked players for &a&l" + game[0].game + "&6: " + min + "\n");
				} else {
					Command.cmds[1].sub[3].help();
				}
				event.setCancelled(true);
				
				
			} else if (tokens.length > 1 && tokens[1].startsWith("top")) {
				if (tokens.length > 2 && Leaderboard.games.containsKey(tokens[2])) {
					Stat[] game = Leaderboard.games.get(tokens[2]);
					String min = "";
					for (int i = 0; i < 5; i++) {
						Stat s = game[i];
						min += String.format("\n&f - &e#%d.   %s  &a(%d %s)", 
								s.rank, Font.ljust(s.rname, 16*6+2, "&e"), s.wins, s.award);
					}
					Debug.chatInfo("\n&6Highest Ranked players for &a&l" + game[0].game + "&6: " + min + "\n");
				} else {
					Command.cmds[1].sub[3].help();
				}
				event.setCancelled(true);
				
				
			} else if (tokens.length > 1 && (tokens[1].equals("reload") || tokens[1].equals("rl"))) {
//				Leaderboard.loadLeaderboards();
				time = System.currentTimeMillis();
				new Thread(Leaderboard::loadLeaderboards).start();
				event.setCancelled(true);
				return;
				
				
			} else if (tokens.length > 2 && tokens[1].startsWith("l")) {
				String name = tokens[2].toLowerCase();
				Player[] pl = Leaderboard.findPlayers(name);
				
				if (Leaderboard.players.containsKey(name) || pl.length == 1) {
					Player p;
					if (pl.length == 1) {
						p = pl[0];
						name = p.lname;
					} else {
						p = Leaderboard.players.get(name);
					}
					List<Stat> st = p.getStats();
			
					Collections.sort(st, (a, b) -> a.rank - b.rank);
					
					String stats = "", 
							col = Leaderboard.col(name), 
							rname = p.name;
					int rating = Leaderboard.rating(name);
					
					for (Stat s : st) {
						stats += String.format("\n%s - &e%s%s (%s %s)", 
								col, Font.ljust("#"+s.rank+".", 34, "&e"), Font.ljust(s.game, 123, "&e"), s.wins, s.award);
					}
					Debug.chatInfo("\n\n " + col + "&k&l::&6&l " + rname + "&6's leaderboards: &7(" + col + st.size() + "&7) (Rating: " + col + rating + "&7)" + stats + "\n");
				} else {
										
					if (pl.length == 0) {
						Debug.chatError(name + " is not on any leaderboards.");
						event.setCancelled(true);
						return;
					} else {
						String players = "";
						Arrays.sort(pl, Comparator.comparing((Player p) -> -p.rating2()));
						if (pl.length < 10) {
							for (Player p : pl) {
								int rank = Leaderboard.rank(p.name);
								players += String.format("\n&f - &e%s %s (%s&e)", 
										Font.ljust("#"+rank+".", 32, "&e"), Font.ljust(p.name, 102, "&e"), Colour.num(p.getStats().size()));
							}
						} else {
							players = "\n&e" + String.join(", ", Arrays.stream(pl).map(p -> p.name).collect(Collectors.toList()));
						}
						Debug.chatInfo("\n&6Leaderboard search for: &l" + name + " &7(&b" + pl.length + "&7)" + players + "\n");
					}
				}
				event.setCancelled(true);
			} else {
//				Debug.chatHelp(Command.cmds[1].display());
				Command.cmds[1].help();
				event.setCancelled(true);
			}
			
			
		} else 
		if (msg.toLowerCase().startsWith("/name")) {
			String[] tokens = msg.split(" +");
			new Thread(() -> {
				if (tokens.length > 1) {
					String name = tokens[1];
					String[] names = com.dash.utils.Player.getOldNames(name);
					String nameString = String.join(", ", names);
					String col = Colour.numColour(names.length);
					
					Debug.chatInfo(
							col + "\n\n&l&k::&6&l " + name + 
							"&6's names: &7(" + col + names.length + 
							"&7)&e\n" + nameString + "\n"
					);
				} else {
					Command.cmds[3].help();
				}
			}).start();
			event.setCancelled(true);

			
		} else if (msg.toLowerCase().startsWith("/ping")) {
			String[] tokens = msg.split(" +");
			if (tokens.length > 1) {
				String name = tokens[1];
				int ping = com.dash.utils.Player.getPing(name);
				String col = Colour.pingColour(ping).toString();
				Debug.chatInfo(
						col + "\n\n&l&k::&6&l " + name + 
						"&6's ping: " + col + ping + "ms\n"
				);
				event.setCancelled(true);
			} 

			
		} else if (msg.toLowerCase().startsWith("/rep ")) {
			String[] tokens = msg.split("\\s+");
			new Thread(() -> {
				if (tokens.length > 1) {
					String name = tokens[1];
					Offence[] off = Appeals.getOffences(name);
					String col = Colour.numColour(off.length);
					
					
					String rep = "";
					for (Offence o : off) {
						if (o.current) {
							rep += String.format("\n&8(&4&l%s&8) &6By &c%s &8(&5%s&8) &f- &3%s", 
									o.punishment, o.staff, o.date, o.evidence);
						} else {
							rep += String.format("\n&7(&c%s&7) &eBy &6%s &7(&d%s&7) &f- &b%s", 
									o.punishment, o.staff, o.date, o.evidence);
						}
					}
					Debug.chatInfo(
							"\n\n&f&l&k::&6&l " + name + "&6's offences: &7(&f" + 
							off.length + "&7)" + rep + "\n"
					);
				} else {
					Command.cmds[6].help();
				}
			}).start();
			event.setCancelled(true);
			
			
		}
		if (msg.toLowerCase().equals("/ppp")) {
			The5zigAPI.getAPI().getServerPlayers().forEach(np -> {
//				Debug.chatDebug("Invalid: " + (np.getDisplayName() == null));
				try {
					Class wnpi = Class.forName("WrappedNetworkPlayerInfo");
					Object p = wnpi.cast(np);
					
					try {
						Field wrapped = p.getClass().getDeclaredField("wrapped");
						wrapped.setAccessible(true);
						try {
							Object bsa = wrapped.get(p);
							
							try {
								Method health = bsa.getClass().getDeclaredMethod("n");
								
								int h;
								try {
									h = (int) health.invoke(bsa);
									
									Debug.chatDebug(np.getGameProfile().getName() + "'s Health: " + h);
									
									try {
										Field dh = bsa.getClass().getDeclaredField("i");
										dh.setAccessible(true);
										
										int d = dh.getInt(bsa);
										
										Debug.chatDebug("dh: " + d);
									} catch (NoSuchFieldException | SecurityException e) {
										Debug.chatError("dh field not found!");
										e.printStackTrace();
									} 
								} catch (InvocationTargetException e) {
									Debug.chatError("Failed to invoke getDisplayHealth method.");
									e.printStackTrace();
								}
								
							} catch (NoSuchMethodException e) {
								Debug.chatError("Cant find Method.");
								e.printStackTrace();
							}
						} catch (IllegalArgumentException | IllegalAccessException e) {
							Debug.chatError("Can't get wrapped object bsa.");
							e.printStackTrace();
						}
						
					} catch (NoSuchFieldException | SecurityException e) {
						Debug.chatError("Field not found!");
						e.printStackTrace();
					} 
				} catch (ClassNotFoundException e) {
					Debug.chatError("Class not found!");
					e.printStackTrace();
				}
			});
			
			event.setCancelled(true);
		}
		
		if (msg.toLowerCase().startsWith("/spec")) {
			specials = !specials;
			Debug.chatInfo("Specials automessages: " + specials);
			event.setCancelled(true);
		}
		
		event.setMessage(Colour.recolour(msg));
		StateHandler.correctMessage(event);
		if (!event.isCancelled())
			The5zigAPI.getLogger().warn("You just said: " + event.getMessage());
	}
	
	public static void reload() {
		LoadedPlugin pl = The5zigMod.getAPI().getPluginManager().getPlugin("Auto GG"); 
		//Debug.chatDebug("Plugin name: " + pl.getName());
		The5zigMod.getAPI().getPluginManager().unloadPlugin(pl.getName());
		try {
			System.gc();
			The5zigMod.getAPI().getPluginManager().loadPlugin(pl.getFile());
		} catch (Throwable e) {
			//Debug.chatError("Error loading itself!");
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void leaveServer(ServerQuitEvent event) {
		StateHandler.updateStates(event);
	}
	
	@EventHandler
	public void timer(WorldTickEvent event) {
		tick++;
//		if (tick % (20*60*15) == 0) {
//			time = System.currentTimeMillis();
//			new Thread(Leaderboard::loadLeaderboards).start();
//		}
		Thank.timer(event);
		AutoComplete.onChatType();
//		if (The5zigMod.getVars().isChatOpened()) {
//			String msg = The5zigMod.getVars().getChatBoxText();
//			if (msg.length() > 0) {
//				The5zigAPI.getAPI().messagePlayerInSecondChat(msg);
//			} else {
//				The5zigAPI.getAPI().messagePlayerInSecondChat("Open!");
//				The5zigMod.getVars().typeInChatGUI("t");
//			}
//		}
	}
	
}
