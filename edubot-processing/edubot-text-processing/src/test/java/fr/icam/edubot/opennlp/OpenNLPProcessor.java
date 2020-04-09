package fr.icam.edubot.opennlp;

import java.util.List;

import io.sloi.opennlp.Label;
import io.sloi.opennlp.Text;
import io.sloi.opennlp.TextAnalyzer;
import io.sloi.opennlp.TextAnalyzer.Level;

public class OpenNLPProcessor {

	public static void main(String[] arguments) throws Exception {
		TextAnalyzer analyzer = new TextAnalyzer(Level.Word);
		analyzer.setUp();
		Text text = new Text("hello John. i'd like to know if i'm busy today? thank you.");
		analyzer.doProcess(text);
		List<Label> sentences = text.getLabels("sentence");
		for (Label sentence : sentences) {
			System.out.println(sentence.getText());
			List<Label> words = text.getLabels("word", sentence.getStart(), sentence.getStop());
			for (Label word : words) {
				String token = word.getText();
				String tag = word.getFeature(String.class, "part-of-speech");
				String lemma = word.getFeature(String.class, "lemma");
				System.out.println(token + (tag == null ? "" : "\t" + tag) + (lemma == null ? "" : "\t" + lemma));
			}
		}
		analyzer.tearDown();
	}
	
}