
import 'dart:async';

import 'package:flutter/services.dart';

class EmpaticaPorting {
  static const MethodChannel _channel =
      const MethodChannel('empatica_porting');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
