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

package org.mobicents.slee.container.management.jmx.editors;

import javax.slee.resource.ConfigProperties;

import org.jboss.util.propertyeditor.TextPropertyEditorSupport;

/**
 * 
 * Property Editor for {@link ConfigProperties}. Note that the editor considers
 * the string "null" to be null value, thus it is not possible to pass
 * properties with as value the strig null
 * 
 * @author martins
 * 
 */
public class ConfigPropertiesPropertyEditor extends TextPropertyEditorSupport {

	public void setAsText(String text) {
		try {
			ConfigProperties properties = new ConfigProperties();
			String props = text;
			if (props.charAt(0) == '[') {
				props = props.substring(1);
				if (props.charAt(props.length()-1) == ']') {
					props = props.substring(0, props.length() - 1);
					// removed wrapping chars, now get properties
					String propString = null;
					String propType = null;
					String propValue = null;
					int propStart = -1;
					while ((propStart = props.indexOf('(')) != -1) {
						propString = props.substring(propStart + 1, props
								.indexOf(')'));
						int sep1 = propString.indexOf(':');
						int sep2 = propString.indexOf('=', sep1);
						propType = propString.substring(sep1 + 1, sep2);
						propValue = propString.substring(sep2 + 1, propString
								.length());
						properties.addProperty(new ConfigProperties.Property(
								propString.substring(0, sep1), propType,
								(propValue.equals("null") ? null
										: ConfigProperties.Property.toObject(
												propType, propValue))));
						props = props.substring(propString.length() + 2);
						if (props.length() != 0) {
							// remove the next ','
							props = props.substring(1);
						} else {
							break;
						}
					}
					this.setValue(properties);
					return;
				}
			}
			throw new IllegalArgumentException(
					"config properties must be passed as [(pName1:pType1=pValue1),(pName2:pType2=pValue2)], got "
							+ text);
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"config properties must be passed as [(pName1:pType1=pValue1),(pName2:pType2=pValue2)], got "
							+ text, e);
		}
	}
}
