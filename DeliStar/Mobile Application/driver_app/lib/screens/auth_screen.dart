import '../auth/auth_form.dart';
import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';

class AuthScreen extends StatefulWidget {
  @override
  _AuthScreenState createState() => _AuthScreenState();
}

class _AuthScreenState extends State<AuthScreen> {
  Future getPosition() async {
    bool services;
    services = await Geolocator.isLocationServiceEnabled();
    print(services);
    if (services == false) {
      return showDialog<Null>(
        barrierDismissible: false,
        context: context,
        builder: (ctx) => SimpleDialog(
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(20),
          ),
          title: const Text(
            'Device location is turned off please enable device location',
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
                        borderRadius: BorderRadius.circular(20.0),
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
                      getPosition();
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
    }
  }

  Future getPermisson() async {
    LocationPermission per;
    per = await Geolocator.checkPermission();

    // التطبيق و امكانية وصوله لل موقع
    if (per ==
        LocationPermission.denied) // App can not access location. // enum
    {
      per = await Geolocator.requestPermission();
    }
    if (per == LocationPermission.whileInUse) {
      getPosition();
    }
    print(per);
  }

  @override
  void initState() {
    getPermisson();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    var height = MediaQuery.of(context).size.height;
    return Stack(
      children: [
        Container(
          color: Colors.yellow[600],
        ),
        Padding(
          padding: EdgeInsets.only(top: 40.0),
          child: Container(
            height: height * 100,
            decoration: BoxDecoration(
              borderRadius: BorderRadius.only(
                topLeft: Radius.circular(35),
                topRight: Radius.circular(35),
              ),
              color: Colors.grey[200],
            ),
          ),
        ),
        SingleChildScrollView(
          child: AuthForm(),
        ),
      ],
    );
  }
}
