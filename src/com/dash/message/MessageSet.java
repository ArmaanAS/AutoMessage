package com.dash.message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.dash.utils.Chat;

public class MessageSet {
	private List<Message> messages = 
			new ArrayList<Message>();
	private String msgList = "";
	
	public MessageSet(String[] messages) {
		for (String i : messages) {
			this.messages.add(new Message(i));
			msgList += ", " + i;
		}
		msgList = msgList.substring(2);
	}
	
	public Message getMessage() {
		return this.messages.get(
		    ThreadLocalRandom.current()
		    .nextInt(this.messages.size())
		);
	}
	
	public String getMessagesString() {
		return msgList;
	}
	
	public List<Message> getMessageList() {
		return messages;
	}
	
	public MessageSet print() {
		System.out.println(getMessagesString());
		return this;
	}
}
