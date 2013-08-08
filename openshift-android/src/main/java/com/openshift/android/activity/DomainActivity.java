package com.openshift.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.openshift.android.R;
import com.openshift.android.model.DomainResource;
import com.openshift.android.model.OpenshiftDataList;
import com.openshift.android.model.OpenshiftResponse;
import com.openshift.android.processor.OpenshiftActions;
import com.openshift.android.rest.RestRequest;
import com.openshift.android.security.AuthorizationManager;
import com.openshift.android.service.OpenshiftServiceHelper;


/** Displays a list of Domains owned by the currently logged in User
 * 
 * @author Andrew Block
 * 
 * @see Activity
 *
 */
public class DomainActivity extends ListActivity {
	
	private BroadcastReceiver requestReceiver;

	private OpenshiftServiceHelper mOpenshiftServiceHelper;
	
	public static final String DOMAIN_RESOURCE_EXTRA = "com.redhat.openshift.DOMAIN_MESSAGE";

	
	private ArrayAdapter<DomainResource> domainAdapter;
	private List<DomainResource> domainList = new ArrayList<DomainResource>();
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_domain);
	    	    
	    domainAdapter = new ArrayAdapter<DomainResource>(this, R.layout.simple_textview_layout, android.R.id.text1, domainList);
	    setListAdapter(domainAdapter);
	    
		

	}
	
	/**
	 * Displays a logout option when the Menu button is pressed
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Logout");
		return true;
	}
	
	/**
	 * Performs the action from a menu selection
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(item.getTitle().equals("Logout")) {
			AuthorizationManager.getInstance(getApplicationContext()).invalidateAuthentication();
			
			Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			return true;
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
	
	
	/**
	 * Shows a short toast display
	 * 
	 * @param message message to display
	 */
	private void showToast(String message) {
		if(!isFinishing()){
			Toast toast = Toast.makeText(this,message, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();

		}

	}
	
	@Override
	public void onPause() {
		if(requestReceiver != null) {
			this.unregisterReceiver(requestReceiver);
		}
		
		super.onPause();
	}
	
	/**
	 * Initializes the {@link IntentFilter} to listen for Rest Responses from the listing of domain operation.
	 */
	@Override
	public void onResume() {
		
		super.onResume();
		
		IntentFilter filter = new IntentFilter(OpenshiftActions.LIST_DOMAINS);
		requestReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				 
				RestRequest<OpenshiftResponse<OpenshiftDataList<DomainResource>>> domainRequest = (RestRequest<OpenshiftResponse<OpenshiftDataList<DomainResource>>>) intent.getSerializableExtra(OpenshiftServiceHelper.EXTRA_RESULT_DATA);
				Log.v(DomainActivity.class.getPackage().getName(),"Retrieved Domains Response Code: "+domainRequest.getStatus());
				OpenshiftResponse<OpenshiftDataList<DomainResource>> response = (OpenshiftResponse<OpenshiftDataList<DomainResource>>) domainRequest.getResponse();
				
				if(domainRequest.getStatus()==200){	
					domainAdapter.clear();
					Log.v(DomainActivity.class.getPackage().getName(),"Retrieved Domains Size: "+domainList.size());
					
					
					for(int i = 0; i<response.getData().getList().size();i++){ 
						domainAdapter.insert(response.getData().getList().get(i), i);
					}
					
					domainAdapter.setNotifyOnChange(true);					
					domainAdapter.notifyDataSetChanged();


				}
				else {
					showToast("Failed to Retrieve Domain Information: "+domainRequest.getMessage());
				}
				
				
			}
			
		};
		
		mOpenshiftServiceHelper = OpenshiftServiceHelper.getInstance(this);
		this.registerReceiver(requestReceiver, filter);
		
		mOpenshiftServiceHelper.listDomains();

	}


}
