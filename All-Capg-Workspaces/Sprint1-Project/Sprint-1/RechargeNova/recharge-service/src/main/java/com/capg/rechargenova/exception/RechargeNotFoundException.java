package com.capg.rechargenova.exception;


/*
 * ================================================================
 * AUTHOR: Sanjana
 * CLASS: RechargeNotFoundException
 * DESCRIPTION:
 * Custom exception thrown when a recharge is not found.
 * ================================================================
 */
public class RechargeNotFoundException extends RuntimeException {

    public RechargeNotFoundException(String message) {
        super(message);
    }
}