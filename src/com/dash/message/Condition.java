package com.dash.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.dash.utils.Debug;
import com.dash.utils.Replace;
import com.dash.utils.Spam;

import eu.the5zig.mod.The5zigAPI;

public class Condition {
	
	protected List<String> 
		words = new ArrayList<String>(), 
		reject = new ArrayList<String>(), 
		regWords = new ArrayList<String>(), 
		regReject = new ArrayList<String>();
	protected boolean official = false, delayed = false;
	protected Replace replace = Replace.NONE;
	protected Spam spam = Spam.NORMAL;
	
	public Condition(String[] and, String[] not) {
		this.words.addAll(Arrays.asList(and));
		this.reject.addAll(Arrays.asList(not));
	}
	
	public Condition(List<String> and, List<String> not) {
		this.words.addAll(and);
		this.reject.addAll(not);
	}
	
	public Condition(List<String> and, List<String> not, List<String> rAnd, List<String> rNot) {
		this.words.addAll(and);
		this.reject.addAll(not);
		this.regWords.addAll(rAnd);
		this.regReject.addAll(rNot);
	}
	
	public Condition(String[] and, String[] not, boolean official, boolean delay) {
		this.words.addAll(Arrays.asList(and));
		this.reject.addAll(Arrays.asList(not));
		this.official = official;
		this.delayed = delay;
	}
	
	public Condition(String[] and, String[] not, boolean official, boolean delay, Replace replace) {
		this.words.addAll(Arrays.asList(and));
		this.reject.addAll(Arrays.asList(not));
		this.official = official;
		this.replace = replace;
		this.delayed = delay;
	}
	
	public Condition(String[] and, String[] not, boolean official, boolean delay, Spam spam) {
		this.words.addAll(Arrays.asList(and));
		this.reject.addAll(Arrays.asList(not));
		this.official = official;
		this.delayed = delay;
		this.spam = spam;
	}
	
	public Condition(String[] and, String[] not, boolean official, boolean delay, Replace replace, Spam spam) {
		this.words.addAll(Arrays.asList(and));
		this.reject.addAll(Arrays.asList(not));
		this.official = official;
		this.replace = replace;
		this.delayed = delay;
	}
	
	public Condition(List<String> and, List<String> not, List<String> rAnd, List<String> rNot, boolean official, boolean delay, Spam spam) {
		this.words.addAll(and);
		this.reject.addAll(not);
		this.regWords.addAll(rAnd);
		this.regReject.addAll(rNot);
		this.official = official;
		this.delayed = delay;
		this.spam = spam;
	}
	
	public Condition(List<String> and, List<String> not, List<String> rAnd, List<String> rNot, boolean official, boolean delay, Replace replace) {
		this.words.addAll(and);
		this.reject.addAll(not);
		this.regWords.addAll(rAnd);
		this.regReject.addAll(rNot);
		this.official = official;
		this.delayed = delay;
		this.replace = replace;
	}
	
	public Condition(List<String> and, List<String> not, List<String> rAnd, List<String> rNot, boolean official, boolean delay, Replace replace, Spam spam) {
		this.words.addAll(and);
		this.reject.addAll(not);
		this.regWords.addAll(rAnd);
		this.regReject.addAll(rNot);
		this.official = official;
		this.replace = replace;
		this.delayed = delay;
		this.spam = spam;
	}
	
	public void addAcceptedWords(List<String> w) {
		this.words.addAll(w);
	}
	
	public void addRejectedWords(List<String> w) {
		this.reject.addAll(w);
	}
	
	public void addAcceptedRegexWords(List<String> w) {
		this.regWords.addAll(w);
	}
	
	public void addRejectedRegexWords(List<String> w) {
		this.regReject.addAll(w);
	}
	
	public boolean matches(String s) {
		for (String i : words) {
			/*Debug.shadowChatState(
				s + " matches " + i + " = " + s.matches("(?i).*" + i + ".*")
			);*/
			if (!s.toLowerCase().contains(i.toLowerCase())) {
				return false;
			}
		}
		
		for (String i : reject) {
			/*Debug.shadowChatState(
				s + " contains " + i + " = " + s.toLowerCase().contains(i.toLowerCase())
			);*/
			if (s.toLowerCase().contains(i.toLowerCase())) {
				return false;
			}
		}
		
		for (String i : regWords) {
			/*Debug.shadowChatState(
				s + " matches " + i + " = " + s.matches("(?i).*" + i + ".*")
			);*/
			if (!s.matches("(?i).*" + i + ".*")) {
				return false;
			}
		}
		
		for (String i : regReject) {
			if (s.matches("(?i).*" + i + ".*")) {
				return false;
			}
		}
		
		if (this.official && !isOfficial(s)) {
			return false;
		}
		
		return true;	
	}
	
	public Replace getReplace() {
		return this.replace;
	}
	
	public boolean isOfficial(String s) {
		return !s.contains(":");
	}
	
	public List<String> getAcceptedMessages() {
		return this.words;
	}
	
	public List<String> getRejectedMessages() {
		return this.reject;
	}
	
	public List<String> getAcceptedRegexMessages() {
		return this.regWords;
	}
	
	public List<String> getRejectedRegexMessages() {
		return this.regReject;
	}
	
	public boolean getDelayed() {
		return this.delayed;
	}
	
	public Spam getSpam() {
		return spam;
	}

	public void setSpam(Spam spam) {
		this.spam = spam;
	}

	public Condition clone() {
		return new Condition(
			words, 
			reject,
			regWords,
			regReject,
			official, 
			delayed, 
			replace,
			spam
		);
	}
}
