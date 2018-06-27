package com.dash.gui;

import java.util.List;
import java.util.stream.Collectors;

import com.dash.core.Config;
import com.dash.message.Message;
import com.dash.utils.Debug;
import com.google.common.collect.Lists;

import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.Gui;
import eu.the5zig.mod.gui.elements.Clickable;
import eu.the5zig.mod.gui.elements.IButton;
import eu.the5zig.mod.gui.elements.IGuiList;
import eu.the5zig.util.minecraft.ChatColor;

public class AutoMessagesGui extends Gui implements Clickable<AutoMessageRow> {
	
	private IGuiList<AutoMessageRow> automessages;
	private IGuiList<MessageRow> messages;
	private List<AutoMessageRow> nameList;
	private List<MessageRow> messageList;
	private final int guilistwidth = 150;
	
	public AutoMessagesGui(Gui lastScreen) {
		super(lastScreen);
	}
	
	public void initGui() {
		// Done & Cancel
		addButton(MinecraftFactory.getVars().createButton(1, getWidth() / 2 - 100, getHeight() - 32, 200, 20,
				MinecraftFactory.getVars().translate("gui.done", new Object[0])));
		//addButton(MinecraftFactory.getVars().createButton(200, getWidth() / 2 - 155, getHeight() - 32, 150, 20,
		//		MinecraftFactory.getVars().translate("gui.cancel", new Object[0])));
		
		
		// Edit Messages & Conditions
		addButton(MinecraftFactory.getVars().createButton(3, getWidth() / 2 + 5, getHeight() - 87, 150, 20,
				MinecraftFactory.getVars().translate("Edit Messages", new Object[0])));
		addButton(MinecraftFactory.getVars().createButton(4, getWidth() / 2 + 5, getHeight() - 65, 150, 20,
				MinecraftFactory.getVars().translate("Edit Conditions", new Object[0])));
		addButton(MinecraftFactory.getVars().createButton(5, getWidth() / 2 - 5 - 150, getHeight() - 65, 150, 20,
				MinecraftFactory.getVars().translate("New Auto Message", new Object[0])));
		
		nameList = Lists.newArrayList();
		messageList = Config.getAutoMessages().get(0).getMessages()
			.getMessageList().stream()
			.map(i -> i.getMessage())
			.map(i -> new MessageRow(i, guilistwidth - 10))
			.collect(Collectors.toList());
		// slotwidth, slotheight, starty, endy, startx, endx, 
		automessages = The5zigMod.getVars().createGuiList(
			this, 
			guilistwidth, getHeight(), 
			40, getHeight() - 45 - 22, 
			getWidth()/2 - 5 - guilistwidth, getWidth()/2 - 5,
			this.nameList
		);
		messages = The5zigMod.getVars().createGuiList(
			null, 
			guilistwidth, getHeight(), 
			40, getHeight() - 80, 
			getWidth()/2 + 5, getWidth()/2 + 5 + guilistwidth,
			this.messageList
		);
		
		addGuiList(automessages);
		addGuiList(messages);
		
		automessages.setHeader(
			ChatColor.GOLD.toString() + 
			ChatColor.BOLD.toString() +
			ChatColor.UNDERLINE.toString() +
			"Auto Messages"
		);
		automessages.setHeaderPadding(8);
		automessages.setScrollX(getWidth()/2 - 10);
		automessages.setLeftbound(true);
		automessages.setRowWidth(guilistwidth - 10);
		
		messages.setHeader("Messages");
		messages.setHeaderPadding(8);
		messages.setScrollX(getWidth()/2 + guilistwidth);
		messages.setLeftbound(true);
		messages.setRowWidth(guilistwidth - 10);
		
		for (int i = 0; i < Config.getAutoMessages().size(); i++) {
			nameList.add(new AutoMessageRow(i, guilistwidth - 10));
		}
	}
	
	@Override
	protected void actionPerformed(IButton button) {
		//Debug.chatDebug("Clicked!");
		if (button.getId() == 3) {
			The5zigMod.getVars().displayScreen(
				new MessagesGui(this, 
					automessages.getSelectedRow().getIndex()
				)
			);
		} else if (button.getId() == 1) {
			The5zigMod.getVars().displayScreen(this.lastScreen);
		}
	}

	@Override
	public void onSelect(int id, AutoMessageRow msg, boolean doubleClick) {
		messageList.clear();
		for (Message i : Config.getAutoMessages()
			.get(msg.getIndex()).getMessages()
			.getMessageList()) {
				messageList.add(new MessageRow(i.getMessage(), 100000));
		}
	}
	
	public String getTitleKey() {
		return "Auto GG - Auto Messages";
	}
	
}