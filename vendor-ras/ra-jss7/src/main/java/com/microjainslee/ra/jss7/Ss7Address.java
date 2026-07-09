/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.jss7;

/**
 * Immutable SS7 address (Global Title + Point Code + SSN).
 */
public record Ss7Address(
        String globalTitle,
        int translationType,
        int numberingPlan,
        int natureOfAddress,
        int pointCode,
        int subSystemNumber
) {
    public static Ss7Address of(String gt, int ssn) {
        return new Ss7Address(gt, 0, 1, 4, 0, ssn);
    }

    public static Ss7Address of(String gt, int ssn, int pc) {
        return new Ss7Address(gt, 0, 1, 4, pc, ssn);
    }

    @Override
    public String toString() {
        return String.format("GT=%s SSN=%d PC=%d", globalTitle, subSystemNumber, pointCode);
    }
}
