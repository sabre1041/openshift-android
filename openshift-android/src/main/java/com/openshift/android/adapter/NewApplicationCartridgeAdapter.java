package com.openshift.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.openshift.android.R;
import com.openshift.android.model.ApplicationAliasResource;
import com.openshift.android.model.ApplicationResource;
import com.openshift.android.model.CartridgeResource;
import com.openshift.android.util.ImageUtils;

/**
 * Adapter which provides a menu of available Standalone Cartridges for a User
 * 
 * @author Andrew Block
 * 
 * @see ArrayAdapter
 * @see ApplicationResource
 * 
 */
public class NewApplicationCartridgeAdapter extends
		ArrayAdapter<CartridgeResource> {

	private List<CartridgeResource> objects;
	private Context context;

	public NewApplicationCartridgeAdapter(Context context,
			int textViewResourceId, List<CartridgeResource> objects) {
		super(context, textViewResourceId, objects);
		this.objects = objects;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;

		if (vi == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			vi = inflater.inflate(
					R.layout.new_application_cartridge_row_layout, parent,
					false);
		}

		CartridgeResource cartridge = objects.get(position);

		ImageView newApplicationLogo = (ImageView) vi
				.findViewById(R.id.newApplicationImageView);
		newApplicationLogo.setImageResource((ImageUtils.getImageResourceId(
				context, cartridge.getName())));

		TextView appName = (TextView) vi.findViewById(R.id.newApplicationName);
		appName.setText(cartridge.getDisplayName());

		return vi;

	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getView(position, convertView, parent);
	}

}
