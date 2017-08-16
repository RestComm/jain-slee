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

import javax.slee.profile.Profile;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileTable;

/**
 * Annotation for a JAIN SLEE 1.1 Profile Spec.
 * @author martins
 *
 */
@Documented
@Target(value={ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProfileSpec {

	/**
	 * the profile spec name
	 * @return
	 */
	String name();
	
	/**
	 * the profile spec vendor
	 * @return
	 */
	String vendor();
	
	/**
	 * the profile spec version
	 * @return
	 */
	String version();
	
	/**
	 * the profile cmp interface
	 * @return
	 */
	Class<?> cmpInterface();
	
	/**
	 * the optional profile local interface
	 * @return
	 */
	Class<? extends ProfileLocalObject> localInterface() default ProfileLocalObject.class;
	
	/**
	 * the optional profile management interface
	 * @return
	 */
	Class<?> managementInterface() default Object.class;
	
	/**
	 * the optional profile abstract class
	 * @return
	 */
	Class<? extends Profile> abstractClass() default Profile.class;
	
	/**
	 * the optional profile table interface
	 * @return
	 */
	Class<? extends ProfileTable> tableInterface() default ProfileTable.class;
	
	/**
	 * 
	 * @return
	 */
	LibraryRef[] libraryRefs() default {};
	
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
	String securityPermissions() default "";
	
	// TODO add static queries
	
}
