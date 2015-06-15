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

package org.jboss.aerogear.windows.mpns.util.matches;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.aerogear.windows.mpns.internal.Utilities;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

public class IsValidXml extends BaseMatcher<Object> {

    public boolean matches(Object obj) {
        final byte[] in;
        if (obj instanceof String) {
            in = Utilities.toUTF8((String) obj);
        } else if (obj instanceof byte[]) {
            in = (byte[])obj;
        } else {
            return false;
        }

        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(new ByteArrayInputStream(in), new DefaultHandler());

            return true;
        } catch (Exception e) {
            try {
                System.out.println(new String(in, "UTF8"));
            } catch (UnsupportedEncodingException e1) {
                throw new RuntimeException("No UTF8 encoded bytes", e1);
            }
            return false;
        }
    }

    public void describeTo(Description arg0) {
    }

    public static IsValidXml isValidXml() {
        return new IsValidXml();
    }
}
