package cn.abtion.neuqercc.team.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.abtion.neuqercc.R;
import cn.abtion.neuqercc.account.activities.LoginActivity;
import cn.abtion.neuqercc.base.activities.ToolBarActivity;
import cn.abtion.neuqercc.common.Config;
import cn.abtion.neuqercc.home.models.InitContestRecylerViewDataRequest;
import cn.abtion.neuqercc.home.models.InitContestRecylerViewItemRequest;
import cn.abtion.neuqercc.network.APIResponse;
import cn.abtion.neuqercc.network.DataCallback;
import cn.abtion.neuqercc.network.RestClient;
import cn.abtion.neuqercc.team.models.EstablishTeamRequest;
import cn.abtion.neuqercc.utils.DialogUtil;
import cn.abtion.neuqercc.utils.ToastUtil;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author lszr
 * @since 2017/11/20 下午7:16
 * email wsyglszr@gmail.com
 */

public class EstablishTeamActivity extends ToolBarActivity {
    @BindView(R.id.edit_team_name)
    EditText editTeamName;
    @BindView(R.id.edit_want_join)
    TextView editWantJoin;
    @BindView(R.id.edit_declaration)
    EditText editDeclaration;
    @BindView(R.id.edit_want_good_at)
    EditText editWantGoodAt;

    private String[] stringList;

    public static int page = 1;
    private int flagTempGrade = 0;
    private int flagGrade = -1;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_establish_team;
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void initView() {
        initTitle();

    }

    @Override
    protected void loadData() {

    }


    public void initTitle() {

        setActivityTitle(getString(R.string.title_establish_team));
        setTextOver(getString(R.string.title_over));
        setOnOverTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EstablishTeamRequest establishTeamRequest = new EstablishTeamRequest(
                        LoginActivity.phoneNumber,
                        editTeamName.getText().toString().trim(),
                        editWantJoin.getText().toString().trim(),
                        editDeclaration.getText().toString().trim(),
                        editWantGoodAt.getText().toString().trim()
                );

                if (isInformationCorrect(establishTeamRequest)) {

                    Intent intent = new Intent(EstablishTeamActivity.this, EstablishTeamFinishActivity.class);


                    String gson = new Gson().toJson(establishTeamRequest);

                    intent.putExtra("establishTeamRequest", gson);
                    startActivity(intent);
                }

            }
        });

    }

    private boolean isInformationCorrect(EstablishTeamRequest establishTeamRequest) {
        boolean flag = true;
        if (establishTeamRequest.getGood_at().equals(Config.EMPTY_FIELD) ||
                establishTeamRequest.getCompetition_desc().equals("点击选择比赛") ||
                establishTeamRequest.getDeclaration().equals(Config.EMPTY_FIELD) ||
                establishTeamRequest.getTeam_name().equals(Config.EMPTY_FIELD)) {
            flag = false;
            ToastUtil.showToast("队伍信息不完整，请检查后补充完整");
        }
        return flag;

    }


    @OnClick(R.id.edit_want_join)
    public void onViewClicked() {
        showContestName();


    }

    private void showContestName() {


        //弹出progressDialog
        progressDialog.setMessage(getString(R.string.dialog_wait_moment));
        progressDialog.show();

        //网络请求
        RestClient.getService().initContestRecylerView(page, Config.size).enqueue(new DataCallback<APIResponse<InitContestRecylerViewDataRequest<List<InitContestRecylerViewItemRequest>>>>() {
            @Override
            public void onDataResponse
                    (Call<APIResponse<InitContestRecylerViewDataRequest<List<InitContestRecylerViewItemRequest>>>>
                             call, Response<APIResponse<InitContestRecylerViewDataRequest<List
                            <InitContestRecylerViewItemRequest>>>> response) {


                final List<InitContestRecylerViewItemRequest> list = response.body().getData().getItem();


                if (list != null) {

                    if (progressDialog.isShowing()) {
                        disMissProgressDialog();
                    }


                    stringList = new String[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        stringList[i] = list.get(i).getName();
                    }

                    DialogUtil.NativeDialog nativeDialog = new DialogUtil().new NativeDialog().singleInit
                            (EstablishTeamActivity.this);
                    nativeDialog.setSingleChoice(stringList, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            flagTempGrade = which;
                        }
                    });
                    nativeDialog.setNegativeButton(getString(R.string.dialog_btn_cancel));
                    nativeDialog.setPositiveButton(getString(R.string.dialog_btn_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            flagGrade = flagTempGrade;
                            editWantJoin.setText(list.get(flagGrade).getName());
                        }
                    });

                    nativeDialog.showNativeDialog();

                }
            }

            @Override
            public void onDataFailure(Call<APIResponse<InitContestRecylerViewDataRequest<List
                    <InitContestRecylerViewItemRequest>>>> call, Throwable t) {

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
