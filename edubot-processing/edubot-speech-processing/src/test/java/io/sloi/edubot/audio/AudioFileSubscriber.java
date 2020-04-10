package io.sloi.edubot.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class AudioFileSubscriber implements MqttCallbackExtended {

	private static final String BROKER = "tcp://localhost:1883";
	
	private static final String CLIENT = "9c0326a8-b623-40c3-abbb-1671eedc6599";
	
	private MqttClient client;
	
	private boolean living;
	private boolean recording;
	private int recorded;
	private File outputFile;
	private OutputStream outputStream;
	private AudioFormat audioFormat;
	private int audioLength;
	
	public void setUp() throws Exception {
		living = true;
		recording = false;
		recorded = -1;
		audioLength = -1;
		client = new MqttClient(BROKER, CLIENT, new MemoryPersistence());
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(true);
		client.connect(options);
		client.setCallback(this);
		System.err.println("[sub] connected to " + BROKER + " as " + CLIENT);
		client.subscribe(AudioFilePublisher.META_TOPIC, 1);
		System.err.println("[sub] subscribing to " + AudioFilePublisher.META_TOPIC);
		client.subscribe(AudioFilePublisher.DATA_TOPIC, 0);
		System.err.println("[sub] subscribing to " + AudioFilePublisher.DATA_TOPIC);
	}
	
	public void tearDown() throws Exception {
		if (client.isConnected()) {
			client.unsubscribe(AudioFilePublisher.META_TOPIC);
			client.unsubscribe(AudioFilePublisher.DATA_TOPIC);
			client.disconnect();
			System.err.println("[sub] disconnected");
		}
	}
	
	public boolean isLiving() {
		return living;
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		if (topic.equals(AudioFilePublisher.DATA_TOPIC)) {
			if (recording) {
				byte[] payload = message.getPayload();
				outputStream.write(payload);
				audioLength += payload.length;
				recorded++;
			} else {
				System.err.println("[sub] message skipped");
			}
		} else if (topic.equals(AudioFilePublisher.META_TOPIC)) {
			byte[] payload = message.getPayload();
			String content = new String(payload);
			if (content.equals("hello")) {
				System.err.println("[sub] publisher connected");
			} else if (content.startsWith("start")) {
				audioFormat = this.getAudioFormat(content);
				// System.out.println("[sub] audio format:\t" + audioFormat.toString());
				recording = true;
				recorded = 0;
				audioLength = 0;
				outputFile = File.createTempFile("mqtt-audio-test-", ".tmp");
				outputStream = new FileOutputStream(outputFile);
			} else if (content.equals("stop")) {
				recording = false;
				outputStream.close();
				System.out.println("[sub]\t" + recorded + "\t" + outputFile.getAbsoluteFile());
				recorded = -1;
				run();
			} else if (content.equals("quit")) {
				if (recording) {
					recording = false;
					outputStream.close();
					System.err.println("[sub]\t" + recorded + "\t" + outputFile.getAbsoluteFile());
					recorded = -1;
					run();
				}
				living = false;
				System.err.println("[sub] publisher disconnected");					
			}
		}
	}

	private AudioFormat getAudioFormat(String payload) {
		String[] items = payload.substring(6).split("\\s+");
		Encoding encoding = new Encoding(items[0]);
		float sampleRate = Float.valueOf(items[1]);
		int sampleSizeInBits = Integer.valueOf(items[2]);
		int channels = Integer.valueOf(items[3]);
		int frameSize = Integer.valueOf(items[4]);
		float frameRate = Float.valueOf(items[5]);
		boolean bigEndian = Boolean.valueOf(items[6]);
		AudioFormat format = new AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, frameSize, frameRate, bigEndian);
		return format;
	}
	
	public void run() {
		try {
			InputStream inputStream = new FileInputStream(outputFile);
			AudioInputStream audioStream = new AudioInputStream(inputStream, audioFormat, audioLength);
			String outputPath = outputFile.getAbsolutePath();
			String path = outputPath.substring(0, outputPath.length() - 4);
			File file = new File(path + ".wav");
			AudioSystem.write(audioStream, Type.WAVE, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] arguments) throws Exception {
		AudioFileSubscriber subscriber = new AudioFileSubscriber();
		subscriber.setUp();
		while (subscriber.isLiving()) { Thread.sleep(1000); }
		subscriber.tearDown();
		System.exit(0);
	}

	@Override
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connectComplete(boolean reconnect, String serverURI) {
		// TODO Auto-generated method stub
		
	}
	
}
