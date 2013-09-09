package com.openshift.android.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.openshift.android.R;
import com.openshift.android.activity.ApplicationsActivity;
import com.openshift.android.adapter.CartridgeAdapter;
import com.openshift.android.model.ApplicationResource;
import com.openshift.android.model.CartridgeResource;
import com.openshift.android.model.OpenshiftResponse;
import com.openshift.android.rest.OpenshiftRestManager;
import com.openshift.android.util.ActivityUtils;

public class ApplicationCartridgesFragment extends ListFragment implements RefreshableFragment {
	
	private CartridgeAdapter cartridgeAdapter;
	private List<CartridgeResource> cartridgeList = new ArrayList<CartridgeResource>();
	private ApplicationResource applicationResource;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.fragment_cartridge,
	        container, false);
	    
		Bundle args = getArguments();
		
		this.applicationResource = (ApplicationResource) args.getSerializable(ApplicationsActivity.APPLICATION_RESOURCE_EXTRA);

	    loadData();
	    
	    
	    return view;
	} 

    @Override
   public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
	    cartridgeAdapter = new CartridgeAdapter(getActivity(), R.layout.cartridge_row_layout, cartridgeList);
	    setListAdapter(cartridgeAdapter);
    	
    }
    
    /**
	 * Binds the Application Response Data to the List
	 * 
	 * @param response The response data to fill the adapter
	 */
	private void updateList(List<CartridgeResource> cartridgeResources) {
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
	public void refresh() {
		loadData();
		
	}
	
	public void loadData() {
		OpenshiftRestManager.getInstance().getApplicationWithCartridge(applicationResource.getDomainId(), applicationResource.getName(), new Response.Listener<OpenshiftResponse<ApplicationResource>>() {

			@Override
			public void onResponse(
					OpenshiftResponse<ApplicationResource> response) {
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
									ActivityUtils.showToast(getActivity().getApplicationContext(), "Unable to Retrieve Application");
								}
							});	
				}

				
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				ActivityUtils.showToast(getActivity().getApplicationContext(), "Unable to Retrieve Application");
			}
		});

	}

}
