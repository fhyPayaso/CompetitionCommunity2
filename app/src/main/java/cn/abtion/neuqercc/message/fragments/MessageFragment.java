package cn.abtion.neuqercc.message.fragments;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.abtion.neuqercc.R;
import cn.abtion.neuqercc.base.fragments.BaseFragment;
import cn.abtion.neuqercc.message.activities.SearchUserActivity;
import cn.abtion.neuqercc.message.adapters.MessageViewPagerAdapter;

/**
 * @author abtion.
 * @since 17/9/24 上午2:15.
 * email caiheng@hrsoft.net.
 */

public class MessageFragment extends BaseFragment {


    @BindView(R.id.table_header_message)
    TabLayout tableHeaderMessage;
    @BindView(R.id.viewpager_message)
    ViewPager viewpagerMessage;
    Unbinder unbinder;
    Unbinder unbinder1;

    private TabLayout.Tab mTabChat;
    private TabLayout.Tab mTabFriends;
    private TabLayout.Tab mTabGroup;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initVariable() {


        MessageViewPagerAdapter messageViewPagerAdapter = new MessageViewPagerAdapter(getFragmentManager());
        viewpagerMessage.setAdapter(messageViewPagerAdapter);
        tableHeaderMessage.setupWithViewPager(viewpagerMessage);
        mTabChat = tableHeaderMessage.getTabAt(0);
        mTabFriends = tableHeaderMessage.getTabAt(1);
        mTabGroup = tableHeaderMessage.getTabAt(2);

    }

    @Override
    protected void initView() {


        mTabChat.setIcon(R.drawable.img_chat_selector);
        mTabFriends.setIcon(R.drawable.img_friends_selector);
        mTabGroup.setIcon(R.drawable.img_group_selector);


    }

    @Override
    protected void loadData() {

    }


    /**
     * 跳转到搜索用户界面
     */
    @OnClick(R.id.img_add_friends)
    public void onViewClicked() {
        SearchUserActivity.startActivity(getContext());
    }
}
