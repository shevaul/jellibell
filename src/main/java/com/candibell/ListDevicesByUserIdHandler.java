package com.candibell;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.candibell.common.ApiMapper;
import com.candibell.common.Constant;
import com.candibell.dal.M_Device;
import com.candibell.rest.IoTDevice;

public class ListDevicesByUserIdHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger logger = Logger.getLogger(ListDevicesByUserIdHandler.class);
	
	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		try {
			@SuppressWarnings("unchecked")
			Map<String,String> pathParameters =  (Map<String,String>) input.get("pathParameters");
			String userId = pathParameters.get("userid");

			try {
				userId = URLDecoder.decode(userId, Constant.URL_ENCODING);
			} catch (Exception e) {
				logger.warn("Bad userId:" + userId);
				return ApiGatewayResponse.badField("Bad userId:" + userId);
			}

			List<M_Device> mDevices = new M_Device().getDevicesByUserId(userId);
			List<IoTDevice> devices = new ArrayList<>(mDevices.size());
			for (M_Device mDevice : mDevices) {
				devices.add(ApiMapper.M_Device2IoTDevice(mDevice));
			}
			return ApiGatewayResponse.successWithBody(devices);
			
		} catch (Exception ex) {
			logger.error("Error in retrieving devices: " + ex);
			return ApiGatewayResponse.serverInternalError(input);
		}
	}
}
