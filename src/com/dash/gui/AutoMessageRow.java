package com.dash.gui;

import com.dash.core.Config;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.elements.IButton;
import eu.the5zig.mod.gui.elements.RowExtended;

public class AutoMessageRow implements RowExtended {
	private int x, y, width;
	private boolean toRemove = false;
	private int AutoMessageIndex;
	private String name;
	
	public AutoMessageRow(int index, int width) {
		this.AutoMessageIndex = index;
		this.name = Config.getAutoMessages().get(index).getName();
	}
	
	public void draw(int x, int y) {
		
	}
	
	public void draw(int x, int y, int slotHeight, int mouseX, int mouseY) {
		this.x = x;
		this.y = y;
		
		The5zigMod.getVars().drawString(name, x + 2, y + 2);
	}
	
	public IButton mousePressed(int mouseX, int mouseY) {
		/*if (((this.user instanceof FriendSuggestion)) && (mouseX >= this.x + 120) && (mouseX <= this.x + 126)
				&& (mouseY >= this.y + 5) && (mouseY <= this.y + 13)) {
			The5zigMod.getFriendManager().hideSuggestion(this.user.getUniqueId());
			this.toRemove = true;
		}*/
		return null;
	}
	
	public int getIndex() {
		return AutoMessageIndex;
	}
	
	public int getLineHeight() {
		return 16;
	}
	
}
