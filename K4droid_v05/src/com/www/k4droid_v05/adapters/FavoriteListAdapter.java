package com.www.k4droid_v05.adapters;

import java.util.List;

import com.www.k4droid_v05.R;
import com.www.k4droid_v05.db.SQLQueries;
import com.www.k4droid_v05.model.DetailWindow;
import com.www.k4droid_v05.obj.ObjSong;
import com.www.k4droid_v05.obj.ViewsHolder;
import com.www.k4droid_v05.ui.IndexableListView;
import com.www.k4droid_v05.util._Log;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class FavoriteListAdapter extends BaseListAdapter implements
		com.www.k4droid_v05.OnTextFragmentAnimationEndListener,
		FragmentManager.OnBackStackChangedListener {

	private static final String TAG = "FavoriteListAdapter";
	private static Context context;
	private int idResource;
	private List<ObjSong> listFound;
	private Animation animation;
	private IndexableListView listView;

	protected boolean isEverDetailed = false;
	private DetailWindow detailWindow;
	private Animation animShow;
	private Animation animHide;
	private RelativeLayout itemContainer;

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
		animShow = AnimationUtils.loadAnimation(getContext(),
				R.anim.enter_from_right);
		animHide = AnimationUtils.loadAnimation(getContext(),
				R.anim.exit_to_right);

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
		// Retrieve and cache the system's default "short" animation time.
		shortAnimationDuration = ((Activity) context).getResources()
				.getInteger(R.integer.config_shortAnimTime);
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

		itemContainer = (RelativeLayout) row.findViewById(R.id.relative);
		detailWindow = (DetailWindow) ((Activity) context)
				.findViewById(R.id.popup_window);
		detailWindow.setVisibility(View.INVISIBLE);
		final View other_row = row;
//		itemContainer.setOnClickListener(new OnClickListener() {
//
//			@SuppressLint("NewApi")
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(getContext(), "relative layout is clicked",
//						Toast.LENGTH_SHORT).show();
//				// Bundle bundle = new Bundle(5);
//				// bundle.putString(ObjSong.SONG_ID, song.getSongId());
//				// bundle.putString(ObjSong.SONG_NAME, song.getSongName());
//				// bundle.putString(ObjSong.SONG_AUTHOR, song.getSongAuthor());
//				// bundle.putString(ObjSong.SONG_LYRIC, song.getSongLyric());
//				// bundle.putInt(ObjSong.SONG_IS_FAVORITE,
//				// song.getSongIsFavorite());
//				//
//				_Log.d("item clicked", song.getSongName());
//				// if (isEverDetailed == false) {
//				// DetailsFragment detailsFragment = new DetailsFragment();
//				// detailsFragment.setArguments(bundle);
//				// android.app.FragmentTransaction fragmentTransaction =
//				// ((Activity) getContext())
//				// .getFragmentManager().beginTransaction();
//				// fragmentTransaction.setCustomAnimations(R.animator.fade_in,
//				// R.animator.fade_out);
//				// fragmentTransaction.replace(R.id.contaier, detailsFragment);
//				// fragmentTransaction.addToBackStack(fragmentTransaction
//				// .getClass().getName());
//				// fragmentTransaction.commit();
//				// isEverDetailed = true;
//				// } else {
//				// DetailsFragment detailsFragment = (DetailsFragment)
//				// ((Activity) context)
//				// .getFragmentManager()
//				// .getFragment(bundle, "objSong");
//				//
//				// detailsFragment.updateViews();
//				//
//				// }
//				// if (isEverDetailed) {
//				// detailWindow.startAnimation(animHide);
//				// detailWindow.setVisibility(View.INVISIBLE);
//				//
//				// listView.setEnabled(true);
//				// listView.setFocusable(true);
//				// listView.setFocusableInTouchMode(true);
//				// isEverDetailed = false;
//				// } else {
//				// detailWindow.setVisibility(View.VISIBLE);
//				// detailWindow.startAnimation(animShow);
//				//
//				// listView.setEnabled(false);
//				// listView.setFocusable(false);
//				// listView.setFocusableInTouchMode(false);
//				// isEverDetailed = true;
//				//
//				// }
//
//				zoomDetailChildView(other_row);
//			}
//		});
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
								}
							})
					.setNegativeButton("NO",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									notifyDataSetChanged();
									dialog.dismiss();
								}
							}).setCancelable(false).create().show();
		}
	}

	@Override
	public void onBackStackChanged() {

	}

	@Override
	public void onAnimationEnd() {

	}

	private Animator currentAnimator;
	private int shortAnimationDuration;

	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	protected void zoomDetailChildView(final View smallFormView) {
		// If there is an an1imation in progress , cancel it immediately and
		// proceed with this one.
		if (currentAnimator != null)
			currentAnimator.cancel();
		// Load the high resolution "zoomed-in" image
		final DetailWindow detailedWindow = (DetailWindow) ((Activity) context)
				.findViewById(R.id.popup_window);

		// Calculate the starting and ending bounds for the zoom-in view .
		final Rect startBounds = new Rect();
		final Rect finalBounds = new Rect();
		final Point globalOffset = new Point();

		// The start bounds are the global visible rectangle of the thumbnal,
		// and the final bounds are the global visible rectangleof the
		// contaner-view. Also set the container view's offset the origin for
		// the bounds, since that's the origin for the positing animation
		// properties (X,Y).
		smallFormView.getGlobalVisibleRect(startBounds);
		((Activity) context).findViewById(R.id.wrappercontaier)
				.getGlobalVisibleRect(finalBounds, globalOffset);

		startBounds.offset(-globalOffset.x, -globalOffset.y);
		finalBounds.offset(-globalOffset.x, -globalOffset.y);
		Log.v("-globalOffset.x - y", -globalOffset.x + "       "
				+ -globalOffset.y);

		// Adjust the start bounds to be the same aspect ratio as the final
		// bounds using the "center crop" technique. This prevent undesireable
		// stretching during the animation. Also calculate the start scaling
		// factor ( the end scaling factor is alway 1.0).
		float startScale;
		// if ((float) finalBounds.width() / finalBounds.height() > startBounds
		// .width() / startBounds.height()) {
		// Extend start bounds horizontally
		startScale = (float) startBounds.height() / finalBounds.height();
		float startWidth = startScale * finalBounds.width();
		float deltaWidth = (startWidth - startBounds.width()) / 2;
		startBounds.left -= deltaWidth;
		startBounds.right += deltaWidth;

		// } else {
		// Extend start bounds vertically
		// startScale = (float) startBounds.width() / finalBounds.width();
		// float startHeight = startScale * finalBounds.height();
		// float deltaHeight = (startHeight - startBounds.height()) / 2;
		// startBounds.top -= deltaHeight;
		// startBounds.bottom += deltaHeight;
		// }

		detailedWindow.setVisibility(View.VISIBLE);

		// Set the pivot point for SCALE_X and SCALE_Y transformations to the
		// top-left corner of the zoomed-in view (the default is the center of
		// the view).
		detailedWindow.setPivotX(0f);
		detailedWindow.setPivotY(0f);

		// construct and run the parallel animation ofthe four transtation and
		// scale properties (X, Y ,SCALE_X, SCALE_Y)
		AnimatorSet set = new AnimatorSet();
		set.play(
				ObjectAnimator.ofFloat(detailedWindow, View.Y,
						startBounds.bottom, finalBounds.bottom))
				// .with(ObjectAnimator.ofFloat(detailedWindow, View.X,
				// startBounds.right, finalBounds.right))
				.with(ObjectAnimator.ofFloat(detailedWindow, View.Y,
						startBounds.top, finalBounds.top))
				.with(ObjectAnimator.ofFloat(detailedWindow, View.SCALE_Y,
						startScale, 1f))
				.with(ObjectAnimator.ofFloat(detailedWindow, View.SCALE_Y,
						startScale, 1f));

		set.setDuration(shortAnimationDuration);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				// thumbView.setAlpha(1f);
				detailedWindow.setVisibility(View.GONE);
				currentAnimator = null;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				// thumbView.setAlpha(1f);
				detailedWindow.setVisibility(View.GONE);
				currentAnimator = null;
			}
		});
		set.start();
		currentAnimator = set;

		// Upon clicking the zoomed-in image, it should zoom back down the the
		// original bounds and show the thumbnail instead of the expanded image.
		final float startScaleFinal = startScale;
		detailedWindow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (currentAnimator != null)
					currentAnimator.cancel();

				// Animata the four positioning/sizing properties in parallel,
				// back to their original values.
				AnimatorSet set = new AnimatorSet();
				set.play(
						ObjectAnimator.ofFloat(detailedWindow, View.X,
								startBounds.bottom))
						.with(ObjectAnimator.ofFloat(detailedWindow, View.Y,
								startBounds.top))
						.with(ObjectAnimator.ofFloat(detailedWindow,
								View.SCALE_Y, startScaleFinal));

				set.setDuration(shortAnimationDuration);
				set.setInterpolator(new DecelerateInterpolator());
				set.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						super.onAnimationEnd(animation);
						// thumbView.setAlpha(1f);
						detailedWindow.setVisibility(View.GONE);
						currentAnimator = null;
					}

					@Override
					public void onAnimationCancel(Animator animation) {
						// thumbView.setAlpha(1f);
						detailedWindow.setVisibility(View.GONE);
						currentAnimator = null;
					}
				});

				set.start();
				currentAnimator = set;
			}
		});
	}
}