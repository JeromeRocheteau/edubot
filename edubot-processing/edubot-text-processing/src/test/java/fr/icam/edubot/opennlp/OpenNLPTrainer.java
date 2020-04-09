package fr.icam.edubot.opennlp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import opennlp.tools.sentdetect.SentenceDetectorFactory;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.sentdetect.SentenceSample;
import opennlp.tools.sentdetect.SentenceSampleStream;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

public class OpenNLPTrainer {
	
	private static final String TXT = "/org/opennlp/fr/fr-wikinews-2013-01-10.txt";
	
	private static final String TOK = "/home/jerome/Documents/Développement/edubot/src/test/resources/org/opennlp/fr/fr-wikinews-2013-01-10.tok";
	
	private static final String POS = "/home/jerome/Documents/Développement/edubot/src/test/resources/org/opennlp/fr/fr-wikinews-2013-01-10.pos";

	public void doTrain() throws Exception {
		//File file = new File(TXT);
		InputStream is = this.getClass().getResourceAsStream(TXT);
		InputStreamFactory isFactory = new DefaultInputStreamFactory(is);
		ObjectStream<String> lineStream =  new PlainTextByLineStream(isFactory, StandardCharsets.UTF_8);
		ObjectStream<SentenceSample> sampleStream = new SentenceSampleStream(lineStream);
		SentenceDetectorFactory factory = new SentenceDetectorFactory("fr", true, null, new char[]{});
		TrainingParameters parameters = new TrainingParameters();
		SentenceModel model = SentenceDetectorME.train("fr", sampleStream, factory, parameters);
		OutputStream modelOut = new BufferedOutputStream(new FileOutputStream(TXT + ".bin"));
		model.serialize(modelOut);
	}

	public static void main(String[] arguments) throws Exception {
		OpenNLPTrainer trainer = new OpenNLPTrainer();
		trainer.doTrain();
	}
	
}
