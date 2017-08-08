package com.td.qianhai.epay.oem.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.share.app.entity.response.Constans;
import com.share.app.entity.response.SubordinateListResponse;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.mpay.utils.DateUtil;

/**
 * Created by Snow on 2017/7/26.
 */

public class SubMemeberAdapter extends QuickAdapter<SubordinateListResponse> {
    @Override
    protected void fillView(QuickAdapterHolder holder, int pos, final SubordinateListResponse data) {
        ImageView mImgPhoto = holder.findViewById(R.id.user_head_img);
        TextView mTvName = holder.findViewById(R.id.name_tv);
        ImageView mImgLabel = holder.findViewById(R.id.imgs);
        TextView mTvPhone = holder.findViewById(R.id.phonenum_tv);
        TextView mTvDate = holder.findViewById(R.id.date_tv);
        View mBtnCall= holder.findViewById(R.id.call_phone);
        Glide.with(mImgPhoto.getContext()).load(data.getPhoto()).error(R.drawable.share_s_public_head_small_big).into(mImgPhoto);
        mTvName.setText(data.getMercnam());
        mTvPhone.setText(data.getMobile());
        mTvDate.setText(data.getDate());
        if (Constans.MemberLevel.LEVEL_VIP.equals(data.getLevel())) {
            mImgLabel.setImageResource(R.drawable.share_s_homepage_member_senior);
        } else {
            mImgLabel.setImageResource(R.drawable.share_s_homepage_member_senior1);
        }
        mBtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel:"+data.getMobile());
                Intent it = new Intent(Intent.ACTION_DIAL, uri);
                v.getContext().startActivity(it);
            }
        });
    }

    @Override
    protected int getLayoutId(int type) {
        return R.layout.mycircle_list_item2;
    }
}
