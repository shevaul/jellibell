package com.candibell;

import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.candibell.common.ApiMapper;
import com.candibell.common.Utils;
import com.candibell.dal.M_UserCustomProfile;
import com.candibell.product.profile.TrackingProfile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class AddCustomProfileHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
	
	private static final Logger logger = Logger.getLogger(AddCustomProfileHandler.class);
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		try {
			String body = Utils.getHttpBody(input);
			if (body == null || body.isEmpty()) {
				return ApiGatewayResponse.missingMandatoryField("Cannot locate body object");
			}
			TrackingProfile profile = objectMapper.readValue(body, TrackingProfile.class);
			if (profile == null) {
				return ApiGatewayResponse.missingMandatoryField("Cannot parse tracking profile object");
			}
			if (profile.getCustomizedUser() == null || profile.getCustomizedUser().isEmpty()) {
				return ApiGatewayResponse.missingMandatoryField("Missing user id");
			}
			if (profile.getProductCategory() == null || profile.getProductCategory().isEmpty()) {
				return ApiGatewayResponse.missingMandatoryField("Missing product category");
			}
			if (profile.getTimeOnlyModel() == null || profile.getTimeOnlyModel().getTrackingPeriodInSeconds() == null
					|| profile.getTimeOnlyModel().getTrackingPeriodInSeconds() <= 0) {
				return ApiGatewayResponse.badField("Tracking seconds is mandatory and must great than 0");
			}
			
			M_UserCustomProfile newUserCustomProfile = null;
			try {
				newUserCustomProfile = ApiMapper.TrackingProfile2M_UserCustomProfile(profile);
			} catch (JsonProcessingException jpe) {
				return ApiGatewayResponse.badField("Input cannot be properly parsed:" + profile);
			}
			M_UserCustomProfile mUserCustomProfile = M_UserCustomProfile.getUserCustomProfile(newUserCustomProfile.getUserId(), newUserCustomProfile.getCategory());
			if (mUserCustomProfile != null) {
				return ApiGatewayResponse.badField("The profile is already created, please update it if needed");
			}
			newUserCustomProfile.update();
			
			return ApiGatewayResponse.successNoBody();
		} catch (Exception ex) {
			logger.error("Error in add custom profile: " + ex);
			return ApiGatewayResponse.serverInternalError(input);
		}
	}

}
