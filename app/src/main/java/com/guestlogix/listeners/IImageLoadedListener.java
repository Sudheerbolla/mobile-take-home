package com.guestlogix.listeners;

import android.graphics.Bitmap;

public interface IImageLoadedListener {

    void successResponse(Bitmap bitmap);

    void errorResponse();

}
