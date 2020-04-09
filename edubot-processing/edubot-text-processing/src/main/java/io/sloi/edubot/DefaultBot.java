package io.sloi.edubot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DefaultBot extends AbstractBot {

	private Index index;
	
	protected abstract Index getIndex() throws Exception;

	protected DefaultBot() throws Exception {
		index = this.getIndex();
		index.doResolve();
	}
	
	@Override
	protected Message getDefault() {
		Message message = new Message();
		message.setAttributes(new HashMap<String, String>());
		message.setIntent(index.getDefault());
		return message;
	}

	@Override
	protected Message getConnect() {
		Message message = new Message();
		message.setAttributes(new HashMap<String, String>());
		message.setIntent(index.getConnect());
		return message;
	}

	@Override
	protected Message getDisconnect() {
		Message message = new Message();
		message.setAttributes(new HashMap<String, String>());
		message.setIntent(index.getDisconnect());
		return message;
	}

	@Override
	protected String getText(Message message) {
		Map<String,String> attributes = message.getAttributes();
		Intent intent = message.getIntent();
		List<Text> texts = intent.getTexts();
		Text text = this.doChoose(texts, attributes);
		return this.getText(text, attributes);
	}

	private Text doChoose(List<Text> texts, Map<String, String> attributes) {
		Text text = null;
		for (Text t : texts) {
			text = this.doSelect(text, t, attributes);
		}
		return text;
	}

	private Text doSelect(Text previous, Text current, Map<String, String> attributes) {
		if (previous == null) {
			return current;
		} else {
			return previous;
		}
	}

	private String getText(Text text, Map<String, String> attributes) {
		List<Parameter> parameters = text.getParameters();
		StringBuffer buffer = new StringBuffer(text.getText());
		for (Parameter parameter : parameters) {
			String key = parameter.getKey();
			int start = parameter.getStart();
			int stop = parameter.getStop();
			String value = attributes.get(key);
			if (value == null) {
				continue;
			} else {
				buffer = buffer.replace(start, stop, value);
			}
		}
		return buffer.toString();
	}

	@Override
	protected Message getMessage(String text) {
		Message message = this.getDefault();
		message.getAttributes().put("_text", text);
		message.getAttributes().put("_name", "you");
		return message;
	}

	@Override
	protected Message doProcess(Message message) {
		return message;
	}

}
