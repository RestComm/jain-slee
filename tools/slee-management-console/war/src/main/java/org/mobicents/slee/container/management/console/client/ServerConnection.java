/*
 * JBoss, Home of Professional Open Source
 * Copyright 2003-2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.slee.container.management.console.client;

import org.mobicents.slee.container.management.console.client.activity.ActivityService;
import org.mobicents.slee.container.management.console.client.activity.ActivityServiceAsync;
import org.mobicents.slee.container.management.console.client.alarms.AlarmsService;
import org.mobicents.slee.container.management.console.client.alarms.AlarmsServiceAsync;
import org.mobicents.slee.container.management.console.client.components.ComponentsService;
import org.mobicents.slee.container.management.console.client.components.ComponentsServiceAsync;
import org.mobicents.slee.container.management.console.client.deployableunits.DeployableUnitsService;
import org.mobicents.slee.container.management.console.client.deployableunits.DeployableUnitsServiceAsync;
import org.mobicents.slee.container.management.console.client.log.LogService;
import org.mobicents.slee.container.management.console.client.log.LogServiceAsync;
import org.mobicents.slee.container.management.console.client.profiles.ProfileService;
import org.mobicents.slee.container.management.console.client.profiles.ProfileServiceAsync;
import org.mobicents.slee.container.management.console.client.resources.ResourceService;
import org.mobicents.slee.container.management.console.client.resources.ResourceServiceAsync;
import org.mobicents.slee.container.management.console.client.sbb.entity.SbbEntitiesService;
import org.mobicents.slee.container.management.console.client.sbb.entity.SbbEntitiesServiceAsync;
import org.mobicents.slee.container.management.console.client.services.ServicesService;
import org.mobicents.slee.container.management.console.client.services.ServicesServiceAsync;
import org.mobicents.slee.container.management.console.client.sleestate.SleeStateService;
import org.mobicents.slee.container.management.console.client.sleestate.SleeStateServiceAsync;
import org.mobicents.slee.container.management.console.client.usage.RaUsageService;
import org.mobicents.slee.container.management.console.client.usage.RaUsageServiceAsync;
import org.mobicents.slee.container.management.console.client.usage.UsageService;
import org.mobicents.slee.container.management.console.client.usage.UsageServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * 
 * @author Stefano Zappaterra
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ServerConnection {

  final static public ComponentsServiceAsync componentsService;

  final static public DeployableUnitsServiceAsync deployableUnitsService;

  final static public SleeStateServiceAsync sleeStateServiceAsync;

  final static public ServicesServiceAsync servicesServiceAsync;

  final static public UsageServiceAsync usageServiceAsync;

  final static public ResourceServiceAsync resourceServiceAsync;

  final static public RaUsageServiceAsync raUsageServiceAsync;

  final static public ActivityServiceAsync activityServiceAsync;

  final static public SbbEntitiesServiceAsync sbbEntitiesServiceAsync;

  final static public AlarmsServiceAsync alarmsServiceAsync;

  final static public LogServiceAsync logServiceAsync;

  final static public ProfileServiceAsync profileServiceAsync;

  static {

    componentsService = (ComponentsServiceAsync) GWT.create(ComponentsService.class);
    ServiceDefTarget componentsEndpoint = (ServiceDefTarget) componentsService;
    componentsEndpoint.setServiceEntryPoint(GWT.getModuleBaseURL().replaceAll("org.mobicents.slee.container.management.console.ManagementConsole", "") + "/ComponentsService");

    deployableUnitsService = (DeployableUnitsServiceAsync) GWT.create(DeployableUnitsService.class);
    ServiceDefTarget deployableUnitsEndpoint = (ServiceDefTarget) deployableUnitsService;
    deployableUnitsEndpoint.setServiceEntryPoint(GWT.getModuleBaseURL().replaceAll("org.mobicents.slee.container.management.console.ManagementConsole", "") + "/DeployableUnitsService");

    sleeStateServiceAsync = (SleeStateServiceAsync) GWT.create(SleeStateService.class);
    ServiceDefTarget sleeStateEndpoint = (ServiceDefTarget) sleeStateServiceAsync;
    sleeStateEndpoint.setServiceEntryPoint(GWT.getModuleBaseURL().replaceAll("org.mobicents.slee.container.management.console.ManagementConsole", "") + "/SleeStateService");

    servicesServiceAsync = (ServicesServiceAsync) GWT.create(ServicesService.class);
    ServiceDefTarget servicesEndpoint = (ServiceDefTarget) servicesServiceAsync;
    servicesEndpoint.setServiceEntryPoint(GWT.getModuleBaseURL().replaceAll("org.mobicents.slee.container.management.console.ManagementConsole", "") + "/ServicesService");

    usageServiceAsync = (UsageServiceAsync) GWT.create(UsageService.class);
    ServiceDefTarget usageEndpoint = (ServiceDefTarget) usageServiceAsync;
    usageEndpoint.setServiceEntryPoint(GWT.getModuleBaseURL().replaceAll("org.mobicents.slee.container.management.console.ManagementConsole", "") + "/UsageService");

    resourceServiceAsync = (ResourceServiceAsync) GWT.create(ResourceService.class);
    ServiceDefTarget resourceEndpoint = (ServiceDefTarget) resourceServiceAsync;
    resourceEndpoint.setServiceEntryPoint(GWT.getModuleBaseURL().replaceAll("org.mobicents.slee.container.management.console.ManagementConsole", "") + "/ResourceService");

    raUsageServiceAsync = (RaUsageServiceAsync) GWT.create(RaUsageService.class);
    ServiceDefTarget raUsageEndpoint = (ServiceDefTarget) raUsageServiceAsync;
    raUsageEndpoint.setServiceEntryPoint(GWT.getModuleBaseURL().replaceAll("org.mobicents.slee.container.management.console.ManagementConsole", "") + "/RaUsageService");

    activityServiceAsync = (ActivityServiceAsync) GWT.create(ActivityService.class);
    ServiceDefTarget activityEndpoint = (ServiceDefTarget) activityServiceAsync;
    activityEndpoint.setServiceEntryPoint(GWT.getModuleBaseURL().replaceAll("org.mobicents.slee.container.management.console.ManagementConsole", "") + "/ActivityService");

    sbbEntitiesServiceAsync = (SbbEntitiesServiceAsync) GWT.create(SbbEntitiesService.class);
    ServiceDefTarget sbbEntitiesEndpoint = (ServiceDefTarget) sbbEntitiesServiceAsync;
    sbbEntitiesEndpoint.setServiceEntryPoint(GWT.getModuleBaseURL().replaceAll("org.mobicents.slee.container.management.console.ManagementConsole", "") + "/SbbEntitiesService");

    alarmsServiceAsync = (AlarmsServiceAsync) GWT.create(AlarmsService.class);
    ServiceDefTarget alarmsEndpoint = (ServiceDefTarget) alarmsServiceAsync;
    alarmsEndpoint.setServiceEntryPoint(GWT.getModuleBaseURL().replaceAll("org.mobicents.slee.container.management.console.ManagementConsole", "") + "/AlarmsService");

    logServiceAsync = (LogServiceAsync) GWT.create(LogService.class);
    ServiceDefTarget logEndpoint = (ServiceDefTarget) logServiceAsync;
    logEndpoint.setServiceEntryPoint(GWT.getModuleBaseURL().replaceAll("org.mobicents.slee.container.management.console.ManagementConsole", "") + "/LogService");

  profileServiceAsync = (ProfileServiceAsync) GWT.create(ProfileService.class);
		ServiceDefTarget profileEndpoint = (ServiceDefTarget) profileServiceAsync;
		profileEndpoint.setServiceEntryPoint(GWT.getModuleBaseURL().replaceAll(
				"org.mobicents.slee.container.management.console.ManagementConsole", "") + "/ProfileService");
  }

}
