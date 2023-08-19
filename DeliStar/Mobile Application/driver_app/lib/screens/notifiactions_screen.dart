import 'package:flutter/material.dart';
import '../widgets/app_drawer.dart';
import '../widgets/notification_list.dart';
import '../screens/tabs_screen.dart';

class Notifications extends StatefulWidget {
  static const routeName = '/notifications';

  @override
  State<Notifications> createState() => _NotificationsState();
}

class _NotificationsState extends State<Notifications> {
  @override
  Widget build(BuildContext context) {
    var height = MediaQuery.of(context).size.height;
    var width = MediaQuery.of(context).size.width;

    return Scaffold(
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
        actions: [
          IconButton(
            iconSize: 40,
            onPressed: () {
              Navigator.of(context).pushReplacementNamed(TabsScreen.routeName);
            },
            icon: Icon(Icons.close),
          ),
        ],
        centerTitle: true,
        title: const Text(
          'Notifications',
          style: TextStyle(
            fontSize: 30,
            fontWeight: FontWeight.bold,
          ),
        ),
        backgroundColor: Colors.transparent,
        shadowColor: Colors.transparent,
      ),
      drawer: AppDrawer(),
      body: Container(
        height: height * 100,
        width: width * 100,
        decoration: BoxDecoration(
          color: Colors.grey[200],
          borderRadius: BorderRadius.only(
            topLeft: Radius.circular(35),
            topRight: Radius.circular(35),
          ),
        ),
        child: NotificationList(),
      ),
    );
  }
}
