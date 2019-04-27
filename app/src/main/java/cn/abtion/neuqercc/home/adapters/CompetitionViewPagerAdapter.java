package cn.abtion.neuqercc.home.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.abtion.neuqercc.common.Config;
import cn.abtion.neuqercc.home.fragments.DetailsFragment;
import cn.abtion.neuqercc.home.fragments.RaidersFragment;
import cn.abtion.neuqercc.home.models.RaidersAndDetailsRequest;

/**
 * @author fhyPayaso
 * @since 2017/11/7 16:56
 * email fhyPayaso@qq.com
 */

public class CompetitionViewPagerAdapter extends FragmentPagerAdapter {


    private String[] titles = new String[]{Config.TAB_TITLE_DETAILS, Config.TAB_TITLE_RAIDERS};
    private DetailsFragment detailsFragment;
    private RaidersFragment raidersFragment;
    private android.support.v4.app.Fragment currentFragment;
    private RaidersAndDetailsRequest data;

    public CompetitionViewPagerAdapter(FragmentManager fm, RaidersAndDetailsRequest raidersAndDetailsRequestdata) {
        super(fm);
        data = raidersAndDetailsRequestdata;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {


        switch (position) {
            case Config.FLAG_DETAILS:
                if (detailsFragment == null) {
                    detailsFragment = new DetailsFragment();
                    detailsFragment.setData(data);
                }
                currentFragment = detailsFragment;
                break;
            case Config.FLAG_RAIDERS:
                if (raidersFragment == null) {
                    raidersFragment = new RaidersFragment();
                    raidersFragment.setData(data);
                }
                currentFragment = raidersFragment;
                break;
            default:

        }

        return currentFragment;
    }

    @Override
    public int getCount() {
        return Config.PAGE_COMPETITION_LIMIT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }


}
