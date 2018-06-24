package com.testkart.exam.edu.rest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.testkart.exam.edu.register.GroupResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by testkart on 12/4/17.
 */

public class ApiClient {

    //public static final String BASE_URL = "http://elfemo.com/envato/demos/edu-ibps/";

    public static final String BASE_URL = "http://www.testkart.com/";

    //http://www.testkart.com/rest/Users/login.json


    /*
 http://elfemo.com/envato/demos/edu-ibps
    http://elfemo.com/envato/demos/edu-ibps/rest/Packages/index.json

    John Developer (johndeveloper2008@gmail.com)
http://elfemo.com/demo/server3/api/rest/Exams/start/1.json?public_key=89479F981EFB90A&private_key=e1d2bcf08fc796c7dcb0d45b146347d76abd930a1face43ed96563e55e88d712
     */

    //public static final String BASE_URL = "http://api.themoviedb.org/3/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL).client(getHttpClient())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(GSON))
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }


    private static OkHttpClient getHttpClient() {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60,TimeUnit.SECONDS).build();

        return client;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private static final Gson gson = new GsonBuilder()
            .setLenient()
            .create();


    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(GroupResponse.class, new CustomDeserializer())
            .create();

    public static class CustomDeserializer implements JsonDeserializer<List<Map<Integer, String>>> {

        @Override
        public List<Map<Integer, String>> deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            List<Map<Integer, String>> randomList = new ArrayList<>();
            JsonObject parentJsonObject = element.getAsJsonObject();
            Map<Integer, String> childMap;
            for(Map.Entry<String, JsonElement> entry : parentJsonObject.entrySet()){
                childMap = new HashMap<>();
                for(Map.Entry<String, JsonElement> entry1 : entry.getValue().getAsJsonObject().entrySet()){
                    childMap.put(Integer.parseInt(entry1.getKey()), entry1.getValue().toString());
                }
                randomList.add(childMap);
            }
            return randomList;
        }
    }


}
