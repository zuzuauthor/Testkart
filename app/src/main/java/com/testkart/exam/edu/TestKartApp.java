package com.testkart.exam.edu;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.devs.acr.AutoErrorReporter;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.stetho.DumperPluginsProvider;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.thefinestartist.Base;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by testkart on 12/4/17.
 */

public class TestKartApp extends MultiDexApplication {

    private static TestKartApp instance;

    public static boolean drmON;

    @Override
    public void onCreate() {
        super.onCreate();

        AutoErrorReporter.get(this)
                .setEmailAddresses("admin@testkart.com")
                .setEmailSubject("Testkart Crash Report")
                .start();

        Base.initialize(this);

      /* Stetho.newInitializerBuilder(this)
               .enableDumpapp(new MyDumperPlugin(this))
               .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
               .build();*/

        Stetho.initializeWithDefaults(this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        instance = this;

        drmON = false;

        // Enabling database for resume support even after the application is killed:
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(getApplicationContext(), config);

    }



    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }


    public static TestKartApp getInstance(){
        return instance;
    }

    class MyDumperPlugin implements DumperPluginsProvider {

        private final Context context;

        public MyDumperPlugin(Context context){

            this.context = context;
        }

        @Override
        public Iterable<DumperPlugin> get() {

            ArrayList<DumperPlugin> plugins = new ArrayList<>();

            for (DumperPlugin defaultPlugins:Stetho.defaultDumperPluginsProvider(context).get()
                 ) {


                plugins.add(defaultPlugins);
            }

            //custom plugins
            //plugins.add(new HelloWorldDumperPlugins());

            return plugins;
        }
    }


    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if(appDir.exists()){
            String[] children = appDir.list();
            for(String s : children){
                if(!s.equals("lib")){
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "File /data/data/APP_PACKAGE/" + s +" DELETED");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

}
