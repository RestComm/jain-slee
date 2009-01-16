package javax.slee;

import java.util.Iterator;
import java.util.Collection;

/**
 * The <code>ChildRelation</code> interface is used by SBBs that require child relationships
 * with other SBBs.  Any child-relation accessor method defined in an SBB abstract class
 * must have this interface as its return type.
 * <p>
 * A <code>ChildRelation</code> object, and any <code>java.util.Iterator</code> objects
 * obtained from it, are only valid within the transaction in which they materialized.
 * <p>
 * A <code>ChildRelation</code> object conforms to the Java Collections API with the
 * following exceptions:
 * <ul>
 *   <li>The <code>add</code> and <code>addAll</code> methods throw
 *       <code>java.lang.UnsupportedOperationException</code> if an attempt is made to modify
 *       the collection.  The {@link #create create} method should be used instead to add child SBB
 *       entities to the collection.
 *   <li>If an SBB entity is removed from a collection as a result of a <code>clear</code>,
 *       <code>remove</code>, <code>removeAll</code>, or <code>retainAll</code> method call on
 *       a <code>ChildRelation</code> object, or by a <code>remove</code> method call on
 *       a <code>java.util.Iterator</code> object obtained from a <code>ChildRelation</code>
 *       object, a cascade delete of the SBB entity tree rooted at the removed SBB entity
 *       is initiated.  This means that removing an SBB entity from a <code>ChildRelation</code>
 *       collection has the same behavior as if the {@link SbbLocalObject#remove remove}
 *       method was invoked on the removed SBB entity itself.
 * </ul>
 * If a null parameter is passed to any method, a <code>java.lang.NullPointerException</code>
 * is thrown.
 */
public interface ChildRelation extends Collection {
    /**
     * Create a new SBB entity of the SBB type associated with the relation.  The new
     * SBB entity is automatically added to the relationship collection.  The returned object
     * may be cast to the required local interface type using the normal Java typecast
     * mechanism.
     * <p>
     * This method is a mandatory transactional method.
     * @return a local object reference to the created SBB entity.
     * @throws CreateException if an SBB entity could not be created.  This exception
     *        may have propagated from the SBB object's {@link Sbb#sbbCreate} or {@link
     *        Sbb#sbbPostCreate}, or may have been thrown by the SLEE.
     * @throws TransactionRequiredLocalException if this method is invoked without a valid transaction
     *        context.
     * @throws SLEEException if the child SBB could not be created due to a system-level
     *        failure.
     */
    public SbbLocalObject create()
        throws CreateException, TransactionRequiredLocalException, SLEEException;

}

