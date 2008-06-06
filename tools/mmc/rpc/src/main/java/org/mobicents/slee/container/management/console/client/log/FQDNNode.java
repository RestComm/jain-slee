/*
 * Mobicents: The Open Source VoIP Middleware Platform
 *
 * Copyright 2003-2006, Mobicents, 
 * and individual contributors as indicated
 * by the @authors tag. See the copyright.txt 
 * in the distribution for a full listing of   
 * individual contributors.
 *
 * This is free software; you can redistribute it
 * and/or modify it under the terms of the 
 * GNU General Public License (GPL) as
 * published by the Free Software Foundation; 
 * either version 2 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that 
 * it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the 
 * GNU General Public
 * License along with this software; 
 * if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, 
 * Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org.
 */
package org.mobicents.slee.container.management.console.client.log;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;


/**
 * This class represents node - like "org","mobicents" or "net" in fqdn -
 * org.mobicents.slee.container.SomeClass This class is intended to ease logger
 * tree creation - this process could be achieved along with parsing, but its
 * tricky and error prone.
 * 
 * @author baranowb
 */


public class FQDNNode{
	//IT CANT BE SERIALIZABLE - GWT CANT COMPILE RECURENCY SERIALIZATION!!!? ;/
	//public class FQDNNode implements IsSerializable{	
	//Should be TreeMap ;/
	private HashMap childNodes = new HashMap();
	private boolean wasLeaf = false;
	private String fqdName = null;
	private String shortName = null;

	public FQDNNode()
	{}
	
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
			} else {
				FQDNNode node = new FQDNNode(true, makeName(fqdn,index), fqdn[index]);
				this.childNodes.put(fqdn[index], node);
			}
		} else if (index + 1 < fqdn.length) {
			// we have to add node and push add to it
			if (this.childNodes.containsKey(fqdn[index])) {
				// This node has been added before, but could be that its not a
				// lead
				FQDNNode node = (FQDNNode) this.childNodes.get(fqdn[index]);
				node.addNode(fqdn, fullName, index + 1);
			} else {
				FQDNNode node = new FQDNNode(false, makeName(fqdn,index), fqdn[index]);
				this.childNodes.put(fqdn[index], node);
				node.addNode(fqdn, fullName, index + 1);
			}
		}
	}

	private String makeName(String[] fqdn, int index)
	{
		StringBuffer ret=new StringBuffer(fqdn[0]);
		
		for(int i=1;i<=index && index<=fqdn.length;i++)
			ret.append("."+fqdn[i]);
		return ret.toString();
	}
	
	public Set getChildrenNames() {
		return this.childNodes.keySet();
	}

	public FQDNNode getChild(String name) {
		return (FQDNNode) this.childNodes.get(name);
	}

	public Collection getChildren() {
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
