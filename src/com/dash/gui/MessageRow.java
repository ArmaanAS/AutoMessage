package com.dash.gui;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.elements.IButton;
import eu.the5zig.mod.gui.elements.Row;

public class MessageRow implements Row {
	private int x, y, width;
	private boolean toRemove = false;
	private String msg;
	
	public MessageRow(String msg, int width) {
		this.msg = msg;
	}
	
	public void draw(int x, int y) {
		this.x = x;
		this.y = y;
		
		The5zigMod.getVars().drawString(msg, x + 2, y + 2);
	}
	
	public String getMessage() {
		return msg;
	}
	
	public int getLineHeight() {
		return 16;
	}
	
}
