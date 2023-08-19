import 'package:flutter/material.dart';

import 'package:readmore/readmore.dart';

// ignore: must_be_immutable
class NotificationItem extends StatefulWidget {
  final _val;
  NotificationItem(this._val);

  @override
  State<NotificationItem> createState() => _NotificationItemState();
}

class _NotificationItemState extends State<NotificationItem> {
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.only(
        left: 15,
        right: 15,
        bottom: 10,
      ),
      child: Card(
        shadowColor: Colors.transparent,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(20.0),
        ),
        child: Container(
          height: 170,
          child: Row(
            children: <Widget>[
              SizedBox(width: 10),
              Container(
                padding: EdgeInsets.only(
                  bottom: 110,
                ),
                margin: EdgeInsets.only(
                  top: 20,
                  left: 5,
                ),
                child: CircleAvatar(
                  radius: 20,
                  backgroundImage: AssetImage('images/message_icon.png'),
                ),
              ),
              SizedBox(width: 10),
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  Container(
                    margin: EdgeInsets.only(
                      top: 21,
                      right: 45,
                    ),
                    child: Text(
                      'Admin',
                      style: TextStyle(
                        color: Colors.grey,
                        fontWeight: FontWeight.bold,
                        fontSize: 16,
                      ),
                    ),
                  ),
                  SizedBox(
                    height: 15,
                  ),
                  Container(
                    width: 250,
                    height: 80,
                    padding: EdgeInsets.only(
                      right: 45,
                    ),
                    child: SingleChildScrollView(
                      child: ReadMoreText(
                        widget._val,
                        trimLines: 3,
                        trimMode: TrimMode.Line,
                        trimCollapsedText: "Show More",
                        trimExpandedText: "Show Less",
                        lessStyle: TextStyle(
                          fontWeight: FontWeight.bold,
                          fontSize: 20,
                          color: Colors.green,
                        ),
                        moreStyle: TextStyle(
                          fontWeight: FontWeight.bold,
                          fontSize: 13,
                          color: Colors.green,
                        ),
                        style: TextStyle(
                          fontWeight: FontWeight.bold,
                          fontSize: 15,
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}
