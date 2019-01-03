package com.example.john.kleen.Controller;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.john.kleen.DB.CallBack;
import com.example.john.kleen.DB.DBHandler;
import com.example.john.kleen.Model.ProgressObject;
import com.example.john.kleen.Model.SaveData;
import com.example.john.kleen.Model.StepEvent;
import com.example.john.kleen.Model.Util.BusStation;
import com.example.john.kleen.R;
import com.example.john.kleen.View.GraphFragment;
import com.example.john.kleen.View.CalorieFragment;
import com.example.john.kleen.View.StepFragment;
import com.example.john.kleen.View.WeightFragment;
import com.squareup.otto.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private TextView steps;
    private FragmentManager fragmentManager;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    public SaveData save = new SaveData(this);
    private List<ProgressObject> poList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusStation.getBus().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusStation.getBus().unregister(this);
    }

    @Subscribe
    public void receivedData(ProgressObject progressObject) {
        StepFragment.updateStepCounter(progressObject.getSteps());
    }

    @Subscribe
    public void receivedDataList(ArrayList<ProgressObject> list) {
        poList = list;
        Log.i("DebugFirebase","received data");
        GraphFragment.setPoList(poList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment frag = null;
            if (position == 0) {
                return new StepFragment();
            } else if (position == 1) {
                return new GraphFragment();
            } else if (position == 2) {
                return new CalorieFragment();
            } else if (position == 3) {
                return new WeightFragment();
            }
            return frag;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }
    }


}
