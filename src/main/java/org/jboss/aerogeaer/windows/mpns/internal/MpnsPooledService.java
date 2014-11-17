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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.jboss.aerogeaer.windows.mpns.MpnsDelegate;
import org.jboss.aerogeaer.windows.mpns.MpnsNotification;
import org.jboss.aerogeaer.windows.mpns.MpnsService;

import java.util.concurrent.ExecutorService;

public class MpnsPooledService extends AbstractMpnsService implements MpnsService {
    private final HttpClient httpClient;
    private final ExecutorService executor;
    private final MpnsDelegate delegate;

    public MpnsPooledService(HttpClient httpClient, ExecutorService executor, MpnsDelegate delegate) {
        this.httpClient = httpClient;
        this.executor = executor;
        this.delegate = delegate;
    }

    @Override
    protected void push(final HttpPost request, final MpnsNotification message) {
        executor.execute(new Runnable() {
            public void run() {
                try {
                    HttpResponse response = httpClient.execute(request);
                    Utilities.fireDelegate(message, response, delegate);
                    EntityUtils.consume(response.getEntity());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void stop() {
        super.stop();
        this.httpClient.getConnectionManager().shutdown();
        this.executor.shutdown();
    }

}
