package com.candibell.firmware.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Hex;

import com.candibell.common.Constant;
import com.candibell.rest.IoTDeviceMeasurement;

public class FirmwareTLV {
	
	
	private static final String COMMAND_UPLOAD_SENSOR_VALUES = "A1";
	private static final String COMMAND_USER_DEVICE_UPDATE = "B3";
	private static final String COMMAND_SET_SENSOR_STATE = "B4";
	private static final String COMMAND_SET_HUB_UPLOAD_INTERVAL = "B5";
	private static final String COMMAND_SET_TAG_TIMER = "B6";
	
	private static final int TLV_CMDTAG_HEXSTR_LEN = 2;
	private static final int NUM_OBJ_HEXSTR_LEN = 4;
	private static final int ETAG_HEXSTR_LEN = 8;
	private static final int DEVICE_MAC_HEXSTR_LEN = 12;
	
	// -------------- Hub Upload Sensor Values ------------------- //
	// Server subscribes to topic: data/{user_email}
	//S.H.3, TLV: A1 + MAC address of HUB (6 bytes) + Length of Object counts (2 bytes) + Object #1, #2...
	// Object is comprised of: Sensor MAC (6 bytes) + Temperature (2 bytes) + Humidity (2 bytes) + Light Level (1 byte) + Motion Counter (2 bytes)
	// + timeStamp (4 bytes) + Session Counter (2 bytes)
	// Sample data: A1+EEFFAABBCC+0002+AABBCCAABBCC+AABB+AABB+AA+AABB+00112233+0010+AABBCCAABBCC+AABB+AABB+AA+AABB+00114433+0010
	public static List<IoTDeviceMeasurement> parseHubUploadMeasurementsTLV(String tlv) {
		return null;
	}
	
	public static String parseHubIdFromMeasurementsTLV(String tlv) {
		return null;
	}
	
	
	// -------------- Server Push ------------------- //
	// Server publishes to topic: user/{user_email}
	// S.H.4, TLV: B3 + Etag (4 bytes) + Length of number of Objects (2 bytes) + Object #1, #2...
	// Object is mac address of tag (6 bytes)
	public static String constructUserDevicesTLV(String etag, List<String> macs) {
		if (etag.length() != ETAG_HEXSTR_LEN) {
			throw new IllegalArgumentException("Bad etag value:" + etag);
		}
		if (macs == null)
			macs = new ArrayList<String>(0);
		
		StringBuilder sb = new StringBuilder(
				TLV_CMDTAG_HEXSTR_LEN + ETAG_HEXSTR_LEN + NUM_OBJ_HEXSTR_LEN + DEVICE_MAC_HEXSTR_LEN*macs.size());
		sb.append(COMMAND_USER_DEVICE_UPDATE);
		sb.append(etag);
		sb.append(get2BytesLength(macs.size()));
		for (String mac : macs) {
			if (mac.length() != DEVICE_MAC_HEXSTR_LEN) {
				throw new IllegalArgumentException("Bad device mac:" + mac);
			}
			sb.append(mac);
		}
		
		return sb.toString().toUpperCase(Constant.DEFAULT_LOCATE);
	}
	
	
	public static void main(String[] args) {
//		System.out.println(get2BytesLength(10010));
		
		List<String> macs = new ArrayList<String>();
		macs.add("DA74AC4193BB");
		macs.add("DA74AC4193C6");
		System.out.println(constructUserDevicesTLV("491f79f8", macs));
	}
	
	private static String get2BytesLength(int length) {
		byte[] lower2Bytes = new byte[]{(byte) ((length >> 8) & 0xFF), (byte) (length & 0xFF)};
		return Hex.encodeHexString(lower2Bytes);
	}

}
