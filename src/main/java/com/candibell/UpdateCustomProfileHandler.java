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

public final class UpdateCustomProfileHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
	
	private static final Logger logger = Logger.getLogger(UpdateCustomProfileHandler.class);
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
			
			M_UserCustomProfile updateUserCustomProfile = null;
			try {
				updateUserCustomProfile = ApiMapper.TrackingProfile2M_UserCustomProfile(profile);
			} catch (JsonProcessingException jpe) {
				return ApiGatewayResponse.badField("Input cannot be properly parsed:" + profile);
			}
			M_UserCustomProfile mUserCustomProfile = M_UserCustomProfile.getUserCustomProfile(updateUserCustomProfile.getUserId(), updateUserCustomProfile.getCategory());
			if (mUserCustomProfile == null) {
				return ApiGatewayResponse.notFound("Cannot find user custom profile: " + updateUserCustomProfile.getUserId() + ", " + updateUserCustomProfile.getCategory());
			}
			
			//Update Values:
			if (updateUserCustomProfile.getCustomName() != null && !updateUserCustomProfile.getCustomName().isEmpty()) {
				mUserCustomProfile.setCustomName(updateUserCustomProfile.getCustomName());
			}
			if (updateUserCustomProfile.getTimeOnlyModel() != null && !updateUserCustomProfile.getTimeOnlyModel().isEmpty()) {
				mUserCustomProfile.setTimeOnlyModel(updateUserCustomProfile.getTimeOnlyModel());
			}
			if (updateUserCustomProfile.getUserAlert() != null && !updateUserCustomProfile.getUserAlert().isEmpty()) {
				mUserCustomProfile.setUserAlert(updateUserCustomProfile.getUserAlert());
			}
			mUserCustomProfile.update();
			return ApiGatewayResponse.successNoBody();
		} catch (Exception ex) {
			logger.error("Error in patch custom profile: " + ex);
			return ApiGatewayResponse.serverInternalError(input);
		}
	}

}
