package org.mobicents.slee.resource.parlay.util.corba;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.PortableServer.IdAssignmentPolicyValue;
import org.omg.PortableServer.IdUniquenessPolicyValue;
import org.omg.PortableServer.ImplicitActivationPolicyValue;
import org.omg.PortableServer.LifespanPolicyValue;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.RequestProcessingPolicyValue;
import org.omg.PortableServer.ServantRetentionPolicyValue;
import org.omg.PortableServer.ThreadPolicyValue;

/**
 * 
 * Class Description for PolicyFactory
 */
public class PolicyFactory {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(PolicyFactory.class);

    public PolicyFactory() {
        super();
    }
    /**
     * Creates the policy set applicable to a persistent poa.
     * @param poa
     * @return
     */
    public static org.omg.CORBA.Policy[] createPeristentPoaPolicies(final POA poa) {
        if(logger.isDebugEnabled()) {
            logger.debug("Creating PERSISTENT poa policies");
        }
        
        final org.omg.CORBA.Policy[] result = {
                poa.create_lifespan_policy(LifespanPolicyValue.PERSISTENT),
                poa.create_servant_retention_policy(ServantRetentionPolicyValue.RETAIN),
                poa.create_request_processing_policy(RequestProcessingPolicyValue.USE_ACTIVE_OBJECT_MAP_ONLY),
                poa.create_id_assignment_policy(IdAssignmentPolicyValue.USER_ID),
                poa.create_id_uniqueness_policy(IdUniquenessPolicyValue.UNIQUE_ID),
                poa.create_implicit_activation_policy(ImplicitActivationPolicyValue.NO_IMPLICIT_ACTIVATION),
                poa.create_thread_policy(ThreadPolicyValue.ORB_CTRL_MODEL) };

        return result;
    }

    /**
     * Creates the policy set applicable to a transient poa.
     * @param poa
     * @return
     */
    public static org.omg.CORBA.Policy[] createTransientPoaPolicies(final POA poa) {
        if(logger.isDebugEnabled()) {
            logger.debug("Creating TRANSIENT poa policies");
        }
        
        final org.omg.CORBA.Policy[] result = {
                poa.create_lifespan_policy(LifespanPolicyValue.TRANSIENT),
                poa.create_servant_retention_policy(ServantRetentionPolicyValue.RETAIN),
                poa.create_request_processing_policy(RequestProcessingPolicyValue.USE_ACTIVE_OBJECT_MAP_ONLY),
                poa.create_id_assignment_policy(IdAssignmentPolicyValue.SYSTEM_ID),
                poa.create_id_uniqueness_policy(IdUniquenessPolicyValue.UNIQUE_ID),
                poa.create_implicit_activation_policy(ImplicitActivationPolicyValue.NO_IMPLICIT_ACTIVATION),
                poa.create_thread_policy(ThreadPolicyValue.ORB_CTRL_MODEL) };

        return result;
    }
}