
package com.kramer.smauthenticator.Activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.Fragment;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewDatabase;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kramer.smauthenticator.R;
import com.kramer.smauthenticator.javaClass.MainClass;
import com.kramer.smauthenticator.utility.IsHttpOnlyValue;
import com.kramer.smauthenticator.utility.IsSecureValue;
import com.kramer.smauthenticator.utility.SameSiteValue;
import com.kramer.smauthenticator.utility.UrlUtilityClass;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import okhttp3.MediaType;


/**
 * A simple {@link Fragment} subclass.
 */
public class Office_365_Fragment extends DialogFragment implements Observer {

    LinearLayout rootLayout;
    RelativeLayout rAppTilteBar;
    ImageView btnBack;
    MainClass main;
    ProgressBar progressBar;
    TextView tvLoadingText;
    public static final MediaType JSON
            = MediaType.parse("content-type\", application/x-www-form-urlencoded; charset=utf-8");
    HashMap<String, String> jsonMapValueData = new HashMap<>();
    HashMap<String, String> jsonMapValueData1 = new HashMap<>();
    JSONObject authclientJsonObject = new JSONObject();
    //String tokenEndpoint = "https://key.kramerupl.com:8443/realms/kramer/protocol/openid-connect/token";
    WebView mWebView;
    String strPath = "";
    String strExpireValue = "";
    String strDomainValue = "";
    //RelativeLayout rlloader;
    CookieManager cookieManager;
    JSONArray cookiesArray = new JSONArray();
    JSONArray cookiesArray1 = new JSONArray();
    boolean redirect = false;
    boolean loadingFinished = true;
    TextView txtHeaderModuleName;

    boolean chkForURLLoadFinished = false;
    JSONObject cookiesJson = new JSONObject();
    JSONObject cookiesJsonMoodle = new JSONObject();
    String urlLink;

    public void ClearCookies(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
        }
    }
public void ClearWebCookiesData(){
    mWebView.clearHistory();
    mWebView.clearCache(true);
    mWebView.clearFormData();
    cookieManager.removeSessionCookie();
    cookieManager.removeAllCookie();
    ClearCookies(main.currentContext);
    mWebView.stopLoading();
}
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1001: {
                    ClearWebCookiesData();
                }
                break;
                case 1002: {

                }
                default:
                    break;
            }


        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = MainClass.getMainObj();
        main.currentFragment = Office_365_Fragment.this;
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TransparentDialog);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_office_365, container, false);
        main = MainClass.getMainObj();
        //main.currentFragment = App_Module_Fragment.this;
        main.currentContext = getActivity();
        main.addObserver(this);
        rootLayout = view.findViewById(R.id.root_Layout);
        rAppTilteBar = view.findViewById(R.id.rAppTilteBar);
        btnBack = view.findViewById(R.id.btn_back);
        //rlloader = (RelativeLayout)view.findViewById(R.id.rlloaderview);
        mWebView = (WebView) view.findViewById(R.id.web_view_office);
        progressBar = view.findViewById(R.id.progress);
        progressBar.setIndeterminateTintList(ColorStateList.valueOf(Color.parseColor("#56FFC2")));
        tvLoadingText = (view.findViewById(R.id.loadingtxt));
//        tvLoadingText.setVisibility(View.VISIBLE);
//        progressBar.setVisibility(View.VISIBLE);
        txtHeaderModuleName = (TextView) view.findViewById(R.id.txtMoodle);
        if (main.chkPageUIVisibility) {
            loadPageUIShow();

        } else {
            // main.callTeamUrl = true;
            loadPageUIHide();
        }

        urlLink = "https://www.microsoft365.com/?auth=2&home=1";
        txtHeaderModuleName.setText(R.string.office_365_login);


//        try {
//            cookiesJson.put("cleanCookies", "true");
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }

//        mWebView.getSettings().setGeolocationEnabled(true);
//        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);

        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
//        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
////        mWebView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36");
//        mWebView.getSettings().getAllowContentAccess();
//        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
//        mWebView.getSettings().setAllowFileAccessFromFileURLs(true);


//        mWebView.clearHistory();
//        mWebView.clearCache(true);


//Make sure no autofill for Forms/ user-name password happens for the app
//        mWebView.clearFormData();
//        mWebView.getSettings().setSavePassword(false);
//        mWebView.getSettings().setSaveFormData(false);
//        WebViewDatabase.getInstance(main.currentContext).clearFormData();

        loadUrl(urlLink);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager = CookieManager.getInstance();
//            cookieManager.setAcceptThirdPartyCookies(mWebView, false);
//            cookieManager.setAcceptFileSchemeCookies(false);
//
//
            cookieManager.setAcceptCookie(true);
////            cookieManager.acceptCookie();
//            cookieManager.removeSessionCookie();
//            cookieManager.setCookie(urlLink, "$cookieKey=$cookieValue");
//
//            CookieSyncManager.getInstance().sync();
//
//
//            setCookie(new DefaultHttpClient(), urlLink);

        }
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!loadingFinished) {
                    redirect = true;
                }
                loadingFinished = false;
                view.loadUrl(url);
                return false;
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.v("VIEWTITLE", "VIEWTITLE ON PAGE STARTED Called");
                chkForURLLoadFinished = false;
                super.onPageStarted(view, url, favicon);
                loadingFinished = false;
            }

            public void onPageFinished(WebView view, final String url) {
                if (chkForURLLoadFinished == false) {
                    chkForURLLoadFinished = true;
                    executeRequest(url);
                }


//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
                Log.v("VIEWTITLE:", "Office365 VIEWTITLE:  " + view.getTitle());
                if (view.getTitle().contains("Microsoft Teams classic")) {
                    //main.chkForOfficeLoginStatus = true;
                    main.chkForFirstLoad = true;
                    // main.setUrlTitlePref(main.currentContext, view.getTitle());
                    launchApplicationFragment();
                    main.navigateAppPage = false;
                    main.chkShowIndicatorTeams = true;
                    main.chkShowIndicatorTeamsRed = false;
                    main.notifyObserver("STATUSUPDATE");
                    //main.removeMoodleCookiesPref();
                }else if (view.getTitle().contains("Webpage not available")||view.getTitle().contains("Web page not available")) {
                    main.chkShowIndicatorTeamsRed = true;
                    main.navigateAppPage = false;
                    launchApplicationFragment();
                    main.notifyObserver("STATUSUPDATE");
                } else if (view.getTitle().contains("Microsoft Teams - Error")) {
                    main.chkShowIndicatorTeamsRed = true;
                    main.chkShowIndicatorTeams = false;
                    main.notifyObserver("STATUSUPDATE");

                }else if (view.getTitle().contains("Sign out")) {
                    main.chkShowIndicatorTeamsRed = false;
                    main.chkShowIndicatorTeams = false;
                    main.removeTeamCookiesPref();
                    main.removeCombineCookiesPref();
                    main.removeMoodleCookiesPref();
                    ClearWebCookiesData();
                    main.notifyObserver("STATUSUPDATE");

                }
//                else if (view.getTitle().equals("")) {
//                    main.chkForFirstLoad = true;
//                    main.chkShowIndicatorTeams = true;
//                    main.chkShowIndicatorTeamsRed = false;
//                    main.notifyObserver("STATUSUPDATE");
//
//                }
                else if (view.getTitle().contains("teams.microsoft.com/_#/code=0") || view.getTitle().contains("Home | Microsoft 365")) {
                    //main.setUrlTitlePref(main.currentContext, view.getTitle());
                    main.chkForFirstLoad = true;
                    //main.chkForOfficeLoginStatus = true;
//                            main.setCookiesPrefForOffice(main.currentContext,"OFFICELOGGEDIN");
                    launchApplicationFragment();
                    main.navigateAppPage = false;
                    main.chkShowIndicatorTeams = true;
                    main.chkShowIndicatorTeamsRed = false;
                    main.notifyObserver("STATUSUPDATE");

                    //  main.removeMoodleCookiesPref();
                } else if (!main.getTeamCookiesPref(main.currentContext).isEmpty()) {
//                    if (view.getTitle().equals("")) {
//                        main.navigateAppPage = false;
//                        //main.chkForOfficeLoginStatus = true;
//                        main.chkShowIndicatorTeams = true;
//                        main.chkShowIndicatorTeamsRed = false;
//                        main.notifyObserver("STATUSUPDATE");
//                    } else
                        if (view.getTitle().contains("Sign in to your account")) {
                        main.navigateAppPage = false;
                        //main.chkForOfficeLoginStatus = true;
                        main.chkShowIndicatorTeamsRed = false;
                        main.chkShowIndicatorTeams = true;
                        main.notifyObserver("STATUSUPDATE");
                    } else if (view.getTitle().contains("checking your credentials")) {
                        main.navigateAppPage = false;
                        //main.chkForOfficeLoginStatus = true;
                        main.chkShowIndicatorTeamsRed = false;
                        main.chkShowIndicatorTeams = true;
                        main.notifyObserver("STATUSUPDATE");
                    }
                } else if (view.getTitle().contains("Home | Microsoft 365")) {
//                            main.setCookiesPrefForOffice(main.currentContext,"OFFICELOGGEDIN");
                    main.chkForFirstLoad = true;
                    //main.chkForOfficeLoginStatus = true;
                    launchApplicationFragment();
                    main.navigateAppPage = false;
                    main.chkShowIndicatorTeams = true;
                    main.chkShowIndicatorTeamsRed = false;
                    main.notifyObserver("STATUSUPDATE");
                }
//                else if (view.getTitle().contains("Webpage not available")) {
//                    main.chkShowIndicatorMoodlesRed = true;
//                    main.navigateAppPage = false;
//                    launchApplicationFragment();
//                }
                progressBar.setVisibility(View.GONE);
                tvLoadingText.setVisibility(View.GONE);
//                    }
//                }, 5000);

                getCookiesJsonData(url, view.getTitle());

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                onReceivedError(view, error.getErrorCode(), error.getDescription().toString(), request.getUrl().toString());
                if(!main.getTeamCookiesPref(main.currentContext).isEmpty()){
                    main.chkShowIndicatorTeamsRed=true;
                    main.notifyObserver("STATUSUPDATE");
//                    if(!main.chkNetworkStatus(main.currentContext)){
//
//                        main.chkShowIndicatorTeamsRed=true;
//                        main.chkShowIndicatorTeams=false;
//                    }else{
//                        main.chkShowIndicatorTeamsRed=false;
//                        main.chkShowIndicatorTeams=true;
//                    }

                }
//                if(!main.getMoodleCookiesPref(main.currentContext).isEmpty()) {
//                    if (!main.chkNetworkStatus(main.currentContext)) {
//                        MoodleIndicator.setBackgroundResource(R.drawable.red_indicator);
//                        main.chkShowIndicatorMoodlesRed=true;
//                        main.chkShowIndicatorMoodles=false;
//                    }else{
//                        main.chkShowIndicatorMoodlesRed=false;
//                        main.chkShowIndicatorMoodles=true;
//                    }
//                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.setApplicationViewFragment(getActivity());
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @SuppressLint("ResourceAsColor")
    public void loadPageUIHide() {
        rootLayout.setVisibility(View.GONE);
        rootLayout.setBackgroundColor(android.R.color.transparent);
        rAppTilteBar.setVisibility(View.GONE);
        rAppTilteBar.setBackgroundColor(android.R.color.transparent);
        txtHeaderModuleName.setVisibility(View.GONE);
        txtHeaderModuleName.setTextColor(android.R.color.transparent);
        btnBack.setVisibility(View.GONE);
        mWebView.setVisibility(View.GONE);
        main.navigateAppPage = true;

    }

    @SuppressLint("ResourceAsColor")
    public void loadPageUIShow() {
        rootLayout.setVisibility(View.VISIBLE);
        rootLayout.setBackgroundColor(android.R.color.transparent);
        rAppTilteBar.setVisibility(View.VISIBLE);
        rAppTilteBar.setBackgroundColor(Color.parseColor("#000000"));
        txtHeaderModuleName.setVisibility(View.VISIBLE);
        txtHeaderModuleName.setTextColor(Color.parseColor("#FFFFFFFF"));
        btnBack.setVisibility(View.VISIBLE);
        mWebView.setVisibility(View.VISIBLE);


    }

    private void launchApplicationFragment() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (main.currentFragment instanceof App_Module_Fragment) {
//                    if (!main.navigateAppPage) {
//                        main.setApplicationViewFragment(main.currentContext);
//                    }
//                }
//            }
//        }, 10000);
    }


    @Override
    public void onResume() {
        super.onResume();
        CookieSyncManager.getInstance().sync();
//        getAuthTokenClientHttpPost();
//        sendHttpPostRequest();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        main.setOrientation(main.currentContext);
    }

    private void setCookie(DefaultHttpClient httpClient, String url) {
        List<Cookie> cookies = httpClient.getCookieStore().getCookies();
        if (cookies != null) {
            CookieSyncManager.createInstance(getContext());
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);

            for (int i = 0; i < cookies.size(); i++) {
                Cookie cookie = cookies.get(i);
                String cookieString = cookie.getName() + "=" + cookie.getValue();
                cookieManager.setCookie(url, cookieString);
            }
            CookieSyncManager.getInstance().sync();
        }
    }

    public void getCookiesJsonData(String url, String title) {
        String tempCookie = cookieManager.getCookie(url);

        String cookies = String.valueOf(main.stringBufferCookiesDataTemp.append(tempCookie));

        Log.v("tempCookieValue", "tempCookieValue=" + tempCookie);
        Log.v("CookiesValue", "CookiesValue=" + cookies);
        if (title.contains("teams.microsoft.com/_#/code=0") || title.contains("Home | Microsoft 365")) {
            if (cookies != null&&!strDomainValue.trim().equals("")&&main.getTeamCookiesPref(main.currentContext).isEmpty()) {
                // if (cookies.contains("ESTSAUTHPERSISTENT")) {
                cookiesArray = new JSONArray();
                cookiesJson = new JSONObject();
                String[] cookieArray = cookies.toString().split(";");

                for (String strCookie : cookieArray) {
//                    if (!strCookie.equals("cookieKey=$cookieValue")) {
                    Log.v("strCookie123:", "strCookie123: " + strCookie);

                    String[] keyValue = strCookie.split("=");

                    if (keyValue.length == 2) {

                        jsonMapValueData.put(keyValue[0].trim(), keyValue[1].trim());




                    }
//                    List list=null;
//                    Set set = jsonMapValueData.entrySet();
//                    Iterator i = set.iterator();
//                    while(i.hasNext()) {
//                        Map.Entry me = (Map.Entry)i.next();
//                        list= Collections.singletonList(jsonMapValueData.get(me.getKey()));
//
//                        for(int j=0;j<list.size();j++)
//                        {
//                            System.out.println(me.getKey()+": value :"+list.get(j));
//                        }
//                    }
//                    else {
//                        StringBuffer stringBuffer1 = new StringBuffer();
//                        if (keyValue.length > 0) {
//                            for(int i=0;i< keyValue.length;i++){
//                                stringBuffer1.append(keyValue[i]+"=");
//
//                            }
//                            String str =stringBuffer1.toString();
////
//                            str = str.substring(0, str.length() - 1);
//                            jsonMapValueData.put(keyValue[0], str);
//                            stringBuffer1.delete(0, stringBuffer1.length());
//                           // jsonMapValueData.put(keyValue[0], keyValue[1] + "=" + keyValue[2] + "=" + keyValue[3]);
//                        }
//                        JSONObject cookieObj1 = new JSONObject();
//
//                        try {
//                            cookieObj1.put("Name", keyValue[0].trim());
//                            StringBuffer stringBuffer = new StringBuffer();
//                            if (keyValue.length > 0) {
//                               for(int i=0;i< keyValue.length;i++){
//                                   stringBuffer.append(keyValue[i]+"=");
//
//                               }
//                               String str =stringBuffer.toString();
////
//                                str = str.substring(0, str.length() - 1);
//                                cookieObj1.put("Value", str);
//                                stringBuffer.delete(0, stringBuffer.length());
//                            }
//                            cookieObj1.put("Expires", strExpireValue);
//                            cookieObj1.put("Path", strPath);
//
//                            if (IsSecureValue.getIsSecureValue() == IsSecureValue.secure) {
//                                cookieObj1.put("IsSecure", true);
//                            } else {
//                                cookieObj1.put("IsSecure", false);
//                            }
//
//                            if (IsHttpOnlyValue.getIsHttpOnlyValue() == IsHttpOnlyValue.httponly) {
//                                cookieObj1.put("IsHttpOnly", true);
//                            } else {
//                                cookieObj1.put("IsHttpOnly", false);
//                            }
//
//                            if (SameSiteValue.getSameSiteValue() == SameSiteValue.none) {
//                                cookieObj1.put("SameSite", false);
//                            } else {
//                                cookieObj1.put("SameSite", true);
//                            }
//                            cookieObj1.put("Domain", strDomainValue);
//                            if (strDomainValue.equals("") || strDomainValue.equals(null)) {
//                                strDomainValue = ".login.microsoftonline.com";
//                            }
//
//                            cookieObj1.put("IsSession", false);
//                            cookiesArray.put(cookieObj1);
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }
                }
                for (Map.Entry<String, String> entry : jsonMapValueData.entrySet()) {
                    JSONObject cookieObj = new JSONObject();

                    try {
                        cookieObj.put("Name", entry.getKey());
                        cookieObj.put("Value", entry.getValue());
                        cookieObj.put("Expires", strExpireValue);
                        cookieObj.put("Path", strPath);
                        if (IsSecureValue.getIsSecureValue() == IsSecureValue.secure) {
                            cookieObj.put("IsSecure", true);
                        } else {
                            cookieObj.put("IsSecure", false);
                        }

                        if (IsHttpOnlyValue.getIsHttpOnlyValue() == IsHttpOnlyValue.httponly) {
                            cookieObj.put("IsHttpOnly", true);
                        } else {
                            cookieObj.put("IsHttpOnly", false);
                        }

                        if (SameSiteValue.getSameSiteValue() == SameSiteValue.none) {
                            cookieObj.put("SameSite", false);
                        } else {
                            cookieObj.put("SameSite", true);
                        }
                        cookieObj.put("Domain", strDomainValue);
                        if (strDomainValue.equals("") || strDomainValue.equals(null)) {
                            strDomainValue = ".login.microsoftonline.com";
                        }

                        cookieObj.put("IsSession", false);
                        cookiesArray.put(cookieObj);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    JSONObject cookiesJson1 = new JSONObject();

//                    cookiesJson.put("uri", urlLink);
                    cookiesJson.put("cookies", cookiesArray);
                    cookiesJson1.put("cookies", cookiesJson);
                    main.strJsonDataVal = "";
                    main.strJsonDataVal = cookiesJson1.toString();
                    main.chkForFirstLoad = true;
                    //main.chkForOfficeLoginStatus = true;
                    main.setCookiesPref(main.currentContext, "Office365cookiesdata");
                    main.stringBufferCookiesData.append(main.strJsonDataVal);
                    main.setCombineCookiesPref(main.currentContext, String.valueOf(main.stringBufferCookiesData));
//                    main.removeMoodleCookiesPref();
                    main.setTeamCookiesPref(main.currentContext, main.strJsonDataVal);
//                    launchApplicationFragment();
                    System.out.println("cookiesJson:" + cookiesJson.toString());

                    // callCookiesPostData();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

//            }else
//                if (title.contains("Working")) {
////                    if (!main.getMoodleCookiesPref(main.currentContext).isEmpty()) {
////                        main.setTeamCookiesPref(main.currentContext, main.getMoodleCookiesPref(main.currentContext));
////                        Log.v("", "");
////                    }
//                }
            }
        }
    }

    public void convertStringToJson(Map<String, List<String>> cookiemapdata) {
        Log.v("convertStringToJson", "convertStringToJson function called ");
        for (Map.Entry<String, List<String>> entry : cookiemapdata.entrySet()) {

            // if (entry.getKey() != null && entry.getKey().equals("Set-Cookie")) {
            chkForURLLoadFinished = true;
            System.out.println("CookiesJSONVALUE123: " + entry.getKey());
            for (Iterator i = entry.getValue().iterator(); i.hasNext(); ) {
                JSONObject cookieObj = new JSONObject();
                String key = (String) i.next();
                // if(key.toString().equals("HTTP/1.1 200 OK")){
                System.out.println("CookiesJSONVALUE123: " + key.toString());

                String[] cookieArray = key.toString().split(";");
                System.out.println("CookiesJSONVALUE123212321: " + key.toString());
                for (String cookie : cookieArray) {
                    String[] keyValue = cookie.split("=");
                    if (keyValue.length == 2) {
                        if (cookie.toString().contains("path")) {
                            strPath = keyValue[1].trim();
                        } else if (cookie.toString().contains("expires")) {
                            strExpireValue = keyValue[1].toString().trim();
                            Log.v("strExpireValue", "strExpireValue" + strExpireValue);
                        } else if (cookie.toString().contains("domain")) {

                            strDomainValue = keyValue[1].toString().trim();
                            if (strDomainValue.equals("") || strDomainValue.equals(null) || strDomainValue.equals("office.com")) {
                                strDomainValue = ".login.microsoftonline.com";
                            }
                        } else if (cookie.toString().contains("SameSite") || cookie.toString().contains("samesite")) {
                            if (cookie.contains("None")) {
                                SameSiteValue.setSameSiteValue(SameSiteValue.none);
                            }
                        }

                    } else {
                        Log.v("kookie", "kookie" + cookie);
                        if (cookie.toString().contains("httponly") || cookie.toString().contains("HttpOnly")) {
                            Log.v("", "");
                            IsHttpOnlyValue isHttpOnlyValue;
                            if (cookie.contains("httponly")) {
                                isHttpOnlyValue = IsHttpOnlyValue.httponly;
                                IsHttpOnlyValue.setIsHttpOnlyValue(isHttpOnlyValue);
                            }
                        } else if (cookie.toString().contains("secure")) {
                            Log.v("", "");
                            IsSecureValue isSecureValue;
                            if (cookie.contains("secure")) {
                                isSecureValue = IsSecureValue.secure;
                                IsSecureValue.setIsSecureValue(isSecureValue);
                            }
                        }
                    }

                }


                // }
            }

        }


    }

    private WebResourceResponse executeRequest(final String url) {

        ((Activity) main.currentContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                URLConnection connection = null;
                try {
                    connection = new URL(url).openConnection();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                final URLConnection finalConnection = connection;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Map<String, List<String>> cookie = finalConnection.getHeaderFields();

                        convertStringToJson(cookie);
                    }
                }).start();

            }
        });

        return null;
    }

    private void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    @Override
    public void update(Observable o, Object data) {
        if (data.equals("ClearCookies")) {
            handler.sendEmptyMessage(1001);
        }
//        else if (data.equals("PasswordNotExpired")) {
//            handler.sendEmptyMessage(1002);
//        }
    }
}
