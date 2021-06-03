import 'package:flutter/services.dart';
import 'package:rxdart/rxdart.dart';
import 'bluetooth_device.dart';

class EmpaticaManager {
  late BehaviorSubject<String> status;
  late BehaviorSubject<List<BluetoothDevice>> discoveredDevices;
  static const MethodChannel _channel = const MethodChannel('empatica_porting');

  EmpaticaManager() {
    status = BehaviorSubject<String>();
    discoveredDevices = BehaviorSubject.seeded([]);
    _channel.invokeMethod("createDeviceManager");
    _setupCallbacks();
  }

  destroy() {
    status.close();
    discoveredDevices.close();
  }

  void authenticateWithAPIKey(String apiKey) =>
      _channel.invokeMethod('authenticateWithAPIKey', {'apiKey': apiKey});

  void startScanning() => _channel.invokeMethod('startScanning');
  void stopScanning() => _channel.invokeMethod('stopScaning');

  void connectDevice(BluetoothDevice device) =>
      _channel.invokeMethod('connectDevice', {'device': device});

  void _setupCallbacks() {
    _channel.setMethodCallHandler((call) async {
      switch (call.method) {
        case 'didUpdateStatus':
          return _didUpdateStatus(call.arguments);
        case 'didDiscoverDevice':
          return _didDiscoverDevice(call.arguments);
        default:
          return false;
      }
    });
  }

  _didUpdateStatus(dynamic arguments) {
    final s = arguments as String;
    status.sink.add(s);
  }

  _didDiscoverDevice(dynamic arguments) {
    final BluetoothDevice b = BluetoothDevice.fromBuffer(arguments);
    List<BluetoothDevice> devices = discoveredDevices.value;
    if (!devices.contains(b)) {
      devices.add(b);
      discoveredDevices.sink.add(devices);
    }
  }
}
