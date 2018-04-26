package mdsouza5.finalprojectitmd555;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import mdsouza5.finalprojectitmd555.recipefragments.MyRecipesFragment;
import mdsouza5.finalprojectitmd555.recipefragments.MyTopRecipesFragment;
import mdsouza5.finalprojectitmd555.recipefragments.RecentRecipesFragment;

public class ShowPagesActivity extends BaseActivity {
    private static final String LOGTAG = "ShowPagesActivity";
    private FragmentPagerAdapter fragmentPagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_pages);

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] fragments = new Fragment[]{
                    new RecentRecipesFragment(),
                    new MyTopRecipesFragment(),
                    new MyRecipesFragment()
            };

            private final String[] fragmentNames = new String[]{
                    getString(R.string.heading_recent),
                    getString(R.string.heading_my_recipes),
                    getString(R.string.heading_my_top_recipes)
            };

            @Override
            public int getCount() {
                return fragments.length;
            }

            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return fragmentNames[position];
            }
        };

        viewPager = findViewById(R.id.container);
        viewPager.setAdapter(fragmentPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
