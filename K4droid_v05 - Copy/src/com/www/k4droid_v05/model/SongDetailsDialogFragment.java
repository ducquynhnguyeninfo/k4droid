package com.www.k4droid_v05.model;

import com.www.k4droid_v05.R;
import com.www.k4droid_v05.obj.ObjSong;
import com.www.k4droid_v05.obj.ViewsHolder;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

/**
 * This class can show detail properties of a single song. We use this to
 * display a song's metadata.
 * 
 * @author DUC QUYNH
 * 
 */
public class SongDetailsDialogFragment extends DialogFragment {

	public interface OnCheckboxCallBackListener {
		void onCheckboxCallBackListener(View v);
	}

	public interface OnReceivedDataListener {
		void onReceivedDataListener();
	}

	OnCheckboxCallBackListener checkboxCallBackListener;
	private OnReceivedDataListener receivedDataListener;
	private Bundle bundle;
	private boolean checkboxStates[];

	public SongDetailsDialogFragment() {
		checkboxStates = null;
	}

	ViewsHolder holder;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		final Dialog dialog = new Dialog(getActivity());
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.song_details);
		holder = new ViewsHolder(dialog);
		// this.receivedDataListener.onReceivedDataListener();
		bundle = getArguments();
		holder.textViewId.setText(this.bundle.getString(ObjSong.SONG_ID));
		holder.textViewName.setText(this.bundle.getString(ObjSong.SONG_NAME));
		holder.textViewAuthor.setText(this.bundle
				.getString(ObjSong.SONG_AUTHOR));
		holder.textViewLyric.setText(this.bundle.getString(ObjSong.SONG_LYRIC));
		if (this.bundle.getInt(ObjSong.SONG_IS_FAVORITE) == 1)
			holder.isFavorite.setChecked(true);
		else
			holder.isFavorite.setChecked(false);
		holder.isFavorite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkboxCallBackListener
						.onCheckboxCallBackListener(holder.isFavorite);
			}
		});
		return dialog;
	}

	public void setOnReceivedDataListener(
			OnReceivedDataListener receivedDataListener) {
		this.receivedDataListener = receivedDataListener;
	}

	public void setOnCheckboxCallBackListener(
			OnCheckboxCallBackListener checkboxCallBackListener) {
		this.checkboxCallBackListener = checkboxCallBackListener;
	}

	public void unpackData() {
		bundle = getArguments();
	}

	public void reSetCheckboxState() {
		holder.isFavorite.setChecked(true);
	}
}
