package com.guestlogix.utils;

import com.guestlogix.BuildConfig;

public class Constants {

    public final static int ONE_THOUSAND = 1000;
    public final static int SEVEN_FIFTY = 750;
    public final static int FIVE_HUNDRED = 500;
    public final static int TWO_FIFTY = 250;

    public final static int BACK_PRESSED_TIME = 2200;

    public final static int PERMISSION_STORAGE = 0X001;

    public static final int REQUEST_IMAGE = 100;

    public static final int WS_REQUEST_GET_EPISODES = 101;
    public static final int WS_REQUEST_GET_CHARACTERS = 102;

    public static final String WS_BASE_URL = BuildConfig.BASE_URL;
    public static final String WS_GET_EPISODES = WS_BASE_URL + "/episode";
    public static final String WS_GET_CHARACTERS = WS_BASE_URL + "/character/";

}
