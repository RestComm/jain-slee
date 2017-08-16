/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
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

import javax.slee.ActivityContextInterface;

/**
 * Annotation for a JAIN SLEE 1.1 RA Type.
 * @author martins
 *
 */
@Documented
@Target(value={ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceAdaptorType {

	/**
	 * the RA Type name
	 * @return
	 */
	String name();
	
	/**
	 * the RA Type vendor
	 * @return
	 */
	String vendor();
	
	/**
	 * the RA Type version
	 * @return
	 */
	String version();
	
	/**
	 * 
	 * @return
	 */
	Class<?>[] activities() default {};
	
	/**
	 * 
	 * @return
	 */
	EventTypeRef[] events() default {};
	
	/**
	 * the optional (in case no activity objects defined) {@link ActivityContextInterface} factory interface
	 * @return
	 */
	Class<?> aciFactory() default Object.class;
	
	/**
	 * the optional sbb interface
	 * @return
	 */
	Class<?> sbbInterface() default Object.class;
	
	/**
	 * 
	 * @return
	 */
	LibraryRef[] libraryRefs() default {};
	
}
