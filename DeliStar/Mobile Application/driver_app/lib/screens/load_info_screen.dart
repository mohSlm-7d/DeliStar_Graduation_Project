import '../screens/tabs_screen.dart';
import 'package:flutter/material.dart';
import '../providers/orders.dart';
import 'package:provider/provider.dart';
import 'package:lottie/lottie.dart';

class LoadInfo extends StatefulWidget {
  static const routeName = '/loadeInfo';

  @override
  State<LoadInfo> createState() => _LoadInfoState();
}

class _LoadInfoState extends State<LoadInfo> {
  var _isInit = true;
  var _isLoading = false;

  void didChangeDependencies() {
    if (_isInit) {
      setState(() {
        _isLoading = true;
      });

      Provider.of<Orders>(context).fetchAndSetOrders().then((_) {
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
              'An Error Occured While Loading The Orders',
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
                        Navigator.of(context)
                            .pushReplacementNamed(TabsScreen.routeName);
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
    return _isLoading
        ? Scaffold(
            body: Container(
              color: Colors.grey[200],
              child: Center(
                child: Lottie.asset(
                  'images/delivery.json',
                ),
              ),
            ),
          )
        : TabsScreen();
  }
}
