import 'package:flutter/material.dart';
import 'package:bordered_text/bordered_text.dart';
import '../screens/delivered_orders_screen.dart';
import '../screens/orders_overview_screen.dart';
import '../widgets/app_drawer.dart';
import '../screens/canceled_orders_screen.dart';
import '../screens/notifiactions_screen.dart';
import 'package:flutter_animate/flutter_animate.dart';

class TabsScreen extends StatefulWidget {
  static const routeName = '/TabsScreen';

  @override
  State<TabsScreen> createState() => _TabsScreenState();
}

class _TabsScreenState extends State<TabsScreen> {
  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
      length: 3,
      child: Scaffold(
        appBar: AppBar(
          leading: Builder(
            builder: (BuildContext context) {
              return IconButton(
                icon: const Icon(
                  Icons.menu_open_rounded,
                  size: 40,
                ),
                onPressed: () {
                  Scaffold.of(context).openDrawer();
                },
                tooltip: MaterialLocalizations.of(context).openAppDrawerTooltip,
              );
            },
          ),
          actions: <Widget>[
            Padding(
              padding: EdgeInsets.only(
                right: 3,
              ),
              child: IconButton(
                icon: const Icon(
                  Icons.notifications_none_rounded,
                  size: 40,
                  color: Colors.black,
                ),
                onPressed: () {
                  Navigator.push(context, MaterialPageRoute(builder: (context) {
                    return Notifications();
                  }));
                },
              ),
            ),
          ],
          centerTitle: true,
          title: BorderedText(
            strokeWidth: 3,
            child: Text(
              'DeliStar',
              style: TextStyle(
                fontSize: 35,
                fontWeight: FontWeight.w500,
                color: Colors.amber[400],
                fontStyle: FontStyle.italic,
              ),
            ),
          ).animate().slide(delay: 250.ms).fadeIn(),
          backgroundColor: Colors.transparent,
          shadowColor: Colors.transparent,
        ),
        drawer: AppDrawer(),
        body: Column(
          children: <Widget>[
            Padding(
              padding: EdgeInsets.only(
                top: 3,
                left: 15,
                right: 15,
                bottom: 15,
              ),
              child: Container(
                height: 40,
                decoration: BoxDecoration(
                  color: Colors.grey[300],
                  borderRadius: BorderRadius.all(
                    Radius.circular(15),
                  ),
                ),
                child: TabBar(
                  indicator: BoxDecoration(
                    color: Colors.amber[400],
                    borderRadius: BorderRadius.all(
                      Radius.circular(15),
                    ),
                  ),
                  labelColor: Colors.black,
                  unselectedLabelColor: Colors.grey[600],
                  tabs: [
                    Tab(text: 'Orders'),
                    Tab(text: 'Delivered'),
                    Tab(text: 'Canceled'),
                  ],
                ),
              ),
            ).animate().fadeIn(),
            Expanded(
              child: TabBarView(
                children: <Widget>[
                  OrdersOverviewScreen(),
                  DeliveredOrders(),
                  CanceledOrders(),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
