package com.dash.gui;

import java.util.List;
import java.util.stream.Collectors;

import com.dash.core.Config;
import com.dash.utils.Debug;

import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.Gui;
import eu.the5zig.mod.gui.elements.Clickable;
import eu.the5zig.mod.gui.elements.IButton;
import eu.the5zig.mod.gui.elements.IGuiList;
import eu.the5zig.mod.gui.elements.ITextfield;

public class MessagesGui extends Gui implements Clickable<MessageRow> {
	
	private IGuiList<MessageRow> messages;
	private List<MessageRow> messageList;
	private ITextfield textBox;
	private final int guilistwidth = 150;
	private int messageIndex;
	
	public MessagesGui(Gui lastScreen, int message) {
		super(lastScreen);
		this.messageIndex = message;
	}
	
	public void initGui() {
		// Done
		addButton(MinecraftFactory.getVars().createButton(1, getWidth() / 2 - 100, getHeight() - 32, 200, 20,
				MinecraftFactory.getVars().translate("gui.done", new Object[0])));
		
		The5zigMod.getVars().drawString(
			"Add message to list", 
			getWidth() / 2 - 5 - 70, 
			20 + 8 + 10
		);
		
		messageList = Config.getAutoMessages().get(messageIndex).getMessages()
			.getMessageList().stream()
			.map(i -> i.getMessage())
			.map(i -> new MessageRow(i, guilistwidth - 10))
			.collect(Collectors.toList());
		// slotwidth, slotheight, starty, endy, startx, endx, 
		messages = The5zigMod.getVars().createGuiList(
			this, 
			guilistwidth, getHeight(), 
			40, getHeight() - 80, 
			getWidth()/2 + 5, getWidth()/2 + 5 + guilistwidth,
			this.messageList
		);
		
		textBox = The5zigMod.getVars().createTextfield(
			0, 
			getWidth() / 2 - 5 - 140, 
			40 + 8 + 10, 
			140, 14, 256
		);
		
		addGuiList(messages);
		addTextField(textBox);
		
		messages.setHeader("Messages");
		messages.setHeaderPadding(8);
		messages.setScrollX(getWidth()/2 + guilistwidth);
		messages.setLeftbound(true);
		messages.setRowWidth(guilistwidth - 10);
	}

	@Override
	protected void actionPerformed(IButton button) {
		if (button.getId() == 1) {
			The5zigMod.getVars().displayScreen(this.lastScreen);
		}
	}
	
	protected void onKeyType(char character, int key) {
		
	}
	
	@Override
	public void onSelect(int id, MessageRow row, boolean doubleClick) {
		Debug.chatDebug("Row message: ");
		textBox.setText(row.getMessage());
		getTextfieldById(0).setText(row.getMessage());
	}
	
	public String getTitleKey() {
		return "Auto GG - Message Settings";
	}
}
