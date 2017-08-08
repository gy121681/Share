package com.shareshenghuo.app.user.widget.dialog;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.ShopTypeListAdapter;
import com.shareshenghuo.app.user.network.bean.CategoryInfo;
import com.shareshenghuo.app.user.widget.dialog.CommonDialog;

/**
 * @author hang
 * 商户分类选择
 */
public class ShopTypeWindow extends CommonDialog implements OnItemClickListener {
	
	private ListView lvParentType, lvChildType;
	private ShopTypeListAdapter parentAdapter, childAdapter;
	
	private List<CategoryInfo> data;
	
	private PickTypeCallback callback;
	
	private Context context;
	
	private int positions	, parentpo = 0, chaildtpo = 0;

	public ShopTypeWindow(Context context, List<CategoryInfo> data,int position) {
		super(context, R.layout.dlg_shop_type, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		CategoryInfo item = new CategoryInfo();
		this.context = context;
		item.id = 0;
		item.type_name = "全部";
		this.positions  = position;
		data.add(0, item);
		this.data = data;

	}
	
	

	@Override
	public void initDlgView() {

		lvParentType = getView(R.id.lvParentType);
		lvChildType = getView(R.id.lvChildType);
		parentAdapter = new ShopTypeListAdapter(context, data);
		
		lvParentType.setAdapter(parentAdapter);
		
		showChildTypeList(positions,-1);
		
		lvParentType.setOnItemClickListener(this);
		lvChildType.setOnItemClickListener(this);
		
	}
	
	public void showChildTypeList(int index ,int childindex) {
		parentAdapter.selectedIndex = index;
//		parentAdapter.setcheckd(index);
		
		Log.e("", ""+data.get(index).type_name);
		CategoryInfo item= new CategoryInfo();
		item.id = 0;
		item.type_name = data.get(index).type_name;
		if(data.get(index).child_shop_type_list!=null&&!data.get(index).child_shop_type_list.get(0).type_name.equals(data.get(index).type_name)){
			data.get(index).child_shop_type_list.add(0,item);
		}

		childAdapter = new ShopTypeListAdapter(context, data.get(index).child_shop_type_list);
		childAdapter.selectedIndex = childindex;
		lvChildType.setAdapter(childAdapter);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	
		if(parent == lvParentType) {
			parentAdapter.selectedIndex = (int)id;
			parentpo =  (int)id;
			
			parentAdapter.notifyDataSetChanged();
			showChildTypeList((int)id,-1);
			if((int)id == 0) {
//				(MainActivity) ShopListFragment.this.getActivity().positions = 0;
				//全部分类
				odismiss();
				if(callback != null)
					callback.onPickedType(parentAdapter.getItem(position), childAdapter.getItem(position),parentpo,chaildtpo);
			}
		} else if(parent == lvChildType) {
			chaildtpo =  (int)id;
			childAdapter.selectedIndex =  (int)id;
			childAdapter.notifyDataSetChanged();
			odismiss();
			if(callback != null){
				callback.onPickedType(parentAdapter.getItem(parentAdapter.selectedIndex), childAdapter.getItem(position),parentpo,chaildtpo);
			}
		}
	}
	
	public void setPickTypeCallback(PickTypeCallback callback) {
		this.callback = callback;
	}
	
	public interface PickTypeCallback {
		public void onPickedType(CategoryInfo parentInfo, CategoryInfo info, int position, int position1);
	}

	@Override
	public void onDismiss() {
		// TODO Auto-generated method stub
		
	}
}
