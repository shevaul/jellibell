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
import com.candibell.dal.M_UserCustomProfile;
import com.candibell.product.profile.TrackingProfile;

public class GetCustomProfilesByUserIdHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
	
	private static final Logger logger = Logger.getLogger(GetCustomProfilesByUserIdHandler.class);
	
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
			
			List<M_UserCustomProfile> mUserCustomProfiles = M_UserCustomProfile.getCustomeProfilesByUserId(userId);
			List<TrackingProfile> profiles = new ArrayList<>(mUserCustomProfiles.size());
			for (M_UserCustomProfile mProfile : mUserCustomProfiles) {
				profiles.add(ApiMapper.M_UserCustomProfile2TrackingProfile(mProfile));
			}
			return ApiGatewayResponse.successWithBody(profiles);
		} catch (Exception ex) {
			logger.error("Error in retrieving custom profiles: " + ex);
			return ApiGatewayResponse.serverInternalError(input);
		}
	}
}
