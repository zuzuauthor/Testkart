package com.testkart.exam.testkart.notifications;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.testkart.exam.edu.SplashActivity;
import com.testkart.exam.edu.helper.DBHelper;
import com.testkart.exam.edu.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by elfemo on 24/7/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {

                String notifyId = remoteMessage.getMessageId();

                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json, notifyId);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json, String messageId) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");
            boolean sticky = data.getBoolean("sticky");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);


            boolean imageContain = false;

            //insert into db
            DBHelper dbHelper = new DBHelper(this);
            SessionManager session = new SessionManager(this);

            if(session.isLoggedIn()){

                String userId = session.getUserDetails().get(SessionManager.KEY_STUDENT_ID);
                DataNotification dataNoti = new DataNotification();
                dataNoti.setDateTime(timestamp);
                dataNoti.setNotificationId(System.currentTimeMillis()+"");
                dataNoti.setImageUrl(imageUrl);
                dataNoti.setMessage(message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    dataNoti.setIncludeImage(false);

                    imageContain = false;

                } else {
                    // image is present, show notification with image
                    dataNoti.setIncludeImage(true);

                    imageContain = true;
                }

                dataNoti.setTitle(title);

                dbHelper.addNotification(userId, dataNoti);

            }else{

                //no need to insert in db
                String userId = "guest";//session.getUserDetails().get(SessionManager.KEY_STUDENT_ID);
                DataNotification dataNoti = new DataNotification();
                dataNoti.setDateTime(timestamp);
                dataNoti.setNotificationId(System.currentTimeMillis()+"");
                dataNoti.setImageUrl(imageUrl);
                dataNoti.setMessage(message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    dataNoti.setIncludeImage(false);

                    imageContain = false;

                } else {
                    // image is present, show notification with image
                    dataNoti.setIncludeImage(true);

                    imageContain = true;
                }

                dataNoti.setTitle(title);

                dbHelper.addNotification(userId, dataNoti);
            }


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                pushNotification.putExtra("title", title);
                pushNotification.putExtra("image_url", imageUrl);
                pushNotification.putExtra("contain_image", imageContain);
                pushNotification.putExtra("time", timestamp);
                pushNotification.putExtra("push", true);

                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), SplashActivity.class);
                //resultIntent.putExtra("message", message);

                resultIntent.putExtra("message", message);
                resultIntent.putExtra("title", title);
                resultIntent.putExtra("image_url", imageUrl);
                resultIntent.putExtra("contain_image", imageContain);
                resultIntent.putExtra("time", timestamp);
                resultIntent.putExtra("push", true);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent, sticky);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl, sticky);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent, boolean sticky) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, "", sticky);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl, boolean sticky) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl, sticky);
    }

}