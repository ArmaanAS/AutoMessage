/**
 * 
 */
package com.dash.message;

import java.util.List;

import com.dash.core.StateHandler;
import com.dash.utils.Chat;
import com.dash.utils.Colour;
import com.dash.utils.Debug;
import com.dash.utils.Replace;
import com.dash.utils.Spam;

import eu.the5zig.mod.The5zigAPI;

/**
 * @author Armaan
 *
 */
public class AutoMessage {
	private MessageSet set;
	private List<Condition> conditions;
	private long time = 0;
	private String name;
	
	public AutoMessage(MessageSet msg, List<Condition> conditions) {
		this.conditions = conditions;
		this.set = msg;
	}
	
	/*public String getMessage() {		
		return msg.getMessage();
	}*/
	
	public List<Condition> getConditions() {
		return conditions;
	}
	
	public MessageSet getMessages() {
		return set;
	}
	
	public Condition inChat(String chat) {
		for (Condition i : conditions) {			
			if (i.matches(chat)) {
				return i;
			}
		}
		
		return null;
	}
	
	public String getName() {
		return getMessages().getMessage().getMessage();
	}
	
	private long getTimeSinceLastSend() {
		return System.currentTimeMillis() - time;
	}
	
	private void setTime() {
		time = System.currentTimeMillis();
	}
	
	private Condition cnd;
	public void sendMessage(String line) {
		if ((cnd = inChat(line)) == null) return;
		
		if (!cnd.getSpam().canSpam() && this.getTimeSinceLastSend() < 1000) {
			if (cnd.getSpam().warn()) {
				The5zigAPI.getLogger().warn(
					"Cancelled Auto message, sending too fast!" + 
					set.getMessagesString()
				);
				Debug.overHeadMessage(
					"Sending messages too fast!"
				);
			}
			return;
		}
		setTime();
		
		Message msg = set.getMessage();
		
		
		String message = Replace.replacePresets(
			msg.getMessage(), 
			cnd.getReplace(), 
			line
		);
		
		message = Colour.recolour(message);
		
		StateHandler.sendMessage(
			message, 
			msg.getChatOutput(), 
			true//cnd.getDelayed()
		);
		StateHandler.messageSent();
	}
}
