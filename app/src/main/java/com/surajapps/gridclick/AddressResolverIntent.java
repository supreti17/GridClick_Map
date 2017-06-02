package com.surajapps.gridclick;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by upret on 6/1/2017.
 */

public class AddressResolverIntent extends IntentService {

    List<Address> addresses;

    protected ResultReceiver resultReceiver;

    public AddressResolverIntent() {
        super("");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AddressResolverIntent(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Location location = intent.getParcelableExtra(
                Constants.LOCATION_DATA_EXTRA);
        resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String postalCode = addresses.get(0).getPostalCode();
        String fullAddress = address + ", " + city + ", " + state + " " +  postalCode;
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, fullAddress);
        resultReceiver.send(Constants.RESULT_CODE, bundle);

    }

     final class Constants {
        static final int RESULT_CODE = 2222;
        static final String PACKAGE_NAME =
                "com.surajapps.gridclick";
        static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
        static final String RESULT_DATA_KEY = PACKAGE_NAME +
                ".RESULT_DATA_KEY";
        static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
                ".LOCATION_DATA_EXTRA";
    }
}
