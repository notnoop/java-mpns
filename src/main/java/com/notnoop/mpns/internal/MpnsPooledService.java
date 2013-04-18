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
package com.notnoop.mpns.internal;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import com.notnoop.mpns.MpnsDelegate;
import com.notnoop.mpns.MpnsNotification;
import com.notnoop.mpns.MpnsService;

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
            	HttpResponse response = null;
                try {
                    response = httpClient.execute(request);
                    Utilities.fireDelegate(message, response, delegate);
                } catch (Exception e) {
                	delegate.error(message, e);
                } finally {
                	//Release the connection
                    try
                    {
                    	if(response != null) {
                    		EntityUtils.consume(response.getEntity());
                    	}
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
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
