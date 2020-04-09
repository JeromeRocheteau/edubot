package io.sloi.edubot;

public class Parameter implements Comparable<Parameter> {
	
	private String key;

	private int start;
	
	private int stop;
	
	private Text text;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getStop() {
		return stop;
	}

	public void setStop(int stop) {
		this.stop = stop;
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	@Override
	public int compareTo(Parameter parameter) {
		int diff = this.getStop() - parameter.getStop();
		return diff == 0 ? 0 : (diff < 0 ? 1 : -1);
	}
	
}
