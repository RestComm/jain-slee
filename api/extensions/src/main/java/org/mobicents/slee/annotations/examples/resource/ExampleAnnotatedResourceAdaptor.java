package org.mobicents.slee.annotations.examples.resource;

import javax.slee.facilities.Tracer;
import javax.slee.resource.ResourceAdaptorContext;

import org.mobicents.slee.ConfigProperties;
import org.mobicents.slee.ResourceAdaptorExt;
import org.mobicents.slee.annotations.ConfigPropertiesField;
import org.mobicents.slee.annotations.ConfigProperty;
import org.mobicents.slee.annotations.EventTypeRef;
import org.mobicents.slee.annotations.LibraryRef;
import org.mobicents.slee.annotations.ProfileSpecRef;
import org.mobicents.slee.annotations.ResourceAdaptor;
import org.mobicents.slee.annotations.ResourceAdaptorContextField;
import org.mobicents.slee.annotations.ResourceAdaptorType;
import org.mobicents.slee.annotations.ResourceAdaptorTypeRef;
import org.mobicents.slee.annotations.TracerField;
import org.mobicents.slee.annotations.examples.ExampleUsageParametersInterface;
import org.mobicents.slee.annotations.examples.event.ExampleEvent;

@ResourceAdaptorType(name="ExampleAnnotatedResourceAdaptorType",vendor="javax.slee",version="1.0",
	activities={ExampleActivity.class},
	events={@EventTypeRef(name=ExampleEvent.EVENT_TYPE_NAME,vendor=ExampleEvent.EVENT_TYPE_VENDOR,version=ExampleEvent.EVENT_TYPE_VERSION)},
	aciFactory=ExampleActivityContextInterfaceFactory.class,
	sbbInterface=ExampleResourceAdaptorSbbInterface.class,
	libraryRefs={@LibraryRef(name="ExampleLibrary",vendor="javax.slee",version="1.0")})
@ResourceAdaptor(name="ExampleAnnotatedResourceAdaptor",vendor="javax.slee",version="1.0",
	raTypeRefs={@ResourceAdaptorTypeRef(name="ExampleAnnotatedResourceAdaptorType",vendor="javax.slee",version="1.0")},
	properties={@ConfigProperty(name="X",type=String.class)},
	usageParametersInterface=ExampleUsageParametersInterface.class,
	profileSpecRefs={@ProfileSpecRef(name="ExampleProfileSpec",vendor="javax.slee",version="1.0",alias="profileSpec")},
	securityPermissions="...")
public class ExampleAnnotatedResourceAdaptor extends ResourceAdaptorExt {

	@TracerField
	private Tracer tracer;
	
	@ConfigPropertiesField
	private ConfigProperties configProperties;
	
	@ResourceAdaptorContextField
	private ResourceAdaptorContext resourceAdaptorContext;
	
	// RA logic ...
	
}
