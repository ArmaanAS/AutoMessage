package com.dash.utils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.dash.utils.network.Scraper;

import eu.the5zig.util.minecraft.ChatColor;

public class Appeals {
	private static Map<String, Offence[]> memo = new HashMap();
	
	public static Offence[] getOffences(String name) {
		if (memo.containsKey(name.toLowerCase())) {
			return memo.get(name.toLowerCase());
		}
		
		String url = "https://appeals.cubecraft.net/find_appeals/" + name +"/JAVA";
		String html;
		try {
			html = Scraper.readURL(url);
		} catch (KeyManagementException | NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
			Debug.chatError("Failed to load appeals for " + ChatColor.DARK_RED + name);
			return new Offence[0];
		}
			
		Pattern p = Pattern.compile("(?s)(?<=\"card-body\">).*?(?=<[^s/])");
		Matcher m = p.matcher(html);
		
		List<Offence> offences = new ArrayList<>();
		
		int count = 0;
		while (m.find()) {
			count++;
			
			String msg = m.group().trim().replaceAll("<[/\\w]+>", "");
			
			if (count > 2) {
				System.out.println(msg);	
				
				String[] tokens = msg.split("(You are |You were | for | at | by )");
				
				for (String token : tokens) {
					System.out.println(token);
				}
				System.out.println();
				
				offences.add(
					new Offence(
							msg.startsWith("You are"),
							StringUtils.capitalize(tokens[1]), 
							tokens[2], 
							tokens[3].split("(?<=\\d) (?=\\d)")[0], 
							tokens[4].replace("&quot;", "\"")
						)
				);
			}
		}
		
//		System.out.println("Count: " + count);
		
		Offence[] o = offences.toArray(new Offence[offences.size()]);
		memo.put(name.toLowerCase(), o);
		
		return o;
	}
	
	public static class Offence {
		public String punishment, staff, date, evidence;
		public boolean current = false;
		
		public Offence(boolean current, String punishment, String staff, String date, String evidence) {
			this.current = current;
			this.punishment = punishment;
			this.staff = staff;
			this.date = date;
			this.evidence = evidence.replaceAll("http", " http");
		}
	}
}
