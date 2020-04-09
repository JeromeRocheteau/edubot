package io.sloi.edubot.audio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

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
	
	public void setUp() throws Exception {
		living = true;
		recording = false;
		recorded = -1;
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
				recorded++;
			} else {
				System.err.println("[sub] message skipped");
			}
		} else if (topic.equals(AudioFilePublisher.META_TOPIC)) {
			byte[] payload = message.getPayload();
			String command = new String(payload);
			if (command.equals("hello")) {
				System.err.println("[sub] publisher connected");
			} else if (command.equals("start")) {
				recording = true;
				recorded = 0;
				outputFile = File.createTempFile("mqtt-audio-test-", ".wav");
				outputStream = new FileOutputStream(outputFile);
			} else if (command.equals("stop")) {
				recording = false;
				outputStream.close();
				System.out.println("[sub]\t" + recorded + "\t" + outputFile.getAbsoluteFile());
				recorded = -1;
			} else if (command.equals("quit")) {
				if (recording) {
					recording = false;
					outputStream.close();
					System.err.println("[sub]\t" + recorded + "\t" + outputFile.getAbsoluteFile());
					recorded = -1;
				}
				living = false;
				System.err.println("[sub] publisher disconnected");					
			}
		}
	}

	public static void main(String[] arguments) throws Exception {
		AudioFileSubscriber subscriber = new AudioFileSubscriber();
		subscriber.setUp();
		while (subscriber.isLiving()) {/* nothing to do but to listen to messages */}
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
