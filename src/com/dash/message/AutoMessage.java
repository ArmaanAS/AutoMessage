/**
 * 
 */
package com.dash.message;

import java.util.List;

import com.dash.core.StateHandler;
import com.dash.message.condition.Condition;
import com.dash.message.condition.Condition.ReplaceIncompatibleException;
import com.dash.message.condition.Condition.SyntaxException;
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
//	private List<Condition> conditions;
//	private ConditionSet conditions;
	private Condition conditions;
	private long time = 0;
	private String name;
	
//	public AutoMessage(MessageSet msg, List<Condition> conditions) {
//		this.set = msg;
//		this.conditions = new ConditionSet(conditions);
//		if (!this.conditions.isValid()) {
//			Debug.chatWarn("Errors in Message(s): " + msg.getMessagesString());
//		}
//	}
	
	public AutoMessage(MessageSet msg, Condition conditions) throws SyntaxException, ReplaceIncompatibleException {
		this.set = msg;
		this.conditions = conditions;
	}
	
	
	/*public String getMessage() {		
		return msg.getMessage();
	}*/
	
	public Condition getConditions() {
		return conditions;
	}
	
	public MessageSet getMessages() {
		return set;
	}
	
//	public Condition inChat(String chat) {
//		for (Condition i : conditions) {			
//			if (i.matches(chat)) {
//				return i;
//			}
//		}
//		
//		return null;
//	}
	
	public String getName() {
		return getMessages().getMessage().getMessage();
	}
	
	private long getTimeSinceLastSend() {
		return System.currentTimeMillis() - time;
	}
	
	private void setTime() {
		time = System.currentTimeMillis();
	}
	
//	private Condition cnd;
	public void sendMessage(String line, String rawLine) {
//		if ((cnd = inChat(line)) == null) return;
		if (!conditions.matches(line)) return;
		
//		if (!cnd.getSpam().canSpam() && this.getTimeSinceLastSend() < 1000) {
		if (!conditions.canSpam() && this.getTimeSinceLastSend() < 1000) {
//			if (cnd.getSpam().warn()) {
			if (conditions.warn()) {
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
		
//		if (!cnd.isParallel()) {
		if (!conditions.isParallel()) {
			Message msg = set.getMessage();
			
			
//			String message = Replace.replacePresets(
//				msg.getMessage(), 
//				cnd.getReplace(), 
//				line
//			);
			String message = conditions.replace(
				msg.getMessage(), 
				line
			);
			
			message = Colour.recolour(message);
			
			StateHandler.sendMessage(
				message, 
				msg.getChatOutput(), 
				true  //conditions.isDelayed()
			);
			StateHandler.messageSent();
		} else {
			Thread t = new Thread(new Runnable() {
				public void run() {
					Message msg = set.getMessage();
					
					
//					String message = Replace.replacePresets(
//						msg.getMessage(), 
//						cnd.getReplace(), 
//						line
//					);
					String message = conditions.replace(
						msg.getMessage(), 
						line
					);
					
//					message = Colour.recolour(message);
					
					StateHandler.sendMessage(
						message, 
						msg.getChatOutput(), 
						true  //conditions.isDelayed()
					);
					StateHandler.messageSent();
				}
			});
			t.start();
		}
	}
	
}
