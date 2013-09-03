package com.openshift.android.rest;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UnknownFormatConversionException;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.openshift.android.OpenshiftAndroidApplication;
import com.openshift.android.OpenshiftConstants;
import com.openshift.android.security.AuthorizationManager;

public class OpenshiftAndroidRequest<T> extends Request<T> {

	private final Gson gson = GsonSingleton.getInstance();
	private final Map<String, String> headers;
	private final Listener<T> listener;
	private final Map<String,String> parameters;
	private int method;
	private Class<T> clazz;
	private Type type;

	public OpenshiftAndroidRequest(int method, String url, Class<T> clazz,
			Map<String, String> headers, Map<String,String> parameters, Listener<T> listener,
			ErrorListener errorListener) {
		super(method, url, errorListener);
		this.parameters = parameters;
		this.method = method;
		this.clazz = clazz;
		this.headers = headers;
		this.listener = listener;
	}

	public OpenshiftAndroidRequest(int method, String url, Type type,
			Map<String, String> headers, Map<String,String> parameters, Listener<T> listener,
			ErrorListener errorListener) {
		super(method, url, errorListener);
		this.method = method;
		this.parameters = parameters;
		this.type = type;
		this.headers = headers;
		this.listener = listener;
	}
	
    @Override
    public Map<String, String> getParams() {
    	Map<String, String> params = parameters != null ? parameters : new HashMap<String,String>();
    	
    	// Add nolinks Query Parameter to suppress output size/length
    		params.put("nolinks", "true");
    	
        return params;
    }


	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> head = headers != null ? headers : new HashMap<String,String>();
		
		
		// Add Authentication Header
		AuthorizationManager authorizationManager = OpenshiftAndroidApplication
				.getInstance().getAuthorizationManger();

		if (authorizationManager.getOpenshiftAccount() != null
				&& authorizationManager.getOpenshiftPassword() != null) {

			String auth = "Basic "
					+ Base64.encodeToString(
							(authorizationManager.getOpenshiftAccount() + ":" + authorizationManager
									.getOpenshiftPassword()).getBytes(),
							Base64.NO_WRAP);
			head.put("Authorization", auth);
		}

		// Openshift API Version
		head.put("Accept","application/json; version="+OpenshiftConstants.REST_API_VERSION);
		
		return head;
	}

	@Override
	protected void deliverResponse(T response) {
		listener.onResponse(response);
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
		
			return Response.success(getJson(json),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}
	
	@Override
    public String getBodyContentType() {
        return "application/x-www-form-urlencoded";
    }


	private T getJson(String json) throws UnknownFormatConversionException {
		if (type != null) {
			return gson.fromJson(json, type);
		}

		if (clazz != null) {
			return gson.fromJson(json, clazz);
		}

		throw new UnknownFormatConversionException("Class or Type not Defined");
	}
}
