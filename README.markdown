java-mpns is a Java client for Microsoft Push Notification service (MPNs).
The library aims to provide a highly scalable interface to the Apple
server, while still being simple and modular.

The interface aims to require very minimal code to achieve the most common
cases, but have it be reconfigurable so you can even use your own networking
connections or XML library if necessary.

Links: [Installation](http://wiki.github.com/notnoop/java-mpns/installation)
- [Javadocs](http://notnoop.github.com/java-mpns/apidocs/index.html)
- [Changelog](https://github.com/notnoop/java-mpns/blob/master/CHANGELOG)

Features:
--------------
  *  Easy to use, high performance MPNS Service API
  *  Easy to extend and reuse
  *  Easy to integrate with dependency injection frameworks
  *  Easy to setup custom notification payloads
  *  Supports connection pooling


Sample Code
----------------

To send a notification, you can do it in two steps:

1. Setup the connection

        MpnsService service =
            MPNS.newService()
            .build();

2. Create and send the message

        MpnsMessage notification = MPNS.newMessage()
            .tile().count(2).title("Tile message")
            .build();
        String subscriptionUri = "https://..../"
        service.push(subscriptionUri, notification);

That's it!

Contact
---------------
Support mailing list: http://groups.google.com/group/java-apns-discuss
