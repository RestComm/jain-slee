package org.mobicents.sleetests.container.installService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.ActivityContextInterface;
import javax.slee.EventTypeID;
import javax.slee.InitialEventSelector;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedActivityException;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.TimerPreserveMissed;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.EventTypeIDImpl;
import org.mobicents.slee.container.component.MobicentsSbbDescriptor;
import org.mobicents.slee.container.component.SbbEventEntry;
import org.mobicents.slee.container.component.ServiceDescriptorImpl;
import org.mobicents.slee.container.component.ServiceIDImpl;
import org.mobicents.slee.container.service.ServiceComponent;
import org.mobicents.slee.resource.EventLookup;
import org.mobicents.slee.resource.ResourceAdaptorType;
import org.mobicents.slee.resource.ResourceAdaptorTypeIDImpl;

import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;
import com.opencloud.sleetck.lib.resource.events.TCKResourceEventY;
import com.opencloud.sleetck.lib.resource.impl.TCKResourceEventImpl;
import com.opencloud.sleetck.lib.sbbutils.BaseTCKSbb;
import com.opencloud.sleetck.lib.sbbutils.TCKSbbUtils;

public abstract class InstallServiceTestSbb extends BaseTCKSbb {

	public static final int SERVICE_NAME_POSITION=0;
	public static final int EVENT_IDS_POSITION=1;
	public static final int RESOURCE_OPTIONS_POSITION=2;
	public static final int RA_TYPE_COMPONENT_KEY=3;
	public static final int RA_COMPONENT_KEY=4;
	public static final int COMPONENTKEYS_TO_SERVICEIDS_POSITION=5;
	public static final int COMPONENTKEYS_TO_OPTIONS_POSITION=6;
	
	private ServiceDescriptorImpl serviceDesc = null;
	protected ActivityContextInterface nullACIForTimer = null;

	protected TimerOptions tOptions = new TimerOptions(false, 5000,
			TimerPreserveMissed.LAST);
	/*
	 * protected boolean cancelingEventReceived = false;
	 * 
	 * protected boolean dialogTerminatedEventReceived = false;
	 * 
	 * protected boolean dialogSetupFialedEventReceived = false;
	 */
	protected static Logger logger = Logger.getLogger("InstallServiceTestSbb.class");

	protected TimerFacility tf = null;
	
	private static SleeContainer container = null;
	private static EventLookup eventLookup=null;
	
	
	
	public abstract void setServiceID(String serviceID);
	public abstract String getServiceID();
	//private String serviceID = null;
	public abstract void setSvc(ServiceComponent svc);
	public abstract ServiceComponent getSvc();
	//private ServiceComponent svc =null;   //SVC COMPONENT FOR WHICH WE RUN
	public abstract void setRaTypeKey(ComponentKey raTypeKey);
	public abstract ComponentKey getRaTypeKey();
	//private ComponentKey raTypeKey = null;
	public abstract void setRaKey(ComponentKey raKey);
	public abstract ComponentKey getRaKey();
	//private ComponentKey raKey = null;
	



	//GATHERED FROM X1/ OR raKey, raTypeKey and service Id can be gathered from y1
	public abstract void setX1_eventIDsPassedToRABySleeContainer(int[] X1_eventIDsPassedToRABySleeContainer);
	public abstract int[] getX1_eventIDsPassedToRABySleeContainer();
	//private int[] X1_eventIDsPassedToRABySleeContainer = null;
	public abstract void setX1_optionsPassedToRABySleeContainer(String[] X1_optionsPassedToRABySleeContainer);
	public abstract String[] getX1_optionsPassedToRABySleeContainer();
	//private String[] X1_optionsPassedToRABySleeContainer = null;
	public abstract void setX1_eventComponentKeysToServiceIdSets_RA_STATE(Map X1_eventComponentKeysToServiceIdSets_RA_STATE);
	public abstract Map getX1_eventComponentKeysToServiceIdSets_RA_STATE();

	
	
	public abstract void setX1_areAllPassedEventIDsInRaTypeDesc(boolean X1_areAllPassedEventIDsInRaTypeDesc);
	public abstract boolean getX1_areAllPassedEventIDsInRaTypeDesc();
	//private boolean X1_areAllPassedEventIDsInRaTypeDesc=false;
	public abstract void setX1_eventComponentKeysThatAreNotInRaDesc(HashSet X1_eventComponentKeysThatAreNotInRaDesc);
	public abstract  HashSet getX1_eventComponentKeysThatAreNotInRaDesc();
	//private HashSet X1_eventComponentKeysThatAreNotInRaDesc=new HashSet();
	public abstract void setX1_haveEventIDPassedbySleeContainerBeenDoubled(boolean X1_haveEventIDPassedbySleeContainerBeenDoubled);
	public abstract boolean getX1_haveEventIDPassedbySleeContainerBeenDoubled();
	//private boolean X1_haveEventIDPassedbySleeContainerBeenDoubled=true;                                      //if YES SOME EVENT IDS  PASSED BY SLEE ARE DOUBLED!!!! WRONG
	public abstract void setX1_areAllEventIDsPassedToRADecalredByService(boolean X1_areAllEventIDsPassedToRADecalredByService);
	public abstract boolean getX1_areAllEventIDsPassedToRADecalredByService();
	
	
	public abstract void setEndMessage(StringBuffer sb);
	public abstract StringBuffer getEndMessage();
	// TODO: Perform further operations if required in these methods.
	public void setSbbContext(SbbContext context) {
		this.sbbContext = context;
		container=SleeContainer.lookupFromJndi();
		eventLookup=container.getEventLookupFacility();
	}

	public void unsetSbbContext() {
		this.sbbContext = null;
	}

	// TODO: Implement the lifecycle methods if required
	public void sbbCreate() throws javax.slee.CreateException {
	}

	public void sbbPostCreate() throws javax.slee.CreateException {
	}

	public void sbbActivate() {
	}

	public void sbbPassivate() {
	}

	public void sbbRemove() {
	}

	public void sbbLoad() {
	}

	public void sbbStore() {
	}

	public void sbbExceptionThrown(Exception exception, Object event,
			ActivityContextInterface activity) {
	}

	public void sbbRolledBack(RolledBackContext context) {
	}

	/**
	 * Convenience method to retrieve the SbbContext object stored in
	 * setSbbContext.
	 * 
	 * TODO: If your SBB doesn't require the SbbContext object you may remove
	 * this method, the sbbContext variable and the variable assignment in
	 * setSbbContext().
	 * 
	 * @return this SBB's SbbContext object
	 */

	protected SbbContext getSbbContext() {

		return sbbContext;
	}

	/**
	 * Does JNDI lookup to create new reference to TimerFacility. If its
	 * successful it stores it in CMP field "timerFacility" and returns this
	 * reference.
	 * 
	 * @return TimerFacility object reference
	 */
	protected TimerFacility retrieveTimerFacility() {
		try {

			Context myEnv = (Context) new InitialContext()
					.lookup("java:comp/env");
			TimerFacility tf = (TimerFacility) myEnv
					.lookup("slee/facilities/timer");

			return tf;
		} catch (NamingException NE) {
			logger.info("COULDNT GET TIMERFACILITY: " + NE.getMessage());
		}
		return null;
	}

	/**
	 * Encapsulates JNDI lookups for creation of nullActivityContextInterface.
	 * 
	 * @return New NullActivityContextInterface.
	 */
	protected ActivityContextInterface retrieveNullActivityContext() {
		ActivityContextInterface localACI = null;
		NullActivityFactory nullACFactory = null;
		NullActivityContextInterfaceFactory nullActivityContextFactory = null;
		try {
			logger.info("Creating nullActivity!!!");
			Context myEnv = (Context) new InitialContext()
					.lookup("java:comp/env");
			nullACFactory = (NullActivityFactory) myEnv
					.lookup("slee/nullactivity/factory");
			NullActivity na = nullACFactory.createNullActivity();
			nullActivityContextFactory = (NullActivityContextInterfaceFactory) myEnv
					.lookup("slee/nullactivity/activitycontextinterfacefactory");
			localACI = nullActivityContextFactory
					.getActivityContextInterface(na);

		} catch (NamingException ne) {
			logger.info("Could not create nullActivityFactory: "
					+ ne.getMessage());
		} catch (UnrecognizedActivityException UAE) {
			logger
					.info("Could not create nullActivityContextInterfaceFactory: "
							+ UAE.getMessage());
		}
		return localACI;
	}

	protected void setResultPassed(String msg) throws Exception {
		logger.info("Success: " + msg);

		HashMap sbbData = new HashMap();
		sbbData.put("result", Boolean.TRUE);
		sbbData.put("message", msg);
		TCKSbbUtils.getResourceInterface().sendSbbMessage(sbbData);
	}

	protected void setResultFailed(String msg) throws Exception {
		logger.info("Failed: " + msg);

		HashMap sbbData = new HashMap();
		sbbData.put("result", Boolean.FALSE);
		sbbData.put("message", msg);
		TCKSbbUtils.getResourceInterface().sendSbbMessage(sbbData);
	}

	protected SbbContext sbbContext; // This SBB's SbbContext

	protected void activateTimer() {
		tf.setTimer(nullACIForTimer, null, System.currentTimeMillis() + 20000,
				tOptions); // 10
	}

	protected void activateTimer(int miliseconds) {
		tf.setTimer(nullACIForTimer, null, System.currentTimeMillis()
				+ ((long) miliseconds) * 1000, tOptions); // 10
	}

	public void onTimeEvent(javax.slee.facilities.TimerEvent event,
			ActivityContextInterface aci) {
		
	}

	
	
	public void onStartServiceEvent(
			javax.slee.serviceactivity.ServiceStartedEvent event,
			ActivityContextInterface aci) {
		
	}
	
	
	public void onTCKResourceEventX1(TCKResourceEventX event,
			ActivityContextInterface aci) {
		try {
			
			logger.info("\n------------------------- X1 -----------------------");
			
			Object[] message = (Object[]) event.getMessage();

			setServiceID( (String) message[SERVICE_NAME_POSITION]);
			setRaTypeKey ((ComponentKey) message[RA_TYPE_COMPONENT_KEY]);
			setRaKey((ComponentKey) message[RA_COMPONENT_KEY]);
			setX1_eventIDsPassedToRABySleeContainer((int[]) message[EVENT_IDS_POSITION]);
			setX1_eventComponentKeysToServiceIdSets_RA_STATE( (Map) message[COMPONENTKEYS_TO_SERVICEIDS_POSITION]);
			//printMessage(message);
			//printState("X1");
			
			
			String serviceClearID = getServiceID().toString().split("\\[")[1].split("\\]")[0];
			ComponentKey serviceKey = new ComponentKey(serviceClearID);
			ServiceID serviceID = new ServiceIDImpl(serviceKey);
			serviceDesc = (ServiceDescriptorImpl) container.getComponentManagement().getComponentDescriptor(serviceID);
			                                                		
			if(serviceDesc==null)
			       throw new IllegalStateException(" SERVICE DESC FOR:\""+serviceID+"\" RETURNED BY SleeContainerImpl is null.");
			                                                		
			setSvc( new ServiceComponent(serviceDesc));
			
			
			setX1_eventComponentKeysThatAreNotInRaDesc(new HashSet());
				
			
			setEndMessage(new StringBuffer("\n"));
			StringBuffer raport=getEndMessage();
			raport.append("\n|========================================================================================|");
			raport.append("\n|                         FINAL RAPORT FOR:                                              |");
			raport.append("\n|========================================================================================|");
			String text="| RATYPE:"+getRaTypeKey();
			String white="                                                                                                                            ";
			raport.append("\n"+text+white.substring(0,90-text.length()-1)+"|");
			text= "| RA    :"+getRaKey();
			raport.append("\n"+text+white.substring(0,90-text.length()-1)+"|");
			raport.append("\n|========================================================================================|");
			
			
			logger.info("\n============== X1 ====================\n" + getServiceID() + "\n"
					+ getRaTypeKey() + "\n" + "" + getRaKey()
					+ "\n==================================");
			
			//LETS MAKE SURE THAT SERVICES FOR OTHER RA's ARE UP - IN CASE WHICH RA
			//DOES NTO SEND ANYTHING TEST SHOULD FAIL, SO WE NEED SERVIES TO BE UP FOR THEM
			
			doFirstStageTest();
			
			
			String dots="................................................................................................................";
			text="| 1. Events passed to RA by container:";
			raport.append("\n"+text+dots.substring(0,80-text.length())+(getX1_areAllPassedEventIDsInRaTypeDesc()?"[PASSED] |":"[FAILED] |"));
			text="| 2. Test ids of events passed against doubling.";
			raport.append("\n"+text+dots.substring(0,80-text.length())+(!getX1_haveEventIDPassedbySleeContainerBeenDoubled()?"[PASSED] |":"[FAILED] |"));
			text="| 3. Test service against event ids.";
			raport.append("\n"+text+dots.substring(0,80-text.length())+(getX1_areAllEventIDsPassedToRADecalredByService()?"[PASSED] |":"[FAILED] |"));
			raport.append("\n|----------------------------------------------------------------------------------------|");
			raport.append("\n| TESTs EXAPLANATIONS:                                                                   |");
			raport.append("\n| Tests 1-3 are meant to test if SleeContainerImpl conforms to Slee Specs 1.1            |");
			raport.append("\n| section 15.4.2.15 page 302.                                                            |");
			raport.append("\n|----------------------------------------------------------------------------------------|");
			raport.append("\n| 1. Checks if SleeContainer passes eventIDs of events that are decalred in              |");
			raport.append("\n|    RA TYPE that this RA implements.                                                    |");
			raport.append("\n| 2. Chcecks if SleeContainer did not double eventIDs - this can happen when             |");
			raport.append("\n|    more than one sbb decalres that it wants to receive particualr event.               |");
			raport.append("\n| 3. Checks if eventIDs passed by SleeContainer are of interest of service.              |");
			raport.append("\n|========================================================================================|");
			raport.append("\n| SOME DEBUG INFO:                                                                       |");
			raport.append("\n|----------------------------------------------------------------------------------------|");
			raport.append("\n| EVENTS DECALRED BY RA TYPE:                                                            |");
			ResourceAdaptorTypeIDImpl raTypeID=new ResourceAdaptorTypeIDImpl(getRaTypeKey());
			ResourceAdaptorType raType=container.getResourceManagement().getResourceAdaptorType(raTypeID);
			ComponentKey[] raTypEventRefs=raType.getRaTypeDescr().getEventTypeRefEntries();
			text="\n| ";
			try{
			for(int i=0;i<raTypEventRefs.length;i++)
			{
				if(89-text.length()<=0)
					white+=white;
				if( (text+raTypEventRefs[i]).length()>88)
				{
					
					raport.append(text+white.substring(0,89-text.length())+" |");
					text="\n| ";
				}
				else
					text+=raTypEventRefs[i];
			}
			
			raport.append(text+white.substring(0,89-text.length())+" |");
			}catch(Exception e)
			{ 
				text+="ERROR!!!";
				if(89-text.length()<=0)
					white+=white;
				raport.append(text+white.substring(0,89-text.length())+" |");
				e.printStackTrace();
			}
			raport.append("\n|                                                                                        |");
			raport.append("\n| EVENTS PASSED TO RA ON INSTALL:                                                        |");
			text="\n| ";
			int[] eventIDs=getX1_eventIDsPassedToRABySleeContainer();
			logger.info("EVENT NUMBER:"+eventIDs.length);
			try{
			for(int i=0;i<eventIDs.length;i++)
			{
				ComponentKey key= ((EventTypeIDImpl)eventLookup.getEventTypeID(eventIDs[i])).getComponentKey();
				logger.info("KEY:"+key);
				//if( (text+key).length()>78)
				//{
					text+=key;
					if(89-text.length()<=0)
						white+=white;
					raport.append(text+white.substring(0,89-text.length())+" |");
					text="\n| ";
				//}
				//else
				//	text+=key;
				}
				raport.append(text+white.substring(0,89-text.length())+" |");
			}catch(Exception e)
			{ 
				text+="ERROR!!!";
				if(89-text.length()<=0)
					white+=white;
				raport.append(text+white.substring(0,89-text.length())+" |");
				e.printStackTrace();
			}
			raport.append("\n|                                                                                        |");                                                                    
			raport.append("\n| EVENTS DECLARED BY SERVICE:                                                            |");
			try{
				Set sbbIDs=getSvc().getSbbComponents();
				Iterator sbbIdsIterator=sbbIDs.iterator();
				int sbbNum=0;
				while(sbbIdsIterator.hasNext())
				{

					MobicentsSbbDescriptor  sbbdesc = container.getSbbManagement().getSbbComponent((SbbID)sbbIdsIterator.next());
					if(sbbdesc==null)
						continue;
	        	
					//EventTypeID[] eventTypeIDs = sbbdesc.getEventTypes();
	        	
					//HashMap sbbResourceOptionsForEventsIfInterest=new HashMap(20);
					//maps EventTypeID to coresponding SbbEventEntry
					//IT CONTAINS MAPPING FOR ALL EVENTS THAT ARE OF INTEREST IF THIS SBB
					HashMap eventTypeIdToEventEntriesMappings=sbbdesc.getEventTypesMappings();
					
					Iterator it=eventTypeIdToEventEntriesMappings.values().iterator();
					text="\n| SBB:"+sbbNum+++"#EVENTS COUNT:"+eventTypeIdToEventEntriesMappings.values().size();
					if(89-text.length()<=0)
						white+=white;
					raport.append(text+white.substring(0,89-text.length())+" |");
					text="\n| ";
					ComponentKey key=null;
					String valueOfKey=null;
					int counter=0;
					while(it.hasNext())
					{
						
						Object o=it.next();
						SbbEventEntry eventEntry=(SbbEventEntry) o;
						try{
							key=eventEntry.getEventTypeRefKey();
							valueOfKey=key.toString();
							//valueOfKey=o.toString();
						}catch(Exception e1)
						{
							e1.printStackTrace();
							valueOfKey="["+counter+"] -> VALUE NOT VALID?: "+o; 
						}catch(Error er)
						{
							er.printStackTrace();
							valueOfKey="["+counter+"] -> VALUE NOT VALID?: "+o; 
						}
						
						//if( (text+key).length()>78)
						//{
							text+="\""+valueOfKey+"\"";
							if(89-text.length()<=0)
								white+=white;
							raport.append(text+white.substring(0,89-text.length())+" |");
							text="\n| ";
						//}
						//else
						//	text+=key;
							counter++;
					}
				}
				
				raport.append(text+white.substring(0,89-text.length())+" |");
			}catch(Exception e)
			{ 
				text+="ERROR!!!";
				e.printStackTrace();
				//raport.append(text+white.substring(0,79-text.length())+" |");
				
			}
			raport.append("\n|                                                                                        |");
			
			if(getX1_eventComponentKeysThatAreNotInRaDesc()==null)
			{   //strange its not retained after creation i sservice started....
				setX1_eventComponentKeysThatAreNotInRaDesc(new HashSet());
				
			}
			HashSet set=getX1_eventComponentKeysThatAreNotInRaDesc();
			//log.info("\n========================== "+set+"===========================");
			//set.add(new ComponentKey("1","2","3"));
			if(set.size()>0)
			{
				raport.append("\n| EVENTS PASSED BUT NOT PRESENT IN RA TYPE DESC:                                         |");
				logger.log(Level.FINEST,"\n========================== 1 ===========================");
				Iterator it=set.iterator();
				try{
					
					text="\n| ";
					while(it.hasNext())
					{
						
						logger.log(Level.FINEST,"\n========================== 2 ===========================");
						ComponentKey key=(ComponentKey) it.next();
						if(89-(text+key).length()<=0)
							white+=white;
						if( (text+key).length()>88)
						{
							logger.log(Level.FINEST,"\n========================== 3 ===========================");
							raport.append(text+white.substring(0,89-text.length())+" |");
							text="\n| ";
						}
						else
							text+=key;
					}
					logger.log(Level.FINEST,"\n========================== 4 ===========================");
					raport.append(text+white.substring(0,89-text.length())+" |");
				}catch(Exception e)
				{ 
					text+="ERROR!!!";
					raport.append(text+white.substring(0,89-text.length())+" |");
					e.printStackTrace();
				}
			}
			//NOW LETS TEST IF eventIDsPassedToRABySleeContainer contain VALID eventIDs for RA and SERVICE
		} catch (Exception e) {
			try {
				setResultFailed(getEndMessage()+"\nERROR:\n"+e);
				
			} catch (Exception e1) {
				TCKSbbUtils.handleException(e1);
				e1.printStackTrace();
				return;
			}
			e.printStackTrace();
			return;
		}
		
		
		
		try{
		//NOW LETS CHECH IF IT IS SUCCES OR NOT:
			if(getX1_areAllPassedEventIDsInRaTypeDesc() && !getX1_haveEventIDPassedbySleeContainerBeenDoubled() && getX1_areAllEventIDsPassedToRADecalredByService())
			{
				setResultPassed(getEndMessage().toString());
			}
			else
			{
				setResultFailed(getEndMessage().toString());
			}
		} catch (Exception e) {
			try {
				setResultFailed(getEndMessage()+"\nERROR:\n"+e);
			} catch (Exception e1) {
				TCKSbbUtils.handleException(e1);
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	/**
	 * Tests if eventIDsPassedToRABySleeContainer ( future test should also include resourceOptions )
	 * contain valid eventIDs for RA. After that it checks if service decalres that it has interest in them all.
	 *
	 */
	private void doFirstStageTest() throws Exception
	{

		ResourceAdaptorTypeIDImpl raTypeID=new ResourceAdaptorTypeIDImpl(getRaTypeKey());

		ResourceAdaptorType raType=container.getResourceManagement().getResourceAdaptorType(raTypeID);
		//HERE WE HAVE raType DECALRED EVENTs

		EventTypeID[] eventTypeIDs=raType.getRaTypeDescr().getEventTypes();

		HashSet setOfEventIDsRaTypeDecalers=new HashSet(20);
		
		for(int i=0;i<eventTypeIDs.length;i++)
		{

			ComponentKey key= ((EventTypeIDImpl)eventTypeIDs[i]).getComponentKey();

			int eventID=eventLookup.getEventID(key);
			if(eventID==-1)
			    throw new Exception(" EVENT: "+key+" IS NOT REGISTERED EVENT TYPE!!!");
			else
				setOfEventIDsRaTypeDecalers.add(eventLookup.getEventTypeID(eventID));
				//setOfEventIDsRaTypeDecalers.add(new Integer(eventID));

		}
		//WE NEED TO KNOW EXACT SIZE SO WE DONT ASSIGN ANY ARG TO CONSTRUCTOR
		HashSet eventsPassedToRABySleeContainer=new HashSet();
		int[] X1_eventIDsPassedToRABySleeContainer=getX1_eventIDsPassedToRABySleeContainer();
		for(int i=0;i<X1_eventIDsPassedToRABySleeContainer.length;i++)
			if(X1_eventIDsPassedToRABySleeContainer[i]==-1)
				throw new Exception(" EVENT PASSED FROM SLEE  IS NOT REGISTERED EVENT TYPE!!!"); //THIS SHOULDNT HAPPEN !!!!
			else
				eventsPassedToRABySleeContainer.add(eventLookup.getEventTypeID(X1_eventIDsPassedToRABySleeContainer[i]));

				//eventsPassedToRABySleeContainer.add(new Integer(eventIDsPassedToRABySleeContainer[i]));
		
		if(X1_eventIDsPassedToRABySleeContainer.length!=eventsPassedToRABySleeContainer.size())
			setX1_haveEventIDPassedbySleeContainerBeenDoubled(true);  //NOT GOOD
		else
			setX1_haveEventIDPassedbySleeContainerBeenDoubled(false);
		//HAS SleeC PASSED GOOD EVENT IDS? DECALRED BY RATYPE?
		setX1_areAllPassedEventIDsInRaTypeDesc(setOfEventIDsRaTypeDecalers.containsAll(eventsPassedToRABySleeContainer));
		
		
		if(!getX1_areAllPassedEventIDsInRaTypeDesc())
		{
			//WE HAVE TO FIND THOSE THAT ARE NOT IN RATYPE
			HashSet tmp=new HashSet(eventsPassedToRABySleeContainer);
			tmp.removeAll(setOfEventIDsRaTypeDecalers);
			Iterator it=tmp.iterator();
			
			if(getX1_eventComponentKeysThatAreNotInRaDesc()==null)
			{
				setX1_eventComponentKeysThatAreNotInRaDesc(new HashSet());
				
			}
			while(it.hasNext())
			{
				EventTypeIDImpl id=(EventTypeIDImpl) it.next();
				ComponentKey wrongEventKey=id.getComponentKey();
				getX1_eventComponentKeysThatAreNotInRaDesc().add(wrongEventKey);

			}
		}
		
		//NOW WE HAVE TO CHECK IF ALL PASSED EVENTS ARE DECLARED BY SERVICE
		

		Set sbbIDs=getSvc().getSbbComponents();
		Iterator sbbIdsIterator=sbbIDs.iterator();
		Set eventTypeIDsOfServiceInterest=new HashSet();
		while(sbbIdsIterator.hasNext())
        {
        	
        	MobicentsSbbDescriptor  sbbdesc = container.getSbbManagement().getSbbComponent((SbbID)sbbIdsIterator.next());
        	if(sbbdesc==null)
        		continue;
        	
        	//EventTypeID[] eventTypeIDs = sbbdesc.getEventTypes();
        	
        	//HashMap sbbResourceOptionsForEventsIfInterest=new HashMap(20);
        	//maps EventTypeID to coresponding SbbEventEntry
        	//IT CONTAINS MAPPING FOR ALL EVENTS THAT ARE OF INTEREST IF THIS SBB
        	HashMap eventTypeIdToEventEntriesMappings=sbbdesc.getEventTypesMappings();
        	eventTypeIDsOfServiceInterest.addAll(eventTypeIdToEventEntriesMappings.keySet());
        }
		//log.info("\n================= doFirstStageTest(19) ================");
		
		setX1_areAllEventIDsPassedToRADecalredByService(eventTypeIDsOfServiceInterest.containsAll(eventsPassedToRABySleeContainer));
	}
	
	
	
public InitialEventSelector chooseService(InitialEventSelector ies) {
		
		Object event = ies.getEvent();
		
		try {
			if (event instanceof TCKResourceEventImpl) {
				TCKResourceEventImpl tckEvent = (TCKResourceEventImpl) event;
				String eventName = tckEvent.getEventTypeName();
				if (eventName.indexOf("X.X")!=-1) {
					
					TCKResourceEventX xEvent = (TCKResourceEventX) event;
					Object[] message = (Object[]) xEvent.getMessage();
	
					String serviceID = (String) message[SERVICE_NAME_POSITION]
							.toString();

					String name = serviceID.split("\\[")[1].split("\\]")[0];

					name = message[RA_TYPE_COMPONENT_KEY] + "_"
							+ message[RA_COMPONENT_KEY]+"_"+name;
					//Address address= new Address(AddressPlan.SMTP,name.hashCode()+"@alaX.pl");
					//ies.setAddress(address);
					//ies.setAddressSelected(true);
					ies.setCustomName(name);
					ies.setInitialEvent(true);
					logger.log(Level.FINEST,"\n================= NAMEX ================================\n"+name+"\n=======================================================");
					
				} else {
					TCKResourceEventY yEvent = (TCKResourceEventY) event;
					Object[] message = (Object[]) yEvent.getMessage();

					String serviceID = (String) message[SERVICE_NAME_POSITION]
							.toString();

					String name = serviceID.split("\\[")[1].split("\\]")[0];
					name =   message[1] + "_" + message[2]+"_"+name;
					
					logger.log(Level.FINEST,"\n================= NAMEY ================================\n"+name+"\n=======================================================");
					//Address address= new Address(AddressPlan.SMTP,name.hashCode()+"@alaY.pl");
					//ies.setAddress(address);
					//ies.setAddressSelected(true);
					ies.setCustomName(name);
					ies.setInitialEvent(true);
				}
			} else
				ies.setInitialEvent(false);

			logger.log(Level.FINEST,"\n----------------------------------------------------\nISE:\n"+ies+"\n-------------------------------------------------");
			return ies;
		} catch (Exception e) {
			e.printStackTrace();
			//THIS ISNT TAKEN INTO CONSIDERATION....
			ies.setInitialEvent(false);
			return ies;
		}

	}

	
	
	
}
