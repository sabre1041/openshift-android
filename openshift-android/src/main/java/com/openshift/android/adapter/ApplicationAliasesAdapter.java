package com.openshift.android.adapter;

import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.openshift.android.R;
import com.openshift.android.activity.AliasActivity;
import com.openshift.android.model.ApplicationAliasResource;
import com.openshift.android.model.ApplicationResource;
import com.openshift.android.model.OpenshiftResponse;
import com.openshift.android.rest.OpenshiftRestManager;

/**
 * Adapter which provides a menu of Openshift Application Aliases for an Application
 * 
 * @author Andrew Block
 * 
 * @see ArrayAdapter
 * @see ApplicationResource
 *
 */
public class ApplicationAliasesAdapter extends ArrayAdapter<ApplicationAliasResource> {

	private List<ApplicationAliasResource> objects;
	private Context context;
	private ProgressDialog progressDialog;
	private String tag;
	
	public ApplicationAliasesAdapter(Context context, int textViewResourceId,
			List<ApplicationAliasResource> objects, String tag) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.objects = objects;
		this.tag = tag;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		
		View vi = convertView;
		
		if(vi == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			vi = inflater.inflate(R.layout.aliases_row_layout, parent, false);
		}
		
		final ApplicationAliasResource applicationAlias = objects.get(position);
		
		TextView aliasId = (TextView) vi.findViewById(R.id.aliasNameLabel);
		
		aliasId.setText(applicationAlias.getId());
		
		Button modifyAliasButton = (Button) vi.findViewById(R.id.editAliasButton);
		modifyAliasButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, AliasActivity.class);
				intent.putExtra(AliasActivity.APPLICATION_ALIAS_RESOURCE, applicationAlias);
				context.startActivity(intent);

				
			}
		});
		
		Button deleteAliasButton = (Button) vi.findViewById(R.id.deleteAliasButton);
		deleteAliasButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Delete Application?");
				builder.setMessage("Are you sure you want to delete "+applicationAlias.getId()+"?");
				
				
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						progressDialog = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);
						progressDialog.setTitle(applicationAlias.getId());
						progressDialog.setMessage("Deleting Alias");
						progressDialog.show();

						OpenshiftRestManager.getInstance().removeAlias(applicationAlias.getApplicationResource().getDomainId(), applicationAlias.getApplicationResource().getName(), applicationAlias.getId(), new Response.Listener<OpenshiftResponse<ApplicationAliasResource>>() {

							@Override
							public void onResponse(
									OpenshiftResponse<ApplicationAliasResource> arg0) {
								objects.remove(applicationAlias);
								notifyDataSetChanged();
								
								progressDialog.dismiss();
								new AlertDialog.Builder(context).setTitle("Success").setMessage("Alias deleted successfully").setNeutralButton("Close", null).create().show();
								
							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError arg0) {
								progressDialog.dismiss();
								new AlertDialog.Builder(context).setTitle("Failure").setMessage("Could not delete alias").setNeutralButton("Close", null).create().show();
								
							}
						}, tag);
						

					
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
		});
	
		
		return vi;
		
	}
	
	
	

}
