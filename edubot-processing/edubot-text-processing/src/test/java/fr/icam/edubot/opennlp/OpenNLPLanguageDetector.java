package fr.icam.edubot.opennlp;

import java.io.File;
import java.io.InputStream;

import opennlp.tools.cmdline.langdetect.LanguageDetectorModelLoader;
import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetector;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.langdetect.LanguageDetectorModel;

public class OpenNLPLanguageDetector {

	private static final String MODEL = "/langdetect-183.bin";
	
	private LanguageDetectorModel model;
	
	private LanguageDetector detector;
	
	public void setUp() throws Exception {
		File file = new File("/home/jerome/Documents/Développement/edubot/src/test/resources/" + MODEL);
		model = new LanguageDetectorModelLoader().load(file);
		detector = new LanguageDetectorME(model);
	}
	
	public void tearDown() throws Exception { }
	
	public void doProcess(String text) throws Exception {
		Language bestLanguage = detector.predictLanguage(text);
		System.out.println("Best language: " + bestLanguage.getLang());
		System.out.println("Best language confidence: " + bestLanguage.getConfidence());
		Language[] languages = detector.predictLanguages(null);
		for (Language language : languages) {
			System.out.println("Language: " + language.getLang());
			System.out.println("Language confidence: " + language.getConfidence());
		}
	}

	public static void main(String[] arguments) throws Exception {
		OpenNLPLanguageDetector detector = new OpenNLPLanguageDetector();
		detector.setUp();
		detector.doProcess("hello John. i'd like to know if i'm busy today? thank you.");
		detector.tearDown();
	}
	
	
}
