package ggaworowski.worktime.service;

import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import ggaworowski.worktime.receiver.WifiReceiver;

/**
 * Created by gregor on 14.04.18.
 */

public class FirebaseNetworkJobService extends JobService implements  WifiReceiver.ConnectivityReceiverListener {
    private static final String TAG = FirebaseNetworkJobService.class.getSimpleName();

    private WifiReceiver mConnectivityReceiver;

    @Override
    public void onCreate() {
        mConnectivityReceiver = new WifiReceiver(this);
        super.onCreate();
    }

    @Override
    public boolean onStartJob(JobParameters job) {
        Log.i(TAG, "onStartJob" + mConnectivityReceiver);
        registerReceiver(mConnectivityReceiver, new IntentFilter("android.net.wifi.STATE_CHANGE"));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.i(TAG, "onStopJob");
        unregisterReceiver(mConnectivityReceiver);
        return true;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        String message = isConnected ? "Good! Connected to Internet" : "Sorry! Not connected to internet";
       // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
