package com.candibell.product.profile;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, creatorVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
public class TrackingProfile implements Comparable<TrackingProfile> {
	
	@JsonProperty("customizedUser")
	private String customizedUser;
	
	@JsonProperty("productCategory")
	private String productCategory;
	
	@JsonProperty("profileName")
	private String profileName; 
	
	@JsonProperty("lastUpdatedTime")
	private String lastUpdatedTime;
	
	@JsonProperty("timeOnlyModel")
	private TimeOnlyModel timeOnlyModel;
	
	@JsonProperty("userAlert")
	private UserAlert userAlert;

	public TimeOnlyModel getTimeOnlyModel() {
		return timeOnlyModel;
	}

	public void setTimeOnlyModel(TimeOnlyModel timeOnlyModel) {
		this.timeOnlyModel = timeOnlyModel;
	}

	public UserAlert getUserAlert() {
		return userAlert;
	}

	public void setUserAlert(UserAlert userAlert) {
		this.userAlert = userAlert;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getCustomizedUser() {
		return customizedUser;
	}

	public void setCustomizedUser(String customizedUser) {
		this.customizedUser = customizedUser;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(String lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

	@Override
	public String toString() {
		return "TrackingProfile [timeOnlyModel=" + timeOnlyModel + ", userAlert=" + userAlert + ", productCategory="
				+ productCategory + ", customizedUser=" + customizedUser + ", profileName=" + profileName
				+ ", lastUpdatedTime=" + lastUpdatedTime + "]";
	}

	@Override
	public int compareTo(TrackingProfile tp) {
		if (tp == null) {
			return -1;
		}
		String s1 = productCategory + customizedUser;
		return s1.compareTo(tp.productCategory + tp.customizedUser);
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 31*result + productCategory.hashCode();
		if (customizedUser != null) {
			result = 31*result + customizedUser.hashCode();
		}
		return result;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
        if (!(o instanceof TrackingProfile)) {
            return false;
        }
        TrackingProfile tp = (TrackingProfile) o;
        boolean result = productCategory.equals(tp.productCategory);
        if (customizedUser != null) {
        	result = result || customizedUser.equals(tp.customizedUser);
        }
		return result;
	}
}
