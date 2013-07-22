package com.redhat.openshift.activity;

import java.util.ArrayList;
import java.util.List;

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

import com.redhat.openshift.R;
import com.redhat.openshift.model.DomainResource;
import com.redhat.openshift.model.OpenshiftDataList;
import com.redhat.openshift.model.OpenshiftResponse;
import com.redhat.openshift.processor.OpenshiftActions;
import com.redhat.openshift.rest.RestRequest;
import com.redhat.openshift.security.AuthorizationManager;
import com.redhat.openshift.service.OpenshiftServiceHelper;

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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Logout");
		return true;
	}
	
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
