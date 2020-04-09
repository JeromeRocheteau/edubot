package io.sloi.edubot;

import java.util.List;

public class Intent {

	private Integer code;
	
	private String label;
	
	private List<Text> texts;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<Text> getTexts() {
		return texts;
	}

	public void setTexts(List<Text> texts) {
		this.texts = texts;
	}
	
	public void doResolve() {
		this.texts.forEach(text -> text.doResolve());
	}
	
}
