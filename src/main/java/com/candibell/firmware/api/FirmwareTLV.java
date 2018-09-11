package com.candibell.firmware.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

import com.candibell.common.Constant;
import com.candibell.rest.IoTDeviceMeasurement;

public class FirmwareTLV {
	
	private static final Logger logger = Logger.getLogger(FirmwareTLV.class);
	
	private static final String COMMAND_UPLOAD_SENSOR_VALUES = "A1";
	private static final String COMMAND_USER_DEVICE_UPDATE = "B3";
//	private static final String COMMAND_SET_SENSOR_STATE = "B4";
//	private static final String COMMAND_SET_HUB_UPLOAD_INTERVAL = "B5";
//	private static final String COMMAND_SET_TAG_TIMER = "B6";
	
	private static final int TLV_CMDTAG_HEXSTR_LEN = 2;
	private static final int NUM_OBJ_HEXSTR_LEN = 4;
	private static final int ETAG_HEXSTR_LEN = 8;
	private static final int DEVICE_MAC_HEXSTR_LEN = 12;
	
	public static String parseHubIdFromMeasurementsTLV(String tlv) {
		if (tlv == null || tlv.length() < DEVICE_MAC_HEXSTR_LEN + TLV_CMDTAG_HEXSTR_LEN) {
			return null;
		} else {
			return tlv.substring(TLV_CMDTAG_HEXSTR_LEN, TLV_CMDTAG_HEXSTR_LEN+DEVICE_MAC_HEXSTR_LEN);
		}
	}
	
	// -------------- Hub Upload Sensor Values ------------------- //
	// Server subscribes to topic: data/{user_email}
	//S.H.3, TLV: A1 + MAC address of HUB (6 bytes) + Length of Object counts (2 bytes) + Object #1, #2...
	// Object is comprised of: Sensor MAC (6 bytes) + category (2 bytes) + Temperature (1 byte, -128~127) + Humidity (1 bytes, 0~100) + Light Level (1 byte, 0~256) + Motion Counter (2 bytes, 0~65535)
	// + timeStamp (4 bytes) + Session Counter (2 bytes)
	// Sample data: A1+EEFFAABBCCDD+0002+AABBCCAABBCC+AABB+AA+BB+CC+AABB+00112233+0010+AABBCCAABBCC+AABB+AA+BB+CC+AABB+00114433+0010
	private static final int A1_OBJ_STR_LEN = 12 + 4 + 2 + 2 + 2 + 4 + 8 + 4;
	private static final int A1_META_STR_LEN = TLV_CMDTAG_HEXSTR_LEN + DEVICE_MAC_HEXSTR_LEN + NUM_OBJ_HEXSTR_LEN;
	public static List<IoTDeviceMeasurement> parseHubUploadMeasurementsTLV(String tlv) {
		if (tlv == null || tlv.length() < A1_META_STR_LEN || !tlv.substring(0,2).equalsIgnoreCase(COMMAND_UPLOAD_SENSOR_VALUES))
			return null;
		String objLenStr = tlv.substring(TLV_CMDTAG_HEXSTR_LEN + DEVICE_MAC_HEXSTR_LEN, A1_META_STR_LEN);
		Integer objLen = getUnsignedIntegerFromLen4HexStr(objLenStr);
		if (objLen == null || A1_META_STR_LEN + objLen*A1_OBJ_STR_LEN != tlv.length()) {
			return null;
		}
		List<IoTDeviceMeasurement> ioTDeviceMeasurements = new ArrayList<>(objLen);
		for (int i = 0; i < objLen; i++) {
			int startInd = A1_META_STR_LEN + i * A1_OBJ_STR_LEN;
			String measurementInHex = tlv.substring(startInd, startInd + A1_OBJ_STR_LEN);
			IoTDeviceMeasurement ioTDeviceMeasurement = parseA1Obj2IoTMeasurement(measurementInHex);
			if (ioTDeviceMeasurement != null)
				ioTDeviceMeasurements.add(ioTDeviceMeasurement);
		}
		return ioTDeviceMeasurements;
	}
	private static IoTDeviceMeasurement parseA1Obj2IoTMeasurement(String objInHex) {
		if (objInHex == null || objInHex.length() != A1_OBJ_STR_LEN)
			return null;
		IoTDeviceMeasurement ioTDeviceMeasurement = new IoTDeviceMeasurement();
		ioTDeviceMeasurement.setDeviceId(objInHex.substring(0, 12));
		ioTDeviceMeasurement.setCategory("0" + objInHex.substring(13, 16)); // Mask Version Char which is between tag and hub only
		ioTDeviceMeasurement.setTemperature(getSignedIntegerFromLen2HexStr(objInHex.substring(16, 18)));
		ioTDeviceMeasurement.setHumidity(getUnsignedIntegerFromLen2HexStr(objInHex.substring(18, 20)));
		ioTDeviceMeasurement.setLightLevel(getUnsignedIntegerFromLen2HexStr(objInHex.substring(20, 22)));
		ioTDeviceMeasurement.setMotionCounts(getUnsignedIntegerFromLen4HexStr(objInHex.substring(22, 26)));
		ioTDeviceMeasurement.setRawData(objInHex);
		ioTDeviceMeasurement.setInSessionTime(objInHex.substring(26, 34));
		ioTDeviceMeasurement.setSessionId(objInHex.substring(34, 38));
		
		return ioTDeviceMeasurement;
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
		sb.append(get2BytesLenIntStr(macs.size()));
		for (String mac : macs) {
			if (mac.length() != DEVICE_MAC_HEXSTR_LEN) {
				throw new IllegalArgumentException("Bad device mac:" + mac);
			}
			sb.append(mac);
		}
		
		return sb.toString().toUpperCase(Constant.DEFAULT_LOCATE);
	}
	
	
	private static String get2BytesLenIntStr(int length) {
		byte[] lower2Bytes = new byte[]{(byte) ((length >> 8) & 0xFF), (byte) (length & 0xFF)};
		return Hex.encodeHexString(lower2Bytes);
	}
	
	private static Integer getUnsignedIntegerFromLen4HexStr(String hexStr) {
		byte[] numBytes = null;
		try {
			numBytes = Hex.decodeHex(hexStr.toCharArray());
		} catch (DecoderException e) {
			logger.info("Failed to decode Hex String to an Integer:" + hexStr);
			return null;
		}
		return getUnsignedIntFrom2Bytes(numBytes);
	}
	
	private static Integer getUnsignedIntegerFromLen2HexStr(String hexStr) {
		byte[] numBytes = null;
		try {
			numBytes = Hex.decodeHex(hexStr.toCharArray());
		} catch (DecoderException e) {
			logger.info("Failed to decode Hex String to an Integer:" + hexStr);
			return null;
		}
		return numBytes[0] & 0xff;
	}
	
	private static int getUnsignedIntFrom2Bytes(byte[] bytes) {
		int val = ((bytes[0] & 0xff) << 8) | (bytes[1] & 0xff);
		return val;
	}
	
	private static Integer getSignedIntegerFromLen2HexStr(String hexStr) {
		byte[] numBytes = null;
		try {
			numBytes = Hex.decodeHex(hexStr.toCharArray());
		} catch (DecoderException e) {
			logger.info("Failed to decode Hex String to an Integer:" + hexStr);
			return null;
		}
		return (int) numBytes[0];
	}
	
	public static void main(String[] args) {
//		System.out.println(get2BytesLength(10010));
		
//		List<String> macs = new ArrayList<String>();
//		macs.add("DA74AC4193BB");
//		macs.add("DA74AC4193C6");
//		System.out.println(constructUserDevicesTLV("491f79f8", macs));
		
//		System.out.println(parseHubIdFromMeasurementsTLV(""));
//		System.out.println(parseHubIdFromMeasurementsTLV("A1"));
//		System.out.println(parseHubIdFromMeasurementsTLV("A1EEFFAABBCCDD0002"));
		
//		System.out.println(getIntegerFromLen4HexStr("0000"));
//		System.out.println(getIntegerFromLen4HexStr("000z"));
//		System.out.println(getUnsignedIntegerFromLen4HexStr("0008"));
//		System.out.println(getUnsignedIntegerFromLen4HexStr("ffff"));
		
//		System.out.println(getSignedIntegerFromLen2HexStr("20"));
//		System.out.println(getSignedIntegerFromLen2HexStr("80"));
		
//		System.out.println(getUnsignedIntegerFromLen2HexStr("00"));
//		System.out.println(getUnsignedIntegerFromLen2HexStr("ff"));
//		System.out.println(getIntegerFromLen2HexStr("1A"));
//		System.out.println(getIntegerFromLen2HexStr("tA"));
		
//		System.out.println(parstA1Obj2IoTMeasurement("AABBCCAABBCCAABBAABBCCAABB001122330010"));//AABBCCAABBCC+AABB+AA+BB+CC+AABB+00112233+0010
//		System.out.println(parseA1Obj2IoTMeasurement("AABBCCAABBCC00120020080030001144330010"));
		
//		System.out.println(parseHubIdFromMeasurementsTLV("A1EEFFAABBCCDD0002AABBCCAABBCCAABBAABBCCAABB001122330010AABBCCAABBCCAABBAABBCCAABB001144330010"));
//		List<IoTDeviceMeasurement> measurements = parseHubUploadMeasurementsTLV("A1EEFFAABBCCDD0002AABBCCAABBCCAABBAABBCCAABB001122330010AABBCCAABBCCAABBAABBCCAABB001144330010");
//		for (IoTDeviceMeasurement measurement : measurements) {
//			System.out.println(measurement);
//		}
	}
	

}
