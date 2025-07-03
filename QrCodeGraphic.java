package com.kramer.smauthenticator.qrcodereader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

//import com.Activities.collaboration.CLoginActivity;
//import com.JavaClass.collab8.MainClass;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.barcode.Barcode;
import com.kramer.smauthenticator.javaClass.MainClass;
import com.kramer.smauthenticator.qrcodereader.ui.camera.GraphicOverlay;


/**
 * Created by dev on 7/11/2016.
 */
public class QrCodeGraphic extends GraphicOverlay.Graphic {

    private int mId;

    AESEncryption aesEncryption = new AESEncryption();
    private static final int COLOR_CHOICES[] = {
            Color.RED
    };

    private static int mCurrentColorIndex = 0;

    private Paint mRectPaint;
    private Paint mTextPaint;
    private volatile Barcode mBarcode;
    MainClass mainClass;
    String finalReversedecodedString = "";
//    private static final String initializationVector = "aaaaaaaaaaaaaaaa";
//    private static final String cypherInstance = " 0123456789abcdef0123456789abcdef";

    public QrCodeGraphic(GraphicOverlay overlay) {
        super(overlay);

        mCurrentColorIndex = (mCurrentColorIndex + 1) % COLOR_CHOICES.length;
        final int selectedColor = COLOR_CHOICES[mCurrentColorIndex];
        mainClass = MainClass.getMainObj();
        mRectPaint = new Paint();
        mRectPaint.setColor(selectedColor);
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setStrokeWidth(6.0f);

        mTextPaint = new Paint();
        mTextPaint.setColor(selectedColor);
        mTextPaint.setTextSize(36.0f);

    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public Barcode getBarcode() {
        return mBarcode;
    }

    /**
     * Updates the barcode instance from the detection of the most recent frame.  Invalidates the
     * relevant portions of the overlay to trigger a redraw.
     */
    void updateItem(Barcode barcode) {
        mBarcode = barcode;
        postInvalidate();
    }

    /**
     * Draws the barcode annotations for position, size, and raw value on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {




        Barcode barcode = mBarcode;
        if (barcode == null) {
            return;
        }

        try {
            finalReversedecodedString=aesEncryption.decrypt(barcode.displayValue);
        } catch (Exception e) {
            e.printStackTrace();
            mainClass.notifyObserver("QRCODENOTVALID");
        }

        String[] keyValue = finalReversedecodedString.split(",");
        if (keyValue.length == 3) {
            mainClass.strCompID = keyValue[0];
            mainClass.strDeviceuuid = keyValue[1];
            mainClass.strDeviceName = keyValue[2];
            mainClass.webViewVisibility = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mainClass.setShareInfoFragment(mainClass.currentContext);
                }
            }, 1000);
        } else {
            mainClass.notifyObserver("QRCODENOTVALID");
//            mainClass.strDeviceName = "";
//            mainClass.strDeviceuuid = barcode.displayValue;
        }





//        Barcode barcode = mBarcode;
//        if (barcode == null) {
//            return;
//        }
//        String[] keyValue = barcode.displayValue.split(",");
//        if (keyValue.length == 2) {
//            mainClass.strDeviceuuid = keyValue[0];
//            mainClass.strDeviceName = keyValue[1];
//            mainClass.webViewVisibility = true;
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mainClass.setShareInfoFragment(mainClass.currentContext);
//                }
//            }, 1000);
//        } else {
//            mainClass.notifyObserver("QRCODENOTVALID");
////            mainClass.strDeviceName = "";
////            mainClass.strDeviceuuid = barcode.displayValue;
//        }



        // Draws the bounding box around the barcode.
//        RectF rect = new RectF(barcode.getBoundingBox());

//        int left, top, right, bottom;
//        left = barcode.cornerPoints[0].x;
//        top = barcode.cornerPoints[0].y;
//        right = barcode.cornerPoints[0].x;
//        bottom = barcode.cornerPoints[0].y;
//
//        for (int i = 1; i < barcode.cornerPoints.length; i++) {
//            left = left > barcode.cornerPoints[i].x ? barcode.cornerPoints[i].x : left;
//            top = top > barcode.cornerPoints[i].y ? barcode.cornerPoints[i].y : top;
//            right = right < barcode.cornerPoints[i].x ? barcode.cornerPoints[i].x : right;
//            bottom = bottom < barcode.cornerPoints[i].y ? barcode.cornerPoints[i].y : bottom;
//        }
//
//        canvas.drawRect(left + 10, top - 80, right + 20, bottom - 100, mRectPaint);


//        try {
//            mainClass.url = null;
//            String decrypted = aesEncryption.decrypt(barcode.rawValue);
//           // Toast.makeText(mainClass.currentContext, "QRCode" + decrypted, Toast.LENGTH_LONG).show();
//            //String scannerIP = intent.getStringExtra("ipcode");
//            final String split[] = decrypted.split("#");
//            final String splittedIP = split[0];//192.168.100.226|192.168.100.209
//
//            if (mainClass.meetingJoin) {
//                mainClass.url = "https://teams.microsoft.com/_#/calendarv2";
//               // mainClass.setMeetingJoinFragment(mainClass.currentContext,);
//
//            }
//           // if (mainClass.calendarSync) {
//                //mainClass.url = "https://" + splittedIP + "/calendar2.0/auth.php";
//           // }
//
//            //  mainClass.url  = "https://teams.microsoft.com/_#/calendarv2";
//            //mainClass.url = "https://" + splittedIP + "/1.php";
//            mainClass.webViewVisibility = true;
//
//
////            Intent i = new Intent(Intent.ACTION_VIEW);
////            i.setData(Uri.parse(url));
////            mainClass.currentContext.startActivity(i);
////            mainClass.aesEncryptCheck = true;
//            //if (!(mainClass.currentContext instanceof BarcodeCaptureActivity)) {
//
//            if (mainClass.url != null) {
//
////                Intent in = new Intent(mainClass.currentContext,
////                        BarcodeCaptureActivity.class);
////                in.putExtra("ipcode", decrypted);
////
////                Thread.sleep(100);
////                ((AppCompatActivity)mainClass.currentContext).finish();
////
////                mainClass.currentContext.startActivity(in);
//
//            }
//            //  }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            try {
//                finalReversedecodedString = decryptBase64(barcode.rawValue);
//                Toast.makeText(mainClass.currentContext, "QRCode" + finalReversedecodedString, Toast.LENGTH_LONG).show();
//
////                String url = "https://teams.microsoft.com/_#/calendarv2";
//                //    CWebView myWebView =new CWebView()
//
////                Intent i = new Intent(Intent.ACTION_VIEW);
////                i.setData(Uri.parse(url));
////                mainClass.currentContext.startActivity(i);
//
////                mainClass.aesEncryptCheck = false;
////                if (!(mainClass.currentContext instanceof CLoginActivity)) {
////                    mainClass.autoLoginFromScannerIp = true;
////
////                    Intent in = new Intent(mainClass.currentContext,
////                            CLoginActivity.class);
////                    in.putExtra("ipcode", finalReversedecodedString);
////
////                    try {
////                        Thread.sleep(100);
////                    } catch (InterruptedException ex) {
////                        ex.printStackTrace();
////                    }
////                    mainClass.currentContext.startActivity(in);
////                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                Toast.makeText(mainClass.currentContext, "Invalid QR", Toast.LENGTH_LONG).show();
//
//            }

//        }
    }

    private String decryptBase64(String _strBarCodeRawValue) {
        String strBase64Value = "";
        String decodedString = new String(Base64.decode(
                _strBarCodeRawValue, Base64.DEFAULT));

        StringBuffer sb = new StringBuffer(decodedString);
        String rev = sb.reverse().toString();

        String afterReversedecodedString = new String(
                Base64.decode(rev, Base64.DEFAULT));

        StringBuffer finalReverseSb = new StringBuffer(
                afterReversedecodedString);
        String finalStr = finalReverseSb.reverse().toString();

        strBase64Value = new String(
                Base64.decode(finalStr, Base64.DEFAULT));

        return strBase64Value;
    }
}


//
//package com.qrcodereader;
//
//import android.content.Intent;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.util.Base64;
//
//import com.Activities.collab8.CLoginActivity;
//import com.JavaClass.collab8.MainClass;
//import com.google.android.gms.vision.barcode.Barcode;
//import com.qrcodereader.ui.camera.GraphicOverlay;
//
//
///**
// * Created by dev on 7/11/2016.
// */
//public class QrCodeGraphic extends GraphicOverlay.Graphic {
//
//    private int mId;
//
//    AESEncryption aesEncryption = new AESEncryption();
//    private static final int COLOR_CHOICES[] = {
//            Color.RED
//    };
//
//    private static int mCurrentColorIndex = 0;
//
//    private Paint mRectPaint;
//    private Paint mTextPaint;
//    private volatile Barcode mBarcode;
//    MainClass mainClass;
//    String finalReversedecodedString = "";
//    private static final String initializationVector = "aaaaaaaaaaaaaaaa";
//    private static final String cypherInstance = " 0123456789abcdef0123456789abcdef";
//
//    public QrCodeGraphic(GraphicOverlay overlay) {
//        super(overlay);
//
//        mCurrentColorIndex = (mCurrentColorIndex + 1) % COLOR_CHOICES.length;
//        final int selectedColor = COLOR_CHOICES[mCurrentColorIndex];
//        mainClass = MainClass.getMainObj();
//        mRectPaint = new Paint();
//        mRectPaint.setColor(selectedColor);
//        mRectPaint.setStyle(Paint.Style.STROKE);
//        mRectPaint.setStrokeWidth(6.0f);
//
//        mTextPaint = new Paint();
//        mTextPaint.setColor(selectedColor);
//        mTextPaint.setTextSize(36.0f);
//
//    }
//
//    public int getId() {
//        return mId;
//    }
//
//    public void setId(int id) {
//        this.mId = id;
//    }
//
//    public Barcode getBarcode() {
//        return mBarcode;
//    }
//
//    /**
//     * Updates the barcode instance from the detection of the most recent frame.  Invalidates the
//     * relevant portions of the overlay to trigger a redraw.
//     */
//    void updateItem(Barcode barcode) {
//        mBarcode = barcode;
//        postInvalidate();
//    }
//
//    /**
//     * Draws the barcode annotations for position, size, and raw value on the supplied canvas.
//     */
//    @Override
//    public void draw(Canvas canvas) {
//        Barcode barcode = mBarcode;
//        if (barcode == null) {
//            return;
//        }
//
//        // Draws the bounding box around the barcode.
////        RectF rect = new RectF(barcode.getBoundingBox());
//
//        int left, top, right, bottom;
//        left = barcode.cornerPoints[0].x;
//        top = barcode.cornerPoints[0].y;
//        right = barcode.cornerPoints[0].x;
//        bottom = barcode.cornerPoints[0].y;
//
//        for (int i = 1; i < barcode.cornerPoints.length; i++) {
//            left = left > barcode.cornerPoints[i].x ? barcode.cornerPoints[i].x : left;
//            top = top > barcode.cornerPoints[i].y ? barcode.cornerPoints[i].y : top;
//            right = right < barcode.cornerPoints[i].x ? barcode.cornerPoints[i].x : right;
//            bottom = bottom < barcode.cornerPoints[i].y ? barcode.cornerPoints[i].y : bottom;
//        }
//
//        canvas.drawRect(left + 10, top - 80, right + 20, bottom - 100, mRectPaint);
//
//
//        try {
//
////            String decodedString = new String(Base64.decode(
////                    barcode.rawValue, Base64.DEFAULT));
////
////            StringBuffer sb = new StringBuffer(decodedString);
////            String rev = sb.reverse().toString();
////
////            String afterReversedecodedString = new String(
////                    Base64.decode(rev, Base64.DEFAULT));
////
////            StringBuffer finalReverseSb = new StringBuffer(
////                    afterReversedecodedString);
////            String finalStr = finalReverseSb.reverse().toString();
////
////            finalReversedecodedString = new String(
////                    Base64.decode(finalStr, Base64.DEFAULT));
//
//            finalReversedecodedString = decryptBase64(barcode.rawValue);
//
////
////              mainClass.aesEncryptCheck = true;
////
////
////                String decrypted = aesEncryption.decrypt(barcode.rawValue);
//                if (!(mainClass.currentContext instanceof CLoginActivity)) {
//                    mainClass.autoLoginFromScannerIp = true;
//
//                    Intent in = new Intent(mainClass.currentContext,
//                            CLoginActivity.class);
//                    in.putExtra("ipcode", finalReversedecodedString);
//
//                    Thread.sleep(100);
//                    mainClass.currentContext.startActivity(in);
//                }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            mainClass.aesEncryptCheck = false;
//            if (!(mainClass.currentContext instanceof CLoginActivity)) {
//                mainClass.autoLoginFromScannerIp = true;
//
//                Intent in = new Intent(mainClass.currentContext,
//                        CLoginActivity.class);
//                in.putExtra("ipcode", finalReversedecodedString);
//
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
//                mainClass.currentContext.startActivity(in);
//            }
//            //Toast.makeText(mainClass.currentContext, "Invalid QR", Toast.LENGTH_LONG).show();
//        }
//
//
//    }
//    private String decryptBase64(String _strBarCodeRawValue){
//        String strBase64Value= "";
//        String decodedString = new String(Base64.decode(
//                _strBarCodeRawValue, Base64.DEFAULT));
//
//        StringBuffer sb = new StringBuffer(decodedString);
//        String rev = sb.reverse().toString();
//
//        String afterReversedecodedString = new String(
//                Base64.decode(rev, Base64.DEFAULT));
//
//        StringBuffer finalReverseSb = new StringBuffer(
//                afterReversedecodedString);
//        String finalStr = finalReverseSb.reverse().toString();
//
//        strBase64Value = new String(
//                Base64.decode(finalStr, Base64.DEFAULT));
//
//        return strBase64Value;
//    }
//}


