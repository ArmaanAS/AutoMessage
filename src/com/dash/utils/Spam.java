package com.dash.utils;

public enum Spam {
	
	NORMAL(false, true), 
	SUPPRESS(false, false), 
	ALLOW(true, false);
	
	boolean canSpam, warn;
	
	Spam(boolean canSpam, boolean warn) {
		this.canSpam = canSpam;
		this.warn = warn;
	}

	public boolean canSpam() {
		return canSpam;
	}

	public boolean warn() {
		return warn;
	}	
}
