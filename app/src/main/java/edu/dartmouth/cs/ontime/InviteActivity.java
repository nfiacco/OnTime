package edu.dartmouth.cs.ontime;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class InviteActivity extends Activity {

    public ListView mList;
    static final String[] invites = new String[] {"Bowling \nNick invited you: accepted"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        getWindow().setBackgroundDrawableResource(R.drawable.bokeh1copy3);

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.hide();

        mList = (ListView)findViewById(R.id.list);

        mList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, invites));

        MainActivity.ListUtils.setDynamicHeight(mList);
    }
}