package com.openshift.android.model;

import java.util.List;

/**
 * Wrapper class for holding a {@link List} of OpenShift Resources
 * 
 * @author Andrew Block
 *
 * @param <T> The Openshift Resource
 * 
 * @see OpenshiftResource
 */
public class OpenshiftDataList<T extends OpenshiftResource> implements OpenshiftResource  {
	
	private List<T> list;

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

}
