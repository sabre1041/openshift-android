package com.openshift.android.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.openshift.android.R;
import com.openshift.android.activity.ApplicationListActivity;
import com.openshift.android.activity.ApplicationViewActivity;
import com.openshift.android.model.ApplicationResource;
import com.openshift.android.model.CartridgeResource;

/**
 * Adapter which provides a menu of OpenShift Cartridge for an Application
 * 
 * @author Andrew Block
 * 
 * @see ArrayAdapter
 * @see ApplicationResource
 *
 */
public class CartridgeAdapter extends ArrayAdapter<CartridgeResource> {

	private List<CartridgeResource> objects;
	
	public CartridgeAdapter(Context context, int textViewResourceId,
			List<CartridgeResource> objects) {
		super(context, textViewResourceId, objects);
		this.objects = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		
		View vi = convertView;
		
		if(vi == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			vi = inflater.inflate(R.layout.cartridge_row_layout, parent, false);
		}
		
		vi.setClickable(false);
		
		CartridgeResource cartridge = objects.get(position);
		
		TextView cartridgeName = (TextView) vi.findViewById(R.id.cartridgeName);
		cartridgeName.setText(cartridge.getName());
		
		TextView cartridgeDisplayName = (TextView) vi.findViewById(R.id.cartridgeDisplayName);
		cartridgeDisplayName.setText(cartridge.getDisplayName());
		
		TextView status = (TextView) vi.findViewById(R.id.cartridgeStatus);
		
		if(cartridge.getStatusMessages() == null || cartridge.getStatusMessages().size() ==0) {
			status.setText("Pending...");
			status.setTextColor(Color.BLACK);
		}
		else {
			if(cartridge.getStatusMessages().get(0).getMessage().toLowerCase().contains("running")) {
				status.setText("Up");
				status.setTextColor(Color.parseColor("#339900"));
			}
			else if(cartridge.getStatusMessages().get(0).getMessage().toLowerCase().contains("stopped")) {
				status.setText("Down");	
				status.setTextColor(Color.RED);
			}
			else {
				status.setText("Unknown");
				status.setTextColor(Color.DKGRAY);

			}
			
		}
		
		return vi;
		
	}

}
