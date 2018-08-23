package com.candibell.common;

import java.util.Map;

public class Utils {
	
	public static String getHttpBody(Map<String, Object> input) {
		return (String) input.get("body");
	}
	
}
