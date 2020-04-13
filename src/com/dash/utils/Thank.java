package com.dash.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.stream.Collectors;

import com.dash.core.Main;
import com.dash.message.AutoMessage;
import com.dash.message.condition.Condition.InvalidPresetException;
import com.dash.message.condition.Condition.ReplaceIncompatibleException;
import com.dash.message.condition.Condition.SyntaxException;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.event.ChestSetSlotEvent;
import eu.the5zig.mod.event.WorldTickEvent;
import eu.the5zig.mod.gui.ingame.ItemStack;
import eu.the5zig.util.minecraft.ChatColor;

public class Thank {
	public static int thanking = 0;
	public static Map<String, Long> multipliers = new HashMap<>();
	public static List<String> titles = new ArrayList<>(),
			names = new ArrayList<>();
	private static AutoMessage thank;
	private static boolean close = false;
	private static int delay = 0;
	
	static {
		try {
			thank = ConfigParser.compile("\"/thank <2>\": \" has activated a Double\"");
		} catch (SyntaxException | ReplaceIncompatibleException | InvalidPresetException e) {
			e.printStackTrace();
		}
	}
	
	public static void multipliers(ChestSetSlotEvent event) {
//		Debug.chatDebug(event.getItemStack().getDisplayName());
		if (event.getContainerTitle().equals("Multipliers")) {
			ItemStack item = event.getItemStack();
			String title = item.getDisplayName();
			
			if (!title.contains("Double")) {
				Main.mult = false;
//				Debug.chatDebug("Close!");
				titles = new ArrayList<>();
				names = new ArrayList<>();
				close = true;
//				The5zigMod.getVars().closeContainer();
//				The5zigMod.getVars().closeContainer();
//				Debug.chatDebug(""+The5zigAPI.getAPI().getOpenContainerTitle());
				return;
			} else {
				titles.add(title);
				
				String name = ChatColor.stripColor(item.getLore().get(1)).substring(14);
				
				if (multipliers.containsKey(title)) return;
				if (names.contains(name)) return;
				
				names.add(name);
				thanking++;
				The5zigAPI.getAPI().sendPlayerMessage("/thank " + name);
//				Debug.chatDebug("Thanking: " + name);
				
				
				String time = ChatColor.stripColor(
						item.getLore().get(item.getLore().size()-6)
				);
				String[] times = time.split("( minutes?, | seconds?)");
				
//				for (int i = 0; i < times.length; i++) {
//					Debug.chatDebug(times[i]);
//				}
				
				int mins = 0, secs = 0;
				if (times.length == 2) {
					mins = Integer.parseInt(times[0]);
					secs = Integer.parseInt(times[1]);
				} else if (times.length == 1) {
					if (time.contains("min")) {
						mins = Integer.parseInt(times[0]);
						secs = 0;
					} else if (time.contains("sec")) {
						mins = 0;
						secs = Integer.parseInt(times[0]);
					}
				}
				long end = System.currentTimeMillis() + secs*1000 + mins*60000;
				multipliers.put(title, end);
//				Debug.chatDebug("Ending in " + (mins*60+secs) + " Seconds");
			}
			
		}
	}
	
	public static void autoThank(String msg, String rawline) {
		thank.sendMessage(msg, rawline);
	}
	
	public static void timer(WorldTickEvent event) {
		List<String> rem = new ArrayList<>();
		multipliers.forEach((name, time) -> {
			if (System.currentTimeMillis() >= time) {
				rem.add(name);
				Debug.chatDebug(name + " has expired.");
			}
		});
		rem.forEach(multipliers::remove);
//		multipliers = multipliers.keySet().stream()
//			.filter(n -> System.currentTimeMillis() >= multipliers.get(n))
//			.collect(Collectors.toMap(n -> n, n -> multipliers.get(n)));
		
		if (close) {
			The5zigMod.getVars().closeContainer();
			close = false;
		}
		
		if (delay > 0 && --delay == 0) {
			Main.mult = true;
			The5zigAPI.getAPI().sendPlayerMessage("/multipliers");			
		} 
//		else if (delay > 0) Debug.chatState("delay = " + delay);
	}
	
	public static void thankAll() {
//		Main.mult = true;
//		The5zigAPI.getAPI().sendPlayerMessage("/multipliers");
//		DelayedSend.SendMessage("/multipliers", Chat.STANDARD);
		delay = 20;
	}
}
