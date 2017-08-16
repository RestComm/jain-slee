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

package org.mobicents.slee.container.management.jmx.editors;

import javax.slee.resource.ConfigProperties;

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
