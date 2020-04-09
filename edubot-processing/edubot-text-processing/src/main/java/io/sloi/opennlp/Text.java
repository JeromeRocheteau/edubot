package io.sloi.opennlp;

import java.util.LinkedList;
import java.util.List;

public class Text {

	private String text;
	
	private List<Label> labels;
	
	String getText() {
		return text;
	}
	
	public boolean addLabel(String name, int start, int stop) {
		if (0 <= start && start < stop && stop <= text.length()) {
			Label label = new Label(this, name, start, stop);
			labels.add(label);
			return true;
		} else {
			return false;
		}
	}
	
	public List<Label> getLabels(String name) {
		List<Label> labels = new LinkedList<Label>();
		for (Label label : this.labels) {
			if (name.equals(label.getName())) {
				labels.add(label);
			} else {
				continue;
			}
		}
		return labels;
	}

	public List<Label> getLabels(int start, int stop) {
		List<Label> labels = new LinkedList<Label>();
		for (Label label : this.labels) {
			if (label.getStart() < start) {
				continue;
			} else if (stop < label.getStop()) {
				continue;
			} else {
				labels.add(label);
			}
		}
		return labels;
	}
	
	public List<Label> getLabels(String name, int start, int stop) {
		List<Label> labels = new LinkedList<Label>();
		for (Label label : this.labels) {
			if (label.getStart() < start) {
				continue;
			} else if (stop < label.getStop()) {
				continue;
			} else if (name.equals(label.getName())) {
				labels.add(label);
			} else {
				continue;
			}
		}
		return labels;
	}

	public Text(String text) {
		this.text = text;
		this.labels = new LinkedList<Label>();
		this.addLabel("text", 0, text.length());
	}
	
}
