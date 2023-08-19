import 'package:flutter/material.dart';
import 'package:flutter_animate/flutter_animate.dart';
import '../constants/links.dart';
import '../main.dart';
import '../widgets/app_drawer.dart';
import '../widgets/delivered_order_item.dart';
import 'package:provider/provider.dart';
import 'package:lottie/lottie.dart';
import '../providers/orders.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;

class DeliveredOrders extends StatefulWidget {
  static const routeName = '/deliveredOrders';

  @override
  State<DeliveredOrders> createState() => _DeliveredOrdersState();
}

class _DeliveredOrdersState extends State<DeliveredOrders> {
  var _isInit = true;

  var _isLoading = false;

  void didChangeDependencies() {
    if (_isInit) {
      setState(() {
        _isLoading = true;
      });
      Provider.of<Orders>(context).fetchAndSetDeliveredOrders().then((_) {
        setState(() {
          _isLoading = false;
        });
      }).catchError((error) {
        print(error);
        return showDialog<Null>(
          context: context,
          builder: (ctx) => SimpleDialog(
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(20),
            ),
            title: const Text(
              'An error occured while loading the orders',
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
    var height = MediaQuery.of(context).size.height;

    var width = MediaQuery.of(context).size.width;

    final deliveredOrdersData = Provider.of<Orders>(context);

    final _deliveredOrdersList = deliveredOrdersData.getDeliveredOrdersList;

    var id = sharedPref?.getString('id').toString();

    var driverToken = sharedPref?.getString('token').toString();

    return Scaffold(
      drawer: AppDrawer(),
      body: _isLoading
          ? Center(
              child: CircularProgressIndicator(),
            )
          : deliveredOrdersData.navigateTo == 1
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
              : _deliveredOrdersList.isEmpty
                  ? Container(
                      height: height * 100,
                      width: width * 100,
                      decoration: BoxDecoration(
                        color: Colors.grey[200],
                        borderRadius: BorderRadius.only(
                          topLeft: Radius.circular(25),
                          topRight: Radius.circular(25),
                        ),
                      ),
                      child: Column(
                        children: <Widget>[
                          SizedBox(
                            height: 100,
                          ),
                          Container(
                            width: 250,
                            height: 250,
                            child: Lottie.asset('images/check_list.json'),
                          ),
                          SizedBox(
                            height: 100,
                          ),
                          Text(
                            'No Delivered Orders',
                            style: TextStyle(
                              fontSize: 30,
                              fontWeight: FontWeight.bold,
                              color: Colors.grey,
                            ),
                          ),
                        ],
                      ),
                    ).animate().fadeIn()
                  : Container(
                      height: height * 100,
                      width: width * 100,
                      decoration: BoxDecoration(
                        color: Colors.grey[200],
                        borderRadius: BorderRadius.only(
                          topLeft: Radius.circular(25),
                          topRight: Radius.circular(25),
                        ),
                      ),
                      child: Container(
                        padding: EdgeInsets.only(
                          top: 10,
                        ),
                        height: 400,
                        child: ListView.builder(
                          itemCount: _deliveredOrdersList.length,
                          itemBuilder: (ctx, index) =>
                              ChangeNotifierProvider.value(
                            value: _deliveredOrdersList[index],
                            child: Padding(
                              padding: EdgeInsets.only(
                                top: 5,
                                left: 15,
                                right: 15,
                                bottom: 5,
                              ),
                              child: Card(
                                shadowColor: Colors.transparent,
                                shape: RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(20.0),
                                ),
                                child: Container(
                                  height: 170,
                                  child: DeliveredOrderItem(),
                                ),
                              ),
                            ),
                          ),
                        ),
                      ).animate().fadeIn(delay: 200.ms),
                    ),
    );
  }
}
