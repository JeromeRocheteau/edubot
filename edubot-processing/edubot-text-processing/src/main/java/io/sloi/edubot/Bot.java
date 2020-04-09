package io.sloi.edubot;

public interface Bot {

	public String onConnect();
	
	public String onDisconnect();
	
	public String onMessage(String text);
	
}