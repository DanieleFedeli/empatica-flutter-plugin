class DataValue {
  int timestamp;
  double value;

  DataValue({required this.timestamp, required this.value});

  factory DataValue.fromBuffer(Map<String, dynamic> data) {
    return DataValue(timestamp: data['timestamp'] as int, value: data['value']);
  }
}
