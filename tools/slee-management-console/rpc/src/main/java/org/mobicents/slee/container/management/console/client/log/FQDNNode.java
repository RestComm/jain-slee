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

package org.mobicents.slee.container.management.console.client.log;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * This class represents node - like "org","restcomm" or "net" in fqdn - org.mobicents.slee.container.SomeClass This class is intended to
 * ease logger tree creation - this process could be achieved along with parsing, but its tricky and error prone.
 * 
 * @author baranowb
 */

public class FQDNNode {
  // IT CANT BE SERIALIZABLE - GWT CANT COMPILE RECURENCY SERIALIZATION!!!? ;/
  // public class FQDNNode implements IsSerializable{
  // Should be TreeMap ;/
  private HashMap<String, FQDNNode> childNodes = new HashMap<String, FQDNNode>();
  private boolean wasLeaf = false;
  private String fqdName = null;
  private String shortName = null;

  public FQDNNode() {
  }

  public FQDNNode(boolean wasLeaf, String fqdName, String shortName) {
    super();
    this.wasLeaf = wasLeaf;
    this.fqdName = fqdName;
    this.shortName = shortName;
  }

  public void addNode(String[] fqdn, String fullName, int index) {

    if (index + 1 == fqdn.length) {

      // We have last node, need to set it right
      if (this.childNodes.containsKey(fqdn[index])) {
        // This node has been added before, but could be that its not a
        // leaf yet
        FQDNNode node = (FQDNNode) this.childNodes.get(fqdn[index]);
        node.wasLeaf = true;
      }
      else {
        FQDNNode node = new FQDNNode(true, makeName(fqdn, index), fqdn[index]);
        this.childNodes.put(fqdn[index], node);
      }
    }
    else if (index + 1 < fqdn.length) {
      // we have to add node and push add to it
      if (this.childNodes.containsKey(fqdn[index])) {
        // This node has been added before, but could be that its not a
        // lead
        FQDNNode node = (FQDNNode) this.childNodes.get(fqdn[index]);
        node.addNode(fqdn, fullName, index + 1);
      }
      else {
        FQDNNode node = new FQDNNode(false, makeName(fqdn, index), fqdn[index]);
        this.childNodes.put(fqdn[index], node);
        node.addNode(fqdn, fullName, index + 1);
      }
    }
  }

  private String makeName(String[] fqdn, int index) {
    StringBuffer ret = new StringBuffer(fqdn[0]);

    for (int i = 1; i <= index && index <= fqdn.length; i++)
      ret.append("." + fqdn[i]);
    return ret.toString();
  }

  public Set<String> getChildrenNames() {
    return this.childNodes.keySet();
  }

  public FQDNNode getChild(String name) {
    return (FQDNNode) this.childNodes.get(name);
  }

  public Collection<FQDNNode> getChildren() {
    return this.childNodes.values();
  }

  public void removeChild(String name) {
    this.childNodes.remove(name);
  }

  // ------------ GETTERS

  public boolean isWasLeaf() {
    return wasLeaf;
  }

  public String getFqdName() {
    return fqdName;
  }

  public String getShortName() {
    return shortName;
  }

}
