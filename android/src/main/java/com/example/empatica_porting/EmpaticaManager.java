package com.example.empatica_porting;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.empatica.empalink.ConnectionNotAllowedException;
import com.empatica.empalink.EmpaDeviceManager;
import com.empatica.empalink.EmpaticaDevice;
import com.empatica.empalink.config.EmpaSensorType;
import com.empatica.empalink.config.EmpaStatus;
import com.empatica.empalink.delegate.EmpaDataDelegate;
import com.empatica.empalink.delegate.EmpaStatusDelegate;
import com.empatica.empalink.config.EmpaSensorStatus;

import java.util.HashMap;
import java.util.Map;

import io.flutter.Log;
import io.flutter.plugin.common.MethodChannel;

public class EmpaticaManager extends AppCompatActivity implements EmpaDataDelegate, EmpaStatusDelegate {
    final MethodChannel channel;
    final private EmpaDeviceManager _manager;
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

        final Map<String, Object> payload = new HashMap<>();
        payload.put("value", gsr);
        payload.put("timestamp", timestamp);

        this.runOnUiThread(() -> channel.invokeMethod("didReceiveGSR", payload));

    }

    @Override
    public void didReceiveBVP(float bvp, double timestamp) {

        final Map<String, Object> payload = new HashMap<>();
        payload.put("value", bvp);
        payload.put("timestamp", timestamp);

        this.runOnUiThread(() -> channel.invokeMethod("didReceiveBVP", payload));

    }

    @Override
    public void didReceiveIBI(float ibi, double timestamp) {

        final Map<String, Object> payload = new HashMap<>();
        payload.put("value", ibi);
        payload.put("timestamp", timestamp);

        this.runOnUiThread(() -> channel.invokeMethod("didReceiveIBI", payload));

    }

    @Override
    public void didReceiveTemperature(float t, double timestamp) {

        final Map<String, Object> payload = new HashMap<>();
        payload.put("value", t);
        payload.put("timestamp", timestamp);

        this.runOnUiThread(() -> channel.invokeMethod("didReceiveTemperature", payload));

    }

    @Override
    public void didReceiveAcceleration(int x, int y, int z, double timestamp) {

    }

    @Override
    public void didReceiveBatteryLevel(float level, double timestamp) {
        Log.d(TAG, "Battery level" + level);
        this.runOnUiThread(() -> channel.invokeMethod("didReceiveBatteryLevel", level));
    }

    @Override
    public void didReceiveTag(double timestamp) {

    }

    @Override
    public void didUpdateStatus(EmpaStatus status) {
        final EmpaStatus _status = status;
        Log.d(TAG, "Status: " + _status.name());

        this.runOnUiThread(() -> channel.invokeMethod("didUpdateStatus", _status.name()));
    }

    @Override
    public void didEstablishConnection() {

    }

    @Override
    public void didUpdateSensorStatus(@EmpaSensorStatus int status, EmpaSensorType type) {
        this.runOnUiThread(() -> channel.invokeMethod("didUpdateSensorStatus", status));
    }

    @Override
    public void didDiscoverDevice(EmpaticaDevice device, String deviceLabel, int rssi, boolean allowed) {
        if (!allowed)
            return;

        final Map<String, Object> payload = new HashMap<>();
        payload.put("deviceLabel", deviceLabel);
        payload.put("advertisingName", device.advertisingName);
        payload.put("mAddress", device.device.getAddress());
        payload.put("firmwareVersion", device.firmwareVersion);
        payload.put("serialNumber", device.serialNumber);
        payload.put("hardwareId", device.hardwareId);

        final String[] serialNumber = deviceLabel.split(" - ");
        discoveredDevices.put(serialNumber[1], device);

        channel.invokeMethod("didDiscoverDevice", payload);
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
    public void didUpdateOnWristStatus(@EmpaSensorStatus int status) {
        this.runOnUiThread(() -> channel.invokeMethod("didUpdateSensorStatus", status));
    }

}
