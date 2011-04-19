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

package org.mobicents.slee.runtime.activity;

import java.util.Comparator;
import java.util.LinkedList;

import org.mobicents.slee.container.sbbentity.SbbEntity;
import org.mobicents.slee.container.sbbentity.SbbEntityFactory;
import org.mobicents.slee.container.sbbentity.SbbEntityID;

/**
 * Comparator used to sort a set of sbb entities.
 * 
 * @author martins
 * 
 */
public class SbbEntityComparator implements Comparator<SbbEntityID> {

	private final SbbEntityFactory sbbEntityFactory;
	
	/**
	 * @param sbbEntityFactory
	 */
	public SbbEntityComparator(SbbEntityFactory sbbEntityFactory) {
		this.sbbEntityFactory = sbbEntityFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(SbbEntityID sbbeId1, SbbEntityID sbbeId2) {

		final SbbEntity sbbe1 = sbbEntityFactory.getSbbEntity(sbbeId1,true);
		final SbbEntity sbbe2 = sbbEntityFactory.getSbbEntity(sbbeId2,true);
		
		if (sbbe1 == null) {
			if (sbbe2 == null) {
				return 0;
			} else {
				return 1;
			}
		} else {
			if (sbbe2 == null) {
				return -1;
			} else {
				return higherPrioritySbb(sbbe1, sbbe2);
			}
		}

	}

	/**
	 * 
	 * @param sbbe1
	 * @param sbbe2
	 * @return
	 */
	private int higherPrioritySbb(SbbEntity sbbe1, SbbEntity sbbe2) {

		final LinkedList<SbbEntity> stack1 = priorityOfSbb(sbbe1);
		final LinkedList<SbbEntity> stack2 = priorityOfSbb(sbbe2);
		
		// stacks may be null if sbb entity is removed concurrently
		if (stack1 == null) {
			if (stack2 == null) {
				return 0;
			} else {
				return 1;
			}
		}
		else {
			if (stack2 == null) {
				return -1;
			}
		}
		
		// both are non null
		SbbEntity sbb1a = null;
		SbbEntity sbb2a = null;
		while (true) {
			sbb1a = stack1.removeFirst();
			sbb2a = stack2.removeFirst();
			if (sbb1a == sbb2a) {
				// sbb entities have the same ancestor.
				if (stack1.isEmpty()) {
					return -1;
				} else if (stack2.isEmpty()) {
					return 1;
				}
			} else {
				if (sbb1a.getPriority() > sbb2a.getPriority()) {
					return -1;
				}
				else {
					return 1;
				}				
			}
		}
	}

	/**
	 * 
	 * @param sbbe
	 * @return
	 */
	private LinkedList<SbbEntity> priorityOfSbb(SbbEntity sbbe) {
		final LinkedList<SbbEntity> list = new LinkedList<SbbEntity>();
		// push all non root sbb entities
		while (!sbbe.getSbbEntityId().isRootSbbEntity()) {
			list.addFirst(sbbe);
			sbbe = sbbEntityFactory.getSbbEntity(sbbe.getSbbEntityId().getParentSBBEntityID(),false);
			if (sbbe == null) {
				// edge case where a parent sbb entity was concurrently removed, ignore this sbb entity
				return null;
			}
		}
		// push the root one
		list.addFirst(sbbe);

		return list;
	}
	
}
