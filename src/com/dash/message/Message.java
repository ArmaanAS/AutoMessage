package com.dash.message;

import com.dash.utils.Chat;
import com.dash.utils.Colour;

public class Message {
	private String message;
	private Chat output;
	
	public Message(String msg) {
		this.message = msg.substring(msg.indexOf("\"")+1, msg.length()-1);
		this.output = Chat.getChatOutput(msg);
	}
	
	public String getMessage() {
		return message;
	}
	
	public Chat getChatOutput() {
		return this.output;
	}
}
