package javax.slee.facilities;

import javax.slee.ActivityContextInterface;
import javax.slee.TransactionRequiredLocalException;

/**
 * The Activity Context Naming Facility provides a global flat namespace 
 * for naming Activity Contexts. It allows an SBB entity to bind a name to
 * an Activity Context, and other SBB entities to lookup the
 * Activity Context by this name. An Activity Context can be bound
 * to zero or more names.
 * <p>
 * This facility is transactional in nature.  Naming operations only take
 * effect once the enclosing transaction commits.
 * <p>
 * An SBB obtains access to an <code>ActivityContextNamingFacility</code> object
 * via its JNDI environment.  The Activity Context Naming Facility is bound into
 * JNDI using the name specified by {@link #JNDI_NAME}.
 */
public interface ActivityContextNamingFacility {
    /**
     * Constant declaring the JNDI name where an <code>ActivityContextNamingFacility</code>
     * object is bound into an SBB's JNDI environment.
     * <p>
     * The value of this constant is "java:comp/env/slee/facilities/activitycontextnaming".
     * @since SLEE 1.1
     */
    public static final String JNDI_NAME = "java:comp/env/slee/facilities/activitycontextnaming";

    /**
     * Bind an Activity Context to a name.
     * <p>
     * This method is a mandatory transactional method.
     * @param aci an <code>ActivityContextInterface</code> object that encapsulates
     *        the Activity Context to bind.
     * @param name the name to bind the Activity Context to.
     * @throws NullPointerException if any argument is <code>null</code>.
     * @throws IllegalArgumentException if <code>name</code> is zero-length.
     * @throws TransactionRequiredLocalException if this method is invoked without a
     *        valid transaction context.
     * @throws NameAlreadyBoundException if the specified name is already bound to
     *        some Activity Context.
     * @throws FacilityException if the activity context could not be bound due to
     *        a system-level failure.
     */
    public void bind(ActivityContextInterface aci, String name)
        throws NullPointerException, IllegalArgumentException,
               TransactionRequiredLocalException, NameAlreadyBoundException,
               FacilityException;

    /**
     * Unbind a bound name.
     * <p>
     * This method is a mandatory transactional method.
     * @param name the name that should be unbound.
     * @throws NullPointerException if <code>name</code> is <code>null</code>.
     * @throws TransactionRequiredLocalException if this method is invoked without a
     *        valid transaction context.
     * @throws NameNotBoundException if the specified name is not currently bound
     *        to an Activity Context.
     * @throws FacilityException if the name could not be unbound due to a
     *        system-level failure.
     */
    public void unbind(String name)
        throws NullPointerException, TransactionRequiredLocalException,
               NameNotBoundException, FacilityException;

    /**
     * Get a reference to the Activity Context bound to a particular name.
     * <p>
     * This method is a mandatory transactional method.
     * @param name the name the Activity Context is bound to.
     * @return an <code>ActivityContextInterface</code> object that encapsulates
     *        the Activity Context bound to the specified name.  If no Activity
     *        Context is bound to the name, this method returns <code>null</code>.
     * @throws NullPointerException if <code>name</code> is <code>null</code>.
     * @throws TransactionRequiredLocalException if this method is invoked without a
     *        valid transaction context.
     * @throws FacilityException if the lookup failed due to a system-level failure.
     */
    public ActivityContextInterface lookup(String name)
        throws NullPointerException, TransactionRequiredLocalException, FacilityException;

}
