/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

/**
 * 
 */
package org.mobicents.slee.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.slee.ActivityEndEvent;

/**
 * Annotation for a JAIN SLEE 1.1 {@link ActivityEndEvent} type, an alternative to the usage of generic {@link EventHandler}.
 * @author martins
 *
 */
@Documented
@Target(value={ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityEndEventHandler {

	/**
	 * indicates if the event is defined as initial, default is false
	 * @return
	 */
	boolean initialEvent() default false;
	
	/**
	 * defines the variables that should be included in the convergence name calculation performed by the SLEE when considering an initial event for a service's root SBB.
	 * @return
	 */
	InitialEventSelect[] initialEventSelect() default { InitialEventSelect.ActivityContext };
	
	/**
	 * contains the name of an initial event selector method defined in the SBB abstract class.
	 * @return
	 */
	String initialEventSelectorMethod() default "";
	
	/**
	 * 
	 * @return
	 */
	String eventResourceOption() default "";
	
	/**
	 * 
	 * @return
	 */
	boolean maskOnAttach() default false;
	
}
