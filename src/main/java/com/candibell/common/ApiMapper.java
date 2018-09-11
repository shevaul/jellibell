package com.candibell.common;

import java.io.IOException;

import com.candibell.dal.M_Device;
import com.candibell.dal.M_Measurement;
import com.candibell.dal.M_UserCustomProfile;
import com.candibell.product.profile.TimeOnlyModel;
import com.candibell.product.profile.TrackingProfile;
import com.candibell.product.profile.UserAlert;
import com.candibell.rest.IoTDevice;
import com.candibell.rest.IoTDeviceMeasurement;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class ApiMapper {
	
	public static M_Device IoTDevice2M_Device(IoTDevice ioTDevice) {
		if (ioTDevice == null) 
			return null;
		
		M_Device mDevice = new M_Device();
		if (ioTDevice.getDeviceId() != null && !ioTDevice.getDeviceId().isEmpty())
			mDevice.setId(ioTDevice.getDeviceId());
		if (ioTDevice.getUserId() != null && !ioTDevice.getUserId().isEmpty())
			mDevice.setUserId(ioTDevice.getUserId());
		if (ioTDevice.getDeviceType() != null && !ioTDevice.getDeviceType().isEmpty())
			mDevice.setDeviceType(ioTDevice.getDeviceType());
		if (ioTDevice.getDeviceName() != null && !ioTDevice.getDeviceName().isEmpty())
			mDevice.setDeviceName(ioTDevice.getDeviceName());
		if (ioTDevice.getCreatedTime() != null && !ioTDevice.getCreatedTime().isEmpty())
			mDevice.setCreatedTime(ioTDevice.getCreatedTime());
		if (ioTDevice.getLastUpdatedTime() != null && !ioTDevice.getLastUpdatedTime().isEmpty())
			mDevice.setLastUpdatedTime(ioTDevice.getLastUpdatedTime());
		
		return mDevice;
	}
	
	public static IoTDevice M_Device2IoTDevice(M_Device mDevice) {
		if (mDevice == null) 
			return null;
		
		IoTDevice ioTDevice = new IoTDevice();
		if (mDevice.getId() != null && !mDevice.getId().isEmpty())
			ioTDevice.setDeviceId(mDevice.getId());
		if (mDevice.getUserId() != null && !mDevice.getUserId().isEmpty())
			ioTDevice.setUserId(mDevice.getUserId());
		if (mDevice.getDeviceType() != null && !mDevice.getDeviceType().isEmpty())
			ioTDevice.setDeviceType(mDevice.getDeviceType());
		if (mDevice.getDeviceName() != null && !mDevice.getDeviceName().isEmpty())
			ioTDevice.setDeviceName(mDevice.getDeviceName());
		if (mDevice.getCreatedTime() != null && !mDevice.getCreatedTime().isEmpty())
			ioTDevice.setCreatedTime(mDevice.getCreatedTime());
		if (mDevice.getLastUpdatedTime() != null && !mDevice.getLastUpdatedTime().isEmpty())
			ioTDevice.setLastUpdatedTime(mDevice.getLastUpdatedTime());
		
		return ioTDevice;
	}

	public static M_Measurement IoTDeviceMeasurement2M_Measurement(IoTDeviceMeasurement measurement) {
		M_Measurement mMeasurement = new M_Measurement();
		mMeasurement.setDeviceIdUserId(measurement.getDeviceId(), measurement.getUserId());
		mMeasurement.setCategory(measurement.getCategory());
		mMeasurement.setId(measurement.getSessionId(), measurement.getInSessionTime());
		mMeasurement.setTemperature(measurement.getTemperature());
		mMeasurement.setHumidity(measurement.getHumidity());
		mMeasurement.setLightLevel(measurement.getLightLevel());
		mMeasurement.setMotionCounts(measurement.getMotionCounts());
		mMeasurement.setRawdata(measurement.getRawData());
		
		return mMeasurement;
	}
	
	public static M_UserCustomProfile TrackingProfile2M_UserCustomProfile(TrackingProfile trackingProfile) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		M_UserCustomProfile mUserCustomProfile = new M_UserCustomProfile();
		mUserCustomProfile.setUserId(trackingProfile.getCustomizedUser());
		mUserCustomProfile.setCategory(trackingProfile.getProductCategory());
		mUserCustomProfile.setCustomName(trackingProfile.getProfileName());
		mUserCustomProfile.setTimeOnlyModel(mapper.writeValueAsString(trackingProfile.getTimeOnlyModel()));
		mUserCustomProfile.setUserAlert(mapper.writeValueAsString(trackingProfile.getUserAlert()));
		
		return mUserCustomProfile;
	}
	
	public static TrackingProfile M_UserCustomProfile2TrackingProfile(M_UserCustomProfile mUserCustomProfile) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		TrackingProfile trackingProfile = new TrackingProfile();
		trackingProfile.setCustomizedUser(mUserCustomProfile.getUserId());
		trackingProfile.setProductCategory(mUserCustomProfile.getCategory());
		trackingProfile.setProfileName(mUserCustomProfile.getCustomName());
		if (mUserCustomProfile.getTimeOnlyModel() != null && !mUserCustomProfile.getTimeOnlyModel().isEmpty()) {
			trackingProfile.setTimeOnlyModel(mapper.readValue(mUserCustomProfile.getTimeOnlyModel(), TimeOnlyModel.class));
		}
		if (mUserCustomProfile.getUserAlert() != null && !mUserCustomProfile.getUserAlert().isEmpty()) {
			trackingProfile.setUserAlert(mapper.readValue(mUserCustomProfile.getUserAlert(), UserAlert.class));
		}
		trackingProfile.setLastUpdatedTime(mUserCustomProfile.getLastUpdatedTime());
		
		return trackingProfile;
	}
	
	
	public static void main(String[] args) {
	}

}
