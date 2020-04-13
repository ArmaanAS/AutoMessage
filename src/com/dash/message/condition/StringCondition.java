package com.dash.message.condition;

import com.dash.utils.Debug;

public class StringCondition extends SubCondition {
	protected String condition;
	
	public StringCondition() {}
	
	public StringCondition(boolean inverted, String condition) {
		super();
		this.inverted = inverted;
		this.condition = condition.toLowerCase();
	}
	
	public boolean matches(String line) {
		return inverted != line.toLowerCase().contains(condition);
	}

	@Override
	public String toString() {
		if (inverted) {
			return "not \"" + condition + "\"";			
		} else {
			return "\"" + condition + "\"";
		}
	}
}
