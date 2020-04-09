package fr.icam.edubot.opennlp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import io.sloi.opennlp.Label;
import io.sloi.opennlp.Text;
import io.sloi.opennlp.TextAnalyzer;
import io.sloi.opennlp.TextAnalyzer.Level;
import opennlp.tools.cmdline.doccat.DoccatModelLoader;
import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

public class OpenNLPCategorizer {

	private DoccatModel categorizerModel;
	
	private DocumentCategorizerME categorizer;
	
	public void doTrain() throws Exception {
		File file = new File("/home/jerome/Documents/Développement/edubot/src/test/resources/org/opennlp/en/en-chat.train");
		InputStreamFactory isFactory = new MarkableFileInputStreamFactory(file);
		ObjectStream<String> lineStream = new PlainTextByLineStream(isFactory, StandardCharsets.UTF_8);
		ObjectStream<DocumentSample> samples = new DocumentSampleStream(lineStream);
		TrainingParameters parameters = new TrainingParameters();
        parameters.put(TrainingParameters.ITERATIONS_PARAM, "10");
        parameters.put(TrainingParameters.CUTOFF_PARAM, "0");
        // parameters.put(AbstractTrainer.ALGORITHM_PARAM, NaiveBayesTrainer.NAIVE_BAYES_VALUE);
		categorizerModel = DocumentCategorizerME.train("en", samples, parameters, new DoccatFactory());		
		OutputStream modelOut = new BufferedOutputStream(new FileOutputStream("/home/jerome/Documents/Développement/edubot/src/test/resources/org/opennlp/en/en-chat.bin"));
		categorizerModel.serialize(modelOut);
	}
	
	public void setUp() throws Exception {
		File file = new File("/home/jerome/Documents/Développement/edubot/src/test/resources/org/opennlp/en/en-chat.bin");
		categorizerModel = new DoccatModelLoader().load(file);
		categorizer = new DocumentCategorizerME(categorizerModel);
	}
	
	public void doProcess(String t) throws Exception {
		TextAnalyzer analyzer = new TextAnalyzer(Level.Word);
		analyzer.setUp();
		Text text = new Text(t);
		analyzer.doProcess(text);
		List<Label> sentences = text.getLabels("sentence");
		for (Label sentence : sentences) {
			List<Label> words = text.getLabels("word", sentence.getStart(), sentence.getStop());
			String[] tokens = new String[words.size()];
			for (int i = 0; i < words.size(); i++) {
				Label word = words.get(i);
				String token = word.getText();
				tokens[i] = token;
			}
			double[] outcomes = categorizer.categorize(tokens);
			String category = categorizer.getBestCategory(outcomes);
			System.out.println(category + "\t" + sentence.getText());
		}
		analyzer.tearDown();
	}
	
	public static void main(String[] arguments) throws Exception {
		OpenNLPCategorizer categorizer = new OpenNLPCategorizer();
		categorizer.setUp();
		categorizer.doProcess("hello John. i'd like to know if i'm busy today? thank you.");
		categorizer.doProcess("hi! is my database schema correct? bye bye");
		categorizer.doProcess("good morning. i wonder if my schema is good. however, i can't fix it. see you soon.");
	}
	
}
