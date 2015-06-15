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

package org.jboss.aerogear.windows.mpns;

import org.jboss.aerogear.windows.mpns.exceptions.NetworkIOException;

import java.util.Collection;
import java.util.Map;

/**
 * Represents the connection and interface to the Microsoft MPNS servers.
 *
 * The service is created by {@link MpnsServiceBuilder} like:
 *
 * <pre>
 *   MpnsService service = MPNS.newService().build()
 * </pre>
 */
public interface MpnsService {

    /**
     * Sends a push notification with the provided {@code payload} to the
     * iPhone of {@code deviceToken}.
     *
     * The payload needs to be a valid JSON object, otherwise it may fail
     * silently.  It is recommended to use {@link MpnsServiceBuilder} to create
     * one.
     *
     * @param subscriptionUri   the destination
     * @param payload       The payload message
     * @throws NetworkIOException if a network error occurred while
     *      attempting to send the message
     */
    void push(String subscriptionUri, String payload,
            Collection<? extends Map.Entry<String, String>> headers)
        throws NetworkIOException;

    /**
     * Sends the provided notification {@code message} to the desired
     * destination.
     * @throws NetworkIOException if a network error occurred while
     *      attempting to send the message
     */
    void push(String subscriptionUri, MpnsNotification message)
        throws NetworkIOException;
    
    /**
     * Starts the service.
     *
     * The underlying implementation may prepare its connections or
     * data structures to be able to send the messages.
     *
     * This method is a blocking call, even if the service represents
     * a Non-blocking push service.  Once the service is returned, it is ready
     * to accept push requests.
     *
     * @throws NetworkIOException if a network error occurred while
     *      starting the service
     */
    void start();

    /**
     * Stops the service and frees any allocated resources it created for this
     * service.
     *
     * The underlying implementation should close all connections it created,
     * and possibly stop any threads as well.
     */
    void stop();
}
