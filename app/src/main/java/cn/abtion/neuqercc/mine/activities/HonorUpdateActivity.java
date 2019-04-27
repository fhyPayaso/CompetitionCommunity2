package cn.abtion.neuqercc.mine.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import cn.abtion.neuqercc.R;
import cn.abtion.neuqercc.account.activities.LoginActivity;
import cn.abtion.neuqercc.base.activities.ToolBarActivity;
import cn.abtion.neuqercc.common.Config;
import cn.abtion.neuqercc.mine.models.ShowHonorResponse;
import cn.abtion.neuqercc.network.APIResponse;
import cn.abtion.neuqercc.network.DataCallback;
import cn.abtion.neuqercc.network.RestClient;
import cn.abtion.neuqercc.utils.DialogUtil;
import cn.abtion.neuqercc.utils.FileUtil;
import cn.abtion.neuqercc.utils.ToastUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author fhyPayaso
 * @since 2017/11/20 19:57
 * email fhyPayaso@qq.com
 */

public class HonorUpdateActivity extends ToolBarActivity {


    /**
     * 获取默认的年级和擅长领域信息
     */
    private String currentTime;

    /**
     * 获取当前item在列表的第几项
     */
    private int flagTime = 0;

    /**
     * 列表中点击时暂存点击位置
     */
    private int flagTempTime = 0;

    private String[] honorTimeList;

    /**
     * 信息上传类型，0为修改，1为添加
     */
    private int uploadType = 0;


    /**
     * 判断是否从本地上传了照片
     */
    private boolean flagUpLoad;
    private int currentOrder;

    private String photoPath;
    private ShowHonorResponse honorResponse;

    Button btnTakePhoto;
    Button btnFromAlbum;
    Button btnCancel;

    @BindView(R.id.img_add_honor)
    ImageView imgAddHonor;
    @BindView(R.id.btn_honor_confirm_edit)
    Button btnHonorConfirmEdit;
    @BindView(R.id.edit_event_name)
    EditText editEventName;
    @BindView(R.id.txt_update_time)
    TextView txtUpdateTime;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_mine_honor_update;
    }

    @Override
    protected void initVariable() {

        flagUpLoad = false;
        setActivityTitle(getString(R.string.title_certificate_editing));
        FileUtil.verifyStoragePermissions(HonorUpdateActivity.this);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void loadData() {

        loadIntent();
    }

    @OnClick(R.id.img_add_honor)
    public void onImgAddHonorClicked() {

        showHonorDialog();
    }

    public void loadIntent() {

        Intent intent = getIntent();
        honorTimeList = intent.getStringArrayExtra("timeList");
        honorResponse = new Gson().fromJson(intent.getStringExtra("honorInformation"), ShowHonorResponse.class);

        //0代表编辑证书，1代表添加证书
        if ("0".equals(intent.getStringExtra("honorWall"))) {

            uploadType = 0;
            editEventName.setText(honorResponse.getGloryName());
            currentTime = honorResponse.getGloryTime();
            txtUpdateTime.setText(currentTime);
            currentOrder = honorResponse.getOrder();
            String picUrl = honorResponse.getGloryPicUrl();
            Glide.with(this).load(picUrl).into(imgAddHonor);

        } else {

            uploadType = 1;

            imgAddHonor.setImageResource(R.drawable.bg_add_honor);
            txtUpdateTime.setText(honorTimeList[0]);
        }

    }


    public void showHonorDialog() {


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


        btnTakePhoto = (Button) dialogAddHonor.findViewById(R.id.btn_take_photo);
        btnFromAlbum = (Button) dialogAddHonor.findViewById(R.id.btn_from_album);
        btnCancel = (Button) dialogAddHonor.findViewById(R.id.btn_cancel);

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //打开相机并制定存储路径
                String filePath = FileUtil.createNewFile(HonorUpdateActivity.this, "NEUQerCC");
                photoPath = FileUtil.openCamera(HonorUpdateActivity.this, filePath);

                if (dialogAddHonor.isShowing()) {
                    dialogAddHonor.dismiss();
                }
            }
        });

        btnFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //打开相册
                FileUtil.openAlbum(HonorUpdateActivity.this);

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
                case FileUtil.ALBUM_REQUEST:
                    photoPath = FileUtil.getSelectedPicturePath(data, HonorUpdateActivity.this);
                    imgAddHonor.setImageBitmap(BitmapFactory.decodeFile(photoPath));
                    flagUpLoad = true;
                    break;
                case FileUtil.CAMERA_REQUEST:
                    imgAddHonor.setImageBitmap(BitmapFactory.decodeFile(photoPath));
                    flagUpLoad = true;
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 确认修改点击事件
     */
    @OnClick(R.id.btn_honor_confirm_edit)
    public void onBtnConfirmClicked() {

        if (isDataTrue()) {

            uploadHonorInformation();
        } else {
            ToastUtil.showToast(getString(R.string.toast_lack_information));
        }

    }


    public void uploadHonorInformation() {

        //弹出progressDialog
        progressDialog.setMessage(getString(R.string.dialog_wait_moment));
        progressDialog.show();

        //修改证书网络请求
        if (uploadType == 0) {


            //接口里的顺序差了1


            if (flagUpLoad) {

                File file = new File(photoPath);
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part part = MultipartBody.Part.createFormData("glory_pic", file.getName(), requestBody);


                honorResponse = new ShowHonorResponse(currentOrder + 1, editEventName.getText().toString(),
                        txtUpdateTime.getText().toString(), photoPath);

                RestClient.getService().uploadHonor(LoginActivity.phoneNumber,
                        honorResponse.setHonorMap(), part).enqueue(new DataCallback<APIResponse>() {

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

                honorResponse = new ShowHonorResponse(currentOrder + 1, editEventName.getText().toString(),
                        txtUpdateTime.getText().toString(), "no_pic");

                RestClient.getService().uploadHonor(LoginActivity.phoneNumber,
                        honorResponse.setHonorMap()).enqueue(new DataCallback<APIResponse>() {

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

        } else {

            File file = new File(photoPath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("glory_pic", file.getName(), requestBody);


            /**
             * 添加证书网络请求
             */
            honorResponse = new ShowHonorResponse(editEventName.getText().toString(),
                    txtUpdateTime.getText().toString(), photoPath);

            RestClient.getService().addHonor(LoginActivity.phoneNumber,
                    honorResponse.setHonorMap(), part).enqueue(new DataCallback<APIResponse>() {

                @Override
                public void onDataResponse(Call<APIResponse> call, Response<APIResponse> response) {

                    ToastUtil.showToast("添加成功");
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
     * 判断信息是否完整
     *
     * @return
     */
    private boolean isDataTrue() {
        boolean flag = true;

        if (editEventName.getText().toString().trim().equals(Config.EMPTY_FIELD)) {
            flag = false;
        } else if (!flagUpLoad && uploadType == 1) {

            //添加证书必须上传照片
            flag = false;
        }

        return flag;
    }


    @OnClick(R.id.liner_update_time)
    public void onLinerUpdateTimeClicked() {

        showTimeList();
    }


    public void showTimeList() {


        for (int i = 0; i < honorTimeList.length; i++) {

            if (honorTimeList[i].equals(currentTime)) {
                flagTime = i;
            }
        }


        DialogUtil.NativeDialog nativeDialog = new DialogUtil().new NativeDialog().singleInit(HonorUpdateActivity.this);

        nativeDialog.setSingleChoice(honorTimeList, flagTime, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                flagTempTime = which;

            }
        });

        nativeDialog.setNegativeButton("取消");
        nativeDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                flagTime = flagTempTime;
                txtUpdateTime.setText(honorTimeList[flagTime].trim());
            }
        });

        nativeDialog.showNativeDialog();


    }

}
