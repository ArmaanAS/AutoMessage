package com.dash.message;

import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.dash.core.StateHandler;
import com.dash.message.condition.Condition;
import com.dash.message.condition.PresetCondition;
import com.dash.utils.Chat;
import com.dash.utils.Debug;
import com.dash.utils.DelayedSend;
import com.dash.utils.Replace;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;

public class AutoGG implements Runnable {
	private static boolean enabled = true, xenabled = true, 
		inGame = false, xinGame = false;
	private static final boolean xD = true;
	
	public static void init() {
		enabled = xenabled = true;
		inGame = xinGame = false;
	}
	
	public static void timeGG(String msg) {
		
//		if (Preset.EquationSolver.matches(msg)) {
//			DelayedSend.SendMessage(
//				Replace.replacePresets("", Replace.EQUATION, msg), 
//				Chat.GLOBAL, new Random().nextInt(1000) + 750
//			);
//			return;
//		}
		
		if (msg.toLowerCase().contains("+1 ex") &&
			!msg.contains(":")) {
				enabled = false;
				inGame = false;
				log();
				return;
		}
		
		if (PresetCondition.getPresetSafe("gamestart").matches(msg)) {
			enabled = false;
			inGame = true;
			log();
			return;
		}
		
		if (!inGame) return;
		
		if (PresetCondition.getPresetSafe("gameover").matches(msg)) {
			enabled = true;
			inGame = false;
			timedMessage();				
			log();
			return;
		}
		
		if (PresetCondition.getPresetSafe("lose").matches(msg)) {
			enabled = true;
			inGame = false;
			timedMessage();
			log();
			return;
		}
	}
	
	private static void timedMessage() {
		new ScheduledThreadPoolExecutor(0)
			.schedule(new AutoGG(), 2, TimeUnit.SECONDS);
	}

	@Override
	public void run() {
		if (enabled && !inGame) {
//			The5zigAPI.getAPI().sendPlayerMessage("GG");
			StateHandler.sendMessage("GG", Chat.GLOBAL, true);
		}
	}
	
	private static void log() {
		if (xD) return;
		if (inGame == xinGame) {
			Debug.chatState("inGame = " + inGame);
		} else {
			xinGame = inGame;
			Debug.chatState(ChatColor.BOLD + "inGame = " + inGame);
		}
		if (enabled == xenabled) {
			Debug.chatState("muted = " + enabled);
		} else {
			xenabled = enabled;
			Debug.chatState(ChatColor.BOLD + "muted = " + enabled);
		}
	}
}
