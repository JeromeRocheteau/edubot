package io.sloi.edubot;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Text {

	private String text;
	
	private List<Parameter> parameters;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public void doResolve() {
		this.parameters = new LinkedList<Parameter>();
		int state = 0; // 0: looking for '$', 1: looking for '{', 2: looking for '}'
		int last = 0;
		int length = this.text.length();
		for (int i = 0; i < length; i++) {
			char c = this.text.charAt(i);
			if (c == '$') {
				if (state == 0) {
					state = 1;
					last = i;
				} else {
					state = 0;
					last = 0;
					// throw new Exception("unexpected character '$' at " + i);
				}
			} else if (c == '{') {
				if (state == 1) {
					state = 2;
				}
			} else if (c == '}') {
				this.addParameter(last, i);
				state = 0;
				last = 0;
			} else if (state == 1) {
				state = 0;
				last = 0;
				// throw new Exception("unexpected character '" + c + "' at " + i + "\n expecting character '{'");				
			}
		}
		Collections.sort(this.parameters);
	}

	private void addParameter(int start, int stop) {
		if (start + 1 < stop) {
			String key = this.text.substring(start + 2, stop);
			Parameter parameter = new Parameter();
			parameter.setKey(key);
			parameter.setStart(start);
			parameter.setStop(stop + 1);
			this.parameters.add(parameter);
			// System.err.println("'" + this.text + "'" + key + " : " + start + "-" + stop);
		}
	}
	
}
