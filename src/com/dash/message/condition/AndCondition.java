package com.dash.message.condition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AndCondition extends SubCondition {
	private List<SubCondition> conditions = new ArrayList<>();
	
	public AndCondition() {}
	
	public AndCondition(boolean inverted) {
		super();
		this.inverted = inverted;	
	}
	
	public void add(SubCondition condition) {
		conditions.add(condition);
	}
	
	public boolean matches(String line) {
		for (SubCondition c : conditions) {
			if (!c.matches(line)) {
				return inverted;
			}
		}
		
		return !inverted;
	}

	@Override
	public String toString() {
		if (conditions.size() > 1) {
			List<SubCondition> c = new ArrayList<>();
			c.addAll(conditions);
			Collections.reverse(c);
			return (inverted ? "not " : "") + "(" + String.join(" and ", c.stream().map((s) -> s.toString()).collect(Collectors.toList())) + ")";
		} else {
			return "&&";
		}
	}
}
