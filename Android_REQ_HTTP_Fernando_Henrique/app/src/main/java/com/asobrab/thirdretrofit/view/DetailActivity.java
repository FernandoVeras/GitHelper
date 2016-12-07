package com.asobrab.thirdretrofit.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.asobrab.thirdretrofit.R;
import com.asobrab.thirdretrofit.fragments.DetailFragment;
import com.asobrab.thirdretrofit.model.Contributor;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity{

    Contributor contributor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contributor =  Parcels.unwrap(getIntent().getParcelableExtra("contributor"));

        DetailFragment df = DetailFragment.newInstace(contributor);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.detail, df, "contributor")
                .commit();

    }

}
