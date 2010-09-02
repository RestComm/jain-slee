package org.mobicents.slee;

import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.resource.InvalidConfigurationException;

/**
 * Extension interface for a JAIN SLEE 1.1 SBB, provides lifecycles methods to
 * deal with config properties, in a similar way as Resource Adaptors work.
 * 
 * A new state is introduced, Unconfigured, which stands between Does Not Exists
 * and Pooled states. Unconfigured is reached from Does Not Exists after
 * invocation of {@link Sbb#setSbbContext(javax.slee.SbbContext)}, and after
 * invocation of {@link SbbExt#sbbConfigure(ConfigProperties)} the Pooled state
 * is reached. In the reverse direction of the state machine, instead of moving
 * from Pooled to Does Not Exists state, now it goes from Pooled to
 * Unconfigured, upon invocation of {@link SbbExt#sbbUnconfigure()}, and upon
 * invocation of {@link Sbb#unsetSbbContext()} it goes to Does Not Exists state
 * again.
 * 
 * @author Eduardo Martins
 * 
 */
public abstract class SbbExt implements Sbb {

	/**
	 * The SLEE invokes this method on a SBB object in the Unconfigured state to
	 * provide it with configuration properties common for any SBB entity that
	 * uses the object.
	 * <p>
	 * This method is invoked with an unspecified transaction context.
	 * 
	 * @param properties
	 *            the configuration properties specified for the SBB object.
	 */
	public void sbbConfigure(ConfigProperties properties) {
		
	}

	/**
	 * The SLEE invokes this method on a SBB object in the Pooled state,
	 * indicating the transition to Unconfigured state. The object should
	 * release any resources acquired when invocation of
	 * {@link SbbExt#sbbConfigure(ConfigProperties)} or
	 * {@link SbbExt#sbbConfigurationUpdate(ConfigProperties)}.
	 * <p>
	 * This method is invoked with an unspecified transaction context.
	 */
	public void sbbUnconfigure() {
		
	}

	/**
	 * This method is invoked by the SLEE whenever a new service, which refers
	 * the SBB, is activated by the Administrator, or when the Administrator
	 * attempts to update the configuration properties of an existing service
	 * which refers the SBB. The implementation of this method should examine
	 * the configuration properties supplied and verify that the configuration
	 * properties are valid for the SBB.
	 * <p>
	 * This method may be invoked on a SBB object in any valid state, therefore
	 * the implementation of this method should assume nothing about the
	 * internal state of the SBB object.
	 * <p>
	 * This method is invoked with an unspecified transaction context.
	 * 
	 * @param properties
	 *            contains the proposed new values for all configuration
	 *            properties specified for the SBB object.
	 * @throws InvalidConfigurationException
	 *             if the configuration properties are not valid for some
	 *             reason.
	 */
	public void sbbVerifyConfiguration(ConfigProperties properties)
			throws InvalidConfigurationException {
		
	}

	/**
	 * This method is invoked by the SLEE whenever the Administrator
	 * successfully updates a Service and SBB with new configuration properties.
	 * The implementation of this method should apply the new configuration
	 * properties to its internal state.
	 * <p>
	 * This method is invoked with an unspecified transaction context.
	 * 
	 * @param properties
	 *            contains the new values for all configuration properties
	 *            specified for the SBB object.
	 * @param properties
	 */
	public void sbbConfigurationUpdate(ConfigProperties properties) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.Sbb#sbbActivate()
	 */
	@Override
	public void sbbActivate() {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.Sbb#sbbCreate()
	 */
	@Override
	public void sbbCreate() throws CreateException {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.Sbb#sbbExceptionThrown(java.lang.Exception, java.lang.Object, javax.slee.ActivityContextInterface)
	 */
	@Override
	public void sbbExceptionThrown(Exception exception, Object event,
			ActivityContextInterface aci) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.Sbb#sbbLoad()
	 */
	@Override
	public void sbbLoad() {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.Sbb#sbbPassivate()
	 */
	@Override
	public void sbbPassivate() {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.Sbb#sbbPostCreate()
	 */
	@Override
	public void sbbPostCreate() throws CreateException {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.Sbb#sbbRemove()
	 */
	@Override
	public void sbbRemove() {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.Sbb#sbbRolledBack(javax.slee.RolledBackContext)
	 */
	@Override
	public void sbbRolledBack(RolledBackContext context) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.Sbb#sbbStore()
	 */
	@Override
	public void sbbStore() {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.Sbb#setSbbContext(javax.slee.SbbContext)
	 */
	@Override
	public void setSbbContext(SbbContext context) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.Sbb#unsetSbbContext()
	 */
	@Override
	public void unsetSbbContext() {
		
	}
}
