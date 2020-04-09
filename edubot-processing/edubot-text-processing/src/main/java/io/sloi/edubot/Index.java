package io.sloi.edubot;

import java.util.List;

public class Index {

	private List<Intent> intents;
	
	public List<Intent> getIntents() {
		return intents;
	}

	public void setIntents(List<Intent> intents) {
		this.intents = intents;
	}

	public Intent getDefault() {
		return this.intents.get(0);
	}
	
	public Intent getConnect() {
		return this.intents.get(1);
	}
	
	public Intent getDisconnect() {
		return this.intents.get(2);
	}
	
	public void doResolve() {
		this.intents.forEach(intent -> intent.doResolve());
	}
	
}
