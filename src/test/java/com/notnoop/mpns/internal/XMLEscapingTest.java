package com.notnoop.mpns.internal;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import static com.notnoop.mpns.internal.Utilities.escapeXml;

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
