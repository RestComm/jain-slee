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

package net.java.slee.resource.diameter.cca;

import javax.slee.ActivityContextInterface;

/**
 * Interface for factory implemented by the SLEE to create ACIs.
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a> 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 */
public interface CreditControlActivityContextInterfaceFactory {

  /**
   * Method for obtaining a ACI wrapping the given Credit-Control Client Session.
   * @param cccs the Credit-Control Client Session
   * @return an ActivityContextInterface
   */
  public ActivityContextInterface getActivityContextInterface(CreditControlClientSession cccs);

  /**
   * Method for obtaining a ACI wrapping the given Credit-Control Server Session.
   * @param ccss the Credit-Control Client Session
   * @return an ActivityContextInterface
   */
  public ActivityContextInterface getActivityContextInterface(CreditControlServerSession ccss);

}
