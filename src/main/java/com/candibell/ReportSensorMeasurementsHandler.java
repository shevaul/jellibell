package com.candibell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.candibell.common.ApiMapper;
import com.candibell.common.Utils;
import com.candibell.dal.M_Device;
import com.candibell.dal.M_Measurement;
import com.candibell.firmware.api.FirmwareTLV;
import com.candibell.rest.IoTDeviceMeasurement;

public final class ReportSensorMeasurementsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
	
	private static final Logger logger = Logger.getLogger(ReportSensorMeasurementsHandler.class);
	
	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		try {
			String tlv = Utils.getHttpBody(input);
			
			String hubId = FirmwareTLV.parseHubIdFromMeasurementsTLV(tlv);
			if (hubId == null || hubId.isEmpty()) {
				logger.info("Cannot parse HubId from TLV:" + tlv);
				return ApiGatewayResponse.badField("Cannot parse HubId from TLV:" + tlv);
			}
			M_Device mDevice = new M_Device().getDeviceById(hubId);
			if (mDevice == null) {
				logger.info("No Hub found, id:" + hubId + ", TLV:" + tlv);
				return ApiGatewayResponse.badField("No Hub found, id:" + hubId);
			}
			if (mDevice.getUserId() == null || mDevice.getUserId().isEmpty()) {
				logger.info("No User found associated with Hub, id:" + hubId);
				return ApiGatewayResponse.badField("No User found associated with Hub, id:" + hubId);
			}
			List<IoTDeviceMeasurement> measurements = FirmwareTLV.parseHubUploadMeasurementsTLV(tlv);
			if (measurements == null || measurements.isEmpty()) {
				logger.info("Cannot parse measurements from TLV:" + tlv);
				return ApiGatewayResponse.badField("Cannot parse measurements from TLV:" + tlv);
			}
			
			for (IoTDeviceMeasurement measurement : measurements) {
				measurement.setUserId(mDevice.getUserId());
			}
			
			List<M_Measurement> mMeasurements = new ArrayList<>(measurements.size());
			for (IoTDeviceMeasurement measurement : measurements) {
				mMeasurements.add(ApiMapper.IoTDeviceMeasurement2M_Measurement(measurement));
			}
			
			M_Measurement.batchSave(mMeasurements);
			
			// Update Live Sessions
			for (M_Measurement measurement : mMeasurements) {
				try {
					updateLiveSession(measurement);
				} catch (Throwable t) {
					logger.warn("Failed to update live session for measurement:" + measurement, t);
				}
			}
		}  catch (Exception ex) {
			logger.error("Error in report sensor measurements: " + ex);
			return ApiGatewayResponse.serverInternalError(input);
		}
		return ApiGatewayResponse.successNoBody();
	}
	
	private static void updateLiveSession(M_Measurement measurement) {
		//If session not there or session id is different; restart
		
	}

}
