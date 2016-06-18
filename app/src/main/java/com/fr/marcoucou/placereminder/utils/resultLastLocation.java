package com.fr.marcoucou.placereminder.utils;

import android.location.Location;

/**
 * Created by sirisak on 13/06/16.
 */
public interface ResultLastLocation {
     void connectedLocation(Location location);
     void disconnectedLocation();
}
