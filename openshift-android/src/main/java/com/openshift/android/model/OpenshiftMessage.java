package com.openshift.android.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class OpenshiftMessage implements Serializable {

	@SerializedName("exit_code")
	private String exitCode;
	
	private String field;
	private String severity;
	private String text;
	
	public String getExitCode() {
		return exitCode;
	}
	public void setExitCode(String exitCode) {
		this.exitCode = exitCode;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	
}
