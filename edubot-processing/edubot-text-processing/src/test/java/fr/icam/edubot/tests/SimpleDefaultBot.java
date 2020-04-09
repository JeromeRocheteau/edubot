package fr.icam.edubot.tests;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Scanner;

import com.google.gson.Gson;

import io.sloi.edubot.Bot;
import io.sloi.edubot.DefaultBot;
import io.sloi.edubot.Index;

public class SimpleDefaultBot extends DefaultBot implements Bot {

	public static void main(String[] arguments) throws Exception {
		SimpleDefaultBot bot = new SimpleDefaultBot();
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
	
	private SimpleDefaultBot() throws Exception {
		this.getIndex();
	}

	@Override
	protected Index getIndex() throws Exception {
		Gson parser = new Gson();
		InputStream input = this.getClass().getResourceAsStream("/simple-bot.json");
		Reader reader = new InputStreamReader(input);
		Index index = parser.fromJson(reader, Index.class);
		reader.close();
		input.close();
		return index;

	}
	
}