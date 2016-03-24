/*
 * JBoss, Home of Professional Open Source
 * Copyright 2003-2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.slee.container.management.console.server;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;
import org.mobicents.slee.container.management.console.client.PropertiesInfo;

import javax.slee.resource.ConfigProperties;
import javax.slee.resource.ConfigProperties.Property;
import java.lang.reflect.Constructor;

/**
 * 
 * @author Stefano Zappaterra
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class PropertiesInfoUtils {

  public static PropertiesInfo toPropertiesInfo(ConfigProperties properties) {
    PropertiesInfo propertiesInfo = new PropertiesInfo();

    if (properties == null)
      return propertiesInfo;

    for (Property property : properties.getProperties()) {
      String key = property.getName() + " :: " + property.getType();
      String value = property.getValue().toString();
      propertiesInfo.setProperty(key, value);
    }
    return propertiesInfo;
  }

  public static ConfigProperties toProperties(PropertiesInfo propertiesInfo) throws ManagementConsoleException {
    ConfigProperties properties = new ConfigProperties();

    for (String key : propertiesInfo.keySet()) {
      String[] nameAndType = key.split(" :: ", 2);

      // obtain a value depending on the type of property
      //String value = propertiesInfo.getProperty(key);
      Object value = propertiesInfo.getProperty(key);
      try {
        Class clazz = Class.forName(nameAndType[1]);
        Constructor con = clazz.getConstructor(String.class);
        value = con.newInstance((String)value);
      } catch (Exception e) {
          throw new ManagementConsoleException("Value of " + nameAndType[0]
                  + " is not supported for type " + nameAndType[1] + ".");
      }

      Property property = new Property(nameAndType[0], nameAndType[1], value);
      properties.addProperty(property);
    }
    return properties;
  }
}
