/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.camel.command;

/** Explicitly end a correlated activity (application session finished). */
public record EndCamelActivity(String activityId) implements CamelCommand {
}
