package com.mobgen.droidcon.offline.shared.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

public class NetworkMonitor extends BroadcastReceiver {

    private ConnectivityListener mConnectivityListener;

    private NetworkMonitor(@NonNull ConnectivityListener connectivityListener) {
        mConnectivityListener = connectivityListener;
    }

    public static boolean isNetworkConnected(@NonNull Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @NonNull
    public static NetworkMonitor register(@NonNull Context context, @NonNull ConnectivityListener listener) {
        NetworkMonitor monitor = new NetworkMonitor(listener);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        context.registerReceiver(monitor, filter);
        return monitor;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mConnectivityListener != null) {
            mConnectivityListener.onConnectionChanged(isNetworkConnected(context));
        }
    }

    public void unsubscribe(@NonNull Context context) {
        context.unregisterReceiver(this);
        mConnectivityListener = null;
    }

    public interface ConnectivityListener {
        void onConnectionChanged(boolean available);
    }
}
