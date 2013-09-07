package com.openshift.android.activity;

import java.util.ArrayList;
import java.util.List;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.openshift.android.R;
import com.openshift.android.adapter.ApplicationAdapter;
import com.openshift.android.model.ApplicationResource;
import com.openshift.android.model.DomainResource;
import com.openshift.android.model.EventType;
import com.openshift.android.model.OpenshiftDataList;
import com.openshift.android.model.OpenshiftResponse;
import com.openshift.android.rest.OpenshiftRestManager;
import com.openshift.android.util.ActivityUtils;

/**
 * Displays a list of Applications within an OpenShift Domain
 * @author Andrew Block
 *
 */
public class ApplicationListActivity extends ListActivity {
	
	
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
	    
	    makeApplicationListRequest();
	}
	
	@Override
	public void onPause() {
	
		super.onPause();
		

	}
	
	@Override
	public void onResume() {
		
		super.onResume();
		registerForContextMenu(getListView());
		makeApplicationListRequest();

	}
	
	
	@Override
	protected void onListItemClick (ListView l, View v, int position, long id) {
		
		ApplicationResource applicationResource = (ApplicationResource) l.getAdapter().getItem(position);
		Intent intent = new Intent(this, ApplicationViewActivity.class);
		intent.putExtra(ApplicationListActivity.APPLICATION_RESOURCE_EXTRA, applicationResource);
		startActivity(intent);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Create Application");
		return true;
	}
	
	/**
	 * Performs the action from a menu selection
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(item.getTitle().equals("Create Application")) {
			Intent intent = new Intent(this, ApplicationNewActivity.class);
			intent.putExtra(DomainActivity.DOMAIN_RESOURCE_EXTRA, domainResource);
			startActivity(intent);
			return true;
		}
		
		return super.onOptionsItemSelected(item);
		
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

				executeApplicationEvent(appResource.getName(), EventType.STOP, "Stopped");				
				
			}
			else if(item.getTitle().equals("Start Application")) {
				progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
				progressDialog.setTitle(appResource.getName());
				progressDialog.setMessage("Starting Application");
				progressDialog.show();

				executeApplicationEvent(appResource.getName(), EventType.START, "Started");
				
				
			}
			else if(item.getTitle().equals("Restart Application")) {
				progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
				progressDialog.setTitle(appResource.getName());
				progressDialog.setMessage("Restarting Application");
				progressDialog.show();
				
				executeApplicationEvent(appResource.getName(), EventType.RESTART, "Restarted");
				
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
						
						executeApplicationEvent(appResource.getName(), EventType.DELETE, "Deleted");
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
	
	private void executeApplicationEvent(final String applicationName, EventType eventType, final String responseMessage) {
		
		OpenshiftRestManager.getInstance().applicationEvent(domainResource.getName(), applicationName, eventType,
				new Response.Listener<OpenshiftResponse<ApplicationResource>>() {

					@Override
					public void onResponse(
							OpenshiftResponse<ApplicationResource> response) {
						applicationActionResponse(true, applicationName + " Successfully "+responseMessage, responseMessage);
						
						// Get a fresh set of data
						makeApplicationListRequest();
						
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						applicationActionResponse(false, null, responseMessage);
					}
				});

	}
	
	private void makeApplicationListRequest() {
		OpenshiftRestManager.getInstance().listApplications(domainResource.getName(),
	    		new Response.Listener<OpenshiftResponse<OpenshiftDataList<ApplicationResource>>>() {

					@Override
					public void onResponse(
							OpenshiftResponse<OpenshiftDataList<ApplicationResource>> response) {
						displayList(response);	

						
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						ActivityUtils.showToast(getApplicationContext(), "Unable to get Application List");
					}
				});

	}
	
	


}
