package org.mobicents.slee;

import javax.slee.profile.Profile;
import javax.slee.profile.ProfileContext;
import javax.slee.resource.InvalidConfigurationException;

/**
 * Extension interface for a JAIN SLEE 1.1 Profile, provides lifecycles methods
 * to deal with config properties, in a similar way as Resource Adaptors work.
 * 
 * A new state is introduced, Unconfigured, which stands between Does Not Exists
 * and Pooled states. Unconfigured is reached from Does Not Exists after
 * invocation of {@link Profile#setProfileContext(ProfileContext)}, and after
 * invocation of {@link ProfileExt#profileConfigure(ConfigProperties)} the
 * Pooled state is reached. In the reverse direction of the state machine,
 * instead of moving from Pooled to Does Not Exists state, now it goes from
 * Pooled to Unconfigured, upon invocation of
 * {@link ProfileExt#profileUnconfigure()}, and upon invocation of
 * {@link Profile#unsetProfileContext()} it goes to Does Not Exists state again.
 * 
 * @author Eduardo Martins
 * 
 */
public interface ProfileExt extends Profile {

	/**
	 * The SLEE invokes this method on a Profile object in the Unconfigured
	 * state to provide it with configuration properties common for any Profile
	 * entity that uses the object.
	 * <p>
	 * This method is invoked with an unspecified transaction context.
	 * 
	 * @param properties
	 *            the configuration properties specified for the Profile object.
	 */
	public void profileConfigure(ConfigProperties properties);

	/**
	 * The SLEE invokes this method on a Profile object in the Pooled state,
	 * indicating the transition to Unconfigured state. The object should
	 * release any resources acquired when invocation of
	 * {@link ProfileExt#profileConfigure(ConfigProperties)} or
	 * {@link ProfileExt#profileConfigurationUpdate(ConfigProperties)}.
	 * <p>
	 * This method is invoked with an unspecified transaction context.
	 */
	public void profileUnconfigure();

	/**
	 * This method is invoked by the SLEE whenever a new Profile Spec, which
	 * refers the abstract Profile class, is activated by the Administrator, or
	 * when the Administrator attempts to update the configuration properties.
	 * The implementation of this method should examine the configuration
	 * properties supplied and verify that the configuration properties are
	 * valid for the Profile.
	 * <p>
	 * This method may be invoked on a Profile object in any valid state,
	 * therefore the implementation of this method should assume nothing about
	 * the internal state of the Profile object.
	 * <p>
	 * This method is invoked with an unspecified transaction context.
	 * 
	 * @param properties
	 *            contains the proposed new values for all configuration
	 *            properties specified for the Profile object.
	 * @throws InvalidConfigurationException
	 *             if the configuration properties are not valid for some
	 *             reason.
	 */
	public void profileVerifyConfiguration(ConfigProperties properties)
			throws InvalidConfigurationException;

	/**
	 * This method is invoked by the SLEE whenever the Administrator
	 * successfully updates a Profile Spec with new configuration properties.
	 * The implementation of this method should apply the new configuration
	 * properties to its internal state.
	 * <p>
	 * This method is invoked with an unspecified transaction context.
	 * 
	 * @param properties
	 *            contains the new values for all configuration properties
	 *            specified for the Profile object.
	 * @param properties
	 */
	public void profileConfigurationUpdate(ConfigProperties properties);

}
