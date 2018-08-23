package com.candibell;

import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.candibell.common.Utils;

public final class ReportSensorMeasurementsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
	
	private static final Logger logger = Logger.getLogger(ReportSensorMeasurementsHandler.class);
	
	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		try {
			String body = Utils.getHttpBody(input);
			logger.warn("+++++++++++++++++++++");
			logger.warn(input);
			logger.warn("+++++++++++++++++++++");
			logger.warn(body);
		}  catch (Exception ex) {
			logger.error("Error in report sensor measurements: " + ex);
			return ApiGatewayResponse.serverInternalError(input);
		}
		return ApiGatewayResponse.successNoBody();
	}

}
