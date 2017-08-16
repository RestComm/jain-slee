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

package org.mobicents.slee.container.profile;

import javax.slee.profile.Profile;
import javax.slee.profile.ProfileManagement;

/**
 * 
 * Start time:16:51:13 2009-03-13<br>
 * Project: restcomm-jainslee-server-core<br>
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
