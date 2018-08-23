package com.candibell.dal;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

import com.candibell.common.EnumValue;
import com.candibell.mqtt.MqttClient;

public class DBUtil {
	
	private static final Logger logger = Logger.getLogger(DBUtil.class);
	
	private static String genEtag() {
		long timestamp = System.currentTimeMillis();
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.putLong(timestamp);
		String raw = Hex.encodeHexString(buffer.array());
		return raw.substring(8); // only interested in the last half
	}
	
	public static void registerDevice2User(M_Device mDevice) throws IOException {
		mDevice.addRegisterDevice();
		if (EnumValue.DeviceType.SENSOR == EnumValue.DeviceType.valueOf(mDevice.getDeviceType())) {
			userSensorUpdate(mDevice);
		}
	}
	
	public static void deregisterDeviceFromUser(M_Device mDevice) throws IOException {
		mDevice.deRegisterDevice();
		if (EnumValue.DeviceType.SENSOR == EnumValue.DeviceType.valueOf(mDevice.getDeviceType())) {
			userSensorUpdate(mDevice);
		}
	}
	
	private static void userSensorUpdate(M_Device mDevice) {
		try {
			M_UserDevicesEtag userDevicesEtag = new M_UserDevicesEtag();
			userDevicesEtag.setUserId(mDevice.getUserId()).setEtag(genEtag());
			userDevicesEtag.update();
			
			List<M_Device> mDevices = new M_Device().getDevicesByUserId(mDevice.getUserId());
			List<String> macs = new ArrayList<>(mDevices.size());
			for (M_Device device : mDevices) {
				if (EnumValue.DeviceType.SENSOR == EnumValue.DeviceType.valueOf(device.getDeviceType())) {
					macs.add(device.getId());
				}
			}
			MqttClient.notifyUserLatestDevices(mDevice.getUserId(), macs, userDevicesEtag.getEtag());
		} catch (Throwable t) {
			logger.warn("Failed to notify User Device List updated", t);
		}
	}
	
	
	public static void main(String[] args) {
		System.out.println(genEtag());
	}
	
}
