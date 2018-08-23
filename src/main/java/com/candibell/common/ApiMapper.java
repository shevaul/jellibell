package com.candibell.common;

import com.candibell.dal.M_Device;
import com.candibell.rest.IoTDevice;

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

	
	public static void main(String[] args) {
	}

}
