package com.openshift.android.fragment;

/**
 * Interface that states that a fragment should be refreshable
 *
 * @author Andrew Block
 * @author Joey Yore
 * @version 1.0
 *
 */
public interface RefreshableFragment {

	/**
 	* Method that should be implemented to perform the refresh
 	*/
	public void refresh();
}
