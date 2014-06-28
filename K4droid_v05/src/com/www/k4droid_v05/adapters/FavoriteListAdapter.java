package com.www.k4droid_v05.adapters;

import java.util.List;

import com.www.k4droid_v05.R;
import com.www.k4droid_v05.db.SQLQueries;
import com.www.k4droid_v05.model.SongDetailsDialogFragment;
import com.www.k4droid_v05.model.SongDetailsDialogFragment.OnCheckboxCallBackListener;
import com.www.k4droid_v05.obj.ObjSong;
import com.www.k4droid_v05.obj.ViewsHolder;
import com.www.k4droid_v05.ui.IndexableListView;
import com.www.k4droid_v05.util._Log;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class FavoriteListAdapter extends BaseListAdapter {

	private static final String TAG = "FavoriteListAdapter";
	private static Context context;
	private int idResource;
	private List<ObjSong> listFound;
	private Animation animation;
	private IndexableListView listView;

	protected boolean isEverDetailed = false;
	private RelativeLayout itemContainer;

	private SongDetailsDialogFragment detailsDialogFragment;

	/**
	 * @param context
	 *            The current context.
	 * @param resource
	 *            The resource ID for a layout file containing a TextView to use
	 *            when instantiating views.
	 */

	public FavoriteListAdapter(final Context context, int resource,
			List<ObjSong> songs, View view) {
		super(context, resource, songs);

		this.idResource = resource;
		this.context = context;
		listFound = songs;
		this.listView = (IndexableListView) view;
		animation = AnimationUtils.loadAnimation(context, R.anim.fade_anim);

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
	public FavoriteListAdapter(Context context, int resource,
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
	public FavoriteListAdapter(Context context, int resource, ObjSong[] Songs) {
		super(context, resource, Songs);
		this.context = context;
	}

	// SongDetailsFragment detailsFragment = new SongDetailsFragment();
	/**
	 * getView method is called again and again when listview is scrolling up or
	 * down, to make the new views and attach to the list
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		final ObjSong song = listFound.get(position);
		ViewsHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(idResource, parent, false);
			holder = new ViewsHolder(row);
			row.setTag(holder);
		} else {
			holder = (ViewsHolder) row.getTag();
		}
		final int cbPos = position;
		itemContainer = (RelativeLayout) row.findViewById(R.id.relative);
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
				detailsDialogFragment
						.setOnCheckboxCallBackListener(new OnCheckboxCallBackListener() {

							@Override
							public void onCheckboxCallBackListener(View view) {
								view.setId(cbPos);
								FavoriteListAdapter.this.onClick(view);
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
		holder.isFavorite.setChecked(true);
		holder.isFavorite.setId(position);
		_Log.d("getView ", "set checkBox id = " + position);
		holder.setId(position);
		holder.isFavorite.setOnClickListener(this);
		holder.isFavorite.setFocusable(false);
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
	public void onClick(final View v) {
		final int pos = Integer.valueOf(v.getId());
		final View holderView = (View) listView.getChildAt(pos);
		_Log.d("onClick ", "checkBox id = " + pos);
		final ObjSong song = listFound.get(pos);// get the song at the exact
												// position on the list
		if (((CheckBox) v).isChecked()) {
			SQLQueries.addFavorite(Integer.valueOf(song.getSongId()));
			Toast.makeText(
					getContext(),
					context.getString(R.string.addFavor1) + song.getSongName()
							+ context.getString(R.string.addFavor2),
					Toast.LENGTH_SHORT).show();
			_Log.w(TAG,
					song.getRow_Id() + " - " + song.getSongName()
							+ context.getString(R.string.addFavor2));
		} else {
			new AlertDialog.Builder(getContext())
					.setTitle(
							context.getString(R.string.unFavor1)
									+ song.getSongName()
									+ context.getString(R.string.unFavor2))
					.setPositiveButton("YES",
							new DialogInterface.OnClickListener() {

								@SuppressLint("NewApi")
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									animation
											.setAnimationListener(new AnimationListener() {

												@Override
												public void onAnimationStart(
														Animation animation) {
													// holderView.setHasTransientState(true);
												}

												@Override
												public void onAnimationRepeat(
														Animation animation) {

												}

												@Override
												public void onAnimationEnd(
														Animation animation) {
													// holderView.setHasTransientState(false);
													SQLQueries.unFavorite(song
															.getSongId());
													listFound.remove(pos);
													_Log.w(TAG,
															song.getRow_Id()
																	+ "  -----  "
																	+ song.getSongId()
																	+ " --- "
																	+ song.getSongName()
																	+ " is removed from favorite list at "
																	+ pos);
													notifyDataSetChanged();
												}
											});
									if (holderView != null) {
										holderView.startAnimation(animation);
										_Log.w(TAG, "holder view not null");
									} else {
										_Log.w(TAG, "holder view IS null");
										int start = listView
												.getFirstVisiblePosition();
										for (int i = start, j = listView
												.getLastVisiblePosition(); i <= j; i++)
											if (song == listFound.get(i)) {
												listView.getChildAt(i - start)
														.startAnimation(
																animation);
											}
									}
									/*
									 * Checks detailsDialogFragment is showed
									 * any once or not and it is showing right
									 * now.
									 */
									if (FavoriteListAdapter.this.detailsDialogFragment != null
											&& !(detailsDialogFragment
													.isDetached()))
										FavoriteListAdapter.this.detailsDialogFragment
												.dismiss();
								}
							})
					.setNegativeButton("NO",
							new DialogInterface.OnClickListener() {

								@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (detailsDialogFragment != null
											&& !(detailsDialogFragment
													.isDetached()))
										detailsDialogFragment
												.reSetCheckboxState();
									notifyDataSetChanged();
									dialog.dismiss();
								}
							}).setCancelable(false).create().show();
		}
	}
}