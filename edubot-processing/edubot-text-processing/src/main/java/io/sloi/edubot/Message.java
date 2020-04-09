package io.sloi.edubot;

import java.util.Map;

public class Message {
	
	private Intent intent;
	
	private Map<String, String> attributes;

	public Intent getIntent() {
		return intent;
	}

	public void setIntent(Intent intent) {
		this.intent = intent;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
	
}
