package org.mobicents.slee.example;

import java.util.UUID;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.TimerPreserveMissed;
import javax.slee.facilities.Tracer;
import javax.slee.profile.ProfileFacility;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.ProfileTable;
import javax.slee.profile.UnrecognizedProfileTableNameException;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;

public abstract class TimerExampleSbb implements javax.slee.Sbb {

	private Tracer tracer;

	private SbbContext sbbContext; // This SBB's SbbContext

	private TimerFacility timerFacility;
	private ProfileFacility profileFacility;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbActivate()
	 */
	public void sbbActivate() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbCreate()
	 */
	public void sbbCreate() throws CreateException {
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbExceptionThrown(java.lang.Exception,
	 *      java.lang.Object, javax.slee.ActivityContextInterface)
	 */
	public void sbbExceptionThrown(Exception arg0, Object arg1,
			ActivityContextInterface arg2) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbLoad()
	 */
	public void sbbLoad() {
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbPassivate()
	 */
	public void sbbPassivate() {
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbPostCreate()
	 */
	public void sbbPostCreate() throws CreateException {
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbRemove()
	 */
	public void sbbRemove() {
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbRolledBack(javax.slee.RolledBackContext)
	 */
	public void sbbRolledBack(RolledBackContext arg0) {
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#sbbStore()
	 */
	public void sbbStore() {
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#setSbbContext(javax.slee.SbbContext)
	 */
	public void setSbbContext(SbbContext context) {

		this.sbbContext = context;
		this.tracer = sbbContext.getTracer(TimerExampleSbb.class.getSimpleName());
		
		try {
			Context ctx = (Context) new InitialContext();
			timerFacility = (TimerFacility) ctx.lookup(TimerFacility.JNDI_NAME);
			profileFacility = (ProfileFacility) ctx.lookup(ProfileFacility.JNDI_NAME);			
		} catch (NamingException ne) {
			tracer.severe("Could not set SBB context:", ne);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.Sbb#unsetSbbContext()
	 */
	public void unsetSbbContext() {
		this.sbbContext = null;
		this.tracer = null;
		this.timerFacility = null;
		this.profileFacility = null;
	}

	// CMP
	public abstract void setProfile(ProfileLocalObject value);
	public abstract ProfileLocalObject getProfile();
	
	public void onStartServiceEvent(
			javax.slee.serviceactivity.ServiceStartedEvent event,
			ActivityContextInterface aci) {
		
		tracer.info("Service activated, creating profile and setting 5 minute timer.");

		// get profile table (create if needed)
		String profileTableName = "TimerExampleProfileTable";
		ProfileTable profileTable = null;
		try {
			profileTable = profileFacility.getProfileTable(profileTableName);
		}
		catch (UnrecognizedProfileTableNameException e) {
			/**
			 *  profile tables are created through JMX, this is just done this way to simplify example running, don't try this at home since you will be bound to mobicents specific implementation
			 */
			ProfileSpecificationID profileSpecificationID = new ProfileSpecificationID("TimerExampleProfile", "org.mobicents", "1.0");
			SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
			ProfileSpecificationComponent profileSpecificationComponent = sleeContainer.getComponentRepositoryImpl().getComponentByID(profileSpecificationID);
			try {
				sleeContainer.getSleeProfileTableManager().addProfileTable(profileTableName, profileSpecificationComponent);
				tracer.info("Created profile table with name "+profileTableName);

			}
			catch (Throwable f) {
				tracer.warning(f.getMessage(), f);
			}
			try {
				profileTable = profileFacility.getProfileTable(profileTableName);
			}
			catch (UnrecognizedProfileTableNameException f) {
				tracer.severe("can't proceed, unable to retrieve or create profile table", f);
			}
		}
		
		// create a profile, set value and store in cmp
		try {
			ProfileLocalObject profileLocalObject = profileTable.create(UUID.randomUUID().toString());
			((TimerExampleProfileCMP)profileLocalObject).setValue("the holy grail");
			setProfile(profileLocalObject);
			tracer.info("Created profile with name "+profileLocalObject.getProfileName()+" in table with name "+profileLocalObject.getProfileTableName());
		}
		catch (Throwable e) {
			tracer.severe("can't proceed, unable to create profile", e);
		}
		
		
		TimerOptions options = new TimerOptions();
		options.setPreserveMissed(TimerPreserveMissed.ALL);
		
		this.timerFacility.setTimer(aci, null, System
					.currentTimeMillis()
					+ 300000, options);
		
	}

	public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {
		// get profile and trace its name and 
		ProfileLocalObject profileLocalObject = getProfile();
		tracer.info("Timer event received, CMP field has a profile with name "+profileLocalObject.getProfileName()+" in table with name "+profileLocalObject.getProfileTableName()+". It stores "+((TimerExampleProfileCMP)profileLocalObject).getValue());
		/**
		 *  profile tables are removed through JMX, this is just done this way to simplify example running, don't try this at home since you will be bound to mobicents specific implementation
		 */
		try {
			SleeContainer.lookupFromJndi().getSleeProfileTableManager().removeProfileTable(profileLocalObject.getProfileTableName());
			tracer.info("Removed profile table with name "+profileLocalObject.getProfileTableName());

		}
		catch (Throwable f) {
			tracer.warning(f.getMessage(), f);
		}
	}

}
