package com.candibell;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.candibell.product.profile.TimeOnlyModel;
import com.candibell.product.profile.TrackingProfile;

public class GetDefaultProfilesHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
	
	private static final Logger logger = Logger.getLogger(GetDefaultProfilesHandler.class);
	
	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		try {
			List<TrackingProfile> results = new ArrayList<>(loadDefaultTrackingProfiles());
			return ApiGatewayResponse.successWithBody(results);
		} catch (Exception ex) {
			logger.error("Error in fetching default profiles: " + ex);
			return ApiGatewayResponse.serverInternalError(input);
		}
	}
	
	public static Set<TrackingProfile> loadDefaultTrackingProfiles() {
		ClassLoader classLoader = GetDefaultProfilesHandler.class.getClassLoader();
		File file = new File(classLoader.getResource("DefaultProfiles").getFile());

		Set<TrackingProfile> profiles = new LinkedHashSet<>(32);
		try (Scanner scanner = new Scanner(file)) {
			String line = scanner.nextLine(); // Ignore title line
			while (scanner.hasNextLine()) {
				line = scanner.nextLine();
				String[] records = line.split(",");
				if (records == null || records.length < 3) {
					throw new IllegalArgumentException("Default profile record cannot be parsed:" + line);
				}
				TrackingProfile profile = new TrackingProfile();
				profile.setProductCategory(records[0]);
				profile.setProfileName(records[1]);
				
				TimeOnlyModel timeOnlyModel = new TimeOnlyModel();
				Integer trackingTime = Integer.parseInt(records[2]);
				if (trackingTime > 0) {
					timeOnlyModel.setTrackingPeriodInSeconds(trackingTime);
				}
				profile.setTimeOnlyModel(timeOnlyModel);
				
				profiles.add(profile);
				System.out.println(profile);
			}
			scanner.close();
		} catch (Throwable e) {
			logger.error("Failed to load default profiles", e);
		}
		return profiles;
	}
	
	public static void main(String[] args) {
		loadDefaultTrackingProfiles();
	}

}
