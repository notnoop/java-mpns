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

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.notnoop.mpns.DeliveryClass;
import com.notnoop.mpns.internal.Pair;

@SuppressWarnings("unchecked")
abstract class AbstractNotificationBuilder<A extends AbstractNotificationBuilder<A, B>, B> {
    protected List<Entry<String, String>> headers = new ArrayList<Entry<String, String>>();
    
    public A messageId(String messageId) {
        this.headers.add(Pair.of("X-MessageId", messageId));
        return (A)this;
    }

    public A notificationClass(DeliveryClass delivery) {
        this.headers.add(Pair.of("X-NotificationClass", String.valueOf(deliveryValueOf(delivery))));
        return (A)this;
    }

    public A notificationType(String type) {
        this.headers.add(Pair.of("X-WindowsPhone-Target", type));
        return (A)this;
    }

    public A callbackUri(String callbackUri) {
        this.headers.add(Pair.of("X-CallbackURI", callbackUri));
        return (A)this;
    }

    protected abstract int deliveryValueOf(DeliveryClass delivery);
    
    protected abstract B build();
}
