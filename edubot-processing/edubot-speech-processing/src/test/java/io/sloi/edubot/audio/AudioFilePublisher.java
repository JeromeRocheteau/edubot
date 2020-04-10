package io.sloi.edubot.audio;

import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class AudioFilePublisher {

	private static final String BROKER = "tcp://localhost:1883";
	
	private static final String CLIENT = "0b722acf-0749-4f93-aa77-3489b4dd1bc8";
	
	public static final String META_TOPIC = "edubot/" + CLIENT + "/status";
	
	public static final String DATA_TOPIC = "edubot/" + CLIENT + "/broadcast";
	
	private MqttClient client;
	
	public void setUp() throws Exception {
		client = new MqttClient(BROKER, CLIENT, new MemoryPersistence());
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(true);
		client.connect(options);
		System.err.println("[pub] connected to " + BROKER + " as " + CLIENT);
		client.publish(META_TOPIC, "hello".getBytes(), 1, false);
	}
	
	public void tearDown() throws Exception {
		if (client.isConnected()) {
			client.publish(META_TOPIC, "quit".getBytes(), 1, false);
			client.disconnect();
			System.err.println("[pub] disconnected");
		}
	}
	
	public void doProcess(String sample, int round, int size, InputStream inputStream) throws Exception {
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);
		AudioFormat audioFormat = audioInputStream.getFormat();
		String format = this.getAudioFormat(audioFormat);
		int bytesPerFrame = audioFormat.getFrameSize();
		client.publish(META_TOPIC, ("start " + format).getBytes(), 1, false);
		long started = System.currentTimeMillis();
		int numBytes = size * bytesPerFrame; 
		byte[] audioBytes = new byte[numBytes];
		int numBytesRead = -1;
		int countLoop = 0;
		do {
			numBytesRead =  audioInputStream.read(audioBytes);
			client.publish(DATA_TOPIC, audioBytes, 0, false);
			countLoop++;
		} while (numBytesRead != -1);
		long stopped = System.currentTimeMillis();
		System.out.println("[pub]\t" + sample + "\t" + size + "\t" + round + "\t" + countLoop + "\t" + (stopped - started));
		client.publish(META_TOPIC, "stop".getBytes(), 1, false);
	}

	private String getAudioFormat(AudioFormat format) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(format.getEncoding().toString());
		buffer.append(' ');
		buffer.append(format.getSampleRate());
		buffer.append(' ');
		buffer.append(format.getSampleSizeInBits());
		buffer.append(' ');
		buffer.append(format.getChannels());
		buffer.append(' ');
		buffer.append(format.getFrameSize());
		buffer.append(' ');
		buffer.append(format.getFrameRate());
		buffer.append(' ');
		buffer.append(format.isBigEndian());
		return buffer.toString();
	}

	public static void main(String[] arguments) throws Exception {
		AudioFilePublisher publisher = new AudioFilePublisher();
		publisher.setUp();
		String[] samples = new String[] {"01","02"};
		int[] sizes = new int[] {128, 256, 512, 1024, 2048, 4096};
		for (String sample : samples) {
			for (int size : sizes) {
				for (int i = 0; i < 10; i++) {
					InputStream inputStream = AudioFilePublisher.class.getResourceAsStream("/test-" + sample + ".wav");
					publisher.doProcess(sample, i, size, inputStream);
					inputStream.close();
				}
			}
		}
		publisher.tearDown();
		System.exit(0);
	}
	
}
