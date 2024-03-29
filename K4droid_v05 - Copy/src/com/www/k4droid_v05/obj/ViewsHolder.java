package com.www.k4droid_v05.obj;

import com.www.k4droid_v05.R;

import android.app.Dialog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class ViewsHolder {
	private int id;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public CheckBox isFavorite = null;
	public TextView textViewId = null;
	public TextView textViewName = null;
	public TextView textViewAuthor = null;
	public TextView textViewLyric = null;
	
	
	public ViewsHolder(View singleRow) {
		this.textViewId = (TextView)singleRow.findViewById(R.id.songid);
		this.textViewName = (TextView)singleRow.findViewById(R.id.songname);
		this.textViewAuthor = (TextView)singleRow.findViewById(R.id.songauthor);
		this.textViewLyric = (TextView)singleRow.findViewById(R.id.songlyric);
		this.isFavorite = (CheckBox)singleRow.findViewById(R.id.isfavorite);
		isFavorite.setClickable(true);
		
	}

	public ViewsHolder(Dialog dialog) {
		this.textViewId = (TextView)dialog.findViewById(R.id.details_songid);
		this.textViewName = (TextView)dialog.findViewById(R.id.details_songname);
		this.textViewAuthor = (TextView)dialog.findViewById(R.id.details_songauthor);
		this.textViewLyric = (TextView)dialog.findViewById(R.id.details_songlyric);
		this.isFavorite = (CheckBox)dialog.findViewById(R.id.details_isfavorite);
		isFavorite.setClickable(true);
	}
}
