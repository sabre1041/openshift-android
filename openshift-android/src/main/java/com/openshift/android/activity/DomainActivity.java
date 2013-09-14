package com.openshift.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.openshift.android.OpenshiftAndroidApplication;
import com.openshift.android.OpenshiftConstants;
import com.openshift.android.R;
import com.openshift.android.adapter.DomainAdapter;
import com.openshift.android.model.DomainResource;
import com.openshift.android.model.OpenshiftDataList;
import com.openshift.android.model.OpenshiftResponse;
import com.openshift.android.rest.OpenshiftRestManager;
import com.openshift.android.security.AuthorizationManager;
import com.openshift.android.util.ActivityUtils;


/** Displays a list of Domains owned by the currently logged in User
 * 
 * @author Andrew Block
 * 
 * @see Activity
 *
 */
public class DomainActivity extends ListActivity {
		
	public static final String DOMAIN_RESOURCE_EXTRA = "com.openshift.android.DOMAIN_MESSAGE";

	private DomainAdapter domainAdapter;
	private List<DomainResource> domainList = new ArrayList<DomainResource>();
	private LinearLayout progressLayout;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_domain);
	    	    
	    domainAdapter = new DomainAdapter(this, R.layout.domain_row_layout, domainList);
	    setListAdapter(domainAdapter);
	    
	    progressLayout = (LinearLayout) findViewById(R.id.domainLinearLayout);
	    
	    progressLayout.setVisibility(View.VISIBLE);
	    
	    OpenshiftRestManager.getInstance().listDomains(new Response.Listener<OpenshiftResponse<OpenshiftDataList<DomainResource>>>() {

					@Override
					public void onResponse(
							OpenshiftResponse<OpenshiftDataList<DomainResource>> response) {
						displayList(response);
						progressLayout.setVisibility(View.GONE);
						
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						ActivityUtils.showToast(getApplicationContext(), "Failed to Display Domains");
						progressLayout.setVisibility(View.GONE);
					}
				}, OpenshiftConstants.DOMAINACTIVITY_TAG);
	    
	    
	}
	
	/**
	 * Displays a logout option when the Menu button is pressed
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.domain_menu, menu);

		return true;
	}
	
	/**
	 * Performs the action from a menu selection
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()) {
			case R.id.menu_logout:
				AuthorizationManager.getInstance(getApplicationContext()).invalidateAuthentication();
				OpenshiftAndroidApplication.getInstance().getRequestQueue().getCache().clear();
				
				Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
				
			}

		
		return super.onOptionsItemSelected(item);
		
	}
	
	
	
	@Override
	protected void onListItemClick (ListView l, View v, int position, long id) {
		
		DomainResource domainResource = (DomainResource) l.getAdapter().getItem(position);
		Intent intent = new Intent(this, ApplicationListActivity.class);
		intent.putExtra(DomainActivity.DOMAIN_RESOURCE_EXTRA, domainResource);
		startActivity(intent);
	}
	

    @Override
    public void onStop() {
    	
    	super.onStop();
    	
		OpenshiftAndroidApplication.getInstance().getRequestQueue().cancelAll(OpenshiftConstants.DOMAINACTIVITY_TAG);

    }
		
	@Override
	public void onPause() {

		super.onPause();
	}
	
	/**
	 * Initializes the {@link IntentFilter} to listen for Rest Responses from the listing of domain operation.
	 */
	@Override
	public void onResume() {
		
		super.onResume();
		
	}
	
	/**
	 * Binds the Domain Response Data to the List
	 * 
	 * @param response The response data to fill the adapter
	 */
	private void displayList(OpenshiftResponse<OpenshiftDataList<DomainResource>> response) {
		
		domainAdapter.clear();
		
		
		for(int i = 0; i<response.getData().getList().size();i++){ 
			Log.v(DomainActivity.class.getPackage().getName(),"Retrieved Domains : "+response.getData().getList().get(i).getName());

			domainAdapter.insert(response.getData().getList().get(i), i);
		}
		
		domainAdapter.setNotifyOnChange(true);					
		domainAdapter.notifyDataSetChanged();

	}


}
