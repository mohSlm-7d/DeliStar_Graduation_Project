import 'package:flutter/material.dart';
import 'package:flutter_animate/flutter_animate.dart';
import 'package:bordered_text/bordered_text.dart';
import 'package:geolocator/geolocator.dart';
import 'dart:convert';
import '../main.dart';
import 'package:http/http.dart' as http;
import '../constants/links.dart';

class AuthForm extends StatefulWidget {
  @override
  _AuthFormState createState() => _AuthFormState();
}

class _AuthFormState extends State<AuthForm> {
  final _passwordFocusNode = FocusNode();

  final _form = GlobalKey<FormState>();

  Map<String, String> _authData = {
    'email': '',
    'password': '',
  };

  bool _isLoading = false;

  bool _hidePassword = true;

  late Position cl;

  double latitude = 0;

  double longitude = 0;

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
      sharedPref?.setDouble("lati", latitude);
      sharedPref?.setDouble("longi", longitude);
    }
  }

  _saveForm() async {
    final isValid = _form.currentState!.validate();

    if (isValid) {
      _form.currentState?.save();

      var url = Uri.parse(loginLink);

      setState(() {
        _isLoading = true;
      });
      var email = _authData['email'];
      var password = _authData['password'];
      try {
        var response = await http.post(
          url,
          body: jsonEncode(
            <String, String>{
              'driverEmail': email.toString(),
              'driverPassword': password.toString(),
            },
          ),
        );

        Map<String, dynamic> responseBody = jsonDecode(response.body);
        print(responseBody);

        if (responseBody['status'] == 'Success') {
          sharedPref?.setString(
              "token", responseBody['driver']['driverToken'].toString());

          sharedPref?.setString(
              "id", responseBody['driver']['driverId'].toString());

          sharedPref?.setString(
              "name", responseBody['driver']['driverName'].toString());

          Navigator.of(context).pushReplacementNamed('loadInfo');
        }
        if (responseBody['status'] == 'Incorrect_Info') {
          showDialog(
            context: context,
            builder: (context) => SimpleDialog(
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(20),
              ),
              title: const Text(
                'Username or password does not exist or user does not exist',
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
                          backgroundColor: Colors.green,
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
                        onPressed: () {
                          Navigator.of(context).pop();
                        },
                      ),
                      const SizedBox(height: 20),
                    ],
                  ),
                ),
              ],
            ),
          );
        }
        if (responseBody['status'] == 'Driver_Banned') {
          showDialog(
            context: context,
            builder: (context) => SimpleDialog(
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(20),
              ),
              title: const Text(
                'Sorry you have been banned by your company admin',
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
                          backgroundColor: Colors.green,
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
                        onPressed: () {
                          Navigator.of(context).pop();
                        },
                      ),
                      const SizedBox(height: 20),
                    ],
                  ),
                ),
              ],
            ),
          );
        }
        if (responseBody['status'] == 'Failed') {
          showDialog(
            context: context,
            builder: (context) => SimpleDialog(
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(20),
              ),
              title: const Text(
                'Failed to complete your request',
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
                          backgroundColor: Colors.green,
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
                        onPressed: () {
                          Navigator.of(context).pop();
                        },
                      ),
                      const SizedBox(height: 20),
                    ],
                  ),
                ),
              ],
            ),
          );
        }
        if (responseBody['driver']['driverIsBanned'] == true) {
          showDialog(
            context: context,
            builder: (context) => SimpleDialog(
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(20),
              ),
              title: const Text(
                'You are banned you can only view your delivered and canceled orders and notifications.',
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
                          backgroundColor: Colors.red,
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
                      SizedBox(height: 20),
                    ],
                  ),
                ),
              ],
            ),
          );
        }
      } catch (error) {
        print(error);
      }
      setState(() {
        _isLoading = false;
      });
    }
  }

  @override
  void initState() {
    super.initState();
    getPosition();
    getLatAndLong();
  }

  @override
  Widget build(BuildContext context) {
    var height = MediaQuery.of(context).size.height;
    var width = MediaQuery.of(context).size.width;

    return Stack(
      children: [
        Card(
          shadowColor: Colors.transparent,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(35),
          ),
          margin: EdgeInsets.fromLTRB(0, height * 0.19, 0, width * 0.19),
          child: Padding(
            padding: EdgeInsets.all(40),
            child: Form(
              key: _form,
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: <Widget>[
                  const SizedBox(height: 70),
                  BorderedText(
                    strokeWidth: 4.5,
                    child: Text(
                      'DeliStar',
                      style: TextStyle(
                        fontSize: 50,
                        fontWeight: FontWeight.w500,
                        color: Colors.amber[400],
                        fontStyle: FontStyle.italic,
                      ),
                    ),
                  ).animate().fadeIn(delay: 200.ms).slideX(),
                  const SizedBox(height: 55),
                  Column(
                    children: <Widget>[
                      TextFormField(
                          decoration: InputDecoration(
                            border: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(20),
                            ),
                            prefixIcon: Icon(Icons.account_circle),
                            labelText: 'Username',
                          ),
                          keyboardType: TextInputType.emailAddress,
                          textInputAction: TextInputAction.next,
                          onFieldSubmitted: (_) {
                            FocusScope.of(context)
                                .requestFocus(_passwordFocusNode);
                          },
                          validator: (value) {
                            if (value!.isEmpty) {
                              return 'Please enter username';
                            }
                            if (!value.contains('@') || !value.contains('.')) {
                              return 'Invalid Email';
                            }
                            return null;
                          },
                          onSaved: (value) {
                            _authData['email'] = value.toString().trim();
                          }).animate().fadeIn(delay: 150.ms),
                      const SizedBox(height: 30),
                      TextFormField(
                          decoration: InputDecoration(
                            border: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(20),
                            ),
                            prefixIcon: Icon(Icons.lock),
                            suffixIcon: IconButton(
                              onPressed: () {
                                setState(() {
                                  _hidePassword = !_hidePassword;
                                });
                              },
                              icon: Icon(
                                _hidePassword
                                    ? Icons.visibility_off
                                    : Icons.visibility,
                              ),
                            ),
                            labelText: 'Password',
                          ),
                          obscureText: _hidePassword,
                          validator: (value) {
                            if (value!.isEmpty) {
                              return 'Password can\'t be empty';
                            }
                            return null;
                          },
                          onSaved: (value) {
                            _authData['password'] = value.toString();
                          }).animate().fadeIn(delay: 150.ms),
                    ],
                  ),
                  const SizedBox(height: 30),
                  _isLoading
                      ? LinearProgressIndicator(
                          color: Colors.grey[300],
                          backgroundColor: Colors.amber[400],
                          minHeight: 15,
                        )
                      : ElevatedButton(
                          style: ElevatedButton.styleFrom(
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(20.0),
                            ),
                            backgroundColor: Colors.yellow[600],
                            shadowColor: Colors.transparent,
                          ),
                          child: Container(
                            padding: EdgeInsets.only(
                                left: 40, right: 40, top: 10, bottom: 10),
                            child: const Text(
                              'Login',
                              style: TextStyle(
                                fontSize: 20,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                          ),
                          onPressed: () {
                            _saveForm();
                          },
                        ).animate().fade(delay: 200.ms),
                  const SizedBox(height: 40),
                ],
              ),
            ),
          ),
        ),
        Positioned(
          top: height * 0.13,
          right: width * 0.05,
          left: width * 0.05,
          child: Center(
            child: CircleAvatar(
              radius: 60,
              backgroundImage: AssetImage('images/user.png'),
            ),
          ),
        ),
      ],
    );
  }
}
