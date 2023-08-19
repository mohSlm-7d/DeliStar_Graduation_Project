import 'package:driver_app/screens/load_info_screen.dart';
import './screens/auth_screen.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'screens/orders_overview_screen.dart';
import 'providers/orders.dart';
import './screens/notifiactions_screen.dart';
import './screens/canceled_orders_screen.dart';
import './screens/delivered_orders_screen.dart';
import './screens/tabs_screen.dart';
import 'package:flutter/services.dart';
import 'package:shared_preferences/shared_preferences.dart';

late SharedPreferences? sharedPref;

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  SystemChrome.setPreferredOrientations([
    DeviceOrientation.portraitUp,
    DeviceOrientation.portraitDown,
  ]);
  sharedPref = await SharedPreferences.getInstance();
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider(
          create: (ctx) => Orders(),
        ),
      ],
      child: MaterialApp(
        debugShowCheckedModeBanner: false,
        title: 'DeliStar',
        theme: ThemeData(
          primarySwatch: Colors.grey,
        ),
        // ignore: unnecessary_null_comparison
        initialRoute:
            sharedPref?.getString('id') == null ? 'authScreen' : 'loadInfo',
        routes: {
          'authScreen': (context) => AuthScreen(),
          'loadInfo': (context) => LoadInfo(),
          OrdersOverviewScreen.routeName: (ctx) => OrdersOverviewScreen(),
          Notifications.routeName: (ctx) => Notifications(),
          CanceledOrders.routeName: (ctx) => CanceledOrders(),
          DeliveredOrders.routeName: (ctx) => DeliveredOrders(),
          TabsScreen.routeName: (ctx) => TabsScreen(),
        },
      ),
    );
  }
}
