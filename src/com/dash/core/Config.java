package com.dash.core;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.dash.message.AutoMessage;
import com.dash.message.condition.Condition;
import com.dash.message.condition.Condition.InvalidPresetException;
import com.dash.message.condition.Condition.ReplaceIncompatibleException;
import com.dash.message.condition.Condition.SyntaxException;
import com.dash.utils.ConfigParser;
import com.dash.utils.Debug;

import eu.the5zig.mod.The5zigAPI;

public class Config {
	
	public final static String name = 
			System.getProperty("user.dir") + "/the5zigmod/plugins/Messages.txt";
	private static File config;
	
	private static List<String> lines = Arrays.asList(
		//"// Config is in beta, More functionality will be added soon!", 
//		"",
//		"INFO:-",
//		"	// Words between \"s will be searched for in chat. (Case Insensitive)",
//		"	// If the word(s) are found, you will say the message in chat.",
//		"	// E.g. !\"Hey\": \"hi guys\"",
//		"	//      If |hi guys| is found anywhere in chat, you will say |Hey| in global chat",
//		"	// E.g. @\"Im coming!\": \"Party\" and \"help\" ",
//		"	//      If |Party| and |help| is found in the same sentence anywhere in chat, you will say |Im coming!| in party chat",
//		"	// There is also regex support using $\"text to find\"",
//		"	// E.g. !\"Hey, <Player>!\": [PlayerMessage] and \"<name>\" and $\" (hi)|(hey)\"",
//		"	//      If Notch in chat says |Hey Dashsmashing| I will say |Hey, Notch!|",
//		"",
//		"	// <NAME> will be automatically replaced by", 
//		"	// your name when inside \"s, E.g. \"My name is <Name>...\"",
//		"",
//		"	// If your message contains &x, the  following message will have a rainbow effect (Ranked only)",
//		"	// Chat Colour Effects:     &x  - Rainbow effect",
//		"	//                          &y* - Fade colour effect (*colour code)",
//		"	//                          &z* - Dip colour effect",
//		"	// Colour codes *:          0   - Black",
//		"	//                          1   - Blue",
//		"	//                          2   - Green",
//		"	//                          3   - Red",
//		"	// E.g. \"&y3Red dip message\" (Try typing this in chat!)",
//		"",
//		"	// Universal presets: <Name> (More added soon!)",
//		"",
//		"	// Condition presets: [GameOver],       [LevelUp],        [Killed], [Kill],",
//		"	//                    [GameJoin],       [PartyJoin],      [GameStart],",
//		"	//                    [KillAssist],     [PartyAccept],    [FriendJoin],",
//		"	//                    [Lose],           [DuelRematch],",
//		"	// Killed by Presets: [KilledWithFire], [KilledWithVoid], [KilledWithTNT],",
//		"	//                    [KilledWithShot], [KilledWithFall], [KilledWithOther]",
//		"	//                    [KilledWithMagic]",
//		"	//                    ([Killed] contains all of these)",
//		"	// Message Presets:   [PrivateMessage], [FriendMessage],  [GlobalMessage]",
//		"	//                    [PlayerMessage],  [PartyMessage],   [TeamMessage]",
//		"	//                    ([Message] includes all of these)",
//		"",
//		"	// For the presets [Killed], [Kill], [KilledWith*] in the message:",
//		"	//     <Killer> will be replaced by the name of the killer",
//		"	//     <Dead> will be replaced by the name of the player who was killed",
//		"	// [PartyJoin]:   <Player> name of the player that joined",
//		"	// [PartyAccept]: <Owner> the owner of the party ",
//		"	// [DuelRematch]: <Accept> or <Deny>",
//		"	// [FriendJoin]:  <Friend> name of friend that came online",
//		"	// [*Message]: <Player> is the name of the player who said the message",
//		"	//             <Message> is the message said",
//		"	//             <Ping> is the ping of that player",
//		"	// [PlayerMessage]: <Player>, <Rank>, <Message>, <Ping>",
//		"",
//		"	// You can get words from the line that triggered it,",
//		"	//     Replace by index of the word in chat",
//		"	// E.g. \"The third word is: <2>\": \"welcome to cubecraft\"",
//		"	// 	Line in chat \"Welcome to CubeCraft network\", you would say \"The third word is: CubeCraft\"",
//		"",
//		"	// A random message will be chosen if multiple are listed",
//		"	//     E.g. !\"msg1\", !\"msg2\", @\"msg3\": \"this text\" and not \"that text\"",
//		"	//     E.g. #\"<Dead> was good!\", #\"<Dead> was bad!\": [Kill]",
//		"",
//		"	// !<Message> is Global chat",
//		"	// @<Message> is Party chat (If possible)",
//		"	// #<Message> is Team chat (If possible)",
//		"	// <Message> (no prefix) is current, default chat, E.g. in party chat if enabled",
//		"",
//		"MESSAGES:-",
//		"	// (Prefix){Messages}:           {Conditions}",
//		"	!\"Good Game!\", !\"Well Played\":   [GameOver] or [Lose]",
//		"	!\"Good luck!\"                  : [GameStart]",
//		"	@\"&xLevel Up!\"                 : [LevelUp]",
//		"	 \"<Accept>\"                    : [DuelRematch]",
//		"	!\"&xCubelet!\"                  : \"Cubelet found\" and not \":\"",
//		"",
//		"",
//		"	// OPTIONAL MESSAGES: (remove the // to enable)",
//		"	// \"/leave\"                    : [Death]",
//		"	// \"Hey!\"                      : [GameJoin] or \" hey \" and \"<NAME>\"",
//		"	// @\"<DEAD> is easy.\"          : [Kill]",
//		"	// !\"Good Fight, <KILLER>\"     : [Killed]",
//		"	\"/f add <Player>\"           : [PlayerMessage] and \"friend\" and $\"(Dashsmashing|Forgiveme):\""
//		"	// (Prefix){Messages}:           {Conditions}",
		"",
		"!\"Good Game!\", !\"Well Played!\": [GameOver] or [Lose]",
		"!\"Good luck!\": [GameStart]",
		"",
		"@\"&xLevel Up!\": [LevelUp]",
		"",
		"\"<Accept>\": [DuelRematch]",
		"",
		"!\"&xCubelet!\": [Cubelet]",
		""
	);
	private static List<String> rawMessages;
	private static List<AutoMessage> messages;

	public static void init() {/*Debug.chatDebug(config.getAbsolutePath()); try {
		Desktop.getDesktop().open(config);
	} catch (IOException e) {
		e.printStackTrace();
	}*/
		config = new File(name);
		
		if (!config.exists()) {
			create();
		} 
		
		rawMessages = new ArrayList<>();
		messages = new ArrayList<>();
		
		The5zigAPI.getLogger().info("Loading config...");
		try {
			loadLines();
		} catch (IOException e) {
			Debug.chatError("Failed to load config.");
			e.printStackTrace();
		}
		
		The5zigAPI.getLogger().info("Registering messages...");
		registerConfigMessages();
		
		The5zigAPI.getLogger().info("Loaded config!");
	}
	
	public static List<AutoMessage> getAutoMessages() {
		return messages;
	}
	
	private static void create() {
		try {
			The5zigAPI.getLogger().info("Creating new Config...");
			config.createNewFile();
			Path file = Paths.get(name);
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			Debug.chatError("Failed to create Messages.txt file!");
			e.printStackTrace();
		}
	}
	
	private static void loadLines() throws IOException {
		rawMessages = Files.lines(Paths.get(name))
			.filter(i -> i.matches("^\\s*[\"!@#].*"))
			.collect(Collectors.toList());
	}
	
	private static void registerConfigMessages() {
		for (String line : rawMessages) {
//			The5zigAPI.getLogger().warn("Compiling: " + line);
			try {
				AutoMessage c = ConfigParser.compile(line, true);
				
				messages.add(c);
			} catch (SyntaxException e) {
				Debug.chatError("Syntax error in: " + line);
			} catch (ReplaceIncompatibleException | InvalidPresetException e) {
				e.printStackTrace();
			}
		}
	}
	
}
