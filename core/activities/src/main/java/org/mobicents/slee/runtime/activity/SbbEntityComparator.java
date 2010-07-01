package org.mobicents.slee.runtime.activity;

import java.util.Comparator;
import java.util.LinkedList;

import org.mobicents.slee.container.sbbentity.SbbEntity;
import org.mobicents.slee.container.sbbentity.SbbEntityFactory;

/**
 * Comparator used to sort a set of sbb entities.
 * 
 * @author martins
 * 
 */
public class SbbEntityComparator implements Comparator<String> {

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
	public int compare(String sbbeId1, String sbbeId2) {

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
		while (!sbbe.isRootSbbEntity()) {
			list.addFirst(sbbe);
			sbbe = sbbEntityFactory.getSbbEntity(sbbe.getParentSbbEntityId(),false);
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
