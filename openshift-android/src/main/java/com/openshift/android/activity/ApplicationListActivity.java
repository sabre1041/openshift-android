package com.openshift.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.openshift.android.R;
import com.openshift.android.adapter.ApplicationAdapter;
import com.openshift.android.model.ApplicationResource;
import com.openshift.android.model.DomainResource;
import com.openshift.android.model.OpenshiftDataList;
import com.openshift.android.model.OpenshiftResource;
import com.openshift.android.model.OpenshiftResponse;
import com.openshift.android.processor.OpenshiftActions;
import com.openshift.android.rest.RestRequest;
import com.openshift.android.service.OpenshiftServiceHelper;

public class ApplicationListActivity extends ListActivity {
	
	private BroadcastReceiver requestReceiver;

	private OpenshiftServiceHelper mOpenshiftServiceHelper;
	
	private DomainResource domainResource;
	
	private ApplicationAdapter applicationAdapter;
	private List<ApplicationResource> applicationList = new ArrayList<ApplicationResource>();
	private ProgressDialog progressDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_applications);
	    Intent intent = getIntent();
	    DomainResource domain = (DomainResource) intent.getSerializableExtra(DomainActivity.DOMAIN_RESOURCE_EXTRA);
	    this.domainResource = domain;
	    
	    applicationAdapter = new ApplicationAdapter(this, R.layout.application_row_layout, applicationList);
	    setListAdapter(applicationAdapter);
	    
	    registerForContextMenu(getListView());


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
		registerForContextMenu(getListView());
		IntentFilter filter = new IntentFilter();
		filter.addAction(OpenshiftActions.LIST_APPLICATIONS);
		filter.addAction(OpenshiftActions.STOP_APPLICATION);
		filter.addAction(OpenshiftActions.START_APPLICATION);
		filter.addAction(OpenshiftActions.RESTART_APPLICATION);
		filter.addAction(OpenshiftActions.DELETE_APPLICATION);
		requestReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				 
				if(OpenshiftActions.LIST_APPLICATIONS.equals(intent.getAction()))
				{
				
					RestRequest<OpenshiftResponse<OpenshiftDataList<ApplicationResource>>> applicationRequest = (RestRequest<OpenshiftResponse<OpenshiftDataList<ApplicationResource>>>) intent.getSerializableExtra(OpenshiftServiceHelper.EXTRA_RESULT_DATA);
					OpenshiftResponse<OpenshiftDataList<ApplicationResource>> response = (OpenshiftResponse<OpenshiftDataList<ApplicationResource>>) applicationRequest.getResponse();
					
					if(applicationRequest.getStatus()==200){	
						applicationAdapter.clear();
						Log.v(ApplicationListActivity.class.getPackage().getName(),"Retrieved Application Size Size: "+applicationList.size());
						
						
						for(int i = 0; i<response.getData().getList().size();i++){ 
							applicationAdapter.insert(response.getData().getList().get(i), i);
						}
						
						applicationAdapter.setNotifyOnChange(true);					
						applicationAdapter.notifyDataSetChanged();
	
	
					}
					else {
						showToast("Failed to Retrieve Application Information: "+applicationRequest.getMessage());
					}
				}
				else {
					RestRequest<OpenshiftResponse<ApplicationResource>> applicationRequest = (RestRequest<OpenshiftResponse<ApplicationResource>>) intent.getSerializableExtra(OpenshiftServiceHelper.EXTRA_RESULT_DATA);

					if(OpenshiftActions.STOP_APPLICATION.equals(intent.getAction())){
					  applicationActionResponse(applicationRequest, "Application Successfully Stopped", "Stopped");
					}
					else if (OpenshiftActions.START_APPLICATION.equals(intent.getAction())){
						  applicationActionResponse(applicationRequest, "Application Successfully Started", "Started");
					}
					else if (OpenshiftActions.RESTART_APPLICATION.equals(intent.getAction())){
						  applicationActionResponse(applicationRequest, "Application Restarted", "Restarted");
					}					
					else if (OpenshiftActions.DELETE_APPLICATION.equals(intent.getAction())){
						  applicationActionResponse(applicationRequest, "Application Deleted", "Deleted");
					}
					
				
				}
			}
			
		};
		
		mOpenshiftServiceHelper = OpenshiftServiceHelper.getInstance(this);
		this.registerReceiver(requestReceiver, filter);
		
		mOpenshiftServiceHelper.listApplications(domainResource.getId());

	}
	
	private void showToast(String message) {
		if(!isFinishing()){
			Toast toast = Toast.makeText(this,message, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();

		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	    ContextMenuInfo menuInfo) {
		
		super.onCreateContextMenu(menu, v, menuInfo);
		
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		
		ApplicationResource appResource = applicationAdapter.getItem(info.position);
		menu.setHeaderTitle(appResource.getName());
		menu.add("View Application");
		menu.add("Restart Application");
		menu.add("Stop Application");
		menu.add("Start Application");
		menu.add("Delete Application");
	}
	
	 @Override 
     public boolean onContextItemSelected(MenuItem item)
     {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
			final ApplicationResource appResource = applicationAdapter.getItem(info.position);

						
			if(item.getTitle().equals("View Application")) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(appResource.getApplicationUrl()));
				startActivity(i);
			}
			else if(item.getTitle().equals("Stop Application")) {
				progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
				progressDialog.setTitle(appResource.getName());
				progressDialog.setMessage("Stopping Application");
				progressDialog.show();
				
				mOpenshiftServiceHelper.stopApplication(appResource.getDomain(), appResource.getName());
			}
			else if(item.getTitle().equals("Start Application")) {
				progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
				progressDialog.setTitle(appResource.getName());
				progressDialog.setMessage("Starting Application");
				progressDialog.show();
				
				mOpenshiftServiceHelper.startApplication(appResource.getDomain(), appResource.getName());
			}
			else if(item.getTitle().equals("Restart Application")) {
				progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
				progressDialog.setTitle(appResource.getName());
				progressDialog.setMessage("Restarting Application");
				progressDialog.show();
				
				mOpenshiftServiceHelper.restartApplication(appResource.getDomain(), appResource.getName());
			}
			else if(item.getTitle().equals("Delete Application")) {
				
			
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Delete Application?");
				builder.setMessage("Are you sure you want to delete "+appResource.getName()+"?");
				
				
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						progressDialog = new ProgressDialog(ApplicationListActivity.this, ProgressDialog.STYLE_SPINNER);
						progressDialog.setTitle(appResource.getName());
						progressDialog.setMessage("Deleting Application");
						progressDialog.show();
						
						mOpenshiftServiceHelper.deleteApplication(appResource.getDomain(), appResource.getName());
						
					}
				});
				
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				
				builder.create().show();
			}
			
			
		 return super.onContextItemSelected(item);
     }
	 
	 private void applicationActionResponse(RestRequest<? extends OpenshiftResource> restRequest,  String successMessage, String action) {
			
			if(restRequest.getStatus()==200){	
				if(progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				new AlertDialog.Builder(ApplicationListActivity.this).setTitle("Application " +action).setMessage(successMessage).setNeutralButton("Close", null).show();
				
			}
			else {
				if(progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				new AlertDialog.Builder(ApplicationListActivity.this).setTitle("Openshift Failure").setMessage("Application Failed to be "+action).setNeutralButton("Close", null).show();
			}					

	 }
	
	


}
