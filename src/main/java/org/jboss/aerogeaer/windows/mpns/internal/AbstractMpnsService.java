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

package org.jboss.aerogeaer.windows.mpns.internal;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.jboss.aerogeaer.windows.mpns.MpnsNotification;
import org.jboss.aerogeaer.windows.mpns.MpnsService;
import org.jboss.aerogeaer.windows.mpns.exceptions.NetworkIOException;

import java.util.Collection;
import java.util.Map.Entry;

public abstract class AbstractMpnsService implements MpnsService {

    protected HttpPost postMessage(String subscriptionUri, byte[] requestBody,
            Collection<? extends Entry<String, String>> headers) {
        HttpPost method = new HttpPost(subscriptionUri);
        method.setEntity(new ByteArrayEntity(requestBody));

        for (Entry<String, String> header: headers) {
            method.addHeader(header.getKey(), header.getValue());
        }

        return method;
    }

    protected abstract void push(HttpPost request, MpnsNotification message);

    public void push(String subscriptionUri, String payload,
            Collection<? extends Entry<String, String>> headers)
            throws NetworkIOException {
        this.push(postMessage(subscriptionUri, Utilities.toUTF8(payload), headers), null);
    }

    public void push(String subscriptionUri, MpnsNotification message)
            throws NetworkIOException {
        this.push(postMessage(subscriptionUri, message.getRequestBody(),
                message.getHttpHeaders()), message);
    }

    public void start() {}

    public void stop() {}
}
