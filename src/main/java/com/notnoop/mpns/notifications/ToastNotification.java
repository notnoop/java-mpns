/*
 * Copyright 2011, Mahmood Ali.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following disclaimer
 *     in the documentation and/or other materials provided with the
 *     distribution.
 *   * Neither the name of Mahmood Ali. nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
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
package com.notnoop.mpns.notifications;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import com.notnoop.mpns.DeliveryClass;
import com.notnoop.mpns.MpnsNotification;
import com.notnoop.mpns.internal.Utilities;

import static com.notnoop.mpns.internal.Utilities.ifNonNull;
import static com.notnoop.mpns.internal.Utilities.escapeXml;

public class ToastNotification implements MpnsNotification {
    private final String title;
    private final String subtitle;
    private final String parameter;

    private final List<? extends Entry<String, String>> headers;

    public ToastNotification(String title, String subtitle, String parameter,
            List<? extends Entry<String, String>> headers) {
        this.title = title;
        this.subtitle = subtitle;
        this.parameter = parameter;

        this.headers = headers;
    }

    public byte[] getRequestBody() {
        String message =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
            "<wp:Notification xmlns:wp=\"WPNotification\">" +
               "<wp:Toast>" +
                    ifNonNull(title, "<wp:Text1>" + escapeXml(title) + "</wp:Text1>") +
                    ifNonNull(subtitle, "<wp:Text2>" + escapeXml(subtitle) + "</wp:Text2>") +
                    ifNonNull(parameter, "<wp:Param>" + escapeXml(parameter) + "</wp:Param>") +
               "</wp:Toast> " +
            "</wp:Notification>";

        return Utilities.toUTF8(message);
    }

    public List<? extends Entry<String, String>> getHttpHeaders() {
        return Collections.unmodifiableList(this.headers);
    }

    public static class Builder extends AbstractNotificationBuilder<Builder, ToastNotification> {
        private String title, subtitle, parameter;

        public Builder() {
            super("toast");
            contentType(Utilities.XML_CONTENT_TYPE);
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public Builder parameter(String parameter) {
            this.parameter = parameter;
            return this;
        }

        @Override
        protected int deliveryValueOf(DeliveryClass delivery) {
            switch (delivery) {
            case IMMEDIATELY:   return 2;
            case WITHIN_450:    return 12;
            case WITHIN_900:    return 22;
            default:
                throw new AssertionError("Unknown Value: " + delivery);
            }
        }

        @Override
        public ToastNotification build() {
            return new ToastNotification(title, subtitle, parameter, headers);
        }
    }
}
