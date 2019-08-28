package com.guestlogix.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.guestlogix.listeners.IImageLoadedListener;

import java.io.InputStream;

public class LoadImageFromServerTask extends AsyncTask<String, Void, Bitmap> {

    private IImageLoadedListener imageLoadedListener;

    public LoadImageFromServerTask(IImageLoadedListener imageLoadedListener) {
        this.imageLoadedListener = imageLoadedListener;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap bmp = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            bmp = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bmp;
    }

    protected void onPostExecute(Bitmap result) {
        if (result == null) imageLoadedListener.errorResponse();
        else imageLoadedListener.successResponse(result);
    }

}