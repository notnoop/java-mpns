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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.notnoop.mpns.exceptions.InvalidSSLConfig;
import com.notnoop.mpns.exceptions.RuntimeIOException;
import com.notnoop.mpns.internal.AbstractMpnsService;
import com.notnoop.mpns.internal.MpnsPooledService;
import com.notnoop.mpns.internal.MpnsQueuedService;
import com.notnoop.mpns.internal.MpnsServiceImpl;
import com.notnoop.mpns.internal.Utilities;

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

    private SSLContext sslContext;
	
    private int pooledMax = 1;
    private ExecutorService executor = null;

    private boolean isQueued = false;
    private HttpHost proxy = null;
    private HttpClient httpClient = null;
    private int timeout = -1;

    private MpnsDelegate delegate;

    /**
     * Constructs a new instance of {@code MpnsServiceBuilder}
     */
    public MpnsServiceBuilder() { }
    
    public MpnsServiceBuilder withCert(String fileName, String password, String ksType, String kAlgo)
    		throws RuntimeIOException, InvalidSSLConfig 
    {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(fileName);
            return withCert(stream, password, ksType, kAlgo);
        } catch (FileNotFoundException e) {
            throw new RuntimeIOException(e);
        } finally {
            Utilities.close(stream);
        }
    }
    
    public MpnsServiceBuilder withCert(InputStream stream, String password, String ksType, String kAlgo)
    		throws InvalidSSLConfig 
    {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Passwords must be specified." +
                    "Oracle Java SDK does not support passwordless p12 certificates");
        }

        return withSSLContext(Utilities.newSSLContext(stream, password,ksType, kAlgo));
    }
    
    public MpnsServiceBuilder withSSLContext(SSLContext sslContext) {
        this.sslContext = sslContext;
        return this;
    }

    /**
     * Specify the address of the HTTP proxy the connection should
     * use.
     *
     * <p>Read the <a href="http://java.sun.com/javase/6/docs/technotes/guides/net/proxies.html">
     * Java Networking and Proxies</a> guide to understand the
     * proxies complexity.
     *
     * @param host  the hostname of the HTTP proxy
     * @param port  the port of the HTTP proxy server
     * @return  this
     */
    public MpnsServiceBuilder withHttpProxy(String host, int port) {
        this.proxy = new HttpHost(host, port);
        return this;
    }

    // TODO: Support Proxy again
//    /**
//     * Specify the proxy to be used to establish the connections
//     * to Microsoft Servers
//     *
//     * <p>Read the <a href="http://java.sun.com/javase/6/docs/technotes/guides/net/proxies.html">
//     * Java Networking and Proxies</a> guide to understand the
//     * proxies complexity.
//     *
//     * @param proxy the proxy object to be used to create connections
//     * @return  this
//     */
//    public MpnsServiceBuilder withProxy(Proxy proxy) {
//        this.proxy = proxy;
//        return this;
//    }

    /**
     * Sets the HttpClient instance along with any configuration
     *
     * NOTE: This is an advanced option that should be probably be used as a
     * last resort.
     *
     * @param httpClient    the httpClient to be used
     * @return this
     */
    public MpnsServiceBuilder withHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
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
     * Sets the timeout for the connection
     *
     * @param   timeout     the time out period in millis
     * @return this
     */
    public MpnsServiceBuilder timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public MpnsServiceBuilder delegate(MpnsDelegate delegate) {
        this.delegate = delegate;
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

        // Client Configuration
        HttpClient client;
        if (httpClient != null) {
            client = httpClient;
        } else if (pooledMax == 1) {
        	client = new DefaultHttpClient();
        	
            SSLSocketFactory socketFactory = new SSLSocketFactory(sslContext);
            Scheme sch = new Scheme("https", 443, socketFactory);
            client.getConnectionManager().getSchemeRegistry().register(sch);
        } else {
            client = new DefaultHttpClient(Utilities.poolManager(pooledMax));
            
            SSLSocketFactory socketFactory = new SSLSocketFactory(sslContext);
            Scheme sch = new Scheme("https", 443, socketFactory);
            client.getConnectionManager().getSchemeRegistry().register(sch);
        }

        if (proxy != null) {
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }

        if (timeout > 0) {
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, timeout);
            HttpConnectionParams.setSoTimeout(params, timeout);
        }

        // Configure service
        AbstractMpnsService service;
        if (pooledMax == 1) {
            service = new MpnsServiceImpl(client, delegate);
        } else {
            service = new MpnsPooledService(client, executor, delegate);
        }

        if (isQueued) {
            service = new MpnsQueuedService(service);
        }

        service.start();
        return service;
    }

    private void checkInitialization() {
        if (pooledMax != 1 && executor == null) {
            throw new IllegalStateException("Executor service is required for pooled connections");
        }
    }
}
