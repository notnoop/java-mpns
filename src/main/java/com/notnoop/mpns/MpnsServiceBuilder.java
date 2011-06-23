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

import java.net.InetSocketAddress;
import java.net.Proxy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import com.notnoop.mpns.internal.*;

/**
 * The class is used to create instances of {@link MpnsService}.
 *
 * Note that this class is not synchronized.  If multiple threads access a
 * {@code MpnsServiceBuilder} instance concurrently, and at least on of the
 * threads modifies one of the attributes structurally, it must be
 * synchronized externally.
 *
 * Starting a new {@code MpnsService} is easy:
 *
 * <pre>
 *   MpnsService service = MPNS.newService()
 *                  .build()
 * </pre>
 */
public class MpnsServiceBuilder {
    private int pooledMax = 1;
    private ExecutorService executor = null;

    private boolean isQueued = false;
    private Proxy proxy = null;

    /**
     * Constructs a new instance of {@code MpnsServiceBuilder}
     */
    public MpnsServiceBuilder() { }

    /**
     * Specify the address of the SOCKS proxy the connection should
     * use.
     *
     * <p>Read the <a href="http://java.sun.com/javase/6/docs/technotes/guides/net/proxies.html">
     * Java Networking and Proxies</a> guide to understand the
     * proxies complexity.
     *
     * <p>Be aware that this method only handles SOCKS proxies, not
     * HTTPS proxies.  Use {@link #withProxy(Proxy)} instead.
     *
     * @param host  the hostname of the SOCKS proxy
     * @param port  the port of the SOCKS proxy server
     * @return  this
     */
    public MpnsServiceBuilder withSocksProxy(String host, int port) {
        Proxy proxy = new Proxy(Proxy.Type.SOCKS,
                new InetSocketAddress(host, port));
        return withProxy(proxy);
    }

    /**
     * Specify the proxy to be used to establish the connections
     * to Microsoft Servers
     *
     * <p>Read the <a href="http://java.sun.com/javase/6/docs/technotes/guides/net/proxies.html">
     * Java Networking and Proxies</a> guide to understand the
     * proxies complexity.
     *
     * @param proxy the proxy object to be used to create connections
     * @return  this
     */
    public MpnsServiceBuilder withProxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    /**
     * Constructs a pool of connections to the notification servers.
     *
     */
    public MpnsServiceBuilder asPool(int maxConnections) {
        return asPool(Executors.newFixedThreadPool(maxConnections), maxConnections);
    }

    /**
     * Constructs a pool of connections to the notification servers.
     *
     * Note: The maxConnections here is used as a hint to how many connections
     * get created.
     */
    public MpnsServiceBuilder asPool(ExecutorService executor, int maxConnections) {
        this.pooledMax = maxConnections;
        this.executor = executor;
        return this;
    }

    /**
     * Constructs a new thread with a processing queue to process
     * notification requests.
     *
     * @return  this
     */
    public MpnsServiceBuilder asQueued() {
        this.isQueued = true;
        return this;
    }

    /**
     * Returns a fully initialized instance of {@link MpnsService},
     * according to the requested settings.
     *
     * @return  a new instance of MpnsService
     */
    public MpnsService build() {
        checkInitialization();
        AbstractMpnsService service;

        if (pooledMax == 1) {
            HttpClient client = new DefaultHttpClient();
            service = new MpnsServiceImpl(client);
        } else {
            HttpClient client = new DefaultHttpClient(Utilities.poolManager(pooledMax));
            service = new MpnsPooledService(client, executor);
        }

        if (isQueued) {
            service = new MpnsQueuedService(service);
        }

        return service;
    }

    private void checkInitialization() {
        if (pooledMax != 1 && executor == null) {
            throw new IllegalStateException("Executor service is required for pooled connections");
        }
        if (this.proxy != null) {
            throw new RuntimeException("Proxies are not supported yet");
        }
    }
}
