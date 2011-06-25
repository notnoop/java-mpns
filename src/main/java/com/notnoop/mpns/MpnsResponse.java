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

/**
 * Represents the logical response of MpnsService
 */
public enum MpnsResponse {
    /*
     * The notification request was accepted and queued for delivery.
     */
    RECEIVED(200, "Received", "Connected", "Active", true, false),

    /**
     * The notification request was accepted and queued for delivery. However,
     * the device is temporarily disconnected.
     */
    QUEUED(200, "Received", "Temporarily Disconnected", "Active", true, false),

    /**
     * Queue overflow. The web service should re-send the notification later.
     * A best practice is to use an exponential backoff algorithm in minute
     * increments.
     */
    QUEUE_FULL(200, "QueueFull", null, "Active", false, true),

    /**
     * The push notification was received and dropped by the Push Notification
     * Service. The Suppressed status can occur if the notification channel
     * was configured to suppress push notifications for a particular push
     * notification class.
     */
    SUPPRESSED(200, "Suppressed", null, "Active", false, false),

    /**
     * This error occurs when the web service sends a notification request
     * with a bad XML document or malformed notification URI.
     */
    BAD_REQUEST(400, null, null, null, false, false),

    /**
     * Sending this notification is unauthorized. This error can occur for one
     * of the following reasons:
     */
    UNAUTHORIZED(401, null, null, null, false, false),

    /**
     * The subscription is invalid and is not present on the Push Notification
     * Service. The web service should stop sending new notifications to this
     * subscription, and drop the subscription state for its corresponding
     * application session.
     */
    EXPIRED(404, "Dropped", null, "Expired", false, false),

    /**
     * Invalid method (PUT, DELETE, CREATE). Only POST is allowed when sending
     * a notification request.
     */
    METHOD_NOT_ALLOWED(405, null, null, null, false, false),

    /**
     * This error occurs when an unauthenticated web service has reached the
     * per day throttling limit for a subscription. The web service can try to
     * re-send the push notification every one hour after receiving this
     * error. The web service may need to wait up to 24 hours before normal
     * notification flow will resume.
     */
    OVER_LIMIT(406, "Dropped", null, "Active", false, true),

    /**
     * The device is in an inactive state. The web service may re-attempt
     * sending the request one time per hour at maximum after receiving this
     * error. If the web service violates the maximum of one re-attempt per
     * hour, the Push Notification Service will de-register or permanently
     * block the web service.
     */
    INACTIVATE_STATE(412, "Dropped", "Inactive", null, false, true),

    /**
     * The Push Notification Service is unable to process the request. The web
     * service should re-send the notification later. A best practice is to
     * use an exponential backoff algorithm in minute increments.
     */
    SERVICE_UNAVAILABLE(503, null, null, null, false, true);

    //// Response Code,NotificationStatus,DeviceConnectionStatus,SubscriptionStatus,Comments
    private final int responseCode;
    private final String notificationStatus;
    private final String deviceConnectionStatus;
    private final String subscriptionStatus;

    private final boolean success;
    private final boolean shouldRetry;

    MpnsResponse(int responseCode, String notificationStatus,
            String deviceConnectionStatus,
            String subscriptionStatus,
            boolean success,
            boolean requiresRetry) {
        this.responseCode = responseCode;
        this.notificationStatus = notificationStatus;
        this.deviceConnectionStatus = deviceConnectionStatus;
        this.subscriptionStatus = subscriptionStatus;
        this.success = success;
        this.shouldRetry = requiresRetry;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getNotificationStatus() {
        return notificationStatus;
    }

    public String getDeviceConnectionStatus() {
        return deviceConnectionStatus;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public boolean isSuccessful() {
        return success;
    }

    public boolean shouldRetry() {
        return shouldRetry;
    }
}
