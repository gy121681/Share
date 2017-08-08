package com.td.qianhai.epay.oem.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.share.app.entity.response.ActivationdeListResponse;
import com.share.app.entity.response.Constans;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.views.CircleImageView;

/**
 * Created by Snow on 2017/7/25.
 */

public class ActivatedCodeAdapter extends QuickAdapter<ActivationdeListResponse> {
    @Override
    protected void fillView(QuickAdapterHolder holder, int pos, ActivationdeListResponse data) {
        CircleImageView img = holder.findViewById(R.id.img_photo);
        TextView tvName = holder.findViewById(R.id.tv_name);
        TextView tvPhone = holder.findViewById(R.id.tv_phone);
        ImageView imgLabel = holder.findViewById(R.id.img_label);
        Glide.with(img.getContext()).load(data.getPhoto()).error(R.drawable.share_s_public_head_small_big).into(img);
        tvName.setText(data.getMercnam());
        tvPhone.setText(data.getMobile());
        if (Constans.MemberLevel.LEVEL_NORMAL.equals(data.getLevel())) {
            imgLabel.setImageResource(R.drawable.share_s_homepage_member_senior1);
        } else if (Constans.MemberLevel.LEVEL_VIP.equals(data.getLevel())) {
            imgLabel.setImageResource(R.drawable.share_s_homepage_member_senior);
        }
    }

    @Override
    protected int getLayoutId(int type) {
        return R.layout.item_activated_code;
    }
}
