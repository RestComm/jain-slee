/*
 * micro-jainslee 1.1.0 -- example application (example-quarkus)
 */

package com.example.ussddemo.quarkus.bootstrap;

import com.microjainslee.api.ProfileAbstractCmp;
import com.microjainslee.api.ProfileAccessorInvoker;

import java.lang.reflect.Method;

/**
 * Subscriber profile seeded at bootstrap. Tier stored as String (GOLD/SILVER/STANDARD).
 */
public final class UssdSubscriberProfile extends ProfileAbstractCmp {

    public static final String TABLE_NAME = "ussdSubscribers";

    public String getMsisdn() {
        return (String) ProfileAccessorInvoker.getValue(this, findGetter("msisdn"));
    }

    public void setMsisdn(String msisdn) {
        ProfileAccessorInvoker.setValue(this, findSetter("msisdn", String.class), msisdn);
    }

    public String getTier() {
        String v = (String) ProfileAccessorInvoker.getValue(this, findGetter("tier"));
        return v == null ? "STANDARD" : v;
    }

    public void setTier(String tier) {
        ProfileAccessorInvoker.setValue(this, findSetter("tier", String.class), tier);
    }

    private static Method findGetter(String field) {
        String name = "get" + Character.toUpperCase(field.charAt(0)) + field.substring(1);
        try {
            return UssdSubscriberProfile.class.getDeclaredMethod(name);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("no getter for " + field, e);
        }
    }

    private static Method findSetter(String field, Class<?> type) {
        String name = "set" + Character.toUpperCase(field.charAt(0)) + field.substring(1);
        try {
            return UssdSubscriberProfile.class.getDeclaredMethod(name, type);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("no setter for " + field, e);
        }
    }
}
