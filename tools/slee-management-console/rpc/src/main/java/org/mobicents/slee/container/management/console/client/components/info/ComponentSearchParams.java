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

package org.mobicents.slee.container.management.console.client.components.info;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Stefano Zappaterra
 * 
 */
public class ComponentSearchParams implements IsSerializable {

  private String name;

  private String id;

  private String vendor;

  private String version;

  public ComponentSearchParams() {
    super();
  }

  public ComponentSearchParams(String name, String id, String vendor, String version) throws ManagementConsoleException {
    super();
    this.name = name.trim();
    this.id = id.trim();
    this.vendor = vendor.trim();
    this.version = version.trim();
    validate();
  }

  public void validate() throws ManagementConsoleException {
    if (name != null && name.length() > 0)
      return;
    if (id != null && id.length() > 0)
      return;
    if (vendor != null && vendor.length() > 0)
      return;
    if (version != null && version.length() > 0)
      return;
    throw new ManagementConsoleException("No search parameter specified");
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getVendor() {
    return vendor;
  }

  public String getVersion() {
    return version;
  }

  private boolean isTextDefined(String text) {
    if (text != null && text.length() > 0)
      return true;
    return false;
  }

  private boolean nameMatches(String name) {
    if (!isTextDefined(this.name))
      return true;

    if (!isTextDefined(name))
      return false;

    if (name.toLowerCase().indexOf(this.name.toLowerCase()) == -1)
      return false;

    return true;
  }

  private boolean idMatches(String id) {
    if (!isTextDefined(this.id))
      return true;

    if (!isTextDefined(id))
      return false;

    if (id.toLowerCase().indexOf(this.id.toLowerCase()) == -1)
      return false;

    return true;
  }

  private boolean vendorMatches(String vendor) {
    if (!isTextDefined(this.vendor))
      return true;

    if (!isTextDefined(vendor))
      return false;

    if (vendor.toLowerCase().indexOf(this.vendor.toLowerCase()) == -1)
      return false;

    return true;
  }

  private boolean versionMatches(String version) {
    if (!isTextDefined(this.version))
      return true;

    if (!isTextDefined(version))
      return false;

    if (version.toLowerCase().indexOf(this.version.toLowerCase()) == -1)
      return false;

    return true;
  }

  public boolean matches(String name, String id, String vendor, String version) {
    if (nameMatches(name) && idMatches(id) && vendorMatches(vendor) && versionMatches(version))
      return true;

    return false;
  }
}
