package com.kramer.smauthenticator.Activity;

import android.Manifest;
import android.annotation.SuppressLint;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
//import com.journeyapps.barcodescanner.CaptureActivity;
//import com.journeyapps.barcodescanner.ScanOptions;
import com.kramer.smauthenticator.R;
import com.kramer.smauthenticator.javaClass.MainClass;
import com.kramer.smauthenticator.qrcodereader.QrCodeGraphic;
import com.kramer.smauthenticator.qrcodereader.QrcodeTrackerFactory;
import com.kramer.smauthenticator.qrcodereader.ui.camera.CameraSource;
import com.kramer.smauthenticator.qrcodereader.ui.camera.CameraSourcePreview;
import com.kramer.smauthenticator.qrcodereader.ui.camera.GraphicOverlay;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public final class BarcodeCaptureFragment extends Fragment implements Observer {
    private static final String TAG = "Barcode-reader";

    // intent request code to handle updating play services if needed.
    private static final int RC_HANDLE_GMS = 9001;

    // permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    // constants used to pass extra data in the intent
    public static final String AutoFocus = "AutoFocus";
    public static final String UseFlash = "UseFlash";
    public static final String BarcodeObject = "Barcode";

    private CameraSource mCameraSource;
    private CameraSourcePreview mPreview;
    private GraphicOverlay<QrCodeGraphic> mGraphicOverlay;

    // helper objects for detecting taps and pinches.
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
  //  private ImageButton imgcrossicon;
    MainClass mainClass;
    ImageButton scanLine;
    Button btnQrInfo;
    boolean autoFocus = true;
    boolean useFlash = false;

    /**
     * Initializes the UI and creates the detector pipeline.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mainClass=MainClass.getMainObj();
        mainClass.currentContext=getActivity();
        mainClass.currentFragment =BarcodeCaptureFragment.this;
        mainClass.addObserver(this);
        //setContentView(R.layout.fragment_qr_code_scanner);

//        mPreview =  findViewById(R.id.preview);
//        //imgcrossicon= findViewById(R.id.crossicon);
//        mGraphicOverlay = findViewById(R.id.graphicOverlay);
//        mainClass =MainClass.getMainObj();
//        mainClass.currentContext = BarcodeCaptureActivity.this;
//        // read parameters from the intent used to launch the activity.
//        boolean autoFocus = true;
//        boolean useFlash = false;
//
//        // Check for the camera permission before accessing the camera.  If the
//        // permission is not granted yet, request permission.
//        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
//        if (rc == PackageManager.PERMISSION_GRANTED) {
//            createCameraSource(autoFocus, useFlash);
//            gestureDetector = new GestureDetector(this, new CaptureGestureListener());
//            scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
//            startCameraSource();
//        } else {
//            requestCameraPermission();
//        }


//        imgcrossicon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//                mainClass.notifyObserver("CloseBarCodeView");
//            }
//        });
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_code_scanner, container, false);
        {
            mainClass=MainClass.getMainObj();
            mainClass.currentContext=getActivity();
           // mainClass.currentFragment=BarcodeCaptureFragment.this;
            mPreview =  view.findViewById(R.id.preview);
            btnQrInfo = view.findViewById(R.id .btn_txt_info);
            //imgcrossicon= findViewById(R.id.crossicon);
            mGraphicOverlay = view.findViewById(R.id.graphicOverlay);
            scanLine=view.findViewById(R.id.scanLine);
            mainClass =MainClass.getMainObj();
            mainClass.navigateAppPage=true;
            // read parameters from the intent used to launch the activity.

            scanLine.startAnimation(AnimationUtils.loadAnimation(mainClass.currentContext, R.anim.scan_animation));
            // Check for the camera permission before accessing the camera.  If the
            // permission is not granted yet, request permission.
            int rc = ActivityCompat.checkSelfPermission(mainClass.currentContext, Manifest.permission.CAMERA);
            if (rc == PackageManager.PERMISSION_GRANTED) {

                createCameraSource(autoFocus, useFlash);
                gestureDetector = new GestureDetector(mainClass.currentContext, new CaptureGestureListener());
                scaleGestureDetector = new ScaleGestureDetector(mainClass.currentContext, new ScaleListener());
                startCameraSource();

            } else {
                requestCameraPermission();
            }
        }
        return view;
    }
    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */
    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        requestPermissions( permissions, RC_HANDLE_CAMERA_PERM);
//        return;
    }
    @SuppressLint({"LongLogTag", "SourceLockedOrientationActivity"})
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mainClass.setOrientation(mainClass.currentContext);
//        mainClass.setOrientation(mainClass.currentContext);
//
//        mainClass.setUIChanged(true);


    }
//    public void setCode(){
//        ScanOptions options=new ScanOptions();
//        options.setBeepEnabled(true);
//        options.setOrientationLocked(true);
//        options.setCaptureActivity(CaptureActivity.class);
//
//    }

//    ActivityResultLauncher<ScanOptions> barLauncher=getActivity().registerForActivityResult

   // @Override
//    public boolean onTouchEvent(MotionEvent e) {
//        boolean b = scaleGestureDetector.onTouchEvent(e);
//
//        boolean c = gestureDetector.onTouchEvent(e);
//
//        return b || c || super.onTouchEvent(e);
//    }


    /**
     * Creates and starts the camera.  Note that this uses a higher resolution in comparison
     * to other detection examples to enable the barcode detector to detect small barcodes
     * at long distances.
     *
     * Suppressing InlinedApi since there is a check that the minimum version is met before using
     * the constant.
     */
    @SuppressLint("InlinedApi")
    private void createCameraSource(boolean autoFocus, boolean useFlash) {
        Context context = mainClass.currentContext;

        // A barcode detector is created to track barcodes.  An associated multi-processor instance
        // is set to receive the barcode detection results, track the barcodes, and maintain
        // graphics for each barcode on screen.  The factory is used by the multi-processor to
        // create a separate tracker instance for each barcode.

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context).setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE).build();
        QrcodeTrackerFactory barcodeFactory = new QrcodeTrackerFactory(mGraphicOverlay);
        barcodeDetector.setProcessor(
                new MultiProcessor.Builder<>(barcodeFactory).build());

        if (!barcodeDetector.isOperational()) {
            // Note: The first time that an app using the barcode or face API is installed on a
            // device, GMS will download a native libraries to the device in order to do detection.
            // Usually this completes before the app is run for the first time.  But if that
            // download has not yet completed, then the above call will not detect any barcodes
            // and/or faces.
            //
            // isOperational() can be used to check if the required native libraries are currently
            // available.  The detectors will automatically become operational once the library
            // downloads complete on device.
            Log.w(TAG, "Detector dependencies are not yet available.");

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
//            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
//            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;
//
//            if (hasLowStorage) {
//                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
//                Log.w(TAG, getString(R.string.low_storage_error));
//            }
        }

        // Creates and starts the camera.  Note that this uses a higher resolution in comparison
        // to other detection examples to enable the barcode detector to detect small barcodes
        // at long distances.
        CameraSource.Builder builder = new CameraSource.Builder(mainClass.currentContext, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK);
//                .setRequestedPreviewSize(1600, 1024)
//                .setRequestedFps(30.0f);//.setRequestedFps(15.0f);



        // make sure that auto focus is an available option
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            builder = builder.setFocusMode(
                    autoFocus ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : null);
        }

        mCameraSource = builder
                .setFlashMode(useFlash ? Camera.Parameters.FLASH_MODE_TORCH : null)
                .build();
    }

    /**
     * Restarts the camera.
     */
    @Override
    public void onResume() {
        super.onResume();
        mainClass.addObserver(this);
        int rc = ActivityCompat.checkSelfPermission(mainClass.currentContext, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {

            createCameraSource(autoFocus, useFlash);
            gestureDetector = new GestureDetector(mainClass.currentContext, new CaptureGestureListener());
            scaleGestureDetector = new ScaleGestureDetector(mainClass.currentContext, new ScaleListener());
            startCameraSource();

        } else {
            requestCameraPermission();
        }
      //  mainClass.setOrientation(mainClass.currentContext);

    }

    /**
     * Stops the camera.
     */
    @Override
    public void onPause() {
        super.onPause();
        mainClass.deleteObserver(this);
        if (mPreview != null) {
            mPreview.stop();
        }
    }


//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
////        mainClass.nfcLoginTagEnable = false;
////      //  mainClass.loadCGetRoomNameFragment();
////        finish();
////        mainClass.notifyObserver("CloseBarCodeView");
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
                if (mPreview != null) {
            mPreview.release();
        }
    }

    /**
     * Releases the resources associated with the camera source, the associated detectors, and the
     * rest of the processing pipeline.
     */


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (mPreview != null) {
//            mPreview.release();
//        }
//    }

    @Override
    public void onStop() {
        super.onStop();
       // finish();
        mainClass.notifyObserver("CloseBarCodeView");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
//

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == RC_HANDLE_CAMERA_PERM) {

            boolean autoFocus = true;
            boolean useFlash = false;
            createCameraSource(autoFocus, useFlash);
            gestureDetector = new GestureDetector(mainClass.currentContext, new CaptureGestureListener());
            scaleGestureDetector = new ScaleGestureDetector(mainClass.currentContext, new ScaleListener());
            startCameraSource();

        } else if (requestCode != RC_HANDLE_CAMERA_PERM) {
//            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        } else {
            //mainClass.setLoginView(this);
        }
        Log.d(TAG, "Camera permission granted - initialize the camera source");


        return;

    }

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() throws SecurityException {
        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                mainClass.currentContext);
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    /**
     * onTap is called to capture the oldest barcode currently detected and
     * return it to the caller.
     *
     * @param rawX - the raw position of the tap
     * @param rawY - the raw position of the tap.
     * @return true if the activity is ending.
     */
    private boolean onTap(float rawX, float rawY) {

        //TODO: use the tap position to select the barcode.
        QrCodeGraphic graphic = mGraphicOverlay.getFirstGraphic();
        Barcode barcode = null;
        if (graphic != null) {
            barcode = graphic.getBarcode();
            if (barcode != null) {
                Intent data = new Intent();
                data.putExtra(BarcodeObject, barcode);
                getActivity().setResult(CommonStatusCodes.SUCCESS, data);
               getActivity().finish();
            }
            else {
                Log.d(TAG, "barcode data is null");
            }
        }
        else {
            Log.d(TAG,"no barcode detected");
        }
        return barcode != null;
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
        if(arg.toString().equals("QRCODENOTVALID")){
            btnQrInfo.setVisibility(View.VISIBLE);
        }
    }


    private class CaptureGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            return onTap(e.getRawX(), e.getRawY()) || super.onSingleTapConfirmed(e);
        }
    }

    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {

        /**
         * Responds to scaling events for a gesture in progress.
         * Reported by pointer motion.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         * @return Whether or not the detector should consider this event
         * as handled. If an event was not handled, the detector
         * will continue to accumulate movement until an event is
         * handled. This can be useful if an application, for example,
         * only wants to update scaling factors if the change is
         * greater than 0.01.
         */
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            return false;
        }

        /**
         * Responds to the beginning of a scaling gesture. Reported by
         * new pointers going down.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         * @return Whether or not the detector should continue recognizing
         * this gesture. For example, if a gesture is beginning
         * with a focal point outside of a region where it makes
         * sense, onScaleBegin() may return false to ignore the
         * rest of the gesture.
         */
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        /**
         * Responds to the end of a scale gesture. Reported by existing
         * pointers going up.
         * <p/>
         * Once a scale has ended, {@link ScaleGestureDetector#getFocusX()}
         * and {@link ScaleGestureDetector#getFocusY()} will return focal point
         * of the pointers remaining on the screen.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         */
        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            mCameraSource.doZoom(detector.getScaleFactor());

        }
    }
}
