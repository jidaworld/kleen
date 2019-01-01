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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private TextView steps;
    private FragmentManager fragmentManager;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    public SaveData save = new SaveData(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ArrayList<ProgressObject> list = new ArrayList<>();
//        int steps;
//        save.read();
//        LocalDate tmp = LocalDate.of(2018, 1, 1);
//        while (tmp.isBefore(LocalDate.of(2019, 1, 1))) {
//            steps = ThreadLocalRandom.current().nextInt(8000, 13000);
//            list.add(new ProgressObject(
//                    steps,
//                    tmp.isBefore(LocalDate.of(2018, 5, 23)) ? 10000 : 11000,
//                    tmp,
//                    tmp.isBefore(LocalDate.of(2018, 12, 16)) ? 65 : 64,
//                    steps * 0.05
//            ));
//            tmp = tmp.plusDays(1);
//        }
//        save.write(list);

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

        Intent intent = new Intent(this, StepCounterService.class);
        startService(intent);

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
    public void receivedData(StepEvent stepEvent) {
        StepFragment.updateStepCounter(stepEvent.getSteps());
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
