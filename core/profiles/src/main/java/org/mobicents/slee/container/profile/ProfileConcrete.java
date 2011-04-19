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

package org.mobicents.slee.container.profile;

import javax.slee.profile.Profile;
import javax.slee.profile.ProfileManagement;

/**
 * 
 * Start time:16:51:13 2009-03-13<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * Profile object interface which declares extra management methods to store
 * some runtime properties. This interface represents actual object that has
 * local copy of profile data. Its logical counter part is
 * SbbEntity+SbbConcrete. We can have multiple object representing SbbE cached
 * data, with this object it is similar with this. However we cant use instance
 * of this classes as ProfileEntities, since they MUST act ass concrete impl
 * (see how SbbConcrete is used inside SbbObject)
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface ProfileConcrete extends Profile,ProfileManagement {

	public void setProfileObject(ProfileObjectImpl value);
	public ProfileObjectImpl getProfileObject();

}
