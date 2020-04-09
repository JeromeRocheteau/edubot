package io.sloi.edubot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class AbstractBot implements Bot {

	private List<Message> messages;
	
	private Map<String, Object> attributes;
	
	protected AbstractBot() {
		messages = new LinkedList<Message>();
		attributes = new HashMap<String, Object>();
	}

	@Override
	public final String onConnect() {
		Message message = this.getConnect();
		return this.getText(message);
	}

	@Override
	public final String onDisconnect() {
		messages.clear();
		Message message = this.getDisconnect();
		return this.getText(message);
	}

	@Override
	public final String onMessage(String text) {
		Message input = this.getMessage(text);
		input.getAttributes().forEach((key, value) -> attributes.put(key, value));
		messages.add(input);
		Message output = this.doProcess(input);
		messages.add(output);
		return this.getText(output);
	}

	protected abstract Message getDefault();
	
	protected abstract Message getConnect();
	
	protected abstract Message getDisconnect();
	
	protected abstract String getText(Message message);

	protected abstract Message getMessage(String text);

	protected abstract Message doProcess(Message message);

}
