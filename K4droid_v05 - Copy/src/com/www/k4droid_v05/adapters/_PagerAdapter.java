package com.www.k4droid_v05.adapters;

import com.www.k4droid_v05.SuperAwesomeCardFragment;
import com.www.k4droid_v05.model.ContentFragment;
import com.www.k4droid_v05.util.Util;
import com.www.k4droid_v05.util._Log;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * Implementation of {@link FragmentPagerAdapter} that represents each page as a
 * Fragment that is persistently kept in the fragment manager as long as the
 * user can return to the page.
 * 
 * This version of the pager is best for use when there are a handful of
 * typically more static fragments to be paged through, such as a set of tabs.
 * The fragment of each page the user visits will be kept in memory, though its
 * view hierarchy may be destroyed when not visible. This can result in using a
 * significant amount of memory since fragment instances can hold on to an
 * arbitrary amount of state. For larger sets of pages, consider
 * FragmentStatePagerAdapter.
 * 
 * @author DUC QUYNH
 * 
 */
public class _PagerAdapter extends FragmentPagerAdapter {

	private FragmentManager fManager;
	private final String[] TITLES = { "TÌM KIẾM", "CA KHÚC", "YÊU THÍCH" };

	public _PagerAdapter(FragmentManager fm) {
		super(fm);
		this.fManager = fm;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return TITLES[position];
	}

	@Override
	public float getPageWidth(int position) {
		return super.getPageWidth(position);
	}

	/**
	 * searching fragment.
	 */
	private ContentFragment searchingFragment = null;
	/**
	 * Favorite fragment.
	 */
	private ContentFragment favoriteFragment = null;

	/**
	 * Get the fragment that has already declared to reuse. If the called
	 * fragment does not exist, make a new instance then return it like a new
	 * one.
	 */
	@Override
	public Fragment getItem(int position) {

		_Log.d("position", "---" + position);
		switch (position) {
		case 0:
			if (searchingFragment != null) {
				_Log.d("position", "- uses old fragment keke - " + position);
				return searchingFragment.refresh(position);
			}

			else {
				_Log.d("position", "- create new fragment at tab - " + position);
				searchingFragment = (ContentFragment) ContentFragment
						.newInstance(position, Util.searchingfragment);
				return searchingFragment;
			}
		case 1:
			return SuperAwesomeCardFragment.newInstance(position);
		case 2:
			if (favoriteFragment != null) {
				_Log.d("position", "- uses old favoriteFragment keke - "
						+ position);
				return favoriteFragment.refresh(position);
			}

			else {
				_Log.d("position", "- create new favoriteFragment at tab - "
						+ position);
				favoriteFragment = (ContentFragment) ContentFragment
						.newInstance(position, Util.favoritefragment);
				return favoriteFragment;
			}

		}
		return null;
	}

	/**
	 * Get the fragment which is presented right now.
	 * 
	 * @param container
	 * @param position
	 * @return
	 */
	public Fragment getActiveFragment(ViewPager container, int position) {
		String name = makeFragmentName(container.getId(), position);
		return fManager.findFragmentByTag(name);
	}

	private static String makeFragmentName(int viewId, int index) {
		return "android:switcher:" + viewId + ":" + index;
	}

	/**
	 * Count the fragment that this {@link _PagerAdapter} contains.
	 */
	@Override
	public int getCount() {
		return TITLES.length;
	}

	/**
	 * Returns a fragment by calls its name.
	 * 
	 * @param tag The name of fragment.
	 * @return The fragment that suits with the name.
	 */
	public Fragment getItemByTag(String tag) {
		Util.ItemTag mItemTag = Util.ItemTag.valueOf(tag);
		if (mItemTag.toString().equalsIgnoreCase(tag)) {
			switch (mItemTag) {
			case TAG_SEARCHING:
				return getItem(0);
			case TAG_LIST:
				return getItem(1);
			case TAG_FAVORITE:
				return getItem(2);
			}
		}
		return null;
	}
}
