package com.openshift.android.activity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AdapterView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.openshift.android.OpenshiftAndroidApplication;
import com.openshift.android.OpenshiftConstants;
import com.openshift.android.R;
import com.openshift.android.fragment.ApplicationAliasFragment;
import com.openshift.android.fragment.ApplicationCartridgesFragment;
import com.openshift.android.fragment.ApplicationInfoFragment;
import com.openshift.android.fragment.OnApplicationUpdateListener;
import com.openshift.android.model.ApplicationResource;
import com.openshift.android.model.EventType;
import com.openshift.android.model.OpenshiftResponse;
import com.openshift.android.rest.OpenshiftRestManager;

public class ApplicationsActivity extends Activity implements OnApplicationUpdateListener {
	
	private ApplicationResource applicationResource;
	
	  private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	  
	  private ProgressDialog progressDialog;
	  
	public static final String APPLICATION_RESOURCE_EXTRA = "com.openshift.android.APPLICATION_MESSAGE";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	   
	    Intent intent = getIntent();
	    ApplicationResource application = (ApplicationResource) intent.getSerializableExtra(ApplicationsActivity.APPLICATION_RESOURCE_EXTRA);
	    this.applicationResource = application;

	    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	    
	    setContentView(R.layout.activity_applications_base);
	    
	    ActionBar actionbar = getActionBar();
	    
	    
	    Bundle bundle = new Bundle();
	    bundle.putSerializable(ApplicationsActivity.APPLICATION_RESOURCE_EXTRA, applicationResource);
	    
	    actionbar.setTitle(applicationResource.getName());
	    actionbar.setSubtitle(applicationResource.getFramework());
	    actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    actionbar.addTab(actionbar.newTab().setText(R.string.info).setTabListener(new TabListener<ApplicationInfoFragment>(this, "info", ApplicationInfoFragment.class, bundle)));
	    actionbar.addTab(actionbar.newTab().setText(R.string.cartridges).setTabListener(new TabListener<ApplicationCartridgesFragment>(this, "cartridges", ApplicationCartridgesFragment.class, bundle)));
	    actionbar.addTab(actionbar.newTab().setText(R.string.aliases).setTabListener(new TabListener<ApplicationAliasFragment>(this, "aliases", ApplicationAliasFragment.class, bundle)));
	    
	}
	
	@Override
	  public void onRestoreInstanceState(Bundle savedInstanceState) {
	    // Restore the previously serialized current tab position.
	    if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
	      getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
	    }
	  }

	  @Override
	  public void onSaveInstanceState(Bundle outState) {
	    // Serialize the current tab position.
	    outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
	        .getSelectedNavigationIndex());
	  }
	
		 /**
		  * Performs an action initiated by a long click action
		  */
		 @Override 
	     public boolean onOptionsItemSelected(MenuItem item)
	     {
				AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

				progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
				progressDialog.setTitle(applicationResource.getName());

				
				switch(item.getItemId()) {
					case R.id.menu_application_start:
						progressDialog.setMessage("Starting Application");
						progressDialog.show();

						executeApplicationEvent(applicationResource.getName(), EventType.START, "Started");						
						break;
					case R.id.menu_application_stop:
						progressDialog.setMessage("Stopping Application");
						progressDialog.show();

						executeApplicationEvent(applicationResource.getName(), EventType.STOP, "Stopped");										
						break;
					case R.id.menu_application_restart:
						progressDialog.setMessage("Restarting Application");
						progressDialog.show();
						
						executeApplicationEvent(applicationResource.getName(), EventType.RESTART, "Restarted");						
						break;
					case R.id.menu_application_delete:
						AlertDialog.Builder builder = new AlertDialog.Builder(this);
						builder.setTitle("Delete Application?");
						builder.setMessage("Are you sure you want to delete "+applicationResource.getName()+"?");
						
						
						builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								progressDialog.setMessage("Deleting Application");
								progressDialog.show();
								
								executeApplicationEvent(applicationResource.getName(), EventType.DELETE, "Deleted");
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
						
				}
				
/*							
				if(item.getTitle().equals("View Application")) {
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(applicationResource.getApplicationUrl()));
					startActivity(i);
				}
				else if(item.getTitle().equals("Stop Application")) {
					
				}
				else if(item.getTitle().equals("Start Application")) {
					
					
				}
				else if(item.getTitle().equals("Restart Application")) {
					
				}
				else if(item.getTitle().equals("Delete Application")) {
					
				
				}
	*/			
				
			 return super.onContextItemSelected(item);
	     }

			private void executeApplicationEvent(final String applicationName, EventType eventType, final String responseMessage) {
				
				OpenshiftRestManager.getInstance().applicationEvent(applicationResource.getDomainId(), applicationName, eventType,
						new Response.Listener<OpenshiftResponse<ApplicationResource>>() {

							@Override
							public void onResponse(
									OpenshiftResponse<ApplicationResource> response) {
								applicationActionResponse(true, applicationName + " Successfully "+responseMessage, responseMessage);								
								
							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								applicationActionResponse(false, null, responseMessage);
							}
						}, OpenshiftConstants.APPLICATIONSACTIVITY_TAG);

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
						AlertDialog dialog = new AlertDialog.Builder(ApplicationsActivity.this).setTitle("Application " +action).setMessage(successMessage).setNeutralButton("Close", null).create();
						
						if("Deleted".equals(action)) {
							dialog.setOnDismissListener(new OnDismissListener() {
								
								@Override
								public void onDismiss(DialogInterface dialog) {
									finish();
									
								}
							});
						}
						
						dialog.show();
						
					}
					else {
						if(progressDialog != null && progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
						new AlertDialog.Builder(ApplicationsActivity.this).setTitle("Openshift Failure").setMessage("Application Failed to be "+action).setNeutralButton("Close", null).show();
					}					

			 }


	
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.application_menu, menu);
	    return true;
	  } 
	
	public class TabListener<T extends Fragment> implements ActionBar.TabListener {
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;
        private final Bundle mArgs;
        private Fragment mFragment;

        public TabListener(Activity activity, String tag, Class<T> clz) {
            this(activity, tag, clz, null);
        }

        public TabListener(Activity activity, String tag, Class<T> clz, Bundle args) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
            mArgs = args;

            // Check to see if we already have a fragment for this tab, probably
            // from a previously saved state.  If so, deactivate it, because our
            // initial state is that a tab isn't shown.
            mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
            if (mFragment != null && !mFragment.isDetached()) {
            	
                FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.detach(mFragment);
                ft.commit();
            }
        }

        public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
            if (mFragment == null) {
                mFragment = Fragment.instantiate(mActivity, mClass.getName(), mArgs);
                ft.add(android.R.id.content, mFragment, mTag);
            } else {
                ft.attach(mFragment);
            }
        }

        public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
            if (mFragment != null) {            	
                ft.detach(mFragment);
            }
        }

        public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
//            Toast.makeText(mActivity, "Reselected!", Toast.LENGTH_SHORT).show();
        }
    }

	@Override
	public void onApplicationUpdate(ApplicationResource applicationResource) {
		this.applicationResource = applicationResource;
		
	}
	
    @Override
    public void onStop() {
    	
    	super.onStop();
    	
		OpenshiftAndroidApplication.getInstance().getRequestQueue().cancelAll(OpenshiftConstants.APPLICATIONSACTIVITY_TAG);

    }

	
	



}
