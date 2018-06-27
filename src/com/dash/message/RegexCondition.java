package com.dash.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.dash.utils.Debug;
import com.dash.utils.Replace;
import com.dash.utils.Spam;

import eu.the5zig.mod.The5zigAPI;

public class RegexCondition extends Condition {

	public RegexCondition(String[] and, String[] not, boolean official, boolean delay, Replace replace, Spam spam) {
		super(
			Collections.emptyList(), 
			Collections.emptyList(),
			Arrays.asList(and),
			Arrays.asList(not),
			official,
			delay,
			replace,
			spam
		);
	}
	
	public RegexCondition(String[] and, String[] not, boolean official, boolean delay, Replace replace) {
		super(
			Collections.emptyList(), 
			Collections.emptyList(),
			Arrays.asList(and),
			Arrays.asList(not),
			official,
			delay,
			replace
		);
	}
	public RegexCondition(String[] and, String[] not, boolean official, boolean delay, Spam spam) {
		super(
			Collections.emptyList(), 
			Collections.emptyList(),
			Arrays.asList(and),
			Arrays.asList(not),
			official,
			delay,
			spam
		);
	}
}
