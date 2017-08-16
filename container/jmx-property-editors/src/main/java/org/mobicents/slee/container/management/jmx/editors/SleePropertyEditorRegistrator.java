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

package org.mobicents.slee.container.management.jmx.editors;

import java.beans.PropertyEditorManager;

import javax.slee.Address;
import javax.slee.ComponentID;
import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.facilities.Level;
import javax.slee.facilities.TraceLevel;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.LibraryID;
import javax.slee.management.NotificationSource;
import javax.slee.management.ResourceAdaptorEntityState;
import javax.slee.management.ServiceState;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

/**
 * Handles registration of jmx property editors.
 * 
 * @author martins
 *
 */
@SuppressWarnings("deprecation")
public class SleePropertyEditorRegistrator {

	/**
	 * Register the property editors for jboss jmx console, so non string SLEE api types can be used in specs mbeans methods as args
	 */
	public void register() {
		
		PropertyEditorManager.registerEditor(ComponentID.class,
				ComponentIDPropertyEditor.class);
		PropertyEditorManager.registerEditor(EventTypeID.class,
				ComponentIDPropertyEditor.class);
		PropertyEditorManager.registerEditor(LibraryID.class,
				ComponentIDPropertyEditor.class);
		PropertyEditorManager.registerEditor(ProfileSpecificationID.class,
				ComponentIDPropertyEditor.class);
		PropertyEditorManager.registerEditor(ResourceAdaptorID.class,
				ComponentIDPropertyEditor.class);
		PropertyEditorManager.registerEditor(ResourceAdaptorTypeID.class,
				ComponentIDPropertyEditor.class);
		PropertyEditorManager.registerEditor(SbbID.class,
				ComponentIDPropertyEditor.class);
		PropertyEditorManager.registerEditor(ServiceID.class,
				ComponentIDPropertyEditor.class);
		
		PropertyEditorManager.registerEditor(ComponentID[].class,
				ComponentIDArrayPropertyEditor.class);
		PropertyEditorManager.registerEditor(EventTypeID[].class,
				ComponentIDArrayPropertyEditor.class);
		PropertyEditorManager.registerEditor(LibraryID[].class,
				ComponentIDArrayPropertyEditor.class);
		PropertyEditorManager.registerEditor(ProfileSpecificationID[].class,
				ComponentIDArrayPropertyEditor.class);
		PropertyEditorManager.registerEditor(ResourceAdaptorID[].class,
				ComponentIDArrayPropertyEditor.class);
		PropertyEditorManager.registerEditor(ResourceAdaptorTypeID[].class,
				ComponentIDArrayPropertyEditor.class);
		PropertyEditorManager.registerEditor(SbbID[].class,
				ComponentIDArrayPropertyEditor.class);
		PropertyEditorManager.registerEditor(ServiceID[].class,
				ComponentIDArrayPropertyEditor.class);
		
		PropertyEditorManager.registerEditor(DeployableUnitID.class,
				DeployableUnitIDPropertyEditor.class);
		
		PropertyEditorManager.registerEditor(Level.class,
				LevelPropertyEditor.class);
		PropertyEditorManager.registerEditor(TraceLevel.class,
				TraceLevelPropertyEditor.class);
		
		PropertyEditorManager.registerEditor(ConfigProperties.class,
				ConfigPropertiesPropertyEditor.class);
		
		PropertyEditorManager.registerEditor(NotificationSource.class,
				NotificationSourcePropertyEditor.class);
				
		PropertyEditorManager.registerEditor(Object.class,
				ObjectPropertyEditor.class);
		
		PropertyEditorManager.registerEditor(ServiceState.class,
				ServiceStatePropertyEditor.class);
		PropertyEditorManager.registerEditor(ResourceAdaptorEntityState.class,
				ResourceAdaptorEntityStatePropertyEditor.class);
		PropertyEditorManager.registerEditor(Address.class,
				AddressPropertyEditor.class);
		
	}
}
