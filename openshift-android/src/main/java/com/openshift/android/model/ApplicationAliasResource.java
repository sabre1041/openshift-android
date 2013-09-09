package com.openshift.android.model;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class ApplicationAliasResource implements OpenshiftResource {
	
	private String id;
	
	@SerializedName("certificate_added_at")
	private Date certificateAddedAt;
	
	@SerializedName("has_private_ssl_certificate")
	private boolean privateSslCertificate;
	
	private ApplicationResource applicationResource;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCertificateAddedAt() {
		return certificateAddedAt;
	}

	public void setCertificateAddedAt(Date certificateAddedAt) {
		this.certificateAddedAt = certificateAddedAt;
	}

	public boolean isPrivateSslCertificate() {
		return privateSslCertificate;
	}

	public void setPrivateSslCertificate(boolean privateSslCertificate) {
		this.privateSslCertificate = privateSslCertificate;
	}

	public ApplicationResource getApplicationResource() {
		return applicationResource;
	}

	public void setApplicationResource(ApplicationResource applicationResource) {
		this.applicationResource = applicationResource;
	}

}
