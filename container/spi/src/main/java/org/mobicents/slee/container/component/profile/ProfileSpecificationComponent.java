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
package org.mobicents.slee.container.component.profile;

import java.util.Map;

import javax.slee.facilities.AlarmFacility;
import javax.slee.management.DeploymentException;
import javax.slee.profile.ProfileSpecificationID;

import org.mobicents.slee.container.component.SleeComponentWithUsageParametersInterface;
import org.mobicents.slee.container.profile.entity.ProfileEntityFramework;

/**
 * 
 * @author martins
 *
 */
public interface ProfileSpecificationComponent extends SleeComponentWithUsageParametersInterface {

	/**
	 * Retrieves the component's descriptor.
	 * @return
	 */
	public ProfileSpecificationDescriptor getDescriptor();
	
	/**
	 * Retrieves the profile abstract class
	 * 
	 * @return
	 */
	public Class<?> getProfileAbstractClass();

	/**
	 *  
	 * @return the profileConcreteClassInfo
	 */
	public ProfileConcreteClassInfo getProfileConcreteClassInfo();
	
	/**
	 * Retrieves a unmodifiable map of {@link ProfileAttribute}, the key of this map is the attribute name 
	 * @return
	 */
	public Map<String, ProfileAttribute> getProfileAttributes();

	/**
	 * Returns class object representing concrete impl of cmp interface - it
	 * implements all required
	 * 
	 * @return
	 */
	public Class<?> getProfileCmpConcreteClass();

	/**
	 * Retrieves the profile cmp interface
	 * 
	 * @return
	 */
	public Class<?> getProfileCmpInterfaceClass();

	/**
	 * Retreives the Profile CMP Slee 1.0 Wrapper class, an object that implements the Profile CMP
	 * Interface, wrapping the SLEE 1.1 real profile concrete object in a SLEE 1.0
	 * compatible interface
	 * @return
	 */
	public Class<?> getProfileCmpSlee10WrapperClass();

	/**
	 * Retrieves the entity framework for the component
	 * @return
	 */
	public ProfileEntityFramework getProfileEntityFramework();

	/**
	 * Retrieves the profile local interface
	 * 
	 * @return
	 */
	public Class<?> getProfileLocalInterfaceClass();

	
	/**
	 * sget profile local object concrete class - this is instrumented class
	 * that handles runtime calls
	 * 
	 * @return
	 */
	public Class<?> getProfileLocalObjectConcreteClass();

	/**
	 * Retrieves the profile management interface
	 * 
	 * @return
	 */
	public Class<?> getProfileManagementInterfaceClass();

	/**
	 * returns concrete MBean impl that manages this profile spec
	 * 
	 * @return
	 */
	public Class<?> getProfileMBeanConcreteImplClass();

	/**
	 * Returns concrete/generated mbean interface for this profile specs
	 * 
	 * @return
	 */
	public Class<?> getProfileMBeanConcreteInterfaceClass();

	public Class<?> getProfilePersistanceTransientStateConcreteClass();

	/**
	 * Retrieves the profile specification id
	 * 
	 * @return
	 */
	public ProfileSpecificationID getProfileSpecificationID();

	/**
	 * get profile table concrete class - its impl of profile table interface -
	 * either default or provided by dev - this is instrumented class that
	 * handles runtime calls
	 * 
	 * @return
	 */
	public Class<?> getProfileTableConcreteClass();

	/**
	 * Retrieves the profile table interface
	 * 
	 * @return
	 */
	public Class<?> getProfileTableInterfaceClass();

	/**
	 * Retrieves the JAIN SLEE specs descriptor
	 * 
	 * @return
	 */
	public javax.slee.profile.ProfileSpecificationDescriptor getSpecsDescriptor();

	/**
	 * Sets the profile abstract class
	 * 
	 * @param c
	 */
	public void setProfileAbstractClass(Class<?> c);

	/**
	 * Set class object representing concrete impl of cmp interface - it
	 * implements all required
	 * 
	 * @param c
	 */
	public void setProfileCmpConcreteClass(Class<?> c);

	/**
	 * Sets the profile cmp interface and builds the profile attribute map
	 * 
	 * @param c
	 * @throws DeploymentException 
	 */
	public void setProfileCmpInterfaceClass(Class<?> c) throws DeploymentException;

	/**
	 * Sets the Profile CMP Slee 1.0 Wrapper class, an object that implements the Profile CMP
	 * Interface, wrapping the SLEE 1.1 real profile concrete object in a SLEE 1.0
	 * compatible interface
	 * @param c
	 */
	public void setProfileCmpSlee10WrapperClass(Class<?> c);

	/**
	 * Sets the entity framework for the component
	 * @param profileEntityFramework
	 */
	public void setProfileEntityFramework(
			ProfileEntityFramework profileEntityFramework);

	/**
	 * Sets the profile local interface
	 * 
	 * @param c
	 */
	public void setProfileLocalInterfaceClass(Class<?> c);

	/**
	 * set profile local object concrete class - this is instrumented class that
	 * handles runtime calls
	 * 
	 * @param c
	 */
	public void setProfileLocalObjectConcreteClass(Class<?> c);

	/**
	 * Sets the profile management interface
	 * 
	 * @param c
	 */
	public void setProfileManagementInterfaceClass(Class<?> c);

	/**
	 * sets concrete MBean impl that manages this profile spec
	 * 
	 * @param c
	 */
	public void setProfileMBeanConcreteImplClass(Class<?> c);

	/**
	 * Sets concrete/generated mbean interface for this profile specs
	 * 
	 * @param c
	 */
	public void setProfileMBeanConcreteInterfaceClass(Class<?> c) ;

	/**
	 * 
	 * @param c
	 */
	public void setProfilePersistanceTransientStateConcreteClass(Class<?> c);
	
	/**
	 * set profile table concrete class - its impl of profile table interface -
	 * either default or provided by dev - this is instrumented class that
	 * handles runtime calls
	 * 
	 * @param c
	 */
	public void setProfileTableConcreteClass(Class<?> c);
	
	/**
	 * Sets the profile table interface
	 * 
	 * @param c
	 */
	public void setProfileTableInterfaceClass(Class<?> c);
	
	/**
	 * Retrieves the alarm facility bound to the profile spec component
	 * @return
	 */
	public AlarmFacility getAlarmFacility();
	
	/**
	 * Sets the alarm facility for the profile spec component.
	 * @param alarmFacility
	 */
	public void setAlarmFacility(AlarmFacility alarmFacility);
}
