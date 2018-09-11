package com.candibell.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, creatorVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_DEFAULT)
public final class IoTDeviceMeasurement {
	
	@JsonProperty("userId")
	private String userId;
	
	@JsonProperty("deviceId")
	private String deviceId;
	
	@JsonProperty("category")
	private String category;
	
	@JsonProperty("sessionId")
	private String sessionId;
	
	@JsonProperty("inSessionTime")
	private String inSessionTime;
	
	@JsonProperty("temperature")
	private Integer temperature;
	
	@JsonProperty("humidity")
	private Integer humidity;
	
	@JsonProperty("lightLevel")
	private Integer lightLevel;
	
	@JsonProperty("rawData")
	private String rawData;
	
	@JsonProperty("motionCounts")
	private Integer motionCounts;
	
	@JsonProperty("lastUpdatedTime")
	private String lastUpdatedTime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getInSessionTime() {
		return inSessionTime;
	}

	public void setInSessionTime(String inSessionTime) {
		this.inSessionTime = inSessionTime;
	}

	public Integer getTemperature() {
		return temperature;
	}

	public void setTemperature(Integer temperature) {
		this.temperature = temperature;
	}

	public Integer getHumidity() {
		return humidity;
	}

	public void setHumidity(Integer humidity) {
		this.humidity = humidity;
	}

	public Integer getLightLevel() {
		return lightLevel;
	}

	public void setLightLevel(Integer lightLevel) {
		this.lightLevel = lightLevel;
	}

	public Integer getMotionCounts() {
		return motionCounts;
	}

	public void setMotionCounts(Integer motionCounts) {
		this.motionCounts = motionCounts;
	}
	
	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public String getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(String lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

	@Override
	public String toString() {
		return "IoTDeviceMeasurement [userId=" + userId + ", deviceId=" + deviceId + ", category=" + category
				+ ", sessionId=" + sessionId + ", inSessionTime=" + inSessionTime + ", temperature=" + temperature
				+ ", humidity=" + humidity + ", lightLevel=" + lightLevel + ", rawData=" + rawData + ", motionCounts="
				+ motionCounts + ", lastUpdatedTime=" + lastUpdatedTime + "]";
	}

}
