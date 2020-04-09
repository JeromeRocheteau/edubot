package fr.icam.edubot.opennlp;

import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.util.InputStreamFactory;

public class DefaultInputStreamFactory implements InputStreamFactory {

	private InputStream inputStream;
	
	public DefaultInputStreamFactory(InputStream insputStream) {
		this.inputStream = insputStream;
	}
	
	@Override
	public InputStream createInputStream() throws IOException {
		return inputStream;
	}

}
