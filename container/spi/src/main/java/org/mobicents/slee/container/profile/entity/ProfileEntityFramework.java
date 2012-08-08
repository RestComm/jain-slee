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

package org.mobicents.slee.container.profile.entity;

import java.util.Collection;

import javax.slee.InvalidArgumentException;
import javax.slee.profile.AttributeTypeMismatchException;
import javax.slee.profile.UnrecognizedAttributeException;
import javax.slee.profile.UnrecognizedQueryNameException;
import javax.slee.profile.query.QueryExpression;

import org.mobicents.slee.container.component.profile.ProfileAttribute;

/**
 * A profile entity framework is responsible to implement concrete details about
 * profile entity management, including class generation, persistent data
 * storage, query parsing, etc.
 *  
 * @author martins
 * 
 */
public interface ProfileEntityFramework {
	  
	  /**
	 * 
	 * @param profileTable
	 * @return
	 */
	public Collection<ProfileEntity> findAll(String profileTable);

	/**
	 * 
	 * @param profileTable
	 * @param profileName
	 * @return
	 */
	public ProfileEntity findProfile(String profileTable,
			String profileName);

	/**
	 * 
	 * @param profileTable
	 * @param profileAttribute
	 * @param attributeValue
	 * @return
	 */
	public Collection<ProfileEntity> findProfilesByAttribute(
			String profileTable, ProfileAttribute profileAttribute,
			Object attributeValue);

	/**
	 * 
	 * @return
	 */
	public Class<?> getProfileEntityClass();

	/**
	 * 
	 * @return
	 */
	public ProfileEntityFactory getProfileEntityFactory();

	/**
	 * 
	 * @param profileTableName
	 * @param expr
	 * @return
	 */
	public Collection<ProfileEntity> getProfilesByDynamicQuery(
			String profileTable, QueryExpression expr)
			throws UnrecognizedAttributeException,
			AttributeTypeMismatchException;

	/**
	 * 
	 * @param profileTableName
	 * @param queryName
	 * @param parameters
	 * @return
	 */
	public Collection<ProfileEntity> getProfilesByStaticQuery(
			String profileTable, String queryName, Object[] parameters)
			throws NullPointerException, UnrecognizedQueryNameException,
			AttributeTypeMismatchException, InvalidArgumentException;

	/**
	 * 
	 * @param component
	 */
	public void install();

	/**
	 * 
	 * @param profileEntity
	 */
	public void persistProfile(ProfileEntity profileEntity);

	/**
	 * 
     * @param profileEntity
     */
    public void removeprofile(ProfileEntity profileEntity);

    /**
     * 
     * @param oldProfileTableName
     * @param newProfileTableName
     */
    public void renameProfileTable(String oldProfileTableName, String newProfileTableName);

	/**
	 * 
	 * @param profileTable
	 * @param profileName
	 * @return
	 */
	public ProfileEntity retrieveProfile(String profileTable,
			String profileName);

	/**
	 * 
	 * @param component
	 */
	public void uninstall();

}
