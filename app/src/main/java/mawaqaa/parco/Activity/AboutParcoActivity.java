package mawaqaa.parco.Activity;

import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import mawaqaa.parco.Fragment.AboutParco;
import mawaqaa.parco.Fragment.FAQFragment;
import mawaqaa.parco.Fragment.MainBaseFragment;
import mawaqaa.parco.R;

public class AboutParcoActivity extends MainBaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView about;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_parco_activity);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        about = findViewById(R.id.about_parco);
        Typeface normal = Typeface.createFromAsset(getAssets(), "corbelbold.ttf");
        about.setTypeface(normal);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        AboutParcoActivity.ViewPagerAdapter adapter = new AboutParcoActivity.ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new AboutParco(), "Parco");
        adapter.addFragment(new FAQFragment(), getString(R.string.faq));
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
