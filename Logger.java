package com.kramer.smauthenticator.utility;


/**
 * Created by Akash on 24-04-2024.
 */

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.FileHandler;

public class Logger {

    public static FileHandler logger = null;

    public static String LoggerFolder = "displayinfo";

    private static String filename = "sm_auth_log";

    private static String filenamecodecinfo = "display_codecinfo_log";

    static boolean isExternalStorageAvailable = false;
    static boolean isExternalStorageWriteable = false;
    static String state = Environment.getExternalStorageState();

    public static void resetLog()
    {
        String path = Environment.getExternalStorageDirectory().getPath() + "/" + "smLog.txt";
        File dumpFile = new File(path);
        if (dumpFile.exists()) {
            Log.d("File delete ", "File delete");
            dumpFile.delete();
        }
        try {
            Log.d("File created ", "File created ");
            dumpFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


        File logFile = new File(Environment.getExternalStorageDirectory().getPath() + "/" + filename + ".txt");

        if (logFile.exists()) {

            Log.d("display_log delete ", "File delete");
            logFile.delete();

        }

        try {
            logFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File codecLog = new File(Environment.getExternalStorageDirectory().getPath() + "/" + filenamecodecinfo + ".txt");

        if (codecLog.exists()) {
            Log.d("codecLog delete ", "File delete");
            codecLog.delete();

        }

        try {
            codecLog.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void addRecordToLog(String message) {
        String path = Environment.getExternalStorageDirectory().getPath() + "/" + "viaLog.txt";
        File dumpFile = new File(path);
        if (dumpFile.exists()) {
//            File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/" + LoggerFolder);
//
//            if (Environment.MEDIA_MOUNTED.equals(state)) {
//                if (!dir.exists()) {
//                    dir.mkdirs();
//                }
            File logFile = new File(Environment.getExternalStorageDirectory().getPath() + "/" + filename + ".txt");

            if (!logFile.exists()) {
                try {
                    Log.d("File created ", "File created ");
                    logFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                //BufferedWriter for performance, true to set append to file flag
                BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));

                buf.write(message + "\r\n");
                //buf.append(message);
                buf.newLine();
                buf.flush();
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addRecordToLogCodec(String message) {

        String path = Environment.getExternalStorageDirectory().getPath() + "/" + "viaLog.txt";
        File dumpFile = new File(path);


        if (dumpFile.exists()) {

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                // We can read and write the media
                isExternalStorageAvailable = isExternalStorageWriteable = true;
            } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                // We can only read the media
                isExternalStorageAvailable = true;
                isExternalStorageWriteable = false;
            } else {
                // Something else is wrong. It may be one of many other states, but all we need
                //  to know is we can neither read nor write
                isExternalStorageAvailable = isExternalStorageWriteable = false;
            }

//            File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/" + LoggerFolder);
//            if (Environment.MEDIA_MOUNTED.equals(state)) {
//                if (!dir.exists()) {
//                    dir.mkdirs();
//                }
            File logFile = new File(Environment.getExternalStorageDirectory().getPath() + "/" + filenamecodecinfo + ".txt");
//                File logFile = new File(dir.getAbsolutePath() + "/" + filenamecodecinfo + ".txt");

            if (!logFile.exists()) {
                try {
                    logFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                //BufferedWriter for performance, true to set append to file flag
                BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));

                buf.write(message + "\r\n");
                //buf.append(message);
                buf.newLine();
                buf.flush();
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
