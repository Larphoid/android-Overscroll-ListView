package com.larphoid.overscrollinglistview;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

public class OverscrollListview extends ListView implements OnScrollListener, View.OnTouchListener, android.widget.AdapterView.OnItemSelectedListener {
	public Handler mHandler = new Handler();
	private View measure;
	public int nHeaders = 1, nFooters = 1, divHeight, delay = 10;
	public boolean rebound = false;
	private int firstVis, visibleCnt, lastVis, totalItems, scrollstate;
	private boolean bounce = true, recalcV = false, trackballEvent = false;
	private long flingTimestamp = 0;
	private float velocity = 0;
	private static final float BREAKSPEED = 4.0f, BOUNCEBRAKE = -.5f;
	private GestureDetector gesture;

	public OverscrollListview(Context context) {
		super(context);
		initialize(context);
	}

	public OverscrollListview(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}

	public OverscrollListview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(context);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		firstVis = firstVisibleItem;
		visibleCnt = visibleItemCount;
		totalItems = totalItemCount;
		lastVis = firstVisibleItem + visibleItemCount;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		scrollstate = scrollState;
		if (scrollState != OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
			rebound = true;
			mHandler.postDelayed(checkListviewTopAndBottom, delay);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> av, View v, int position, long id) {
		rebound = true;
		mHandler.postDelayed(checkListviewTopAndBottom, delay);
	}

	@Override
	public void onNothingSelected(AdapterView<?> av) {
		rebound = true;
		mHandler.postDelayed(checkListviewTopAndBottom, delay);
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		trackballEvent = true;
		rebound = true;
		mHandler.postDelayed(checkListviewTopAndBottom, delay);
		return super.onTrackballEvent(event);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		gesture.onTouchEvent(event);
		return false;
	}

	private class thisGestureListener implements OnGestureListener {
		@Override
		public boolean onDown(MotionEvent e) {
			rebound = false;
			recalcV = false;
			velocity = 0;
			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			rebound = true;
			recalcV = true;
			velocity = velocityY / 25.0f;
			flingTimestamp = System.currentTimeMillis();
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			rebound = true;
			recalcV = false;
			velocity = 0;
			return false;
		}
	};

	public void initialize(Context context) {
		View header = new View(context);
		header.setMinimumHeight(((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight());
		addHeaderView(header, null, false);
		addFooterView(header, null, false);

		gesture = new GestureDetector(new thisGestureListener());
		gesture.setIsLongpressEnabled(false);
		flingTimestamp = System.currentTimeMillis();
		setHeaderDividersEnabled(false);
		setFooterDividersEnabled(false);
		setOnTouchListener(this);
		setOnScrollListener(this);
		setOnItemSelectedListener(this);
	}

	public void initializeValues() {
		nHeaders = getHeaderViewsCount();
		nFooters = getFooterViewsCount();
		divHeight = getDividerHeight();
		firstVis = 0;
		visibleCnt = 0;
		lastVis = 0;
		totalItems = 0;
		scrollstate = 0;
	}

	public void setBounce(boolean bouncing) {
		bounce = bouncing;
	}

	public Runnable checkListviewTopAndBottom = new Runnable() {
		@Override
		public void run() {
			mHandler.removeCallbacks(checkListviewTopAndBottom);

			if (rebound) {

				if (trackballEvent && firstVis < nHeaders && lastVis >= totalItems) {
					trackballEvent = false;
					rebound = false;
					return;
				}

				if (firstVis < nHeaders) {

					// hack to avoid strange behaviour when there aren't enough items to fill the entire listview
					if (lastVis >= totalItems) {
						smoothScrollBy(0, 0);
						rebound = false;
						recalcV = false;
						velocity = 0;
					}

					if (recalcV) {
						recalcV = false;
						velocity /= (1.0f + ((System.currentTimeMillis() - flingTimestamp) / 1000.0f));
					}
					if (firstVis == nHeaders) {
						recalcV = false;
					}
					measure = getChildAt(nHeaders);
					try {
						if (measure.getTop() + velocity < divHeight) {
							velocity *= BOUNCEBRAKE;
							if (!bounce || Math.abs(velocity) < 1.0f) {
								rebound = false;
								recalcV = false;
								velocity = 0;
							} else {
								setSelectionFromTop(nHeaders, divHeight - 1);
							}
						}
					} catch (Exception e) {
						if (velocity > 0) velocity = -velocity;
					}
					if (rebound) {
						smoothScrollBy((int) -velocity, 0);
						if (velocity > BREAKSPEED) velocity /= BREAKSPEED;
						else velocity -= BREAKSPEED;
					}

				} else if (lastVis >= totalItems) {

					if (recalcV) {
						recalcV = false;
						velocity /= (1.0f + ((System.currentTimeMillis() - flingTimestamp) / 1000.0f));
					}
					if (lastVis == totalItems - nHeaders - nFooters) {
						rebound = false;
						recalcV = false;
						velocity = 0;
					} else {
						measure = getChildAt(visibleCnt - nHeaders - nFooters);
						try {
							if (measure.getBottom() + velocity > getHeight() - divHeight) {
								velocity *= BOUNCEBRAKE;
								if (!bounce || Math.abs(velocity) < 1.0f) {
									rebound = false;
									recalcV = false;
									velocity = 0;
								} else {
									setSelectionFromTop(lastVis - nHeaders - nFooters, getHeight() - divHeight - measure.getHeight() + 1);
								}
							}
						} catch (Exception e) {
							if (velocity < 0) velocity = -velocity;
						}
					}
					if (rebound) {
						smoothScrollBy((int) -velocity, 0);
						if (velocity < -BREAKSPEED) velocity /= BREAKSPEED;
						else velocity += BREAKSPEED;
					}

				} else if (scrollstate == OnScrollListener.SCROLL_STATE_IDLE) {

					rebound = false;
					recalcV = false;
					velocity = 0;
				}
				mHandler.postDelayed(checkListviewTopAndBottom, delay);
				return;
			}

			if (scrollstate != OnScrollListener.SCROLL_STATE_IDLE) return;

			if (totalItems == (nHeaders + nFooters) || firstVis < nHeaders) {
				setSelectionFromTop(nHeaders, divHeight);
				smoothScrollBy(0, 0);
			} else if (lastVis == totalItems) {
				int offset = getHeight() - divHeight;
				measure = getChildAt(visibleCnt - nHeaders - nFooters);
				if (measure != null) offset -= measure.getHeight();
				setSelectionFromTop(lastVis - nHeaders - nFooters, offset);
				smoothScrollBy(0, 0);
			}
		}
	};
}
