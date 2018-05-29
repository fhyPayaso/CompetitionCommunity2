package cn.abtion.neuqercc.home.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.abtion.neuqercc.R;
import cn.abtion.neuqercc.base.fragments.BaseFragment;
import cn.abtion.neuqercc.home.models.RaidersAndDetailsRequest;

/**
 * @author fhyPayaso
 * @since 2017/11/7 16:40
 * email fhyPayaso@qq.com
 */

public class RaidersFragment extends BaseFragment {
    @BindView(R.id.img_raiders)
    ImageView imgRaiders;
    @BindView(R.id.txt_raiders)
    TextView txtRaiders;

    public void setData(RaidersAndDetailsRequest data) {
        this.data = data;
    }

    private RaidersAndDetailsRequest data;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_raiders;
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void initView() {
        Glide.with(RaidersFragment.this).load(data.getGod_pic()).into(imgRaiders);
        txtRaiders.setText(data.getGod_desc());
    }
    @Override
    protected void loadData() {

    }

}
