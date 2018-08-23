package com.candibell.mqtt;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.candibell.firmware.api.FirmwareTLV;

public class MqttClient {
	
	private static final String MQTT_ACCESS_KEY_ID = "AKIAIFUSXWZDUCDXBJYQ";
	private static final String MQTT_ACCESS_KEY_SECRET = "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";
	private static final String MQTT_CLIENT_ENDPOINT = "a3c78thhwgxfi7.iot.us-east-1.amazonaws.com";
	
	public static void notifyUserLatestDevices(String userId, List<String> macs, String userDevicesEtag) throws AWSIotException {
		AWSIotMqttClient awsIotClient = 
				new AWSIotMqttClient(MQTT_CLIENT_ENDPOINT, UUID.randomUUID().toString(), MQTT_ACCESS_KEY_ID, MQTT_ACCESS_KEY_SECRET);
		String topic = constructUserTopic(userId);
 		String payload = FirmwareTLV.constructUserDevicesTLV(userDevicesEtag, macs);
		AWSIotMessage message = new AWSIotMessage(topic, AWSIotQos.QOS1, payload);
		awsIotClient.connect();
		awsIotClient.publish(message);
		awsIotClient.onConnectionClosed();
	}
	
	private static String constructUserTopic(String userId) {
		return "user/" + userId;
	}
	

	public static void main(String[] args) {
		try {
			List<String> macs = new ArrayList<>();
			macs.add("DA74AC4193BB");
			macs.add("DA74AC4193C6");
			notifyUserLatestDevices("niusidi@gmail.com", macs, "491f79f8");
		} catch (AWSIotException e) {
			e.printStackTrace();
		}
	}

}
