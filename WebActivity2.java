//package com.kramer.smauthenticator.Activity;
//
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.StrictMode;
//import android.util.Log;
//import android.webkit.CookieManager;
//import android.webkit.CookieSyncManager;
//import android.webkit.WebResourceRequest;
//import android.webkit.WebResourceResponse;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//
//import com.kramer.smauthenticator.R;
//import com.kramer.smauthenticator.javaClass.MainClass;
//import com.kramer.smauthenticator.utility.IsHttpOnlyValue;
//import com.kramer.smauthenticator.utility.IsSecureValue;
//import com.kramer.smauthenticator.utility.SameSiteValue;
//
//import org.apache.http.cookie.Cookie;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLConnection;
//import java.net.URLEncoder;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import okhttp3.MediaType;
//
//public class WebActivity2 extends Activity {
//    public static final MediaType JSON
//            = MediaType.parse("content-type\", application/x-www-form-urlencoded; charset=utf-8");
//    MainClass mainClass;
//    HashMap<String, String> jsonMapValueData = new HashMap<>();
//    JSONObject authclientJsonObject = new JSONObject();
//    String tokenEndpoint = "https://key.kramerupl.com:8443/realms/kramer/protocol/openid-connect/token";
//    WebView mWebView;
//    String strJsonDataVal;
//    String strPath = "";
//    String strExpireValue = "";
//    String strDomainValue = "";
//    CookieManager cookieManager;
//    JSONArray cookiesArray = new JSONArray();
//    boolean redirect = false;
//    boolean loadingFinished = true;
//
//
//
//    boolean chkForURLLoadFinished = false;
//    JSONObject cookiesJson = new JSONObject();
//    String urlLink;
//
//
//    public void convertStringToJson(Map<String, List<String>> cookiemapdata) {
//        Log.v("convertStringToJson", "convertStringToJson function called ");
//        for (Map.Entry<String, List<String>> entry : cookiemapdata.entrySet()) {
//
//            if (entry.getKey() != null && entry.getKey().equals("Set-Cookie")) {
//                chkForURLLoadFinished = true;
//
//                for (Iterator i = entry.getValue().iterator(); i.hasNext(); ) {
//                    JSONObject cookieObj = new JSONObject();
//                    String key = (String) i.next();
//                    System.out.println("CookiesJSONVALUE123: " + key.toString());
//                    String[] cookieArray = key.toString().split(";");
//                    for (String cookie : cookieArray) {
//                        String[] keyValue = cookie.split("=");
//                        if (keyValue.length == 2) {
//                            if (cookie.toString().contains("path")) {
//                                strPath = keyValue[1].trim();
//                            } else if (cookie.toString().contains("expires")) {
//                                strExpireValue = keyValue[1].toString().trim();
//                            } else if (cookie.toString().contains("domain")) {
//                                strDomainValue = keyValue[1].toString().trim();
//                            } else if (cookie.toString().contains("SameSite") || cookie.toString().contains("samesite")) {
//                                if (cookie.contains("None")) {
//                                    SameSiteValue.setSameSiteValue(SameSiteValue.none);
//                                }
//                            }
//
//                        } else {
//                            Log.v("kookie", "kookie" + cookie);
//                            if (cookie.toString().contains("httponly") || cookie.toString().contains("HttpOnly")) {
//                                Log.v("", "");
//                                IsHttpOnlyValue isHttpOnlyValue;
//                                if (cookie.contains("httponly")) {
//                                    isHttpOnlyValue = IsHttpOnlyValue.httponly;
//                                    IsHttpOnlyValue.setIsHttpOnlyValue(isHttpOnlyValue);
//                                }
//                            } else if (cookie.toString().contains("secure")) {
//                                Log.v("", "");
//                                IsSecureValue isSecureValue;
//                                if (cookie.contains("secure")) {
//                                    isSecureValue = IsSecureValue.secure;
//                                    IsSecureValue.setIsSecureValue(isSecureValue);
//                                }
//                            }
//                        }
//
//                    }
//
//
//                }
//            }
//
//        }
//
//
//    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(1);
//        setContentView(R.layout.activity_web2);
//
//        Bundle extras = getIntent().getExtras();
//        urlLink = extras.getString("url");
//        mainClass = MainClass.getMainObj();
//        try {
//
//            cookiesJson.put("cleanCookies", "true");
//
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
//        this.mWebView = (WebView) findViewById(R.id.web_view_2);
//        this.mWebView.getSettings().setGeolocationEnabled(true);
//        this.mWebView.getSettings().setDatabaseEnabled(true);
//        this.mWebView.getSettings().setDomStorageEnabled(true);
//        this.mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
//        this.mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        this.mWebView.getSettings().setDomStorageEnabled(true);
//        this.mWebView.getSettings().setJavaScriptEnabled(true);
//        this.mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
//        this.mWebView.getSettings().getAllowContentAccess();
//        this.mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
//        this.mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            cookieManager = CookieManager.getInstance();
//            cookieManager.setAcceptThirdPartyCookies(mWebView, true);
//            cookieManager.setAcceptFileSchemeCookies(true);
//
//
//            cookieManager.setAcceptCookie(true);
//            cookieManager.acceptCookie();
//            cookieManager.removeSessionCookie();
//            cookieManager.setCookie("https://teams.microsoft.com/", "$cookieKey=$cookieValue");
//
//            CookieSyncManager.getInstance().sync();
//
//
//            setCookie(new DefaultHttpClient(), "https://teams.microsoft.com/");
//
//        }
//        this.mWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                return super.shouldOverrideUrlLoading(view, request);
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                if (!WebActivity2.this.loadingFinished) {
//                    WebActivity2.this.redirect = true;
//                }
//                WebActivity2.this.loadingFinished = false;
//                view.loadUrl(url);
//                return true;
//            }
//
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                chkForURLLoadFinished = false;
//                super.onPageStarted(view, url, favicon);
//                WebActivity2.this.loadingFinished = false;
//            }
//
//            public void onPageFinished(WebView view, final String url) {
//                if (chkForURLLoadFinished == false) {
//                    chkForURLLoadFinished = true;
//                    executeRequest(url);
//                }
//
//                jsonMapValueData.clear();
//                getCookiesJsonData(url);
//
//                loadUrl(urlLink);
//            }
//
//
//        });
//    }
//    public void getCookiesJsonData(String url) {
//        String cookies = cookieManager.getCookie(url);
//        if (cookies != null) {
//            if (cookies.contains("ESTSAUTHPERSISTENT")) {
//                cookiesArray = new JSONArray();
//                cookiesJson = new JSONObject();
//                String[] cookieArray = cookies.toString().split(";");
//
//                for (String strCookie : cookieArray) {
//                    String[] keyValue = strCookie.split("=");
//
//                    if (keyValue.length == 2) {
//
//                        jsonMapValueData.put(keyValue[0], keyValue[1]);
//                        JSONObject cookieObj = new JSONObject();
//
//                        try {
//                            cookieObj.put("Name", keyValue[0].trim());
//                            cookieObj.put("Value", keyValue[1].trim());
//                            cookieObj.put("Expires", strExpireValue);
//                            cookieObj.put("Path", strPath);
//                            if (IsSecureValue.getIsSecureValue() == IsSecureValue.secure) {
//                                cookieObj.put("IsSecure", true);
//                            } else {
//                                cookieObj.put("IsSecure", false);
//                            }
//
//                            if (IsHttpOnlyValue.getIsHttpOnlyValue() == IsHttpOnlyValue.httponly) {
//                                cookieObj.put("IsHttpOnly", true);
//                            } else {
//                                cookieObj.put("IsHttpOnly", false);
//                            }
//
//                            if (SameSiteValue.getSameSiteValue() == SameSiteValue.none) {
//                                cookieObj.put("SameSite", false);
//                            } else {
//                                cookieObj.put("SameSite", true);
//                            }
//                            cookieObj.put("Domain", strDomainValue);
//                            cookieObj.put("IsSession", false);
//                            cookiesArray.put(cookieObj);
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//
//                    } else {
//                        jsonMapValueData.put(keyValue[0], keyValue[1] + "=" + keyValue[2] + "=" + keyValue[3]);
//                        JSONObject cookieObj1 = new JSONObject();
//
//                        try {
//                            cookieObj1.put("Name", keyValue[0].trim());
//                            cookieObj1.put("Value", keyValue[1] + "=" + keyValue[2] + "=" + keyValue[3].trim());
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
//
//
//                            cookieObj1.put("Domain", strDomainValue);
//                            cookieObj1.put("IsSession", false);
//                            cookiesArray.put(cookieObj1);
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }
//                try {
//                    JSONObject cookiesJson1 = new JSONObject();
//
//                    cookiesJson.put("uri", "https://teams.microsoft.com/");
//                    cookiesJson.put("cookies", cookiesArray);
//                    cookiesJson1.put("cookies", cookiesJson);
//                    strJsonDataVal = cookiesJson1.toString();
//                    System.out.println("cookiesJson:" + cookiesJson.toString());
//
//                    mainClass.callCookiesPostData();
//                } catch (JSONException e) {
//                    throw new RuntimeException(e);
//                }
//
//            }
//        }
//    }
//
//    private WebResourceResponse executeRequest(final String url) {
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                URLConnection connection = null;
//                try {
//                    connection = new URL(url).openConnection();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//                final URLConnection finalConnection = connection;
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        final Map<String, List<String>> cookie = finalConnection.getHeaderFields();
//
//                        convertStringToJson(cookie);
//                    }
//                }).start();
//
//            }
//        });
//
//        return null;
//    }
//
//    private void setCookie(DefaultHttpClient httpClient, String url) {
//        List<org.apache.http.cookie.Cookie> cookies = httpClient.getCookieStore().getCookies();
//        if (cookies != null) {
//            CookieSyncManager.createInstance(WebActivity2.this);
//            CookieManager cookieManager = CookieManager.getInstance();
//            cookieManager.setAcceptCookie(true);
//
//            for (int i = 0; i < cookies.size(); i++) {
//                Cookie cookie = cookies.get(i);
//                String cookieString = cookie.getName() + "=" + cookie.getValue();
//                cookieManager.setCookie(url, cookieString);
//            }
//            CookieSyncManager.getInstance().sync();
//        }
//    }
//
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        this.mWebView.saveState(outState);
//    }
//
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        this.mWebView.restoreState(savedInstanceState);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        CookieSyncManager.getInstance().sync();
//        getAuthTokenClientHttpPost();
////        sendHttpPostRequest();
//
//    }
//
//    private void getAuthTokenClientHttpPost() {
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                .permitAll().build();
//
//        WebActivity2.this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        HttpURLConnection urlConnection = null;
//
//                        BufferedReader reader = null;
//
//
//                        try {
//
////                            String tokenEndpoint = "https://key.kramerupl.com:8443/realms/kramer/protocol/openid-connect/token";
//                            String tokenEndpoint = "https://ssoa-devtmp.kramerupl.com/realms/kramer/protocol/openid-connect/token";
//
//                            String password = "%#1?Uj@B9#l4.Pa9:?XHfV09Sc0Z^PBqp52X)8d!I!0Pdy4q%&8Xq=/bQ~Z8?;B";
//
//                            String encodedPassword = URLEncoder.encode(password, "UTF-8");
//
//                            String parameters = "client_id=backend_api&client_secret=D3IHm06VmRWdpKZSgjiprKMfaHuS0TKr&username=cec9a4fc51f2479f9aa9014be559216e&grant_type=password&password=" + encodedPassword;
//
//                            URL url = new URL(tokenEndpoint);
//
//                            urlConnection = (HttpURLConnection) url.openConnection();
//                            urlConnection.setRequestMethod("POST");
//
//                            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//
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
//                                String strData = reader.readLine();
//
//                                authclientJsonObject = new JSONObject(strData);
//
//                                mainClass.strAuthToken = authclientJsonObject.getString("access_token");
//                                Log.v("sdfsd","sdfs");
//
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
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
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
//
////    private void callCookiesPostData() {
////        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
////                .permitAll().build();
////
////        WebActivity2.this.runOnUiThread(new Runnable() {
////            @Override
////            public void run() {
////                new Thread(new Runnable() {
////                    @Override
////                    public void run() {
////                        String response = "";
////
////                        HttpURLConnection urlConnection = null;
////
////                        BufferedReader reader = null;
////                        try {
////                            tokenEndpoint = "https://www.kramerupl.com:8888/session-mgr/api/v1/device/31eefb52-be11-4752-961f-dd554611acc3/pc";
////
////
////                            String parameters = strJsonDataVal;//"client_id=backend_api&client_secret=D3IHm06VmRWdpKZSgjiprKMfaHuS0TKr&username=cec9a4fc51f2479f9aa9014be559216e&grant_type=password&password=" + encodedPassword;
////
////                            URL url = new URL(tokenEndpoint);
////
////                            urlConnection = (HttpURLConnection) url.openConnection();
////                            urlConnection.setRequestMethod("POST");
////                            JSONObject jsonObject = new JSONObject();
////
////                            urlConnection.setRequestProperty("Content-Type", "application/json");
////                            urlConnection.setRequestProperty("Authorization", "Bearer " + strAuthToken);
////                            urlConnection.setDoOutput(true);
////
////                            OutputStream outputStream = urlConnection.getOutputStream();
////
////                            outputStream.write(parameters.getBytes());
////                            outputStream.flush();
////                            outputStream.close();
////
////                            int responseCode = urlConnection.getResponseCode();
////
////                            if (responseCode == HttpURLConnection.HTTP_OK) {
////
////                                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
////
////                                StringBuilder stringBuilder = new StringBuilder();
////
////                                String line;
////                                while ((line = reader.readLine()) != null) {
////
////                                    stringBuilder.append(line).append("\n");
////
////                                }
////                                response = stringBuilder.toString();
////                                Log.v("response", "response" + response.toString());
////
////                            } else {
////
////                                Log.e("HTTP Error", "Response Code: " + responseCode);
////
////                            }
////
////                        } catch (IOException e) {
////
////                            e.printStackTrace();
////
////                        } finally {
////
////                            if (urlConnection != null) {
////
////                                urlConnection.disconnect();
////
////                            }
////
////                            if (reader != null) {
////
////                                try {
////
////                                    reader.close();
////
////                                } catch (IOException e) {
////
////                                    e.printStackTrace();
////
////                                }
////
////                            }
////
////                        }
////                    }
////                }).start();
////
////            }
////        });
////
////    }
//
//    //        private void sendHttpPostRequest(){
////        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
////                .permitAll().build();
////        StrictMode.setThreadPolicy(policy);
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////
////                OkHttpClient.Builder builder = new OkHttpClient.Builder();
////
////                OkHttpClient client = null;
////                client = builder.build();
////                String encodedPassword="";
////                String strPass="%#1?Uj@B9#l4.Pa9:?XHfV09Sc0Z^PBqp52X)8d!I!0Pdy4q%&8Xq=/bQ~Z8?;B";
////                try {
////                     encodedPassword = URLEncoder.encode(strPass, "UTF-8");
////                } catch (UnsupportedEncodingException e) {
////                    throw new RuntimeException(e);
////                }
////                RequestBody formBody = new FormBody.Builder()
////                        .add("client_id", "backend_api")
////                        .add("client_secret ", "D3IHm06VmRWdpKZSgjiprKMfaHuS0TKr")
////                        .add("username ", "cec9a4fc51f2479f9aa9014be559216e")
////                        .add("grant_type", "password")
////                        .add("password ", encodedPassword)
////                        .build();
////                Request request = new Request.Builder().url(tokenEndpoint)
//////                        .addHeader("Content-type", "application/x-www-form-urlencoded")
////                        .post(formBody)
////                        .build();
////
////                    client.newCall(request).enqueue(new Callback() {
////                        @Override
////                        public void onFailure(Call call, IOException e) {
////                            e.printStackTrace();
////                        }
////
////                        @Override
////                        public void onResponse(Call call, Response response) throws IOException {
////                            String strResponse=  response.body().string();
////                            Log.v("","");
////                        }
////                    });
////
////            }
////        }).start();
////    }
//    private void loadUrl(String url) {
//        this.mWebView.loadUrl(url);
//    }
//
//    public void onBackPressed() {
//        if (this.mWebView.canGoBack()) {
//            this.mWebView.goBack();
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//}
