package com.android.falldetector;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;

    private FileWriter mFileWriter;
    private HistoryDBHelper mDbHelper;

    private Timer checkImmobile = new Timer();
    private TimerTask ok;

    private final int MAX_RECORDS = 200;
    private final int NUM_FALL_THRESHOLD = 11;
    private final double FALL_MAG_THRESHOLD = 35;
    private final int REST_THRESHOLD = 20;

    private int currRecordInd;
    private int accel_count; // fall occurs if accel_count >= NUM_ACCEL_THRESHOLD
    private int idle_count;
    private boolean cycle;

    private float[] accel_data;

    private boolean isAYOActive;
    private static final String fileName = "acc.csv";
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity", "------> onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        /*
      The {@link android.support.v4.view.PagerAdapter} that will provide
      fragments for each of the sections. We use a
      {@link FragmentPagerAdapter} derivative, which will keep every
      loaded fragment in memory. If this becomes too memory intensive, it
      may be best to switch to a
      {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        /*
      The {@link ViewPager} that will host the section contents.
     */
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        isAYOActive = false;
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);

        File file = new File(getFilesDir(), fileName);
        try {
            mFileWriter = new FileWriter(file, false);
//            Log.d("MainActivity", String.valueOf(getFilesDir()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mDbHelper = new HistoryDBHelper(this);
        mDbHelper.insertData(Calendar.getInstance().getTime().toString(),
                "Thunder Bay", 1);
    }

    @Override
    protected void onResume() {
        Log.d("MainActivity", "------> onResume");
        super.onResume();
        isAYOActive = false;
        currRecordInd = 0;
        accel_count = 0;
        cycle = false;
        idle_count = 0;
        accel_data = new float[MAX_RECORDS];
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("MainActivity", "==============> onSensorChanged");
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
            return;
        }
        float ax = event.values[0];
        float ay = event.values[1];
        float az = event.values[2];

        //---- display sensor data in Wave fragment
        SectionsPagerAdapter adapter = ((SectionsPagerAdapter)mViewPager.getAdapter());
        Wave waveFrag = (Wave)adapter.getFragment(0);
        if (waveFrag != null) {
            waveFrag.updateView(ax, ay, az);
        }

        //---- save sensor data in file
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        try {
            mFileWriter.append(date.toString()).append(',')
                    .append(Float.toString(ax)).append(',')
                    .append(Float.toString(ay)).append(',')
                    .append(Float.toString(az)).append('\n');
        } catch (Exception e) {
            e.printStackTrace();
        }

        //---- fall detection
        // 1) get new accelerometer reading
        float accelValue = ax * ax + ay * ay + az * az;

        // 2) record accelerometer difference, then increment currRecordInd
        if (currRecordInd != 0) { // if not the very first record

            // 3) update accel_count
            boolean newRecordTap = accelValue < FALL_MAG_THRESHOLD;
            boolean oldRecordTap = accel_data[currRecordInd] < FALL_MAG_THRESHOLD;
            if (newRecordTap) {
                if (!oldRecordTap || !cycle) {
                    accel_count++;
                }
                idle_count = 0;
            } else {
                if (oldRecordTap && cycle) {
                    accel_count--;
                }
                idle_count++;
                if (idle_count >= REST_THRESHOLD)
                    accel_count = Math.max(0, accel_count - 2);
            }
        }
        accel_data[currRecordInd] = accelValue;
        currRecordInd = (currRecordInd + 1) % MAX_RECORDS;

        // 4) check if accel_count threshold is met, if so switch activity
        if (accel_count >= NUM_FALL_THRESHOLD) {
            //Need to check if the "are you okay is already called"
            if (!isAYOActive) {
                isAYOActive = true;
                Intent verification = new Intent(this, Verification.class);
                startActivity(verification);
                currRecordInd++; //Remove this line IF text of Accelerometer is different.
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onStop() {
        Log.d("MainActivity", "------> onStop");
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("MainActivity", "------> onCreateOptionMenu");
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

            Intent intent = new Intent(this, EditTemplate.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.d("PlaceholderFragment", "---> onCreateView");
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                return inflater.inflate(R.layout.fragment_alert, container, false);
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 4) {
                return inflater.inflate(R.layout.fragment_statistics, container, false);
            } else { // should not run into this case
                return inflater.inflate(R.layout.fragment_main, container, false);
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        HashMap<Integer, Fragment> mPageReferenceMap = new HashMap<>();

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            mPageReferenceMap.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            mPageReferenceMap.remove(position);
        }

        Fragment getFragment(int key) {
            return mPageReferenceMap.get(key);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("SectionsPagerAdapter", "---> getItem " + position);
            // getItem is called to instantiate the fragment for the given page.
            // Return a Fragment.
            if (position == 0) {
                return Wave.newInstance(position + 1);
            } else if (position == 1) {
                return PlaceholderFragment.newInstance(position + 1);
            } else if (position == 2) {
                return History.newInstance();
            } else if (position == 3) {
                return PlaceholderFragment.newInstance(position + 1);
            } else { // should not run into this case
                return PlaceholderFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "WAVE";
                case 1:
                    return "ALERT";
                case 2:
                    return "HISTORY";
                case 3:
                    return "STATISTICS";
            }
            return null;
        }
    }

    protected void onPause() {
        Log.d("MainActivity", "------> onPause");
        super.onPause();
    }

    public void onSettingsButtonClick(View v) {
        Intent settingsIntent = new Intent(this, EditTemplate.class);
        startActivity(settingsIntent);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) { // Whenever ANYTHING is pressed!
        if (ok != null)
            ok.cancel();

        ok = new TimerTask() {
            public void run() {
                int from = 100;
                int to = 601;
                Calendar c = Calendar.getInstance();
                int t = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE);
                if (t < from && t > to) {
                    Intent notif = new Intent(MainActivity.this, Verification.class);
                    startActivity(notif);
                } else dispatchTouchEvent(null); //Resets timer if sleeping
            }
        };
        if (event == null) { //If sleeping, sets timer to 10:00am
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, 10);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            checkImmobile.schedule(ok, c.getTime());
        } else checkImmobile.schedule(ok, 14400000); //4 Hours == 14400000

        return super.dispatchTouchEvent(event);  //Allows event to continue propagating
    }

    @Override
    protected void onDestroy() {
        Log.d("MainActivity", "------> onDestroy");
        super.onDestroy();
        mSensorManager.unregisterListener(this);
        try {
            mFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mDbHelper.close();
    }
}
