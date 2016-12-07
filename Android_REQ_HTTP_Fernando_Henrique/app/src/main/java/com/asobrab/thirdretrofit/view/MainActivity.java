package com.asobrab.thirdretrofit.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.asobrab.thirdretrofit.R;
import com.asobrab.thirdretrofit.fragments.DetailFragment;
import com.asobrab.thirdretrofit.fragments.FavoriteFragment;
import com.asobrab.thirdretrofit.fragments.MainFragment;
import com.asobrab.thirdretrofit.model.Contributor;

import org.parceler.Parcels;

public class MainActivity extends AppCompatActivity
            implements ContributorClick {

    MainFragment mainFragment;
    FavoriteFragment favorFragment;
    SelectorPageAdapter selectorPageAdapter;
    ViewPager viewPager;
    Toolbar toolbar;
    TabLayout tabLayout;
    DetailFragment detailFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager  = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        setSupportActionBar(toolbar);

        buildViewPager();
    }

    private void buildViewPager(){
        selectorPageAdapter = new SelectorPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(selectorPageAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void Click(Contributor contributor) {
        if(getResources().getBoolean(R.bool.phone)) {
            Intent it = new Intent(this, DetailActivity.class);
            it.putExtra("contributor", Parcels.wrap(contributor));
            this.startActivity(it);
        }else {
            DetailFragment df = DetailFragment.newInstace(contributor);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail, df, "contributor")
                    .commit();
        }
    }

    public class SelectorPageAdapter extends FragmentPagerAdapter {

        public SelectorPageAdapter(FragmentManager fm) {super(fm);}

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if(favorFragment == null){
                        favorFragment = new FavoriteFragment();
                    }
                    return favorFragment;

                case 1:
                default:
                    if (mainFragment == null){
                        mainFragment = new MainFragment();
                    }
                    return mainFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position){
            switch (position){
                case 0:
                    return getString(R.string.favorite);
                case 1:
                default:
                    return getString(R.string.list);
            }

        }
    }
}
