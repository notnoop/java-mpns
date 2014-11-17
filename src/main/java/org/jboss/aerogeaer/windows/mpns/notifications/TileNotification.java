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
package org.jboss.aerogeaer.windows.mpns.notifications;

import org.jboss.aerogeaer.windows.mpns.DeliveryClass;
import org.jboss.aerogeaer.windows.mpns.MpnsNotification;
import org.jboss.aerogeaer.windows.mpns.internal.Utilities;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import static org.jboss.aerogeaer.windows.mpns.internal.Utilities.xmlElement;

public class TileNotification implements MpnsNotification {
    private final Builder builder;

    private final List<? extends Entry<String, String>> headers;

    public TileNotification(Builder builder, List<? extends Entry<String, String>> headers) {
    	this.builder = builder;
    	this.headers = headers;
    }

    public byte[] getRequestBody() {
    	return this.builder.toByteArray();
    }

    public List<? extends Entry<String, String>> getHttpHeaders() {
        return Collections.unmodifiableList(this.headers);
    }
    
    public static class Builder extends AbstractNotificationBuilder<Builder, TileNotification> {
        private String backgroundImage, title, backBackgroundImage, backTitle, backContent;
        private int count;

        public Builder() {
            super("token");
            contentType(Utilities.XML_CONTENT_TYPE);
        }

        public Builder backgroundImage(String backgroundImage) {
            this.backgroundImage = backgroundImage;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder backBackgroundImage(String backBackgroundImage) {
            this.backBackgroundImage = backBackgroundImage;
            return this;
        }

        public Builder backTitle(String backTitle) {
            this.backTitle = backTitle;
            return this;
        }

        public Builder backContent(String backContent) {
            this.backContent = backContent;
            return this;
        }

        public Builder count(int count) {
            this.count = count;
            return this;
        }

        @Override
        protected int deliveryValueOf(DeliveryClass delivery) {
            return Utilities.getTileDelivery(delivery);
        }

        @Override
        public TileNotification build() {
            return new TileNotification(this, headers);
        }
        
        public byte[] toByteArray() {
            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            sb.append("<wp:Notification xmlns:wp=\"WPNotification\">");
            sb.append("<wp:Tile>");
            sb.append(xmlElement("BackgroundImage", backgroundImage));
            sb.append(xmlElement("Count", ""+count));
            sb.append(xmlElement("Title", title));
            sb.append(xmlElement("BackBackgroundImage", backBackgroundImage));
            sb.append(xmlElement("BackTitle", backTitle));
            sb.append(xmlElement("BackContent", backContent));
            sb.append("</wp:Tile>");
            sb.append("</wp:Notification>");

            return Utilities.toUTF8(sb.toString());
        }
    }
}
