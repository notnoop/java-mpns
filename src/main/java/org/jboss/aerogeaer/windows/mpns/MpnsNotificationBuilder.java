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

package org.jboss.aerogeaer.windows.mpns;

import org.jboss.aerogeaer.windows.mpns.notifications.CycleTileNotification;
import org.jboss.aerogeaer.windows.mpns.notifications.FlipTileNotification;
import org.jboss.aerogeaer.windows.mpns.notifications.IconicTileNotification;
import org.jboss.aerogeaer.windows.mpns.notifications.RawNotification;
import org.jboss.aerogeaer.windows.mpns.notifications.TileNotification;
import org.jboss.aerogeaer.windows.mpns.notifications.ToastNotification;

/**
 * Represents a builder for constructing the notifications requests,
 * as specified by
 * <a href="http://msdn.microsoft.com/en-us/library/ff402558(v=vs.92).aspx">
 * Microsoft Push Notification Guide</a>:
 *
 */
public class MpnsNotificationBuilder {
    public MpnsNotificationBuilder() {}

    /**
     * Sets the notification type to a Tile notification
     *
     * @return  a tile notification builder
     */
    public TileNotification.Builder tile() {
        return new TileNotification.Builder();
    }
    
    /**
     * Windows 8: Flip Tile
     */
    public FlipTileNotification.Builder flipTile() {
    	return new FlipTileNotification.Builder();
    }
    
    /**
     * Windows 8: Iconic Tile
     */
    public IconicTileNotification.Builder iconicTile() {
    	return new IconicTileNotification.Builder();
    }
    
    /**
     * Windows 8: Cycle Tile
     */
    public CycleTileNotification.Builder cycleTile() {
    	return new CycleTileNotification.Builder();
    }

    /**
     * Sets the notification type to a Toast notification
     *
     * @return a toast notification builder
     */
    public ToastNotification.Builder toast() {
        return new ToastNotification.Builder();
    }

    /**
     * Sets the notification type to a Raw notification
     *
     * @return  a row notification builder
     */
    public RawNotification.Builder raw() {
        return new RawNotification.Builder();
    }
}
