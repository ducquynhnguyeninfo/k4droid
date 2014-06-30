package com.www.k4droid_v05.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.www.k4droid_v05.R;
import com.www.k4droid_v05.model.actionbar.SpinnerNavItem;
import com.www.k4droid_v05.obj.ObjSong;
/**
 * Provides dataset for the drop down spinner.
 * @author DUC QUYNH
 *
 */
public class TitleNavigationAdapter extends BaseAdapter {

	private ImageView imgIcon;
	private TextView txtTitle;
	private ArrayList<SpinnerNavItem> spinnerNavItems;
	private Context context;

	public TitleNavigationAdapter(Context context, ArrayList<SpinnerNavItem> spinnerNavItems) {
		this.spinnerNavItems = spinnerNavItems;
		this.context = context;
	}

	@Override
	public int getCount() {
		return spinnerNavItems.size();
	}

	@Override
	public SpinnerNavItem getItem(int position) {
		return spinnerNavItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(
					R.layout.list_item_title_navigation, null);
		}
		imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
		txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
		
		imgIcon.setImageResource(spinnerNavItems.get(position).getIcon());
		txtTitle.setText(spinnerNavItems.get(position).getTitle());
		
		return convertView;
	}
}
