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
package com.notnoop.mpns;

import com.notnoop.mpns.notifications.IconicTileNotification;
import com.notnoop.mpns.notifications.RawNotification;
import com.notnoop.mpns.notifications.TileNotification;
import com.notnoop.mpns.notifications.ToastNotification;

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
     * Sets the notification type to a Toast notification
     *
     * @return a toast notification builder
     */
    public ToastNotification.Builder toast() {
        return new ToastNotification.Builder();
    }
    
    /**
     * Sets the notification type to a Iconic Tile notification
     *
     * @return a iconic tile notification builder
     */
    public IconicTileNotification.Builder iconicTile() {
        return new IconicTileNotification.Builder();
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
