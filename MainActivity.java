package com.kramer.smauthenticator;

//import static com.kramer.smauthenticator.javaClass.MainClass.getUniqueDeviceID;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.kramer.smauthenticator.Activity.App_Module_Fragment;
import com.kramer.smauthenticator.Activity.BarcodeCaptureFragment;
import com.kramer.smauthenticator.Activity.CApplicationFragment;
import com.kramer.smauthenticator.Activity.Office_365_Fragment;
import com.kramer.smauthenticator.Activity.ShareInfoFragment;
import com.kramer.smauthenticator.javaClass.MainClass;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    MainClass main;

    RelativeLayout room, applications;
    Button btnRoom, btnApps;
    TextView txtRoom, txtApps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = MainClass.getMainObj();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        main.currentContext = MainActivity.this;
        main.strUniqueDeviceID = main.getUniqueDeviceID();
        Log.v("strUniqueDeviceID", "strUniqueDeviceID " + main.strUniqueDeviceID);
        setContentView(R.layout.activity_main);
//        hideBottomBar(MainActivity.this);
        room = findViewById(R.id.ll_Room_Root);
        applications = findViewById(R.id.ll_App_Root);
        btnRoom = findViewById(R.id.btn_room);
        txtRoom = findViewById(R.id.txt_room);
        btnApps = findViewById(R.id.btn_apps);
        txtApps = findViewById(R.id.txt_apps);
        btnApps.setBackgroundResource(R.drawable.selected_app);
        txtApps.setTextColor(Color.parseColor("#69FFC3"));
        btnRoom.setBackgroundResource(R.drawable.unselected_room);
        txtRoom.setTextColor(Color.parseColor("#B2B9BF"));
        main.chkPageUIVisibility = false;

        room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnRoom.setBackgroundResource(R.drawable.ic_sharp_room);
                txtRoom.setTextColor(Color.parseColor("#69FFC3"));
                btnApps.setBackgroundResource(R.drawable.ic_appstore);
                txtApps.setTextColor(Color.parseColor("#B2B9BF"));
                // main.setShareInfoFragment(MainActivity.this);
                main.setBarcodeCaptureFragment(MainActivity.this);


            }
        });
        applications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.chkPageUIVisibility = true;
                btnRoom.setBackgroundResource(R.drawable.unselected_room);
                txtRoom.setTextColor(Color.parseColor("#B2B9BF"));

                btnApps.setBackgroundResource(R.drawable.selected_app);
                txtApps.setTextColor(Color.parseColor("#69FFC3"));
                main.setApplicationViewFragment(MainActivity.this);

            }
        });

        main.setApplicationViewFragment(this);
        // startInside(strUrl);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {

        super.onPause();
        main.setApplicationViewFragment(main.currentContext);
        btnRoom.setBackgroundResource(R.drawable.unselected_room);
        txtRoom.setTextColor(Color.parseColor("#B2B9BF"));

        btnApps.setBackgroundResource(R.drawable.selected_app);
        txtApps.setTextColor(Color.parseColor("#69FFC3"));
    }

    @Override
    protected void onResume() {
        super.onResume();

//        String ts = Context.TELEPHONY_SERVICE;
//        TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(ts);
//        String imsi = mTelephonyMgr.getSubscriberId();
//        String imei = mTelephonyMgr.getDeviceId();
        String strAndroidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.v("IMEINUMBER","IMEINUMBER " + strAndroidID  );
        if(!main.chkForFirstLaunch) {
            main.chkForFirstLaunch=true;
            if (!main.getMoodleCookiesPref(main.currentContext).isEmpty()) {
                main.appModuleFragment = new App_Module_Fragment();

                FragmentManager fm = getFragmentManager();
                Fragment prev = getFragmentManager().findFragmentByTag("fragment_dialog");
                if (prev == null) {


                    main.appModuleFragment.show(fm, "fragment_dialog");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            main.currentFragment = new CApplicationFragment();

                            main.appModuleFragment.dismiss();


                        }
                    }, 300);


                }


            }

            if (!main.getTeamCookiesPref(main.currentContext).isEmpty()) {
                main.office365Fragment = new Office_365_Fragment();

                FragmentManager fm1 = getFragmentManager();
                Fragment prev1 = getFragmentManager().findFragmentByTag("fragment_dialog");
                if (prev1 == null) {


                    main.office365Fragment.show(fm1, "fragment_dialog");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            main.currentFragment = new CApplicationFragment();

                            main.office365Fragment.dismiss();


                        }
                    }, 500);


                }


            }


        }




    }

    public void hideBottomBar(Context context) {
//        if (!(model.equals("KT-107") || model.equals("KT-1010"))) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_IMMERSIVE;

                ((Activity) context).getWindow().getDecorView().setSystemUiVisibility(flags);
                hideSystemUI(context);
                final View decorView = ((Activity) context).getWindow().getDecorView();
                decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
//                    hideSystemUI(context);
                    }
                });
            }
        });
//        }
    }

    /**
     * Hide bottom bar
     *
     * @param context Context of the application of Activity
     * @return
     */
    private void hideSystemUI(Context context) {
        ((Activity) context).getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);


        WindowInsetsControllerCompat windowInsetsController =
                ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController == null) {
            return;
        }
        // Configure the behavior of the hidden system bars
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());

    }

    private void killApplication() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            List<ActivityManager.AppTask> tasks = am.getAppTasks();
            if (tasks != null && tasks.size() > 0) {
                tasks.get(0).setExcludeFromRecents(true);
            }
        }
        finishAffinity();
        System.exit(0);
        finishAndRemoveTask();
    }

    @Override
    public void onBackPressed() {
        if (main.currentFragment instanceof CApplicationFragment) {
        killApplication();
    }else if (main.currentFragment instanceof App_Module_Fragment) {
            main.setApplicationViewFragment(main.currentContext);
        }
        else if (main.currentFragment instanceof ShareInfoFragment) {
        main.setApplicationViewFragment(main.currentContext);
        btnRoom.setBackgroundResource(R.drawable.unselected_room);
        txtRoom.setTextColor(Color.parseColor("#B2B9BF"));

        btnApps.setBackgroundResource(R.drawable.selected_app);
        txtApps.setTextColor(Color.parseColor("#69FFC3"));
    }else if (main.currentFragment instanceof BarcodeCaptureFragment) {
        main.setApplicationViewFragment(main.currentContext);
        btnRoom.setBackgroundResource(R.drawable.unselected_room);
        txtRoom.setTextColor(Color.parseColor("#B2B9BF"));

        btnApps.setBackgroundResource(R.drawable.selected_app);
        txtApps.setTextColor(Color.parseColor("#69FFC3"));
    }
       else {
        super.onBackPressed();
    }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        main.setOrientation(main.currentContext);
    }
    //    void startInside(String urlString) {
//        Intent intent = new Intent(getApplicationContext(), WebActivity2.class);
//        intent.putExtra("url", urlString);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//    }
}