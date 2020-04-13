package com.dash.message.condition;

public abstract class SubCondition {
	protected boolean inverted = false;
	
	public abstract boolean matches(String line);	
	
	public SubCondition inverted(boolean inverted) {
		this.inverted = inverted;
		return this;
	}
	
	public final boolean isInverted() {
		return inverted;
	}
	
	public abstract String toString();
}
