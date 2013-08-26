package com.openshift.android.model;

import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * Model class representing an OpenShift Application
 * 
 * @author Andrew Block
 * 
 * @see OpenshiftResource
 * @see SerializedName
 *
 */
public class ApplicationResource implements OpenshiftResource {

	@SerializedName("app_url")
	private String applicationUrl;
	
	@SerializedName("build_job_url")
	private String buildJobUrl;
	
	@SerializedName("building_app")
	private String buildingApp;
	
	@SerializedName("building_with")
	private String buildingWith;
	
	private List<CartridgeResource> cartridges;
	
	@SerializedName("creation_time")
	private Date creationTime;
	
	@SerializedName("domain_id")
	private String domainId;
	
	private String framework;
	
	@SerializedName("gear_count")
	private String gearCount;
	
	@SerializedName("gear_profile")
	private String gearProfile;
	
	@SerializedName("git_url")
	private String gitUrl;
	
	@SerializedName("health_check_path")
	private String healthCheckPath;
	
	private String name;
	
	private boolean scalable;
	
	@SerializedName("ssh_url")
	private String sshUrl;

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

	public String getBuildingApp() {
		return buildingApp;
	}

	

	
	public String getBuildingWith() {
		return buildingWith;
	}

	public void setBuildingWith(String buildingWith) {
		this.buildingWith = buildingWith;
	}

	public void setBuildingApp(String buildingApp) {
		this.buildingApp = buildingApp;
	}
	
	public void setBuildingApplication(String buildingApp) {
		this.buildingApp = buildingApp;
	}

	public List<CartridgeResource> getCartridges() {
		return cartridges;
	}

	public void setCartridges(List<CartridgeResource> cartridges) {
		this.cartridges = cartridges;
	}

	public Date getCreationTime() {
		return creationTime;
	}
	
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}


	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
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
	
	public String getGearProfile() {
		return gearProfile;
	}

	public void setGearProfile(String gearProfile) {
		this.gearProfile = gearProfile;
	}
	
	public String getGitUrl() {
		return gitUrl;
	}

	public void setGitUrl(String gitUrl) {
		this.gitUrl = gitUrl;
	}
	

	public String getHealthCheckPath() {
		return healthCheckPath;
	}

	public void setHealthCheckPath(String healthCheckPath) {
		this.healthCheckPath = healthCheckPath;
	}

	public boolean isScalable() {
		return scalable;
	}

	public void setScalable(boolean scalable) {
		this.scalable = scalable;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public String getSshUrl() {
		return sshUrl;
	}

	public void setSshUrl(String sshUrl) {
		this.sshUrl = sshUrl;
	}
	
	@Override
	public String toString() {
		return name;
	}


	
}
