class BluetoothDevice {
  final String id;
  final String name;

  BluetoothDevice({required this.id, required this.name});

  factory BluetoothDevice.fromBuffer(String label) {
    final List<String> labelSplitted = label.split(' - ');
    return BluetoothDevice(id: labelSplitted[0], name: labelSplitted[1]);
  }
}
