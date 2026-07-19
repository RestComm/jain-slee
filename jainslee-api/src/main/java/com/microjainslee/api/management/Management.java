/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.api.management;

/**
 * JAIN-SLEE 1.1 §14 — Management facility marker.
 * <p>
 * micro-jainslee deliberately does <strong>not</strong> expose management over
 * JMX/MBeans — that surface is native-image-hostile and has been dropped. The
 * concrete management surface is plain in-process ports plus the telemetry REST
 * API (see {@code jainslee-telemetry} / {@code jainslee-monitor}). The interface
 * is intentionally empty so downstream code can already take a {@code Management}
 * dependency and have it resolve; embedders should not rely on the marker
 * carrying any methods.
 *
 * @author Tran Nhan (nhanth87)
 */
public interface Management {
    // Management is exposed via ports + telemetry REST, not JMX/MBeans.
}
