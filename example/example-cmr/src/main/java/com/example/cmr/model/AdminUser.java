/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.model;

/**
 * An admin account allowed into the {@code /admin} back office.
 *
 * @param username     login name
 * @param passwordHash salted hash (never the raw password)
 * @param displayName  shown in the dashboard header
 */
public record AdminUser(String username, String passwordHash, String displayName) {
}
