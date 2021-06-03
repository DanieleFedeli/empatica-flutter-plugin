package com.example.empatica_porting;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.empatica.empalink.ConnectionNotAllowedException;
import com.empatica.empalink.EmpaDeviceManager;
import com.empatica.empalink.EmpaticaDevice;
import com.empatica.empalink.config.EmpaSensorType;
import com.empatica.empalink.config.EmpaStatus;
import com.empatica.empalink.delegate.EmpaDataDelegate;
import com.empatica.empalink.delegate.EmpaStatusDelegate;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import io.flutter.Log;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class EmpaticaManager extends AppCompatActivity implements EmpaDataDelegate, EmpaStatusDelegate {
    final MethodChannel channel;
    final private EmpaDeviceManager _manager;
    final private Handler handler = new Handler(Looper.getMainLooper());

    final private String TAG = "EmpaticaPortingPlugin";
    Map<String, EmpaticaDevice> discoveredDevices = new HashMap<>();

    public EmpaticaManager(MethodChannel channel, Context context) {
        this.channel = channel;

        this._manager = new EmpaDeviceManager(context, this, this);
    }


    public void authenticateWithAPIKey(String apiKey) {
        this._manager.authenticateWithAPIKey(apiKey);
    }

    public void startScanning() {
        this._manager.prepareScanning();
        this._manager.startScanning();
        this.discoveredDevices = new HashMap<>();
    }

    public void stopScanning() {
        this._manager.stopScanning();
        this.discoveredDevices = new HashMap<>();
    }

    public void connectDevice(String serialNumber) throws ConnectionNotAllowedException {
        this._manager.stopScanning();
        final EmpaticaDevice device = discoveredDevices.get(serialNumber);

        if (device != null) {

            this._manager.connectDevice(device);
        }
    }

    @Override
    public void didReceiveGSR(float gsr, double timestamp) {

    }

    @Override
    public void didReceiveBVP(float bvp, double timestamp) {

    }

    @Override
    public void didReceiveIBI(float ibi, double timestamp) {

    }

    @Override
    public void didReceiveTemperature(float t, double timestamp) {

    }

    @Override
    public void didReceiveAcceleration(int x, int y, int z, double timestamp) {

    }

    @Override
    public void didReceiveBatteryLevel(float level, double timestamp) {

    }

    @Override
    public void didReceiveTag(double timestamp) {

    }

    @Override
    public void didUpdateStatus(EmpaStatus status) {
        Log.d(TAG, "Status: " + status.name());

        this.runOnUiThread(() ->
                channel.invokeMethod("didUpdateStatus", status.name()));
    }

    @Override
    public void didEstablishConnection() {

    }

    @Override
    public void didUpdateSensorStatus(int status, EmpaSensorType type) {

    }

    @Override
    public void didDiscoverDevice(EmpaticaDevice device, String deviceLabel, int rssi, boolean allowed) {
        Log.d(TAG, "Discovered device: " + deviceLabel);
        if (!allowed) return;
        final String[] serialNumber = deviceLabel.split(" - ");
        discoveredDevices.put(serialNumber[1], device);
        channel.invokeMethod("didDiscoverDevice", deviceLabel);
    }

    @Override
    public void didFailedScanning(int errorCode) {

    }

    @Override
    public void didRequestEnableBluetooth() {

    }

    @Override
    public void bluetoothStateChanged() {

    }

    @Override
    public void didUpdateOnWristStatus(int status) {

    }


}
