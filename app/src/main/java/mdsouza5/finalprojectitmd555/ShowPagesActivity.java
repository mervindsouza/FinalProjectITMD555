/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mdsouza5.finalprojectitmd555;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

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

        findViewById(R.id.fab_new_recipe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowPagesActivity.this, NewRecipeActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipeapp, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
