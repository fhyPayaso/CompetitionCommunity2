package cn.abtion.neuqercc.message.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.abtion.neuqercc.R;
import cn.abtion.neuqercc.base.activities.ToolBarActivity;
import cn.abtion.neuqercc.base.adapters.BaseRecyclerViewAdapter;
import cn.abtion.neuqercc.message.adapters.SearchUseRecAdapter;
import cn.abtion.neuqercc.message.models.SearchUserModel;
import cn.abtion.neuqercc.network.APIResponse;
import cn.abtion.neuqercc.network.DataCallback;
import cn.abtion.neuqercc.network.RestClient;
import cn.abtion.neuqercc.utils.ToastUtil;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author FanHongyu.
 * @since 18/1/24 21:11.
 * email fanhongyu@hrsoft.net.
 */

public class SearchUseResActivity extends ToolBarActivity {


    @BindView(R.id.rec_user)
    RecyclerView recUser;

    private List<SearchUserModel> mUserModelList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_user_res;
    }

    @Override
    protected void initVariable() {

        mUserModelList = new ArrayList<>();
    }

    @Override
    protected void initView() {
        setActivityTitle("搜索结果");
        loadUserList();
    }

    @Override
    protected void loadData() {

    }


    /**
     * 初始化RecyclerView
     */
    private void initRec() {


        SearchUseRecAdapter mAdapter = new SearchUseRecAdapter(SearchUseResActivity.this, mUserModelList);
        recUser.setAdapter(mAdapter);
        recUser.setLayoutManager(new LinearLayoutManager(
                SearchUseResActivity.this, LinearLayoutManager.VERTICAL, false));
        mAdapter.setOnItemClickedListener(new BaseRecyclerViewAdapter.OnItemClicked<SearchUserModel>() {
            @Override
            public void onItemClicked(SearchUserModel searchUserModel, BaseRecyclerViewAdapter.ViewHolder holder) {


                FriendInfoActivity.startActivity(SearchUseResActivity.this, searchUserModel.getPhone());
            }
        });
    }


    /**
     * 加载用户搜索结果
     */
    private void loadUserList() {

        String content = getIntent().getStringExtra("searchUserName");
        RestClient.getService().searchUser(content).enqueue(new DataCallback<APIResponse<List<SearchUserModel>>>() {
            @Override
            public void onDataResponse(Call<APIResponse<List<SearchUserModel>>> call, Response<APIResponse<List<SearchUserModel>>>
                    response) {

                List<SearchUserModel> responseList = response.body().getData();
                mUserModelList.clear();
                mUserModelList.addAll(responseList);
                initRec();
            }

            @Override
            public void onDataFailure(Call<APIResponse<List<SearchUserModel>>> call, Throwable t) {

                ToastUtil.showToast("没有符合条件的用户");
            }

            @Override
            public void dismissDialog() {

            }
        });
    }
}
