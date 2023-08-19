import 'package:driver_app/main.dart';
import 'package:flutter/material.dart';
import './order.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import '../constants/links.dart';
import 'notification.dart';
import 'package:geolocator/geolocator.dart';

class Orders with ChangeNotifier {
  List<Order> _orders = [];

  List<Order> _remOrders = [];

  List<Order> _deliveredOrdersList = [];

  List<Order> _canceledOrdersList = [];

  List<String> _locations = [];

  List<Notifcation> _notifications = [];

  List<String> _content = [];

  var responseNotiCancel = "";

  var responseNotiCorrect = "";

  late Position cl;

  double latitude = 0;

  double longitude = 0;

  int navigate = 0;

  Future<Position> getLatAndLong() async {
    return await Geolocator.getCurrentPosition()
        .then((value) => value); //return value
  }

  Future getPosition() async {
    LocationPermission per;
    per = await Geolocator.checkPermission();
    bool services;
    services = await Geolocator.isLocationServiceEnabled();
    print(services);

    if (per == LocationPermission.whileInUse) {
      cl = await getLatAndLong();

      latitude = cl.latitude;
      longitude = cl.longitude;
      print(latitude);
      print(longitude);
    }
  }

  Future<void> fetchAndSetOrders() async {
    final url = Uri.parse(fetchOrdersLink);
    getPosition();
    var id = sharedPref?.getString('id').toString();
    var driverToken = sharedPref?.getString('token').toString();

    var lati = sharedPref?.getDouble('lati');
    var longi = sharedPref?.getDouble('longi');

    try {
      final response = await http.post(
        url,
        body: jsonEncode(
          <String, String>{
            "driverId": id.toString(),
            "driverToken": driverToken.toString(),
            "driverLat": lati.toString(),
            "driverLng": longi.toString(),
            "orders_State": "pending",
          },
        ),
      );

      Map<String, dynamic> responseBody = json.decode(response.body);
      if (responseBody['status'] == 'Success') {
        print('Pending orders loaded');

        print(responseBody);

        sharedPref?.setString(
          "token",
          responseBody['driverToken'].toString(),
        );

        final List<Order> loadedOrders = [];
        for (var i = 0; i < responseBody['listOfOrders']!.length; i++) {
          loadedOrders.add(
            Order(
              responseBody['listOfOrders']![i]['orderNumber'],
              responseBody['listOfOrders']![i]['customerName'].toString(),
              responseBody['listOfOrders']![i]['orderDropOffAddress']
                  .toString(),
              responseBody['listOfOrders']![i]['orderState'].toString(),
              responseBody['listOfOrders']![i]['orderLocationLink'].toString(),
              responseBody['listOfOrders']![i]['customerPhoneNo'].toString(),
            ),
          );
        }
        _orders = [...loadedOrders];
        notifyListeners();

        final List<Order> loadRemOrders = [];
        for (var i = 1; i < responseBody['listOfOrders']!.length; i++) {
          loadRemOrders.add(
            Order(
              responseBody['listOfOrders']![i]['orderNumber'],
              responseBody['listOfOrders']![i]['customerName'].toString(),
              responseBody['listOfOrders']![i]['orderDropOffAddress']
                  .toString(),
              responseBody['listOfOrders']![i]['orderState'].toString(),
              responseBody['listOfOrders']![i]['orderLocationLink'].toString(),
              responseBody['listOfOrders']![i]['customerPhoneNo'].toString(),
            ),
          );
        }
        _remOrders = loadRemOrders;

        notifyListeners();
      } else if (responseBody['status'] == 'Driver_Banned') {
        print('Driver banned');
        sharedPref?.setString(
          "token",
          responseBody['driverToken'].toString(),
        );
      } else if (responseBody['driverToken'] != null) {
        print('Token error');

        sharedPref?.setString(
          "token",
          responseBody['driverToken'].toString(),
        );
      } else {
        navigate = 1;
        _remOrders.clear();
        _orders.clear();
        sharedPref?.clear();
      }
    } catch (error) {
      throw error;
    }
  }

  Future<void> fetchAndSetDeliveredOrders() async {
    var url = Uri.parse(fetchOrdersLink);
    var driverId = sharedPref?.getString('id').toString();
    var driverToken = sharedPref?.getString('token').toString();
    try {
      var response = await http.post(
        url,
        body: jsonEncode(
          <String, String>{
            "driverId": driverId.toString(),
            "driverToken": driverToken.toString(),
            "orders_State": "confirmed",
          },
        ),
      );
      Map<String, dynamic> responseBody = json.decode(response.body);
      if (responseBody['status'] == 'Success') {
        print('Got the delivered orders');
        print(responseBody['listOfOrders']);
        List check = responseBody['listOfOrders'];
        sharedPref?.setString(
          "token",
          responseBody['driverToken'].toString(),
        );

        if (check.isNotEmpty) {
          final List<Order> deliOrders = [];
          for (var i = 0; i < responseBody['listOfOrders']!.length; i++) {
            deliOrders.add(
              Order(
                responseBody['listOfOrders']![i]['orderNumber'],
                responseBody['listOfOrders']![i]['customerName'],
                responseBody['listOfOrders']![i]['orderDropOffAddress'],
                "Delivered",
                responseBody['listOfOrders']![i]['orderLocationLink'],
                responseBody['listOfOrders']![i]['customerPhoneNo'],
              ),
            );
          }
          _deliveredOrdersList = deliOrders;
        }
      } else if (responseBody['driverToken'] != null) {
        print('Token error');

        sharedPref?.setString(
          "token",
          responseBody['driverToken'].toString(),
        );

        navigate = 1;
        _remOrders.clear();
        _orders.clear();
      } else {
        navigate = 1;
        _deliveredOrdersList.clear();
        sharedPref?.clear();
      }
    } catch (e) {
      print(e);
    }
  }

  fetchAndSetCanceledOrders() async {
    var url = Uri.parse(fetchOrdersLink);
    var driverId = sharedPref?.getString('id').toString();
    var driverToken = sharedPref?.getString('token').toString();
    var response = await http.post(
      url,
      body: jsonEncode(
        <String, String>{
          "driverId": driverId.toString(),
          "driverToken": driverToken.toString(),
          "orders_State": "cancelled",
        },
      ),
    );
    Map<String, dynamic> responseBody = json.decode(response.body);
    if (responseBody['status'] == 'Success') {
      print('Got the canceled orders');
      print(responseBody['listOfOrders']);
      sharedPref?.setString(
        "token",
        responseBody['driverToken'].toString(),
      );
      List checkCanl = responseBody['listOfOrders'];
      if (checkCanl.isNotEmpty) {
        final List<Order> canceledOrders = [];
        for (var i = 0; i < responseBody['listOfOrders']!.length; i++) {
          canceledOrders.add(
            Order(
              responseBody['listOfOrders']![i]['orderNumber'],
              responseBody['listOfOrders']![i]['customerName'],
              responseBody['listOfOrders']![i]['orderDropOffAddress'],
              "Cancelled",
              responseBody['listOfOrders']![i]['orderLocationLink'],
              responseBody['listOfOrders']![i]['customerPhoneNo'],
            ),
          );
        }
        _canceledOrdersList = canceledOrders;
      }
    } else if (responseBody['driverToken'] != null) {
      print('Token error');

      sharedPref?.setString(
        "token",
        responseBody['driverToken'].toString(),
      );
      _remOrders.clear();
      _orders.clear();
    } else {
      navigate = 1;
      _deliveredOrdersList.clear();
      _canceledOrdersList.clear();
      sharedPref?.clear();
    }
  }

  addDeliveredOrder(Order order, int orderNumber) async {
    final url = Uri.parse(confirmOrderLink);

    var id = sharedPref?.getString('id').toString();
    var driverToken = sharedPref?.getString('token').toString();

    getLatAndLong();
    getPosition();

    try {
      var response = await http.post(
        url,
        body: jsonEncode(
          <String, String>{
            "driverId": id.toString(),
            "driverToken": driverToken.toString(),
            "driverLat": latitude.toString(),
            "driverLng": longitude.toString(),
            "orderNumber": orderNumber.toString(),
          },
        ),
      );
      Map<String, dynamic> responseBody = json.decode(response.body);

      if (responseBody['status'] == 'Success') {
        sharedPref?.setString(
          "token",
          responseBody['driverToken'].toString(),
        );

        _orders.clear();
        _remOrders.clear();

        if (responseBody['listOfOrders'] == null) {
        } else {
          final List<Order> loadedOrders2 = [];

          for (var i = 0; i < responseBody['listOfOrders']!.length; i++) {
            loadedOrders2.add(
              Order(
                responseBody['listOfOrders']![i]['orderNumber'],
                responseBody['listOfOrders']![i]['customerName'].toString(),
                responseBody['listOfOrders']![i]['orderDropOffAddress']
                    .toString(),
                responseBody['listOfOrders']![i]['orderState'].toString(),
                responseBody['listOfOrders']![i]['orderLocationLink']
                    .toString(),
                responseBody['listOfOrders']![i]['customerPhoneNo'].toString(),
              ),
            );
          }
          _orders = loadedOrders2;
          notifyListeners();
          final List<Order> loadRemOrders2 = [];
          for (var i = 1; i < responseBody['listOfOrders']!.length; i++) {
            loadRemOrders2.add(
              Order(
                responseBody['listOfOrders']![i]['orderNumber'],
                responseBody['listOfOrders']![i]['customerName'].toString(),
                responseBody['listOfOrders']![i]['orderDropOffAddress']
                    .toString(),
                responseBody['listOfOrders']![i]['orderState'].toString(),
                responseBody['listOfOrders']![i]['orderLocationLink']
                    .toString(),
                responseBody['listOfOrders']![i]['customerPhoneNo'].toString(),
              ),
            );
          }
          _remOrders = loadRemOrders2;

          notifyListeners();

          print(responseBody);
        }
      } else if (responseBody['driverToken'] != null) {
        print('Token error');

        sharedPref?.setString(
          "token",
          responseBody['driverToken'].toString(),
        );
        _remOrders.clear();
        _orders.clear();
      } else {
        print("Failed to add delivered order");
        print(responseBody['status']);
        navigate = 1;
        sharedPref?.clear();
      }
    } catch (error) {
      print(error);
    }
    notifyListeners();
  }

  addCanceledOrder(Order order, int orderNumber, String reportedMessage) async {
    final url = Uri.parse(cancelOrderLink);

    var id = sharedPref?.getString('id').toString();
    var driverToken = sharedPref?.getString('token').toString();

    getLatAndLong();
    getPosition();

    try {
      var response = await http.post(
        url,
        body: jsonEncode(
          <String, String>{
            "driverId": id.toString(),
            "driverToken": driverToken.toString(),
            "driverLat": latitude.toString(),
            "driverLng": longitude.toString(),
            "orderNumber": orderNumber.toString(),
            "orderReport": reportedMessage.toString(),
          },
        ),
      );
      Map<String, dynamic> responseBody = json.decode(response.body);

      if (responseBody['status'] == 'Success') {
        sharedPref?.setString(
          "token",
          responseBody['driverToken'].toString(),
        );

        _orders.clear();
        _remOrders.clear();

        if (responseBody['listOfOrders'] == null) {
        } else {
          final List<Order> loadedOrders3 = [];
          for (var i = 0; i < responseBody['listOfOrders']!.length; i++) {
            loadedOrders3.add(
              Order(
                responseBody['listOfOrders']![i]['orderNumber'],
                responseBody['listOfOrders']![i]['customerName'].toString(),
                responseBody['listOfOrders']![i]['orderDropOffAddress']
                    .toString(),
                responseBody['listOfOrders']![i]['orderState'].toString(),
                responseBody['listOfOrders']![i]['orderLocationLink']
                    .toString(),
                responseBody['listOfOrders']![i]['customerPhoneNo'].toString(),
              ),
            );
          }
          _orders = loadedOrders3;
          notifyListeners();
          final List<Order> loadRemOrders3 = [];
          for (var i = 1; i < responseBody['listOfOrders']!.length; i++) {
            loadRemOrders3.add(
              Order(
                responseBody['listOfOrders']![i]['orderNumber'],
                responseBody['listOfOrders']![i]['customerName'].toString(),
                responseBody['listOfOrders']![i]['orderDropOffAddress']
                    .toString(),
                responseBody['listOfOrders']![i]['orderState'].toString(),
                responseBody['listOfOrders']![i]['orderLocationLink']
                    .toString(),
                responseBody['listOfOrders']![i]['customerPhoneNo'].toString(),
              ),
            );
          }
          _remOrders = loadRemOrders3;

          notifyListeners();

          print(responseBody);
        }
      } else if (responseBody['driverToken'] != null) {
        print('Token error');

        sharedPref?.setString(
          "token",
          responseBody['driverToken'].toString(),
        );
        _remOrders.clear();
        _orders.clear();
      } else {
        print("Failed to add canceled order");
        print(responseBody['status']);
        navigate = 1;
        sharedPref?.clear();
      }
    } catch (error) {
      print(error);
    }
    notifyListeners();
  }

  Future<void> fetchNotifications() async {
    var url = Uri.parse(notiFetch);
    var id = sharedPref?.getString('id').toString();
    var driverToken = sharedPref?.getString('token').toString();
    try {
      final response = await http.post(
        url,
        body: jsonEncode(
          <String, String>{
            "driverId": id.toString(),
            "driverToken": driverToken.toString()
          },
        ),
      );
      Map<String, dynamic> responseBody = jsonDecode(response.body);
      if (responseBody['status'] == 'Success') {
        responseNotiCorrect = "correct";
        sharedPref?.setString(
          "token",
          responseBody['driverToken'].toString(),
        );

        final List<Notifcation> loadedNotifications = [];
        final List<String> loadedContent = [];
        for (var i = 0; i < responseBody['notifications']!.length; i++) {
          loadedNotifications.add(
            Notifcation(
              responseBody['notifications']![i].toString(),
            ),
          );

          loadedContent.add(
            responseBody['notifications']![i].toString(),
          );
        }
        _content = loadedContent;

        _notifications = loadedNotifications;
        notifyListeners();
      } else if (responseBody['driverToken'] != null) {
        print('Token error');

        sharedPref?.setString(
          "token",
          responseBody['driverToken'].toString(),
        );
        _remOrders.clear();
        _orders.clear();
      } else {
        navigate = 1;
        print("Failed to fetch notifications");
        print(responseBody['status']);
        responseNotiCorrect = "";
        responseNotiCancel = 'empty';
        _content.clear();
        _notifications.clear();
      }
    } catch (error) {
      print(error);
    }
  }

  String get getResponseCorrect {
    return responseNotiCorrect;
  }

  int get navigateTo {
    return navigate;
  }

  String get getResponseCancel {
    return responseNotiCancel;
  }

  List<Order> get getRemOrders {
    return [..._remOrders];
  }

  List<String> get getContent {
    return [..._content];
  }

  List<Notifcation> get getListNotifications {
    return [..._notifications];
  }

  List<String> get locationLinks {
    return [..._locations];
  }

  List<Order> get getDeliveredOrdersList {
    return [..._deliveredOrdersList.toList()];
  }

  List<Order> get getRemainingOrders {
    return [...getRemainingOrders];
  }

  List<Order> get getOrder {
    return [..._orders];
  }

  List<Order> get getCanceledOrdersList {
    return [..._canceledOrdersList.toList()];
  }

  void removeOrder(orderNumber) {
    _orders.removeWhere((element) => element.getOrderNumber == orderNumber);
    _remOrders
        .removeWhere((element) => element.getOrderNumber == orderNumber + 1);
  }
}
