/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.handmark.pulltorefresh.library;

import android.annotation.TargetApi;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.ScrollView;

public class PullToRefreshScrollView extends PullToRefreshBase<ScrollView> {
	
	
		private int lastScrollY;  
	 private OnScrollListener onScrollListener;  
	 private  OnScrollChangedListeners onscrollchangedlistener;
	 private boolean istag = false;
		/** 正向移动的单位像素距离 **/
		private final int POSITIVE_MOVE_Y = 1;
		/** 正向移动的单位像素距离 **/
		private final int NAGATIVE_MOVE_Y = -1;
		/** 移动单位距离需要睡眠的时间 **/
		private final int SLEEP_EVERY_TIME = 1;
		/** 正向移动信息标志 **/
		private final int POSITIVE_MARK = 0;
		/** 负向移动信息标志 **/
		private final int NAGATIVE_MARK = 1;
		/** 当前一次操作需要移动的总距离 **/
		private int range;
		/** 是否屏蔽ScrollView的触摸事件 1表示屏蔽 0表示不屏蔽 **/
		private int state = 0;
		/** 当我们的ScrollView正在自动滑动的时候 我们不能开启新的线程再次让其自动滑动 所以要有一个开关来控制 默认是没有正在自动滑动 **/
		private boolean isScrolling;

	public PullToRefreshScrollView(Context context) {
		super(context);
	}

	public PullToRefreshScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToRefreshScrollView(Context context, Mode mode) {
		super(context, mode);
	}

	public PullToRefreshScrollView(Context context, Mode mode, AnimationStyle style) {
		super(context, mode, style);
	}

	@Override
	public final Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}
	
	@Override
	public  void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		if(onscrollchangedlistener==null){
			return;
		}
		try {
			onscrollchangedlistener.onScrollChanged(l, t, oldl, oldt);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	
	@Override
	protected ScrollView createRefreshableView(Context context, AttributeSet attrs) {
		ScrollView scrollView;
		if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
			scrollView = new InternalScrollViewSDK9(context, attrs);
		} else {
			scrollView = new ScrollView(context, attrs);
		}

		scrollView.setId(R.id.scrollview);
		return scrollView;
	}

	@Override
	protected boolean isReadyForPullStart() {
		return mRefreshableView.getScrollY() == 0;
	}

	@Override
	protected boolean isReadyForPullEnd() {
		View scrollViewChild = mRefreshableView.getChildAt(0);
		if (null != scrollViewChild) {
			return mRefreshableView.getScrollY() >= (scrollViewChild.getHeight() - getHeight());
		}
		return false;
	}

	@TargetApi(9)
	final class InternalScrollViewSDK9 extends ScrollView {

		public InternalScrollViewSDK9(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
				int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
			
//	          int scrollYs = PullToRefreshScrollView.this.getScrollY();  
	            
	          //此时的距离和记录下的距离不相等，在隔5毫秒给handler发送消息  
//	          if(lastScrollY != scrollY){  
//	              lastScrollY = scrollY;  
//	              handler.sendMessageDelayed(handler.obtainMessage(), 5);    
//	          }  
//	          if(onScrollListener != null){  
			try {
				  onScrollListener.onScroll(scrollY);  
			} catch (Exception e) {
				// TODO: handle exception
			}
	            
//	          }  
			final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
					scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

			// Does all of the hard work...
			OverscrollHelper.overScrollBy(PullToRefreshScrollView.this, deltaX, scrollX, deltaY, scrollY,
					getScrollRange(), isTouchEvent);

			return returnValue;
		}

		/**
		 * Taken from the AOSP ScrollView source
		 */
		private int getScrollRange() {
			int scrollRange = 0;
			if (getChildCount() > 0) {
				View child = getChildAt(0);
				scrollRange = Math.max(0, child.getHeight() - (getHeight() - getPaddingBottom() - getPaddingTop()));
			}
			return scrollRange;
		}
	}
	
    
//  /** 
//   * 设置滚动接口 
//   * @param onScrollListener 
//   */  
  public void setOnScrollchangeListeners(OnScrollChangedListeners onscrollchangedlistener) {  
      this.onscrollchangedlistener = onscrollchangedlistener;  
  }  

  /** 
   * 设置滚动接口 
   * @param onScrollListener 
   */  
  public void setOnScrollListener(OnScrollListener onScrollListener) {  
      this.onScrollListener = onScrollListener;  
  } 
  
  public interface OnScrollChangedListeners{  
      /** 
       * 回调方法， 返回MyScrollView滑动的Y方向距离 
       * @param scrollY 
       *              、 
       */  
      public void onScrollChanged(int l, int t, int oldl, int oldt);
  } 
  /** 
   * 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中 
   */  
//  private Handler handler = new Handler() {  
//
//      public void handleMessage(android.os.Message msg) {  
//          int scrollY = PullToRefreshScrollView.this.getScrollY();  
//            
//          //此时的距离和记录下的距离不相等，在隔5毫秒给handler发送消息  
//          if(lastScrollY != scrollY){  
//              lastScrollY = scrollY;  
//              handler.sendMessageDelayed(handler.obtainMessage(), 5);    
//          }  
//          if(onScrollListener != null){  
//              onScrollListener.onScroll(scrollY);  
//          }  
//            
//      };  
//
//  };   

  /** 
   * 重写onTouchEvent， 当用户的手在MyScrollView上面的时候， 
   * 直接将MyScrollView滑动的Y方向距离回调给onScroll方法中，当用户抬起手的时候， 
   * MyScrollView可能还在滑动，所以当用户抬起手我们隔5毫秒给handler发送消息，在handler处理 
   * MyScrollView滑动的距离 
   */  
//  @Override  
//  public boolean onTouchEvent(MotionEvent ev) {  
//      if(onScrollListener != null){  
//          onScrollListener.onScroll(lastScrollY = this.getScrollY());  
//      }  
//      switch(ev.getAction()){  
//      case MotionEvent.ACTION_UP:  
//           handler.sendMessageDelayed(handler.obtainMessage(), 5);    
//          break;  
//      }  
//      return super.onTouchEvent(ev);  
//  }  


  /** 
   *  
   * 滚动的回调接口 
   *  
   * @author xiaanming 
   * 
   */  
  public interface OnScrollListener{  
      /** 
       * 回调方法， 返回MyScrollView滑动的Y方向距离 
       * @param scrollY 
       *              、 
       */  
      public void onScroll(int scrollY);  
  }  
  
	public void roreydiuAotuScroll(final int moveToY) {
		if (moveToY < 0) {
			// Toast.makeText(getContext(), "您输入的位置不合法", 0).show();
			return;
		}
		if (moveToY == PullToRefreshScrollView.this.getScrollY()) {
			// Toast.makeText(getContext(), "与上一次的位置相同", 0).show();
			return;
		}
		if (isScrolling == true) {
			// Toast.makeText(getContext(), "正在自动滑动", 0).show();
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					isScrolling = true;
					state = 1;
					if ((moveToY - PullToRefreshScrollView.this.getScrollY()) >= 0) {
						/** 这里只是我的视图总高度是10500 因为我的手机像素密度是3.0的 做的只是一个简单的演示 没有写规范 **/
						// 这里的目的是当输入的值过大时 能够即时判断 不让线程做一些过多的无用工作 因为已经早就滑到了底部
						// 也避免触摸事件的长久屏蔽 实际运用中不会出现这样的问题 所以不加入此判断也是没有关系的
						if (moveToY > 10500) {
							range = 10500 - PullToRefreshScrollView.this.getScrollY();
						} else {
							range = (int) (moveToY - PullToRefreshScrollView.this
									.getScrollY());
						}
						for (int i = 0; i < range; i++) {
							Thread.sleep(SLEEP_EVERY_TIME);
							Log.i("===", POSITIVE_MOVE_Y + "");
							handler1.sendEmptyMessage(POSITIVE_MARK);
						}
					} else {
						range = (int) (PullToRefreshScrollView.this.getScrollY() - moveToY);
						for (int i = 0; i < range; i++) {
							Thread.sleep(SLEEP_EVERY_TIME);
							handler1.sendEmptyMessage(NAGATIVE_MARK);
						}
					}
					state = 0;
					isScrolling = false;// 滑动完毕可以再次开启
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
  
	/** 当线程开启移动的时候 屏蔽ScrollView的触摸事件 当线程结束的时候再次打开 这样一是保护线程 二是保证移动的位置准确性 **/
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (state == 0) {
			
			switch (ev.getAction()) {
			case MotionEvent.ACTION_MOVE: 
					
//					float newScrollValue = Math.round(Math.max(initialMotionValue - lastMotionValue, 0) / FRICTION);
					int mLastMotionY = (int) ev.getY();
					int mLastMotionX = (int) ev.getX();
					
//					onScrollListener.onScroll(mLastMotionY);  
				break;
				
			}
			
			return super.onTouchEvent(ev);
		} else {
			return false;
		}

	}
	private Handler handler1 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case POSITIVE_MARK:
				PullToRefreshScrollView.this.scrollBy(0, POSITIVE_MOVE_Y);
				break;
			case NAGATIVE_MARK:
				PullToRefreshScrollView.this.scrollBy(0, NAGATIVE_MOVE_Y);
				break;
			}
		}
	};
  
}
