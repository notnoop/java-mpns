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
 * 
 * Contributed by vhong
 */
package com.notnoop.mpns.notifications;

import static com.notnoop.mpns.internal.Utilities.xmlElement;
import static com.notnoop.mpns.internal.Utilities.xmlElementClear;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import com.notnoop.mpns.DeliveryClass;
import com.notnoop.mpns.MpnsNotification;
import com.notnoop.mpns.internal.Utilities;

public class IconicTileNotification implements MpnsNotification {
	
	private final Builder builder;
	
    private final List<? extends Entry<String, String>> headers;

    public IconicTileNotification(Builder builder, List<? extends Entry<String, String>> headers) {
    	this.builder = builder;
    	this.headers = headers;
    }

    public byte[] getRequestBody() {
    	return this.builder.toByteArray();
    }

    public List<? extends Entry<String, String>> getHttpHeaders() {
        return Collections.unmodifiableList(this.headers);
    }

    public static class Builder extends AbstractNotificationBuilder<Builder, IconicTileNotification> {
        
        private String tileId;
        
        private boolean isClear;
        
        private String smallIconImage;
        private String iconImage;
        private String wideContent1;
        private String wideContent2;
        private String wideContent3;
        private int count;
        private String title;
        private String backgroundColor;

        public Builder() {
            super("token");
            contentType(Utilities.XML_CONTENT_TYPE);
        }
        
        public Builder tileId(String tileId) {
        	this.tileId = tileId;
        	return this;
        }
        
        public Builder isClear(boolean clear) {
        	this.isClear = clear;
        	return this;
        }

        public Builder smallIconImage(String smallIconImage) {
            this.smallIconImage = smallIconImage;
            return this;
        }

        public Builder iconImage(String iconImage) {
            this.iconImage = iconImage;
            return this;
        }

        public Builder wideContent1(String wideContent1) {
            this.wideContent1 = wideContent1;
            return this;
        }

        public Builder wideContent2(String wideContent2) {
            this.wideContent2 = wideContent2;
            return this;
        }

        public Builder wideContent3(String wideContent3) {
            this.wideContent3 = wideContent3;
            return this;
        }

        public Builder count(int count) {
            this.count = count;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder backgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        @Override
        protected int deliveryValueOf(DeliveryClass delivery) {
            return Utilities.getTileDelivery(delivery);
        }

        @Override
        public IconicTileNotification build() {
            return new IconicTileNotification(this, this.headers);
        }
        
        public byte[] toByteArray() {
            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            sb.append("<wp:Notification xmlns:wp=\"WPNotification\">");
            sb.append("<wp:Tile");
            if( tileId != null ) {
                sb.append(" Id=\"");
                sb.append(tileId);
                sb.append("\"");
            }
            sb.append(" Template=\"IconicTile\">");
            if( isClear ) {
            	sb.append(xmlElementClear("SmallIconImage", smallIconImage));
            } else {
            	sb.append(xmlElement("SmallIconImage", smallIconImage));
            }
            sb.append(xmlElementClear("IconImage", iconImage));
            sb.append(xmlElementClear("WideContent1", wideContent1));
            sb.append(xmlElementClear("WideContent2", wideContent2));
            sb.append(xmlElementClear("WideContent3", wideContent3));
            sb.append(xmlElementClear("Count", ""+count));
            sb.append(xmlElementClear("Title", title));
            sb.append(xmlElementClear("BackgroundColor", backgroundColor));
            sb.append("</wp:Tile>");
            sb.append("</wp:Notification>");

            return Utilities.toUTF8(sb.toString());
        }
    }
}

