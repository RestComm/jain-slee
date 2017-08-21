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
