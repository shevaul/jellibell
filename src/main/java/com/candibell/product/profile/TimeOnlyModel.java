package com.candibell.product.profile;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, creatorVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
public class TimeOnlyModel {
	
//	private static final Integer DEFAULT_TRACKING_HOURS = 60;
//	private static final Double DEFAULT_DETERIATE_SPEED = 1.0/(3600*DEFAULT_TRACKING_HOURS);
	
	private static final Integer DEFAULT_TRACKING_SECONDS = 72 * 3600;
	
	@Deprecated
	@JsonProperty("trackingPeriodInHours")
	private Integer trackingPeriodInHours;
	
	@Deprecated
	@JsonProperty("deteriateSpeedPerSec")
	private Double deteriateSpeedPerSec;
	
	@JsonProperty("trackingPeriodInSeconds")
	private Integer trackingPeriodInSeconds = DEFAULT_TRACKING_SECONDS;
	
	public TimeOnlyModel() {
	}
	
	public TimeOnlyModel(Integer trackingPeriodInHours) {
		this.setTrackingPeriodInHours(trackingPeriodInHours);
		this.setDeteriateSpeedPerSec(1.0/(trackingPeriodInHours*3600));
	}
	
	public Integer getTrackingPeriodInHours() {
		return trackingPeriodInHours;
	}

	public void setTrackingPeriodInHours(Integer trackingPeriodInHours) {
		this.trackingPeriodInHours = trackingPeriodInHours;
	}

	public double getDeteriateSpeedPerSec() {
		return deteriateSpeedPerSec;
	}

	public void setDeteriateSpeedPerSec(double deteriateSpeedPerSec) {
		this.deteriateSpeedPerSec = deteriateSpeedPerSec;
	}
	
	public Integer getTrackingPeriodInSeconds() {
		return trackingPeriodInSeconds;
	}
	
	public void setTrackingPeriodInSeconds(Integer trackingPeriodInSeconds) {
		this.trackingPeriodInSeconds = trackingPeriodInSeconds;
	}
	
	@Override
	public String toString() {
		return "TimeOnlyModel [trackingPeriodInHours=" + trackingPeriodInHours + ", deteriateSpeedPerSec="
				+ deteriateSpeedPerSec + ", trackingPeriodInSeconds=" + trackingPeriodInSeconds + "]";
	}

	
	public static void main(String[] args) {
//		System.out.println(new TimeOnlyModel());
//		System.out.println(new TimeOnlyModel(10));
		
	}

	

}
