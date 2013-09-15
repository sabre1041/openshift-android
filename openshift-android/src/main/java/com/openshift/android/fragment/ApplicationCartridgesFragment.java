package com.openshift.android.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.openshift.android.OpenshiftAndroidApplication;
import com.openshift.android.OpenshiftConstants;
import com.openshift.android.R;
import com.openshift.android.activity.ApplicationsActivity;
import com.openshift.android.activity.CartridgeActivity;
import com.openshift.android.adapter.CartridgeAdapter;
import com.openshift.android.model.ApplicationResource;
import com.openshift.android.model.CartridgeResource;
import com.openshift.android.model.EventType;
import com.openshift.android.model.OpenshiftResponse;
import com.openshift.android.rest.OpenshiftRestManager;
import com.openshift.android.util.ActivityUtils;



/**
 * Fragment for Openshift Cartridges
 *
 * @author Andrew Block
 * @author Joey Yore
 * @version 1.0
 */
public class ApplicationCartridgesFragment extends ListFragment implements RefreshableFragment  {
	
	private CartridgeAdapter cartridgeAdapter;
	private List<CartridgeResource> cartridgeList = new ArrayList<CartridgeResource>();
	private ApplicationResource applicationResource;
	private ProgressDialog progressDialog;
	private LinearLayout progressLayout;

	
    @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setHasOptionsMenu(true);
   }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.fragment_cartridge,
	        container, false);
	    
		Bundle args = getArguments();
		
		this.applicationResource = (ApplicationResource) args.getSerializable(ApplicationsActivity.APPLICATION_RESOURCE_EXTRA);
		    
	    progressLayout = (LinearLayout) view.findViewById(R.id.cartridgeLinearLayout);	    
		
	    return view;
	} 

    @Override
   public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
	    cartridgeAdapter = new CartridgeAdapter(getActivity(), R.layout.cartridge_row_layout, cartridgeList);
	    setListAdapter(cartridgeAdapter);
	    
	    registerForContextMenu(getListView());
    	
    }
    
    @Override
    public void onResume() {
     	super.onResume();
     	
     	loadData();
     }
    
    
    /**
	 * Binds the Application Response Data to the List
	 * 
	 * @param response The response data to fill the adapter
	 */
	private void updateList(List<CartridgeResource> cartridgeResources) {
		applicationResource.setCartridges(cartridgeResources);
		cartridgeAdapter.clear();
		
		
		for(int i = 0; i<cartridgeResources.size();i++){
			cartridgeAdapter.insert(cartridgeResources.get(i), i);
		}
		
		cartridgeAdapter.setNotifyOnChange(true);					
		cartridgeAdapter.notifyDataSetChanged();
	}
	
	private synchronized void updateList(CartridgeResource cartridgeResource) {
		for(int i = 0; i<cartridgeList.size();i++){
			if(cartridgeList.get(i).getName().equals(cartridgeResource.getName())) {
				cartridgeAdapter.remove(cartridgeList.get(i));
				cartridgeAdapter.insert(cartridgeResource, i);
				cartridgeAdapter.setNotifyOnChange(true);					
				cartridgeAdapter.notifyDataSetChanged();
				return;
			}
		}
	}

	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		OpenshiftAndroidApplication.getInstance().getRequestQueue().cancelAll(OpenshiftConstants.APPLICATIONCARTRIDGESFRAGMENT_TAG);

	}
	
	public void loadData() {
		if(cartridgeList.size() == 0) {
			progressLayout.setVisibility(View.VISIBLE);
		}
		
		OpenshiftRestManager.getInstance().getApplicationWithCartridge(applicationResource.getDomainId(), applicationResource.getName(), new Response.Listener<OpenshiftResponse<ApplicationResource>>() {

			@Override
			public void onResponse(
					OpenshiftResponse<ApplicationResource> response) {
				
				progressLayout.setVisibility(View.GONE);
				
				updateList(response.getData().getCartridges());
										
				for(CartridgeResource cartridge : response.getData().getCartridges()) {

					OpenshiftRestManager.getInstance().getCartridge(applicationResource.getDomainId(), applicationResource.getName(), cartridge.getName(), new Response.Listener<OpenshiftResponse<CartridgeResource>>() {

								@Override
								public void onResponse(
										OpenshiftResponse<CartridgeResource> response) {
									updateList(response.getData());
									
								}
							}, new Response.ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError error) {
									Log.v(OpenshiftConstants.APPLICATIONCARTRIDGESFRAGMENT_TAG, "Unable to Retrieve Cartridge: "+error.getMessage());
								}
							}, OpenshiftConstants.APPLICATIONCARTRIDGESFRAGMENT_TAG);	
				}

				
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				
				progressLayout.setVisibility(View.GONE);
				
				ActivityUtils.showToast(getActivity().getApplicationContext(), "Unable to Retrieve Cartridge");
			}
		}, OpenshiftConstants.APPLICATIONCARTRIDGESFRAGMENT_TAG);

	}
	
	 /**
	  * Displays a list of available options when a long click action is performed on the List
	  */	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	    ContextMenuInfo menuInfo) {
		
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.cartridge_fragment_context_menu, menu);
		
		super.onCreateContextMenu(menu, v, menuInfo);
		
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		
		CartridgeResource cartridgeResource = cartridgeAdapter.getItem(info.position);
		menu.setHeaderTitle(cartridgeResource.getName());
	}
	
	 /**
	  * Performs an action initiated by a long click action
	  */
	 @Override 
    public boolean onContextItemSelected(MenuItem item)
    {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
			final CartridgeResource cartridgeResource = cartridgeAdapter.getItem(info.position);

			switch(item.getItemId()) {
			case R.id.menu_cartridge_start:
				progressDialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
				progressDialog.setTitle(cartridgeResource.getName());
				progressDialog.setMessage("Starting Cartridge");
				progressDialog.show();

				executeCartridgeEvent(cartridgeResource.getName(), EventType.START, "Started");
				
				break;
			case R.id.menu_cartridge_stop:
				progressDialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
				progressDialog.setTitle(cartridgeResource.getName());
				progressDialog.setMessage("Stopping Cartridge");
				progressDialog.show();

				executeCartridgeEvent(cartridgeResource.getName(), EventType.STOP, "Stopped");				
				
				break;
			case R.id.menu_cartridge_restart:
				progressDialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
				progressDialog.setTitle(cartridgeResource.getName());
				progressDialog.setMessage("Restarting Cartridge");
				progressDialog.show();
				
				executeCartridgeEvent(cartridgeResource.getName(), EventType.RESTART, "Restarted");
				
				break;
			case R.id.menu_cartridge_delete:
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Delete Cartridge?");
				builder.setMessage("Are you sure you want to delete "+cartridgeResource.getName()+"?");
				
				
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						progressDialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
						progressDialog.setTitle(cartridgeResource.getName());
						progressDialog.setMessage("Deleting Application");
						progressDialog.show();
						
						executeCartridgeEvent(cartridgeResource.getName(), EventType.DELETE, "Deleted");
					}
				});
				
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				
				builder.create().show();
				
				break;
			case R.id.menu_cartridge_reload:
				progressDialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
				progressDialog.setTitle(cartridgeResource.getName());
				progressDialog.setMessage("Reloading Cartridge");
				progressDialog.show();
				
				executeCartridgeEvent(cartridgeResource.getName(), EventType.RELOAD, "Reloaded");
				
				break;
			}
						

			
			
		 return super.onContextItemSelected(item);
    }
	 
	 
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		    super.onCreateOptionsMenu(menu, inflater);
		    inflater.inflate(R.menu.cartridge_fragment_menu, menu);		
		}
		
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) 	    {
	    	switch(item.getItemId()) {
	    	case R.id.menu_add_cartridge:
    		
	    		// Start the Activity
				Intent intent = new Intent(getActivity(), CartridgeActivity.class);
				intent.putExtra(CartridgeActivity.APPLICATION_RESOURCE, applicationResource);
				getActivity().startActivity(intent);
	    		return true;
	    	}
	    	
	        // Be sure to return false or super's so it will proceed to the next fragment!
	        return super.onOptionsItemSelected(item);
	    }
	 
	 
	
		private void executeCartridgeEvent(final String cartridgeName, EventType eventType, final String responseMessage) {
			
			OpenshiftRestManager.getInstance().cartridgeEvent(applicationResource.getDomainId(), applicationResource.getName(), cartridgeName, eventType,
					new Response.Listener<OpenshiftResponse<ApplicationResource>>() {

						@Override
						public void onResponse(
								OpenshiftResponse<ApplicationResource> response) {
							cartridgeActionResponse(true, cartridgeName + " Successfully "+responseMessage, responseMessage);
							
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							cartridgeActionResponse(false, null, responseMessage);
						}
					}, OpenshiftConstants.APPLICATIONCARTRIDGESFRAGMENT_TAG);

		}


		 /**
		 * @param restRequest the rest request
		 * @param successMessage the message to display
		 * @param action the action which was performed
		 */
		private void cartridgeActionResponse(boolean success, String successMessage, String action) {
				
				if(success){	
					if(progressDialog != null && progressDialog.isShowing()) {
						progressDialog.dismiss();
					}
					new AlertDialog.Builder(getActivity()).setTitle("Cartridge " +action).setMessage(successMessage).setNeutralButton("Close", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							loadData();
						}
					}).show();
					
				}
				else {
					if(progressDialog != null && progressDialog.isShowing()) {
						progressDialog.dismiss();
					}
					new AlertDialog.Builder(getActivity()).setTitle("Openshift Failure").setMessage("Cartridge Failed to be "+action).setNeutralButton("Close", null).show();
				}					

		 }
	
	
	@Override
	public void refresh() {
		loadData();
	}

}
