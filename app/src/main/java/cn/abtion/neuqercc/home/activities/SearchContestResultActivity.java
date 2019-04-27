package cn.abtion.neuqercc.home.activities;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.abtion.neuqercc.R;
import cn.abtion.neuqercc.base.activities.ToolBarActivity;
import cn.abtion.neuqercc.base.adapters.BaseRecyclerViewAdapter;
import cn.abtion.neuqercc.common.Config;
import cn.abtion.neuqercc.home.adapters.HomeAdapter;
import cn.abtion.neuqercc.home.models.ContestListModel;
import cn.abtion.neuqercc.home.models.InitContestRecylerViewItemRequest;
import cn.abtion.neuqercc.home.models.SearchContestNameRequest;
import cn.abtion.neuqercc.network.APIResponse;
import cn.abtion.neuqercc.network.DataCallback;
import cn.abtion.neuqercc.network.RestClient;
import cn.abtion.neuqercc.utils.ToastUtil;
import cn.abtion.neuqercc.widget.EndLessOnScrollListener;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author lszr
 * @since 2017/12/24 下午12:12
 * email wsyglszr@gmail.com
 */

public class SearchContestResultActivity extends ToolBarActivity {
    @BindView(R.id.recyler_search_team_result)
    RecyclerView recylerSearchTeamResult;

    private List<ContestListModel> searchResultContest = new ArrayList<>();
    private List<InitContestRecylerViewItemRequest> initContestRecylerViewItemRequests = new ArrayList<>();

    private HomeAdapter homeAdapter;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchContestResultActivity.this, LinearLayoutManager.VERTICAL, false);


    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_contest_result;
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void initView() {
        setActivityTitle(getString(R.string.txt_search_result));
    }


    @Override
    protected void loadData() {

        loadSearchResult();

    }


    /**
     * Recylerview的setAdapter和点击事件
     *
     * @param contestListModels
     */
    private void initAdapter(final ArrayList<ContestListModel> contestListModels) {
        homeAdapter = new HomeAdapter(SearchContestResultActivity.this, contestListModels);
        recylerSearchTeamResult.setAdapter(homeAdapter);
        recylerSearchTeamResult.setLayoutManager(linearLayoutManager);

        //点击事件
        homeAdapter.setOnItemClickedListener(new BaseRecyclerViewAdapter.OnItemClicked<ContestListModel>() {

            @Override
            public void onItemClicked(ContestListModel contestListModel, BaseRecyclerViewAdapter.ViewHolder
                    viewHolder) {
                Intent intent = new Intent(SearchContestResultActivity.this, CompetitionActivity.class);
                intent.putExtra("contestName", contestListModel.getTitle());
                intent.putExtra("contestId", contestListModel.getId());
                startActivity(intent);
            }
        });
    }


    public void loadSearchResult() {

        //弹出progressDialog
        progressDialog.setMessage(getString(R.string.dialog_wait_moment));
        progressDialog.show();

        Intent intent = getIntent();
        String searchContestName = intent.getStringExtra("searchContestName");


        SearchContestNameRequest searchContestNameRequest = new SearchContestNameRequest();
        searchContestNameRequest.setContent(searchContestName);


        RestClient.getService().searchContest(searchContestNameRequest).enqueue(new DataCallback<APIResponse<List<InitContestRecylerViewItemRequest>>>() {
            @Override
            public void onDataResponse(Call<APIResponse<List<InitContestRecylerViewItemRequest>>> call, Response<APIResponse<List<InitContestRecylerViewItemRequest>>> response) {
                initContestRecylerViewItemRequests = response.body().getData();

                ArrayList<ContestListModel> list = new ArrayList<>();
                for (InitContestRecylerViewItemRequest request : initContestRecylerViewItemRequests) {
                    list.add(new ContestListModel(
                            request.getId(),
                            request.getName(),
                            request.getShort_desc(),
                            request.getRegistration_time(),
                            request.getCompetition_time()));
                }
                initAdapter(list);

                ToastUtil.showToast("结果" + list.size());
            }

            @Override
            public void onDataFailure(Call<APIResponse<List<InitContestRecylerViewItemRequest>>> call, Throwable t) {

            }

            @Override
            public void dismissDialog() {
                if (progressDialog.isShowing()) {
                    disMissProgressDialog();
                }
            }
        });
    }
}
