package com.www.k4droid_v05.adapters;

import java.util.List;

import com.www.k4droid_v05.R;
import com.www.k4droid_v05.db.SQLQueries;
import com.www.k4droid_v05.model.SongDetailsDialogFragment;
import com.www.k4droid_v05.model.SongDetailsDialogFragment.OnCheckboxCallBackListener;
//import com.www.k4droid_v05.model.SongDetailsDialogFragment.OnReceivedDataListener;
import com.www.k4droid_v05.obj.ObjSong;
import com.www.k4droid_v05.obj.ViewsHolder;
import com.www.k4droid_v05.ui.IndexableListView;
import com.www.k4droid_v05.util._Log;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SearchingListAdapter extends BaseListAdapter {

	private static final String TAG = "FoundListAdapter";
	private static Context context;
	private int idResource;
	public static boolean[] checkBoxStateFavorite;
	private List<ObjSong> listFound;
	private IndexableListView listView;
	private SongDetailsDialogFragment detailsDialogFragment;
	private RelativeLayout itemContainer;

	/**
	 * @param context
	 *            The current context.
	 * @param resource
	 *            The resource ID for a layout file containing a TextView to use
	 *            when instantiating views.
	 */

	public SearchingListAdapter(Context context, int resource,
			List<ObjSong> songs, View view) {
		super(context, resource, songs);

		this.idResource = resource;
		this.context = context;
		listFound = songs;
		this.checkBoxStateFavorite = new boolean[listFound.size()];

		this.listView = (IndexableListView) view;
		int i = 0;
		for (ObjSong song : listFound) {
			checkBoxStateFavorite[i] = (listFound.get(i).getSongIsFavorite() == 1 ? true
					: false);
			i++;
		}
	}

	/**
	 * @param context
	 *            The current context.
	 * @param resource
	 *            The resource ID for a layout file containing a layout to use
	 *            when instantiating views.
	 * @param textViewResourceId
	 *            The id of the TextView within the layout resource to be
	 *            populated
	 */
	public SearchingListAdapter(Context context, int resource,
			int textViewResourceId) {
		super(context, resource, textViewResourceId);
		this.context = context;
	}

	/**
	 * @param context
	 *            The current context.
	 * @param resource
	 *            The resource ID for a layout file containing a layout to use
	 *            when instantiating views.
	 * @param Songs
	 *            The Songs to represent in the ListView.
	 */
	public SearchingListAdapter(Context context, int resource, ObjSong[] Songs) {
		super(context, resource, Songs);
		this.context = context;
	}

	/**
	 * getView method is called again and again when listview is scrolling up or
	 * down, to make the new views and attach to the list
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		final ObjSong song = listFound.get(position);
		ViewsHolder holder;
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(idResource, parent, false);
			holder = new ViewsHolder(row);
			row.setTag(holder);
		} else {
			holder = (ViewsHolder) row.getTag();
		}

		itemContainer = (RelativeLayout) row.findViewById(R.id.relative);
		final int cbPos = position;
		itemContainer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle(5);
				bundle.putString(ObjSong.SONG_ID, song.getSongId());
				bundle.putString(ObjSong.SONG_NAME, song.getSongName());
				bundle.putString(ObjSong.SONG_AUTHOR, song.getSongAuthor());
				bundle.putString(ObjSong.SONG_LYRIC, song.getSongLyric());
				bundle.putInt(ObjSong.SONG_IS_FAVORITE,
						song.getSongIsFavorite());
				detailsDialogFragment = new SongDetailsDialogFragment();
				detailsDialogFragment.setArguments(bundle);
				// detailsDialogFragment
				// .setOnReceivedDataListener(new OnReceivedDataListener() {
				//
				// @Override
				// public void onReceivedDataListener() {
				// detailsDialogFragment.unpackData();
				// }
				// });
				detailsDialogFragment
						.setOnCheckboxCallBackListener(new OnCheckboxCallBackListener() {

							@Override
							public void onCheckboxCallBackListener(View view) {
								view.setId(cbPos);
								_Log.e("pos at OnCheckboxCallBackListener",
										cbPos + "");
								SearchingListAdapter.this.onClick(view);
							}
						});
				detailsDialogFragment.show(
						((Activity) context).getFragmentManager(), "");
			}
		});
		holder.textViewId.setText(song.getSongId());
		holder.textViewName.setText(song.getSongName());
		if (idResource == (Integer) R.layout.single_row_item_id_name_author) {
			holder.textViewAuthor.setText(song.getSongAuthor());
		} else
			holder.textViewLyric.setText(song.getSongLyric());
		holder.isFavorite.setChecked(checkBoxStateFavorite[position]);
		holder.isFavorite.setId(position);
		_Log.d("getView ", "set checkBox id = " + position);
		holder.setId(position);
		holder.isFavorite.setOnClickListener(this);
		return row;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public ObjSong getItem(int position) {
		return listFound.get(position);
	}

	@Override
	public int getCount() {
		if (listFound != null) {
			return listFound.size();
		}
		return super.getCount();
	}

	@Override
	public void onClick(View v) {
		v = (CheckBox) v;
		final int pos = Integer.valueOf(v.getId());
		_Log.d("onClick ", "checkBox id = " + pos);
		ObjSong song = listFound.get(pos);// get the song at the exact
											// position on the list
		if (((CheckBox) v).isChecked()) {
			addNewFavorite(pos, song);
		} else {
			askForUnfavorite(pos, song);
		}

		_Log.e("pos at onclick", pos + "");
	}

	private void askForUnfavorite(final int pos, final ObjSong song) {
		new AlertDialog.Builder(getContext())
				.setTitle(
						context.getString(R.string.unFavor1)
								+ song.getSongName()
								+ context.getString(R.string.unFavor2))
				.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								SQLQueries.unFavorite(song.getSongId());
								checkBoxStateFavorite[pos] = false;
								_Log.w(TAG,
										song.getRow_Id()
												+ "  -----  "
												+ song.getSongId()
												+ " --- "
												+ song.getSongName()
												+ " is removed from favorite list at "
												+ pos);
								refreshSingleView(song, listView, pos);
							}
						})
				.setNegativeButton("NO", new DialogInterface.OnClickListener() {

					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					@Override
					public void onClick(DialogInterface dialog, int which) {
						refreshSingleView(song, listView, pos);
						if (detailsDialogFragment != null && !(detailsDialogFragment.isDetached()))
							detailsDialogFragment.reSetCheckboxState();
						dialog.dismiss();
					}
				}).setCancelable(false).create().show();
	}

	private void addNewFavorite(final int pos, final ObjSong song) {
		checkBoxStateFavorite[pos] = true;
		SQLQueries.addFavorite(Integer.valueOf(song.getSongId()));
		refreshSingleView(song, listView, pos);
		Toast.makeText(
				getContext(),
				context.getString(R.string.addFavor1) + song.getSongName()
						+ context.getString(R.string.addFavor2),
				Toast.LENGTH_SHORT).show();
		_Log.w(TAG,
				song.getRow_Id() + " - " + song.getSongName()
						+ context.getString(R.string.addFavor2));
	}

	/**
	 * Refresh the state of the views displayed (visible) on the screen.
	 */
	private void refreshSingleView(ObjSong song, IndexableListView listView,
			int pos) {
		song.setSongIsFavorite(checkBoxStateFavorite[pos] == true ? 1 : 0);
		_Log.v("dataset counting", getCount() + "");
		if (listView == null) {
			_Log.v("List view ", "list view is null");
		} else {
			int start = listView.getFirstVisiblePosition();
			for (int i = start, j = listView.getLastVisiblePosition(); i <= j; i++) {
				if (song == listFound.get(i)) {
					View view = (View) listView.getChildAt(i - start);
					_Log.d("get child at position ", i - start
							+ (view == null ? "null" : "not null"));
					if (view != null) {

						CheckBox checkBox = (CheckBox) view
								.findViewById(R.id.isfavorite);
						if (checkBox != null)
							checkBox.refreshDrawableState();
					}
					listView.getAdapter().getView(i, view, listView);
					break;
				}
			}
		}
	}
}