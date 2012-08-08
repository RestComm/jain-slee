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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query;

import org.mobicents.slee.container.component.profile.query.QueryOptionsDescriptor;

/**
 * Start time:11:20:06 2009-01-22<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MQueryOptions implements QueryOptionsDescriptor {

  private boolean readOnly;
  private long maxMatches;

  public MQueryOptions(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.QueryOptions queryOptions11)
  {
    this.readOnly = Boolean.parseBoolean(queryOptions11.getReadOnly());
    this.maxMatches = queryOptions11.getMaxMatches() == null ? -1 : Long.parseLong(queryOptions11.getMaxMatches());
  }

  public long getMaxMatches() {
    return maxMatches;
  }

  public boolean isReadOnly() {
    return readOnly;
  }

}
