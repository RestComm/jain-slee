/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2014, Telestax Inc and individual contributors
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
package org.mobicents.slee.container.management;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.resource.ResourceAdaptorEntity;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;
import javax.slee.resource.ResourceAdaptorTypeID;
import java.io.Serializable;
import java.util.Hashtable;

/**
 * A JNDI Object factory to deal with SBB context references to RA interfaces.
 *
 * @author moliveira@telestax.com
 */
public class ResourceAdaptorSbbInterfaceObjectFactory implements ObjectFactory {

    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
        final Reference reference = (Reference) obj;
        final ResourceAdaptorSbbInterfaceRefAddr refAddr = (ResourceAdaptorSbbInterfaceRefAddr) reference.get(0);
        final ResourceAdaptorTypeID resourceAdaptorTypeID = refAddr.content.resourceAdaptorTypeID;
        final String raLink = refAddr.content.raLink;
        final ResourceManagement resourceManagement = SleeContainer.lookupFromJndi().getResourceManagement();
        final ResourceAdaptorEntity raEntity = resourceManagement
                .getResourceAdaptorEntity(resourceManagement
                        .getResourceAdaptorEntityName(raLink));
        return raEntity.getResourceAdaptorInterface(resourceAdaptorTypeID);
    }

    static Reference getReference(Object resourceAdaptorSbbInterface, ResourceAdaptorTypeID resourceAdaptorTypeID, String raLink) {
        return new Reference(resourceAdaptorSbbInterface.getClass().getName(), new ResourceAdaptorSbbInterfaceRefAddr(resourceAdaptorTypeID, raLink), ResourceAdaptorSbbInterfaceObjectFactory.class.getName(), null);
    }

    static class ResourceAdaptorSbbInterfaceRefAddr extends RefAddr {

        private static final String ADDRESS_TYPE = "ResourceAdaptorSbbInterfaceRefAddr";
        private final Content content;

        ResourceAdaptorSbbInterfaceRefAddr(ResourceAdaptorTypeID resourceAdaptorTypeID, String raLink) {
            super(ADDRESS_TYPE);
            this.content = new Content(resourceAdaptorTypeID, raLink);
        }

        @Override
        public Object getContent() {
            return content;
        }

        static class Content implements Serializable {
            private final ResourceAdaptorTypeID resourceAdaptorTypeID;
            private final String raLink;
            Content(ResourceAdaptorTypeID resourceAdaptorTypeID, String raLink) {
                this.resourceAdaptorTypeID = resourceAdaptorTypeID;
                this.raLink = raLink;
            }
        }
    }
}
