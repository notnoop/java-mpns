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

package org.jboss.aerogear.windows.mpns.exceptions;

/**
 * Base class for all the exceptions thrown in Apns Library
 */
public abstract class MpnsException extends RuntimeException {
    private static final long serialVersionUID = -4756693306121825229L;

    public MpnsException()                      { super(); }
    public MpnsException(String message)        { super(message); }
    public MpnsException(Throwable cause)       { super(cause); }
    public MpnsException(String m, Throwable c) { super(m, c); }

}
