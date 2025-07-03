package com.kramer.smauthenticator.Activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.Fragment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.kramer.smauthenticator.MainActivity;
import com.kramer.smauthenticator.R;
import com.kramer.smauthenticator.javaClass.MainClass;
import com.kramer.smauthenticator.utility.UrlUtilityClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Observable;
import java.util.Observer;


/**
 * A simple {@link Fragment} subclass.
 */
public class CApplicationFragment extends Fragment implements Observer {


    LinearLayout moodle, office365;
    ImageView MoodleIndicator, OfficeIndicator;
    MainClass main;
    RelativeLayout rlProgressbarLayout;
    Button btnClrSession;
    TextView txtInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        main = MainClass.getMainObj();
        main.currentFragment = CApplicationFragment.this;
        main.currentContext =getActivity();
        main.addObserver(this);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_applications, container, false);
        main = MainClass.getMainObj();
        //main.currentFragment = CApplicationFragment.this;
        main.currentContext =getActivity();
        main.addObserver(this);
        moodle = view.findViewById(R.id.rLMoodle);
        rlProgressbarLayout = (RelativeLayout)view.findViewById(R.id.rlprogressbar);
        if (!main.getTeamCookiesPref(main.currentContext).isEmpty()||!main.getMoodleCookiesPref(main.currentContext).isEmpty()) {
            if (!main.chkForFirstLoad) {
                main.chkForFirstLoad= true;
                rlProgressbarLayout.setVisibility(View.VISIBLE);
            }
        }
        main.callTeamUrl = false;
        main.callMoodleUrl=false;
        office365 = view.findViewById(R.id.rLOffice);
        MoodleIndicator = view.findViewById(R.id.btnIndicatorM);
        OfficeIndicator = view.findViewById(R.id.btnIndicatorOff);
        main.chkPageUIVisibility = false;

        btnClrSession = view.findViewById(R.id.btnClrSession);
        txtInfo = view.findViewById(R.id.txtInfo);

        String stringTeamCookies= main.getTeamCookiesPref(main.currentContext);
        String strMoodlesCookies= main.getMoodleCookiesPref(main.currentContext);
        if (!main.getTeamCookiesPref(main.currentContext).isEmpty()||!main.getMoodleCookiesPref(main.currentContext).isEmpty()) {
            btnClrSession.setClickable(true);
            btnClrSession.setEnabled(true);
            btnClrSession.setAlpha(1.0f);
            /*As per requirement I have changed the Visibility of clear cache button */
            btnClrSession.setVisibility(View.VISIBLE);
            txtInfo.setVisibility(View.VISIBLE);
           // btnClrSession.setTextColor(R.drawable.button_selector);
            btnClrSession.setBackgroundResource(android.R.color.transparent);
        }else{
//            btnClrSession.setClickable(false);
//            btnClrSession.setEnabled(false);
//            btnClrSession.setAlpha(0.5f);

           // btnClrSession.setTextColor(Color.parseColor("#B2B9BF"));
            /*As per requirement I have changed the Visibility of clear cache button */
            btnClrSession.setVisibility(View.GONE);
            txtInfo.setVisibility(View.GONE);
            OfficeIndicator.setBackgroundResource(R.drawable.indicator_disable);
            MoodleIndicator.setBackgroundResource(R.drawable.indicator_disable);
        }

        btnClrSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(main.currentContext)
                        .setTitle("")
                        .setMessage("Would you like to clear your credentials?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                               // main.chkForOfficeLoginStatus= false;
                               // main.chkForMoodlesLoginStatus= false;
                                main.notifyObserver("ClearCookies");
                                main.removeMoodleCookiesPref();
                                main.removeTeamCookiesPref();
                                main.removeCombineCookiesPref();
                                main.removeUrlTitlePref();
                                main.removeShareDatePref();
                                main.chkShowIndicatorTeams=false;
                                main.chkShowIndicatorMoodles=false;
                                OfficeIndicator.setBackgroundResource(R.drawable.indicator_disable);
                                MoodleIndicator.setBackgroundResource(R.drawable.indicator_disable);
//                                btnClrSession.setClickable(false);
//                                btnClrSession.setAlpha(0.5f);
                                /*As per requirement I have changed the Visibility of clear cache button */
                                btnClrSession.setVisibility(View.GONE);
                                txtInfo.setVisibility(View.GONE);

                                //btnClrSession.setTextColor(Color.parseColor("#B2B9BF"));
                            }})
                        .setNegativeButton(android.R.string.no, null).show();


            }
        });
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//        if(!main.getTeamCookiesPref(main.currentContext).isEmpty()){
//            if(!main.chkNetworkStatus(main.currentContext)){
//                OfficeIndicator.setBackgroundResource(R.drawable.red_indicator);
//                main.chkShowIndicatorTeamsRed=true;
//                main.chkShowIndicatorTeams=false;
//            }else{
//                main.chkShowIndicatorTeamsRed=false;
//                main.chkShowIndicatorTeams=true;
//            }
//
//        }
//        if(!main.getMoodleCookiesPref(main.currentContext).isEmpty()) {
//            if (!main.chkNetworkStatus(main.currentContext)) {
//                MoodleIndicator.setBackgroundResource(R.drawable.red_indicator);
//                main.chkShowIndicatorMoodlesRed=true;
//                main.chkShowIndicatorMoodles=false;
//            }else{
//                main.chkShowIndicatorMoodlesRed=false;
//                main.chkShowIndicatorMoodles=true;
//            }
//        }


//                if(main.chkForOfficeLoginStatus){
//                   // OfficeIndicator.setBackgroundResource(R.drawable.indicator_enable);
////                    rlProgressbarLayout.setVisibility(View.INVISIBLE);
//                    if (main.chkShowIndicatorTeams) {
//
//                        OfficeIndicator.setBackgroundResource(R.drawable.indicator_enable);
////                            rlProgressbarLayout.setVisibility(View.INVISIBLE);
//                    }
//                    else  if (main.chkShowIndicatorTeamsRed) {
//                        OfficeIndicator.setBackgroundResource(R.drawable.red_indicator);
////                            rlProgressbarLayout.setVisibility(View.INVISIBLE);
//                    }
//                    else {
//                        OfficeIndicator.setBackgroundResource(R.drawable.indicator_disable);
////                            rlProgressbarLayout.setVisibility(View.INVISIBLE);
//                    }
//                }
//                else{
                    if(!main.getTeamCookiesPref(main.currentContext).isEmpty()){

                        if (main.chkShowIndicatorTeams) {

                            OfficeIndicator.setBackgroundResource(R.drawable.indicator_enable);
//                            rlProgressbarLayout.setVisibility(View.INVISIBLE);
                        }
                        else  if (main.chkShowIndicatorTeamsRed) {
                            OfficeIndicator.setBackgroundResource(R.drawable.red_indicator);
//                            rlProgressbarLayout.setVisibility(View.INVISIBLE);
                        }
                        else {
                            OfficeIndicator.setBackgroundResource(R.drawable.indicator_disable);
//                            rlProgressbarLayout.setVisibility(View.INVISIBLE);
                        }

                    }
                    if(main.getTeamCookiesPref(main.currentContext).isEmpty()){
                        OfficeIndicator.setBackgroundResource(R.drawable.indicator_disable);

//                        rlProgressbarLayout.setVisibility(View.INVISIBLE);
                        Log.v("DataValues","DataValues start strJsonDataVal updatestart UI Exit>>>>>>.");

                    }
               // }


//                if(main.chkForMoodlesLoginStatus){
//                    MoodleIndicator.setBackgroundResource(R.drawable.indicator_enable);
////                    rlProgressbarLayout.setVisibility(View.INVISIBLE);
//                }
//                else{
                    if(!main.getMoodleCookiesPref(main.currentContext).isEmpty()){
                        if (main.chkShowIndicatorMoodles) {
                            MoodleIndicator.setBackgroundResource(R.drawable.indicator_enable);
//                            rlProgressbarLayout.setVisibility(View.INVISIBLE);


                        } else {
                            if (main.chkShowIndicatorMoodlesRed) {
                                MoodleIndicator.setBackgroundResource(R.drawable.red_indicator);
//                                rlProgressbarLayout.setVisibility(View.INVISIBLE);
                            } else { Log.v("DataValues","DataValues start strJsonDataVal updatestart UI indicator_disable>>>>>>.");
                                MoodleIndicator.setBackgroundResource(R.drawable.indicator_disable);
//                                rlProgressbarLayout.setVisibility(View.INVISIBLE);

                            }


                        }

                    }
                    if(main.getMoodleCookiesPref(main.currentContext).isEmpty()){
                        MoodleIndicator.setBackgroundResource(R.drawable.indicator_disable);
                    }
                //}

//            }
//        }, 3000);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rlProgressbarLayout.setVisibility(View.INVISIBLE);
            }
        },4000);
        moodle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                main.setCookiesPref(getActivity(),"moodlefordeletecookies");
                main.chkPageUIVisibility = true;
                main.callTeamUrl = false;
                main.callMoodleUrl=true;
                main.setMoodleFragment(getActivity());
            }
        });
        office365.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                main.setCookiesPref(getActivity(),"office365fordeletecookies");
                main.chkPageUIVisibility = true;
                main.callTeamUrl = true;
                main.callMoodleUrl=false;
//                main.setMoodleFragment(getActivity());
                 main.setOffice365Fragment(getActivity());
            }
        });
        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
//        if (main.appModuleFragment!=null && main.appModuleFragment.isVisible()) {
//            main.appModuleFragment.dismiss();
//        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        main.setOrientation(main.currentContext);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an {@code Observable} object's
     * {@code notifyObservers} method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the {@code notifyObservers}
     *            method.
     */
    @Override
    public void update(Observable o, Object arg) {
        if(arg.toString().equals("STATUSUPDATE")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

//                    if(main.chkForMoodlesLoginStatus){
//                        MoodleIndicator.setBackgroundResource(R.drawable.indicator_enable);
////                        rlProgressbarLayout.setVisibility(View.INVISIBLE);
//                    }
//                    else {
                        if (!main.getMoodleCookiesPref(main.currentContext).isEmpty()) {
                            if (main.chkShowIndicatorMoodles) {
                                MoodleIndicator.setBackgroundResource(R.drawable.indicator_enable);
//                                rlProgressbarLayout.setVisibility(View.INVISIBLE);


                            } else {
                                if (main.chkShowIndicatorMoodlesRed) {
                                    MoodleIndicator.setBackgroundResource(R.drawable.red_indicator);
//                                    rlProgressbarLayout.setVisibility(View.INVISIBLE);
                                } else {
                                    Log.v("DataValues", "DataValues start strJsonDataVal updatestart UI indicator_disable>>>>>>.");
                                    MoodleIndicator.setBackgroundResource(R.drawable.indicator_disable);
//                                    rlProgressbarLayout.setVisibility(View.INVISIBLE);

                                }


                            }

                        } else if (main.getMoodleCookiesPref(main.currentContext).isEmpty()) {
                            MoodleIndicator.setBackgroundResource(R.drawable.indicator_disable);
                        }

                   // }

//                    if(main.chkForOfficeLoginStatus){
//                        if (main.chkShowIndicatorTeams) {
//
//                            OfficeIndicator.setBackgroundResource(R.drawable.indicator_enable);
////                                rlProgressbarLayout.setVisibility(View.INVISIBLE);
//                        }
//                        else  if (main.chkShowIndicatorTeamsRed) {
//                            OfficeIndicator.setBackgroundResource(R.drawable.red_indicator);
////                                rlProgressbarLayout.setVisibility(View.INVISIBLE);
//                        }
//                        else {
//                            OfficeIndicator.setBackgroundResource(R.drawable.indicator_disable);
////                                rlProgressbarLayout.setVisibility(View.INVISIBLE);
//                        }
//                        //OfficeIndicator.setBackgroundResource(R.drawable.indicator_enable);
////                        rlProgressbarLayout.setVisibility(View.INVISIBLE);
//                    }

                   // else{
                        if(!main.getTeamCookiesPref(main.currentContext).isEmpty()){

                            if (main.chkShowIndicatorTeams) {

                                OfficeIndicator.setBackgroundResource(R.drawable.indicator_enable);
//                                rlProgressbarLayout.setVisibility(View.INVISIBLE);
                            }
                            else  if (main.chkShowIndicatorTeamsRed) {
                                OfficeIndicator.setBackgroundResource(R.drawable.red_indicator);
//                                rlProgressbarLayout.setVisibility(View.INVISIBLE);
                            }
                            else {
                                OfficeIndicator.setBackgroundResource(R.drawable.indicator_disable);
//                                rlProgressbarLayout.setVisibility(View.INVISIBLE);
                            }

                        }else  if(main.getTeamCookiesPref(main.currentContext).isEmpty()){
                            OfficeIndicator.setBackgroundResource(R.drawable.indicator_disable);

//                            rlProgressbarLayout.setVisibility(View.INVISIBLE);
                            Log.v("DataValues","DataValues start strJsonDataVal updatestart UI Exit>>>>>>.");

                        }
                    }





//                    rlProgressbarLayout.setVisibility(View.INVISIBLE);
               // }
            }, 100);
        }else if(arg.toString().equals("CloseBarCodeView")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(rlProgressbarLayout!=null) {
                        rlProgressbarLayout.setVisibility(View.INVISIBLE);
                    }
                }
            },2000);

        }else{
            rlProgressbarLayout.setVisibility(View.INVISIBLE);
        }
    }
}
