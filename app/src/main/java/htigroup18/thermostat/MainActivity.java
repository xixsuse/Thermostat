package htigroup18.thermostat;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
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
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Scroller;
import android.widget.TextView;
import com.devadvance.circularseekbar.CircularSeekBar;
import com.devadvance.circularseekbar.CircularSeekBar.OnCircularSeekBarChangeListener;

import java.lang.reflect.Array;
import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


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
    RadioGroup rg;
    CircularSeekBar slider;
    TextView testing,ctemp;
    Button plus,minus;
    boolean incrementing,decrementing;
    ImageView arrowup,arrowdown,test,scale;
    public float targettemp,currenttemp;
    boolean test2;
    String day;
    int monday,tuesday,wednesday,thursday,friday,saturday,sunday,time;
    ArrayList<Switch> lmonday,ltuesday,lwednesday,lthursday,lfriday,lsaterday,lsunday;
    boolean sday;
    HorizontalScrollView scroller;
    Runnable updateTimerThread;
    EditText daytemp,nighttemp;


    WeekProgram wpg;
    boolean temporary,permament,schedule,change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        change=false;
        HeatingSystem.BASE_ADDRESS = "http://wwwis.win.tue.nl/2id40-ws/18";
        HeatingSystem.WEEK_PROGRAM_ADDRESS = HeatingSystem.BASE_ADDRESS + "/weekProgram";
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        repeatUpdateHandler = new Handler();
        wpg=new WeekProgram();
        time=-10;
        monday=0;tuesday=0;wednesday=0;thursday=0;friday=0;saturday=0;sunday=0;
          lmonday=new ArrayList<Switch>();ltuesday=new ArrayList<Switch>();lwednesday=new ArrayList<Switch>();lthursday=new ArrayList<Switch>();lfriday=new ArrayList<Switch>();lsaterday=new ArrayList<Switch>();lsunday=new ArrayList<Switch>();
       final android.os.Handler customHandler = new android.os.Handler();
         updateTimerThread = new Runnable()
        {
            public void run()
            {

                if(ctemp!=null) {
                    ctemp.setText(currenttemp + " \u2103");
                    updateUI();
                }
                if(testing!=null){
                testing.setText(targettemp+"\u2103");
                    updateUI();
                }
                if(rg!=null){
                    if(change){
                        if(temporary){
                            rg.check(R.id.radioButton);
                        }
                        if(permament){
                            rg.check(R.id.radioButton2);
                        }
                        if(schedule){
                            rg.check(R.id.radioButton3);
                        }
                        change=false;
                    }

                }


                //write here whaterver you want to repeat*/
                customHandler.postDelayed(this, 1000);
            }
        };

        customHandler.postDelayed(updateTimerThread, 0);

        Thread thread=new Thread();
        thread.execute();



        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        incrementing = false;
        decrementing = false;
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

        targettemp = 5f;

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton:
                if (checked){
                    temporary=true;permament=false;schedule=false;
                    try {
                        HeatingSystem.put("weekProgramState","on");
                    } catch (InvalidInputValueException e) {
                        e.printStackTrace();
                    }
                    getswitch();
                }
                    break;
            case R.id.radioButton2:
                if (checked){
                    temporary=false;permament=true;schedule=false;
                    try {
                        HeatingSystem.put("weekProgramState","off");
                    } catch (InvalidInputValueException e) {
                        e.printStackTrace();
                    }
                }
                    break;
            case R.id.radioButton3:
                if (checked){
                    temporary=false;permament=false;schedule=true;
                    try {
                        HeatingSystem.put("weekProgramState","on");
                    } catch (InvalidInputValueException e) {
                        e.printStackTrace();
                    }
                    schedule();
                }
                    break;
        }

    }
    public void getswitch(){
        try {
            int currenttime=-10;
            String currentday;
            WeekProgram wpg = null;

            do {
                currenttime = Integer.parseInt(HeatingSystem.get("time").replace(":", ""));
                currentday=HeatingSystem.get("day");
                try {
                    wpg=HeatingSystem.getWeekProgram();
                } catch (CorruptWeekProgramException e) {
                    e.printStackTrace();
                }
            }while(currenttime==-10||currentday==null||wpg==null);

            switch (currentday){
                case "Monday":getswitch2(wpg.data.get("Monday"),currenttime);
                    break;
                case "Tuesday":getswitch2(wpg.data.get("Tuesday"),currenttime);
                    break;
                case "Wednesday":getswitch2(wpg.data.get("Wednesday"),currenttime);
                    break;
                case "Thursday":getswitch2(wpg.data.get("Thursday"),currenttime);
                    break;
                case "Friday":getswitch2(wpg.data.get("Friday"),currenttime);
                    break;
                case "Saturday":getswitch2(wpg.data.get("Saturday"),currenttime);
                    break;
                case "Sunday":getswitch2(wpg.data.get("Sunday"),currenttime);
                    break;
            }

        } catch (ConnectException e) {
            e.printStackTrace();
        }

    }

    public void getswitch2(ArrayList<Switch> list,int currenttime){
        int i=0;
        while(i<list.size()   ){
            if(!list.get(i).getState()){
                list.remove(i);
            }
            else{
                i++;
            }
        }
       for(i=0;i<list.size();i++){
           if(currenttime<list.get(i).getTime_Int()){
               time=list.get(i).getTime_Int();
               if(list.get(i).getType().equals("day")){
                   sday=true;
               }
               else{
                   sday=false;
               }
               break;
           }
       }

    }
    public void schedule(){
        getswitch();
        if(!sday){
            try {
                targettemp=Float.parseFloat(HeatingSystem.get("dayTemperature").replace(":",""));
            } catch (ConnectException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                targettemp=Float.parseFloat(HeatingSystem.get("nightTemperature").replace(":",""));
            } catch (ConnectException e) {
                e.printStackTrace();
            }
        }

    }
    public void savetemperatures(View v){
        try {
            if(!daytemp.getText().toString().equals("") && !nighttemp.getText().toString().equals("")) {
                Float daytemperature = Float.parseFloat(daytemp.getText().toString());
                if (daytemperature < 5 || daytemperature > 30) {
                    popup2("day");
                }
                daytemperature = Float.parseFloat(nighttemp.getText().toString());
                if (daytemperature < 5 || daytemperature > 30) {
                    popup2("night");
                } else {
                    HeatingSystem.put("dayTemperature", daytemp.getText().toString());
                    HeatingSystem.put("nightTemperature", nighttemp.getText().toString());
                }
            }
            else{
                popup2("daytemperature and the night");
            }
        } catch (InvalidInputValueException e) {
            e.printStackTrace();
        }
    }
    public void popup2(String day){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("The "+day+"temperature must be between 5 and 30")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void retrievetemperatures(View v){
        try {
            daytemp.setText(HeatingSystem.get("dayTemperature"));
            nighttemp.setText(HeatingSystem.get("nightTemperature"));
        } catch (ConnectException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<Switch> createlist(ArrayList<Switch> list){
        ArrayList<Switch> temp=new ArrayList<Switch>();
        temp.add(new Switch("night", false, "00:00"));
        temp.add(new Switch("day", false, "07:00"));
        temp.add(new Switch("night", false, "08:00"));
        temp.add(new Switch("day", false, "16:00"));
        temp.add(new Switch("night", false, "22:00"));
        temp.add(new Switch("day", false, "23:00"));
        temp.add(new Switch("night", false, "23:00"));
        temp.add(new Switch("day", false, "23:00"));
        temp.add(new Switch("night", false, "23:00"));
        temp.add(new Switch("day", false, "23:00"));
        int i;
        for(i=0;i<list.size();i++){
            String s=list.get(i).getType();
            if(s.equals("day")){
                int k=0;
                while(k<temp.size()){
                    if(temp.get(k).getType().equals("day")){
                        temp.remove(k);
                        break;
                    }
                    k++;
                }
            }
            else{
                int k=0;
                while(k<temp.size()){
                    if(temp.get(k).getType().equals("night")){
                        temp.remove(k);
                        break;
                    }
                    k++;
                }

            }

            temp.add(list.get(i));
        }
        return temp;
        
    }
public void saveprogram(View v){

    String[] days={ "Monday","Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    int i;
    for(i=0;i<days.length;i++){
        wpg.setDefault();
    }
    ArrayList<Switch> test=new ArrayList<Switch>();
    test=createlist(lmonday);
    wpg.setSwitches("Monday",test,test.size());
    test=createlist(ltuesday);
    wpg.setSwitches("Tuesday", test, test.size());
    test=createlist(lwednesday);
    wpg.setSwitches("Wednesday", test, test.size());
    test=createlist(lthursday);
    wpg.setSwitches("Thursday",test,test.size());
    test=createlist(lfriday);
    wpg.setSwitches("Friday",test,test.size());
    test=createlist(lsaterday);
    wpg.setSwitches("Saturday",test,test.size());
    test=createlist(lsunday);
    wpg.setSwitches("Sunday",test,test.size());
    String test32=wpg.toXML();
    //System.out.print(test32);
    HeatingSystem.setWeekProgram(wpg);

}
    public void retrieveprogram(View v){
        try {
            wpg=HeatingSystem.getWeekProgram();
            ArrayList<Switch> temp=new ArrayList<>();
            temp=wpg.data.get("Monday");
            int i=0;
            while(i<temp.size()   ){
                if(!temp.get(i).getState()){
                    temp.remove(i);
                }
                else{
                    i++;
                }
            }
            lmonday.clear();
          lmonday=temp;
            drawswitchesandbars(lmonday,"m");

            temp=wpg.data.get("Tuesday");
            i=0;
            while(i<temp.size()   ){
                if(!temp.get(i).getState()){
                    temp.remove(i);
                }
                else{
                    i++;
                }
            }
            ltuesday.clear();
            ltuesday=temp;
            drawswitchesandbars(ltuesday,"t");

            temp=wpg.data.get("Wednesday");
            i=0;
            while(i<temp.size()   ){
                if(!temp.get(i).getState()){
                    temp.remove(i);
                }
                else{
                    i++;
                }
            }
            lwednesday.clear();
           lwednesday=temp;
            drawswitchesandbars(lwednesday,"w");

            temp=wpg.data.get("Thursday");
            i=0;
            while(i<temp.size()   ){
                if(!temp.get(i).getState()){
                    temp.remove(i);
                }
                else{
                    i++;
                }
            }
            lthursday.clear();
           lthursday=temp;
            drawswitchesandbars(lthursday,"th");

            temp=wpg.data.get("Friday");
            i=0;
            while(i<temp.size()   ){
                if(!temp.get(i).getState()){
                    temp.remove(i);
                }
                else{
                    i++;
                }
            }
            lfriday.clear();
            lfriday=temp;
            drawswitchesandbars(lfriday,"f");

            temp=wpg.data.get("Saturday");
            i=0;
            while(i<temp.size()   ){
                if(!temp.get(i).getState()){
                    temp.remove(i);
                }
                else{
                    i++;
                }
            }
            lsaterday.clear();
          lsaterday=temp;
            drawswitchesandbars(lsaterday,"s");

            temp=wpg.data.get("Sunday");
            i=0;
            while(i<temp.size()   ){
                if(!temp.get(i).getState()){
                    temp.remove(i);
                }
                else{
                    i++;
                }
            }
            lsunday.clear();
            lsunday=temp;
            drawswitchesandbars(lsunday,"su");



        } catch (ConnectException e) {
            e.printStackTrace();
        } catch (CorruptWeekProgramException e) {
            e.printStackTrace();
        }
    }

    public void popup(String day){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You already have the maximum amount of switches (10) on "+day+".")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public int addswitch(ArrayList<Switch> list,Switch s){
        if(list.size()==0){
            list.add(s);
            return list.size()-1;
        }
        else{
            int time=s.getTime_Int();
            int i;
            for(i=0;i<list.size();i++){
                if(time<list.get(i).getTime_Int()){
                    list.add(i,s);
                    return i;
                }
            }
            list.add(s);
            return list.size()-1;
        }

    }

    public void drawswitchesandbars(ArrayList<Switch> list, String day){
        int j=0;
        while(j<25){
            if (j * 100 < 1000) {
                ImageView tem = (ImageView) scroller.findViewWithTag(day+"image" + "0" + j);
                tem.setImageResource(R.drawable.emptyimage);
            } else {
                ImageView tem = (ImageView) scroller.findViewWithTag(day+"image" + j);
                tem.setImageResource(R.drawable.emptyimage);
            }
            j++;
        }

        int i;
        boolean night=true;
        int k=0;
        for(i=0;i<list.size();i++){
            int time=list.get(i).getTime_Int();
            if(time==0){
                night=!night;
            }
            else {
                while (k * 100 < time) {
                    if (night) {
                        if (k * 100 < 1000) {
                            ImageView tem = (ImageView) scroller.findViewWithTag(day+"day" + "0" + k);
                            tem.setImageResource(R.drawable.nightline);
                        } else {
                            ImageView tem = (ImageView) scroller.findViewWithTag(day+"day" + k);
                            tem.setImageResource(R.drawable.nightline);
                        }
                    } else {
                        if (k * 100 < 1000) {
                            ImageView tem = (ImageView) scroller.findViewWithTag( day+"day"+"0" + k);
                            tem.setImageResource(R.drawable.dayline);
                        } else {
                            ImageView tem = (ImageView) scroller.findViewWithTag(day+"day" + k);
                            tem.setImageResource(R.drawable.dayline);
                        }
                    }
                    k++;
                }
                int l=k;
                if (k * 100 < 1000) {
                    ImageView tem = (ImageView) scroller.findViewWithTag(day+"image" + "0" + k);
                    tem.setImageResource(R.drawable.button);
                } else {
                    ImageView tem = (ImageView) scroller.findViewWithTag(day+"image" + k);
                    tem.setImageResource(R.drawable.button);
                }


                night = !night;
            }
        }
        while(k<24){
            if(night){
                if(k*100<1000) {
                    ImageView tem = (ImageView) scroller.findViewWithTag(day+"day"+"0" + k );
                    tem.setImageResource(R.drawable.nightline);
                }
                else {
                    ImageView tem = (ImageView) scroller.findViewWithTag(day+"day"+ k );
                    tem.setImageResource(R.drawable.nightline);
                }
            }
            else{
                if(k*100<1000) {
                    ImageView tem = (ImageView) scroller.findViewWithTag(day+"day"+"0" + k );
                    tem.setImageResource(R.drawable.dayline);
                }
                else {
                    ImageView tem = (ImageView) scroller.findViewWithTag(day +"day"+ k );
                    tem.setImageResource(R.drawable.dayline);
                }
            }
            k++;
        }
    }
    public void setbars(ArrayList<Switch> list,String day){
        boolean night=true;
        int k=0;
        int l=0;
        int i;
        for(i=0;i<list.size();i++){
            int time=list.get(i).getTime_Int();
            if(time==0){
                night=!night;
            }
            else {
                while (k * 100 < time) {
                    if (night) {
                        if (k * 100 < 1000) {
                            ImageView tem = (ImageView) scroller.findViewWithTag(day + "0" + k);
                            tem.setImageResource(R.drawable.nightline);
                        } else {
                            ImageView tem = (ImageView) scroller.findViewWithTag(day + k);
                            tem.setImageResource(R.drawable.nightline);
                        }
                    } else {
                        if (k * 100 < 1000) {
                            ImageView tem = (ImageView) scroller.findViewWithTag( day+"0" + k);
                            tem.setImageResource(R.drawable.dayline);
                        } else {
                            ImageView tem = (ImageView) scroller.findViewWithTag(day + k);
                            tem.setImageResource(R.drawable.dayline);
                        }
                    }
                    k++;
                }

                night = !night;
            }
        }
        while(k<24){
            if(night){
                if(k*100<1000) {
                    ImageView tem = (ImageView) scroller.findViewWithTag(day+"0" + k );
                    tem.setImageResource(R.drawable.nightline);
                }
                else {
                    ImageView tem = (ImageView) scroller.findViewWithTag(day+ k );
                    tem.setImageResource(R.drawable.nightline);
                }
            }
            else{
                if(k*100<1000) {
                    ImageView tem = (ImageView) scroller.findViewWithTag(day+"0" + k );
                    tem.setImageResource(R.drawable.dayline);
                }
                else {
                    ImageView tem = (ImageView) scroller.findViewWithTag(day + k );
                    tem.setImageResource(R.drawable.dayline);
                }
            }
            k++;
        }

    }
    public void checkswitches( ArrayList<Switch> list, int i, String day){
        int k;
        for(k=i;k<list.size();k++){
            String s=list.get(k).getType().toString();
            if(s.equals("day")){
                list.get(k).setType("night");
            }
            else{
                list.get(k).setType("day");
            }
        }
        k=1000;
        setbars(list, day);
    }
    public void removeswitch(ArrayList<Switch> list, int i, String day){
        list.remove(i);
       checkswitches(list,i, day);
    }
   public void switchbutton(View k){
        ImageView v= (ImageView) k;
       String name=v.getTag().toString();
       Switch temp=new Switch("day",true,name.replaceAll("[a-z]","")+":00");
        if(v.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.emptyimage).getConstantState())){
            if(name.replaceAll("[0-9]","").equals("mimage")){
                if(monday<10 ) {
                    v.setImageResource(R.drawable.button);
                    int place=addswitch(lmonday,temp);
                   if(monday==0){
                     temp.setType("day");
                       setbars(lmonday, "mday");
                   }
                    else {
                        if(place%2==0){
                            lmonday.get(place).setType("day");
                            checkswitches(lmonday,place+1,"mday");
                        }
                       else{
                            lmonday.get(place).setType("night");
                            checkswitches(lmonday,place+1,"mday");
                        }
                   }
                    monday++;


                }
                else{
                    popup("Monday");
                }
            }
           else if(name.replaceAll("[0-9]", "").equals("timage")){
                if(tuesday<10) {
                    v.setImageResource(R.drawable.button);
                    int place=addswitch(ltuesday,temp);
                    if(tuesday==0){
                        temp.setType("day");
                        setbars(ltuesday, "tday");
                    }
                    else {
                        if(place%2==0){
                            ltuesday.get(place).setType("day");
                            checkswitches(ltuesday,place+1,"tday");
                        }
                        else{
                            ltuesday.get(place).setType("night");
                            checkswitches(ltuesday,place+1,"tday");
                        }
                    }
                    tuesday++;
                }
                else{
                    popup("Tuesday");
                }
            }
            else if(name.replaceAll("[0-9]","").equals("wimage")){
                if(wednesday<10) {
                    v.setImageResource(R.drawable.button);
                    int place=addswitch(lwednesday,temp);
                    if(wednesday==0){
                        temp.setType("day");
                        setbars(lwednesday, "wday");
                    }
                    else {
                        if(place%2==0){
                            lwednesday.get(place).setType("day");
                            checkswitches(lwednesday,place+1,"wday");
                        }
                        else{
                            lwednesday.get(place).setType("night");
                            checkswitches(lwednesday,place+1,"wday");
                        }
                    }
                    wednesday++;
                }
                else{
                    popup("Wednesday");
                }
            }  else if(name.replaceAll("[0-9]","").equals( "thimage")){
                if(thursday<10) {
                    v.setImageResource(R.drawable.button);
                    int place=addswitch(lthursday,temp);
                    if(thursday==0){
                        temp.setType("day");
                        setbars(lthursday,  "thday" );
                    }
                    else {
                        if(place%2==0){
                            lthursday.get(place).setType("day");
                            checkswitches(lthursday,place+1, "thday" );
                        }
                        else{
                            lthursday.get(place).setType("night");
                            checkswitches(lthursday,place+1,"thday");
                        }
                    }
                    thursday++;
                }
                else{
                    popup("Thursday");
                }
            }
            else if(name.replaceAll("[0-9]","").equals("fimage")){
                if(friday<10) {
                    v.setImageResource(R.drawable.button);
                    int place=addswitch(lfriday,temp);
                    if(friday==0){
                        temp.setType("day");
                        setbars(lfriday,"fday");
                    }
                    else {
                        if(place%2==0){
                            lfriday.get(place).setType("day");
                            checkswitches(lfriday,place+1, "fday");
                        }
                        else{
                            lfriday.get(place).setType("night");
                            checkswitches(lfriday,place+1,  "fday");
                        }
                    }
                    friday++;
                }
                else{
                    popup("Friday");
                }
            }
            else if(name.replaceAll("[0-9]","").equals("simage")){
                if(saturday<10) {
                    v.setImageResource(R.drawable.button);
                    int place=addswitch(lsaterday,temp);
                    if(saturday==0){
                        temp.setType("day");
                        setbars(lsaterday,"sday");
                    }
                    else {
                        if(place%2==0){
                            lsaterday.get(place).setType("day");
                            checkswitches(lsaterday,place+1, "sday");
                        }
                        else{
                            lsaterday.get(place).setType("night");
                            checkswitches(lsaterday,place+1,"sday");
                        }
                    }
                    saturday++;
                }
                else{
                    popup("Saterday");
                }
            }
            else if(name.replaceAll("[0-9]","").equals("suimage")){
                if(sunday<10) {
                    v.setImageResource(R.drawable.button);
                    int place=addswitch(lsunday,temp);
                    if(sunday==0){
                        temp.setType("day");
                        setbars(lsunday,"suday");
                    }
                    else {
                        if(place%2==0){
                            lsunday.get(place).setType("day");
                            checkswitches(lsunday,place+1,"suday");
                        }
                        else{
                            lsunday.get(place).setType("night");
                            checkswitches(lsunday,place+1,"suday");
                        }
                    }
                    sunday++;
                }
                else{
                    popup("Sunday");
                }

            }


        }
        else if(v.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.button).getConstantState())) {
                if (name.replaceAll("[0-9]","").equals("mimage")) {
                    int i;
                    for (i=0;i<lmonday.size();i++){
                        if(lmonday.get(i).getTime().equals(temp.getTime())){

                                removeswitch(lmonday,i, "mday");

                        }
                    }
                    monday--;
                    v.setImageResource(R.drawable.emptyimage);
                }
           else if ( name.replaceAll("[0-9]","").equals( "timage")) {
                    int i;
                    for (i=0;i<ltuesday.size();i++){
                        if(ltuesday.get(i).getTime().equals(temp.getTime())){

                            removeswitch(ltuesday,i, "tday");

                        }
                    }
                tuesday--;
                v.setImageResource(R.drawable.emptyimage);
            }
            else if (name.replaceAll("[0-9]","").equals("wimage")) {
                    int i;
                    for (i=0;i<lwednesday.size();i++){
                        if(lwednesday.get(i).getTime().equals(temp.getTime())){

                            removeswitch(lwednesday,i, "wday");

                        }
                    }
                wednesday--;
                v.setImageResource(R.drawable.emptyimage);
            }
            else if (name.replaceAll("[0-9]","").equals ("thimage")) {
                    int i;
                    for (i=0;i<lthursday.size();i++){
                        if(lthursday.get(i).getTime().equals(temp.getTime())){

                            removeswitch(lthursday,i, "thday");

                        }
                    }
                thursday--;
                v.setImageResource(R.drawable.emptyimage);
            }
            else if (name.replaceAll("[0-9]","").equals("fimage")) {
                    int i;
                    for (i=0;i<lfriday.size();i++){
                        if(lfriday.get(i).getTime().equals(temp.getTime())){

                            removeswitch(lfriday,i, "fday");

                        }
                    }
                friday--;
                v.setImageResource(R.drawable.emptyimage);
            }
            else if (name.replaceAll("[0-9]","").equals("simage")) {
                    int i;
                    for (i=0;i<lsaterday.size();i++){
                        if(lsaterday.get(i).getTime().equals(temp.getTime())){

                            removeswitch(lsaterday,i, "sday");

                        }
                    }
                saturday--;
                v.setImageResource(R.drawable.emptyimage);
            }
            else if (name.replaceAll("[0-9]","").equals("suimage")) {
                    int i;
                    for (i=0;i<lsunday.size();i++){
                        if(lsunday.get(i).getTime().equals(temp.getTime())){

                            removeswitch(lsunday,i, "suday");

                        }
                    }
                sunday--;
                v.setImageResource(R.drawable.emptyimage);
            }
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
        if(targettemp==currenttemp){
            arrowup.setVisibility(View.INVISIBLE);
            arrowdown.setVisibility(View.INVISIBLE);
        }
        else if(targettemp>currenttemp){
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
        mViewPager.setCurrentItem(tab.getPosition());
       /* if(tab.getText().equals("HOME")){
            updateTimerThread.run();
        }*/
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
       /* if(tab.getText().equals("HOME")){
            try {

                updateTimerThread.wait(999999999999999999L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
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


    public class Tab1 extends Fragment {
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
            if(targettemp==currenttemp){
                arrowup.setVisibility(View.INVISIBLE);
                arrowdown.setVisibility(View.INVISIBLE);
            }
            else if(targettemp>currenttemp){
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
            View temporay=(View) findViewById(R.id.radioButton);
            onRadioButtonClicked(temporay);
            rg =(RadioGroup) findViewById(R.id.radioGroup);
            arrowup=(ImageView) findViewById(R.id.arrowup);
            arrowdown=(ImageView) findViewById(R.id.arrowdown);
            arrowup.setVisibility(View.INVISIBLE);
            arrowdown.setVisibility(View.INVISIBLE);
        slider=(CircularSeekBar) findViewById(R.id.circularSeekBar1);
            slider.setMax(250);
            ctemp=(TextView) findViewById(R.id.ctemp);
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
            if(targettemp<5.0){
                targettemp=5f;
            }

            testing.setText(targettemp+" \u2103");

            updateUI();
        }
    }

    public class Tab2 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public   Tab2 newInstance(int sectionNumber) {
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
        @Override
        public void onViewCreated(View view, Bundle savedInstanceState){
            scroller=(HorizontalScrollView) findViewById(R.id.scollview);


        }

    }

    public class Tab3 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public  Tab3 newInstance(int sectionNumber) {
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
        @Override
        public void onViewCreated(View view, Bundle savedInstanceState){
            daytemp=(EditText) findViewById(R.id.editText);
            nighttemp=(EditText) findViewById(R.id.editText2);


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

    protected class Thread extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] params) {

            int k=params.length;
            while(true) {
                try{
                    String temp=HeatingSystem.get("currentTemperature");
                    if(temp!=null) {
                        currenttemp = Float.parseFloat(temp);
                    }
                    if(day!=null && !permament){
                        String test=HeatingSystem.get("day");
                        if(test!=null) {
                            if (day.compareTo(test) != 0) {

                                String temp2=HeatingSystem.get( "nightTemperature");
                                if(temp2!=null) {
                                    targettemp = Float.parseFloat(temp2);
                                    HeatingSystem.put("currentTemperature",""+targettemp);
                                }
                                day = HeatingSystem.get("day");
                            }
                        }
                    }
                    if(time!=-10 && temporary){
                        String test=HeatingSystem.get("time").replace(":","");
                        Integer test2=Integer.parseInt(test);
                            if(time<=test2){
                                temporary=false;schedule=true;
                                change=true;
                                if(sday){
                                    String temp3=HeatingSystem.get("dayTemperature");
                                    targettemp=Float.parseFloat(temp3);
                                }
                                else{
                                    String temp3=HeatingSystem.get("nightTemperature");
                                    targettemp=Float.parseFloat(temp3);
                                }
                            }

                    }
                    if(day==null){
                        day=HeatingSystem.get("day");
                    }
                    HeatingSystem.put("currentTemperature",""+targettemp);
                } catch (InvalidInputValueException e) {
                    e.printStackTrace();
                } catch (ConnectException e) {
                    e.printStackTrace();
                }


            }
        }


    }


}




