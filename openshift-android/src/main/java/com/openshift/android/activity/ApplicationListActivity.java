package com.openshift.android.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.openshift.android.OpenshiftAndroidApplication;
import com.openshift.android.R;
import com.openshift.android.adapter.ApplicationAdapter;
import com.openshift.android.model.ApplicationResource;
import com.openshift.android.model.DomainResource;
import com.openshift.android.model.OpenshiftDataList;
import com.openshift.android.model.OpenshiftResponse;
import com.openshift.android.rest.OpenshiftAndroidRequest;

/**
 * Displays a list of Applications within an OpenShift Domain
 * @author Andrew Block
 *
 */
public class ApplicationListActivity extends ListActivity {
	
//	private BroadcastReceiver requestReceiver;

//	private OpenshiftServiceHelper mOpenshiftServiceHelper;
	
	private DomainResource domainResource;
	
	private ApplicationAdapter applicationAdapter;
	private List<ApplicationResource> applicationList = new ArrayList<ApplicationResource>();
	private ProgressDialog progressDialog;
	
	public static final String APPLICATION_RESOURCE_EXTRA = "com.openshift.android.APPLICATION_MESSAGE";


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
	    
	    Type type = new TypeToken<OpenshiftResponse<OpenshiftDataList<ApplicationResource>>>() {}.getType();
	    
		OpenshiftAndroidRequest<OpenshiftResponse<OpenshiftDataList<ApplicationResource>>> applicationListRequest = new OpenshiftAndroidRequest<OpenshiftResponse<OpenshiftDataList<ApplicationResource>>>(Method.GET,
	    		OpenshiftAndroidApplication.getInstance().getAuthorizationManger().getOpenshiftUrl()+"domains/"+domainResource.getName()+"/applications?nolinks=true", type, null,null,
	    		new Response.Listener<OpenshiftResponse<OpenshiftDataList<ApplicationResource>>>() {

					@Override
					public void onResponse(
							OpenshiftResponse<OpenshiftDataList<ApplicationResource>> response) {
						displayList(response);	

						
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						showToast("Cannot get Application List: "+error.getMessage());
					}
				});
	    
	    OpenshiftAndroidApplication.getInstance().getRequestQueue().add(applicationListRequest);



	}
	
	@Override
	public void onPause() {
//		if(requestReceiver != null) {
//			this.unregisterReceiver(requestReceiver);
//		}
		
		super.onPause();
	}
	
	@Override
	public void onResume() {
		
		super.onResume();
		registerForContextMenu(getListView());
//		IntentFilter filter = new IntentFilter();
//		filter.addAction(OpenshiftActions.LIST_APPLICATIONS);
//		filter.addAction(OpenshiftActions.STOP_APPLICATION);
//		filter.addAction(OpenshiftActions.START_APPLICATION);
//		filter.addAction(OpenshiftActions.RESTART_APPLICATION);
//		filter.addAction(OpenshiftActions.DELETE_APPLICATION);
//		
//		requestReceiver = new BroadcastReceiver() {
//
//			@Override
//			public void onReceive(Context context, Intent intent) {
//				 
//				if(OpenshiftActions.LIST_APPLICATIONS.equals(intent.getAction()))
//				{
//				
//					RestRequest<OpenshiftResponse<OpenshiftDataList<ApplicationResource>>> applicationRequest = (RestRequest<OpenshiftResponse<OpenshiftDataList<ApplicationResource>>>) intent.getSerializableExtra(OpenshiftServiceHelper.EXTRA_RESULT_DATA);
//					OpenshiftResponse<OpenshiftDataList<ApplicationResource>> response = (OpenshiftResponse<OpenshiftDataList<ApplicationResource>>) applicationRequest.getResponse();
//					
//					if(applicationRequest.getStatus()==200){	
//						displayList(response);	
//	
//					}
//					else {
//						showToast("Failed to Retrieve Application Information: "+applicationRequest.getMessage());
//					}
//				}
//				else {
//					RestRequest<OpenshiftResponse<ApplicationResource>> applicationRequest = (RestRequest<OpenshiftResponse<ApplicationResource>>) intent.getSerializableExtra(OpenshiftServiceHelper.EXTRA_RESULT_DATA);
//
//					if(OpenshiftActions.STOP_APPLICATION.equals(intent.getAction())){
//					  applicationActionResponse(applicationRequest, "Application Successfully Stopped", "Stopped");
//					}
//					else if (OpenshiftActions.START_APPLICATION.equals(intent.getAction())){
//						  applicationActionResponse(applicationRequest, "Application Successfully Started", "Started");
//					}
//					else if (OpenshiftActions.RESTART_APPLICATION.equals(intent.getAction())){
//						  applicationActionResponse(applicationRequest, "Application Restarted", "Restarted");
//					}					
//					else if (OpenshiftActions.DELETE_APPLICATION.equals(intent.getAction())){
//						  applicationActionResponse(applicationRequest, "Application Deleted", "Deleted");
//					}
//					
//				
//				}
//			}
//			
//		};
//		
//		mOpenshiftServiceHelper = OpenshiftServiceHelper.getInstance(this);
//		this.registerReceiver(requestReceiver, filter);
//		
//		
//		OpenshiftResponse<OpenshiftDataList<ApplicationResource>> applicationList = (OpenshiftResponse<OpenshiftDataList<ApplicationResource>>) mOpenshiftServiceHelper.listApplications(domainResource.getName());
//		
//		if(applicationList != null) {
//			displayList(applicationList);
//		}

	}
	
	@Override
	protected void onListItemClick (ListView l, View v, int position, long id) {
		
		ApplicationResource applicationResource = (ApplicationResource) l.getAdapter().getItem(position);
		Intent intent = new Intent(this, ApplicationViewActivity.class);
		intent.putExtra(ApplicationListActivity.APPLICATION_RESOURCE_EXTRA, applicationResource);
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
	 /**
	  * Displays a list of available options when a long click action is performed on the List
	  */	
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
	
	 /**
	  * Performs an action initiated by a long click action
	  */
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

				executeApplicationEvent(appResource.getName(), "stop", "Stopped");				
				
//				mOpenshiftServiceHelper.stopApplication(appResource.getDomainId(), appResource.getName());
			}
			else if(item.getTitle().equals("Start Application")) {
				progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
				progressDialog.setTitle(appResource.getName());
				progressDialog.setMessage("Starting Application");
				progressDialog.show();

				executeApplicationEvent(appResource.getName(), "start", "Started");
				
				
//				mOpenshiftServiceHelper.startApplication(appResource.getDomainId(), appResource.getName());
			}
			else if(item.getTitle().equals("Restart Application")) {
				progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
				progressDialog.setTitle(appResource.getName());
				progressDialog.setMessage("Restarting Application");
				progressDialog.show();
				
				executeApplicationEvent(appResource.getName(), "restart", "Restarted");
				
//				mOpenshiftServiceHelper.restartApplication(appResource.getDomainId(), appResource.getName());
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

						//TODO: Delete Application
						
//						mOpenshiftServiceHelper.deleteApplication(appResource.getDomainId(), appResource.getName());
						
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
	 
	 /**
	 * @param restRequest the rest request
	 * @param successMessage the message to display
	 * @param action the action which was performed
	 */
	private void applicationActionResponse(boolean success, String successMessage, String action) {
			
			if(success){	
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
	
	
	
	/**
	 * Binds the Application Response Data to the List
	 * 
	 * @param response The response data to fill the adapter
	 */
	private void displayList(OpenshiftResponse<OpenshiftDataList<ApplicationResource>> response) {
		applicationAdapter.clear();
		Log.v(ApplicationListActivity.class.getPackage().getName(),"Retrieved Application Size Size: "+applicationList.size());
		
		
		for(int i = 0; i<response.getData().getList().size();i++){ 
			applicationAdapter.insert(response.getData().getList().get(i), i);
		}
		
		applicationAdapter.setNotifyOnChange(true);					
		applicationAdapter.notifyDataSetChanged();
	}
	
	private void executeApplicationEvent(String applicationName, String paramName, final String responseMessage) {
		Map<String,String> params = new HashMap<String,String>();
		params.put("event", paramName);
		
		Type type = new TypeToken<OpenshiftResponse<ApplicationResource>>() {}.getType();
		OpenshiftAndroidRequest<OpenshiftResponse<ApplicationResource>> applicationEventRequest = new OpenshiftAndroidRequest<OpenshiftResponse<ApplicationResource>>(Method.POST,
	    		OpenshiftAndroidApplication.getInstance().getAuthorizationManger().getOpenshiftUrl()+"domains/"+domainResource.getName()+"/applications/"+applicationName+"/events", type, null,params,
	    		new Response.Listener<OpenshiftResponse<ApplicationResource>>() {

					@Override
					public void onResponse(
							OpenshiftResponse<ApplicationResource> response) {
						applicationActionResponse(true, "Application Successfully "+responseMessage, responseMessage);

						
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						applicationActionResponse(false, null, responseMessage);
					}
				});
		
		applicationEventRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000, 
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	    
	    OpenshiftAndroidApplication.getInstance().getRequestQueue().add(applicationEventRequest);

	}
	
	


}
