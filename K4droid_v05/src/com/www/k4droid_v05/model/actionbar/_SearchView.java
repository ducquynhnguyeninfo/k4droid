package com.www.k4droid_v05.model.actionbar;

import java.lang.reflect.Field;

import com.www.k4droid_v05.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

public class _SearchView extends SearchView {

	public _SearchView(Context context) {
		super(context);
		setSearchIcons();
		setSearchTextColour();
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onActionViewCollapsed() {
		super.onActionViewCollapsed();
		setQuery("", false);
	}
	
	/**
	 * Set searchView text color
	 * 
	 * @param searchView
	 */
	private void setSearchTextColour() {
		int searchPlateId = this.getContext().getResources()
				.getIdentifier("android:id/search_src_text", null, null);
		EditText searchPlate = (EditText) this
				.findViewById(searchPlateId);
		searchPlate.setTextColor(getResources().getColor(
				android.R.color.holo_blue_bright));
		searchPlate.setBackgroundResource(R.drawable.background_card);
		searchPlate.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
	}

	/**
	 * Set icon search for searchview.
	 * 
	 * @param searchView
	 */
	private void setSearchIcons() {
		try {
			Field searchField = SearchView.class
					.getDeclaredField("mCloseButton");
			searchField.setAccessible(true);
			ImageView closeBtn = (ImageView) searchField.get(this);
			closeBtn.setImageResource(android.R.drawable.ic_input_delete);

			searchField = SearchView.class.getDeclaredField("mVoiceButton");
			searchField.setAccessible(true);
			ImageView voiceBtn = (ImageView) searchField.get(this);
			voiceBtn.setImageResource(android.R.drawable.ic_btn_speak_now);

			// ImageView searchButton = (ImageView) searchView
			// .findViewById(R.id.abs__search_button);
			// searchButton.setImageResource(R.drawable.ic_menu_search);

		} catch (NoSuchFieldException e) {
			Log.e("SearchView", e.getMessage(), e);
		} catch (IllegalAccessException e) {
			Log.e("SearchView", e.getMessage(), e);
		}
	}

}
