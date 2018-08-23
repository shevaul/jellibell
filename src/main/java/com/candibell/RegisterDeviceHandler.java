package com.candibell;

import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.candibell.common.ApiMapper;
import com.candibell.common.Constant;
import com.candibell.common.EnumValue;
import com.candibell.common.Utils;
import com.candibell.dal.DBUtil;
import com.candibell.dal.M_Device;
import com.candibell.rest.IoTDevice;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class RegisterDeviceHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger logger = Logger.getLogger(RegisterDeviceHandler.class);
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		try {
			String body = Utils.getHttpBody(input);
			if (body == null || body.isEmpty()) {
				return ApiGatewayResponse.missingMandatoryField("Cannot locate body object");
			}
			IoTDevice iotDevice = objectMapper.readValue(body, IoTDevice.class);
			if (iotDevice == null) {
				return ApiGatewayResponse.missingMandatoryField("Cannot parse iotDevice object");
			}
			if (iotDevice.getDeviceId() == null || iotDevice.getDeviceId().isEmpty()) {
				return ApiGatewayResponse.missingMandatoryField("Missing deviceId");
			}
			if (Constant.DEVICE_ID_HEXSTR_LENGTH != iotDevice.getDeviceId().length()) {
				return ApiGatewayResponse.badField("Device Id is not supported:" + iotDevice.getDeviceId());
			}
			if (iotDevice.getUserId() == null || iotDevice.getUserId().isEmpty()) {
				return ApiGatewayResponse.missingMandatoryField("Missing userId");
			}
			if (iotDevice.getDeviceType() == null || EnumValue.DeviceType.valueOf(iotDevice.getDeviceType()) == null) {
				return ApiGatewayResponse.missingMandatoryField("Missing or invalid deviceType:" + iotDevice.getDeviceType());
			}

			M_Device mDevice = new M_Device().getDeviceById(iotDevice.getDeviceId());
			if (mDevice != null) {
				if (!iotDevice.getUserId().equalsIgnoreCase(mDevice.getUserId())) {
					logger.error("Device already registered to: " + mDevice.getUserId() + ", ignore request by:" + iotDevice.getUserId());
					return ApiGatewayResponse.unauthorized(input);
				}
			} else {
				mDevice = ApiMapper.IoTDevice2M_Device(iotDevice);
				DBUtil.registerDevice2User(mDevice);
			}
			
			return ApiGatewayResponse.successWithBody(mDevice);
		} catch (Exception ex) {
			logger.error("Error in saving device: " + ex);
			return ApiGatewayResponse.serverInternalError(input);
		}
	}
}
