package com.kramer.smauthenticator.javaClass;

import android.app.Activity;
import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ShareCookiesInfo {
    static MainClass mainClass=MainClass.getMainObj();
    public static JSONArray combineCookiesArray = new JSONArray();
    public static String tokenEndpoint;
      public static String strJsonDataVal = "";
    public static String strJsonDataVal1 = "";
    public static void callCookiesPostData() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();

        ((Activity)mainClass.currentContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String response = "";
                        String strAuthToken="";
                        HttpURLConnection urlConnection = null;

                        BufferedReader reader = null;
                        try {
                            tokenEndpoint = "https://www.kramerupl.com:8888/session-mgr/api/v1/device/31eefb52-be11-4752-961f-dd554611acc3/pc";
                            combineCookiesArray.put(strJsonDataVal);
                            combineCookiesArray.put(strJsonDataVal1);
                            Log.v("finalJsonDataVal","finalJsonDataVal"+combineCookiesArray);
//                            String parameters = finalJsonDataVal;//"client_id=backend_api&client_secret=D3IHm06VmRWdpKZSgjiprKMfaHuS0TKr&username=cec9a4fc51f2479f9aa9014be559216e&grant_type=password&password=" + encodedPassword;
                            URL url = new URL(tokenEndpoint);
                            urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.setRequestMethod("POST");
                            JSONObject jsonObject = new JSONObject();
                            urlConnection.setRequestProperty("Content-Type", "application/json");
                            urlConnection.setRequestProperty("Authorization", "Bearer " + strAuthToken);
                            urlConnection.setDoOutput(true);
                            OutputStream outputStream = urlConnection.getOutputStream();
                            outputStream.write(combineCookiesArray.toString().getBytes());
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
}
