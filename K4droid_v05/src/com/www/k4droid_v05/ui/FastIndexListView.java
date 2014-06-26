package com.www.k4droid_v05.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;

public class FastIndexListView extends ListView {

	private static int indWidth = 20;
	private String[] sections = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "U", "V", "S", "T", "W",
			"X", "Y", "Z" };
	private float scaledWidth;
	private float sx;
	private int indexSize;
	private String section;
	private Context context;
	private boolean showLetter = true;
	private ListHandler listHandler;

	public FastIndexListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;

	}

	public FastIndexListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

	}

	public FastIndexListView(Context context, String keyList) {
		super(context);
		this.context = context;

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		scaledWidth = indWidth * getSizeInPixel(context);
		sx = this.getWidth() - this.getPaddingRight() - scaledWidth;

		Paint p = new Paint();
		p.setColor(Color.RED);
		p.setAlpha(100);

		canvas.drawRect(sx, this.getPaddingTop(), sx + scaledWidth,
				this.getHeight() - this.getPaddingBottom(), p);
		indexSize = (this.getHeight() - this.getPaddingTop() - getPaddingBottom())
				/ sections.length;

		Paint textPaint = new Paint();
		textPaint.setColor(Color.DKGRAY);
		textPaint.setTextSize(scaledWidth / 2);
		for (int i = 0; i < sections.length; i++) {
			canvas.drawText(sections[i].toLowerCase(),
					sx + textPaint.getTextSize() / 2, getPaddingTop()
							+ indexSize * (i + 1), textPaint);
			// We draw letter in the middle
			if (showLetter & section != null && !section.equals("")) {
				Paint textPaint2 = new Paint();
				textPaint2.setColor(Color.DKGRAY);
				textPaint2.setTextSize(2 * indWidth);
				canvas.drawText(section.toUpperCase(), getWidth() / 2,
						getHeight() / 2, textPaint2);

			}
		}
	}

	private static float getSizeInPixel(Context ctx) {
		return ctx.getResources().getDisplayMetrics().density;
	}

	@Override
	public void setFastScrollEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		super.setFastScrollEnabled(true);
	}
	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		if (adapter instanceof SectionIndexer) {
			sections = (String[]) ((SectionIndexer) adapter).getSections();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		float x = ev.getX();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			if (x < sx)
				return super.onTouchEvent(ev);
			else {

				// we touched the index bar
				float y = ev.getY() - this.getPaddingTop()
						- this.getPaddingBottom();
				int currentPosition = (int) Math.floor(y / indexSize);
				section = sections[currentPosition];
				this.setSelection(((SectionIndexer) getAdapter())
						.getPositionForSection(currentPosition));
			}
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			if (x < sx)
				return super.onTouchEvent(ev);
			else {

				// we touched the index bar
				float y = ev.getY();
				int currentPosition = (int) Math.floor(y / indexSize);
				currentPosition = Math.max( 0, currentPosition );
				currentPosition = Math.min( sections.length - 1, currentPosition );
				section = sections[currentPosition];
				this.setSelection(((SectionIndexer) getAdapter())
						.getPositionForSection(currentPosition));
			}
			break;
		}
		case MotionEvent.ACTION_UP: {
			listHandler = new ListHandler();
			listHandler.sendEmptyMessageDelayed(0, 30 * 1000);
			break;
		}
		}
		return true;
	}

	private class ListHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			showLetter = false;
			FastIndexListView.this.invalidate();
		}

	}
}
