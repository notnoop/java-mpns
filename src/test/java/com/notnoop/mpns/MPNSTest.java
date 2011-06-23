package com.notnoop.mpns;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Silly Tests
 */
public class MPNSTest {

    @Test
    public void testInstances() {
        assertThat(MPNS.newNotification(), is(MpnsNotificationBuilder.class));
        assertThat(MPNS.newService(), is(MpnsServiceBuilder.class));
    }

    @Test
    public void notificationShouldGetNewInstances() {
        assertNotSame(MPNS.newNotification(), MPNS.newNotification());
    }

    @Test
    public void newServiceGetNewInstances() {
        assertNotSame(MPNS.newService(), MPNS.newService());
    }
}
