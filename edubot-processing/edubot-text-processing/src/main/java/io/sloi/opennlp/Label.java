package io.sloi.opennlp;

import java.util.HashMap;
import java.util.Map;

public class Label implements Comparable<Label> {

	private Text text;
	
	private int start;
	
	private int stop;
	
	private String name;
	
	private Map<String, Object> features;

	public int getStart() {
		return start;
	}

	public int getStop() {
		return stop;
	}

	public String getName() {
		return name;
	}
	
	public String getText() {
		return text.getText().substring(start, stop);
	}

	public <T> void addFeature(Class<T> type, String key, Object value) {
		features.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T getFeature(Class<T> type, String key) {
		try {
			return (T) features.get(key);
		} catch (Exception e) {
			return null;
		}
	}
	
	Label(Text text, String name, int start, int stop) {
		this.text = text;
		this.name = name;
		this.start = start;
		this.stop = stop;
		this.features = new HashMap<String, Object>();
	}

	@Override
	public int compareTo(Label label) {
		int diff = Integer.compare(start, label.start);
		if (diff == 0) {
			diff = Integer.compare(label.stop, stop);
			if (diff == 0) {
				return name.compareTo(label.name);
			} else {
				return diff;
			}
		} else {
			return diff;
		}
	}

	
	
}
