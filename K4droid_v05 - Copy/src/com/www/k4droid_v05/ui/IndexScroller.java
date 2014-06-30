package com.www.k4droid_v05.ui;

import com.www.k4droid_v05.util._Log;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.SystemClock;

import android.view.MotionEvent;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;

/**
 * This class provides a bar that will be sticked on right side of a ListView.
 * 
 * @author DUC QUYNH
 * 
 */
public class IndexScroller {
	private static final String TAG = IndexScroller.class.getName();

	private static final int STATE_HIDDEN = 0;
	private static final int STATE_SHOWN = 2;
	private static final int STATE_SHOWING = 1;
	private static final int STATE_HIDING = 3;

	private float mDensity;
	private float mScaledDensity;
	private float mIndexbarWidth;
	private float mIndexbarMargin;
	private float mPreviewPadding;
	private float mAlphaRate;

	private int mState = STATE_HIDDEN;
	private int mCurrentSection = -1;
	private int mListViewWidth;
	private int mListViewHeight;

	private boolean mIsIndexing = false;
	private String[] mSections = null;
	private SectionIndexer mIndexer = null;
	private ListView mListView = null;
	private RectF mIndexbarRect = null;

	public IndexScroller(Context context, ListView listView) {
		mDensity = context.getResources().getDisplayMetrics().density;
		mScaledDensity = context.getResources().getDisplayMetrics().scaledDensity;

		mListView = listView;
		setAdapter(mListView.getAdapter());

		mIndexbarWidth = 30 * mDensity;
		mIndexbarMargin = 5 * mDensity;
		mPreviewPadding = 5 * mDensity;
	}

	/**
	 * Draws the bar and index characters on it. Also in charge of aligning and
	 * margin.
	 * 
	 * @param canvas
	 */
	public void draw(Canvas canvas) {
		if (mState == STATE_HIDDEN)
			return;

		// mAlphaRate determine the rate od opacity
		Paint indexbarPaint = new Paint();
		indexbarPaint.setColor(Color.BLACK);
		indexbarPaint.setAlpha((int) (64 * mAlphaRate));
		indexbarPaint.setAntiAlias(true);
		canvas.drawRoundRect(mIndexbarRect, 5 * mDensity, 5 * mDensity,
				indexbarPaint);
		if (mSections != null && mSections.length > 0) {
			// Preview is shown when mCurrentSection is set
			if (mCurrentSection >= 0) {
				Paint previewPaint = new Paint();
				previewPaint.setColor(Color.GRAY);
				previewPaint.setAlpha(96);
				previewPaint.setAntiAlias(true);
				previewPaint.setShadowLayer(3, 0, 0, Color.argb(64, 0, 0, 0));

				Paint previewTextPaint = new Paint();
				previewTextPaint.setColor(Color.WHITE);
				previewTextPaint.setAntiAlias(true);
				previewTextPaint.setTextSize(50 * mScaledDensity);

				float previewTextWidth = previewTextPaint
						.measureText(mSections[mCurrentSection]);
				float previewSize = 2 * mPreviewPadding
						+ previewTextPaint.descent()
						- previewTextPaint.ascent();
				RectF previewRect = new RectF(
						(mListViewWidth - previewSize) / 2,
						(mListViewHeight - previewSize) / 2,
						(mListViewWidth - previewSize) / 2 + previewSize,
						(mListViewHeight - previewSize) / 2 + previewSize);

				canvas.drawRoundRect(previewRect, 5 * mDensity, 5 * mDensity,
						previewPaint);
				canvas.drawText(
						mSections[mCurrentSection],
						previewRect.left + (previewSize - previewTextWidth) / 2
								- 1,
						previewRect.top + mPreviewPadding
								- previewTextPaint.ascent() + 1,
						previewTextPaint);
			}

			Paint indexPaint = new Paint();
			indexPaint.setColor(Color.WHITE);
			indexPaint.setAlpha((int) (255 * mAlphaRate));
			indexPaint.setAntiAlias(true);
			indexPaint.setTextSize(12 * mScaledDensity);

			float sectionHeight = (mIndexbarRect.height() - 2 * mIndexbarMargin)
					/ mSections.length;
			float paddingTop = (sectionHeight - (indexPaint.descent() - indexPaint
					.ascent())) / 2;
			for (int i = 0; i < mSections.length; i++) {
				float paddingLeft = (mIndexbarWidth - indexPaint
						.measureText(mSections[i])) / 2;
				canvas.drawText(mSections[i], mIndexbarRect.left + paddingLeft,
						mIndexbarRect.top + mIndexbarMargin + sectionHeight * i
								+ paddingTop - indexPaint.ascent(), indexPaint);
			}
		}
	}

	/**
	 * Set the character set as indexer for the bar.
	 * 
	 * @param adapter
	 */
	void setAdapter(ListAdapter adapter) {
		if (adapter instanceof SectionIndexer) {
			mIndexer = (SectionIndexer) adapter;
			mSections = (String[]) mIndexer.getSections();
		}
	}

	/**
	 * Handles touching event when it occurs.
	 * 
	 * @param ev
	 * @return
	 */
	public boolean onTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {

		case MotionEvent.ACTION_DOWN:
			// If down event occurs inside index bar region, start indexing
			if (mState != STATE_HIDDEN && constains(ev.getX(), ev.getY())) {
				setState(STATE_SHOWN);

				// It demonstrates that the motion event started from index bar
				mIsIndexing = true;

				// Determine which section the point is in, and move the list to
				// that section
				mCurrentSection = getSectionByPoint(ev.getY());
				mListView.setSelection(mIndexer
						.getPositionForSection(mCurrentSection));
				return true;

			}
			break;

		case MotionEvent.ACTION_MOVE:
			if (mIsIndexing) {
				// If this event moves inside index bar
				if (constains(ev.getX(), ev.getY())) {
					// Determine which section the point is in, and move the
					// list to that section
					mCurrentSection = getSectionByPoint(ev.getY());
					mListView.setSelection(mIndexer
							.getPositionForSection(mCurrentSection));
				}
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mIsIndexing) {
				mIsIndexing = false;
				mCurrentSection = -1;
			}
			if (mState == STATE_SHOWN)
				setState(STATE_HIDING);
			break;
		}
		return false;
	}

	/**
	 * In charge of changing the size of bar and its align, margin.
	 * 
	 * @param w
	 * @param h
	 * @param oldw
	 * @param oldh
	 */
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		mListViewWidth = w;
		mListViewHeight = h;
		mIndexbarRect = new RectF(w - mIndexbarMargin - mIndexbarWidth,
				mIndexbarMargin, w - mIndexbarMargin, h - mIndexbarMargin);
	}

	/**
	 * Changes the state from HIDDEN to SHOWING or from HIDING to HIDDEN.
	 */
	public void show() {
		if (mState == STATE_HIDDEN) {
			setState(STATE_SHOWING);
			_Log.i(TAG, "Change state from HIDDEN -> SHOWING");
		} else if (mState == STATE_HIDING) {
			setState(STATE_HIDDEN);
			_Log.i(TAG, "Change state from HIDING -> HIDDEN");
		}
	}

	/**
	 * Changes the state from SHOWN to HIDING.
	 */
	public void hide() {
		if (mState == STATE_SHOWN)
			setState(STATE_HIDING);
	}

	/**
	 * Handles the IndexScroller activities follows its states (SHOWING, SHOWN,
	 * HIDING, HIDDEN).
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (mState) {
			case STATE_SHOWING:
				// Fade in effect
				mAlphaRate += (1 - mAlphaRate) * 0.2;
				if (mAlphaRate > 0.9) {
					mAlphaRate = 1;
					setState(STATE_SHOWN);
				}
				mListView.invalidate();
				fade(10);
				break;
			case STATE_SHOWN:
				// If there no action, hide automatically
				setState(STATE_HIDING);
				break;
			case STATE_HIDING:
				// Fade out effect
				mAlphaRate -= mAlphaRate * 0.2;
				if (mAlphaRate < 0.1) {
					mAlphaRate = 0;
					setState(STATE_HIDDEN);
				}
				mListView.invalidate();
				fade(10);
				break;
			}
		};
	};

	/**
	 * Set the states (SHOWING, SHOWN, HIDING, HIDDEN) for IndexScroller.
	 * 
	 * @param state
	 */
	private void setState(int state) {
		if (state < STATE_HIDDEN || state > STATE_HIDING)
			return;

		mState = state;

		switch (mState) {
		case STATE_HIDDEN:
			// Cancel any fade effect
			mHandler.removeMessages(0);
			break;
		case STATE_SHOWING:
			// Start to fade in
			mAlphaRate = 0;
			fade(0);
			break;
		case STATE_SHOWN:// Cancel any fade effect
			mHandler.removeMessages(0);
			break;
		case STATE_HIDING:
			// Start to fade out after three seconds
			mAlphaRate = 1;
			fade(3000);
			break;
		}
	}

	/**
	 * Try to fade out the bar after the delaying time.
	 * 
	 * @param delayTime
	 */
	private void fade(long delayTime) {
		mHandler.removeMessages(0);
		mHandler.sendEmptyMessageAtTime(0, SystemClock.uptimeMillis()
				+ delayTime);

	}

	/**
	 * 
	 * @param y
	 *            = eventMotion.getY();
	 * @return Returns the position of the char in the sections array, which's
	 *         coordinate vertical appropriately to y parameter.
	 */
	private int getSectionByPoint(float y) {
		//
		if (mSections == null || mSections.length == 0)
			return 0;
		//
		if (y < mIndexbarRect.top + mIndexbarMargin)
			return 0;
		// if the point's coordinate out bound of last Index bar rectangle,
		// return the last position of the sections array
		if (y >= mIndexbarRect.top + mIndexbarRect.height() - mIndexbarMargin)
			return mSections.length - 1;
		return (int) ((y - mIndexbarRect.top - mIndexbarMargin) / ((mIndexbarRect
				.height() - 2 * mIndexbarMargin) / mSections.length));
	}

	/**
	 * Determine if the point is in index bar region, which includes the right
	 * margin of the bar.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean constains(float x, float y) {
		return (x >= mIndexbarRect.left && y >= mIndexbarRect.top && y <= mIndexbarRect.top
				+ mIndexbarRect.height());
	}
}
