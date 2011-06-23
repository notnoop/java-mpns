package com.notnoop.mpns.internal;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import static com.notnoop.mpns.internal.Utilities.*;

public class UtilitiesTest {

    @Test
    public void ifNonNullHandlesNull() {
        assertThat(ifNonNull(null, "asdf"), is(""));
    }

    @Test
    public void isNonNullHandlesNonNull() {
        assertThat(ifNonNull("A", "asdf" + "fdsa"), is("asdffdsa"));
    }
}
