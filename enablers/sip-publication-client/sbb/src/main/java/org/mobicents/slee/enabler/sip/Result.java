/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.slee.enabler.sip;

import java.io.Serializable;

/**
 * The result for a publication related operation.
 * 
 * @author martins
 * @author baranowb
 * 
 */
public class Result implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int statusCode;
	private String eTag;
	private int expires;
	private String entity;

	
	
	/**
	 * 
	 */
	public Result() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param statusCode
	 * @param eTag
	 * @param expires
	 * @param entity
	 */
	public Result(int statusCode, String eTag, int expires, String entity) {
		super();
		this.statusCode = statusCode;
		this.eTag = eTag;
		this.expires = expires;
		this.entity = entity;
	}

	/**
	 * @param statusCode
	 * @param eTag
	 * @param expires
	 */
	public Result(int statusCode, String eTag, int expires) {
		super();
		this.statusCode = statusCode;
		this.eTag = eTag;
		this.expires = expires;
		this.entity = null;
	}

	/**
	 * @param statusCode
	 */
	public Result(int statusCode) {
		this.statusCode = statusCode;
		this.eTag = null;
		this.expires = -1;
		this.entity = null;
	}

	public Result(int statusCode, String entity) {
		this.statusCode = statusCode;
		this.eTag = null;
		this.expires = -1;
		this.entity = entity;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String geteTag() {
		return eTag;
	}

	public int getExpires() {
		return expires;
	}

	public String getEntity() {
		return entity;
	}

	void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	void seteTag(String eTag) {
		this.eTag = eTag;
	}

	void setExpires(int expires) {
		this.expires = expires;
	}

	void setEntity(String entity) {
		this.entity = entity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eTag == null) ? 0 : eTag.hashCode());
		result = prime * result + ((entity == null) ? 0 : entity.hashCode());
		result = prime * result + expires;
		result = prime * result + statusCode;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Result other = (Result) obj;
		if (eTag == null) {
			if (other.eTag != null)
				return false;
		} else if (!eTag.equals(other.eTag))
			return false;
		if (entity == null) {
			if (other.entity != null)
				return false;
		} else if (!entity.equals(other.entity))
			return false;
		if (expires != other.expires)
			return false;
		if (statusCode != other.statusCode)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Result [statusCode=" + statusCode + ", eTag=" + eTag + ", expires=" + expires + ", entity=" + entity + "]";
	}

}
