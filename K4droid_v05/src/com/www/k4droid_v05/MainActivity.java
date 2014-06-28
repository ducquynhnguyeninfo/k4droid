package com.www.k4droid_v05;

import java.util.ArrayList;
import java.util.List;

import com.www.k4droid_v05.R;
import com.www.k4droid_v05.adapters.TitleNavigationAdapter;
import com.www.k4droid_v05.adapters._PagerAdapter;
import com.www.k4droid_v05.db.DatabaseHandler;
import com.www.k4droid_v05.db.SQLQueries;
import com.www.k4droid_v05.interfaces.IOnTaskCompletion;
import com.www.k4droid_v05.model.ContentFragment;
import com.www.k4droid_v05.model.Loader;
import com.www.k4droid_v05.model.actionbar.SpinnerNavItem;
import com.www.k4droid_v05.model.actionbar._SearchView;
import com.www.k4droid_v05.obj.ObjSong;
import com.www.k4droid_v05.ui.PagerSlidingTabStrip;
import com.www.k4droid_v05.util.*;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends android.support.v4.app.FragmentActivity
		implements SearchView.OnQueryTextListener, SearchView.OnCloseListener,
		PagerSlidingTabStrip.OnPageChangedListener {

	private static final String TAG = "MainActivity";
	private final android.os.Handler handler = new android.os.Handler();
	private com.www.k4droid_v05.ui.PagerSlidingTabStrip tabs;
	private android.support.v4.view.ViewPager pager;

	private com.www.k4droid_v05.adapters._PagerAdapter adapter;
	/**
	 * List of {@link ObjSong}s.
	 */
	private List<ObjSong> songs = new ArrayList<ObjSong>();
	/**
	 * 
	 */
	private Drawable oldBackground = null;
	private int currentColor = 0xFF3F9FE0;
	/**
	 * Database handler.
	 */
	private DatabaseHandler databaseHandler;

	private ActionBar actionBar;
	private ArrayList<SpinnerNavItem> navSpinner;
	private TitleNavigationAdapter navigationAdapter;
	/**
	 * Array contains all types of searching follows: by ID, by name, by author,
	 * by lyric.
	 */
	private String[] searchByArr;
	/**
	 * Indexes the searching type we want to use.
	 */
	public static int searchByIndexer = 0;
	/**
	 * ListView that will show the searching results.
	 */
	private ListView searchingListView;

	// private TextView hintLayout = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_main);
		actionBar = getActionBar();
		tabs = (com.www.k4droid_v05.ui.PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);

		adapter = new _PagerAdapter(getSupportFragmentManager());

		pager.setAdapter(adapter);
		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());

		pager.setPageMargin(pageMargin);
		pager.setOnPageChangeListener(this);
		pager.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "Stop clicking me!!!",
						Toast.LENGTH_SHORT).show();
				_Log.v("pager", "pager was clicked");
			}
		});
		initComponents();
		tabs.setViewPager(pager);
		changeColor(currentColor);

	}

	/**
	 * Initializes fundamental components.
	 */
	private void initComponents() {
		searchByArr = getResources().getStringArray(R.array.searchby);

		// Hide the action bar title
		actionBar.setDisplayShowTitleEnabled(false);
		// Enabling Spinner dropdown navigation
		// actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		_Log.w("init database", "init database");
		Loader dbLoader = new RealLoader() {
			@Override
			public List<ObjSong> onDoInBackgound(String... params) {
				databaseHandler = new DatabaseHandler(getBaseContext());
				return super.onDoInBackgound(params);
			}
		};
		dbLoader.execute("0", "", searchByIndexer + "");
	}

	/**
	 * Helps us change the Actionbar's color easily.
	 * 
	 * @param newColor
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void changeColor(int newColor) {
		tabs.setIndicatorColor(newColor);
		// change ActionBar color just if an ActionBar is available
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			Drawable colorDrawable = new ColorDrawable(newColor);
			Drawable bottomDrawable = getResources().getDrawable(
					R.drawable.actionbar_bottom);
			LayerDrawable ld = new LayerDrawable(new Drawable[] {
					colorDrawable, bottomDrawable });
			if (oldBackground == null) {
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
					ld.setCallback(drawableCallback);
				} else {
					actionBar.setBackgroundDrawable(ld);
				}
			} else {
				TransitionDrawable td = new TransitionDrawable(new Drawable[] {
						oldBackground, ld });
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
					td.setCallback(drawableCallback);
				} else {
					actionBar.setBackgroundDrawable(td);
				}
				td.startTransition(200);
			}
			oldBackground = ld;

			getActionBar().setDisplayShowTitleEnabled(false);
		}
		currentColor = newColor;
	}

	public void onColorClicked(View v) {
		int color = Color.parseColor(v.getTag().toString());
		changeColor(color);
	}

	/**
	 * 
	 * @return ListView of {@link MainActivity}
	 */
	public ListView getSearchingListView() {
		return searchingListView;
	}

	public void setSearchingListView(ListView searchingListView) {
		this.searchingListView = searchingListView;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("currentColor", currentColor);
		FragmentManager manager = getSupportFragmentManager();
		manager.putFragment(outState, adapter.getItem(pager.getCurrentItem())
				.getTag(), adapter.getItem(pager.getCurrentItem()));
	}

	ContentFragment content;

	private void instantiateFragments(Bundle inState) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();

		if (inState != null) {
			content = (ContentFragment) manager.getFragment(inState,
					ContentFragment.TAG);
		} else {
			content = new ContentFragment();
			transaction.add(1, content, content.TAG);
			transaction.commit();
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		currentColor = savedInstanceState.getInt("currentColor");
		changeColor(currentColor);
		instantiateFragments(savedInstanceState);
	}

	private Drawable.Callback drawableCallback = new Drawable.Callback() {

		@Override
		public void unscheduleDrawable(Drawable who, Runnable what) {
			handler.removeCallbacks(what);
		}

		@Override
		public void scheduleDrawable(Drawable who, Runnable what, long when) {
			handler.postAtTime(what, when);

		}

		@SuppressLint("NewApi")
		@Override
		public void invalidateDrawable(Drawable who) {
			getActionBar().setBackgroundDrawable(who);
		}
	};

	/**
	 * Customize searchview
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem itemSearch = menu.add(1, 5234, 0, "search");
		itemSearch.setIcon(android.R.drawable.ic_menu_search);
		itemSearch.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
				| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		_SearchView searchView = new _SearchView(this);
		searchView.setOnQueryTextListener(this);
		searchView.setOnCloseListener(this);
		searchView.setIconifiedByDefault(true);
		itemSearch.setActionView(searchView);
		_Log.v("searching order", itemSearch.getOrder() + "");
		/*
		 * *********************************** Spinner
		 * ****************************
		 */
		MenuItem itemSpinner = menu.add(2, 23, 1, "Spinner");
		itemSpinner.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		itemSpinner.setActionView(declareActionBarSpinner());
		return true;
	}

	/**
	 * Declares Actionbar's drop down spinner items.
	 * 
	 * @return A drop down spinner.
	 */
	private Spinner declareActionBarSpinner() {
		Spinner spinner = new Spinner(getApplicationContext(),
				Spinner.MODE_DROPDOWN);
		// Spinner title navigation data
		navSpinner = new ArrayList<SpinnerNavItem>();
		navSpinner.add(new SpinnerNavItem(searchByArr[0],
				R.drawable.ic_songname));
		navSpinner
				.add(new SpinnerNavItem(searchByArr[1], R.drawable.ic_songid));

		navSpinner.add(new SpinnerNavItem(searchByArr[2],
				R.drawable.ic_songauthor));
		navSpinner.add(new SpinnerNavItem(searchByArr[3],
				R.drawable.ic_songlyrics));
		navigationAdapter = new TitleNavigationAdapter(getApplicationContext(),
				navSpinner);
		spinner.setAdapter(navigationAdapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				_Log.i("spinner selects", position + "");
				searchByIndexer = position;
				new RealLoader().execute(new String[] { whichTab + "", clue,
						searchByIndexer + "" });
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
			}
		});
		return spinner;
	}

	@Override
	public boolean onClose() {
		return false;
	}

	private int whichTab = 0;
	private static String clue = "";

	@Override
	public boolean onQueryTextChange(String newText) {
		clue = newText;
		whichTab = pager.getCurrentItem();
		_Log.v(TAG, "tab number " + whichTab);
		fetchData(clue);
		return true;
	}

	/**
	 * Set data for the ListView.
	 * 
	 * @param songs
	 */
	private void setData(List<ObjSong> songs) {
		this.songs = songs;
	}

	/**
	 * Return data for ListView.
	 * 
	 * @return
	 */
	public List<ObjSong> getData() {
		return this.songs;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {

		return false;
	}

	/**
	 * Fetching the list of songs following the clue received
	 * 
	 * @return The result list of song objects was found
	 */
	public void fetchData(String clue) {
		new RealLoader().execute(new String[] { String.valueOf(whichTab), clue,
				String.valueOf(searchByIndexer) });
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		_Log.v("ON onPageScrollStateChanged ", "onPageScrollStateChanged "
				+ state);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		_Log.v("ON onPageScrolled ", "onPageScrolled " + position);
	}

	FragmentTransaction fragmentTransaction = getSupportFragmentManager()
			.beginTransaction();

	/**
	 * Handles events when a page of ViewPager is chosen.
	 */
	@Override
	public void onPageSelected(int position) {
		_Log.v("ON onPageSelected ", "onPageSelected " + position);
		switch (position) {
		case 0:
			// fragmentTransaction.show(adapter.getItem(0));
			break;
		case 1:
			// fragmentTransaction.hide(adapter.getItem(0));
			break;
		case 2:
			whichTab = position;
			fetchData("");
			break;
		}
	}

	/**
	 * Uses to synchronize the searching typing and the query in the searching
	 * duration or whatever work that needed to synchronize.
	 * 
	 * @author DUC QUYNH
	 * 
	 */
	private class RealLoader extends com.www.k4droid_v05.model.Loader {

		LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			linlaHeaderProgress.setVisibility(View.VISIBLE);
		}

		private Cursor getCursorByType(String clues, String typeIndexser) {
			switch (Integer.valueOf(typeIndexser)) {
			case 0:
				return SQLQueries.selectByName(clues);
			case 1:
				return SQLQueries.selectByID(clues);
			case 2:
				return SQLQueries.selectByAuthor(clues);
			case 3:
				return SQLQueries.selectByLyric(clues);
			default:
				return SQLQueries.selectByName(clues);
			}
		}

		private Cursor getFavoriteCursorByType(String clues, String typeIndexser) {
			switch (Integer.valueOf(typeIndexser)) {
			case 0:
				return SQLQueries.selectFavoriteByName(clues);
			case 1:
				return SQLQueries.selectFavoriteByID(clues);
			case 2:
				return SQLQueries.selectFavoriteByAuthor(clues);
			case 3:
				return SQLQueries.selectFavoriteByLyric(clues);
			default:
				return SQLQueries.selectFavoriteByName(clues);
			}
		}

		@Override
		public List<ObjSong> onDoInBackgound(String... params) {
			Cursor cursor = null;
			List<ObjSong> objSongs = new ArrayList<ObjSong>();
			for (int c = 0; c < params.length; c++) {
				_Log.i("params values - " + c, params[c]);
			}
			switch (Integer.valueOf(params[0])) {
			case 0:
				cursor = getCursorByType(params[1], params[2]);
				break;
			case 2:
				if (params[1].equals(""))
					cursor = SQLQueries.selectFavorite(params[1]);
				else
					cursor = getFavoriteCursorByType(params[1], params[2]);
				break;
			}

			ObjSong song = null;
			try {
				if (cursor != null) {
					_Log.v(clue, cursor.getCount() + "");

					for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
							.moveToNext()) {
						song = new ObjSong();

						song.setSongId(cursor.getString(cursor
								.getColumnIndexOrThrow("song_id")));
						song.setSongName(cursor.getString(cursor
								.getColumnIndexOrThrow("name")));
						song.setSongAuthor(cursor.getString(cursor
								.getColumnIndexOrThrow("author")));
						song.setSongLyric(cursor.getString(cursor
								.getColumnIndexOrThrow("lyric")));
						song.setSongIsFavorite(cursor.getInt(cursor
								.getColumnIndexOrThrow("favorite")));

						objSongs.add(song);
						_Log.i(" Song's name ", song.getSongIsFavorite()
								+ " - " + song.getSongName());
					}
				} else
					_Log.v(clue, "SADLY - cursor is null ");
				databaseHandler.close(null);
				cursor.close();

			} catch (CursorIndexOutOfBoundsException ce) {
				ce.getCause();
			} catch (Exception e) {
				e.getCause();
			}
			return objSongs;
		}

		@Override
		protected List<ObjSong> doInBackground(String... params) {

			return onDoInBackgound(params);
		}

		@Override
		protected void onPostExecute(List<ObjSong> result) {
			super.onPostExecute(result);
			onTaskCompletion.onTaskCompleted(result);
			linlaHeaderProgress.setVisibility(View.GONE);
		}
	}

	/**
	 * A loader task completed listener.
	 */
	private IOnTaskCompletion onTaskCompletion = new IOnTaskCompletion() {

		@Override
		public void onTaskCompleted(List<ObjSong> objSongs) {
			setData(objSongs); // Set data set for the listview to get.
			switch (whichTab) {
			case 0:
				adapter.getItem(0);
				break;
			case 1:
				adapter.getItem(1);
				break;
			case 2:
				adapter.getItem(2);
				break;
			}
		}
	};
}
