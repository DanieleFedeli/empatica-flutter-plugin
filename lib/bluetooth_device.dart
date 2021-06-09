class BluetoothDevice {
  final String advertisingName;
  final String serialNumber;
  final String hardwareId;
  final String mAddress;
  final String deviceLabel;
  final String firmwareVersion;

  BluetoothDevice(
      {required this.advertisingName,
      required this.serialNumber,
      required this.hardwareId,
      required this.mAddress,
      required this.deviceLabel,
      required this.firmwareVersion});

  factory BluetoothDevice.fromBuffer(Map<String, dynamic> data) {
    return BluetoothDevice(
        advertisingName: data['advertisingName'],
        serialNumber: data['serialNumber'],
        hardwareId: data['hardwareId'],
        mAddress: data['mAddress'],
        deviceLabel: data['deviceLabel'],
        firmwareVersion: data['firmwareVersion']);
  }

  @override
  String toString() {
    return "advertisingName: $advertisingName, serialNumber: $serialNumber, hardwareId: $hardwareId, mAddress: $mAddress, deviceLabel: $deviceLabel, firmwareVersion: $firmwareVersion";
  }
}
