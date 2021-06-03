package com.example.empatica_porting;

import android.content.Context;
import androidx.annotation.NonNull;

import com.empatica.empalink.ConnectionNotAllowedException;
import com.empatica.empalink.EmpaticaDevice;

import io.flutter.Log;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * EmpaticaPortingPlugin
 */
public class EmpaticaPortingPlugin implements FlutterPlugin, MethodCallHandler {
    final private String TAG = "EmpaticaPortingPlugin";
    private MethodChannel channel;
    private Context context;
    private EmpaticaManager _manager;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "empatica_porting");
        channel.setMethodCallHandler(this);
        context = flutterPluginBinding.getApplicationContext();
    }

    @Override
    public void onDetachedFromEngine(@NonNull @org.jetbrains.annotations.NotNull FlutterPlugin.FlutterPluginBinding binding) {

    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        switch (call.method) {
            case "createDeviceManager": {
                Log.d(TAG, "createDeviceManager...");
                _manager = new EmpaticaManager(channel, context);
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
                String serial = call.argument("serialNumber");
                try {
                    _manager.connectDevice(serial);
                    result.success(null);
                } catch ( ConnectionNotAllowedException e) {
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

}

