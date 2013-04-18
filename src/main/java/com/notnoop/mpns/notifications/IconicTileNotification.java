package com.notnoop.mpns.notifications;

import static com.notnoop.mpns.internal.Utilities.escapeXml;
import static com.notnoop.mpns.internal.Utilities.ifNonNull;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import com.notnoop.mpns.DeliveryClass;
import com.notnoop.mpns.MpnsNotification;
import com.notnoop.mpns.internal.Utilities;

public class IconicTileNotification implements MpnsNotification {
	
	private final String tileId;
    private final String title;
    private final int count;

    private final String backgroundColor;
    private final String iconImage;
    private final String smallIconImage;
    private final String firstRowOfContent;
    private final String secondRowOfContent;
    private final String thirdRowOfContent;
    
    private final boolean isclear;

    private final List<? extends Entry<String, String>> headers;

    public IconicTileNotification(String tileId, String title, int count, 
    		String backgroundColor, String iconImage, String smallIconImage,
    		String firstRowOfContent, String secondRowOfContent, 
    		String thirdRowOfContent, boolean isclear,
            List<? extends Entry<String, String>> headers) {
    	this.tileId = tileId;
        this.title = title;
        this.count = count;

        this.backgroundColor = backgroundColor;
        this.iconImage = iconImage;
        this.smallIconImage = smallIconImage;
        this.firstRowOfContent = firstRowOfContent;
        this.secondRowOfContent = secondRowOfContent;
        this.thirdRowOfContent = thirdRowOfContent;
        
        this.isclear = isclear;

        this.headers = headers;
    }

    public byte[] getRequestBody() {
        String tileMessage;
        if(isclear) {
        	tileMessage =
        			"<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
        		            "<wp:Notification xmlns:wp=\"WPNotification\" Version=\"2.0\">" +
        		            "<wp:Tile Id=\""+tileId+"\" Template=\"IconicTile\">" +
        		            	ifNonNull(smallIconImage, "<wp:SmallIconImage>" + escapeXml(smallIconImage) + "</wp:SmallIconImage>") +
        		            	ifNonNull(iconImage, "<wp:IconImage>" + escapeXml(iconImage) + "</wp:IconImage>") +
        		            	"<wp:WideContent1 Action=\"Clear\"></wp:WideContent1>" +
        		            	"<wp:WideContent2 Action=\"Clear\"></wp:WideContent2>" +
        		            	"<wp:WideContent3 Action=\"Clear\"></wp:WideContent3>" +
        		            	ifNonNull(count, "<wp:Count Action=\"Clear\">" + count + "</wp:Count>") +
        		            	ifNonNull(title, "<wp:Title>" + escapeXml(title) + "</wp:Title>") +
        		            	ifNonNull(backgroundColor, "<wp:BackgroundColor>" + escapeXml(backgroundColor) + "</wp:BackgroundColor>") +
        		            "</wp:Tile> " +
        		            "</wp:Notification>";
        } else {
        	tileMessage =
		            "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
		            "<wp:Notification xmlns:wp=\"WPNotification\" Version=\"2.0\">" +
		            "<wp:Tile Id=\""+tileId+"\" Template=\"IconicTile\">" +
		            	ifNonNull(smallIconImage, "<wp:SmallIconImage>" + escapeXml(smallIconImage) + "</wp:SmallIconImage>") +
		            	ifNonNull(iconImage, "<wp:IconImage>" + escapeXml(iconImage) + "</wp:IconImage>") +
		            	ifNonNull(firstRowOfContent, "<wp:WideContent1>" + escapeXml(firstRowOfContent) + "</wp:WideContent1>") +
		            	ifNonNull(secondRowOfContent, "<wp:WideContent2>" + escapeXml(secondRowOfContent) + "</wp:WideContent2>") +
		            	ifNonNull(thirdRowOfContent, "<wp:WideContent3>" + escapeXml(thirdRowOfContent) + "</wp:WideContent3>") +
		            	ifNonNull(count, "<wp:Count>" + count + "</wp:Count>") +
		            	ifNonNull(title, "<wp:Title>" + escapeXml(title) + "</wp:Title>") +
		            	ifNonNull(backgroundColor, "<wp:BackgroundColor>" + escapeXml(backgroundColor) + "</wp:BackgroundColor>") +
		            "</wp:Tile> " +
		            "</wp:Notification>";
        }

        return Utilities.toUTF8(tileMessage);
    }

    public List<? extends Entry<String, String>> getHttpHeaders() {
        return Collections.unmodifiableList(this.headers);
    }

    public static class Builder extends AbstractNotificationBuilder<Builder, IconicTileNotification> {
        private String tileId, title, backgroundColor, iconImage, smallIconImage, firstRowOfContent, secondRowOfContent, thirdRowOfContent;
        private int count;
        private boolean isClear = false;

        public Builder() {
            super("token"); // TODO: Check whether it is "tile"
            contentType(Utilities.XML_CONTENT_TYPE);
        }

        public Builder backgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder tileId(String tileId) {
            this.tileId = tileId;
            return this;
        }

        public Builder iconImage(String iconImage) {
            this.iconImage = iconImage;
            return this;
        }

        public Builder firstRowOfContent(String firstRowOfContent) {
            this.firstRowOfContent = firstRowOfContent;
            return this;
        }
        
        public Builder secondRowOfContent(String secondRowOfContent) {
            this.secondRowOfContent = secondRowOfContent;
            return this;
        }
        
        public Builder thirdRowOfContent(String thirdRowOfContent) {
            this.thirdRowOfContent = thirdRowOfContent;
            return this;
        }
        
        public Builder smallIconImage(String smallIconImage) {
            this.smallIconImage = smallIconImage;
            return this;
        }
        
        public Builder clear(boolean clear) {
            this.isClear = clear;
            return this;
        }

        public Builder count(int count) {
            this.count = count;
            return this;
        }

        @Override
        protected int deliveryValueOf(DeliveryClass delivery) {
            switch (delivery) {
            case IMMEDIATELY:   return 1;
            case WITHIN_450:    return 11;
            case WITHIN_900:    return 21;
            default:
                throw new AssertionError("Unknown Value: " + delivery);
            }
        }

        @Override
        public IconicTileNotification build() {
            return new IconicTileNotification(tileId, title, count,
                    backgroundColor, iconImage, smallIconImage, firstRowOfContent,
                    secondRowOfContent, thirdRowOfContent, isClear, headers);
        }
    }

}
