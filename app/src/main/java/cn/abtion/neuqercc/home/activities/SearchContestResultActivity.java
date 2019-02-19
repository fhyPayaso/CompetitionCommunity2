package cn.abtion.neuqercc.home.activities;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.abtion.neuqercc.R;
import cn.abtion.neuqercc.base.activities.ToolBarActivity;
import cn.abtion.neuqercc.base.adapters.BaseRecyclerViewAdapter;
import cn.abtion.neuqercc.home.adapters.HomeAdapter;
import cn.abtion.neuqercc.home.models.ContestListModel;
import cn.abtion.neuqercc.home.models.InitContestRecylerViewItemRequest;
import cn.abtion.neuqercc.home.models.SearchContestNameRequest;
import cn.abtion.neuqercc.home.models.SearchResultContestModel;
import cn.abtion.neuqercc.network.APIResponse;
import cn.abtion.neuqercc.network.DataCallback;
import cn.abtion.neuqercc.network.RestClient;
import cn.abtion.neuqercc.team.activities.SearchTeamResultActivity;
import cn.abtion.neuqercc.team.activities.TeamInformationActivity;
import cn.abtion.neuqercc.team.adapters.SearchTeamResultAdapter;
import cn.abtion.neuqercc.team.models.AllTeamListModel;
import cn.abtion.neuqercc.team.models.SearchResultTeamModel;
import cn.abtion.neuqercc.utils.ToastUtil;
import cn.abtion.neuqercc.widget.CustomLinearLayoutManager;
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
    private LinearLayoutManager linearLayoutManager=new LinearLayoutManager(SearchContestResultActivity.this, LinearLayoutManager.VERTICAL, false);


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



    public void loadSearchResult() {


//        Intent intent = getIntent();
//        String searchContestName = intent.getStringExtra("searchContestName");


        SearchContestNameRequest searchContestNameRequest = new SearchContestNameRequest();
        searchContestNameRequest.setContent("毛球");


        RestClient.getService().searchContest(searchContestNameRequest).enqueue(new DataCallback<APIResponse<List<InitContestRecylerViewItemRequest>>>() {
            @Override
            public void onDataResponse(Call<APIResponse<List<InitContestRecylerViewItemRequest>>> call, Response<APIResponse<List<InitContestRecylerViewItemRequest>>> response) {

                initContestRecylerViewItemRequests = response.body().getData();



                ToastUtil.showToast("akjfnajk");

            }

            @Override
            public void onDataFailure(Call<APIResponse<List<InitContestRecylerViewItemRequest>>> call, Throwable t) {

            }

            @Override
            public void dismissDialog() {

            }
        });







    }


}
