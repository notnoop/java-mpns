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
import org.jboss.aerogeaer.windows.mpns.MpnsNotification;
import org.jboss.aerogeaer.windows.mpns.MpnsService;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class MpnsQueuedService extends AbstractMpnsService implements MpnsService {

    private AbstractMpnsService service;
    private BlockingQueue<Pair<HttpPost, MpnsNotification>> queue;
    private AtomicBoolean started = new AtomicBoolean(false);

    public MpnsQueuedService(AbstractMpnsService service) {
        this.service = service;
        this.queue = new LinkedBlockingQueue<Pair<HttpPost, MpnsNotification>>();
    }

    @Override
    protected void push(final HttpPost request, MpnsNotification message) {
        if (!started.get()) {
            throw new IllegalStateException("Service hans't been started or was closed");
        }

        queue.add(Pair.of(request, message));
    }

    private Thread thread;
    private volatile boolean shouldContinue;

    public void start() {
        if (started.getAndSet(true)) {
            // Should we throw a runtime IllegalStateException here?
            return;
        }

        service.start();
        shouldContinue = true;
        thread = new Thread() {
            public void run() {
                while (shouldContinue) {
                    try {
                        Pair<HttpPost, MpnsNotification> post = queue.take();
                        service.push(post.getKey(), post.getValue());
                    } catch (InterruptedException e) {}
                }
            }
        };
        thread.start();
    }


    @Override
    public void stop() {
        started.set(false);
        shouldContinue = false;
        thread.interrupt();
        service.stop();
    }

}
