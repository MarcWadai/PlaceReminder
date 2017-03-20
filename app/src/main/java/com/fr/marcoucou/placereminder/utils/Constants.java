package com.fr.marcoucou.placereminder.utils;


import android.Manifest;

/**
 * Created by sirisak on 14/06/16.
 */
public class Constants {
    public final static String TYPEFACE_NAME = "fonts/impact.ttf";
    public final static String DB_URI = "https://45a30828-8d21-42b4-aead-f34d96cb6fab-bluemix.cloudant.com/acra-placekeeper/_design/acra-storage/_update/report";
    public final static String ACRA_USER = "witherrestroundandookedi";
    public final static String ACRA_PASS = "bd9136e5689a7af305d83bbd9734bec95cb56441";
    public final static float MAP_ZOOM = 12f;
    public final static float MAP_ZOOM_LARGE = 5f;
    public final static int ACTIVITY_MAIN = 1;
    public final static int ACTIVITY_LIST = 2;
    public final static int ROUND_LEVEL = 10;
    public final static String MY_PREF_FILE = "PrefFile";
    public static final String[] requiredPermissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    public static final String[] requiredLocationPermissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
}
