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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.redhat.openshift.R;
import com.redhat.openshift.model.DomainResource;
import com.redhat.openshift.model.OpenshiftDataList;
import com.redhat.openshift.processor.OpenshiftActions;
import com.redhat.openshift.rest.Response;
import com.redhat.openshift.service.OpenshiftServiceHelper;

public class DomainActivity extends ListActivity {
	
	private BroadcastReceiver requestReceiver;

	private OpenshiftServiceHelper mOpenshiftServiceHelper;

	
	private ArrayAdapter<DomainResource> domainAdapter;
	private List<DomainResource> domainList = new ArrayList<DomainResource>();
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_domain);
	    	    
	    domainAdapter = new ArrayAdapter<DomainResource>(this, R.layout.simple_textview_layout, android.R.id.text1, domainList);
	    setListAdapter(domainAdapter);
	    
		IntentFilter filter = new IntentFilter(OpenshiftActions.LIST_DOMAINS);
		requestReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				int resultCode = intent.getIntExtra(OpenshiftServiceHelper.EXTRA_RESULT_CODE, 0);
				
				Response<OpenshiftDataList<DomainResource>> domains = (Response<OpenshiftDataList<DomainResource>>) intent.getSerializableExtra(OpenshiftServiceHelper.EXTRA_RESULT_DATA);
				
				if(resultCode==200){	
					domainAdapter.clear();
					Log.v(DomainActivity.class.getPackage().getName(),"Retrieved Domains Size: "+domainList.size());
					
					for(int i = 0; i<domains.getResource().getList().size();i++){ 
						domainAdapter.insert(domains.getResource().getList().get(i), i);
					}
					
					domainAdapter.setNotifyOnChange(true);					
					domainAdapter.notifyDataSetChanged();


				}
				else {
					showToast("Failed to Retrieve Domain Information: "+domains.getMessage());
				}
				
				
			}
			
		};
		
		mOpenshiftServiceHelper = OpenshiftServiceHelper.getInstance(this);
		this.registerReceiver(requestReceiver, filter);
		
		mOpenshiftServiceHelper.listDomains();
		

	}
	
	private void showToast(String message) {
		if(!isFinishing()){
			Toast toast = Toast.makeText(this,message, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();

		}

	}	


}
