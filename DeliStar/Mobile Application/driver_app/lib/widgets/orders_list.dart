import 'package:flutter/material.dart';
import 'package:flutter_animate/flutter_animate.dart';
import 'package:lottie/lottie.dart';

import '../widgets/order_item.dart';
import '../providers/orders.dart';
import 'package:provider/provider.dart';
import 'package:flutter_slidable/flutter_slidable.dart';
import 'package:geolocator/geolocator.dart';

class OrdersList extends StatefulWidget {
  @override
  State<OrdersList> createState() => _OrdersListState();
}

class _OrdersListState extends State<OrdersList> {
  var _isLoading = false;
  // Services location => False disabled => True enabled.
  late Position cl;
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
    if (services == false) {
      return showDialog<Null>(
        barrierDismissible: false,
        context: context,
        builder: (ctx) => SimpleDialog(
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(20),
          ),
          title: const Text(
            'Device Location Is Turned Off Please Enable Device Location',
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
    if (per == LocationPermission.whileInUse) {
      cl = await getLatAndLong();
      // print("Lat ${cl.latitude}");
      // print("Lon ${cl.longitude}");
      //  print(cl.latitude);
      //  print(cl.longitude);
      // print("================================");
    }
  }

  @override
  void initState() {
    getPosition();
    super.initState();
  }

  TextEditingController reportController = TextEditingController();

  final _form = GlobalKey<FormState>();
  void _saveForm() {
    final isValid = _form.currentState!.validate();
    print(reportController.text.trim());
    if (isValid) {
      final ordersData = Provider.of<Orders>(context, listen: false);
      final orders = ordersData.getOrder; //list in orders class
      setState(() {
        _isLoading = true;
      });

      Provider.of<Orders>(context, listen: false)
          .addCanceledOrder(orders[0], ordersData.getOrder[0].getOrderNumber,
              reportController.text.trim())
          .then((_) {
        setState(() {
          _isLoading = false;
        });
      });
      Provider.of<Orders>(context, listen: false)
          .removeOrder(ordersData.getOrder[0].getOrderNumber);
      Navigator.of(context).pop();
      reportController.clear();
    }
  }

  @override
  Widget build(BuildContext context) {
    final ordersData = Provider.of<Orders>(context);

    final orders = ordersData.getOrder; // list in orders class

    final remOrders = ordersData.getRemOrders; // That skips order[0]

    //var orderNumber = ordersData.getOrder[0].getOrderNumber;

    return _isLoading
        ? Center(
            child: CircularProgressIndicator(),
          )
        : Column(
            children: <Widget>[
              Container(
                child: Slidable(
                  endActionPane: ActionPane(
                    motion: const StretchMotion(),
                    children: [
                      SlidableAction(
                        borderRadius: BorderRadius.all(
                          Radius.circular(25),
                        ),
                        backgroundColor: Colors.red,
                        icon: Icons.cancel,
                        label: 'Cancel',
                        onPressed: (context) {
                          showDialog(
                            context: context,
                            builder: (context) => SimpleDialog(
                              shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(20),
                              ),
                              title: const Text(
                                'Why do you want to cancel the delivery?',
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
                                Form(
                                  key: _form,
                                  child: Container(
                                    padding: EdgeInsets.only(
                                      left: 25,
                                      right: 25,
                                    ),
                                    child: TextFormField(
                                      controller: reportController,
                                      decoration: InputDecoration(
                                        border: OutlineInputBorder(
                                          borderRadius:
                                              BorderRadius.circular(20),
                                        ),
                                        labelText: 'Write your problem',
                                      ),
                                      validator: (value) {
                                        if (value!.isEmpty) {
                                          return 'This field can\'t be empty';
                                        }
                                        return null;
                                      },
                                    ),
                                  ),
                                ),
                                SizedBox(
                                  height: 20,
                                ),
                                Container(
                                  child: Column(
                                    children: <Widget>[
                                      ElevatedButton(
                                        style: ElevatedButton.styleFrom(
                                          shape: RoundedRectangleBorder(
                                            borderRadius:
                                                BorderRadius.circular(20.0),
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
                                            'Send',
                                            style: TextStyle(
                                              color: Colors.white,
                                              fontSize: 20,
                                              fontWeight: FontWeight.normal,
                                            ),
                                          ),
                                        ),
                                        onPressed: () {
                                          if (ordersData
                                                  .getOrder[0].getOrderNumber >
                                              0) {
                                            _saveForm();
                                          }
                                        },
                                      ),
                                      SizedBox(height: 20),
                                      ElevatedButton(
                                        style: ElevatedButton.styleFrom(
                                          shape: RoundedRectangleBorder(
                                            borderRadius:
                                                BorderRadius.circular(20.0),
                                          ),
                                          backgroundColor: Colors.grey[200],
                                          shadowColor: Colors.transparent,
                                        ),
                                        child: Container(
                                          padding: EdgeInsets.only(
                                            left: 26,
                                            right: 26,
                                            top: 10,
                                            bottom: 12,
                                          ),
                                          child: const Text(
                                            'Go Back',
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
                      SlidableAction(
                        borderRadius: BorderRadius.all(
                          Radius.circular(25),
                        ),
                        backgroundColor: Colors.green,
                        icon: Icons.check_circle,
                        label: 'Confirm',
                        onPressed: (context) {
                          showDialog(
                            context: context,
                            builder: (context) => SimpleDialog(
                              shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(20),
                              ),
                              title: const Text(
                                'Confirm Delivery?',
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
                                            borderRadius:
                                                BorderRadius.circular(20.0),
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
                                            'Confirm',
                                            style: TextStyle(
                                              color: Colors.white,
                                              fontSize: 20,
                                              fontWeight: FontWeight.normal,
                                            ),
                                          ),
                                        ),
                                        onPressed: () {
                                          if (ordersData.getOrder.length > 0) {
                                            setState(() {
                                              _isLoading = true;
                                            });
                                            Provider.of<Orders>(context,
                                                    listen: false)
                                                .addDeliveredOrder(
                                                    orders[0],
                                                    ordersData.getOrder[0]
                                                        .getOrderNumber)
                                                .then((_) {
                                              setState(() {
                                                _isLoading = false;
                                              });
                                            });
                                          }
                                          Provider.of<Orders>(context,
                                                  listen: false)
                                              .removeOrder(ordersData
                                                  .getOrder[0].getOrderNumber);

                                          Navigator.of(context).pop();
                                        },
                                      ),
                                      SizedBox(height: 20),
                                      ElevatedButton(
                                        style: ElevatedButton.styleFrom(
                                          shape: RoundedRectangleBorder(
                                            borderRadius:
                                                BorderRadius.circular(20.0),
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
                    ],
                  ),
                  child: ordersData.getOrder.length > 0
                      ? ChangeNotifierProvider.value(
                          value: orders[0],
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
                                child: OrderItem(
                                  true,
                                ),
                              ),
                            ),
                          ),
                        )
                      : Container(),
                ),
              ),
              Padding(
                padding: EdgeInsets.only(bottom: 10, top: 3),
                child: Row(
                  children: <Widget>[
                    SizedBox(width: 25),
                    Expanded(
                      child: Divider(
                        thickness: 2,
                        color: Colors.grey[400],
                      ),
                    ),
                    SizedBox(width: 10),
                    Text(
                      'Orders left ${orders.length} ',
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        fontSize: 18,
                        fontWeight: FontWeight.w500,
                        color: Colors.grey[500],
                      ),
                    ),
                    SizedBox(width: 10),
                    Expanded(
                      child: Divider(
                        thickness: 2,
                        color: Colors.grey[400],
                      ),
                    ),
                    SizedBox(width: 25),
                  ],
                ),
              ),
              Expanded(
                child: Container(
                  child: ordersData.getOrder.length > 0
                      ? ListView.builder(
                          itemCount: remOrders.length,
                          itemBuilder: (ctx, index) {
                            return ChangeNotifierProvider.value(
                              value: remOrders[index],
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
                                    child: OrderItem(false),
                                  ),
                                ),
                              ),
                            );
                          }).animate().fadeIn()
                      : Container(
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: <Widget>[
                              Lottie.asset(
                                'images/no_orders.json',
                              ),
                              Container(
                                margin: EdgeInsets.only(
                                  bottom: 100,
                                ),
                                child: Text(
                                  'No More Orders Left',
                                  style: TextStyle(
                                    fontSize: 30,
                                    fontWeight: FontWeight.bold,
                                    color: Colors.grey,
                                  ),
                                ),
                              ),
                            ],
                          ),
                        ),
                ).animate().fadeIn(),
              ),
            ],
          );
  }
}
