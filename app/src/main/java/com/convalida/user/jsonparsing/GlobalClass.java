package com.convalida.user.jsonparsing;

import android.app.Application;

/**
 * Created by Convalida on 15-12-2017.
 */

public class GlobalClass extends Application {
    private double latitude;
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
