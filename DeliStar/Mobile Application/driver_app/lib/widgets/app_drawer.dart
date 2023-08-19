import 'package:driver_app/main.dart';
import 'package:flutter/material.dart';
import '../screens/notifiactions_screen.dart';
import '../screens/tabs_screen.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import '../constants/links.dart';

class AppDrawer extends StatefulWidget {
  @override
  State<AppDrawer> createState() => _AppDrawerState();
}

class _AppDrawerState extends State<AppDrawer> {
  var id = sharedPref?.getString('id').toString();
  var driverToken = sharedPref?.getString('token').toString();
  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: SingleChildScrollView(
        child: Column(
          children: <Widget>[
            SizedBox(height: 60),
            ListTile(
              trailing: IconButton(
                iconSize: 30,
                icon: Icon(Icons.close),
                onPressed: () {
                  Navigator.pop(context);
                },
              ),
            ),
            CircleAvatar(
              backgroundColor: Colors.amber[400],
              radius: 60,
              backgroundImage: AssetImage('images/user.png'),
            ),
            SizedBox(height: 30),
            SingleChildScrollView(
              child: Container(
                padding: EdgeInsets.only(left: 20, right: 20),
                child: Text(
                  '${sharedPref?.getString('name')}',
                  style: TextStyle(
                    fontSize: 30,
                    fontWeight: FontWeight.w500,
                  ),
                ),
              ),
            ),
            SizedBox(height: 50),
            Container(
              decoration: BoxDecoration(
                color: Colors.grey[200],
                borderRadius: BorderRadius.all(
                  Radius.circular(25),
                ),
              ),
              child: ListTile(
                title: const Text(
                  'Notifications',
                  textAlign: TextAlign.center,
                  style: TextStyle(
                    fontSize: 20,
                    fontWeight: FontWeight.w500,
                  ),
                ),
                onTap: () {
                  Navigator.of(context).pushNamed(Notifications.routeName);
                },
              ),
            ),
            SizedBox(height: 15),
            Container(
              decoration: BoxDecoration(
                color: Colors.grey[200],
                borderRadius: BorderRadius.all(
                  Radius.circular(25),
                ),
              ),
              child: ListTile(
                title: const Text(
                  'Orders List',
                  textAlign: TextAlign.center,
                  style: TextStyle(
                    fontSize: 20,
                    fontWeight: FontWeight.w500,
                  ),
                ),
                onTap: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) => TabsScreen(),
                    ),
                  );
                },
              ),
            ),
            SizedBox(height: 15),
            Container(
              decoration: BoxDecoration(
                color: Colors.grey[200],
                borderRadius: BorderRadius.all(
                  Radius.circular(25),
                ),
              ),
              child: ListTile(
                title: const Text(
                  'Log Out',
                  textAlign: TextAlign.center,
                  style: TextStyle(
                    fontSize: 20,
                    fontWeight: FontWeight.w500,
                  ),
                ),
                onTap: () {
                  showDialog(
                    context: context,
                    builder: (context) => SimpleDialog(
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(20),
                      ),
                      title: const Text(
                        'Log Out?',
                        textAlign: TextAlign.center,
                        style: TextStyle(
                          fontSize: 25,
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
                                    left: 58,
                                    right: 58,
                                    top: 10,
                                    bottom: 12,
                                  ),
                                  child: const Text(
                                    'Yes',
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
                                  } else {
                                    Navigator.of(context)
                                        .pushReplacementNamed('authScreen');
                                    print('Error occured while logging out');
                                  }
                                  sharedPref?.clear();
                                },
                              ),
                              SizedBox(height: 20),
                              ElevatedButton(
                                style: ElevatedButton.styleFrom(
                                  shape: RoundedRectangleBorder(
                                    borderRadius: BorderRadius.circular(20),
                                  ),
                                  backgroundColor: Colors.grey[200],
                                  shadowColor: Colors.transparent,
                                ),
                                child: Container(
                                  padding: EdgeInsets.only(
                                    left: 45,
                                    right: 45,
                                    top: 10,
                                    bottom: 12,
                                  ),
                                  child: const Text(
                                    'Cancel',
                                    style: TextStyle(
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
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}
