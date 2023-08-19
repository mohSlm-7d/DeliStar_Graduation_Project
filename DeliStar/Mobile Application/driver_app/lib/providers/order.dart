import 'package:flutter/material.dart';

class Order with ChangeNotifier {
  int _orderNumber;
  String _receiverName;
  String _dropOffAddress;
  String _state;
  String _location;
  String _phoneNumber;

  Order(int ord_num, String rec_nam, String drop_off_add, String state,
      String locationLink, String phone)
      : _orderNumber = ord_num,
        _receiverName = rec_nam,
        _state = state,
        _dropOffAddress = drop_off_add,
        _location = locationLink,
        _phoneNumber = phone;

  void set setPhoneNumber(phoneNumber) {
    this._phoneNumber;
  }

  void set setLocationLink(locationLink) {
    this._location;
  }

  void set setOrderNumber(orderNumber) {
    this._orderNumber = orderNumber;
  }

  void set setState(state) {
    this._state = state;
  }

  void set setReciverName(reciverName) {
    this._receiverName = reciverName;
  }

  String get state {
    return _state;
  }

  String get locationLink {
    return _location;
  }

  void set setDropOffAddress(dropOffAddress) {
    this._dropOffAddress = dropOffAddress;
  }

  int get getOrderNumber {
    return _orderNumber;
  }

  String get getReceiverName {
    return _receiverName;
  }

  String get getDropOffAddress {
    return _dropOffAddress;
  }

  String get getPhoneNumber {
    return _phoneNumber;
  }
}
