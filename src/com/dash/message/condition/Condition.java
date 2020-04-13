package com.dash.message.condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dash.utils.Chat;
import com.dash.utils.Debug;
import com.dash.utils.Replace;
import com.dash.utils.Spam;

import eu.the5zig.util.minecraft.ChatColor;

public class Condition {
	public SubCondition condition;
	
	public boolean 
		playerMessage = false, 
		delayed = true,
		parallel = false;
	protected Replace replace = Replace.NONE;
	protected Spam spam = Spam.NORMAL;
	
	private boolean 
		stdRep = true,
		canSpam = false, 
		warn = true, 
		error = false;
	private Set<Replace> replacements = new HashSet<>();
	
	public Condition(SubCondition c) {
		this.condition = c;
	}
		
//	public Condition(String conditions, boolean playerMessage, boolean delayed, Spam spam) throws SyntaxException {
//		this.condition = compile(conditions);
//		this.playerMessage = playerMessage;
//		this.delayed = delayed;
//		this.spam = spam;
//	}
	
	public Condition(Replace replace) {
		this.replace = replace;
		this.condition = new OrCondition();
	}
	
	public Condition(String ... conditions) {
		try {
			if (conditions.length == 1) {
				if (!isConditionsFormatCorrect(conditions[0])) {
					throw new SyntaxException(conditions[0]);
				}
				this.condition = group(conditions[0]);
			} else if (conditions.length > 1) {
				this.condition = new OrCondition();
				for (String c : conditions) {
					if (!isConditionsFormatCorrect(c)) {
						throw new SyntaxException(c);
					}
					((OrCondition) this.condition).add(group(c));
				}
			}
		} catch (SyntaxException | InvalidPresetException e) {
			e.printStackTrace();
		}
	}
	
	public Condition(boolean delayed, String ... conditions) {
		try {
			if (conditions.length == 1) {
				if (!isConditionsFormatCorrect(conditions[0])) {
					throw new SyntaxException(conditions[0]);
				}
				this.condition = group(conditions[0]);
			} else if (conditions.length > 1) {
				this.condition = new OrCondition();
				for (String c : conditions) {
					if (!isConditionsFormatCorrect(c)) {
						throw new SyntaxException(c);
					}
					((OrCondition) this.condition).add(group(c));
				}
			}
		} catch (SyntaxException | InvalidPresetException e) {
			e.printStackTrace();
		}
		this.delayed = delayed;
	}
	
	public Condition(boolean delayed, Replace replace, String ... conditions) {
		try {
			if (conditions.length == 1) {
				if (!isConditionsFormatCorrect(conditions[0])) {
					throw new SyntaxException(conditions[0]);
				}
				this.condition = group(conditions[0]);
			} else if (conditions.length > 1) {
				this.condition = new OrCondition();
				for (String c : conditions) {
					if (!isConditionsFormatCorrect(c)) {
						throw new SyntaxException(c);
					}
					((OrCondition) this.condition).add(group(c));
				}
			}
		} catch (SyntaxException | InvalidPresetException e) {
			e.printStackTrace();
		}
		this.delayed = delayed;
		this.replace = replace;
	}

	public Condition(boolean delayed, Replace replace, Spam spam, String ... conditions) {
		try {
			if (conditions.length == 1) {
				if (!isConditionsFormatCorrect(conditions[0])) {
					throw new SyntaxException(conditions[0]);
				}
				this.condition = group(conditions[0]);
			} else if (conditions.length > 1) {
				this.condition = new OrCondition();
				for (String c : conditions) {
					if (!isConditionsFormatCorrect(c)) {
						throw new SyntaxException(c);
					}
					((OrCondition) this.condition).add(group(c));
				}
			}
		} catch (SyntaxException | InvalidPresetException e) {
			e.printStackTrace();
		}
		this.delayed = delayed;
		this.replace = replace;
		this.spam = spam;
	}
	
	public Condition(boolean delayed, Spam spam, String ... conditions) {
		try {
			if (conditions.length == 1) {
				if (!isConditionsFormatCorrect(conditions[0])) {
					throw new SyntaxException(conditions[0]);
				}
				this.condition = group(conditions[0]);
			} else if (conditions.length > 1) {
				this.condition = new OrCondition();
				for (String c : conditions) {
					if (!isConditionsFormatCorrect(c)) {
						throw new SyntaxException(c);
					}
					((OrCondition) this.condition).add(group(c));
				}
			}
		} catch (SyntaxException | InvalidPresetException e) {
			e.printStackTrace();
		}
		this.delayed = delayed;
		this.spam = spam;
	}
	
	public Condition(boolean playerMessage, boolean delayed, Replace replace, boolean parallel, String ... conditions) {
		try {
			if (conditions.length == 1) {
				if (!isConditionsFormatCorrect(conditions[0])) {
					throw new SyntaxException(conditions[0]);
				}
				this.condition = group(conditions[0]);
			} else if (conditions.length > 1) {
				this.condition = new OrCondition();
				for (String c : conditions) {
					if (!isConditionsFormatCorrect(c)) {
						throw new SyntaxException(c);
					}
					((OrCondition) this.condition).add(group(c));
				}
			}
		} catch (SyntaxException | InvalidPresetException e) {
			e.printStackTrace();
		}
		this.playerMessage = playerMessage;
		this.delayed = delayed;
		this.replace = replace;
		this.parallel = parallel;
	}
	
	public Condition(boolean playerMessage, boolean delayed, String ... conditions) {
		try {
			if (conditions.length == 1) {
				if (!isConditionsFormatCorrect(conditions[0])) {
					throw new SyntaxException(conditions[0]);
				}
				this.condition = group(conditions[0]);
			} else if (conditions.length > 1) {
				this.condition = new OrCondition();
				for (String c : conditions) {
					if (!isConditionsFormatCorrect(c)) {
						throw new SyntaxException(c);
					}
					((OrCondition) this.condition).add(group(c));
				}
			}
		} catch (SyntaxException | InvalidPresetException e) {
			e.printStackTrace();
		}
		this.playerMessage = playerMessage;
		this.delayed = delayed;
	}
	
	public Condition(boolean playerMessage, boolean delayed, Spam spam, String ... conditions) {
		try {
			if (conditions.length == 1) {
				if (!isConditionsFormatCorrect(conditions[0])) {
					throw new SyntaxException(conditions[0]);
				}
				this.condition = group(conditions[0]);
			} else if (conditions.length > 1) {
				this.condition = new OrCondition();
				for (String c : conditions) {
					if (!isConditionsFormatCorrect(c)) {
						throw new SyntaxException(c);
					}
					((OrCondition) this.condition).add(group(c));
				}
			}
		} catch (SyntaxException | InvalidPresetException e) {
			e.printStackTrace();
		}
		this.playerMessage = playerMessage;
		this.delayed = delayed;
		this.spam = spam;
	}
	
	public Condition(boolean playerMessage, boolean delayed, Replace replace, String ... conditions) {
		try {
			if (conditions.length == 1) {
				if (!isConditionsFormatCorrect(conditions[0])) {
					throw new SyntaxException(conditions[0]);
				}
				this.condition = group(conditions[0]);
			} else if (conditions.length > 1) {
				this.condition = new OrCondition();
				for (String c : conditions) {
					if (!isConditionsFormatCorrect(c)) {
						throw new SyntaxException(c);
					}
					((OrCondition) this.condition).add(group(c));
				}
			}
		} catch (SyntaxException | InvalidPresetException e) {
			e.printStackTrace();
		}
		this.playerMessage = playerMessage;
		this.delayed = delayed;
		this.replace = replace;
	}
	
	public Condition(boolean playerMessage, boolean delayed, Replace replace, Spam spam, String ... conditions) {
		try {
			if (conditions.length == 1) {
				if (!isConditionsFormatCorrect(conditions[0])) {
					throw new SyntaxException(conditions[0]);
				}
				this.condition = group(conditions[0]);
			} else if (conditions.length > 1) {
				this.condition = new OrCondition();
				for (String c : conditions) {
					if (!isConditionsFormatCorrect(c)) {
						throw new SyntaxException(c);
					}
					((OrCondition) this.condition).add(group(c));
				}
			}
		} catch (SyntaxException | InvalidPresetException e) {
			e.printStackTrace();
		}
		this.playerMessage = playerMessage;
		this.delayed = delayed;
		this.replace = replace;
		this.spam = spam;
	}
	
	public Condition(boolean playerMessage, boolean delayed, Replace replace, Spam spam, boolean parallel, String ... conditions) {
		try {
			if (conditions.length == 1) {
				if (!isConditionsFormatCorrect(conditions[0])) {
					throw new SyntaxException(conditions[0]);
				}
				this.condition = group(conditions[0]);
			} else if (conditions.length > 1) {
				this.condition = new OrCondition();
				for (String c : conditions) {
					if (!isConditionsFormatCorrect(c)) {
						throw new SyntaxException(c);
					}
					((OrCondition) this.condition).add(group(c));
				}
			}
		} catch (SyntaxException | InvalidPresetException e) {
			e.printStackTrace();
		}
		this.playerMessage = playerMessage;
		this.delayed = delayed;
		this.replace = replace;
		this.spam = spam;
		this.parallel = parallel;
	}
	
	public boolean matches(String line) {
		return isPlayerMessage(line) == playerMessage && condition.matches(line);
	}
	
	public boolean isPlayerMessage(String line) {
		return Chat.getChatType(line) != Chat.OTHER;
	}
	
	public String toString() {
		if (this.condition == null) {
			Debug.chatWarn("this.condition == null");
		}
		return this.condition.toString();
	}
	
	public Replace getReplace() {
		return this.replace;
	}
	
	public boolean isDelayed() {
		return this.delayed;
	}
	
	public Spam getSpam() {
		return spam;
	}

	public void setSpam(Spam spam) {
		this.spam = spam;
	}
	
	public boolean canSpam() {
		return this.canSpam;
	}
	
	public boolean isParallel() {
		return this.parallel;
	}
	
	public boolean warn() {
		return this.warn;
	}
	
	public String replace(String msg, String line) {
		replacements.add(this.replace);
		for (Replace r : replacements) {
			msg = Replace.replacePresets(msg, r, line, stdRep);
		}
		
		return msg;
	}
	
	public Condition print() {
		System.out.println(this.toString());
		return this;
	}
	
	
	// Condition single token match = (?i)(and|or|not|\((\s*\g<1>\s*)*?\)|\$?\"(\\?.)*?\"|\[\s*\w+\s*\])
	//// Conditions full match unordered = (?i)(\s*(and|or|not|\((\s*\g<2>\s*)*?\)|\$?\"(\\?.)*?\"|\[\s*\w+\s*\])\s*)+
	// Condition full match ORDERED = (?i)((\s*(not\s*)*(\((\s*\g<1>\s*)*?\)|\$?\"(\\?.)*?\"|\[\s*\w+\s*\])\s*)(\s*(and|or)\s*\g<2>)*)
	
	// Line FULL match = (?i)([@#!]?\"(\\?.)*?\"\s*,?\s*)+\s*:\s*((\s*(not\s*)*(\((\s*\g<3>\s*)*?\)|\$?\"(\\?.)*?\"|\[\s*\w+\s*\])\s*)(\s*(and|or)\s*\g<4>)*)
	

	public static boolean isConditionsFormatCorrect(String conditions) {
		return conditions.matches("((\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*((\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*)");
//		return conditions.matches("(?i)(\\s*(not\\s*)*(\\((\\s*.*\\s*)*?\\)|\\$?\"(\\\\?.)*?\"|\\[\\s*\\w+\\s*\\])\\s*)((\\s*(and|or)\\s*)(\\s*(not\\s*)*(\\((\\s*.*\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*))*");
	}
	
	public static Condition compile(String conditions, boolean debug) throws SyntaxException, ReplaceIncompatibleException, InvalidPresetException {
		if (conditions.matches("\\[\\w+\\]")) {
			return PresetCondition.getPreset(conditions.substring(1, conditions.length()-1));
		}

		List<Condition> conds = new ArrayList<>();

		SubCondition subConds = group(conditions, conds);

		Condition o = new Condition(subConds);

		HashMap<String, Replace> rep = new HashMap<>();
		boolean stdRep = false,
			stdRepChecked = false,
			stdRepError = false;
		
	
		for (Condition c : conds) {
			// CHECK IF INVERTED 
			if (c.condition.inverted) 
				continue;
			
			if (c.replace != Replace.NONE) {
				o.replacements.add(c.replace);
				
				if (!stdRepChecked) {
					stdRep = c.replace.stdRep;
					stdRepChecked = true;
				} else {
					if (stdRep != c.replace.stdRep) {
						stdRepError = true;
						stdRep = false;
					}
				}
				if (!c.stdRep) {
					o.stdRep = false;
				}
				
				for (String r : c.replace.replacements) {
					if (rep.containsKey(r) && rep.get(r) != c.replace) {
						if (debug) {
							Debug.chatError("ConditionSet duplicate replacement: " + ChatColor.BOLD + r);
						}
						throw new ReplaceIncompatibleException(c.replace, rep.get(r));
					} else {
						rep.put(r, c.replace);
					}
				}
			}
			
			if (!c.getSpam().canSpam()) {
				o.canSpam = false;
			}
			if (c.getSpam().warn()) {
				o.warn = true;
			}
			if (c.isParallel()) {
				o.parallel = true;
			}
			if (c.isDelayed()) {
				o.delayed = true;
			}
			if (c.playerMessage) {
				o.playerMessage = true;
			}
		}
		
		if (stdRepError) {
			o.error = true;
			if (debug) {
				Debug.chatWarn("stdRep incompatible: " + o.replacements.toString() + ".\n <ping>, <player> and <message> substitution may not work.");
			}
		}
		return o;
	}
	
	public static SubCondition group(String line, List<Condition> conditions) throws SyntaxException, InvalidPresetException {
		if (line.charAt(0) == '(' && line.matches("(?i)(and|or|not|\\((\\s*(and|or|not|\\((\\s*(and|or|not|\\((\\s*(and|or|not|\\((\\s*(and|or|not|\\((\\s*(and|or|not|\\((\\s*(and|or|not|\\((\\s*(and|or|not|\\((\\s*.*?\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])")) {
			line = line.substring(1, line.length()-1);
		}

		if (!isConditionsFormatCorrect(line)) {
			throw new SyntaxException("Incorrect condition format: " + conditions);
		}

		
//		Pattern p = Pattern.compile("(?i)(and|or|not|\\((\\s*\\g<1>\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])");
		Pattern p = Pattern.compile("(?i)(and|or|not|\\((\\s*(and|or|not|\\((\\s*(and|or|not|\\((\\s*(and|or|not|\\((\\s*(and|or|not|\\((\\s*(and|or|not|\\((\\s*(and|or|not|\\((\\s*(and|or|not|\\((\\s*.*?\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])\\s*)*?\\)|\\$?\\\"(\\\\?.)*?\\\"|\\[\\s*\\w+\\s*\\])");
		Matcher m = p.matcher(line);
		
		List<SubCondition> tokens = new ArrayList<>();
		boolean inverted = false;

		while (m.find()) {
			String token = m.group();
			
			if (token.equals("not")) {
				inverted = !inverted;
			} else if (token.equals("and")) {
				tokens.add(new AndCondition(inverted));
				inverted = false;
			} else if (token.equals("or")) {
				tokens.add(new OrCondition(inverted));
				inverted = false;
			} else if (token.charAt(0) == '"') {
				tokens.add(
					new StringCondition(
						inverted, 
						token.substring(1, token.length()-1)
					)
				);
				inverted = false;
			} else if (token.charAt(0) == '$') {
				tokens.add(
					new RegexCondition(
						inverted, 
						token.substring(2, token.length()-1)
					)
				);
				inverted = false;
			} else if (token.charAt(0) == '[') {
				Condition c = PresetCondition.getPreset(token.substring(1, token.length()-1));
//					System.out.println("Preset: " + token.substring(1, token.length()-1));
				conditions.add(c);
				tokens.add(c.condition.inverted(inverted));
				
				inverted = false;
			} else if (token.charAt(0) == '(') {
				tokens.add(group(token, conditions).inverted(inverted));
				inverted = false;
			}
		}
		
		if (tokens.size() > 2) {
			for (int i = 0; i < tokens.size()-1; i++) {
				SubCondition c = tokens.get(i);
				
				if (c instanceof AndCondition) {
//					Debug.chatDebug("(" + tokens.size() + ") " + tokens.toString()); 
					
					((AndCondition) c).add(tokens.remove(i+1));
					if (i > 0) {
						((AndCondition) c).add(tokens.remove(i-1));
					}
//					Debug.chatDebug("&& (" + tokens.size() + ") " + tokens.toString()); 
					i--;
				}
			}
		}
		
		if (tokens.size() > 2) {
			for (int i = 0; i < tokens.size()-1; i++) {
				SubCondition c = tokens.get(i);
				
				if (c instanceof OrCondition) {
//					Debug.chatDebug("(" + tokens.size() + ") " + tokens.toString()); 
					
					((OrCondition) c).add(tokens.remove(i+1));
					if (i > 0) {
						((OrCondition) c).add(tokens.remove(i-1));
					}
//					Debug.chatDebug("|| (" + tokens.size() + ") " + tokens.toString()); 
					i--;
				}
			}
		}
		
//		if (tokens.size() > 0) {
//			Debug.chatInfo("Tokens: " + tokens.size() + ", Class: " + tokens.get(0).getClass().getName().split("\\.")[tokens.get(0).getClass().getName().split("\\.").length-1]);
//			Debug.chatInfo(tokens.get(0).toString());
//		}
		
//		Debug.chatDebug(tokens.get(0).toString());

		return tokens.size() == 1 ? tokens.get(0) : null;
	}
	
	public static SubCondition group(String line) throws SyntaxException, InvalidPresetException {
		return group(line, new ArrayList<>());
	}
	
	public static class SyntaxException extends Exception {
		public SyntaxException(String msg) {
			super("Syntax incorrect in: " + msg);
		}
	}
	
	public static class ParenthesisException extends Exception {
		public ParenthesisException(String msg) {
			super(msg);
		}
	}
	
	public static class ReplaceIncompatibleException extends Exception {
		public ReplaceIncompatibleException(Replace a, Replace b) {
			super("Incompatible replace: " + a + ", " + b);
		}
	}
	
	public static class InvalidPresetException extends Exception {
		public InvalidPresetException(String preset) {
			super(preset);
			Debug.chatError("Invalid preset: " + preset);
		}
	}
}
