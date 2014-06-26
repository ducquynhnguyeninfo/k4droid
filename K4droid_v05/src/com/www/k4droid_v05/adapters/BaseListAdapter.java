package com.www.k4droid_v05.adapters;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;

import com.www.k4droid_v05.obj.ObjSong;
import com.www.k4droid_v05.util.StringMatcher;
import com.www.k4droid_v05.util._Log;

/**
 * Extended the ArrayAdapter which has default supported object type is
 * {@link ObjSong}. This class also implements {@link OnClickListener} and
 * {@link SectionIndexer}.
 * 
 * @author DUC QUYNH
 */
public class BaseListAdapter extends ArrayAdapter<ObjSong> implements
		OnClickListener, SectionIndexer {
	private String sections = "#abcdefghijklmnopquvrstwxyz"
			.toUpperCase(Locale.US);

	public BaseListAdapter(Context context, int resource, List<ObjSong> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
	}

	public BaseListAdapter(Context context, int resource, int textViewResourceId) {
		super(context, resource, textViewResourceId);
	}

	public BaseListAdapter(Context context, int resource, ObjSong[] Songs) {
		super(context, resource, Songs);
	}

	@Override
	public int getPositionForSection(int section) {
		// If there is no item for current section, previous section will be
		// selected
		for (int i = section; i >= 0; i--) {
			for (int j = 0; j < getCount(); j++) {
				if (i == 0) {
					// For numeric section
					for (int k = 0; k <= 9; k++) {
						if (StringMatcher.match(
								String.valueOf(getItem(j).getSongName().charAt(
										0)), String.valueOf(k)))
							return j;
					}
				} else {
					if (StringMatcher.match(
							String.valueOf(getItem(j).getSongName().charAt(0)),
							String.valueOf(sections.charAt(i))))
						return j;
				}
			}
		}
		return 0;
	}

	@Override
	public Object[] getSections() {
		_Log.d("ListView", "Get sections");
		String[] sectionsArr = new String[sections.length()];
		for (int i = 0; i < sections.length(); i++)
			sectionsArr[i] = "" + sections.charAt(i);

		return sectionsArr;
	}

	@Override
	public int getSectionForPosition(int arg0) {
		_Log.d("ListView", "Get section For Position");
		return 0;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
}
