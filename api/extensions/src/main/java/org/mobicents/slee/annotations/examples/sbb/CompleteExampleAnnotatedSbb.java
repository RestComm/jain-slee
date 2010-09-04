package org.mobicents.slee.annotations.examples.sbb;

import javax.slee.*;
import javax.slee.facilities.*;
import javax.slee.profile.*;
import javax.slee.serviceactivity.*;
import javax.slee.usage.*;

import org.mobicents.slee.*;
import org.mobicents.slee.annotations.*;
import org.mobicents.slee.annotations.Sbb;
import org.mobicents.slee.annotations.examples.*;
import org.mobicents.slee.annotations.examples.event.*;
import org.mobicents.slee.annotations.examples.resource.*;

@Service(name="CompleteExampleService",vendor="javax.slee",version="1.0",rootSbb=CompleteExampleAnnotatedSbb.class)
@Reentrant
@Sbb(name="CompleteExampleSbb",vendor="javax.slee",version="1.0",
	localInterface=ExampleSbbLocalObject.class,
	activityContextInterface=ExampleSbbActivityContextInterface.class,
	sbbRefs={@SbbRef(name="ChildSbb",vendor="javax.slee",version="1.0",alias="childSbb")},
	libraryRefs={@LibraryRef(name="Library",vendor="javax.slee",version="1.0")},
	profileSpecRefs={@ProfileSpecRef(name="ProfileSpec",vendor="javax.slee",version="1.0",alias="profileSpec")},
	properties={@ConfigProperty(name="X",type=String.class)},
	securityPermissions="..."		
)
public abstract class CompleteExampleAnnotatedSbb implements SbbExt {
	
	@TracerField
	private Tracer tracer;
	
	@ConfigPropertiesField
	private ConfigProperties configProperties;
	
	@SbbContextExtField
	private SbbContextExt sbbContextExt;
	
	@SbbResourceAdaptorInterface(
		raType=@ResourceAdaptorTypeRef(name="ExampleRAType",vendor="javax.slee",version="1.0"),
		raEntityLink="exampleRA")
	private ExampleResourceAdaptorSbbInterface raSbbInterface;
	
	@SbbActivityContextFactory(@ResourceAdaptorTypeRef(name="ExampleRAType",vendor="javax.slee",version="1.0"))
	private ExampleActivityContextInterfaceFactory activityContextInterfaceFactory;
	
	@CMPField
	public abstract String getCMPField();
	public abstract void setCMPField(String value);
	
	@CMPField
	private String anotherCMPField;
	
	@GetChildRelation(sbbAliasRef="childSbb")
	public abstract ChildRelation getChildRelation();
	
	@GetProfileCMP(profileSpecRef="profileSpec")
	public abstract ExampleProfileSpecCMPInterface getExampleProfile(
		ProfileID profileID);

	@UsageParametersInterface
	public abstract ExampleUsageParametersInterface getDefaultSbbUsageParameterSet();
	public abstract ExampleUsageParametersInterface getSbbUsageParameterSet(
			String name) throws UnrecognizedUsageParameterSetNameException;

	@ServiceStartedEventHandler
	public void onServiceStartedEvent(ServiceStartedEvent event,
			ActivityContextInterface aci) {
		// ...
	}
	
	@TimerEventHandler
	public void onTimerEvent(TimerEvent event,
			ActivityContextInterface aci) {
		// ...
	}
	
	@InitialEventSelectorMethod({
		@EventTypeRef(name=ExampleEvent.EVENT_TYPE_NAME,
				vendor=ExampleEvent.EVENT_TYPE_VENDOR,
				version=ExampleEvent.EVENT_TYPE_VERSION)})
	public InitialEventSelector ies(InitialEventSelector ies) {
		return ies;
	}
	
	@EventHandler(eventType=
		@EventTypeRef(name=ExampleEvent.EVENT_TYPE_NAME,
				vendor=ExampleEvent.EVENT_TYPE_VENDOR,
				version=ExampleEvent.EVENT_TYPE_VERSION))
	public void onExampleEvent(ExampleEvent event,
			ActivityContextInterface aci) {
		// ...
	}
	
	@EventFiring(@EventTypeRef(name=ExampleEvent.EVENT_TYPE_NAME,
		vendor=ExampleEvent.EVENT_TYPE_VENDOR,
		version=ExampleEvent.EVENT_TYPE_VERSION))
	public abstract void fireExampleEvent(ExampleEvent event,
			ActivityContextInterface aci, Address address);
	
}
