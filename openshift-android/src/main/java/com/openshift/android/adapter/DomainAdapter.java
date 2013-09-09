package com.openshift.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.openshift.android.R;
import com.openshift.android.model.DomainResource;

/**
 * Adapter which provides a menu of Openshift Domains for a User
 * 
 * @author Andrew Block
 * 
 * @see ArrayAdapter
 * @see DomainResource
 *
 */
public class DomainAdapter extends ArrayAdapter<DomainResource> {

	private List<DomainResource> objects;
	
	public DomainAdapter(Context context, int textViewResourceId,
			List<DomainResource> objects) {
		super(context, textViewResourceId, objects);
		this.objects = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		
		View vi = convertView;
		
		if(vi == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			vi = inflater.inflate(R.layout.domain_row_layout, parent, false);
		}
		
		DomainResource domainResource = objects.get(position);
		
		TextView domainName = (TextView) vi.findViewById(R.id.domainName);
		domainName.setText(domainResource.getName());
		
		TextView domainSuffix = (TextView) vi.findViewById(R.id.domainSuffix);
		domainSuffix.setText(domainResource.getSuffix());
		
		return vi;
		
	}

}
