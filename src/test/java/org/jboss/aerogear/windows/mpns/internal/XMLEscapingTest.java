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

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import static org.jboss.aerogear.windows.mpns.internal.Utilities.escapeXml;

public class XMLEscapingTest {

    @Test
    public void escapesNull() {
        assertThat(escapeXml(null), is((String)null));
    }

    @Test
    public void escapeEmpty() {
        assertThat(escapeXml(""), is(""));
    }

    @Test
    public void escapeNonEntities() {
        String text = "random;23$%^";
        assertThat(escapeXml(text), is(text));
    }

    @Test
    public void escapeAmpt() {
        assertThat(escapeXml("test&"), is("test&amp;"));
    }

    @Test
    public void escapeAllValues() {
        assertThat(escapeXml("AT&T called \"Johns's father\" > 2 hours ago < 5 years ago."),
                is("AT&amp;T called &quot;Johns&apos;s father&quot; &gt; 2 hours ago &lt; 5 years ago."));
    }
}
