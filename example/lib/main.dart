import 'package:empatica_porting/empatica_manager.dart';
import 'package:flutter/material.dart';
import 'dart:async';

import 'package:permission_handler/permission_handler.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  late EmpaticaManager manager;
  @override
  void initState() {
    super.initState();
    initEmpatica();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initEmpatica() async {
    Map<Permission, PermissionStatus> statuses = await [
      Permission.location,
      Permission.bluetooth,
    ].request();

    manager = new EmpaticaManager();
    manager.authenticateWithAPIKey('16442ff6063540589d50faccd62791c0');
    manager.status.listen((value) {
      if (value == 'READY') manager.startScanning();
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Text('Running on: $_platformVersion\n'),
        ),
      ),
    );
  }
}
