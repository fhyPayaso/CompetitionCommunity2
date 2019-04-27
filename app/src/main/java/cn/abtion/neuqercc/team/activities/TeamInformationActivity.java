package cn.abtion.neuqercc.team.activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.abtion.neuqercc.NEUQerCCApplication;
import cn.abtion.neuqercc.R;
import cn.abtion.neuqercc.account.activities.LoginActivity;
import cn.abtion.neuqercc.base.activities.ToolBarActivity;
import cn.abtion.neuqercc.common.constants.CacheKey;
import cn.abtion.neuqercc.message.models.NoticeModel;
import cn.abtion.neuqercc.mine.models.TeamMemberResponse;
import cn.abtion.neuqercc.network.APIResponse;
import cn.abtion.neuqercc.network.DataCallback;
import cn.abtion.neuqercc.network.RestClient;
import cn.abtion.neuqercc.team.adapters.TeamMemberListAdapter;
import cn.abtion.neuqercc.team.models.AllTeamListModel;
import cn.abtion.neuqercc.utils.ToastUtil;
import cn.abtion.neuqercc.widget.CustomLinearLayoutManager;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author lszr
 * @since 2017/11/15 下午5:34
 * email wsyglszr@gmail.com
 */

public class TeamInformationActivity extends ToolBarActivity {


    private List<TeamMemberResponse> teamMemberListModels = new ArrayList<>();
    @BindView(R.id.txt_team_name)
    TextView txtTeamName;
    @BindView(R.id.txt_contest_name)
    TextView txtContestName;
    @BindView(R.id.txt_team_declaration)
    TextView txtTeamDeclaration;
    @BindView(R.id.recylerview_team_member)
    RecyclerView recylerviewTeamMember;

    private String teamName;
    private int teamId;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_team_information;
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void initView() {
        this.setActivityTitle(getString(R.string.title_team_information));

        loadTeamInformation();
    }

    @Override
    protected void loadData() {

    }


    /**
     * 加载队伍信息
     */
    protected void loadTeamInformation() {

        //获得队伍信息
        Intent intent = getIntent();
        String jsonData = intent.getStringExtra("teamInformation");
        AllTeamListModel allTeamListModel = new Gson().fromJson(jsonData, AllTeamListModel.class);
        txtTeamName.setText(allTeamListModel.getTeamName());
        txtContestName.setText(allTeamListModel.getContestName());
        txtTeamDeclaration.setText(allTeamListModel.getDeclaration());

        teamId = allTeamListModel.getId();
        teamName = allTeamListModel.getTeamName();

        RestClient.getService().mineTeamMember(allTeamListModel.getId()).enqueue(new DataCallback<APIResponse<List<TeamMemberResponse>>>() {
            @Override
            public void onDataResponse(Call<APIResponse<List<TeamMemberResponse>>> call,
                                       Response<APIResponse<List<TeamMemberResponse>>> response) {

                teamMemberListModels = response.body().getData();
                initRecyclerView();
            }

            @Override
            public void onDataFailure(Call<APIResponse<List<TeamMemberResponse>>> call, Throwable t) {

            }

            @Override
            public void dismissDialog() {

            }
        });
    }

    protected void initRecyclerView() {

        recylerviewTeamMember.setNestedScrollingEnabled(false);
        TeamMemberListAdapter teamMemberListAdapter = new TeamMemberListAdapter(this, teamMemberListModels);
        recylerviewTeamMember.setLayoutManager(new CustomLinearLayoutManager(this, CustomLinearLayoutManager
                .VERTICAL, false));
        recylerviewTeamMember.setAdapter(teamMemberListAdapter);

    }


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.btn_join_team)
    public void onButtonJoinTeamClicked() {


        View view = View.inflate(TeamInformationActivity.this, R.layout.item_dialog_join_team, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(TeamInformationActivity.this);
        builder.setCancelable(true);

        TextView txtDialogTitle = (TextView) view.findViewById(R.id.txt_dialog_title);
        TextView txtDialogContent = (TextView) view.findViewById(R.id.txt_dialog_content);
        TextView txtLeftButton = (TextView) view.findViewById(R.id.txt_dialog_cancel);
        TextView txtRightButton = (TextView) view.findViewById(R.id.txt_dialog_confirm);

        //  设置Dialog内部文字
        txtDialogTitle.setText(getString(R.string.txt_if_want_join_team));
        txtDialogContent.setText(getString(R.string.txt_dialog_content));
        txtLeftButton.setText(getString(R.string.txt_cancel));
        txtRightButton.setText(getString(R.string.txt_dialog_confirm));


        builder.setView(view);
        final AlertDialog dialog = builder.show();

        //设置点击事件
        txtLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

            }
        });

        txtRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                addNotice();
            }
        });
    }


    /**
     * 向队长发送请求的通知
     */
    private void addNotice() {


        String username = NEUQerCCApplication.getInstance().getCacheUtil().getString(CacheKey.USER_NAME);

        NoticeModel noticeRequest = new NoticeModel(""
                , LoginActivity.phoneNumber
                , null
                , teamId
                , teamName
                , 0
                , username);


        RestClient.getService().addNotice(noticeRequest).enqueue(new DataCallback<APIResponse>() {
            @Override
            public void onDataResponse(Call<APIResponse> call, Response<APIResponse> response) {

                ToastUtil.showToast("发送通知成功");
            }

            @Override
            public void onDataFailure(Call<APIResponse> call, Throwable t) {


                Log.i("", "onDataFailure: "+t.toString());
            }

            @Override
            public void dismissDialog() {

            }
        });
    }
}
