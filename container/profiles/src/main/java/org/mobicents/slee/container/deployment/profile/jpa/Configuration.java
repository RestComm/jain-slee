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

/**
 * 
 */
package org.mobicents.slee.container.deployment.profile.jpa;

/**
 * @author martins
 *
 */
public class Configuration {

	private boolean persistProfiles;
	
	private boolean clusteredProfiles;
	
	private String hibernateDatasource;
	
	private String hibernateDialect;

	/**
	 *  
	 * @param persistProfiles the persistProfiles to set
	 */
	public void setPersistProfiles(boolean persistProfiles) {
		this.persistProfiles = persistProfiles;
	}

	/**
	 *  
	 * @return the clusteredProfiles
	 */
	public boolean isClusteredProfiles() {
		return clusteredProfiles;
	}

	/**
	 *  
	 * @param clusteredProfiles the clusteredProfiles to set
	 */
	public void setClusteredProfiles(boolean clusteredProfiles) {
		this.clusteredProfiles = clusteredProfiles;
	}

	/**
	 *  
	 * @return the persistProfiles
	 */
	public boolean isPersistProfiles() {
		return persistProfiles;
	}

	/**
	 *  
	 * @return the hibernateDatasource
	 */
	public String getHibernateDatasource() {
		return hibernateDatasource;
	}

	/**
	 *  
	 * @return the hibernateDialect
	 */
	public String getHibernateDialect() {
		return hibernateDialect;
	}

	/**
	 * Sets 
	 * @param hibernateDatasource the hibernateDatasource to set
	 */
	public void setHibernateDatasource(String hibernateDatasource) {
		this.hibernateDatasource = hibernateDatasource;
	}

	/**
	 *  
	 * @param hibernateDialect the hibernateDialect to set
	 */
	public void setHibernateDialect(String hibernateDialect) {
		this.hibernateDialect = hibernateDialect;
	}	
	
}
