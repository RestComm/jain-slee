/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.api.annotations;

import java.lang.annotation.*;

/**
 * Inject a Resource Adaptor {@link com.microjainslee.api.RaCommandPort} into
 * an SBB field.
 *
 * <p>
 * The container resolves the RA by the annotation's {@link #name()} value.
 * When {@code name} is left at its default (empty string), the container
 * infers the RA name from the field type or deployment context.
 *
 * <pre>{@code
 * @InjectRa(name = "ussd-gateway")
 * private RaCommandPort ussdRa;
 * }</pre>
 *
 * @see com.microjainslee.api.RaCommandPort
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface InjectRa {

    /**
     * @return the logical RA entity name, or {@code ""} to let the
     *         container infer the binding
     */
    String name() default "";
}
