package com.www.k4droid_v05.model;

import java.util.List;

import com.www.k4droid_v05.R;
import com.www.k4droid_v05.obj.ObjSong;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.LinearLayout;
/**
 * Abstract class. That provides the skeleton Loader AsyncTask.
 * @author DUC QUYNH
 *
 */
public abstract class Loader extends AsyncTask<String, Cursor, List<ObjSong>> {
	/**
	 * Processes all works under background. 
	 * @param params a list of input String.
	 * @return a List of ObjSong.
	 */
	public abstract List<ObjSong> onDoInBackgound(String... params);
}
