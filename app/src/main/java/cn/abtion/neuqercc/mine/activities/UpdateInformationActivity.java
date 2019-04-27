package cn.abtion.neuqercc.mine.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.abtion.neuqercc.R;
import cn.abtion.neuqercc.account.activities.LoginActivity;
import cn.abtion.neuqercc.base.activities.ToolBarActivity;
import cn.abtion.neuqercc.common.Config;
import cn.abtion.neuqercc.mine.adapters.GridHonorAdapter;
import cn.abtion.neuqercc.mine.models.GoodAtRequest;
import cn.abtion.neuqercc.mine.models.PersonInformationResponse;
import cn.abtion.neuqercc.mine.models.ShowHonorResponse;
import cn.abtion.neuqercc.mine.models.StudentGradeRequest;
import cn.abtion.neuqercc.mine.models.UpdatePersonInformationRequest;
import cn.abtion.neuqercc.network.APIResponse;
import cn.abtion.neuqercc.network.DataCallback;
import cn.abtion.neuqercc.network.RestClient;
import cn.abtion.neuqercc.utils.DialogUtil;
import cn.abtion.neuqercc.utils.FileUtil;
import cn.abtion.neuqercc.utils.RegexUtil;
import cn.abtion.neuqercc.utils.ToastUtil;
import cn.abtion.neuqercc.widget.HonorGridView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author fhyPayaso
 * @since 2017/11/15 21:30
 * email fhyPayaso@qq.com
 */

public class UpdateInformationActivity extends ToolBarActivity {


    private static final String TAG = "UpdateInformation";

    /**
     * 数据信息错误类型标志
     */
    public static final int FLAG_CORRECT = 0;
    public static final int FLAG_LACK_ERROR = 1;
    public static final int FLAG_PHONE_ERROR = 2;


    /**
     * 眼睛显示标志
     */
    public static final int FLAG_EYE_OPEN = 1;
    public static final int FLAG_EYE_CLOSE = 0;

    /**
     * 姓名电话是否可见
     */
    public static int flagNameEye = 1;
    public static int flagPhoneEye = 1;


    /**
     * 获取默认的年级和擅长领域信息
     */
    private String currentGrade;
    private String currentGoodAt;

    /**
     * 获取当前item在列表的第几项
     */
    private int flagGrade = -1;
    private int flagGoodAt = -1;

    /**
     * 列表中点击时暂存点击位置
     */
    private int flagTempGoodAt = 0;
    private int flagTempGrade = 0;


    private boolean flagUpLoad;
    private String photoPath;


    @BindView(R.id.img_mine_name_eye)
    ImageView imgNameEye;
    @BindView(R.id.img_mine_phone_eye)
    ImageView imgPhoneEye;
    @BindView(R.id.cb_edit_girl)
    CheckBox checkBoxGirl;
    @BindView(R.id.cb_edit_boy)
    CheckBox checkBoxBoy;
    @BindView(R.id.mine_update_avatar)
    CircleImageView imgUpdateAvatar;
    @BindView(R.id.rlayout_mine_wrap)
    RelativeLayout rlayoutMineWrap;
    @BindView(R.id.edit_nick_name)
    EditText editNickName;
    @BindView(R.id.edit_real_name)
    EditText editRealName;
    @BindView(R.id.edit_phone_number)
    EditText editPhoneNumber;
    @BindView(R.id.edit_update_profession)
    EditText editUpdateProfession;
    @BindView(R.id.edit_update_stu_id)
    EditText editUpdateStuId;
    @BindView(R.id.mine_grid_honor)
    HonorGridView gridHonor;
    @BindView(R.id.txt_update_grade)
    TextView txtUpdateGrade;
    @BindView(R.id.txt_update_good_at)
    TextView txtUpdateGoodAt;


    private GridHonorAdapter gridHonorAdapter;
    private boolean isShowDelete;
    private List<ShowHonorResponse> showHonorResponseList;
    private String[] studentGradeList;
    private String[] goodAtList;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_imformation;
    }

    @Override
    protected void initVariable() {


        flagUpLoad = false;
        showHonorResponseList = new ArrayList<>();


        //动态申请权限
        FileUtil.verifyStoragePermissions(UpdateInformationActivity.this);
    }

    @Override
    protected void initView() {

        initToolBar();
        initCheckbox();

    }

    @Override
    protected void loadData() {

        loadPersonalInformation();
        initHonorWall();
        loadListData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        initHonorWall();
    }

    /**
     * 初始化ToolBar
     */
    protected void initToolBar() {

        setActivityTitle(getString(R.string.title_update_information));
        setTextOver(getString(R.string.title_over));

        //完成按钮点击设置
        TextView txtTitleOver = (TextView) getToolbar().findViewById(R.id.txt_toolbar_over);
        txtTitleOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (isDataTrue()) {

                    case FLAG_LACK_ERROR:
                        ToastUtil.showToast(getString(R.string.toast_lack_information));
                        break;
                    case FLAG_PHONE_ERROR:
                        ToastUtil.showToast(getString(R.string.error_phone_number_illegal));
                        break;
                    default:
                        uploadPersonalInformation();
                        break;

                }

            }
        });
    }


    /**
     * 上传个人信息
     */
    public void uploadPersonalInformation() {

        int flagGender = 0;
        if (checkBoxBoy.isChecked() && !checkBoxGirl.isChecked()) {
            flagGender = 0;
        } else if (!checkBoxBoy.isChecked() && checkBoxGirl.isChecked()) {
            flagGender = 1;
        }


        UpdatePersonInformationRequest updatePersonInformationRequest = new UpdatePersonInformationRequest(
                editPhoneNumber.getText().toString().trim(),
                editNickName.getText().toString().trim(),
                editRealName.getText().toString().trim(),
                txtUpdateGoodAt.getText().toString().trim(),
                editUpdateProfession.getText().toString(),
                Integer.valueOf(editUpdateStuId.getText().toString().trim()),
                flagGender, Integer.valueOf(txtUpdateGrade.getText().toString().trim()),
                flagNameEye, flagPhoneEye);


        //弹出progressDialog
        progressDialog.setMessage(getString(R.string.dialog_wait_moment));
        progressDialog.show();

        if (flagUpLoad) {

            File file = new File(photoPath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("pic", file.getName(), requestBody);


            RestClient.getService().uploadPersonInformation(updatePersonInformationRequest.createCommitParams(),
                    part)
                    .enqueue(new DataCallback<APIResponse>() {

                        @Override
                        public void onDataResponse(Call<APIResponse> call, Response<APIResponse> response) {

                            ToastUtil.showToast(getString(R.string.toast_edit_successful));
                            finish();
                        }

                        @Override
                        public void onDataFailure(Call<APIResponse> call, Throwable t) {

                        }

                        @Override
                        public void dismissDialog() {
                            if(progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                        }
                    });


        } else {


            RestClient.getService().uploadPersonInformation(updatePersonInformationRequest.createCommitParams())
                    .enqueue(new DataCallback<APIResponse>() {

                        @Override
                        public void onDataResponse(Call<APIResponse> call, Response<APIResponse> response) {

                            ToastUtil.showToast(getString(R.string.toast_edit_successful));
                            finish();
                        }

                        @Override
                        public void onDataFailure(Call<APIResponse> call, Throwable t) {

                        }

                        @Override
                        public void dismissDialog() {
                            if(progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                        }
                    });
        }
    }


    /**
     * 初始化CheckBox
     */
    public void initCheckbox() {

        checkBoxGirl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && checkBoxBoy.isChecked()) {
                    checkBoxBoy.setChecked(false);
                }
            }
        });

        checkBoxBoy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked && checkBoxGirl.isChecked()) {
                    checkBoxGirl.setChecked(false);
                }
            }
        });

    }


    /**
     * 控制姓名显示
     */
    @OnClick(R.id.img_mine_name_eye)
    public void onImgNameEyeClicked() {

        if (flagNameEye == FLAG_EYE_OPEN) {
            imgNameEye.setSelected(true);
            flagNameEye = FLAG_EYE_CLOSE;
        } else {
            imgNameEye.setSelected(false);
            flagNameEye = FLAG_EYE_OPEN;
        }

    }

    /**
     * 控制手机号码显示
     */
    @OnClick(R.id.img_mine_phone_eye)
    public void onImgPhoneEyeClicked() {

        if (flagPhoneEye == FLAG_EYE_OPEN) {
            imgPhoneEye.setSelected(true);
            flagPhoneEye = FLAG_EYE_CLOSE;
        } else {
            imgPhoneEye.setSelected(false);
            flagPhoneEye = FLAG_EYE_OPEN;
        }
    }


    /**
     * 加载个人信息
     */
    public void loadPersonalInformation() {

        Intent intent = getIntent();
        PersonInformationResponse informationResponse = new Gson().fromJson(
                intent.getStringExtra("personInformation"), PersonInformationResponse.class);

        editNickName.setText(informationResponse.getUsername());
        editRealName.setText(informationResponse.getName());
        editPhoneNumber.setText(informationResponse.getPhone());
        editUpdateStuId.setText(String.valueOf(informationResponse.getStudentId()));
        editUpdateProfession.setText(informationResponse.getMajor());

        currentGoodAt = informationResponse.getGoodAt();
        txtUpdateGoodAt.setText(currentGoodAt);

        currentGrade = String.valueOf(informationResponse.getGrade());
        txtUpdateGrade.setText(currentGrade);

        if (informationResponse.getPicture() != null) {
            Glide.with(this).load(informationResponse.getPicture()).into(imgUpdateAvatar);
        }


        if (informationResponse.getGender() == 0) {
            checkBoxBoy.setChecked(true);
            checkBoxGirl.setChecked(false);
        } else {
            checkBoxBoy.setChecked(false);
            checkBoxGirl.setChecked(true);
        }



        //初始化眼睛图标

        flagNameEye = informationResponse.getNameSee();
        flagPhoneEye = informationResponse.getPhoneSee();

        if (flagNameEye == FLAG_EYE_OPEN) {
            imgNameEye.setSelected(false);
        } else if (flagNameEye == FLAG_EYE_CLOSE) {
            imgNameEye.setSelected(true);
        }

        if (flagPhoneEye == FLAG_EYE_OPEN) {
            imgPhoneEye.setSelected(false);
        } else if (flagPhoneEye == FLAG_EYE_CLOSE) {
            imgPhoneEye.setSelected(true);
        }
    }


    /**
     * 初始化荣誉墙
     */
    public void initHonorWall() {


        RestClient.getService().showHonorRequest(LoginActivity.phoneNumber).enqueue(new DataCallback<APIResponse<List<ShowHonorResponse>>>() {

            //请求成功时回调
            @Override
            public void onDataResponse(Call<APIResponse<List<ShowHonorResponse>>> call,
                                       Response<APIResponse<List<ShowHonorResponse>>> response) {

                showHonorResponseList = response.body().getData();
                initGrid();

            }

            @Override
            public void onDataFailure(Call<APIResponse<List<ShowHonorResponse>>> call, Throwable t) {

            }

            @Override
            public void dismissDialog() {

            }
        });
    }

    /**
     * 初始化Grid
     */
    public void initGrid() {

        gridHonorAdapter = new GridHonorAdapter(UpdateInformationActivity.this, showHonorResponseList, true);
        gridHonor.setAdapter(gridHonorAdapter);


        gridHonor.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //如果点击添加图片部分

                if (position == parent.getChildCount() - 1) {

                    showHonorDialog();

                } else {

                    Intent intent = new Intent(UpdateInformationActivity.this, HonorUpdateActivity.class);
                    ShowHonorResponse response = showHonorResponseList.get(position);
                    intent.putExtra("honorInformation", new Gson().toJson(response));
                    intent.putExtra("timeList", studentGradeList);
                    intent.putExtra("honorWall", "0");
                    startActivity(intent);
                }
                isShowDelete = false;
            }
        });


        //如果长按图片
        gridHonor.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (position < showHonorResponseList.size()) {

                    if (isShowDelete) {
                        isShowDelete = false;
                        gridHonorAdapter.setIsShowDelete(isShowDelete);
                    } else {
                        isShowDelete = true;
                        gridHonorAdapter.setIsShowDelete(isShowDelete);
                    }
                }
                //返回true只处理长按事件
                return true;
            }
        });
    }


    /**
     * 取消荣誉墙删除按钮点击事件
     */
    @OnClick(R.id.rlayout_mine_wrap)
    public void onViewClicked() {

        if (isShowDelete) {

            isShowDelete = false;
            gridHonorAdapter.setIsShowDelete(isShowDelete);
        } else {
            gridHonorAdapter.setIsShowDelete(isShowDelete);
        }
    }

    /**
     * 显示荣誉底部弹窗
     */
    public void showHonorDialog() {


        final AlertDialog dialogAddHonor = new AlertDialog.Builder(this, R.style.dialog_bottom).create();
        dialogAddHonor.show();
        dialogAddHonor.getWindow().setContentView(R.layout.dialog_mine_honor);

        Window window = dialogAddHonor.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);


        Button btnAddHonor = (Button) dialogAddHonor.findViewById(R.id.btn_add_honor);
        Button btnCancel = (Button) dialogAddHonor.findViewById(R.id.btn_cancel);


        btnAddHonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dialogAddHonor.isShowing()) {
                    dialogAddHonor.dismiss();
                }
                Intent intent = new Intent(UpdateInformationActivity.this, HonorUpdateActivity.class);
                intent.putExtra("honorWall", "1");
                intent.putExtra("timeList", studentGradeList);
                startActivity(intent);

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dialogAddHonor.isShowing()) {
                    dialogAddHonor.dismiss();
                }
            }
        });

    }


    /**
     * 更换头像点击事件
     */
    @OnClick(R.id.mine_update_avatar)
    public void onImgUpdateAvatarClicked() {

        showAvatarDialog();
    }

    /**
     * 显示更换头像底部弹窗
     */
    public void showAvatarDialog() {

        final AlertDialog dialogAddHonor = new AlertDialog.Builder(this, R.style.dialog_bottom).create();
        dialogAddHonor.show();
        dialogAddHonor.getWindow().setContentView(R.layout.dialog_mine_avatar);

        Window window = dialogAddHonor.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);


        Button btnTakePhoto = (Button) dialogAddHonor.findViewById(R.id.btn_take_photo);
        Button btnFromAlbum = (Button) dialogAddHonor.findViewById(R.id.btn_from_album);
        Button btnCancel = (Button) dialogAddHonor.findViewById(R.id.btn_cancel);

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //打开照相机，得到存取照片之后的路径
                String filePath = FileUtil.createNewFile(UpdateInformationActivity.this, "NEUQerCC");
                photoPath = FileUtil.openCamera(UpdateInformationActivity.this, filePath);
                if (dialogAddHonor.isShowing()) {
                    dialogAddHonor.dismiss();
                }
            }
        });


        btnFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FileUtil.openAlbum(UpdateInformationActivity.this);

                if (dialogAddHonor.isShowing()) {
                    dialogAddHonor.dismiss();
                }
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dialogAddHonor.isShowing()) {
                    dialogAddHonor.dismiss();
                }
            }
        });

    }


    public void loadListData() {


        //获取擅长领域列表网络请求

        RestClient.getService().goodAtRequest().enqueue(new DataCallback<APIResponse<List<GoodAtRequest>>>() {

            @Override
            public void onDataResponse(Call<APIResponse<List<GoodAtRequest>>> call,
                                       Response<APIResponse<List<GoodAtRequest>>> response) {

                List<GoodAtRequest> goodAtRequestList = response.body().getData();

                goodAtList = new String[goodAtRequestList.size()];
                for (int i = 0; i < goodAtRequestList.size(); i++) {
                    goodAtList[i] = goodAtRequestList.get(i).getField().trim();

                    if (goodAtList[i].equals(currentGoodAt)) {
                        flagGoodAt = i;
                    }

                }

            }

            @Override
            public void onDataFailure(Call<APIResponse<List<GoodAtRequest>>> call, Throwable t) {

            }

            @Override
            public void dismissDialog() {

            }

        });


        //获取学生年级列表网络请求

        RestClient.getService().studentGradeRequest().enqueue(new DataCallback<APIResponse<List<StudentGradeRequest>>>() {

            @Override
            public void onDataResponse(Call<APIResponse<List<StudentGradeRequest>>> call,
                                       Response<APIResponse<List<StudentGradeRequest>>> response) {

                List<StudentGradeRequest> studentGradeRequestList = response.body().getData();

                studentGradeList = new String[studentGradeRequestList.size()];
                for (int i = 0; i < studentGradeRequestList.size(); i++) {
                    studentGradeList[i] = studentGradeRequestList.get(i).getYear().trim();

                    if (studentGradeList[i].equals(currentGrade)) {
                        flagGrade = i;
                    }
                }

            }

            @Override
            public void onDataFailure(Call<APIResponse<List<StudentGradeRequest>>> call, Throwable t) {

            }

            @Override
            public void dismissDialog() {

            }

        });

    }


    @OnClick(R.id.spinner_student_grade)
    public void onSpinnerStudentGradeClicked() {

        showGradeList();
    }

    @OnClick(R.id.spinner_good_at)
    public void onSpinnerGoodAtClicked() {

        showGoodAtList();
    }

    public void showGoodAtList() {


        DialogUtil.NativeDialog nativeDialog = new DialogUtil().new NativeDialog().singleInit
                (UpdateInformationActivity.this);

        nativeDialog.setSingleChoice(goodAtList, flagGoodAt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                flagTempGoodAt = which;
            }
        });
        nativeDialog.setNegativeButton(getString(R.string.dialog_btn_cancel));
        nativeDialog.setPositiveButton(getString(R.string.dialog_btn_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                flagGoodAt = flagTempGoodAt;
                txtUpdateGoodAt.setText(goodAtList[flagGoodAt].trim());

            }
        });


        nativeDialog.showNativeDialog();
    }

    public void showGradeList() {


        DialogUtil.NativeDialog nativeDialog = new DialogUtil().new NativeDialog().singleInit
                (UpdateInformationActivity.this);

        nativeDialog.setSingleChoice(studentGradeList, flagGrade, new DialogInterface.OnClickListener() {
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
                txtUpdateGrade.setText(studentGradeList[flagGrade].trim());
            }
        });

        nativeDialog.showNativeDialog();
    }


    /**
     * 验证用户输入是否正确
     *
     * @return 正确为true
     */
    private int isDataTrue() {
        int flag = FLAG_CORRECT;
        if (editNickName.getText().toString().trim().equals(Config.EMPTY_FIELD)) {

            flag = FLAG_LACK_ERROR;
        } else if (editRealName.getText().toString().trim().equals(Config.EMPTY_FIELD)) {

            flag = FLAG_LACK_ERROR;
        } else if (editPhoneNumber.getText().toString().trim().equals(Config.EMPTY_FIELD)) {

            flag = FLAG_LACK_ERROR;
        } else if (editUpdateProfession.getText().toString().trim().equals(Config.EMPTY_FIELD)) {

            flag = FLAG_LACK_ERROR;
        } else if (editUpdateStuId.getText().toString().trim().equals(Config.EMPTY_FIELD)) {

            flag = FLAG_LACK_ERROR;
        } else if (!checkBoxBoy.isChecked() && !checkBoxGirl.isChecked()) {

            flag = FLAG_LACK_ERROR;

        } else if (!RegexUtil.checkMobile(editPhoneNumber.getText().toString().trim())) {

            flag = FLAG_PHONE_ERROR;
        }
        return flag;
    }


    /**
     * 返回选择的图片
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //相机回调
                case FileUtil.CAMERA_REQUEST:
                    Log.i("", "onActivityResult: " + photoPath);
                    imgUpdateAvatar.setImageBitmap(BitmapFactory.decodeFile(photoPath));
                    flagUpLoad = true;
                    break;
                //相册回调
                case FileUtil.ALBUM_REQUEST:
                    photoPath = FileUtil.getSelectedPicturePath(data, UpdateInformationActivity.this);
                    Log.i("", "onActivityResult: " + photoPath);
                    imgUpdateAvatar.setImageBitmap(BitmapFactory.decodeFile(photoPath));
                    flagUpLoad = true;
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
