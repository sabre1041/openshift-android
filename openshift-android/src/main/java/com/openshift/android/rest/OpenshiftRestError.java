
package com.openshift.android.rest;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

@SuppressWarnings("serial")
public class OpenshiftRestError extends VolleyError {
	
	private Object obj;
	
    public OpenshiftRestError() {
        super();
    }

    public OpenshiftRestError(Throwable cause) {
        super(cause);
    }

    public OpenshiftRestError(NetworkResponse networkResponse, Object obj) {
        super(networkResponse);
        this.obj = obj;
    }
    
    public Object getObject() {
    	return obj;
    }
    
}
