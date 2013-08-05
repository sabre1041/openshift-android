package com.openshift.android.model;

import com.google.gson.annotations.SerializedName;

public class ApplicationResource implements OpenshiftResource {

	@SerializedName("app_url")
	private String applicationUrl;
	
	@SerializedName("build_job_url")
	private String buildJobUrl;
	
	@SerializedName("building_app")
	private String buildingApplication;
	
	@SerializedName("creation_time")
	private String creationTime;
	
	private String framework;
	
	@SerializedName("gearCount")
	private String gearCount;
	
	@SerializedName("git_url")
	private String gitUrl;
	
	private String name;
	
	@SerializedName("domain_id")
	private String domain;

	public String getApplicationUrl() {
		return applicationUrl;
	}

	public void setApplicationUrl(String applicationUrl) {
		this.applicationUrl = applicationUrl;
	}

	public String getBuildJobUrl() {
		return buildJobUrl;
	}

	public void setBuildJobUrl(String buildJobUrl) {
		this.buildJobUrl = buildJobUrl;
	}

	public String getBuildingApplication() {
		return buildingApplication;
	}

	public void setBuildingApplication(String buildingApplication) {
		this.buildingApplication = buildingApplication;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public String getFramework() {
		return framework;
	}

	public void setFramework(String framework) {
		this.framework = framework;
	}

	public String getGearCount() {
		return gearCount;
	}

	public void setGearCount(String gearCount) {
		this.gearCount = gearCount;
	}

	public String getGitUrl() {
		return gitUrl;
	}

	public void setGitUrl(String gitUrl) {
		this.gitUrl = gitUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
}
