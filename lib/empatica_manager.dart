import 'package:flutter/services.dart';
import 'package:rxdart/rxdart.dart';
import 'bluetooth_device.dart';
import 'data_value.dart';

class EmpaticaManager {
  static const MethodChannel _channel = const MethodChannel('empatica_porting');

  late BehaviorSubject<String> status;
  late BehaviorSubject<List<BluetoothDevice>> discoveredDevices;

  late BehaviorSubject<DataValue> gsr;
  late BehaviorSubject<DataValue> ibi;
  late BehaviorSubject<DataValue> bvp;
  late BehaviorSubject<DataValue> temp;
  late BehaviorSubject<double> battery;
  late BehaviorSubject<int> wristStatus;

  EmpaticaManager() {
    _channel.invokeMethod("createDeviceManager");
    status = BehaviorSubject<String>();
    discoveredDevices = BehaviorSubject.seeded([]);
    gsr = BehaviorSubject();
    ibi = BehaviorSubject();
    bvp = BehaviorSubject();
    temp = BehaviorSubject();
    battery = BehaviorSubject();
    wristStatus = BehaviorSubject();
    _setupCallbacks();
  }

  destroy() {
    status.close();
    gsr.close();
    ibi.close();
    bvp.close();
    temp.close();
    battery.close();
    wristStatus.close();
    discoveredDevices.close();
  }

  Future<void> authenticateWithAPIKey(String apiKey) async =>
      _channel.invokeMethod('authenticateWithAPIKey', {'apiKey': apiKey});

  Future<void> startScanning() async =>
      await _channel.invokeMethod('startScanning');

  Future<void> stopScanning() async =>
      await _channel.invokeMethod('stopScanning');

  Future<void> connectDevice(String serialNumber) async => await _channel
      .invokeMethod('connectDevice', {'serialNumber': serialNumber});

  void _setupCallbacks() {
    _channel.setMethodCallHandler((MethodCall call) async {
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
        case 'didReceiveBatteryLevel':
          return _didReceiveBatteryLevel(call.arguments);
        case 'didUpdateSensorStatus':
          return _didUpdateSensorStatus(call.arguments);
        default:
          return MissingPluginException(call.method);
      }
    });
  }

  _didUpdateSensorStatus(dynamic arguments) {
    final s = arguments as int;
    wristStatus.sink.add(s);
  }

  _didReceiveBatteryLevel(dynamic arguments) {
    final batteryLevel = arguments as double;
    battery.sink.add(batteryLevel);
  }

  _didUpdateStatus(dynamic arguments) {
    final s = arguments as String;
    status.sink.add(s);
  }

  _didDiscoverDevice(dynamic arguments) {
    final BluetoothDevice b =
        BluetoothDevice.fromBuffer(Map<String, dynamic>.from(arguments));

    List<BluetoothDevice> devices = discoveredDevices.value;
    if (!devices.contains(b)) {
      devices.add(b);
      discoveredDevices.sink.add(devices);
      connectDevice(b.serialNumber);
    }
  }

  _didReceiveGSR(dynamic arguments) {
    final DataValue dv =
        DataValue.fromBuffer(Map<String, dynamic>.from(arguments));
    gsr.sink.add(dv);
  }

  _didReceiveBVP(dynamic arguments) {
    final DataValue dv =
        DataValue.fromBuffer(Map<String, dynamic>.from(arguments));
    bvp.sink.add(dv);
  }

  _didReceiveIBI(dynamic arguments) {
    final DataValue dv =
        DataValue.fromBuffer(Map<String, dynamic>.from(arguments));
    ibi.sink.add(dv);
  }

  _didReceiveTemperature(dynamic arguments) {
    final DataValue dv =
        DataValue.fromBuffer(Map<String, dynamic>.from(arguments));
    temp.sink.add(dv);
  }
}
