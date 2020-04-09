package io.sloi.opennlp;

import java.io.IOException;
import java.util.List;

/*
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.chunker.Chunker;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
*/
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.lemmatizer.Lemmatizer;
/*
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
*/
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
/*
import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;
*/
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

public class TextAnalyzer {

	public enum Level { Sentence, Word, PartOfSpeech, Lemma };
	
	private static final String SPLITTER_MODEL = "/org/opennlp/en/en-sent.bin";
	
	private static final String TOKENIZER_MODEL = "/org/opennlp/en/en-token.bin";
	
	private static final String TAGGER_MODEL = "/org/opennlp/en/en-pos-maxent.bin";
	
	private static final String LEMMATIZER_MODEL = "/org/opennlp/en/en-lemmatizer.dict";
	
	// private static final String CHUNKER_MODEL = "/org/opennlp/en/en-chunker.bin";
	
	/*
	private static final String[] FINDER_MODELS = 
		{
				"/org/opennlp/en/en-ner-person.bin",
				"/org/opennlp/en/en-ner-date.bin",
				"/org/opennlp/en/en-ner-location.bin",
				"/org/opennlp/en/en-ner-money.bin",
				"/org/opennlp/en/en-ner-organization.bin",
				"/org/opennlp/en/en-ner-percentage.bin",
				"/org/opennlp/en/en-ner-time.bin"
		};
	*/
	
	private Level level;
	
	public TextAnalyzer() {
		this.level = Level.Word;
	}
	
	public TextAnalyzer(Level level) {
		this.level = level;
	}
	
	private SentenceModel splitterModel;
	
	private TokenizerModel tokenizerModel;
	
	private POSModel taggerModel;
	
	// private ChunkerModel chunkerModel;
	
	private SentenceDetector splitter;
	
	private Tokenizer tokenizer;
	
	private POSTagger tagger;
	
	private Lemmatizer lemmatizer;
	
	// private Stemmer stemmer;
	
	// private Chunker chunker;
	
	// private List<NameFinderME> finders;

	private void setSplitter() throws IOException {
		splitterModel = new SentenceModel(this.getClass().getResourceAsStream(SPLITTER_MODEL));
		splitter = new SentenceDetectorME(splitterModel);
	}

	private void setTokenizer() throws IOException {
		tokenizerModel = new TokenizerModel(this.getClass().getResourceAsStream(TOKENIZER_MODEL));
		tokenizer = new TokenizerME(tokenizerModel);
	}

	private void setTagger() throws IOException {
		taggerModel = new POSModel(this.getClass().getResourceAsStream(TAGGER_MODEL));
		tagger = new POSTaggerME(taggerModel);
	}

	private void setLemmatizer() throws IOException {
		lemmatizer = new DictionaryLemmatizer(this.getClass().getResourceAsStream(LEMMATIZER_MODEL));
	}

	/*
	private void setStemmer() {
		stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
	}
	*/

	/*
	private void setChunker() {
		// chunkerModel = new ChunkerModel(this.getClass().getResourceAsStream(CHUNKER_MODEL));
		// chunker = new ChunkerME(chunkerModel);
	}
	*/

	/*
	private void setFinders() throws IOException {
		finders = new ArrayList<NameFinderME>(FINDER_MODELS.length);
		for (String FINDER_MODEL : FINDER_MODELS) {
			TokenNameFinderModel model = new TokenNameFinderModel(this.getClass().getResourceAsStream(FINDER_MODEL));
			NameFinderME finder = new NameFinderME(model);
			finders.add(finder);
		}
	}
	*/
	
	public void setUp() throws Exception {
		int level = this.level.ordinal();
		if (0 <= level) this.setSplitter();
		if (1 <= level) this.setTokenizer();
		if (2 <= level) this.setTagger();
		if (3 <= level) this.setLemmatizer();
	}
	
	public void tearDown() throws Exception { }
	
	public void doProcess(Text text) throws Exception {
		int level = this.level.ordinal();
		if (0 <= level) this.doSplitter(text);
		if (1 <= level) this.doTokenizer(text);
		if (2 <= level) this.doTagger(text);
		if (3 <= level) this.doLemmatizer(text);
		/*
		String textString = text.getText();
		Span[] sentences = splitter.sentPosDetect(textString);
		for (Span sentence : sentences) {
			String sentenceString = sentence.getCoveredText(textString).toString();
			System.out.println(sentence.getStart() + "\t" + sentence.getEnd() + "\t" + sentenceString);
			
			Span[] tokens = tokenizer.tokenizePos(sentenceString);
			String[] tokenStrings = new String[tokens.length];
			for (int i = 0; i < tokens.length; i++) {
				Span token = tokens[i];
				tokenStrings[i] = token.getCoveredText(sentenceString).toString();
				String tokenString = tokenStrings[i];
				System.out.println(token.getStart() + "\t" + token.getEnd() + "\t" + tokenString);
			}
			/*
			String[] tags = tagger.tag(tokenStrings);
			String[] lemmas = lemmatizer.lemmatize(tokenStrings, tags);
			for (int i = 0; i < tokens.length; i++) {
				String token = tokenStrings[i];
				String tag = tags[i];
				String lemma = lemmas[i];
				// String stem = stemmer.stem(token).toString();
				System.out.println(token + "\t" + tag + "\t" + lemma);
			}
			*/
			/*
			String chunks[] = chunker.chunk(tokens, tags);
			for (String chunk : chunks) {
				System.out.println(chunk);
			}
			for (NameFinderME finder : finders) {
				Span[] spans = finder.find(tokens);
				for (Span span : spans) {
					StringBuffer buffer = new StringBuffer();
					int start = span.getStart();
					int end = span.getEnd();
					for (int i = start; i < end; i++) {
						buffer.append(tokens[i]);
					}
					System.out.println(buffer.toString() + "\t" + span.getType());
				}
				finder.clearAdaptiveData();
			}
		}
	*/
	}

	private void doSplitter(Text text) {
		List<Label> texts = text.getLabels("text");
		for (Label t : texts) {
			String textString = t.getText();
			Span[] sentences = splitter.sentPosDetect(textString);
			int offset = t.getStart();
			for (Span sentence : sentences) {
				text.addLabel("sentence", offset + sentence.getStart(), offset + sentence.getEnd());
			}
		}
	}

	private void doTokenizer(Text text) {
		List<Label> sentences = text.getLabels("sentence");
		for (Label sentence : sentences) {
			String sentenceString = sentence.getText();
			Span[] tokens = tokenizer.tokenizePos(sentenceString);
			int offset = sentence.getStart();
			for (Span token : tokens) {
				text.addLabel("word", offset + token.getStart(), offset + token.getEnd());
			}
		}
	}

	private void doTagger(Text text) {
		List<Label> sentences = text.getLabels("sentence");
		for (Label sentence : sentences) {
			List<Label> words = text.getLabels("word", sentence.getStart(), sentence.getStop());
			String[] tokens = new String[words.size()];
			for (int i = 0; i < words.size(); i++) {
				Label word = words.get(i);
				tokens[i] = word.getText();
			}
			String[] tags = tagger.tag(tokens);
			for (int i = 0; i < words.size(); i++) {
				Label word = words.get(i);
				String tag = tags[i];
				word.addFeature(String.class, "part-of-speech", tag);
			}
		}
	}

	private void doLemmatizer(Text text) {
		List<Label> sentences = text.getLabels("sentence");
		for (Label sentence : sentences) {
			List<Label> words = text.getLabels("word", sentence.getStart(), sentence.getStop());
			String[] tokens = new String[words.size()];
			String[] tags = new String[words.size()];
			for (int i = 0; i < words.size(); i++) {
				Label word = words.get(i);
				tokens[i] = word.getText();
				tags[i] = word.getFeature(String.class, "part-of-speech");
			}
			String[] lemmas = lemmatizer.lemmatize(tokens, tags);
			for (int i = 0; i < words.size(); i++) {
				Label word = words.get(i);
				String lemma = lemmas[i];
				word.addFeature(String.class, "lemma", lemma);
			}	
		}
	}
	
}
