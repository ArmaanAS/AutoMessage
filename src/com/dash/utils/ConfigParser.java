package com.dash.utils;

import java.util.ArrayList;
import java.util.List;

import com.dash.message.AutoMessage;
import com.dash.message.Condition;
import com.dash.message.MessageSet;
import com.dash.message.Preset;

import eu.the5zig.mod.The5zigAPI;

public class ConfigParser {
	
	private MessageSet messages;
	private String[] splitConditions;
	private String line;
	
	public ConfigParser(String line) {
		this.line = line;
		String[] s = split(line);
		this.splitConditions = splitConditions(s[1]);
		this.messages = new MessageSet(splitMessages(s[0]));
	}
	
	private static String[] split(String s) {		
		return s
			.split("//")[0]
			.replaceAll("(^\\s*)|(\\s*$)", "")
			.replaceAll("(?i)<NAME>", The5zigAPI.getAPI().getGameProfile().getName())
			.split("(?<=\")\\s*:\\s*(?=\\$?\"|\\[)", 2);
	}
	
	private static String[] splitConditions(String s) {
		String[] ss = s.split("(?<=(or)|(and)|(not))\\s+(?=(\\[[^\\]]*\\])|(\\$?\"[^\"]*\"))");
        
        List<String> sep = new ArrayList<String>();
        for (String i : ss) {
            String[] sss = i.split("(?<=((\\$?\".{1,75}\")|(\\])).{0,10})\\s+(?=(and)|(or)|(not))");

            for (String j : sss) {
                sep.add(j);
            }
        }
        
        return sep.toArray(new String[0]);
	}
	
	private static String[] splitMessages(String s) {
		return s.split("(?<=\")\\s*,\\s*(?=[\"#@!])");
	}
	
	public AutoMessage getCompiledAutoMessage() {
		List<Condition> conditions = new ArrayList<Condition>(),
			tempCond = new ArrayList<Condition>();
		List<String> yes = new ArrayList<String>(),
			no = new ArrayList<String>(),
			regexYes = new ArrayList<String>(),
			regexNo = new ArrayList<String>();
		boolean reject = false;
		
		for (String i : splitConditions) { //The5zigAPI.getLogger().error(i);
			boolean error = false;
			if (i.startsWith("[")) {
				Condition[] cnds = Preset.getPresets(i);
				
				if (cnds.length == 0) {
					The5zigAPI.getLogger().error("Skipped - Preset unrecognised: " + i);
					Debug.chatError("Skipped - Preset unrecognised: " + i);
					continue;
				}
				
				for (Condition k : cnds) {
					tempCond.add(k);
				}
			} else if (i.startsWith("\"")) {
				if (reject) { 
					no.add(i.substring(1, i.length()-1));
				} else {
					yes.add(i.substring(1, i.length()-1));
				}
				
				reject = false;
			} else if (i.startsWith("$\"")) {
				if (reject) { 
					regexNo.add(i.substring(2, i.length()-1));
				} else {
					regexYes.add(i.substring(2, i.length()-1));
				}
				
				reject = false;
			} else if (i.startsWith("not")) {
				reject = true;
			} else if (i.startsWith("and")) {
				continue;
			} else {
				error = true;
			}
			
		
			if (i.startsWith("or") || i.equals(splitConditions[splitConditions.length-1])) {
				
				if (tempCond.isEmpty()) {
					conditions.add(
						new Condition(
							yes,
							no,
							regexYes,
							regexNo
						)
					);
				} else {
					for (Condition c : tempCond) {
						c.addAcceptedWords(yes);
						c.addRejectedWords(no);
						c.addAcceptedRegexWords(regexYes);
						c.addRejectedRegexWords(regexNo);
						
						conditions.add(c);
					}
				}
				
				yes.clear();
				no.clear();
				regexNo.clear();
				regexYes.clear();
				tempCond.clear();
			} else {
				if (error) {
					The5zigAPI.getLogger().error("Syntax error: \"" + i + "\" in line: " + line);
					Debug.chatError("Syntax error: \"" + i + "\" in line: " + line);
				}
			}
		}
		
		AutoMessage compiledAutoMessage = new AutoMessage(
			messages, 
			conditions
		);
		
		// Debugging display this compiled Auto message
		The5zigAPI.getLogger().warn("Message(s):   " + 
			messages.getMessagesString() + 
			"\tConditions: [" 
			+ conditions.size() + "]"
		);
		for (Condition x : conditions) {
			String concat = "";
			for (String y : x.getAcceptedMessages()) {
				concat += "|" + y + "|\t";
			}
			
			for (String y : x.getAcceptedRegexMessages()) {
				concat += "$|" + y + "|\t";
			}
			concat += "- ";
			for (String y : x.getRejectedMessages()) {
				concat += "|" + y + "|\t";
			}
			
			for (String y : x.getRejectedRegexMessages()) {
				concat += "$|" + y + "|\t";
			}
			The5zigAPI.getLogger().info(" + " + concat);
		}
		
		return compiledAutoMessage;
	}
}
