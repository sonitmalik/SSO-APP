package com.kramer.smauthenticator.javaClass;

import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.ActivityCompat;


import com.kramer.smauthenticator.Activity.BarcodeCaptureFragment;
import com.kramer.smauthenticator.Activity.App_Module_Fragment;
import com.kramer.smauthenticator.Activity.CApplicationFragment;
import com.kramer.smauthenticator.Activity.Office_365_Fragment;
import com.kramer.smauthenticator.Activity.ShareInfoFragment;
import com.kramer.smauthenticator.R;
//import com.kramer.smauthenticator.utility.KeyStoreHelper;
import com.kramer.smauthenticator.utility.UrlUtilityClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

public class MainClass extends Observable {

    //    CWebView cWebView;
    public boolean chkForFirstLaunch= false;
    public Context currentContext = null;
    public String strAuthToken;//=new JSONObject();
    public String strRefreshToken;

    public StringBuffer stringBufferCookiesData= new StringBuffer();
    public StringBuffer stringBufferCookiesDataTemp= new StringBuffer();
    public static MainClass mainObj;
    public String urlTitle;
    public String strDeviceuuid, strDeviceName;
    public App_Module_Fragment appModuleFragment;

    public Office_365_Fragment office365Fragment;

    public boolean chkForFirstLoad= false;
    public Fragment currentFragment = null;
    public Activity chkCurrentActivity = null;
    public boolean navigateAppPage;

    //public boolean chkForOfficeLoginStatus= false;
    public boolean chkForAutoLoginForMoodles= false;

    //public boolean chkForMoodlesLoginStatus= false;
    public boolean chkShowIndicatorTeams = false;
    public boolean chkShowIndicatorTeamsRed = false;
    public boolean chkShowIndicatorMoodles = false;
    public boolean chkShowIndicatorMoodlesRed = false;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public String strUniqueDeviceID="";
    public boolean webViewVisibility;
    public boolean callTeamUrl;
    public boolean callMoodleUrl;
    public boolean chkPageUIVisibility;

    public boolean meetingJoin = false;
    public JSONArray combineCookiesArray = new JSONArray();
    public String callUrl;

    public String fragTag = "";
    public String strJsonDataVal = "";
//    public String strJsonDataVal1 = "";

    public boolean chkForMoodles = false;
    public int cntForMoodles = 0;
    public String strCompID;


    public static MainClass getMainObj() {
        if (mainObj == null) {

            mainObj = new MainClass();

        }


        return mainObj;
    }

//    public CWebView getcWebView() {
//        return cWebView;
//    }
//
//    public void setcWebView(CWebView cWebView) {
//        this.cWebView = cWebView;
//    }

    public void notifyObserver(String IncMessage) {
        try {


            if (mainObj.countObservers() <= 0) {
                mainObj.addObserver((Observer) currentContext);
            }
            setChanged();
            notifyObservers(IncMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setOrientation(Context context) {
        ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

        public void setUrlTitlePref(Context context, String pref) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        removeUrlTitlePref();
        editor.putString("UrlTitle", pref);
        editor.commit();
    }
    public void removeUrlTitlePref() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(currentContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("UrlTitle");
        editor.commit();
    }
    public String getUrlTitlePref(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("UrlTitle", "");


    }
    public void setMoodleTitlePref(Context context, String pref) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        removeMoodleTitlePref();
        editor.putString("MoodleOrgTitle", pref);
        editor.commit();
    }

    public void removeMoodleTitlePref() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(currentContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("MoodleOrgTitle");
        editor.commit();
    }

    public String getMoodleTitlePref(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("MoodleOrgTitle", "");


    }



    public  void setUniqueDeviceIDPref(Context context, String pref) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        removeUniqueDeviceIDPref();
        editor.putString("UniqueDeviceID", pref);
        editor.commit();
    }

    public  void removeUniqueDeviceIDPref() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(currentContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("UniqueDeviceID");
        editor.commit();
    }

    public  String getStrUniqueDeviceIDPref(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("UniqueDeviceID", "");


    }

    public void setShareInfoFragment(Context currentContext) {
        fragTag = "ShareInfo";
        ShareInfoFragment shareInfoFragment = new ShareInfoFragment();
        Activity activity = (Activity) currentContext;
        FragmentManager fragmentManager = activity.getFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.container, shareInfoFragment, fragTag);
        fragmentTransaction.addToBackStack(fragTag);
        fragmentTransaction.commit();
    }

    public void setMoodleFragment(Context currentContext) {
        fragTag = "Moodle";
        App_Module_Fragment moodleFragment = new App_Module_Fragment();
        Activity activity = (Activity) currentContext;
        FragmentManager fragmentManager = activity.getFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.container, moodleFragment, fragTag);
        fragmentTransaction.addToBackStack(fragTag);
        fragmentTransaction.commit();

    }


    //    public void callCookiesPostData() {
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                .permitAll().build();
//
//        ((Activity)currentContext).runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String response = "";
//
//                        HttpURLConnection urlConnection = null;
//
//                        BufferedReader reader = null;
//                        try {
//                            tokenEndpoint = "https://saas-devtmp.kramerupl.com/session-mgr/api/v1/device/31eefb52-be11-4752-961f-dd554611acc3/pc";
//
////                            https://saas-devtmp.kramerupl.com/
////                            session-mgr/api/v1/device/
////                                    {deviceId}/pc
////
//
//                            String parameters = strJsonDataVal;//"client_id=backend_api&client_secret=D3IHm06VmRWdpKZSgjiprKMfaHuS0TKr&username=cec9a4fc51f2479f9aa9014be559216e&grant_type=password&password=" + encodedPassword;
//
//                            URL url = new URL(tokenEndpoint);
//
//                            urlConnection = (HttpURLConnection) url.openConnection();
//                            urlConnection.setRequestMethod("POST");
//                            JSONObject jsonObject = new JSONObject();
//
//                            urlConnection.setRequestProperty("Content-Type", "application/json");
//                            urlConnection.setRequestProperty("Authorization", "Bearer " + strAuthToken);
//                            urlConnection.setDoOutput(true);
//
//                            OutputStream outputStream = urlConnection.getOutputStream();
//
//                            outputStream.write(parameters.getBytes());
//                            outputStream.flush();
//                            outputStream.close();
//
//                            int responseCode = urlConnection.getResponseCode();
//
//                            if (responseCode == HttpURLConnection.HTTP_OK) {
//
//                                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//
//                                StringBuilder stringBuilder = new StringBuilder();
//
//                                String line;
//                                while ((line = reader.readLine()) != null) {
//
//                                    stringBuilder.append(line).append("\n");
//
//                                }
//                                response = stringBuilder.toString();
//                                notifyObserver("SENDCOOKIESSUCCESSFULL");
//                                Log.v("response", "response" + response.toString());
//
//                            } else {
//
//                                Log.e("HTTP Error", "Response Code: " + responseCode);
//
//                            }
//
//                        } catch (IOException e) {
//
//                            e.printStackTrace();
//
//                        } finally {
//
//                            if (urlConnection != null) {
//
//                                urlConnection.disconnect();
//
//                            }
//
//                            if (reader != null) {
//
//                                try {
//
//                                    reader.close();
//
//                                } catch (IOException e) {
//
//                                    e.printStackTrace();
//
//                                }
//
//                            }
//
//                        }
//                    }
//                }).start();
//
//            }
//        });
//
//    }
//    public void callCookiesPostData()

        public void setOffice365Fragment(Context currentContext) {
        fragTag = "Office365";
        Office_365_Fragment office365Fragment = new Office_365_Fragment();
        Activity activity = (Activity) currentContext;
        FragmentManager fragmentManager = activity.getFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.container, office365Fragment, fragTag);
        fragmentTransaction.addToBackStack(fragTag);
        fragmentTransaction.commit();
    }
    CountDownTimer countDownTimer;
    CountDownTimer countDownTimer1;

    private void startTimerForCleanRoom() {

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        ((Activity) currentContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                countDownTimer = new CountDownTimer(7000, 7000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        notifyObserver("ROOMCLEAN");

                    }

                    @Override
                    public void onFinish() {
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                            countDownTimer = null;
                            notifyObserver("WARNINGINVISIBLE");

                        }

                    }
                }.start();
            }
        });

//            }
//        }).start();


    }

    private void startTimerFailedCredentials() {

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        ((Activity) currentContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                countDownTimer = new CountDownTimer(7000, 7000) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                        notifyObserver("SENDCOOKIESFAILED");
                    }

                    @Override
                    public void onFinish() {
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                            countDownTimer = null;
                            notifyObserver("WARNINGINVISIBLE");

                        }

                    }
                }.start();
            }
        });

//            }
//        }).start();


    }

    public void deleteServerCookiesData() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();

        ((Activity) currentContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String response = "";

                        HttpURLConnection urlConnection = null;

                        BufferedReader reader = null;
//                        strDeviceuuid="c9ce868c-074d-44e9-90a0-5bb6ea46caee";
                        try {

                            // tokenEndpoint = "https://saas-devtmp.kramerupl.com/session-mgr/api/v1/device/c9ce868c-074d-44e9-90a0-5bb6ea46caee/cpc";
//                            tokenEndpoint = "https://saas-devtmp.kramerupl.com/session-mgr/api/v1/device/" + strDeviceuuid + "/cpc"; // For devlopment

                            String  tokenEndpoint = UrlUtilityClass.strUrl+strDeviceuuid+"/client/"+getUniqueDeviceID()+"/"+"cpc";// For production

//                            https://saas-devtmp.kramerupl.com/
//                            session-mgr/api/v1/device/
//                                    {deviceId}/pc
//
//                            String parameters1 = strJsonDataVal1;
//
//                            String parameters = strJsonDataVal;//"client_id=backend_api&client_secret=D3IHm06VmRWdpKZSgjiprKMfaHuS0TKr&username=cec9a4fc51f2479f9aa9014be559216e&grant_type=password&password=" + encodedPassword;
                            String parameters;
//                            if (callTeamUrl) {
                                parameters = getTeamCookiesPref(currentContext);
//                            } else {
//                                parameters = getMoodleCookiesPref(currentContext);
//                            }
                            URL url = new URL(tokenEndpoint);

                            urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.setRequestMethod("DELETE");
                            JSONObject jsonObject = new JSONObject();

                            urlConnection.setRequestProperty("Content-Type", "application/json");
                            urlConnection.setRequestProperty("Authorization", "Bearer " + strAuthToken);
                            urlConnection.setDoOutput(true);

//                            OutputStream outputStream = urlConnection.getOutputStream();
//
//                            outputStream.write(parameters.getBytes());
//                            outputStream.flush();
//                            outputStream.close();

                            int responseCode = urlConnection.getResponseCode();

                            if (responseCode == HttpURLConnection.HTTP_OK) {

                                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                                StringBuilder stringBuilder = new StringBuilder();

                                String line;
                                while ((line = reader.readLine()) != null) {

                                    stringBuilder.append(line).append("\n");

                                }
                                response = stringBuilder.toString();
                                startTimerForCleanRoom();
                                removeShareDatePref();
                                Log.v("response", "response" + response.toString());

                            } else {

                                Log.e("HTTP Error", "Response Code: " + responseCode);

                            }

                        } catch (IOException e) {

                            e.printStackTrace();

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

    public void setTeamCookiesPref(Context context, String pref) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        removeTeamCookiesPref();
        editor.putString("TeamTitle", pref);
        editor.commit();
    }
//public boolean chkNetworkStatus(Context context){
//    ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//    boolean connected = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
//            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
//    return connected;
//}
    public void removeTeamCookiesPref() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(currentContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("TeamTitle");
        editor.commit();
    }

    public String getTeamCookiesPref(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("TeamTitle", "");


    }








//    public void setCookiesPrefForMoodles(Context context, String pref) {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = preferences.edit();
//        removeClearCookiesPrefForMoodles();
//        editor.putString("MOODLESLOGGEDIN", pref);
//        editor.commit();
//    }
//
//    public void removeClearCookiesPrefForMoodles() {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(currentContext);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.remove("MOODLESLOGGEDIN");
//        editor.commit();
//    }
//
//    public String getClearCookiesPrefForMoodles(Context context) {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        return preferences.getString("MOODLESLOGGEDIN", "");
//    }

//    public void setCookiesPrefForOffice(Context context, String pref) {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = preferences.edit();
//        removeClearCookiesPrefForOffice();
//        editor.putString("OFFICELOGGEDINNGGGG", pref);
//        editor.commit();
//    }
//
//    public void removeClearCookiesPrefForOffice() {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(currentContext);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.remove("OFFICELOGGEDINNGGGG");
//        editor.commit();
//    }
//
//    public String getClearCookiesPrefForOffice(Context context) {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        return preferences.getString("OFFICELOGGEDINNGGGG", "");
//    }


    public void setCookiesPref(Context context, String pref) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        removeClearCookiesPref();
        editor.putString("COOKIESDELETE", pref);
        editor.commit();
    }

    public void removeClearCookiesPref() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(currentContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("COOKIESDELETE");
        editor.commit();
    }

    public String getClearCookiesPref(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("COOKIESDELETE", "");
    }

    public void setMoodleCookiesPref(Context context, String pref) {
        Log.v("DataValues","DataValues start strJsonDataVal updatestart setMoodleCookiesPref called >>>>>>." + pref);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        removeMoodleCookiesPref();
        editor.putString("MoodleTitle", pref);
        editor.commit();
    }

    public void removeMoodleCookiesPref() {
        Log.v("DataValues","DataValues start strJsonDataVal updatestart removeMoodleCookiesPref called >>>>>>.");
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(currentContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("MoodleTitle");
        editor.commit();
    }

    public String getMoodleCookiesPref(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("MoodleTitle", "");


    }






    //======================


    public void setCombineCookiesPref(Context context, String pref) {
        Log.v("DataValues","DataValues start strJsonDataVal updatestart setMoodleCookiesPref called >>>>>>." + pref);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
//        removeCombineCookiesPref();
        editor.putString("MoodleCombineTitle", pref);
        editor.commit();
    }

    public void removeCombineCookiesPref() {
        Log.v("DataValues","DataValues start strJsonDataVal updatestart removeMoodleCookiesPref called >>>>>>.");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(currentContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("MoodleCombineTitle");
        editor.commit();
    }

    public String getCombineCookiesPref(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("MoodleCombineTitle", "");


    }


    //=================





    public void setShareDatePref(Context context, String pref) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        removeShareDatePref();
        editor.putString("ShareDate", pref);
        editor.commit();
    }

    public void removeShareDatePref() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(currentContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("ShareDate");
        editor.commit();
    }

    public String getShareDatePref(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("ShareDate", "");


    }

    public  String getUniqueDeviceID() {

        String strAndroidID = Settings.Secure.getString(currentContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.v("strAndroidID","strAndroidID " + strAndroidID  );
//      String strUniqueDeviceID=  getStrUniqueDeviceIDPref(currentContext);
//        UUID uuid = UUID.randomUUID();
//        if (strUniqueDeviceID != null && !strUniqueDeviceID.isEmpty() && !strUniqueDeviceID.equals("null")){
//            return strUniqueDeviceID;
//        }else{
//            setUniqueDeviceIDPref(currentContext,uuid.toString());
//            strUniqueDeviceID= uuid.toString();
//        }
        return strAndroidID;
    }


    public void callCookiesPostData() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();

        ((Activity) currentContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String response = "";

                        HttpURLConnection urlConnection = null;

                        BufferedReader reader = null;

                        if(!stringBufferCookiesData.toString().equals("")) {
//                            setCombineCookiesPref(currentContext, String.valueOf(stringBufferCookiesData));
                        }
                        try {

                            //  tokenEndpoint = "https://saas-devtmp.kramerupl.com/session-mgr/api/v1/device/c9ce868c-074d-44e9-90a0-5bb6ea46caee/pc";

                       String  tokenEndpoint = UrlUtilityClass.strUrl+strDeviceuuid+"/client/"+strUniqueDeviceID+"/"+"pc";// For production
//https://saas-devtmp.kramerupl.com/session-mgr/api/v1/device/c9ce868c-074d-44e9-90a0-5bb6ea46caee/client/d3ebb5be-c408-4b76-9622-585bb6ade6da/pc

//                            https://saas-devtmp.kramerupl.com/session-mgr/api/v1/device/c9ce868c-074d-44e9-90a0-5bb6ea46caee/client/d3ebb5be-c408-4b76-9622-585bb6ade6da/pc

                            String parameters = null;
                            if (!getTeamCookiesPref(currentContext).isEmpty()||!getMoodleCookiesPref(currentContext).isEmpty()) {


                                StringBuffer sb=new StringBuffer();
                                sb.append(getTeamCookiesPref(currentContext));
                                sb.append(getMoodleCookiesPref(currentContext));


                                parameters= sb.toString();//getCombineCookiesPref(mainObj.currentContext);

//                                parameters = getTeamCookiesPref(currentContext);
//                                if (parameters.equals("") || parameters.equals(null)) {
//                                    parameters = getMoodleCookiesPref(currentContext);
//                                }
                            }
//                            parameters= getCombineCookiesPref(mainObj.currentContext);

                            URL url = new URL(tokenEndpoint);

                            urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.setRequestMethod("POST");
                            urlConnection.setRequestProperty("Content-Type", "application/json");
                            urlConnection.setRequestProperty("Authorization", "Bearer " + strAuthToken);
                            urlConnection.setDoOutput(true);

                            OutputStream outputStream = urlConnection.getOutputStream();

                            outputStream.write(parameters.getBytes());
                            outputStream.flush();
                            outputStream.close();

                            int responseCode = urlConnection.getResponseCode();

                            if (responseCode == HttpURLConnection.HTTP_OK) {

                                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                                StringBuilder stringBuilder = new StringBuilder();

                                String line;
                                while ((line = reader.readLine()) != null) {

                                    stringBuilder.append(line).append("\n");

                                }
                                response = stringBuilder.toString();
                                notifyObserver("SENDCOOKIESSUCCESSFULL");
                                notifyObserver("SharedCookiesDate");

                                Log.v("response", "response" + response.toString());

                            } else {
                                notifyObserver("SENDCOOKIESFAILED");
                                Log.e("HTTP Error", "Response Code: " + responseCode);
                                startTimerFailedCredentials();
                            }

                        } catch (IOException e) {
                            notifyObserver("SENDCOOKIESFAILED");
                            e.printStackTrace();

                        } finally {
                            notifyObserver("HIDEPROGRESSBAR");
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


    public void setBarcodeCaptureFragment(Context currentContext) {
        fragTag = "Barcode";
        BarcodeCaptureFragment barcodeCaptureFragment = new BarcodeCaptureFragment();
        Activity activity = (Activity) currentContext;
        FragmentManager fragmentManager = activity.getFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.container, barcodeCaptureFragment, fragTag);
        fragmentTransaction.addToBackStack(fragTag);
        fragmentTransaction.commit();
    }

    public void setApplicationViewFragment(Context currentContext) {
        fragTag = "cApplications";
        CApplicationFragment cApplicationFragment = new CApplicationFragment();
        Activity activity = (Activity) currentContext;
        FragmentManager fragmentManager = activity.getFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.container, cApplicationFragment, fragTag);
        fragmentTransaction.addToBackStack(fragTag);
        fragmentTransaction.commit();
    }

    public static void verifyStoragePermissions(AppCompatActivity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}

