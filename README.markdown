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
  *  Supports message delegates and callbacks


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
        
3. Create a ssl connection

		MpnsService service = MPNS.newService()
                    .withCert(WIN_MPNS_KEYSTORE_PATH, WIN_MPNS_CERTIFICATE_PASS, "JKS", KeyManagerFactory.getDefaultAlgorithm())
                    .build();
        The common name of certificate used in creating the keystore should be used during registering for the windows push service
        (opening the push channel) by the windows native app.

That's it!

Features In the Making
---------------------------
  * Authenticated Connections(DONE)
  * Auto retries (exponential back-off feature)
  * More testing!

Sponsorship
---------------

This work is sponsored by [Excitor A/S](http://www.excitor.com/).

License
----------------

Licensed under the [New 3-Clause BSD License](http://www.opensource.org/licenses/BSD-3-Clause).

    Copyright 2011, Mahmood Ali.
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are
    met:

      * Redistributions of source code must retain the above copyright
        notice, this list of conditions and the following disclaimer.
      * Redistributions in binary form must reproduce the above
        copyright notice, this list of conditions and the following disclaimer
        in the documentation and/or other materials provided with the
        distribution.
      * Neither the name of Mahmood Ali. nor the names of its
        contributors may be used to endorse or promote products derived from
        this software without specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
    "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
    LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
    A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
    OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
    SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
    LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
    DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
    THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
    (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
    OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

Contact
---------------
Support mailing list: http://groups.google.com/group/java-apns-discuss
