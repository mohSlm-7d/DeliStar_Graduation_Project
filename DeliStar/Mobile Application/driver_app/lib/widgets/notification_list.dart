import 'package:driver_app/providers/orders.dart';
import 'package:flutter/material.dart';
import 'package:flutter_animate/flutter_animate.dart';
import 'package:lottie/lottie.dart';
import 'package:provider/provider.dart';
import '../constants/links.dart';
import '../main.dart';
import '../widgets/notification_item.dart';

import 'dart:convert';
import 'package:http/http.dart' as http;

class NotificationList extends StatefulWidget {
  @override
  State<NotificationList> createState() => _NotificationListState();
}

class _NotificationListState extends State<NotificationList> {
  var _isInit = true;
  var _isLoading = false;

  void didChangeDependencies() {
    if (_isInit) {
      setState(() {
        _isLoading = true;
      });
      Provider.of<Orders>(context).fetchNotifications().then((_) {
        setState(() {
          _isLoading = false;
        });
      }).catchError((error) {
        return showDialog<Null>(
          barrierDismissible: false,
          context: context,
          builder: (ctx) => SimpleDialog(
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(20),
            ),
            title: const Text(
              'An Error Occured While Loading The Notifications',
              textAlign: TextAlign.center,
              style: TextStyle(
                fontSize: 20,
                fontWeight: FontWeight.normal,
              ),
            ),
            children: <Widget>[
              SizedBox(
                height: 20,
              ),
              Container(
                child: Column(
                  children: <Widget>[
                    ElevatedButton(
                      style: ElevatedButton.styleFrom(
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(20),
                        ),
                        backgroundColor: Colors.green,
                        shadowColor: Colors.transparent,
                      ),
                      child: Container(
                        padding: EdgeInsets.only(
                          left: 40,
                          right: 40,
                          top: 10,
                          bottom: 12,
                        ),
                        child: const Text(
                          'Okay',
                          style: TextStyle(
                            color: Colors.white,
                            fontSize: 20,
                            fontWeight: FontWeight.normal,
                          ),
                        ),
                      ),
                      onPressed: () {
                        Navigator.of(context).pop();
                      },
                    ),
                    SizedBox(
                      height: 20,
                    ),
                  ],
                ),
              ),
            ],
          ),
        );
      }).then((_) {
        setState(() {
          _isLoading = false;
        });
      });
    }

    _isInit = false;
    super.didChangeDependencies();
  }

  @override
  Widget build(BuildContext context) {
    final notificationsData = Provider.of<Orders>(context);

    var notification = notificationsData.getListNotifications;

    var content = notificationsData.getContent;

    var id = sharedPref?.getString('id').toString();

    var driverToken = sharedPref?.getString('token').toString();

    return _isLoading
        ? Center(
            child: CircularProgressIndicator(),
          )
        : notificationsData.navigateTo == 1
            ? SimpleDialog(
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(20),
                ),
                title: const Text(
                  'An error occured',
                  textAlign: TextAlign.center,
                  style: TextStyle(
                    fontSize: 25,
                    fontWeight: FontWeight.normal,
                  ),
                ),
                children: <Widget>[
                  const SizedBox(
                    height: 20,
                  ),
                  Container(
                    child: Column(
                      children: <Widget>[
                        ElevatedButton(
                          style: ElevatedButton.styleFrom(
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(20),
                            ),
                            backgroundColor: Colors.red,
                            shadowColor: Colors.transparent,
                          ),
                          child: Container(
                            padding: const EdgeInsets.only(
                              left: 58,
                              right: 58,
                              top: 10,
                              bottom: 12,
                            ),
                            child: const Text(
                              'Ok',
                              style: TextStyle(
                                color: Colors.white,
                                fontSize: 20,
                                fontWeight: FontWeight.normal,
                              ),
                            ),
                          ),
                          onPressed: () async {
                            var url = Uri.parse(logoutLink);

                            var response = await http.post(
                              url,
                              body: jsonEncode(
                                <String, String>{
                                  'driverId': id.toString(),
                                  'driverToken': driverToken.toString()
                                },
                              ),
                            );
                            var responseBody = jsonDecode(response.body);

                            if (responseBody['status'] == 'Success') {
                              print(responseBody);
                              print('Log out complete');
                              Navigator.of(context)
                                  .pushReplacementNamed('authScreen');
                              sharedPref?.clear();
                            } else {
                              print('Error occured while logging out');
                            }
                            Navigator.of(context)
                                .pushReplacementNamed('authScreen');
                          },
                        ),
                        const SizedBox(height: 20),
                      ],
                    ),
                  ),
                ],
              )
            : notification.isNotEmpty
                ? ListView.builder(
                    itemCount: notification.length,
                    itemBuilder: (ctx, index) {
                      return ChangeNotifierProvider.value(
                        value: notification[index],
                        child: Padding(
                          padding: EdgeInsets.only(
                            top: 20,
                          ),
                          child: NotificationItem(content[index]),
                        ),
                      );
                    }).animate().fadeIn()
                : Column(
                    children: <Widget>[
                      SizedBox(
                        height: 100,
                      ),
                      Container(
                        width: 250,
                        height: 250,
                        child: Lottie.asset('images/notifications.json'),
                      ),
                      SizedBox(
                        height: 101.1,
                      ),
                      Center(
                        child: Text(
                          'No Notifications',
                          style: TextStyle(
                            fontSize: 30,
                            fontWeight: FontWeight.bold,
                            color: Colors.grey,
                          ),
                        ),
                      ),
                    ],
                  );
  }
}
