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
				int result = sbb2a.getPriority() - sbb1a.getPriority();
				if (result == 0) {
					// the older one wins
					result = (int) (sbb1a.getCreationDate().longValue() - sbb2a
							.getCreationDate().longValue());
					if (result == 0) {
						// id string compare, cause a 0 result means one entity is not added to set
						result = sbb1a.getSbbEntityId().compareTo(sbb2a.getSbbEntityId());
						System.out.println("SbbEntity id comparing result is "+result+" for sbb entity "+sbb1a.getSbbEntityId()+" and "+sbb2a.getSbbEntityId());
					}
				}
				
				return result;
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
