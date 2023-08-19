import 'package:flutter/material.dart';

class Notifcation with ChangeNotifier {
  String _content;

  Notifcation(String cont) : _content = cont;

  String get getContent {
    return _content;
  }

  notifyListeners();
}
