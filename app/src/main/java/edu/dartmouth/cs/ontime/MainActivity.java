package edu.dartmouth.cs.ontime;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This is the mainActivity, it will be a listview of all the current events corresponding to that
 * user. You can also navigate to the other activities such as CreateEvent, Invites, and Settings
 */
public class MainActivity extends Activity implements CreateEvent.CreateFinished{

    public static final String TAG = "MainActivity";

    private ArrayList<Event> upcomingEvents;
    private ListView mListToday,mListTomorrow,mListThisweek;
    private Context mContext;
    private ImageButton createEventButton, invitesButton, settingsButton;
    private Button testEventDisplayButton;
    private ArrayList<Event> todayArray = new ArrayList<>();
    private ArrayList<Event> tomorrowArray = new ArrayList<>();
    private ArrayList<Event> thisWeekArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setBackgroundDrawableResource(R.drawable.bokeh1copy3);

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.hide();

        upcomingEvents = new ArrayList<Event>();
        mContext = this;

        settingsButton = (ImageButton)findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SettingsPage.class);
                startActivity(intent);
            }
        });

        invitesButton = (ImageButton)findViewById(R.id.invitesButton);
        invitesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InviteActivity.class);
                startActivity(intent);
            }
        });

        createEventButton = (ImageButton)findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CreateEvent.class);
                startActivity(intent);
            }
        });


        App.setContext(this);

        todayArray = new ArrayList<>();
        tomorrowArray = new ArrayList<>();
        thisWeekArray = new ArrayList<>();

        query();
    }

    @Override
    protected void onResume() {
        super.onResume();
        query();
    }

    public void createEventDone(){
        query();
    }

    /**
     * Queries all of the upcoming events corresponding to that user.
     */
    public void query() {
        ParseUser me = ParseUser.getCurrentUser();
        ArrayList<String> upcomingString = (ArrayList<String>) me.get("accepted");

        ParseQuery query = ParseQuery.getQuery("event");
        query.whereContainedIn("objectId", upcomingString);
        try{
            upcomingEvents = (ArrayList<Event>) query.find();
        }
        catch (ParseException e){
        }

        loadEvents();
    }

    public void loadEvents() {

        todayArray.clear();
        tomorrowArray.clear();
        thisWeekArray.clear();
        for (Event event : upcomingEvents) {

            Calendar date = new GregorianCalendar();
            date.setTime(event.getDate("date"));
            long currentTime = System.currentTimeMillis();
            Calendar today = Calendar.getInstance();
            today.setTimeInMillis(currentTime);
            if (date == null) {
                return;
            }
            else {
                if (date.get(Calendar.DATE) == today.get(Calendar.DATE)) {
                    todayArray.add(event);
                }
                else if (date.get(Calendar.DATE) == today.get(Calendar.DATE) +1) {
                    tomorrowArray.add(event);
                }
                //this line will return -1 if today.getTime is before the last day of the week
                else if ((today.getTime().compareTo(getStartEndOFWeek(date.get(Calendar.WEEK_OF_YEAR), date.get(Calendar.YEAR))) == -1) && (today.get(Calendar.DATE) < date.get(Calendar.DATE))) {
                    thisWeekArray.add(event);
                }
                else {
                    //else, add to "upcoming events" field at bottom
                }
            }

        }

        mListToday = (ListView)findViewById(R.id.listTd);
        mListToday.setBackgroundColor(Color.WHITE);
        mListTomorrow = (ListView)findViewById(R.id.listTm);
        mListTomorrow.setBackgroundColor(Color.WHITE);
        mListThisweek = (ListView)findViewById(R.id.listTw);
        mListThisweek.setBackgroundColor(Color.WHITE);

        mListToday.setAdapter(new EventAdapter
                (this, android.R.layout.simple_list_item_1, todayArray));
        mListTomorrow.setAdapter(new EventAdapter(this, android.R.layout.simple_list_item_1, tomorrowArray));
        mListThisweek.setAdapter(new EventAdapter(this, android.R.layout.simple_list_item_1, thisWeekArray));

        ListUtils.setDynamicHeight(mListToday);
        ListUtils.setDynamicHeight(mListTomorrow);
        ListUtils.setDynamicHeight(mListThisweek);

        //when user selects event, fire EventDisplayActivity
        mListToday.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // Get the event, positioned to the corresponding row in the result set
                Event event = (Event) parent.getAdapter().getItem(position);
                Intent intent = createIntentFromEvent(event);
                startActivity(intent);
            }
        });
        mListTomorrow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // Get the event, positioned to the corresponding row in the result set
                Event event = (Event) parent.getAdapter().getItem(position);
                Intent intent = createIntentFromEvent(event);
                startActivity(intent);
            }
        });
        mListThisweek.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // Get the event, positioned to the corresponding row in the result set
                Event event = (Event) parent.getAdapter().getItem(position);
                Intent intent = createIntentFromEvent(event);
                startActivity(intent);
            }
        });
    }

    public Date getStartEndOFWeek(int enterWeek, int enterYear){

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.WEEK_OF_YEAR, enterWeek);
        calendar.set(Calendar.YEAR, enterYear);

        SimpleDateFormat formatter = new SimpleDateFormat("ddMMM yyyy"); // PST`
        Date startDate = calendar.getTime();
        String startDateInStr = formatter.format(startDate);

        calendar.add(Calendar.DATE, 6);
        Date enddate = calendar.getTime();
        String endDaString = formatter.format(enddate);

        return enddate;

    }

    private Intent createIntentFromEvent(Event event) {
        String objectId = event.getObjectId();
        String title = event.getTitle();
        ArrayList<String> attendees = event.getAcceptedList();

        ParseGeoPoint location = event.getLocation();
        Location sendLoc = new Location("any string");
        sendLoc.setLatitude(location.getLatitude());
        sendLoc.setLongitude(location.getLongitude());

        Date eventDate = event.getDate();
        String stringEventDate = eventDate.toString();

        Date eventTime = event.getTime();
        String stringEventTime = eventTime.toString();

        Intent intent = new Intent(getApplicationContext(), EventDisplayActivity.class);
        intent.putExtra(EventDisplayActivity.OBJECT_ID, objectId);
        intent.putExtra(EventDisplayActivity.TITLE, title);
        intent.putExtra(EventDisplayActivity.ATTENDEES, attendees);
        intent.putExtra(EventDisplayActivity.LOCATION, sendLoc);
        intent.putExtra(EventDisplayActivity.DATE, stringEventDate);
        intent.putExtra(EventDisplayActivity.TIME, stringEventTime);

        return intent;
    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            else {
                int height = 0;
                int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
                for (int i = 0; i < mListAdapter.getCount(); i++) {
                    View listItem = mListAdapter.getView(i, null, mListView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                    height += listItem.getMeasuredHeight();
                }
                ViewGroup.LayoutParams params = mListView.getLayoutParams();
                params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
                mListView.setLayoutParams(params);
                mListView.requestLayout();
            }
        }
    }

    private class EventAdapter extends ArrayAdapter<Event>{
        private EventAdapter(Context context, int resource, ArrayList<Event> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            }

            Event event = getItem(position);

            TextView text = (TextView) convertView.findViewById(android.R.id.text1);
            text.setText(event.getTitle());

            return convertView;
        }
    }

}
