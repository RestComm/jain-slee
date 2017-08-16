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
import javax.slee.SbbLocalObject;

/**
 * @author martins
 *
 */
@Documented
@Target(value={ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Sbb {

	/**
	 * 
	 * @return
	 */
	String name();
	
	/**
	 * 
	 * @return
	 */
	String vendor();
	
	/**
	 * 
	 * @return
	 */
	String version();	
	
	/**
	 * 
	 * @return
	 */
	String alias() default "";
	
	/**
	 * 
	 * @return
	 */
	Class<? extends SbbLocalObject> localInterface() default SbbLocalObject.class;
	
	/**
	 * 
	 * @return
	 */
	Class<? extends ActivityContextInterface> activityContextInterface() default ActivityContextInterface.class;
		
	/**
	 * 
	 * @return
	 */
	String addressProfileSpecAliasRef() default "";
	
	/**
	 * 
	 * @return
	 */
	LibraryRef[] libraryRefs() default {};
	
	/**
	 * 
	 * @return
	 */
	SbbRef[] sbbRefs() default {};
	
	/**
	 * 
	 * @return
	 */
	ProfileSpecRef[] profileSpecRefs() default {};
	
	/**
	 * 
	 * @return
	 */
	ConfigProperty[] properties() default {};
	
	/**
	 * 
	 * @return
	 */
	ActivityContextAttributeAlias[] activityContextAttributeAlias() default {};
	
	/**
	 * 
	 * @return
	 */
	String securityPermissions() default "";
	
	// TODO ejb-ref
	
}
