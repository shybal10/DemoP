package mawaqaa.parco.Activity;

import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import mawaqaa.parco.Fragment.CurrentScheduledPickupFragment;
import mawaqaa.parco.Fragment.PreviousPickupsFragment;
import mawaqaa.parco.R;

public class HistoryActivity extends MainBaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView historyHeading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        historyHeading = (TextView) findViewById(R.id.history_text);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        Typeface bold = Typeface.createFromAsset(getAssets(), "corbelbold.ttf");
        historyHeading.setTypeface(bold);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new CurrentScheduledPickupFragment(), getString(R.string.current_scheduled));
        adapter.addFragment(new PreviousPickupsFragment(), getString(R.string.previous_pickups));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
