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
package org.jboss.aerogear.windows.mpns.notifications;

import static org.jboss.aerogear.windows.mpns.internal.Utilities.xmlElement;
import static org.jboss.aerogear.windows.mpns.internal.Utilities.xmlElementClear;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.jboss.aerogear.windows.mpns.DeliveryClass;
import org.jboss.aerogear.windows.mpns.MpnsNotification;
import org.jboss.aerogear.windows.mpns.internal.Utilities;

public class FlipTileNotification implements MpnsNotification {
	
	private final Builder builder;
	
    private final List<? extends Entry<String, String>> headers;

    public FlipTileNotification(Builder builder, List<? extends Entry<String, String>> headers) {
    	this.builder = builder;
    	this.headers = headers;
    }

    public byte[] getRequestBody() {
    	return this.builder.toByteArray();
    }

    public List<? extends Entry<String, String>> getHttpHeaders() {
        return Collections.unmodifiableList(this.headers);
    }

    public static class Builder extends AbstractNotificationBuilder<Builder, FlipTileNotification> {
        
        private String backgroundImage, title, backBackgroundImage, backTitle, backContent;
        private int count;

    	private String smallBackgroundImage;
    	private String wideBackgroundImage;
    	private String wideBackBackgroundImage;
    	private String wideBackContent;
    	private String tileId;
    	private boolean isClear;

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
        
        public Builder smallBackgroundImage(String smallBackgroundImage) {
        	this.smallBackgroundImage = smallBackgroundImage;
        	return this;
        }

    	public Builder wideBackgroundImage( String wideBackgroundImage ) {
            this.wideBackgroundImage = wideBackgroundImage;
            return this;
        }

    	public Builder wideBackBackgroundImage( String wideBackBackgroundImage ) {
            this.wideBackBackgroundImage = wideBackBackgroundImage;
            return this;
        }

        public Builder wideBackContent( String wideBackContent ) {
            this.wideBackContent = wideBackContent;
            return this;
        }

        @Override
        protected int deliveryValueOf(DeliveryClass delivery) {
            return Utilities.getTileDelivery(delivery);
        }

        @Override
        public FlipTileNotification build() {
            return new FlipTileNotification(this, this.headers);
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
            sb.append(" Template=\"FlipTile\">");
            if( isClear ) {
            	sb.append(xmlElementClear("SmallBackgroundImage", smallBackgroundImage));
            } else {
            	sb.append(xmlElement("SmallBackgroundImage", smallBackgroundImage));
            }
            sb.append(xmlElementClear("WideBackgroundImage", wideBackgroundImage));
            sb.append(xmlElementClear("WideBackBackgroundImage", wideBackBackgroundImage));
            sb.append(xmlElementClear("WideBackContent", wideBackContent));
            sb.append(xmlElementClear("BackgroundImage", backgroundImage));
            sb.append(xmlElementClear("Count", ""+count));
            sb.append(xmlElementClear("Title", title));
            sb.append(xmlElementClear("BackBackgroundImage", backBackgroundImage));
            sb.append(xmlElementClear("BackTitle", backTitle));
            sb.append(xmlElementClear("BackContent", backContent));
            sb.append("</wp:Tile>");
            sb.append("</wp:Notification>");

            return Utilities.toUTF8(sb.toString());
        }
    }
}
