package com.capg.rechargenova.exception;

/*
 * ================================================================
 * AUTHOR: Sanjana
 * CLASS: NotificationNotFoundException
 * DESCRIPTION:
 * Custom exception thrown when a notification is not found
 * in the system. This helps in providing meaningful error
 * responses to the client.
 * ================================================================
 */
public class NotificationNotFoundException extends RuntimeException {

    public NotificationNotFoundException(String message) {
        super(message);
    }
}