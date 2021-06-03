package com.example.empatica_porting;

import android.content.Context;

import androidx.annotation.NonNull;

import com.empatica.empalink.ConnectionNotAllowedException;
import com.empatica.empalink.EmpaDeviceManager;
import com.empatica.empalink.EmpaticaDevice;
import com.empatica.empalink.config.EmpaSensorType;
import com.empatica.empalink.config.EmpaStatus;
import com.empatica.empalink.delegate.EmpaDataDelegate;
import com.empatica.empalink.delegate.EmpaStatusDelegate;

import io.flutter.Log;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * EmpaticaPortingPlugin
 */
public class EmpaticaPortingPlugin implements FlutterPlugin, MethodCallHandler, EmpaStatusDelegate, EmpaDataDelegate {
    final private String TAG = "EmpaticaPortingPlugin";
    private MethodChannel channel;
    private Context context;
    private EmpaDeviceManager _manager;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {

        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "empatica_porting");
        channel.setMethodCallHandler(this);
        context = flutterPluginBinding.getApplicationContext();
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        switch (call.method) {
            case "createDeviceManager": {
                Log.d(TAG, "createDeviceManager...");
                _manager = new EmpaDeviceManager(context, this, this);
                result.success(null);
                break;
            }
            case "authenticateWithAPIKey": {
                Log.d(TAG, "Authentication...");
                final String apiKey = call.argument("apiKey");
                _manager.authenticateWithAPIKey(apiKey);
                result.success(null);
                break;
            }
            case "startScanning": {
                Log.d(TAG, "Start scanning");
                _manager.startScanning();
                result.success(null);
                break;
            }
            case "stopScanning": {
                Log.d(TAG, "Stop scanning");
                _manager.stopScanning();
                result.success(null);
                break;
            }
            case "connectDevice": {
                Log.d(TAG, "Connect to device");
                EmpaticaDevice device = call.argument("device");
                try {
                    _manager.connectDevice(device);
                    result.success(null);
                } catch (ConnectionNotAllowedException e) {
                    result.error("ConnectionNotAllowedException", e.getMessage(), e.getStackTrace());
                }

                break;
            }
            default: {
                Log.d(TAG, call.method);
                result.notImplemented();
            }
        }

    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
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
        channel.invokeMethod("didUpdateStatus", status.name());
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
        channel.invokeMethod("didDiscoverDevice", device);
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

