/*
 * micro-jainslee 1.1.0 -- example application (example-spring)
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.example.ussddemo.spring;

import com.microjainslee.api.Profile;
import com.microjainslee.api.ProfileID;

/**
 * Subscriber profile with msisdn and tier for USSD menu routing.
 */
public final class UssdSubscriberProfile implements Profile {

    private String msisdn;
    private String tier;
    private ProfileID profileId;

    public String getMsisdn() { return msisdn; }
    public void setMsisdn(String msisdn) { this.msisdn = msisdn; }
    public String getTier() { return tier; }
    public void setTier(String tier) { this.tier = tier; }

    @Override
    public ProfileID getProfileID() {
        return profileId;
    }

    void setProfileId(ProfileID profileId) {
        this.profileId = profileId;
    }

    @Override
    public Object getCmpField(String fieldName) {
        return switch (fieldName) {
            case "msisdn" -> msisdn;
            case "tier" -> tier;
            default -> null;
        };
    }

    @Override
    public void setCmpField(String fieldName, Object value) {
        switch (fieldName) {
            case "msisdn" -> {
                if (value instanceof String s) msisdn = s;
            }
            case "tier" -> {
                if (value instanceof String s) tier = s;
            }
            default -> { /* ignore unknown */ }
        }
    }

    @Override
    public String[] getCmpFieldNames() {
        return new String[]{"msisdn", "tier"};
    }
}
