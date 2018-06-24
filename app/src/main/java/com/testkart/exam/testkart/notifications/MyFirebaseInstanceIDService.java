package com.testkart.exam.testkart.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.testkart.exam.edu.helper.SessionManager;
import com.testkart.exam.edu.models.Consts;
import com.testkart.exam.edu.rest.ApiClient;
import com.testkart.exam.edu.rest.ApiInterface;
import com.testkart.exam.testkart.study_material.StudyMaterialResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by elfemo on 24/7/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();


    public static final String KEY_FCM_UPDATE_NEEDED = "fcm_update_needed";
    public static final String KEY_FCM_TOKEN = "fcm_token";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        storeRegIdInPref(this, refreshedToken, true);

        // sending reg id to your server
        //sendRegistrationToServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        /*Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);*/
    }

    public static void sendRegistrationToServer(final String token, final Context context) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);

        SessionManager session = new SessionManager(context);

        if(session.isLoggedIn()){

            String userId = session.getUserDetails().get(SessionManager.KEY_STUDENT_ID);

            if (ApiClient.isNetworkAvailable(context)) {

                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);


                Call<StudyMaterialResponse> call = apiService.updateUserToken(userId, token);

                call.enqueue(new Callback<StudyMaterialResponse>() {
                    @Override
                    public void onResponse(Call<StudyMaterialResponse> call, Response<StudyMaterialResponse> response) {

                        int code = response.code();

                        if (code == 200) {

                            if (response.body().getStatus()) {

                                storeRegIdInPref(context, token, false);

                                Log.v(TAG, "User token updated successfully");

                            }else{

                                Log.v(TAG, response.body().getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StudyMaterialResponse> call, Throwable t) {

                        Log.v(TAG, t.getMessage());
                    }
                });



            } else {

                Log.v(TAG, Consts.NETWORK_ERROR);

                //Toast.makeText(this, Consts.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

            }


        }else{

            //do nothing user not login
            Log.v(TAG, "do nothing user not login");
        }
    }

    public static void storeRegIdInPref(Context context, String token, boolean needUpdate) {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_FCM_TOKEN, token);
        editor.putBoolean(KEY_FCM_UPDATE_NEEDED, needUpdate);
        editor.commit();
    }
}