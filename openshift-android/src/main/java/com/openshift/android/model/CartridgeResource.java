package com.openshift.android.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class CartridgeResource implements OpenshiftResource {
	
	@SerializedName("additional_gear_storage")
	private String additionalGearStorage;
	
	@SerializedName("base_gear_storage")
	private String baseGearStorage;
	
	@SerializedName("collocated_with")
	private List<String> collocatedWith;
	
	@SerializedName("currentScale")
	private Integer currentScale;
	
	private String description;
	
	@SerializedName("display_name")
	private String displayName;
	
	@SerializedName("gear_profile")
	private String gearProfile;
	
//	@SerializedName("help_topics")
//	private List<String> helpTopics;
	
	private String license;
	
	@SerializedName("license_url")
	private String licenseUrl;
	
	private String name;

	@SerializedName("scales_from")
	private Integer scalesFrom;
	
	@SerializedName("scales_to")
	private Integer scalesTo;
	
	@SerializedName("scales_with")
	private String scalesWith;
	
	// Status Message
	@SerializedName("status_messages")
	private List<StatusMessage> statusMessages;
	
	@SerializedName("supported_scales_from")
	private String supportedScalesFrom;
	
	@SerializedName("supported_scales_to")
	private String supportedScalesTo;
	
	private List<String> tags;
	
	private String type;
	
	private String url;
	
	// Usage Rates
	
	private String version;
	
	private String website;

	public String getAdditionalGearStorage() {
		return additionalGearStorage;
	}

	public void setAdditionalGearStorage(String additionalGearStorage) {
		this.additionalGearStorage = additionalGearStorage;
	}

	public String getBaseGearStorage() {
		return baseGearStorage;
	}

	public void setBaseGearStorage(String baseGearStorage) {
		this.baseGearStorage = baseGearStorage;
	}

	public List<String> getCollocatedWith() {
		return collocatedWith;
	}

	public void setCollocatedWith(List<String> collocatedWith) {
		this.collocatedWith = collocatedWith;
	}

	public Integer getCurrentScale() {
		return currentScale;
	}

	public void setCurrentScale(Integer currentScale) {
		this.currentScale = currentScale;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getGearProfile() {
		return gearProfile;
	}

	public void setGearProfile(String gearProfile) {
		this.gearProfile = gearProfile;
	}

//	public List<String> getHelpTopics() {
//		return helpTopics;
//	}
//
//	public void setHelpTopics(List<String> helpTopics) {
//		this.helpTopics = helpTopics;
//	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getLicenseUrl() {
		return licenseUrl;
	}

	public void setLicenseUrl(String licenseUrl) {
		this.licenseUrl = licenseUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getScalesFrom() {
		return scalesFrom;
	}

	public void setScalesFrom(Integer scalesFrom) {
		this.scalesFrom = scalesFrom;
	}

	public Integer getScalesTo() {
		return scalesTo;
	}

	public void setScalesTo(Integer scalesTo) {
		this.scalesTo = scalesTo;
	}

	public String getScalesWith() {
		return scalesWith;
	}

	public void setScalesWith(String scalesWith) {
		this.scalesWith = scalesWith;
	}

	public List<StatusMessage> getStatusMessages() {
		return statusMessages;
	}

	public void setStatusMessages(List<StatusMessage> statusMessages) {
		this.statusMessages = statusMessages;
	}

	public String getSupportedScalesFrom() {
		return supportedScalesFrom;
	}

	public void setSupportedScalesFrom(String supportedScalesFrom) {
		this.supportedScalesFrom = supportedScalesFrom;
	}

	public String getSupportedScalesTo() {
		return supportedScalesTo;
	}

	public void setSupportedScalesTo(String supportedScalesTo) {
		this.supportedScalesTo = supportedScalesTo;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

}
