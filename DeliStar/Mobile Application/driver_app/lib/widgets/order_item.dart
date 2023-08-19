import 'package:url_launcher/url_launcher.dart';
import '../main.dart';
import '../providers/order.dart';
import 'package:provider/provider.dart';
import 'package:flutter/material.dart';
import 'package:readmore/readmore.dart';
import 'package:flutter_phone_direct_caller/flutter_phone_direct_caller.dart';

class OrderItem extends StatelessWidget {
  final _enaDis;

  OrderItem(bool this._enaDis);
  @override
  Widget build(BuildContext context) {
    final ordersData = Provider.of<Order>(context, listen: false);
    var content = ordersData.getDropOffAddress;
    var nameContent = ordersData.getReceiverName;
    var locationLink = ordersData.locationLink;
    var phoneNumber = ordersData.getPhoneNumber;
    var orderNumber = ordersData.getOrderNumber;
    sharedPref?.setString("orderNumber", orderNumber.toString());
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: <Widget>[
        Row(
          children: <Widget>[
            SizedBox(width: 10),
            Container(
              margin: EdgeInsets.only(
                top: 20,
                left: 5,
              ),
              child: CircleAvatar(
                radius: 20,
                backgroundImage: AssetImage('images/location_icon.png'),
              ),
            ),
            SizedBox(width: 10),
            Column(
              children: <Widget>[
                Container(
                  margin: EdgeInsets.only(
                    top: 20,
                    right: 45,
                  ),
                  child: Text(
                    'Drop point',
                    style: TextStyle(
                      color: Colors.grey,
                      fontWeight: FontWeight.bold,
                      fontSize: 16,
                    ),
                  ),
                ),
                SizedBox(
                  height: 10,
                ),
                Container(
                  height: 20,
                  width: 110,
                  margin: EdgeInsets.only(right: 8, bottom: 5),
                  child: SingleChildScrollView(
                    child: ReadMoreText(
                      nameContent,
                      trimLines: 2,
                      trimMode: TrimMode.Line,
                      trimCollapsedText: "Show More",
                      trimExpandedText: "Show Less",
                      lessStyle: TextStyle(
                        fontWeight: FontWeight.bold,
                        fontSize: 13,
                        color: Colors.green,
                      ),
                      moreStyle: TextStyle(
                        fontWeight: FontWeight.bold,
                        fontSize: 13,
                        color: Colors.green,
                      ),
                      style: TextStyle(
                        fontWeight: FontWeight.bold,
                        fontSize: 16,
                      ),
                    ),
                  ),
                ),
              ],
            ),
            SizedBox(width: 10),
            Expanded(
              child: Container(
                margin: EdgeInsets.only(
                  top: 10,
                  right: 2,
                ),
                height: 40,
                width: 150,
                child: SingleChildScrollView(
                  child: ReadMoreText(
                    content,
                    trimLines: 2,
                    trimMode: TrimMode.Line,
                    trimCollapsedText: "Show More",
                    trimExpandedText: "Show Less",
                    lessStyle: TextStyle(
                      fontWeight: FontWeight.bold,
                      fontSize: 13,
                      color: Colors.green,
                    ),
                    moreStyle: TextStyle(
                      fontWeight: FontWeight.bold,
                      fontSize: 13,
                      color: Colors.green,
                    ),
                    style: TextStyle(
                      fontWeight: FontWeight.bold,
                      fontSize: 16,
                    ),
                  ),
                ),
              ),
            ),
          ],
        ),
        SizedBox(height: 35),
        IntrinsicHeight(
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: <Widget>[
              Column(
                //
                children: <Widget>[
                  Container(
                    margin: EdgeInsets.only(
                      top: 2,
                      left: 20,
                    ),
                    child: Text(
                      'Order NO.',
                      style: TextStyle(
                        color: Colors.grey,
                        fontWeight: FontWeight.bold,
                        fontSize: 15,
                      ),
                    ),
                  ),
                  Container(
                    margin: EdgeInsets.only(
                      top: 9,
                      right: 34,
                    ),
                    child: SingleChildScrollView(
                      child: Text(
                        ordersData.getOrderNumber.toString(),
                        style: TextStyle(
                          fontSize: 14,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                  ),
                ],
              ),
              VerticalDivider(
                thickness: 1.5,
                color: Colors.grey[300],
              ),
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  Container(
                    margin: EdgeInsets.only(
                      top: 2.2,
                      bottom: 5,
                    ),
                    child: Text(
                      'Order State',
                      style: TextStyle(
                        color: Colors.grey,
                        fontWeight: FontWeight.bold,
                        fontSize: 15,
                      ),
                    ),
                  ),
                  Container(
                    margin: EdgeInsets.only(left: 1.5, top: 5),
                    child: SingleChildScrollView(
                      child: Text(
                        _enaDis == true ? 'Delivering' : 'Pending',
                        style: TextStyle(
                          //  color: Colors.grey,
                          fontWeight: FontWeight.bold,
                          fontSize: 14,
                        ),
                      ),
                    ),
                  ),
                ],
              ),
              Expanded(
                child: Container(
                  child: IconButton(
                    iconSize: 34,
                    icon: Icon(
                      Icons.phone_rounded,
                    ),
                    onPressed: _enaDis == false
                        ? null
                        : () async {
                            await FlutterPhoneDirectCaller.callNumber(
                                phoneNumber);
                          },
                  ),
                ),
              ),
              Expanded(
                child: Container(
                  child: IconButton(
                    iconSize: 34,
                    icon: Icon(
                      Icons.map_rounded,
                    ),
                    color: Colors.green,
                    onPressed: _enaDis == false
                        ? null
                        : () {
                            final url = Uri.parse(locationLink);
                            launchUrl(url);
                          },
                  ),
                ),
              ),
              Expanded(
                child: Container(
                  child: IconButton(
                    iconSize: 34,
                    icon: Icon(
                      Icons.info_outline,
                    ),
                    color: Colors.amber[400],
                    onPressed: () {
                      showDialog(
                        context: context,
                        builder: (context) => SimpleDialog(
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(20),
                          ),
                          title: Center(
                            child: Text(
                              'Drop off address',
                              style: TextStyle(
                                fontSize: 25,
                              ),
                            ),
                          ),
                          children: <Widget>[
                            Container(
                              height: 90,
                              padding: EdgeInsets.only(
                                left: 6,
                                right: 6,
                              ),
                              child: SingleChildScrollView(
                                child: Text(
                                  ordersData.getDropOffAddress,
                                  textAlign: TextAlign.center,
                                  style: TextStyle(
                                    fontSize: 20,
                                    color: Colors.grey[600],
                                    fontWeight: FontWeight.bold,
                                  ),
                                ),
                              ),
                            ),
                            Container(
                              child: Column(
                                children: <Widget>[
                                  SizedBox(height: 20),
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
                                        left: 45,
                                        right: 45,
                                        top: 10,
                                        bottom: 12,
                                      ),
                                      child: const Text(
                                        'Go Back',
                                        style: TextStyle(
                                          fontSize: 20,
                                          fontWeight: FontWeight.bold,
                                          color: Colors.white,
                                        ),
                                      ),
                                    ),
                                    onPressed: () {
                                      Navigator.of(context).pop();
                                    },
                                  ),
                                  SizedBox(
                                    height: 10,
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
              ),
            ],
          ),
        ),
      ],
    );
  }
}
