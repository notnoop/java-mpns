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

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.jboss.aerogear.windows.mpns.MpnsDelegate;
import org.jboss.aerogear.windows.mpns.MpnsNotification;
import org.jboss.aerogear.windows.mpns.MpnsService;
import org.jboss.aerogear.windows.mpns.exceptions.NetworkIOException;

import java.io.IOException;

public class MpnsServiceImpl extends AbstractMpnsService implements MpnsService {
    private final HttpClient httpClient;
    private final MpnsDelegate delegate;

    public MpnsServiceImpl(HttpClient httpClient, MpnsDelegate delegate) {
        this.httpClient = httpClient;
        this.delegate = delegate;
    }

    @Override
    protected void push(HttpPost request, MpnsNotification message) {
        try {
            HttpResponse response = httpClient.execute(request);
            Utilities.fireDelegate(message, response, delegate);
            EntityUtils.consume(response.getEntity());
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new NetworkIOException(e);
        }
    }

    public void stop() {
        this.httpClient.getConnectionManager().shutdown();
    }

}
