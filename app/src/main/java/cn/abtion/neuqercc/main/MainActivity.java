package cn.abtion.neuqercc.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.transition.Fade;
import android.support.transition.TransitionManager;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;
import cn.abtion.neuqercc.NEUQerCCApplication;
import cn.abtion.neuqercc.R;
import cn.abtion.neuqercc.base.activities.BaseActivity;
import cn.abtion.neuqercc.base.activities.NoBarActivity;
import cn.abtion.neuqercc.common.Config;
import cn.abtion.neuqercc.home.adapters.HomeViewPagerAdapter;
import cn.abtion.neuqercc.utils.ToastUtil;
import cn.abtion.neuqercc.widget.MainViewPager;


/**
 * @author abtion.
 * @since 17/9/22 17:59.
 * email caiheng@hrsoft.net
 */

public class MainActivity extends NoBarActivity {


    @BindView(R.id.img_tab_menu_home)
    ImageView imgTabMenuHome;
    @BindView(R.id.txt_tab_menu_home)
    TextView txtTabMenuHome;
    @BindView(R.id.img_tab_menu_order)
    ImageView imgTabMenuOrder;
    @BindView(R.id.txt_tab_menu_order)
    TextView txtTabMenuTeam;
    @BindView(R.id.img_tab_menu_message)
    ImageView imgTabMenuMessage;
    @BindView(R.id.txt_tab_menu_message)
    TextView txtTabMenuMessage;
    @BindView(R.id.img_tab_menu_mine)
    ImageView imgTabMenuMine;
    @BindView(R.id.txt_tab_menu_mine)
    TextView txtTabMenuMine;
    @BindView(R.id.vp_main_container)
    MainViewPager mainViewPager;



    private long startTime = 0;

    public static void start(BaseActivity context, int flag) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Config.KEY_MAIN_FLAG, flag);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initVariable() {


        //有关viewPager的初始化
        HomeViewPagerAdapter homeViewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        mainViewPager.setCurrentItem(Config.FLAG_HOME);
        mainViewPager.setOffscreenPageLimit(Config.PAGE_LIMIT);
        mainViewPager.setAdapter(homeViewPagerAdapter);

        mainViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changePager(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @Override
    protected void initView() {

        onLyTabMenuHomeClicked();

        //从其他活动转入后指定显示的fragment
        Intent intent = getIntent();
        int flag = intent.getIntExtra(Config.KEY_MAIN_FLAG, -1);
        mainViewPager.setCurrentItem(flag);


        TransitionManager.beginDelayedTransition((ViewGroup) findViewById(android.R.id.content), new Fade());

    }

    @Override
    protected void loadData() {

    }


    /**
     * 用于viewPager更换pager
     */
    private void changePager(int position) {
        switch (position) {
            case Config.FLAG_HOME:
                changeHomeMenuStatus();
                break;
            case Config.FLAG_TEAM:
                changeTeamMenuStatus();
                break;
//            case Config.FLAG_MESSAGE:
//                changeMessageMenuStatus();
//                break;
            case Config.FLAG_MINE:
                changeMineMenuStatus();
                break;
            default:
                break;
        }
    }

    /**
     * 清除选中状态
     */
    private void clearChoiceStatus() {


        imgTabMenuHome.setSelected(false);
        imgTabMenuOrder.setSelected(false);
        imgTabMenuMessage.setSelected(false);
        imgTabMenuMine.setSelected(false);

        txtTabMenuHome.setTextColor(getResources().getColor(R.color.subtitle));
        txtTabMenuTeam.setTextColor(getResources().getColor(R.color.subtitle));
        txtTabMenuMessage.setTextColor(getResources().getColor(R.color.subtitle));
        txtTabMenuMine.setTextColor(getResources().getColor(R.color.subtitle));

    }

    /**
     * 高亮Home
     */
    private void changeHomeMenuStatus() {
        clearChoiceStatus();
        imgTabMenuHome.setSelected(true);
        txtTabMenuHome.setTextColor(getResources().getColor(R.color.text_warning));
    }

    /**
     * 高亮Team
     */
    private void changeTeamMenuStatus() {
        clearChoiceStatus();
        imgTabMenuOrder.setSelected(true);
        txtTabMenuTeam.setTextColor(getResources().getColor(R.color.text_warning));

    }

    /**
     * 高亮Message
     */
    private void changeMessageMenuStatus() {
        clearChoiceStatus();
        imgTabMenuMessage.setSelected(true);
        txtTabMenuMessage.setTextColor(getResources().getColor(R.color.text_warning));

    }

    /**
     * 高亮Mine
     */
    private void changeMineMenuStatus() {
        clearChoiceStatus();
        imgTabMenuMine.setSelected(true);
        txtTabMenuMine.setTextColor(getResources().getColor(R.color.text_warning));
    }


    /**
     * 点击Home
     */
    @OnClick(R.id.ly_tab_menu_home)
    public void onLyTabMenuHomeClicked() {

        changeHomeMenuStatus();
        mainViewPager.setCurrentItem(Config.FLAG_HOME, false);

    }

    /**
     * 点击Team
     */
    @OnClick(R.id.ly_tab_menu_order)
    public void onLyTabMenuOrderClicked() {
        changeTeamMenuStatus();
        mainViewPager.setCurrentItem(Config.FLAG_TEAM, false);

    }

    /**
     * 点击Message
     */
    @OnClick(R.id.ly_tab_menu_message)
    public void onLyTabMenuMessageClicked() {

        changeMessageMenuStatus();
        mainViewPager.setCurrentItem(Config.FLAG_MESSAGE, false);

    }

    /**
     * Mine
     */
    @OnClick(R.id.ly_tab_menu_mine)
    public void onLyTabMenuMineClicked() {

        changeMineMenuStatus();
        mainViewPager.setCurrentItem(Config.FLAG_MINE, false);

    }


    @Override
    public void onBackPressed() {

        long currentTime = System.currentTimeMillis();
        if ((currentTime - startTime) > 2000) {
            ToastUtil.showToast("再按一次退出聚点");
            startTime = currentTime;
        } else {
            NEUQerCCApplication.getInstance().exitApp();
        }
    }


    public static void showFullImgView(Context context, String imgUrl) {

        // 构建查看大图的dialog
        final AlertDialog dialogAddHonor = new AlertDialog.Builder(context, R.style.dialog_full_img).create();
        dialogAddHonor.show();
        dialogAddHonor.getWindow().setContentView(R.layout.item_dialog_full_img);
        ImageView imageView = (ImageView) dialogAddHonor.findViewById(R.id.img_full_dialog);
        Glide.with(context).load(imgUrl).into(imageView);
        //设置全屏显示
        Window window = dialogAddHonor.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
    }
}
