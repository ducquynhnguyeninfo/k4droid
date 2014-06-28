package com.www.k4droid_v05.model;

import java.util.List;

import com.www.k4droid_v05.MainActivity;
import com.www.k4droid_v05.R;
import com.www.k4droid_v05.R.layout;
import com.www.k4droid_v05.R.menu;
import com.www.k4droid_v05.adapters.FavoriteListAdapter;
import com.www.k4droid_v05.adapters.SearchingListAdapter;
import com.www.k4droid_v05.obj.ObjSong;
import com.www.k4droid_v05.ui.IndexableListView;
import com.www.k4droid_v05.util._Log;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ContentFragment extends Fragment {

	public static final String TAG = "Content Fragment";
	public IndexableListView listView = null;
	private SearchingListAdapter newAdapter;
	private FavoriteListAdapter favoriteAdapter;
	private Activity activity;
	private String fragmentName = "";

	private static ContentFragment sF;

	public ContentFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		activity = (MainActivity) getActivity();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
	}

	/**
	 * Gets data source and pours into the List<ObjSong> then makes a new
	 * instance of {@link SearchingListAdapter}. Updates the data-set for the
	 * ListView by that instance.
	 * 
	 * @return itself instance of {@link ContentFragment} class to the UI with
	 *         current state.
	 */
	public ContentFragment refresh(int tab) {
		int itemResource;
		if (MainActivity.searchByIndexer == 3)
			itemResource = R.layout.single_row_item_id_name_lyric;
		else
			itemResource = R.layout.single_row_item_id_name_author;
		List<ObjSong> listFound = ((MainActivity) activity).getData();
		switch (tab) {
		case 0:
			_Log.v("SIZE", this.getFragmentName() + " -- "
					+ (listFound == null ? listFound.size() : " null")
					+ " items");
			newAdapter = new SearchingListAdapter(getActivity(), itemResource,
					listFound, listView);
			listView.setAdapter(newAdapter);
			_Log.v(TAG, listView.getCount() + "--" + listView.getChildCount());
			break;
		case 1:
			;
			break;
		case 2:
			_Log.v("SIZE", this.getFragmentName() + " -- " + listFound.size()
					+ " items");
			favoriteAdapter = new FavoriteListAdapter(getActivity(),
					itemResource, listFound, listView);
			listView.setAdapter(favoriteAdapter);
			_Log.v(TAG, listView.getCount() + "--" + listView.getChildCount());
			break;
		}

		return this;
	}

	/**
	 * Sets the name for indicated instance of {@link ContentFragment}.
	 * 
	 * @param name
	 */
	public void setFragmentName(String name) {
		fragmentName = name;
	}

	/**
	 * Gets the name of an instance of {@link ContentFragment}.
	 * 
	 * @return Name of this instance.
	 */
	public String getFragmentName() {
		return fragmentName;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		_Log.v(fragmentName, "on Create View ---------------------------------");
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rootView = inflater.inflate(R.layout.f_timkiem, container, false);
		listView = (IndexableListView) rootView.findViewById(android.R.id.list);
		listView.setFastScrollEnabled(true);
		listView.setClickable(true);
		listView.setEmptyView(rootView.findViewById(android.R.id.empty));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				_Log.v(TAG, "Item clicked at " + position);
				Toast.makeText(getActivity(), "Stop clicking me!!!",
						Toast.LENGTH_SHORT).show();
			}
		});
		return rootView;
	}

	/**
	 * Makes a new instance of itself class {@link ContentFragment}.
	 * 
	 * @param position
	 *            :The position of Fragment on the PageViewer.
	 * @param name
	 *            :The name indicate the function of each instance.
	 * @return the new instance of {@link ContentFragment} with position and
	 *         name set.
	 */
	public static Fragment newInstance(int position, String name) {
		sF = new ContentFragment();
		sF.setFragmentName(name);
		return sF;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public IndexableListView getLvWrapper() {
		return listView;
	}

	@Override
	public View getView() {

		return super.getView();
	}

	public void setLvWrapper(IndexableListView listView) {
		this.listView = listView;

	}

	@Override
	public void onAttach(Activity activity) {
		_Log.v(fragmentName, "onAttach");
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		_Log.v(fragmentName, "onDetach");
		super.onDetach();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		_Log.v(fragmentName, "onHiddenChanged");
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onPause() {
		_Log.v(fragmentName, "onPause");
		super.onPause();
	}

	@Override
	public void onResume() {
		_Log.v(fragmentName, "onResume");
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		_Log.v(fragmentName, "onSaveInstanceState");
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		_Log.v(fragmentName, "onViewStateRestored");
		super.onViewStateRestored(savedInstanceState);
	}

	@Override
	public void setInitialSavedState(SavedState state) {
		_Log.v(fragmentName, "setInitialSavedState");
		super.setInitialSavedState(state);
	}

}
