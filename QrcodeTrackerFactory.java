package com.kramer.smauthenticator.qrcodereader;


import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;
import com.kramer.smauthenticator.qrcodereader.ui.camera.GraphicOverlay;

/**
 * Created by dev on 7/11/2016.
 */
public class QrcodeTrackerFactory implements MultiProcessor.Factory<Barcode> {
    private GraphicOverlay<QrCodeGraphic> mGraphicOverlay;

    public QrcodeTrackerFactory(GraphicOverlay<QrCodeGraphic> barcodeGraphicOverlay) {
        mGraphicOverlay = barcodeGraphicOverlay;
    }

    @Override
    public Tracker<Barcode> create(Barcode barcode) {
        QrCodeGraphic graphic = new QrCodeGraphic(mGraphicOverlay);
        return new QrCodeGraphicTracker(mGraphicOverlay, graphic);
    }

}
