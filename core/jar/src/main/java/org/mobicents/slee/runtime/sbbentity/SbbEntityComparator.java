package org.mobicents.slee.runtime.sbbentity;

import java.util.Comparator;
import java.util.LinkedList;

/**
 * Comparator used to sort a set of sbb entities.
 * 
 * @author martins
 * 
 */
public class SbbEntityComparator implements Comparator<String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(String sbbeId1, String sbbeId2) {

		SbbEntity sbbe1 = null;
		try {
			sbbe1 = SbbEntityFactory.getSbbEntity(sbbeId1);
		} catch (Exception e) {
			// ignore
		}

		SbbEntity sbbe2 = null;
		try {
			sbbe2 = SbbEntityFactory.getSbbEntity(sbbeId2);
		} catch (Exception e) {
			// ignore
		}

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

		while (true) {
			SbbEntity sbb1a = stack1.removeFirst();
			SbbEntity sbb2a = stack2.removeFirst();
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
			sbbe = SbbEntityFactory.getSbbEntity(sbbe.getParentSbbEntityId());
		}
		// push the root one
		list.addFirst(sbbe);

		return list;
	}
}
