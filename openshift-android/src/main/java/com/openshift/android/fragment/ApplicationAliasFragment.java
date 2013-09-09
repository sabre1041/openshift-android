package com.openshift.android.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.openshift.android.R;
import com.openshift.android.activity.AliasActivity;
import com.openshift.android.activity.ApplicationsActivity;
import com.openshift.android.adapter.ApplicationAliasesAdapter;
import com.openshift.android.model.ApplicationAliasResource;
import com.openshift.android.model.ApplicationResource;
import com.openshift.android.model.OpenshiftDataList;
import com.openshift.android.model.OpenshiftResponse;
import com.openshift.android.rest.OpenshiftRestManager;
import com.openshift.android.util.ActivityUtils;

public class ApplicationAliasFragment extends ListFragment {
	
	private ApplicationResource applicationResource;
	private ApplicationAliasesAdapter aliasesAdapter;
	private List<ApplicationAliasResource> applicationAliases = new ArrayList<ApplicationAliasResource>();
	
	
    @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setHasOptionsMenu(true);
   }
	
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.fragment_alias,
	        container, false);
	    
		Bundle args = getArguments();
		
		this.applicationResource = (ApplicationResource) args.getSerializable(ApplicationsActivity.APPLICATION_RESOURCE_EXTRA);

		loadData();
	    
	    return view;
	  }
	  
	    @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	     	super.onActivityCreated(savedInstanceState);
	     	
	     	aliasesAdapter = new ApplicationAliasesAdapter(getActivity(), R.layout.aliases_row_layout, applicationAliases);
	 	    setListAdapter(aliasesAdapter);

	     	
	     }
	    
	    @Override
	    public void onResume() {
	     	super.onResume();
	     	
	     	loadData();
	     }

	  
		public void loadData() {
			OpenshiftRestManager.getInstance().getApplicationAliases(applicationResource.getDomainId(), applicationResource.getName(),
		    		new Response.Listener<OpenshiftResponse<OpenshiftDataList<ApplicationAliasResource>>>() {

						@Override
						public void onResponse(
								OpenshiftResponse<OpenshiftDataList<ApplicationAliasResource>> response) {

							updateList(response.getData().getList());
							
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							ActivityUtils.showToast(getActivity().getApplicationContext(), "Unable to get Application Aliases");
						}
					});

		}
		
	    /**
		 * Binds the Application Response Data to the List
		 * 
		 * @param response The response data to fill the adapter
		 */
		private void updateList(List<ApplicationAliasResource> applicationAliasesResources) {
			aliasesAdapter.clear();
			
			
			for(int i = 0; i<applicationAliasesResources.size();i++){
				ApplicationAliasResource alias = applicationAliasesResources.get(i);
				alias.setApplicationResource(applicationResource);
				
				aliasesAdapter.insert(alias, i);
			}
			
			aliasesAdapter.setNotifyOnChange(true);					
			aliasesAdapter.notifyDataSetChanged();
		}
		
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		    super.onCreateOptionsMenu(menu, inflater);
		    inflater.inflate(R.menu.alias_fragment_menu, menu);		
		}
		
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) 	    {
	    	switch(item.getItemId()) {
	    	case R.id.menu_create_alias:
		    	Log.v(ApplicationAliasFragment.class.getPackage().getName(),"Woot Within Menu!");
	    		ApplicationAliasResource alias = new ApplicationAliasResource();
	    		alias.setApplicationResource(applicationResource);
	    		
	    		// Start the Activity
				Intent intent = new Intent(getActivity(), AliasActivity.class);
				intent.putExtra(AliasActivity.APPLICATION_ALIAS_RESOURCE, alias);
				getActivity().startActivity(intent);
	    		return true;
	    	}
	    	
	        // Be sure to return false or super's so it will proceed to the next fragment!
	        return super.onOptionsItemSelected(item);
	    }





}
