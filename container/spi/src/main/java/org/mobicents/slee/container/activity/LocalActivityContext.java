/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2015, Telestax Inc and individual contributors
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
 *
 * This file incorporates work covered by the following copyright contributed under the GNU LGPL : Copyright 2007-2011 Red Hat.
 */

package org.mobicents.slee.container.activity;

import org.mobicents.slee.container.eventrouter.EventRouterExecutor;
import org.mobicents.slee.container.eventrouter.EventRoutingTask;

/**
 * Local view and resources of the activity context.
 * 
 * @author martins
 *
 */
public interface LocalActivityContext {

    ActivityContextHandle getActivityContextHandle();

    String getStringId();

    EventRoutingTask getCurrentEventRoutingTask();

    ActivityEventQueueManager getEventQueueManager();

    EventRouterExecutor getExecutorService();

    void setCurrentEventRoutingTask(EventRoutingTask eventRoutingTask);

    void setExecutorService(EventRouterExecutor executor);
}
