package com.dash.message.condition;

import com.dash.utils.Debug;

public class RegexCondition extends StringCondition {
	
	public RegexCondition(boolean inverted, String condition) {
//		super(inverted, condition);
		this.inverted = inverted;
		this.condition = condition;
	}
	
	public boolean matches(String line) {
//		if (inverted != line.matches(".*" + condition + ".*")) {
//			Debug.shadowChatDebug(line + "contains regex: " + condition);
//		}
		return inverted != line.matches(".*" + condition + ".*");
	}

	@Override
	public String toString() {
		if (this.inverted) {
			return "not $\"" + this.condition + "\"";
		} else {
			return "$\"" + this.condition + "\"";
		}
	}
}
