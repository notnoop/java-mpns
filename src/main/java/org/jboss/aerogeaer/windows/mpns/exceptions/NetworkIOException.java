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

package org.jboss.aerogeaer.windows.mpns.exceptions;

import java.io.IOException;

/**
 * Thrown to indicate that that a network operation has failed:
 * (e.g. connectivity problems, domain cannot be found, network
 * dropped).
 */
public class NetworkIOException extends MpnsException {
    private static final long serialVersionUID = 3353516625486306533L;

    public NetworkIOException()                      { super(); }
    public NetworkIOException(String message)        { super(message); }
    public NetworkIOException(IOException cause)       { super(cause); }
    public NetworkIOException(String m, IOException c) { super(m, c); }

}
