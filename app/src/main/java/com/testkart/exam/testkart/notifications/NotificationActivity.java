package com.testkart.exam.testkart.notifications;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.testkart.exam.R;
import com.testkart.exam.edu.TestKartApp;
import com.testkart.exam.edu.helper.DBHelper;
import com.testkart.exam.edu.helper.SessionManager;

import java.util.ArrayList;

/**
 * Created by elfemo on 27/7/17.
 */

public class NotificationActivity extends AppCompatActivity{

    private DBHelper dbHelper;
    private SessionManager session;

    private Context context = this;
    private ListView notificationListView;
    private TextView emptyTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        if(TestKartApp.drmON){

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My Notifications");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        initialization();
    }

    private void initialization() {

        dbHelper = new DBHelper(this);
        session = new SessionManager(this);

        notificationListView = (ListView)findViewById(R.id.notificationListView);
        emptyTextView = (TextView)findViewById(R.id.emptyTextView);

        //get latest 5 notification from db

        ArrayList<DataNotification> dataList;

        if(getIntent() != null && getIntent().getBooleanExtra("IS_GUEST", false)){

            dataList  = dbHelper.getNotifications("\'guest\'", 5);
            dbHelper.markNotificationView("\'guest\'");

        }else{

            dataList = dbHelper.getNotifications(session.getUserDetails().get(SessionManager.KEY_STUDENT_ID), 5);
        }



        /*dataList.add(new DataNotification("SBI Information Technology", "As mentioned, DateUtils.isToday(d.getTime()) will work for determining if Date d is today. But some responses here don't actually answer how to determine if a date was yesterday.", "Today at 11:35", "", false));
        dataList.add(new DataNotification("Batman vs Superman", "You can do that easily using android.text.format.DateFormat class.", "Today at 11:35", "", false));
        dataList.add(new DataNotification("Party Tonight", "it would be nice if orgzly have a timestamp feature, instead of adding CREATED value. what if orgzly have a feature of adding timestamp with syntax e.g \"#now\", \"#hour\", \"#today\". so we can choose which notes to give a timestamp. I know this feature from fiinote, it has feature of adding timestamp by using \"#now\" command.", "Today at 11:35", "", false));
        dataList.add(new DataNotification("Thor Rangaknock", "I'm not sure about typing on mobile device, but a toolbar for quickly inserting those is planned (see 2. in this comment).", "Today at 11:35", "", true));
        dataList.add(new DataNotification("Lights out", "You can do that easily using android.text.format.DateFormat class.", "Today at 11:35", "", false));
*/
        notificationListView.setAdapter(new NotificationAdapter(context, dataList));
        dbHelper.markNotificationView(session.getUserDetails().get(SessionManager.KEY_STUDENT_ID));

        if(dataList.isEmpty()){

            emptyTextView.setVisibility(View.VISIBLE);
            notificationListView.setVisibility(View.GONE);

        }else{

            notificationListView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
