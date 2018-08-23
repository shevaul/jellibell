package com.candibell;

import java.net.URLDecoder;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.candibell.common.ApiMapper;
import com.candibell.common.Constant;
import com.candibell.dal.M_Device;

public final class GetDeviceHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger logger = Logger.getLogger(GetDeviceHandler.class);
	
	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		try {
			@SuppressWarnings("unchecked")
			Map<String,String> pathParameters =  (Map<String,String>) input.get("pathParameters");
			String id = pathParameters.get("id");
			@SuppressWarnings("unchecked")
			Map<String,String> headers = (Map<String,String>) input.get("headers");
			String authHeader = headers.get("Authorization");
			logger.info("Authorization Header is:" + authHeader);

			try {
				id = URLDecoder.decode(id, Constant.URL_ENCODING);
			} catch (Exception e) {
				logger.warn("Bad device id:" + id);
				return ApiGatewayResponse.badField("Bad device id:" + id);
			}
			
			M_Device mDevice = new M_Device().getDeviceById(id);

			if (mDevice != null) {
				return ApiGatewayResponse.successWithBody(ApiMapper.M_Device2IoTDevice(mDevice));
			} else {
				return ApiGatewayResponse.notFound("Device with id: '" + id + "' not found.");
			}
		} catch (Exception ex) {
			logger.error("Error in retrieving device: " + ex);
			return ApiGatewayResponse.serverInternalError(input);
		}
	}
}
