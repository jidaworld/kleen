package com.example.john.kleen.Model.Util;

import android.util.Log;

import com.squareup.otto.Bus;

public class BusStation {
    private static Bus bus = new Bus();

    public static Bus getBus()
    {
        return bus;
    }
}
