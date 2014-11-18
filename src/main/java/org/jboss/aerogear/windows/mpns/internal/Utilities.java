/*
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.aerogear.windows.mpns.internal;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.jboss.aerogear.windows.mpns.DeliveryClass;
import org.jboss.aerogear.windows.mpns.MpnsDelegate;
import org.jboss.aerogear.windows.mpns.MpnsNotification;
import org.jboss.aerogear.windows.mpns.MpnsResponse;

import java.io.UnsupportedEncodingException;

public final class Utilities {
    private Utilities() { throw new AssertionError("Uninstantiable class"); }

    /**
     * The content type "text/xml"
     */
    public static final String XML_CONTENT_TYPE = "text/xml";

    public static ThreadSafeClientConnManager poolManager(int maxConnections) {
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager();
        cm.setMaxTotal(maxConnections);
        cm.setDefaultMaxPerRoute(maxConnections);

        return cm;
    }

    /**
     * Returns {@code value} is the {@code cond} is non-null; otherwise
     * returns an empty String.
     */
    public static String ifNonNull(Object cond, String value) {
        return cond != null ? value : "";
    }
    
    public static String xmlElement(String name, String content) {
    	return xmlElement(name, content, false);
    }
    
    public static String xmlElementClear(String name, String content) {
    	return xmlElement(name, content, true);
    }
    
    private static String xmlElement(String name, String content, boolean isClear) {
    	if( content == null || "".equals(content.trim())) {
    		return "";
    	}
    	StringBuilder sb = new StringBuilder(500);
    	sb.append("<wp:").append(name);
    	if( isClear ) {
    		sb.append(" Action=\"Clear\"");
    	}
		sb.append(">");
    	sb.append(escapeXml(content));
    	sb.append("</wp:").append(name).append(">");
    	return sb.toString();
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

    private static final MpnsResponse[] LOGICAL_RESPONSES = MpnsResponse.values();
    public static MpnsResponse logicalResponseFor(HttpResponse response) {
        for (MpnsResponse r: LOGICAL_RESPONSES) {
            if (r.getResponseCode() != response.getStatusLine().getStatusCode()) {
                continue;
            }

            if (r.getNotificationStatus() != null
                && !r.getNotificationStatus().equals(headerValue(response, "X-NotificationStatus"))) {
                continue;
            }

            if (r.getDeviceConnectionStatus() != null
                && !r.getDeviceConnectionStatus().equals(headerValue(response, "X-DeviceConnectionStatus"))) {
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
    
    public static int getTileDelivery(DeliveryClass delivery) {
    	if( delivery == null ) {
    		delivery = DeliveryClass.IMMEDIATELY;
    	}
        switch (delivery) {
        case IMMEDIATELY:   return 1;
        case WITHIN_450:    return 11;
        case WITHIN_900:    return 21;
        default:            return 1; // IMMEDIATELY is the default
        }
    }
}
