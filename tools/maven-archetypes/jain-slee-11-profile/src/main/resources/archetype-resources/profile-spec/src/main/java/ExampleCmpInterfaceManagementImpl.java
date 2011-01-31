package ${package};

import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.CreateException;
import javax.slee.profile.Profile;
import javax.slee.profile.ProfileContext;

import javax.slee.profile.ProfileVerificationException;

/**
 * Profile Management implementation class.
 */
public abstract class ExampleCmpInterfaceManagementImpl implements Profile,ExampleCmpInterface {

	private ProfileContext profileCtx;

	/**
	 * Initialize the profile with its default values.
	 */
	public void profileInitialize() {
		setIdentity(null);
	}

	public void profileLoad() {
	}

	public void profileStore() {
	}

	/**
	 * Verify the profile's CMP field settings.
	 * 
	 * @throws ProfileVerificationException
	 *             if any CMP field contains an invalid value
	 */
	public void profileVerify() throws ProfileVerificationException {
		String s = getIdentity(); 
		if(s !=null)
		{
			if(s.indexOf(":")!=-1)
			{
				throw new ProfileVerificationException("Illegal char \":\"");
			}
		}
	}

	public void profileActivate() {
		// TODO Auto-generated method stub

	}

	public void profilePassivate() {
		// TODO Auto-generated method stub

	}

	public void profilePostCreate() throws CreateException {
		// TODO Auto-generated method stub

	}

	public void profileRemove() {
		// TODO Auto-generated method stub

	}

	public void setProfileContext(ProfileContext ctx) {
		this.profileCtx = ctx;

	}

	public void unsetProfileContext() {
		// TODO Auto-generated method stub
		this.profileCtx = null;
	}

}