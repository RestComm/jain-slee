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
package org.mobicents.tools.twiddle;

import java.beans.PropertyEditorManager;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
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

import org.jboss.console.twiddle.command.AbstractCommand;
import org.jboss.console.twiddle.command.CommandException;
import org.mobicents.slee.container.management.jmx.editors.AddressPropertyEditor;
import org.mobicents.slee.container.management.jmx.editors.ComponentIDArrayPropertyEditor;
import org.mobicents.slee.container.management.jmx.editors.ComponentIDPropertyEditor;
import org.mobicents.slee.container.management.jmx.editors.ConfigPropertiesPropertyEditor;
import org.mobicents.slee.container.management.jmx.editors.DeployableUnitIDPropertyEditor;
import org.mobicents.slee.container.management.jmx.editors.LevelPropertyEditor;
import org.mobicents.slee.container.management.jmx.editors.NotificationSourcePropertyEditor;
import org.mobicents.slee.container.management.jmx.editors.ObjectPropertyEditor;
import org.mobicents.slee.container.management.jmx.editors.ResourceAdaptorEntityStatePropertyEditor;
import org.mobicents.slee.container.management.jmx.editors.ServiceStatePropertyEditor;
import org.mobicents.slee.container.management.jmx.editors.TraceLevelPropertyEditor;
import org.mobicents.tools.twiddle.op.AbstractOperation;

/**
 * Base class for slee commands. Defines property editors for them.
 * 
 * @author baranowb
 * 
 */
@SuppressWarnings("deprecation")

public abstract class AbstractSleeCommand extends AbstractCommand {

	static {
		// add editors, this will be set once any mc command is loaded.
		//TODO: make this configurable.
		//TODO: editor for facilities.Level
		//TODO: editor for Address ?
		PropertyEditorManager.registerEditor(Address.class, AddressPropertyEditor.class);
		//TODO: editor for ACH?
		//TODO: add support for definition of editors + array editing.
		PropertyEditorManager.registerEditor(ComponentID.class, ComponentIDPropertyEditor.class);
		PropertyEditorManager.registerEditor(EventTypeID.class, ComponentIDPropertyEditor.class);
		PropertyEditorManager.registerEditor(LibraryID.class, ComponentIDPropertyEditor.class);
		PropertyEditorManager.registerEditor(ProfileSpecificationID.class, ComponentIDPropertyEditor.class);
		PropertyEditorManager.registerEditor(ResourceAdaptorID.class, ComponentIDPropertyEditor.class);
		PropertyEditorManager.registerEditor(ResourceAdaptorTypeID.class, ComponentIDPropertyEditor.class);
		PropertyEditorManager.registerEditor(SbbID.class, ComponentIDPropertyEditor.class);
		PropertyEditorManager.registerEditor(ServiceID.class, ComponentIDPropertyEditor.class);

		PropertyEditorManager.registerEditor(ComponentID[].class, ComponentIDArrayPropertyEditor.class);
		PropertyEditorManager.registerEditor(EventTypeID[].class, ComponentIDArrayPropertyEditor.class);
		PropertyEditorManager.registerEditor(LibraryID[].class, ComponentIDArrayPropertyEditor.class);
		PropertyEditorManager.registerEditor(ProfileSpecificationID[].class, ComponentIDArrayPropertyEditor.class);
		PropertyEditorManager.registerEditor(ResourceAdaptorID[].class, ComponentIDArrayPropertyEditor.class);
		PropertyEditorManager.registerEditor(ResourceAdaptorTypeID[].class, ComponentIDArrayPropertyEditor.class);
		PropertyEditorManager.registerEditor(SbbID[].class, ComponentIDArrayPropertyEditor.class);
		PropertyEditorManager.registerEditor(ServiceID[].class, ComponentIDArrayPropertyEditor.class);

		PropertyEditorManager.registerEditor(DeployableUnitID.class, DeployableUnitIDPropertyEditor.class);

		PropertyEditorManager.registerEditor(Level.class, LevelPropertyEditor.class);
		PropertyEditorManager.registerEditor(TraceLevel.class, TraceLevelPropertyEditor.class);

		PropertyEditorManager.registerEditor(ConfigProperties.class, ConfigPropertiesPropertyEditor.class);

		PropertyEditorManager.registerEditor(NotificationSource.class, NotificationSourcePropertyEditor.class);

		PropertyEditorManager.registerEditor(Object.class, ObjectPropertyEditor.class);

		PropertyEditorManager.registerEditor(ServiceState.class, ServiceStatePropertyEditor.class);
		PropertyEditorManager.registerEditor(ResourceAdaptorEntityState.class, ResourceAdaptorEntityStatePropertyEditor.class);

	}

	//TODO: add support for white spaces
	protected AbstractOperation operation;

	public AbstractSleeCommand(String name, String desc) {
		super(name, desc);
	}
	
	public void execute(String[] args) throws Exception {
		// create opts
		processArguments(args);
		// nothing was thrown? lets execute command
		if(operation!=null)
		{
			this.operation.invoke();
		}else
		{
			//nothing has been passed? display help.
			displayHelp();
		}
	}
	
	protected abstract void processArguments(String[] args) throws CommandException;
	
	public abstract ObjectName getBeanOName() throws MalformedObjectNameException, NullPointerException; // public so ops can use that in overriden methods like invoke();
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.console.twiddle.command.Command#displayHelp()
	 */
	public abstract void displayHelp();

}
