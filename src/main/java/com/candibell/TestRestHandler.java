package com.candibell;

import java.util.Collections;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class TestRestHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger logger = Logger.getLogger(TestRestHandler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		logger.info("------------TestRestHandler Entrance------------");
		
		logger.info("+++++++++Input Content+++++++++");
		for (Map.Entry<String, Object> entry : input.entrySet()) {
			logger.info(entry.getKey() + " --> " + entry.getValue());
		}
		
		logger.info("------------TestRestHandler Exit------------");
		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
				.build();
	}
	
	public static void main(String[] args) {
	}

}
