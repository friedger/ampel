package com.example.android.bluetoothlegatt.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.android.bluetoothlegatt.GoActivity;
import com.example.android.bluetoothlegatt.R;
import com.example.android.bluetoothlegatt.TrafficLightActivity;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;

import java.nio.charset.Charset;

/**
 * Created by Maxime on 6/06/15.
 */
public class DeviceScanService extends IntentService {

    private final static String TAG = DeviceScanService.class.getSimpleName();
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000000;


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DeviceScanService(String name) {
        super(name);
    }

    public DeviceScanService(){
        super("DeviceScanService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Service starting");


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        mHandler = new Handler();
        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
        }

        Notification notification = new Notification(R.drawable.ic_launcher, "SCanning", System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, GoActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(this, "Scanning", "Scanning", pendingIntent);
        startForeground(1000, notification);
        scanLeDevice(true);
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    private void setGoAuth(Beacon beacon){
        if (beacon != null){
            //Start Activity here
            Intent intent = new Intent(this,TrafficLightActivity.class);
            intent.putExtra("go_signal",beacon.getDataFields().get(0));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                BeaconParser beaconParser = new BeaconParser().setBeaconLayout("m:2-3=d8fe,i:2-3,p:5-5,d:6-6");
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
                            if (device != null && device.toString().startsWith("C9:E3:E3")) {
                                try {
                                    Beacon beacon = beaconParser.fromScanData(scanRecord, rssi, device);
                                    Log.d(TAG, "my device" + device.toString());
                                    Log.d(TAG, beacon.toString());
                                    Log.d(TAG, new String(scanRecord, Charset.forName("ascii")));
                                    setGoAuth(beacon);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
            };
}

