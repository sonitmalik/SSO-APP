package com.kramer.smauthenticator.Activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.app.Fragment;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.Text;
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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShareInfoFragment extends Fragment implements Observer {


    Button btn_share_info, btn_leave_room;
    RelativeLayout rlloader;
    RelativeLayout llrootlayout;
    MainClass mainClass;
    Drawable img;
    TextView txtDateTime, txtShareStatus, txtDateTimeHeader, txtRoomName;
//    ImageView backBtn;

    LinearLayout backBtn;

    Handler hrefresh = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message message) {
            switch (message.what) {
                case 1000:
                    llrootlayout.setAlpha(1.0f);
                    rlloader.setVisibility(View.INVISIBLE);
                    txtShareStatus.setVisibility(View.VISIBLE);
                    txtShareStatus.setText(R.string.app_credentials_successfully_shared);
                    img = mainClass.currentContext.getResources().getDrawable(R.drawable.green_right);
                    txtShareStatus.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);

                    enableCleanRoom();

                    break;
                case 1001:
                    txtDateTime.setVisibility(View.INVISIBLE);
                    txtDateTimeHeader.setVisibility(View.INVISIBLE);
                    llrootlayout.setAlpha(1.0f);
                    rlloader.setVisibility(View.INVISIBLE);
                    txtShareStatus.setVisibility(View.VISIBLE);
                    txtShareStatus.setText(R.string.share_credentials_failed);
                    img = mainClass.currentContext.getResources().getDrawable(R.drawable.warning_share_status);
                    txtShareStatus.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    break;
                case 1002:
                    txtDateTime.setVisibility(View.INVISIBLE);
                    txtDateTimeHeader.setVisibility(View.INVISIBLE);
                    txtShareStatus.setVisibility(View.VISIBLE);
                    txtShareStatus.setText(R.string.room_clean);
                    img = mainClass.currentContext.getResources().getDrawable(R.drawable.room_clean);
                    txtShareStatus.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    disableCleanRoom();
                    break;
                case 1003:
                    SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, h:mm a");
                    String currentDateandTime = sdf.format(new Date());
                    mainClass.setShareDatePref(mainClass.currentContext, currentDateandTime);
                    txtDateTime.setVisibility(View.VISIBLE);
                    txtDateTimeHeader.setVisibility(View.VISIBLE);
                    txtDateTime.setText(mainClass.getShareDatePref(mainClass.currentContext));
                    break;
                case 1004:
                    txtShareStatus.setVisibility(View.INVISIBLE);
                    break;
                case 1005:
                    rlloader.setVisibility(View.INVISIBLE);
                    break;
                default:
                    break;
            }
        }
    };

    public void enableCleanRoom() {
        btn_leave_room.setVisibility(View.VISIBLE);
//        btn_leave_room.setClickable(true);
//        btn_leave_room.setEnabled(true);
//        btn_leave_room.setAlpha(1.0f);
//        btn_leave_room.setTextColor(Color.parseColor("#69FFC3"));
    }


    public void disableCleanRoom() {
//        btn_leave_room.setClickable(false);
//        btn_leave_room.setEnabled(false);
//        btn_leave_room.setAlpha(0.7f);
//        btn_leave_room.setTextColor(Color.parseColor("#B2B9BF"));
    }

    public void disableShareButton() {
        btn_share_info.setClickable(false);
        btn_share_info.setEnabled(false);
        //btn_share_info.setAlpha(0.7f);

        btn_share_info.setBackgroundResource(R.drawable.disable_share_bg);
       // btn_share_info.setTextColor(Color.parseColor("#B2B9BF"));
    }

    public void enableShareButton() {
        btn_share_info.setVisibility(View.VISIBLE);
        btn_share_info.setClickable(true);
        btn_share_info.setEnabled(true);
        btn_share_info.setAlpha(1.0f);
      //  btn_share_info.setTextColor(Color.parseColor("#69FFC3"));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainClass = MainClass.getMainObj();
        mainClass.currentFragment = ShareInfoFragment.this;

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_info, container, false);
        mainClass = MainClass.getMainObj();
        //mainClass.currentFragment = ShareInfoFragment.this;

        btn_share_info = (Button) view.findViewById(R.id.btn_share_info);
        btn_leave_room = (Button) view.findViewById(R.id.btn_leave_room);
        rlloader = (RelativeLayout) view.findViewById(R.id.rlloaderview);
        llrootlayout = (RelativeLayout) view.findViewById(R.id.ll_Apps);
        txtShareStatus = (TextView) view.findViewById(R.id.shareStatus);
        txtRoomName = (TextView) view.findViewById(R.id.txtRoomName);
        backBtn = (LinearLayout) view.findViewById(R.id.ll_Back_Header);
        mainClass.navigateAppPage = true;
//        txtShareStatus.setVisibility(View.INVISIBLE);
        txtDateTime = (TextView) view.findViewById(R.id.txtDateTime);

//        if(mainClass.getShareDatePref(mainClass.currentContext).equals())

        txtDateTimeHeader = view.findViewById(R.id.txtDateTimeHeader);
        txtRoomName.setText(mainClass.strDeviceName);
        String strSharedDate = mainClass.getShareDatePref(mainClass.currentContext);
        if (strSharedDate != null && !strSharedDate.isEmpty()) {
            txtDateTime.setVisibility(View.VISIBLE);
            txtDateTimeHeader.setVisibility(View.VISIBLE);
            txtDateTime.setText(strSharedDate);
        } else {
            txtDateTime.setVisibility(View.INVISIBLE);
            txtDateTimeHeader.setVisibility(View.INVISIBLE);
        }

        mainClass.addObserver(ShareInfoFragment.this);
        if (!mainClass.getMoodleCookiesPref(mainClass.currentContext).isEmpty() || !mainClass.getTeamCookiesPref(mainClass.currentContext).isEmpty()) {
            enableShareButton();
        } else {
            disableShareButton();
        }

        disableCleanRoom();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainClass.setBarcodeCaptureFragment(mainClass.currentContext);
            }
        });
        btn_share_info.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn_share_info.setBackgroundResource(R.drawable.green_bg);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn_share_info.setBackgroundResource(R.drawable.green_pressed_bg);
                }
                return false;
            }

        });
        btn_share_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!mainClass.getTeamCookiesPref(mainClass.currentContext).isEmpty() || !mainClass.getMoodleCookiesPref(mainClass.currentContext).isEmpty()) {

                    llrootlayout.setAlpha(0.1f);

                    rlloader.setVisibility(View.VISIBLE);

                    mainClass.callCookiesPostData();


                } else {
                    img = mainClass.currentContext.getResources().getDrawable(R.drawable.warning_share_status);
                    txtShareStatus.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    txtShareStatus.setVisibility(View.VISIBLE);
                    txtShareStatus.setText(R.string.no_user_loggedin);
                }


//                mainClass.callCookiesPostData1();

                // ShareCookiesInfo.callCookiesPostData();

            }
        });
        btn_leave_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainClass.deleteServerCookiesData();
            }
        });
        return view;
    }

    private void getAuthTokenClientHttpPost() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();

        ((Activity) mainClass.currentContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection urlConnection = null;

                        BufferedReader reader = null;


                        try {

                            String strTokenEndPoint = UrlUtilityClass.tokenEndpoint;
                            String password = UrlUtilityClass.password;
                            String encodedPassword = URLEncoder.encode(password, "UTF-8");
                            String parameters = UrlUtilityClass.strParametersForAuthCLient + encodedPassword;// FOr production
                            URL url = new URL(strTokenEndPoint);

                            urlConnection = (HttpsURLConnection) url.openConnection();
                            urlConnection.setRequestMethod("POST");

                            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                            urlConnection.setDoOutput(true);

                            OutputStream outputStream = urlConnection.getOutputStream();

                            outputStream.write(parameters.getBytes());
                            outputStream.flush();
                            outputStream.close();

                            int responseCode = urlConnection.getResponseCode();

                            if (responseCode == HttpURLConnection.HTTP_OK) {

                                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                                String strData = reader.readLine();

                                JSONObject authclientJsonObject = new JSONObject(strData);

                                mainClass.strAuthToken = authclientJsonObject.getString("access_token");

                                Log.v("assdf", "");
                            } else {

                                Log.e("HTTP Error", "Response Code: " + responseCode);

                            }

                        } catch (IOException e) {

                            e.printStackTrace();

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        } finally {

                            if (urlConnection != null) {

                                urlConnection.disconnect();

                            }

                            if (reader != null) {

                                try {

                                    reader.close();

                                } catch (IOException e) {

                                    e.printStackTrace();

                                }

                            }

                        }
                    }
                }).start();

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mainClass.addObserver(this);
        getAuthTokenClientHttpPost();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mainClass.setOrientation(mainClass.currentContext);
    }

    @Override
    public void onPause() {
        super.onPause();
        mainClass.deleteObserver(this);
    }


    /**
     * This method is called whenever the observed object is changed. An
     * application calls an {@code Observable} object's
     * {@code notifyObservers} method to have all the object's
     * observers notified of the change.
     *
     * @param o    the observable object.
     * @param data an argument passed to the {@code notifyObservers}
     *             method.
     */
    @Override
    public void update(Observable o, Object data) {
        if (data.toString().equals("SENDCOOKIESSUCCESSFULL")) {
            hrefresh.sendEmptyMessage(1000);
        } else if (data.toString().equals("SENDCOOKIESFAILED")) {
            hrefresh.sendEmptyMessage(1001);
        } else if (data.toString().equals("ROOMCLEAN")) {
            hrefresh.sendEmptyMessage(1002);
        } else if (data.toString().equals("SharedCookiesDate")) {
            hrefresh.sendEmptyMessage(1003);
        } else if (data.toString().equals("WARNINGINVISIBLE")) {
            hrefresh.sendEmptyMessage(1004);
        } else if (data.toString().equals("HIDEPROGRESSBAR")) {
            hrefresh.sendEmptyMessage(1005);
        }
    }
}
