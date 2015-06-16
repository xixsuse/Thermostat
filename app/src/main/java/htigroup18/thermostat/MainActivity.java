package htigroup18.thermostat;



import android.graphics.Color;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.devadvance.circularseekbar.CircularSeekBar;
import com.devadvance.circularseekbar.CircularSeekBar.OnCircularSeekBarChangeListener;

import java.util.Locale;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    private Handler repeatUpdateHandler;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    CircularSeekBar slider;
    TextView testing;
    Button plus,minus;
    boolean incrementing,decrementing;
    ImageView arrowup,arrowdown;
    public float targettemp,currenttemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        repeatUpdateHandler= new Handler();
        currenttemp=25f;
        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        incrementing=false;
        decrementing=false;
        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

            targettemp=5f;
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        slider = (CircularSeekBar) findViewById(R.id.circularSeekBar1);
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return new Tab1();
                case 1:
                    return new Tab2();
                case 2:
                    return new Tab3();
            }
            return new PlaceholderFragment();
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
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

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }


    public  class Tab1 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public  Tab1 newInstance(int sectionNumber) {
            Tab1 fragment = new Tab1();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.tab1, container, false);


            return rootView;
        }
        public void updateUI(){
            if(targettemp<18){
                slider.setCircleProgressColor(Color.BLUE);

            }
            else if(targettemp<25){
                slider.setCircleProgressColor(Color.GREEN);

            }
            else if(targettemp<31){
                slider.setCircleProgressColor(Color.RED);

            }
            else{

            }
            if(targettemp>currenttemp){
                arrowup.setVisibility(View.VISIBLE);
                arrowdown.setVisibility(View.INVISIBLE);
            }
            else if(targettemp<currenttemp){
                arrowup.setVisibility(View.INVISIBLE);
                arrowdown.setVisibility(View.VISIBLE);
            }
            else{
                arrowup.setVisibility(View.INVISIBLE);
                arrowdown.setVisibility(View.INVISIBLE);
            }
            if(targettemp==30.0){
                plus.setTextColor(Color.GRAY);
            }
            else{
                plus.setTextColor(Color.WHITE);
            }
            if(targettemp==5.0){
                minus.setTextColor(Color.GRAY);
            }
            else{
                minus.setTextColor(Color.WHITE);
            }

        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState){
            arrowup=(ImageView) findViewById(R.id.arrowup);
            arrowdown=(ImageView) findViewById(R.id.arrowdown);
            arrowup.setVisibility(View.INVISIBLE);
            arrowdown.setVisibility(View.INVISIBLE);
        slider=(CircularSeekBar) findViewById(R.id.circularSeekBar1);
            slider.setMax(250);
            testing= (TextView) findViewById(R.id.testing);
            slider.setOnSeekBarChangeListener(new CircleSeekBarListener(){
                @Override
                public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                    targettemp=(slider.getProgress()/10f)+5f;
                    testing.setText(targettemp+" \u2103");
                    updateUI();

                }
            });
            plus=(Button) findViewById(R.id.plus);
            minus=(Button) findViewById(R.id.minus);
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(targettemp!=30.0) {
                        slider.setProgress(slider.getProgress() + 1);
                        targettemp = (slider.getProgress() / 10f) + 5f;
                        testing.setText(targettemp + " \u2103");
                        updateUI();
                    }
                }
            });
            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(targettemp!=5.0) {
                        slider.setProgress(slider.getProgress() - 1);
                        targettemp = (slider.getProgress() / 10f) + 5f;
                        testing.setText(targettemp + "\u2103");
                        updateUI();
                    }
                }
            });
            plus.setOnLongClickListener(
                    new View.OnLongClickListener(){
                        public boolean onLongClick(View arg0) {
                            if(targettemp!=30.0) {
                                incrementing = true;
                                repeatUpdateHandler.post(new RptUpdater());
                                return false;
                            }
                            return false;
                        }
                    }
            );

            plus.setOnTouchListener( new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if( (event.getAction()==MotionEvent.ACTION_UP || event.getAction()==MotionEvent.ACTION_CANCEL)
                            && incrementing ){
                        incrementing = false;
                    }
                    return false;
                }
            });
            minus.setOnLongClickListener(
                    new View.OnLongClickListener(){
                        public boolean onLongClick(View arg0) {
                            if(targettemp!=5.0) {
                                decrementing = true;
                                repeatUpdateHandler.post(new RptUpdater());
                                return false;
                            }
                            return false;
                        }
                    }
            );

            minus.setOnTouchListener( new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if( (event.getAction()==MotionEvent.ACTION_UP || event.getAction()==MotionEvent.ACTION_CANCEL)
                            && decrementing ){
                        decrementing = false;
                    }
                    return false;
                }
            });
            slider.setProgress(200);
            targettemp = (slider.getProgress() / 10f) + 5f;
            testing.setText(targettemp+" \u2103");
            updateUI();
        }
    }

    public static class Tab2 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Tab2 newInstance(int sectionNumber) {
            Tab2 fragment = new Tab2();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.tab2, container, false);
            return rootView;
        }
    }

    public static class Tab3 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Tab3 newInstance(int sectionNumber) {
            Tab3 fragment = new Tab3();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.tab3, container, false);
            return rootView;
        }
    }

    class RptUpdater implements Runnable {
        public void run() {
            if( incrementing ){
                increment();
                repeatUpdateHandler.postDelayed( new RptUpdater(), 50 );
            } else if( decrementing ){
                decrement();
                repeatUpdateHandler.postDelayed( new RptUpdater(), 50 );
            }
        }
        public void increment(){
            if(targettemp!=30.0) {
                slider.setProgress(slider.getProgress() + 1);
                targettemp = (slider.getProgress() / 10f) + 5f;
                testing.setText(targettemp + " \u2103");
                updateUI();
            }
        }
        public void updateUI(){
            if(targettemp<18){
                slider.setCircleProgressColor(Color.BLUE);

            }
            else if(targettemp<25){
                slider.setCircleProgressColor(Color.GREEN);

            }
            else if(targettemp<31){
                slider.setCircleProgressColor(Color.RED);

            }
            else{

            }
            if(targettemp>currenttemp){
                arrowup.setVisibility(View.VISIBLE);
                arrowdown.setVisibility(View.INVISIBLE);
            }
            else if(targettemp<currenttemp){
                arrowup.setVisibility(View.INVISIBLE);
                arrowdown.setVisibility(View.VISIBLE);
            }
            else{
                arrowup.setVisibility(View.INVISIBLE);
                arrowdown.setVisibility(View.INVISIBLE);
            }
            if(targettemp==30.0){
                plus.setTextColor(Color.GRAY);
            }
            else{
                plus.setTextColor(Color.WHITE);
            }
            if(targettemp==5.0){
                minus.setTextColor(Color.GRAY);
            }
            else{
                minus.setTextColor(Color.WHITE);
            }


        }
        public void decrement() {
            if(targettemp!=5.0) {
                slider.setProgress(slider.getProgress() - 1);
                targettemp = (slider.getProgress() / 10f) + 5f;
                testing.setText(targettemp + " \u2103");
                updateUI();
            }
        }
    }


}




