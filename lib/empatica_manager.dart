import 'package:flutter/services.dart';
import 'package:rxdart/rxdart.dart';
import 'bluetooth_device.dart';

class EmpaticaManager {
  static const MethodChannel _channel = const MethodChannel('empatica_porting');

  late BehaviorSubject<String> status;
  late BehaviorSubject<List<BluetoothDevice>> discoveredDevices;

  EmpaticaManager() {
    _channel.invokeMethod("createDeviceManager");
    status = BehaviorSubject<String>();
    discoveredDevices = BehaviorSubject.seeded([]);
    _setupCallbacks();
  }

  destroy() {
    status.close();
    discoveredDevices.close();
  }

  Future<void> authenticateWithAPIKey(String apiKey) async =>
      _channel.invokeMethod('authenticateWithAPIKey', {'apiKey': apiKey});

  Future<void> startScanning() async =>
      await _channel.invokeMethod('startScanning');

  Future<void> stopScanning() async =>
      await _channel.invokeMethod('stopScaning');

  Future<void> connectDevice(String serialNumber) async => await _channel
      .invokeMethod('connectDevice', {'serialNumber': serialNumber});

  void _setupCallbacks() {
    _channel.setMethodCallHandler((call) async {
      switch (call.method) {
        case 'didUpdateStatus':
          return _didUpdateStatus(call.arguments);
        case 'didDiscoverDevice':
          return _didDiscoverDevice(call.arguments);
        case 'didReceiveGSR':
          return _didReceiveGSR(call.arguments);
        case 'didReceiveBVP':
          return _didReceiveBVP(call.arguments);
        case 'didReceiveIBI':
          return _didReceiveIBI(call.arguments);
        case 'didReceiveTemperature':
          return _didReceiveTemperature(call.arguments);
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
      connectDevice(b.id);
    }
  }

  _didReceiveGSR(dynamic arguments) {
    print('didReceiveGSR');
    print(arguments);
  }

  _didReceiveBVP(dynamic arguments) {
    print('didReceiveBVP');
    print(arguments);
  }

  _didReceiveIBI(dynamic arguments) {
    print('didReceiveIBI');
    print(arguments);
  }

  _didReceiveTemperature(dynamic arguments) {
    print('didReceiveTemperature');
    print(arguments);
  }
}
