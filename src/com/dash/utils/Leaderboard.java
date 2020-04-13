package com.dash.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;

import com.dash.core.Main;
import com.dash.utils.network.RelaxedSSLContext;
import com.dash.utils.network.Scraper;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;

public class Leaderboard {
	public static Map<String, Player> players;
	public static Map<String, Stat[]> games;
	private static Map<String, Integer> average, topAvg;
	private static Map<String, Float> factor;
	public static List<Player> mostLB, ratings1, ratings2;
	public static int[] r2ranks;
	public static int succeeded = 0, total, avg;
	private static boolean loading = false;
	private static OnlineLB[] olbs = {
			new OnlineLB("skywars", 
					new LB("ssw", "Solo Skywars", "Wins"),
					new LB("tsw", "Team Skywars", "Wins")
			),
			new OnlineLB("eggwars", 
					new LB("tew", "Team Eggwars", "Wins"),
					new LB("sew", "Solo Eggwars", "Wins")
			),
			new OnlineLB("lucky_islands", 
					new LB("sli", "Solo Lucky Islands", "Wins"),
					new LB("tli", "Team Lucky Islands", "Wins")
			),
			new OnlineLB("pvp",
					new LB("dls", "Duels", "Kills", 5f),
					new LB("ffa", "FFA", "Kills", 12f),
					new LB("asn", "Assassination", "Kills", 8f)
			),
			new OnlineLB("minerware", new LB("mw", "Minerware", "Wins")),
			new OnlineLB("tower_defence", new LB("td", "Tower Defence", "Wins")),
			new OnlineLB("parkour", new LB("pkr", "Parkour", "Gold Medals", 120, 0.1f)),
			new OnlineLB("sgsolo", new LB("ssg", "Solo Survival Games", "Wins")),
			new OnlineLB("sgteam", new LB("tsg", "Team Survival Games", "Wins")),
			new OnlineLB("blockwars", new LB("bw", "Blockwars", "Wins")),
			new OnlineLB("paintball", new LB("pb", "Paintball", "Wins")),
			new OnlineLB("linedash", new LB("ld", "Linedash", "Wins")),
			new OnlineLB("slimesurvival", new LB("sls", "Slime Survival", "Wins")),
			new OnlineLB("snowman_survival", new LB("sns", "Snowman Survival", "Medals", 6f)),
			new OnlineLB("wingrush", new LB("wr", "Wing Rush", "Wins")),
			new OnlineLB("quakecraft", new LB("qc", "Quakecraft", "Wins")),
			new OnlineLB("layerspleef", new LB("spl", "Layer Spleef", "Wins")),
			new OnlineLB("infection", new LB("inf", "Infection", "Wins")),
			new OnlineLB("archerassault", new LB("aa", "Archer Assault", "Wins")),
			new OnlineLB("ender", new LB("end", "Ender", "Wins")),
			new OnlineLB("battlezone", new LB("bz", "Battle Zone", "Wins")),
	};
	public static List<OnlineLB> lbs;
	static {
		lbs = Arrays.asList(olbs);
		factor = new HashMap<>();
		factor.put("ffa", 12f);
		factor.put("dls", 5f);
		factor.put("asn", 8f);
		factor.put("sns", 6f);
		factor.put("pkr", 0.1f);
	}
	
	private static float factor(String lb) {
		return factor.getOrDefault(lb.toLowerCase(), 1f);
	}
	
	public static List<Stat> getStats(String name) {
		name = name.toLowerCase();
		if (players.containsKey(name)) {
			return players.get(name).getStats();
		} else {
			return new ArrayList<Stat>();
		}
	}
	
	public static Player[] findPlayers(String name) {
		String c = name.toLowerCase();
		List<Player> pl = new ArrayList<>();
		players.forEach((n, p) -> {
			if (n.contains(c))  {
				pl.add(p);
			}
		});
		
		return pl.toArray(new Player[pl.size()]);
	}
	
	public static Stat[] findStats(String lb, String name) {
		String c = name.toLowerCase();
		List<Stat> st = new ArrayList<>();
		
		for (Stat s : getLB(lb.toLowerCase())) {
			if (s.name.contains(c))  {
				st.add(s);
			}
		}
		
		return st.toArray(new Stat[st.size()]);
	}
	
	public static int rank(String name) {
		if (players.containsKey(name.toLowerCase())) {
			Player p = players.get(name.toLowerCase());
			return r2ranks[ratings2.indexOf(p)];
		}
		return -1;
	}
	
	public static int rating(String name) {
		if (players.containsKey(name.toLowerCase())) {
			Player p = players.get(name.toLowerCase());
			return p.rating2();
		}
		return 0;
	}
	
	public static Stat[] getLB(String lb) {
		return games.getOrDefault(lb.toLowerCase(), new Stat[0]);
	}
	
	public static int getLBIndex(String lb, String name) {
		Stat[] game = games.getOrDefault(lb.toLowerCase(), new Stat[0]);
		name = name.toLowerCase();
		
		for (int i = 0; i < game.length; i++) {
			if (game[i].name.equals(name)) {
				return i;
			}
		}
		return -1;
	}
	
	public static int getRatingsIndex(String name) {
		name = name.toLowerCase();
		for (int i = 0; i < ratings2.size(); i++) {
			if (name.equals(ratings2.get(i).lname)) {
				return i;
			}
		}
		return -1;
	}
	
	public static String col(String name) {
		return Colour.numColour(getStats(name).size());
	}
	
	public static void loadLeaderboards() {
		if (loading) return;
		loading = true;
		Main.loaded = false;
		long t = System.currentTimeMillis();
		players = new HashMap<>();
		games = new HashMap<>();
		topAvg = new HashMap<>();
		total = 0;
		succeeded = 0;
		
		try {
//			lbs.parallelStream().forEach(OnlineLB::get);
			lbs.stream().forEach(OnlineLB::get);
		} catch (Exception e) {
			Debug.chatDebug("Caught error");
			e.printStackTrace();
		}
		if (succeeded == 0) {
			avg = 0;
		} else {
			avg = total / succeeded;
		}
//		Debug.chatState("avg = " + avg);
		players.forEach((name, player) -> player.name = player.getStats().get(0).rname);
	
		Debug.chatInfo("Leaderboards reloaded. (" + 
				ChatColor.GREEN + String.format("%.1f", 
						(System.currentTimeMillis()-t)/1000.) +
				" seconds&7)"
		);
		Debug.chatInfo(
				"Unique Players on " + ChatColor.GREEN + 
				succeeded + ChatColor.GRAY + " Leaderboards: " + 
				ChatColor.AQUA + players.size()
		);
		
		average = new HashMap<>();
		games.forEach((g, st) -> {
			int sum = 0;
			int sum_5 = 0;
			for (int i = 0; i < st.length; i++) {
				Stat s = st[i];
				if (i > 4) {
					sum_5 += s.wins;
				}
				sum += s.wins;
			}
//			Debug.chatDebug(String.format("Average %s: %d, %d", 
//					g, sum/st.length, sum_5/(st.length-5)));
			average.put(g, sum_5/(st.length-5));
		});
		
		mostLB = players.values().stream()
				.sorted(Comparator.comparing((Player p) -> -p.getStats().size()))
				.collect(Collectors.toList());
		ratings2 = players.values().stream()
				.sorted(Comparator.comparing((Player p) -> -p.rating2()))
				.collect(Collectors.toList());
		r2ranks = new int[ratings2.size()];
		int r = 0, prev = 0;
		for (int i = 0; i < r2ranks.length; i++) {
			Player p = ratings2.get(i);
			if (p.rating2() != prev) {
//				r++;
				r = i+1;
			}
			r2ranks[i] = r;
			prev = p.rating2();
		}
		
		Main.loaded = true;
		loading = false;
	}
	
//	static String readlbFile(String game) throws IOException {
//		String path = "C:\\Users\\Student\\Google Drive\\Extensions\\PageScraper\\native\\lb\\";
//		byte[] encoded = Files.readAllBytes(Paths.get(path + game + ".lb"));
//		return new String(encoded);
//	}
	
	public static Stat[][] getLeaderboard(String game, int num, int max) {
		String body = "";
		try {
			body = Scraper.readURL("https://www.cubecraft.net/leaderboards/view_leaderboard.php?network=java&game=" + game);
		} catch (IOException | KeyManagementException | NoSuchAlgorithmException e) {
			Debug.chatWarn("Failed to load " + game + " leaderboard.");
			e.printStackTrace();
			return new Stat[0][0];
		}
//		try {
//			body = readlbFile(game);
//		} catch (IOException e) {
//			Debug.chatWarn("Failed to load " + game + " leaderboard.");
//			e.printStackTrace();
//			return new Stat[0][0];
//		}
		if (!body.contains("td")) {
			The5zigAPI.getLogger().info(body);
		}
		Pattern p = Pattern.compile("(?<=<td>)\\w+(?=</td>)");
		Matcher m = p.matcher(body);
		
		Stat[][] lb = new Stat[num][max];
		String name = "";
		for (int g = 0; g < num; g++) {
			int rank = 0, rrank = 0, prev = 0; 
			for (int i = 0; i < max*3; i++) {
				if (!m.find()) {
					Debug.chatWarn("Failed to load " + game + " leaderboard.");
					The5zigAPI.getLogger().warn("m.find() returned false.");
					The5zigAPI.getLogger().warn(body);
					return new Stat[0][0];
				}
				String n = m.group();
				if (i%3 == 0) {
					rrank = Integer.parseInt(n);
					if (rrank-1 != i/3) {
						lb[g][i/3] = new Stat(i/3+1, i/3+1, "", -1);
						i = rrank*3-3;
					}
				} else if (i%3 == 1) {
					name = n;
				} else {
					int wins = Integer.parseInt(n);
					if (wins != prev) {
						rank = rrank;
//						rank++;
					}
					lb[g][i/3] = new Stat(rank, rrank, name, wins);
					prev = wins;
				}
			}
		}
		
		return lb;
	}
	
//	public static String readURL(String uri) throws IOException, KeyManagementException, NoSuchAlgorithmException {
//		HttpsURLConnection con = (HttpsURLConnection) new URL(uri).openConnection();
//		con.setSSLSocketFactory(RelaxedSSLContext.getInstance().getSocketFactory());
//		con.setRequestProperty("User-Agent", "Mozilla/5.0");
//		
//		InputStream in = null;
//		String encoding, body = "";
//		try {
//		    in = con.getInputStream();
//		} catch (IOException ioe) {
//		    if (con instanceof HttpURLConnection) {
//		        HttpURLConnection httpConn = (HttpURLConnection) con;
//		        int statusCode = httpConn.getResponseCode();
//		        if (statusCode != 200) {
//		            in = httpConn.getErrorStream();
//		        }
//		    }
//		    return "";
//		}
//		
//		encoding = con.getContentEncoding();  // ** WRONG: should use "con.getContentType()" instead but it returns something like "text/html; charset=UTF-8" so this value must be parsed to extract the actual encoding
//		encoding = encoding == null ? "UTF-8" : encoding.split(";\\s*")[1].split("=")[1];
//	
//		return IOUtils.toString(in, encoding);
//	}
	
	public static class Stat implements Comparator<Stat> {
		public String name, rname, game, award;
		public int rank, rrank, wins;
		public Stat(int rank, int rrank, String name, int wins) {
			this.rank = rank;
			this.rrank = rrank;
			this.name = name.toLowerCase();
			this.rname = name;
			this.wins = wins;
		}
		
		public Stat game(String a) {
			this.game = a;
			return this;
		}
		
		public Stat award(String a) {
			this.award = a;
			return this;
		}

		@Override
		public int compare(Stat a, Stat b) {
			return b.rank - a.rank;
		}
	}
	
	public static class Player {
		public Map<String, Stat> games = new HashMap<>();
		public String name, lname;
		private float rating2 = -1;
		
		public Player name(String name) {
			this.name = name;
			this.lname = name.toLowerCase();
			return this;
		}
		
		public List<Stat> getStats() {
			return new ArrayList(games.values());
		}
		
		public int rating2_() {
			for (String g : games.keySet()) {
				Stat s = games.get(g);
				Debug.chatDebug("Multiplier: " + ((float) s.wins / Leaderboard.games.get(g)[4].wins) + 
						", Added: " + (40 + (Leaderboard.games.get(g).length - s.rank) *
						(1 + ((float) s.wins / Leaderboard.games.get(g)[4].wins))));	
			}
			return (int) rating2;
		}
		
		public int rating2() {
			if (rating2 == -1) {
				float r2 = 0;
				for (String g : games.keySet()) {
					Stat s = games.get(g);
					Stat[] game = Leaderboard.games.get(g);
//					r2 += (40 + Leaderboard.games.get(g).length - s.rank)
////							* (1 + ((float) s.wins / game[4].wins)) + 
//							* (1 + ((float) s.wins / topAvg.get(g))) + 
////							((float) s.wins / game[game.length-1].wins);
////							((float) 2 * s.wins / average.get(g));
//							((float) 10 * s.wins / Leaderboard.avg / factor(g));
					boolean top5 = s.rank <= 5,
							top50 = s.rank <= 50;
					float topX = top50 ? top5 ? 3 : 1.5f : 1;
					r2 += (
							20 + (Leaderboard.games.get(g).length - s.rank)
							+ (float) s.wins / topAvg.get(g)
							* (float) s.wins / factor(g) / avg
							* (float) s.wins / average.get(g)
							) * topX;
				}
				rating2 = r2;
			}
			return (int) rating2;
		}
	}
	
	public static class LB {
		public String game, name, award;
		public int max;
		public float factor = 1;
		
		public LB(String game, String name, String award) {
			this.game = game;
			this.name = name;
			this.award = award;
			max = 200;
		}
		
		public LB(String game, String name, String award, float factor) {
			this.game = game;
			this.name = name;
			this.award = award;
			max = 200;
			this.factor = factor;
		}
		
		public LB(String game, String name, String award, int max) {
			this.game = game;
			this.name = name;
			this.award = award;
			this.max = max;
		}
		
		public LB(String game, String name, String award, int max, float factor) {
			this.game = game;
			this.name = name;
			this.award = award;
			this.max = max;
			this.factor = factor;
		}
	}
	
	public static class OnlineLB {
		public String url;
		public LB[] games;
		
		public OnlineLB(String url, LB ... games) {
			this.url = url;
			this.games = games;
		}
		
		public void get() {
			Stat[][] stats = getLeaderboard(url, games.length, games[0].max);
			if (stats.length == games.length) succeeded += games.length;
			else return;
			
			Player p;
			for (int i = 0; i < games.length; i++) {
				LB g = games[i];
				int total = 0, ttotal = 0, tn = 0;
				
				for (Stat s : stats[i]) {
					if (players.containsKey(s.name)) {
						p = players.get(s.name);
					} else {
						p = new Player().name(s.rname);
						players.put(s.name, p);
					}
					p.games.put(g.game, s.game(g.name).award(g.award));
					
					total += s.wins;
					if (s.rrank <= 5 && s.wins > 0) {
						ttotal += s.wins;
						tn++;
					}
				}
				Leaderboard.games.put(g.game, stats[i]);
				
				total /= g.factor * stats[i].length;
				Leaderboard.total += total;
//				Debug.chatDebug(g.game + " avg += " + total);
				
				Leaderboard.topAvg.put(g.game, ttotal / tn);
//				Debug.chatDebug(g.game + " top avg = " + ttotal / tn);
			}
		}
	}
	
	public static void main(String[] args) throws MalformedURLException, IOException {
//		String out = readURL("https://www.cubecraft.net/leaderboards/view_leaderboard.php?network=java&game=skywars");
//		System.out.println(out);
		
//		System.out.println(readlbFile("skywars"));
//		loadLeaderboards();
//		
//		List<Stat> stats = Leaderboard.getStats("Galodox");
//		String statsString = "";
//		
//		for (Stat s : stats) {
//			statsString += "#" + s.rank + ". " + s.game + 
//					" [" + s.wins + " " + s.award + "], ";
//		}
//		if (statsString.length() > 0)
//		statsString = statsString.substring(0, statsString.length()-2);
//		
//		System.out.println(players.get("galodox").rating2());
//		
//		System.out.println(statsString);
	}
}
