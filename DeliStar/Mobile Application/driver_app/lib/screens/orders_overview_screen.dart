import 'package:driver_app/providers/orders.dart';
import 'package:flutter/material.dart';
import 'package:lottie/lottie.dart';
import '../widgets/app_drawer.dart';
import '../widgets/orders_list.dart';
import 'package:provider/provider.dart';
import 'package:flutter_animate/flutter_animate.dart';

class OrdersOverviewScreen extends StatefulWidget {
  static const routeName = '/orders';

  @override
  State<OrdersOverviewScreen> createState() => _OrdersOverviewScreenState();
}

class _OrdersOverviewScreenState extends State<OrdersOverviewScreen> {
  @override
  Widget build(BuildContext context) {
    var height = MediaQuery.of(context).size.height;
    var width = MediaQuery.of(context).size.width;
    var orders = Provider.of<Orders>(context);

    return Scaffold(
      drawer: AppDrawer(),
      body: Container(
        padding: EdgeInsets.only(top: 7),
        height: height * 100,
        width: width * 100,
        decoration: BoxDecoration(
          color: Colors.grey[200],
          borderRadius: BorderRadius.only(
            topLeft: Radius.circular(25),
            topRight: Radius.circular(25),
          ),
        ),
        child: orders.getOrder.length > 0
            ? OrdersList().animate().fadeIn(duration: 500.ms)
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
                    )
                  ],
                ),
              ),
      ),
    );
  }
}
