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

package org.mobicents.slee.container.util;

import org.apache.log4j.Logger;
import org.jboss.as.naming.ServiceBasedNamingStore;
import org.jboss.as.naming.ValueManagedReferenceFactory;
import org.jboss.as.naming.deployment.ContextNames;
import org.jboss.as.naming.service.BinderService;
import org.jboss.as.server.CurrentServiceContainer;
import org.jboss.msc.service.AbstractServiceListener;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.value.ImmediateValue;

import javax.naming.NamingException;

public class JndiRegistry {

    private static final Logger logger = Logger.getLogger(JndiRegistry.class);

    private ServiceContainer serviceContainer = null;

    public void setServiceContainer(final ServiceContainer serviceContainer) {
        this.serviceContainer = serviceContainer;
    }

    public void bindToJndi(String name, Object object) {
        ServiceContainer serviceContainer = this.serviceContainer;
        if (serviceContainer == null) {
            serviceContainer = CurrentServiceContainer.getServiceContainer();
        }

        logger.info("serviceContainer: "+serviceContainer);
        // Only register it in AS7 container.
        if (serviceContainer != null) {
            try {
                // creates binder service
                final ContextNames.BindInfo bindInfo = ContextNames.bindInfoFor(name);
                final BinderService binderService = new BinderService(bindInfo.getBindName());
                final BindListener listener = new BindListener();
                binderService.getManagedObjectInjector()
                        .inject(new ValueManagedReferenceFactory(new ImmediateValue<Object>(object)));
                // creates the service builder with dep to the parent jndi context
                ServiceBuilder<?> builder = serviceContainer
                        .addService(bindInfo.getBinderServiceName(), binderService)
                        .addDependency(bindInfo.getParentContextServiceName(),
                                ServiceBasedNamingStore.class, binderService.getNamingStoreInjector())
                        .setInitialMode(ServiceController.Mode.ACTIVE)
                        .addListener(listener);

                builder.install();
                listener.await();
                binderService.acquire();

            } catch (Throwable e) {
                final NamingException ne = new NamingException("Failed to bind "+ object + " at location " + name);
                ne.setRootCause(e);
                logger.error(ne);
            }
        }
    }

    public void unbindFromJndi(String name){
        ServiceContainer serviceContainer = this.serviceContainer;
        if (serviceContainer == null) {
            serviceContainer = CurrentServiceContainer.getServiceContainer();
        }

        logger.info("serviceContainer: "+serviceContainer);
        if (serviceContainer != null) {
            final ContextNames.BindInfo bindInfo = ContextNames.bindInfoFor(name);
            logger.info("bindInfo: "+bindInfo);
            final ServiceController<?> controller = serviceContainer.getService(bindInfo.getBinderServiceName());
            logger.info("controller: "+controller);
            if (controller != null) {
                final UnbindListener listener = new UnbindListener();
                controller.addListener(listener);

                try {
                    // when added, the listener stops the binding service
                    listener.await();
                } catch (Exception e) {
                    logger.error("Failed to unbind [" + name + "]", e);
                }
            }
        }
    }

    private static class BindListener extends AbstractServiceListener<Object> {
        private Exception exception;
        private boolean complete;

        public synchronized void transition(ServiceController<? extends Object> serviceController, ServiceController.Transition transition) {
            switch (transition) {
                case STARTING_to_UP: {
                    complete = true;
                    notifyAll();
                    break;
                }
                case STARTING_to_START_FAILED: {
                    complete = true;
                    exception = serviceController.getStartException();
                    notifyAll();
                    break;
                }
                default:
                    break;
            }
        }

        public synchronized void await() throws Exception {
            while(!complete) {
                wait();
            }
            if (exception != null) {
                throw exception;
            }
        }
    }

    private static class UnbindListener extends AbstractServiceListener<Object> {
        private boolean complete;

        public void listenerAdded(ServiceController<?> controller) {
            controller.setMode(ServiceController.Mode.REMOVE);
        }

        public synchronized void transition(ServiceController<? extends Object> serviceController, ServiceController.Transition transition) {
            switch (transition) {
                case REMOVING_to_REMOVED: {
                    complete = true;
                    notifyAll();
                    break;
                }
                default:
                    break;
            }
        }

        public synchronized void await() throws Exception {
            while(!complete) {
                wait();
            }
        }
    }

}