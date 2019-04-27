package cn.abtion.neuqercc.message.fragments;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.Unbinder;
import cn.abtion.neuqercc.R;
import cn.abtion.neuqercc.account.activities.LoginActivity;
import cn.abtion.neuqercc.base.fragments.BaseFragment;
import cn.abtion.neuqercc.common.Config;
import cn.abtion.neuqercc.message.activities.ChatWindowActivity;
import cn.abtion.neuqercc.message.adapters.FriendItemListener;
import cn.abtion.neuqercc.message.adapters.MessageRecAdapter;
import cn.abtion.neuqercc.message.models.FriendModel;
import cn.abtion.neuqercc.message.models.MessageModel;
import cn.abtion.neuqercc.network.APIResponse;
import cn.abtion.neuqercc.network.DataCallback;
import cn.abtion.neuqercc.network.RestClient;
import cn.abtion.neuqercc.utils.DateUtils;
import cn.abtion.neuqercc.widget.SwipeItemLayout;
import retrofit2.Call;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static cn.abtion.neuqercc.utils.Utility.runOnUiThread;

/**
 * @author fhyPayaso
 * @since 2018/1/7 on 上午12:51
 * fhyPayaso@qq.com
 */
public class ChatListFragment extends BaseFragment implements FriendItemListener, SwipeRefreshLayout
        .OnRefreshListener, EMMessageListener {


    @BindView(R.id.message_rec)
    RecyclerView mMessageRec;
    Unbinder unbinder;
    @BindView(R.id.ly_refresh_message)
    SwipeRefreshLayout lyRefreshMessage;

    private MessageRecAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<FriendModel> userList;
    private List<MessageModel> messageModelList;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void initVariable() {
        messageModelList = new ArrayList<>();
        userList = new ArrayList<>();
    }

    @Override
    protected void initView() {


        EMClient.getInstance().chatManager().addMessageListener(this);

        initRec();
        initSwipe();
    }

    @Override
    protected void loadData() {

    }


    private void initSwipe() {

        lyRefreshMessage.setRefreshing(true);
        lyRefreshMessage.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        lyRefreshMessage.setOnRefreshListener(this);
        onRefresh();
    }


    @Override
    public void onResume() {
        super.onResume();

        Log.i(TAG, "onResume: 重新进入了这个页面");


        onRefresh();
    }

    private void initRec() {

        mAdapter = new MessageRecAdapter(getContext(), messageModelList);
        mAdapter.setFriendItemListener(this);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mMessageRec.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(getContext()));
        mMessageRec.setLayoutManager(mLayoutManager);
        mMessageRec.setAdapter(mAdapter);
    }


    @Override
    public void onSendMessageClick(int pos) {
        ChatWindowActivity.startActivity(getContext()
                , messageModelList.get(pos).getPhoneNumber()
                , messageModelList.get(pos).getUserName());

    }

    @Override
    public void onDeleteClick(int pos) {

        mAdapter.removeItem(pos);
    }

    @Override
    public void onRefresh() {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        loadChatUser();
                    }
                });
            }
        }, Config.SWIPE_REFRESH_DELAY_TIME);
    }


    /**
     * 先获取好友列表
     */
    private void loadChatUser() {


        RestClient.getService().loadFriendList(LoginActivity.phoneNumber).enqueue(new DataCallback<APIResponse<List<FriendModel>>>() {

            @Override
            public void onDataResponse(Call<APIResponse<List<FriendModel>>> call,
                                       Response<APIResponse<List<FriendModel>>> response) {


                userList.clear();
                messageModelList.clear();
                userList = response.body().getData();

                if (userList != null) {
                    for (int i = 0; i < userList.size(); i++) {
                        loadMessageInfo(i);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDataFailure(Call<APIResponse<List<FriendModel>>> call, Throwable t) {


            }

            @Override
            public void dismissDialog() {

                lyRefreshMessage.setRefreshing(false);
            }
        });


    }


    /**
     * 获取相对每一个用户的聊天信息
     */
    private void loadMessageInfo(int pos) {

        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(userList.get(pos).getPhone
                ());

        if (conversation != null) {
            EMMessage lastMessage = conversation.getLastMessage();

            if (lastMessage != null) {
                EMTextMessageBody textMessageBody = (EMTextMessageBody) lastMessage.getBody();
                long messageTime = lastMessage.getMsgTime();

                messageModelList.add(new MessageModel(userList.get(pos).getPic()
                        , userList.get(pos).getUsername()
                        , textMessageBody.getMessage()
                        , DateUtils.stampToDate(String.valueOf(messageTime))
                        , userList.get(pos).getPhone()));
            }
        }
    }

    @Override
    public void onAvaterClick(int pos) {

    }


    @Override
    public void onMessageReceived(List<EMMessage> messages) {

        Log.i(TAG, "onMessageReceived: 接收到了消息");

        onRefresh();
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {

    }

    @Override
    public void onMessageRead(List<EMMessage> messages) {

    }

    @Override
    public void onMessageDelivered(List<EMMessage> messages) {

    }

    @Override
    public void onMessageChanged(EMMessage message, Object change) {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        EMClient.getInstance().chatManager().removeMessageListener(this);
    }
}
