package com.www.k4droid_v05.model;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.www.k4droid_v05.R;
import com.www.k4droid_v05.obj.ObjSong;

public class DetailsFragment extends Fragment {
	DetailsHolder detailsHolder;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.item_detail_fragment, null);
		detailsHolder = new DetailsHolder(view);
		detailsHolder.setContentViews(getArguments());
		return view;
	}

	public void updateViews() {
		detailsHolder.setContentViews(getArguments().getBundle("objSong"));
	}

	private class DetailsHolder {

		TextView songID;
		TextView songName;
		TextView songAuthor;
		TextView songLyric;

		public DetailsHolder(View view) {
			songID = (TextView) view.findViewById(R.id.det_song_id);
			songAuthor = (TextView) view.findViewById(R.id.det_song_author);
			songName = (TextView) view.findViewById(R.id.det_song_name);
			songLyric = (TextView) view.findViewById(R.id.det_song_lyric);
		}

		public void setContentViews(Bundle bundle) {
			songID.setText(bundle.getString(ObjSong.SONG_ID));
			songAuthor.setText(bundle.getString(ObjSong.SONG_AUTHOR));
			songName.setText(bundle.getString(ObjSong.SONG_NAME));
			songLyric.setText(bundle.getString(ObjSong.SONG_LYRIC));
		}
	}
}
