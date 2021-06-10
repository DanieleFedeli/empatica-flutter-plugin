class DataValue {
  double timestamp;
  double value;

  DataValue({required this.timestamp, required this.value});

  factory DataValue.fromBuffer(Map<String, dynamic> data) {
    return DataValue(timestamp: data['timestamp'], value: data['value']);
  }

  toList() {
    return [timestamp, value];
  }
}
