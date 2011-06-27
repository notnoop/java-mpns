/*
* Copyright 2011, Mahmood Ali.
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are
* met:
*
* * Redistributions of source code must retain the above copyright
* notice, this list of conditions and the following disclaimer.
* * Redistributions in binary form must reproduce the above
* copyright notice, this list of conditions and the following disclaimer
* in the documentation and/or other materials provided with the
* distribution.
* * Neither the name of Mahmood Ali. nor the names of its
* contributors may be used to endorse or promote products derived from
* this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
* "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
* LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
* A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
* OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
* SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
* LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
* DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
* THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
* OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.notnoop.mpns.internal;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import com.notnoop.mpns.MpnsDelegate;
import com.notnoop.mpns.MpnsNotification;
import com.notnoop.mpns.MpnsResponse;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

public final class Utilities {
    private Utilities() { throw new AssertionError("Uninstantiable class"); }

    /**
     * The content type "text/xml"
     */
    public static String XML_CONTENT_TYPE = "text/xml";

    public static ThreadSafeClientConnManager poolManager(int maxConnections) {
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager();
        cm.setMaxTotal(maxConnections);
        cm.setDefaultMaxPerRoute(maxConnections);

        return cm;
    }

    /**
     * Returns {@cond value} is the {@code cond} is non-null; otherwise
     * returns an empty String.
     */
    public static String ifNonNull(Object cond, String value) {
        return cond != null ? value : "";
    }

    public static String escapeXml(String value) {
        if (value == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); ++i) {
            char ch = value.charAt(i);
            switch (ch) {
            case '&': sb.append("&amp;"); break;
            case '<': sb.append("&lt;"); break;
            case '>': sb.append("&gt;"); break;
            case '"': sb.append("&quot;"); break;
            case '\'': sb.append("&apos;"); break;
            default: sb.append(ch);
            }
        }

        return sb.toString();
    }

    public static byte[] toUTF8(String content) {
        try {
            return content.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("The world is coming to an end!  No UTF-8 support");
        }
    }

    private static String headerValue(HttpResponse response, String name) {
        Header header = response.getFirstHeader(name);

        return header == null ? null: header.getValue();
    }

    private static MpnsResponse[] logicalResponses = MpnsResponse.values();
    public static MpnsResponse logicalResponseFor(HttpResponse response) {
        for (MpnsResponse r: logicalResponses) {
            if (r.getResponseCode() != response.getStatusLine().getStatusCode()) {
                continue;
            }

            if (r.getNotificationStatus() != null
                && !r.getNotificationStatus().equals(headerValue(response, "X-NotificationStatus"))) {
                continue;
            }

            if (r.getDeviceConnectionStatus() != null
                && !r.getNotificationStatus().equals(headerValue(response, "X-DeviceConnectionStatus"))) {
                continue;
            }

            if (r.getSubscriptionStatus() != null
                && !r.getSubscriptionStatus().equals(headerValue(response, "X-SubscriptionStatus"))) {
                continue;
            }

            return r;
        }

        // Didn't find anything
        assert false;
        return null;
    }

    public static void fireDelegate(MpnsNotification message, HttpResponse response, MpnsDelegate delegate) {
        if (delegate != null) {
            MpnsResponse r = Utilities.logicalResponseFor(response);

            if (r.isSuccessful()) {
                delegate.messageSent(message, r);
            } else {
                delegate.messageFailed(message, r);
            }
        }
    }
}
