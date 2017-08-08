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
		/** �����ƶ��ĵ�λ���ؾ��� **/
		private final int POSITIVE_MOVE_Y = 1;
		/** �����ƶ��ĵ�λ���ؾ��� **/
		private final int NAGATIVE_MOVE_Y = -1;
		/** �ƶ���λ������Ҫ˯�ߵ�ʱ�� **/
		private final int SLEEP_EVERY_TIME = 1;
		/** �����ƶ���Ϣ��־ **/
		private final int POSITIVE_MARK = 0;
		/** �����ƶ���Ϣ��־ **/
		private final int NAGATIVE_MARK = 1;
		/** ��ǰһ�β�����Ҫ�ƶ����ܾ��� **/
		private int range;
		/** �Ƿ�����ScrollView�Ĵ����¼� 1��ʾ���� 0��ʾ������ **/
		private int state = 0;
		/** �����ǵ�ScrollView�����Զ�������ʱ�� ���ǲ��ܿ����µ��߳��ٴ������Զ����� ����Ҫ��һ������������ Ĭ����û�������Զ����� **/
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
	            
	          //��ʱ�ľ���ͼ�¼�µľ��벻��ȣ��ڸ�5�����handler������Ϣ  
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
//   * ���ù����ӿ� 
//   * @param onScrollListener 
//   */  
  public void setOnScrollchangeListeners(OnScrollChangedListeners onscrollchangedlistener) {  
      this.onscrollchangedlistener = onscrollchangedlistener;  
  }  

  /** 
   * ���ù����ӿ� 
   * @param onScrollListener 
   */  
  public void setOnScrollListener(OnScrollListener onScrollListener) {  
      this.onScrollListener = onScrollListener;  
  } 
  
  public interface OnScrollChangedListeners{  
      /** 
       * �ص������� ����MyScrollView������Y������� 
       * @param scrollY 
       *              �� 
       */  
      public void onScrollChanged(int l, int t, int oldl, int oldt);
  } 
  /** 
   * �����û���ָ�뿪MyScrollView��ʱ���ȡMyScrollView������Y���룬Ȼ��ص���onScroll������ 
   */  
//  private Handler handler = new Handler() {  
//
//      public void handleMessage(android.os.Message msg) {  
//          int scrollY = PullToRefreshScrollView.this.getScrollY();  
//            
//          //��ʱ�ľ���ͼ�¼�µľ��벻��ȣ��ڸ�5�����handler������Ϣ  
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
   * ��дonTouchEvent�� ���û�������MyScrollView�����ʱ�� 
   * ֱ�ӽ�MyScrollView������Y�������ص���onScroll�����У����û�̧���ֵ�ʱ�� 
   * MyScrollView���ܻ��ڻ��������Ե��û�̧�������Ǹ�5�����handler������Ϣ����handler���� 
   * MyScrollView�����ľ��� 
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
   * �����Ļص��ӿ� 
   *  
   * @author xiaanming 
   * 
   */  
  public interface OnScrollListener{  
      /** 
       * �ص������� ����MyScrollView������Y������� 
       * @param scrollY 
       *              �� 
       */  
      public void onScroll(int scrollY);  
  }  
  
	public void roreydiuAotuScroll(final int moveToY) {
		if (moveToY < 0) {
			// Toast.makeText(getContext(), "�������λ�ò��Ϸ�", 0).show();
			return;
		}
		if (moveToY == PullToRefreshScrollView.this.getScrollY()) {
			// Toast.makeText(getContext(), "����һ�ε�λ����ͬ", 0).show();
			return;
		}
		if (isScrolling == true) {
			// Toast.makeText(getContext(), "�����Զ�����", 0).show();
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					isScrolling = true;
					state = 1;
					if ((moveToY - PullToRefreshScrollView.this.getScrollY()) >= 0) {
						/** ����ֻ���ҵ���ͼ�ܸ߶���10500 ��Ϊ�ҵ��ֻ������ܶ���3.0�� ����ֻ��һ���򵥵���ʾ û��д�淶 **/
						// �����Ŀ���ǵ������ֵ����ʱ �ܹ���ʱ�ж� �����߳���һЩ��������ù��� ��Ϊ�Ѿ���ͻ����˵ײ�
						// Ҳ���ⴥ���¼��ĳ������� ʵ�������в���������������� ���Բ�������ж�Ҳ��û�й�ϵ��
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
					isScrolling = false;// ������Ͽ����ٴο���
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
  
	/** ���߳̿����ƶ���ʱ�� ����ScrollView�Ĵ����¼� ���߳̽�����ʱ���ٴδ� ����һ�Ǳ����߳� ���Ǳ�֤�ƶ���λ��׼ȷ�� **/
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
