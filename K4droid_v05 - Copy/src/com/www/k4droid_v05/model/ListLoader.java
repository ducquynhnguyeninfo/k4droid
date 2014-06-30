package com.www.k4droid_v05.model;

import com.www.k4droid_v05.R;
import com.www.k4droid_v05.util._Log;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.LinearLayout;

public abstract class ListLoader extends AsyncTaskLoader<Cursor> implements
		android.support.v4.content.Loader.OnLoadCompleteListener<Cursor> {

	LinearLayout linlaHeaderProgress;

	public ListLoader(Context context) {
		super(context);
		linlaHeaderProgress = (LinearLayout) ((Activity) context)
				.findViewById(R.id.linlaHeaderProgress);

	}

	public void showPB() {
		linlaHeaderProgress.setVisibility(View.VISIBLE);
		_Log.v("Loader", linlaHeaderProgress.isShown() ? "shown" : "no");
		
		
	}

	public void hidePB() {
		linlaHeaderProgress.setVisibility(View.GONE);
		_Log.v("Loader", linlaHeaderProgress.isShown() ? "shown" : "no");
	}
	private Cursor mCursor;

	/* Runs on a worker thread */
	@Override
	public abstract Cursor loadInBackground();

	/* Runs on the UI thread */
	@Override
	public void deliverResult(Cursor cursor) {
		super.deliverResult(cursor);
		if (isReset()) {
			// An async query came in while the loader is stopped
			if (cursor != null) {
				cursor.close();
			}
			return;
		}
		Cursor oldCursor = mCursor;
		mCursor = cursor;

		if (isStarted()) {
			super.deliverResult(cursor);
		}

		if (oldCursor != null && oldCursor != cursor && !oldCursor.isClosed()) {
			oldCursor.close();
		}
	}

	/**
	 * Starts an asynchronous load of the contacts list data. When the result is
	 * ready the callbacks will be called on the UI thread. If a previous load
	 * has been completed and is still valid the result may be passed to the
	 * callbacks immediately.
	 * <p/>
	 * Must be called from the UI thread
	 */
	@Override
	protected void onStartLoading() {
		super.onStartLoading();
		linlaHeaderProgress.setVisibility(View.VISIBLE);
		_Log.v("Loader", linlaHeaderProgress.isShown() ? "shown" : "no");
		if (mCursor != null) {
			deliverResult(mCursor);
		}
		if (takeContentChanged() || mCursor == null) {
			forceLoad();
		}

	}

	/**
	 * Must be called from the UI thread
	 */
	@Override
	protected void onStopLoading() {
		super.onStopLoading();
		// Attempt to cancel the current load task if possible.
		cancelLoad();
		linlaHeaderProgress.setVisibility(View.GONE);
	}

	@Override
	public void onCanceled(Cursor cursor) {
		super.onCanceled(cursor);
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
	}

	@Override
	protected void onReset() {
		super.onReset();

		// Ensure the loader is stopped
		onStopLoading();

		if (mCursor != null && !mCursor.isClosed()) {
			mCursor.close();
		}
		mCursor = null;
	}

	@Override
	public void onLoadComplete(Loader<Cursor> arg0, Cursor arg1) {
		linlaHeaderProgress.setVisibility(View.GONE);
		_Log.v("Loader", linlaHeaderProgress.isShown() ? "shown" : "no");
	}
}