package com.dash.core;

import java.util.ArrayList;
import java.util.List;

import com.dash.gui.AutoMessagesGui;
import com.dash.message.AutoGG;
import com.dash.utils.Colour;
import com.dash.utils.Debug;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.event.ChatEvent;
import eu.the5zig.mod.event.ChatSendEvent;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.event.LoadEvent;
import eu.the5zig.mod.event.ServerJoinEvent;
import eu.the5zig.mod.gui.GuiWelcome;
import eu.the5zig.mod.plugin.LoadedPlugin;
import eu.the5zig.mod.plugin.Plugin;
import eu.the5zig.mod.render.GuiIngame;

@Plugin(name = "Auto GG", version = "Release 2.2") 
public class Main {	
	private List<String> messages;
	public static long time = 0;
	
	@EventHandler
	public void onLoad(LoadEvent event) {
		// Initialise Variables
		messages = new ArrayList<String>();
		time = 0;
		
		// Initialise Statehandler and AutoGG class
		The5zigAPI.getLogger().info("Initialising state handler...");
		StateHandler.init();
		AutoGG.init();
		
		// Load config
		The5zigAPI.getLogger().info("Initialising config...");
		Config.init();	
		
		// Update state handler, rank and party state
		The5zigAPI.getLogger().info("Finished loading Auto GG");
		if (The5zigAPI.getAPI().isInWorld()) {
			try {
				if (The5zigAPI.getAPI().getServer().contains("cube")) {
					The5zigAPI.getAPI().sendPlayerMessage("/pc");
					The5zigAPI.getAPI().sendPlayerMessage("/who");
					time = System.currentTimeMillis();
				}
			} catch (NullPointerException e) {}
		}
		
		// Print number of messages loaded
		The5zigAPI.getLogger().info(Config.getAutoMessages().size() + " messages loaded.");
		Debug.chatDebug(Config.getAutoMessages().size() + " messages loaded.");
		The5zigMod.getVars().displayScreen(
			new AutoMessagesGui(
				The5zigMod.getVars().getCurrentScreen() //The5zigMod.getVars().getCurrentScreen()
			)
		);
	}
	
	@EventHandler
	public void stateCheck(ServerJoinEvent event) {
		if (!event.getHost().contains("cube")) return;
		
		The5zigAPI.getAPI().sendPlayerMessage("/pc");
		The5zigAPI.getAPI().sendPlayerMessage("/who");
		time = System.currentTimeMillis();
	}
	
	@EventHandler
	public void autoGG(ChatEvent event) {
		String msg = event.getMessage().replaceAll("§[0-9a-zA-Z]",	"");
		
		StateHandler.updateStates(event, msg);
		AutoGG.timeGG(msg);
		//event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
		
		//The5zigAPI.getAPI().messagePlayerInSecondChat(msg);
		//for (AutoMessage i : Config.getAutoMessages()) {
		//	i.sendMessage(msg);
			//The5zigAPI.getLogger().warn("Line: " + msg);
			//The5zigAPI.getLogger().warn("Next msg: " + i.getConditions()[0].getAcceptedMessages()[0]);
		//}
		Config.getAutoMessages().forEach(i -> {
			i.sendMessage(msg);
		});	
	}
	
	@EventHandler
	public void sendCorrections(ChatSendEvent event) {
		if (event.getMessage().equals("/aggrl")) {
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
			event.setCancelled(true);
			return;
		}
		
		The5zigAPI.getLogger().warn("You just said: " + event.getMessage());
		event.setMessage(Colour.recolour(event.getMessage()));
		StateHandler.correctMessage(event);
	}
	
}
