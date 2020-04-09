package fr.icam.edubot.tests;

import java.util.Scanner;

import io.sloi.edubot.Bot;

public class SimpleBot implements Bot {

	public String onConnect() {
		return "hello! how can I help you?";
	}

	public String onDisconnect() {
		return "bye bye";
	}

	public String onMessage(String text) {
		return "you said: « " + text + " »";
	}

	public static void main(String[] arguments) throws Exception {
		SimpleBot bot = new SimpleBot();
		Scanner scanner = new Scanner(System.in);
		try {
			System.out.println(bot.onConnect());
			while (true) {
				String text = scanner.nextLine();
				System.out.println(bot.onMessage(text));
			}
		} catch (Throwable t) {
			System.out.println(bot.onDisconnect());
			scanner.close();
		}
	}
	
}