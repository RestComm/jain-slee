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

/**
 * 
 * The activity-context-attribute-alias element allows an SBB to alias activity
 * context attributes. Aliasing an attribute causes it to become available in a
 * global namespace public to all SBBs. The element contains the attribute alias
 * name, and the names of zero or more attributes in the SBB's activity context
 * interface that should be aliased to the given name.
 * 
 * @author Eduardo Martins
 * 
 */
@Documented
@Target(value = { ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityContextAttributeAlias {

	/**
	 * the attribute alias, that is, it's name in the global space shared by all Sbbs.
	 * @return
	 */
	String attributeAlias();

	/**
	 * the name of all attributes pointing to the alias. 
	 * @return
	 */
	String[] attributeNames();

}
