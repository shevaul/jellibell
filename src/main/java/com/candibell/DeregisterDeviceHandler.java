package com.candibell;

import java.net.URLDecoder;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.candibell.common.Constant;
import com.candibell.dal.DBUtil;
import com.candibell.dal.M_Device;

public final class DeregisterDeviceHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger logger = Logger.getLogger(DeregisterDeviceHandler.class);
	
	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		try {
			@SuppressWarnings("unchecked")
			Map<String,String> pathParameters =  (Map<String,String>) input.get("pathParameters");
			String id = pathParameters.get("id");
			String userId = pathParameters.get("userId");
			
			try {
				id = URLDecoder.decode(id, Constant.URL_ENCODING);
			} catch (Exception e) {
				logger.warn("Bad device id:" + id);
				return ApiGatewayResponse.badField("Bad device id:" + id);
			}
			
			try {
				userId = URLDecoder.decode(userId, Constant.URL_ENCODING);
			} catch (Exception e) {
				logger.warn("Bad user id:" + userId);
				return ApiGatewayResponse.badField("Bad user id:" + userId);
			}
			
			M_Device mDevice = new M_Device().getDeviceById(id);
			if (mDevice != null) {
				if (!userId.equalsIgnoreCase(mDevice.getUserId())) {
					return ApiGatewayResponse.unauthorized(input);
				}
				DBUtil.deregisterDeviceFromUser(mDevice);
			}
			
			return ApiGatewayResponse.successNoBody();
		} catch (Exception ex) {
			logger.error("Error in deregistering device: " + ex);
			return ApiGatewayResponse.serverInternalError(input);
		}
	}
}
