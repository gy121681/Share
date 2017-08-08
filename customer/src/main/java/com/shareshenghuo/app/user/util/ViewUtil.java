package com.shareshenghuo.app.user.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.TipsActivity;
import com.shareshenghuo.app.user.manager.ImageLoadManager;
import com.shareshenghuo.app.user.util.Arith;

public class ViewUtil {

    /**
     * 展示欢迎图片
     *
     * @param imgRes    图片ID
     * @param delayTime 时间
     */
    public static void showLuancherImage(Context cxt, int imgRes, int delayTime) {
        final WindowManager mWindows = (WindowManager) cxt
                .getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        wmParams.type = 2002;
        wmParams.flags = 8;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // wmParams.windowAnimations = android.R.style.Animation_Toast;
        wmParams.width = -1;
        wmParams.height = -1;
        final View layoutLuanch = new View(cxt);
        layoutLuanch.setBackgroundResource(imgRes);
        mWindows.addView(layoutLuanch, wmParams);
        Handler mHandler = new Handler() {
            @Override
			public void handleMessage(android.os.Message msg) {
                try {
                    mWindows.removeView(layoutLuanch);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.gc();
            }

            ;
        };
        mHandler.sendEmptyMessageDelayed(2, 3000);
    }

    /**
     * 计算ListView的高度
     */
    public static void setListViewHeightBasedOnChildren(ListView listView,
                                                        int attHeight) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1))
                + attHeight;
        listView.setLayoutParams(params);
    }

    /**
     * 计算GridView的高度
     */
    public static void setGridViewHeightBasedOnChildren(GridView gridView) {
        // 获取GridView对应的Adapter
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int rows;
        int columns = 0;
        int horizontalBorderHeight = 0;
        Class<?> clazz = gridView.getClass();
        try {
            // 利用反射，取得每行显示的个数
            Field column = clazz.getDeclaredField("mRequestedNumColumns");
            column.setAccessible(true);
            columns = (Integer) column.get(gridView);
            // 利用反射，取得横向分割线高度
            Field horizontalSpacing = clazz
                    .getDeclaredField("mRequestedHorizontalSpacing");
            horizontalSpacing.setAccessible(true);
            horizontalBorderHeight = (Integer) horizontalSpacing.get(gridView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 判断数据总数除以每行个数是否整除。不能整除代表有多余，需要加一行
        if (listAdapter.getCount() % columns > 0) {
            rows = listAdapter.getCount() / columns + 1;
        } else {
            rows = listAdapter.getCount() / columns;
        }
        int totalHeight = 0;
        for (int i = 0; i < rows; i++) { // 只计算每项高度*行数
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + horizontalBorderHeight * (rows - 1);// 最后加上分割线总高度
        gridView.setLayoutParams(params);
    }

    /**
     * 展示EditText的错误信息
     */
    public static void showEditError(EditText v, String msg) {
        v.requestFocus();
        v.setHint(Html.fromHtml("<font color=#B2001F>" + msg + "</font>"));
//        v.setError(Html.fromHtml("<font color=#B2001F>" + "" + "</font>"));
//        v.setError(“);
    }
    
    public static void showError(EditText v, String msg) {
        v.setText("");
        v.setHint(Html.fromHtml("<font color=#B2001F>" + msg + "</font>"));
//        v.setError(Html.fromHtml("<font color=#B2001F>" + msg + "</font>"));
    }

    /**
     * 判断EditText内容是否非空，为空显示提醒
     * false 非空      true 空
     */
    public static boolean checkEditEmpty(EditText v, String msg) {
        if(TextUtils.isEmpty(v.getText())) {
            showEditError(v, msg);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 展示EditText自带的清空按钮
     */
    public static void setEditWithClearButton(final EditText edt,
                                              final int imgRes) {

        edt.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Drawable[] drawables = edt.getCompoundDrawables();
                if (hasFocus && edt.getText().toString().length() > 0) {
                    edt.setTag(true);

                    edt.setCompoundDrawablesWithIntrinsicBounds(drawables[0],
                            drawables[1], edt.getContext().getResources()
                                    .getDrawable(imgRes), drawables[3]);

                } else {
                    edt.setTag(false);
                    edt.setCompoundDrawablesWithIntrinsicBounds(drawables[0],
                            drawables[1], null, drawables[3]);
                }
            }
        });
        final int padingRight = Dip2Px(edt.getContext(), 50);
        edt.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        int curX = (int) event.getX();
                        if (curX > v.getWidth() - padingRight
                                && !TextUtils.isEmpty(edt.getText())) {
                            if (edt.getTag() != null && (Boolean) edt.getTag()) {
                                edt.setText("");
                                int cacheInputType = edt.getInputType();
                                edt.setInputType(InputType.TYPE_NULL);
                                edt.onTouchEvent(event);
                                edt.setInputType(cacheInputType);
                                return true;
                            } else {
                                return false;
                            }
                        }
                        break;
                }
                return false;
            }
        });

        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                Drawable[] drawables = edt.getCompoundDrawables();
                if (edt.getText().toString().length() == 0) {
                    edt.setTag(false);
                    edt.setCompoundDrawablesWithIntrinsicBounds(drawables[0],
                            drawables[1], null, drawables[3]);

                } else {
                    edt.setTag(true);
                    edt.setCompoundDrawablesWithIntrinsicBounds(drawables[0],
                            drawables[1], edt.getContext().getResources()
                                    .getDrawable(imgRes), drawables[3]);

                }
            }
        });

    }

    /**
     * 获取视图的宽度
     */
    public static int getViewWidth(View view) {
        int measure = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(measure, measure);
        return view.getMeasuredWidth();
    }

    /**
     * 获取视图的高度
     */
    public static int getViewHeight(View view) {
        int measure = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(measure, measure);
        return view.getMeasuredHeight();
    }

    /**
     * dip to px
     */
    public static int Dip2Px(Context c, int dipValue) {
        float scale = c.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5F);
    }

    public static void setPhotoList(final Context context, LinearLayout layout, String photoUrls) {
    	if(!TextUtils.isEmpty(photoUrls)) {
			List<String> pics = Arrays.asList(photoUrls.split(","));
			if(pics!=null && pics.size()>0) {
				for(String item : pics) {
					final ImageView img = new ImageView(context);
					img.setScaleType(ScaleType.CENTER_CROP);
					layout.addView(img);
					ImageLoadManager.getInstance(context).loadImage(item, new ImageLoadingListener() {
						@Override
						public void onLoadingStarted(String arg0, View arg1) {
						}
						
						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						}
						
						@Override
						public void onLoadingComplete(String arg0, View arg1, Bitmap bm) {
							double h = Double.valueOf(bm.getHeight());
							double w = Double.valueOf(bm.getWidth());
							double scale = Arith.div(h, w);
							int height = (int) Arith.mul(Double.valueOf(BitmapTool.getScreenWidthPX(context)), scale);
							LayoutParams lp = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, height);
							img.setLayoutParams(lp);
							img.setImageBitmap(bm);
						}
						
						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
						}
					});
				}
			}
		}
    }
    
    public static  void setbank(ImageView v,String bank){
		
		if (bank!= null&&bank.length()>=2) {
			if (bank.contains("招商")) {
				v.setImageResource(R.drawable.ps_cmb);
			}else if(bank.contains("农业")) {
				v.setImageResource(R.drawable.ps_abc);
			}else if(bank.contains("农行")){
				v.setImageResource(R.drawable.ps_abc);
			}else if(bank.contains("北京")){
				v.setImageResource(R.drawable.ps_bjb);
			}else if(bank.equals("中国银行")){
//				if(bank.substring(0,4).equals("中国银行")){
//					v.setImageResource(R.drawable.ps_ccb);
//				}else{
					v.setImageResource(R.drawable.ps_boc);
//				}
				
			}else if(bank.contains("建设")){
				v.setImageResource(R.drawable.ps_ccb);
			}else if(bank.contains("光大")){
				v.setImageResource(R.drawable.ps_cebb);
			}else if(bank.contains("兴业")){
				v.setImageResource(R.drawable.ps_cib);
			}else if(bank.contains("中信")){
				v.setImageResource(R.drawable.ps_citic);
			}else if(bank.contains("民生")){
				v.setImageResource(R.drawable.ps_cmbc);
			}else if(bank.contains("交通")){
				v.setImageResource(R.drawable.ps_comm);
			}else if(bank.contains("华夏")){
				v.setImageResource(R.drawable.ps_hxb);
			}else if(bank.contains("广东发展")){
				v.setImageResource(R.drawable.ps_gdb);
			}else if(bank.contains("广发")){
				v.setImageResource(R.drawable.ps_gdb);
			}else if(bank.contains("邮政")){
				v.setImageResource(R.drawable.ps_psbc);
			}else if(bank.contains("邮储")){
				v.setImageResource(R.drawable.ps_psbc);
			}else if(bank.contains("工商")){
				v.setImageResource(R.drawable.ps_icbc);
			}else if(bank.contains("平安")){
				v.setImageResource(R.drawable.ps_spa);
			}else if(bank.contains("浦东")){
				v.setImageResource(R.drawable.ps_spdb);
			}else if(bank.contains("浦发")){
				v.setImageResource(R.drawable.ps_spdb);
			}else if(bank.contains("江苏")){
				v.setImageResource(R.drawable.js_spdb);
			}else if(bank.contains("工商")){
				v.setImageResource(R.drawable.ps_icbc);
			}else if(bank.contains("上海")){
				v.setImageResource(R.drawable.ps_sh);
			}else if(bank.contains("深圳")){
				v.setImageResource(R.drawable.sz_sh);
			}else if(bank.contains("深发")){
				v.setImageResource(R.drawable.sz_sh);
			}else if(bank.contains("中信")){
				v.setImageResource(R.drawable.zx_sh);
			}else{
				v.setImageResource(R.drawable.ps_unionpay);
			}
			
		}else{
			v.setImageResource(R.drawable.ps_unionpay);
		}
		
		
	}
    static int h = 0;
    public static void showtip(final Context context,final View view,int tag,final int hights,final String tags,final int tpis, final View view2){
    	
 		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
 			h=hights;
		}
    	
    	if(tag==0){
    		view.postDelayed(new Runnable() {
                @Override
                public void run() {
                          int hight = view.getHeight()+h;
                          int left = view.getLeft()-15;
                          int right = view.getRight();
                          int top = view.getTop()+h;
                          int bottom = view.getBottom()+h;
                          int loc[] = {left,top,right,bottom,hight,tpis,0};
                          Intent intent = new Intent(context,TipsActivity.class);
                          intent.putExtra("loc",loc);
                          ((Activity) context).startActivityForResult(intent, 100);
                          PreferenceUtils.setPrefBoolean(context, tags, true);
                }
            },500);
    	}else if(tag==2){
			view.postDelayed(new Runnable() {
	            @Override
	            public void run() {
	                     
	                      int right = view.getWidth()/2;
	                      int left = view.getLeft();
	                      int top = view.getTop()+h+view2.getHeight();
	                      int bottom = view.getBottom()+h+view2.getHeight();
	                      int hight = top;
	                      int loc[] = {left,top,right,bottom,hight,tpis,1};
	                      Intent intent = new Intent(context,TipsActivity.class);
	                      intent.putExtra("loc",loc);
//	                      context. startActivity(intent);
	                      ((Activity) context).startActivityForResult(intent, 101);
	                      PreferenceUtils.setPrefBoolean(context, tags, true);
//	                  }
//	              });
	            }
	        },500);
    	}else{
	    	
			view.postDelayed(new Runnable() {
	            @Override
	            public void run() {
	                    
	                      int right = view.getWidth()/2;
	                      int left = view.getLeft();
	                      int top = view.getTop()+h;
	                      int bottom = view.getBottom()+h;
	                      int hight = top;
	                      int loc[] = {left,top,right,bottom,hight,tpis,1};
	                      Intent intent = new Intent(context,TipsActivity.class);
	                      intent.putExtra("loc",loc);
//	                      context. startActivity(intent);
	                      ((Activity) context).startActivityForResult(intent, 101);
	                      PreferenceUtils.setPrefBoolean(context, tags, true);
//	                  }
//	              });
	            }
	        },500);
    	}
    	
    }
    
}
