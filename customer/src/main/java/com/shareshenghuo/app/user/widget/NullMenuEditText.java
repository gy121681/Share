//package com.shareshenghuo.app.user.widget;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.ActionMode;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.EditText;
//
//public class NullMenuEditText extends EditText {
//
//    boolean canPaste() {
//        return false;
//    }
//
//    boolean canCut() {
//        return false;
//    }
//
//    boolean canCopy() {
//        return false;
//    }
//
//    boolean canSelectAllText() {
//        return false;
//    }
//
//    boolean canSelectText() {
//        return false;
//    }
//
//    boolean textCanBeSelected() {
//        return false;
//    }
//
//    public NullMenuEditText(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        setLongClickable(false);
//        setTextIsSelectable(false);
//        setCustomSelectionActionModeCallback(new ActionMode.Callback() {
//
//			@Override
//			public boolean onActionItemClicked(ActionMode arg0, MenuItem arg1) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//
//			@Override
//			public boolean onCreateActionMode(ActionMode arg0, Menu arg1) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//
//			@Override
//			public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//
//			@Override
//			public void onDestroyActionMode(ActionMode arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//        });
//
//    }
//
//    @Override
//    public boolean onTextContextMenuItem(int id) {
//        return true;
//    }
//}